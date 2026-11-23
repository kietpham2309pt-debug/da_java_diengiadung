package vn.edu.huit.diengiadung.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import vn.edu.huit.diengiadung.dto.GioHang;
import vn.edu.huit.diengiadung.dto.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.LoaiSanPhamRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ThuDongThoi {

    private static final Logger log = LoggerFactory.getLogger(ThuDongThoi.class);
    private static final long GIA_THU = 1_000_000L;

    public record DongThoiGian(int stt, String luong, long batDauMs, long ketThucMs, int tonDocDuoc, boolean thanhCong, String ghiChu) {
    }

    public record KetQua(boolean coKhoa, int soKhach, int tonBanDau, int thanhCong, int hetHang, int tonCuoi,
                         long tongMs, List<DongThoiGian> dongThoiGian) {

        public boolean dung() {
            return thanhCong <= tonBanDau && tonCuoi == tonBanDau - thanhCong && tonCuoi >= 0;
        }

        public int banVuot() {
            return Math.max(0, thanhCong - tonBanDau);
        }
    }

    private final DatHangService datHangService;
    private final SanPhamRepository sanPhamRepository;
    private final DonHangRepository donHangRepository;
    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final DiaChiHanhChinh diaChiHanhChinh;
    private final TransactionTemplate giaoDich;

    public ThuDongThoi(DatHangService datHangService, SanPhamRepository sanPhamRepository, DonHangRepository donHangRepository,
                       LoaiSanPhamRepository loaiSanPhamRepository, ThuongHieuRepository thuongHieuRepository,
                       DiaChiHanhChinh diaChiHanhChinh, PlatformTransactionManager quanLyGiaoDich) {
        this.datHangService = datHangService;
        this.sanPhamRepository = sanPhamRepository;
        this.donHangRepository = donHangRepository;
        this.loaiSanPhamRepository = loaiSanPhamRepository;
        this.thuongHieuRepository = thuongHieuRepository;
        this.diaChiHanhChinh = diaChiHanhChinh;
        this.giaoDich = new TransactionTemplate(quanLyGiaoDich);
    }

    public synchronized KetQua chay(int soKhach, int tonBanDau, boolean coKhoa) throws InterruptedException {
        if (soKhach < 2 || soKhach > 30 || tonBanDau < 1 || tonBanDau > 10) {
            throw new LoiNghiepVu("Số khách từ 2 đến 30, tồn ban đầu từ 1 đến 10");
        }
        SanPham sp = taoSanPhamThu(tonBanDau);
        List<Long> donDaTao = new ArrayList<>();
        try {
            return chayVoiSanPham(sp.getId(), soKhach, tonBanDau, coKhoa, donDaTao);
        } finally {
            donHangRepository.deleteAllById(donDaTao);
            sanPhamRepository.deleteById(sp.getId());
        }
    }

    private KetQua chayVoiSanPham(Long idSanPham, int soKhach, int tonBanDau, boolean coKhoa, List<Long> donDaTao)
            throws InterruptedException {
        CountDownLatch cungXuatPhat = new CountDownLatch(1);
        AtomicInteger dem = new AtomicInteger();
        ExecutorService nhom = Executors.newFixedThreadPool(soKhach, viec -> {
            Thread luong = new Thread(viec, "khach-" + dem.incrementAndGet());
            luong.setDaemon(true);
            return luong;
        });
        ConcurrentLinkedQueue<DongThoiGian> ketQua = new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<Long> donMoi = new ConcurrentLinkedQueue<>();
        long[] goc = new long[1];
        for (int i = 1; i <= soKhach; i++) {
            int stt = i;
            nhom.execute(() -> {
                try {
                    cungXuatPhat.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                long batDau = System.currentTimeMillis() - goc[0];
                int[] tonDoc = {-1};
                boolean ok;
                String ghiChu;
                try {
                    if (coKhoa) {
                        GioHang gio = new GioHang();
                        gio.datSoLuong(idSanPham, 1);
                        DonHang don = datHangService.datHang(null, gio, thongTin(stt));
                        donMoi.add(don.getId());
                        ghiChu = "Khoá dòng, trừ tồn, tạo đơn " + don.getMaDon();
                    } else {
                        giaoDich.executeWithoutResult(trangThai -> muaKhongKhoa(idSanPham, tonDoc));
                        ghiChu = "Đọc thấy còn " + tonDoc[0] + ", ghi tồn " + (tonDoc[0] - 1);
                    }
                    ok = true;
                } catch (LoiNghiepVu loi) {
                    ok = false;
                    ghiChu = loi.getMessage();
                } catch (RuntimeException loi) {
                    ok = false;
                    ghiChu = "Lỗi: " + loi.getClass().getSimpleName();
                }
                long ketThuc = System.currentTimeMillis() - goc[0];
                ketQua.add(new DongThoiGian(stt, Thread.currentThread().getName(), batDau, ketThuc, tonDoc[0], ok, ghiChu));
            });
        }
        goc[0] = System.currentTimeMillis();
        cungXuatPhat.countDown();
        nhom.shutdown();
        if (!nhom.awaitTermination(60, TimeUnit.SECONDS)) {
            nhom.shutdownNow();
        }
        long tong = System.currentTimeMillis() - goc[0];
        donDaTao.addAll(donMoi);

        List<DongThoiGian> ds = new ArrayList<>(ketQua);
        ds.sort(Comparator.comparingLong(DongThoiGian::ketThucMs));
        int thanhCong = (int) ds.stream().filter(DongThoiGian::thanhCong).count();
        Integer tonDocLai = sanPhamRepository.docTonKho(idSanPham);
        int tonCuoi = tonDocLai == null ? 0 : tonDocLai;
        log.info("Thử đồng thời {}: {} khách tranh {} sản phẩm, thành công {}, tồn cuối {}, {} ms",
                coKhoa ? "CÓ khoá dòng" : "KHÔNG khoá", soKhach, tonBanDau, thanhCong, tonCuoi, tong);
        return new KetQua(coKhoa, soKhach, tonBanDau, thanhCong, soKhach - thanhCong, tonCuoi, tong, ds);
    }

    private void muaKhongKhoa(Long idSanPham, int[] tonDoc) {
        SanPham sp = sanPhamRepository.findById(idSanPham).orElseThrow();
        tonDoc[0] = sp.getSoLuongTon();
        if (sp.getSoLuongTon() < 1) {
            throw new LoiNghiepVu("Đọc thấy hết hàng");
        }
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sp.setSoLuongTon(sp.getSoLuongTon() - 1);
        sanPhamRepository.save(sp);
    }

    private SanPham taoSanPhamThu(int tonBanDau) {
        SanPham sp = new SanPham();
        sp.setMaSanPham("THU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sp.setTenSanPham("Sản phẩm thử đồng thời (tự xoá)");
        sp.setGiaBan(GIA_THU);
        sp.setSoLuongTon(tonBanDau);
        sp.setBaoHanhThang(12);
        sp.setDangBan(true);
        sp.setLoaiSanPham(loaiSanPhamRepository.findAll().get(0));
        sp.setThuongHieu(thuongHieuRepository.findAll().get(0));
        return sanPhamRepository.save(sp);
    }

    private ThongTinGiaoHang thongTin(int stt) {
        String tinh = diaChiHanhChinh.danhSachTinhThanh().get(0);
        ThongTinGiaoHang tt = new ThongTinGiaoHang();
        tt.setTenNguoiNhan("Khách thử " + stt);
        tt.setSoDienThoaiNhan("0909000111");
        tt.setTinhThanh(tinh);
        tt.setPhuongXa(diaChiHanhChinh.danhSachPhuongXa(tinh).get(0));
        tt.setDiaChiCuThe("1 Đường thử đồng thời");
        tt.setPhuongThuc(DonHang.THANH_TOAN_KHI_NHAN);
        tt.setDongY(true);
        return tt;
    }
}
