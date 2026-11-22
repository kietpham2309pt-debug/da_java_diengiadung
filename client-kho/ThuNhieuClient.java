import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class ThuNhieuClient {

    private static final PrintStream OUT = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

    public static void main(String[] args) throws Exception {
        String may = args.length > 0 ? args[0] : "localhost";
        int cong = args.length > 1 ? Integer.parseInt(args[1]) : 9090;
        int soClient = args.length > 2 ? Integer.parseInt(args[2]) : 5;
        int soLanMoiClient = args.length > 3 ? Integer.parseInt(args[3]) : 4;
        String maSanPham = args.length > 4 ? args[4] : "BDT-BOS-001";
        String tenDangNhap = args.length > 5 ? args[5] : "admin";
        String matKhau = args.length > 6 ? args[6] : "admin123";

        int tonTruoc = docTon(may, cong, maSanPham);
        OUT.printf("Tồn kho %s trước khi chạy: %d%n", maSanPham, tonTruoc);
        OUT.printf("Mở %d client chạy cùng lúc, mỗi client nhập %d lần, mỗi lần 1 sản phẩm...%n", soClient, soLanMoiClient);

        CountDownLatch cungXuatPhat = new CountDownLatch(1);
        ExecutorService nhom = Executors.newFixedThreadPool(soClient);
        List<Future<Integer>> ketQua = new ArrayList<>();
        for (int i = 1; i <= soClient; i++) {
            int stt = i;
            ketQua.add(nhom.submit(() -> {
                int thanhCong = 0;
                try (Socket s = moKetNoi(may, cong);
                     BufferedReader doc = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
                     BufferedWriter ghi = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {
                    List<String> dn = gui(doc, ghi, "DANGNHAP|" + tenDangNhap + "|" + matKhau);
                    if (!dn.get(0).startsWith("OK")) {
                        OUT.printf("Client %d đăng nhập lỗi: %s%n", stt, dn.get(0));
                        return 0;
                    }
                    cungXuatPhat.await();
                    for (int k = 0; k < soLanMoiClient; k++) {
                        List<String> tl = gui(doc, ghi, "NHAPKHO|" + maSanPham + "|1|1000000|1");
                        if (tl.get(0).startsWith("OK")) {
                            thanhCong++;
                            OUT.printf("  client %d lần %d -> phiếu %s%n", stt, k + 1, tl.get(1));
                        } else {
                            OUT.printf("  client %d lần %d -> %s%n", stt, k + 1, tl.get(0));
                        }
                    }
                    gui(doc, ghi, "THOAT");
                }
                return thanhCong;
            }));
        }
        Thread.sleep(500);
        cungXuatPhat.countDown();
        int tongThanhCong = 0;
        for (Future<Integer> f : ketQua) {
            tongThanhCong += f.get();
        }
        nhom.shutdown();
        nhom.awaitTermination(10, TimeUnit.SECONDS);

        int tonSau = docTon(may, cong, maSanPham);
        int mongDoi = tonTruoc + tongThanhCong;
        OUT.printf("Số lần nhập thành công: %d / %d%n", tongThanhCong, soClient * soLanMoiClient);
        OUT.printf("Tồn kho sau khi chạy: %d (mong đợi %d) -> %s%n", tonSau, mongDoi,
                tonSau == mongDoi ? "ĐÚNG, không mất cập nhật" : "SAI, có cập nhật bị ghi đè");
        if (tonSau != mongDoi) {
            System.exit(1);
        }
    }

    private static int docTon(String may, int cong, String ma) throws IOException {
        try (Socket s = moKetNoi(may, cong);
             BufferedReader doc = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter ghi = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {
            List<String> tl = gui(doc, ghi, "TONKHO|" + ma);
            if (!tl.get(0).startsWith("OK")) {
                throw new IOException(tl.get(0));
            }
            gui(doc, ghi, "THOAT");
            return Integer.parseInt(tl.get(1).split("\\|")[2]);
        }
    }

    private static Socket moKetNoi(String may, int cong) throws IOException {
        Socket s = new Socket();
        s.connect(new InetSocketAddress(may, cong), 5000);
        s.setSoTimeout(30000);
        return s;
    }

    private static List<String> gui(BufferedReader doc, BufferedWriter ghi, String lenh) throws IOException {
        ghi.write(lenh);
        ghi.newLine();
        ghi.flush();
        String dau = doc.readLine();
        if (dau == null) {
            throw new IOException("Máy chủ đã đóng kết nối");
        }
        List<String> ra = new ArrayList<>();
        ra.add(dau);
        if (dau.startsWith("OK|")) {
            int n = Integer.parseInt(dau.substring(3).trim());
            for (int i = 0; i < n; i++) {
                ra.add(doc.readLine());
            }
        }
        return ra;
    }
}
