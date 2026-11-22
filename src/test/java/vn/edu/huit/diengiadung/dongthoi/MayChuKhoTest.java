package vn.edu.huit.diengiadung.dongthoi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.mang.MayChuKho;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MayChuKhoTest extends CoSoKiemThuSqlServer {

    @Autowired
    private MayChuKho mayChuKho;

    @Test
    void baClientCungNhapKhoThiTonKhoCongDu() throws Exception {
        SanPham sp = taoSanPham(10, 3_000_000L);
        int soClient = 3;
        int soLan = 5;
        CountDownLatch daDangNhap = new CountDownLatch(soClient);
        CountDownLatch choPhepNhap = new CountDownLatch(1);
        AtomicInteger dongThoiCaoNhat = new AtomicInteger();
        ExecutorService nhom = Executors.newFixedThreadPool(soClient);
        List<Future<Integer>> ketQua = new ArrayList<>();
        for (int i = 0; i < soClient; i++) {
            ketQua.add(nhom.submit(() -> {
                try (KetNoiThu kn = new KetNoiThu(mayChuKho.getCong())) {
                    assertTrue(kn.gui("DANGNHAP|admin|admin123").get(0).startsWith("OK"));
                    daDangNhap.countDown();
                    choPhepNhap.await();
                    int ok = 0;
                    for (int k = 0; k < soLan; k++) {
                        if (kn.gui("NHAPKHO|" + sp.getMaSanPham() + "|1|2000000|1").get(0).startsWith("OK")) {
                            ok++;
                        }
                    }
                    kn.gui("THOAT");
                    return ok;
                }
            }));
        }
        assertTrue(daDangNhap.await(30, TimeUnit.SECONDS));
        dongThoiCaoNhat.set(mayChuKho.getDangPhucVu());
        choPhepNhap.countDown();
        int tong = 0;
        for (Future<Integer> f : ketQua) {
            tong += f.get(60, TimeUnit.SECONDS);
        }
        nhom.shutdown();

        System.out.printf("Máy chủ kho: %d client cùng kết nối, %d lần nhập thành công, tồn kho %d -> %d%n",
                dongThoiCaoNhat.get(), tong, 10, tonKho(sp.getId()));
        assertEquals(soClient, dongThoiCaoNhat.get());
        assertEquals(soClient * soLan, tong);
        assertEquals(10 + soClient * soLan, tonKho(sp.getId()));
    }

    @Test
    void lenhSaiVaChuaDangNhapDuocBaoLoi() throws Exception {
        try (KetNoiThu kn = new KetNoiThu(mayChuKho.getCong())) {
            assertTrue(kn.gui("XINCHAO").get(0).startsWith("OK|1"));
            assertTrue(kn.gui("LENHLA").get(0).startsWith("LOI|"));
            assertTrue(kn.gui("NHAPKHO|BDT-BOS-001|1|1000|1").get(0).startsWith("LOI|"));
            assertTrue(kn.gui("DANGNHAP|admin|saimatkhau").get(0).startsWith("LOI|"));
            List<String> tim = kn.gui("TONKHO|BDT-BOS-001");
            assertTrue(tim.get(0).startsWith("OK|1"));
            assertTrue(tim.get(1).startsWith("BDT-BOS-001|"));
        }
    }

    static class KetNoiThu implements AutoCloseable {
        private final Socket s;
        private final BufferedReader doc;
        private final BufferedWriter ghi;

        KetNoiThu(int cong) throws IOException {
            s = new Socket("localhost", cong);
            s.setSoTimeout(30000);
            doc = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
            ghi = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8));
        }

        List<String> gui(String lenh) throws IOException {
            ghi.write(lenh);
            ghi.newLine();
            ghi.flush();
            List<String> ra = new ArrayList<>();
            String dau = doc.readLine();
            ra.add(dau);
            if (dau != null && dau.startsWith("OK|")) {
                int n = Integer.parseInt(dau.substring(3));
                for (int i = 0; i < n; i++) {
                    ra.add(doc.readLine());
                }
            }
            return ra;
        }

        @Override
        public void close() throws IOException {
            s.close();
        }
    }
}
