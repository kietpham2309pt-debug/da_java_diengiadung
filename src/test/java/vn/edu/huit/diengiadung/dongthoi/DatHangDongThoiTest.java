package vn.edu.huit.diengiadung.dongthoi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vn.edu.huit.diengiadung.dto.GioHang;
import vn.edu.huit.diengiadung.dto.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.DiaChiHanhChinh;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.TuHuyDonQuaHan;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DatHangDongThoiTest extends CoSoKiemThuSqlServer {

    @Autowired
    private DatHangService datHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private DiaChiHanhChinh diaChiHanhChinh;

    @Autowired
    private TuHuyDonQuaHan tuHuyDonQuaHan;

    @Test
    void haiMuoiKhachCungMuaNamSanPhamCuoiThiChiNamDonThanhCong() throws Exception {
        int tonBanDau = 5;
        int soKhach = 20;
        SanPham sp = taoSanPham(tonBanDau, 2_000_000L);

        CountDownLatch cungXuatPhat = new CountDownLatch(1);
        ExecutorService nhom = Executors.newFixedThreadPool(soKhach);
        AtomicInteger thanhCong = new AtomicInteger();
        AtomicInteger hetHang = new AtomicInteger();
        ConcurrentLinkedQueue<String> luongDaChay = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Throwable> loiKhac = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < soKhach; i++) {
            int stt = i;
            nhom.execute(() -> {
                try {
                    cungXuatPhat.await();
                    GioHang gio = new GioHang();
                    gio.datSoLuong(sp.getId(), 1);
                    datHangService.datHang(null, gio, thongTin("Khách " + stt));
                    luongDaChay.add(Thread.currentThread().getName());
                    thanhCong.incrementAndGet();
                } catch (LoiNghiepVu e) {
                    hetHang.incrementAndGet();
                } catch (Throwable e) {
                    loiKhac.add(e);
                }
            });
        }
        cungXuatPhat.countDown();
        nhom.shutdown();
        assertTrue(nhom.awaitTermination(120, TimeUnit.SECONDS));

        System.out.printf("Đặt hàng đồng thời: %d luồng, thành công %d, hết hàng %d, tồn còn %d, luồng thành công %s%n",
                soKhach, thanhCong.get(), hetHang.get(), tonKho(sp.getId()), luongDaChay);
        assertTrue(loiKhac.isEmpty(), () -> "Có lỗi ngoài dự kiến: " + loiKhac);
        assertEquals(tonBanDau, thanhCong.get());
        assertEquals(soKhach - tonBanDau, hetHang.get());
        assertEquals(0, tonKho(sp.getId()));
        assertEquals(tonBanDau, sanPhamRepository.demSoDongDonHang(sp.getId()));
    }

    @Test
    void tacVuDinhKyHuyDonChuyenKhoanQuaHanVaHoanTonKho() {
        SanPham sp = taoSanPham(3, 1_500_000L);
        GioHang gio = new GioHang();
        gio.datSoLuong(sp.getId(), 2);
        ThongTinGiaoHang tt = thongTin("Khách chuyển khoản");
        tt.setPhuongThuc(DonHang.THANH_TOAN_TRUOC);
        DonHang don = datHangService.datHang(null, gio, tt);
        assertEquals(1, tonKho(sp.getId()));

        don.setNgayDat(LocalDateTime.now().minusHours(30));
        donHangRepository.save(don);

        int daHuy = tuHuyDonQuaHan.huyDonQuaHan(LocalDateTime.now().minusHours(24));

        DonHang sau = donHangRepository.findById(don.getId()).orElseThrow();
        assertTrue(daHuy >= 1);
        assertEquals(DonHang.DA_HUY, sau.getTrangThai());
        assertEquals("HE_THONG", sau.getHuyBoi());
        assertEquals(3, tonKho(sp.getId()));
    }

    private ThongTinGiaoHang thongTin(String ten) {
        List<String> tinh = new ArrayList<>(diaChiHanhChinh.danhSachTinhThanh());
        String tinhThanh = tinh.get(0);
        ThongTinGiaoHang tt = new ThongTinGiaoHang();
        tt.setTenNguoiNhan(ten);
        tt.setSoDienThoaiNhan("0909000111");
        tt.setTinhThanh(tinhThanh);
        tt.setPhuongXa(diaChiHanhChinh.danhSachPhuongXa(tinhThanh).get(0));
        tt.setDiaChiCuThe("1 Đường kiểm thử");
        tt.setPhuongThuc(DonHang.THANH_TOAN_KHI_NHAN);
        tt.setDongY(true);
        return tt;
    }
}
