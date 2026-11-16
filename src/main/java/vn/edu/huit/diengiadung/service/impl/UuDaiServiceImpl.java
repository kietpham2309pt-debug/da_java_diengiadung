package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.KhuyenMai;
import vn.edu.huit.diengiadung.entity.MaGiamGia;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.model.BangGia;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.KhuyenMaiRepository;
import vn.edu.huit.diengiadung.repository.MaGiamGiaRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.UuDaiService;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class UuDaiServiceImpl implements UuDaiService {

    private static final String KY_TU_MA = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private final MaGiamGiaRepository duLieuMa;
    private final KhuyenMaiRepository duLieuKhuyenMai;
    private final DonHangRepository duLieuDonHang;
    private final SecureRandom ngauNhien = new SecureRandom();

    public UuDaiServiceImpl(MaGiamGiaRepository duLieuMa, KhuyenMaiRepository duLieuKhuyenMai,
                            DonHangRepository duLieuDonHang) {
        this.duLieuMa = duLieuMa;
        this.duLieuKhuyenMai = duLieuKhuyenMai;
        this.duLieuDonHang = duLieuDonHang;
    }

    @Override
    @Transactional(readOnly = true)
    public BangGia tinhGia(Map<SanPham, Long> thanhTienTheoSanPham, KhachHang khachHang, String ma) {
        long tamTinh = thanhTienTheoSanPham.values().stream().mapToLong(Long::longValue).sum();
        boolean thanhVien = khachHang != null;
        LocalDateTime bayGio = LocalDateTime.now();

        KhuyenMai khuyenMaiTotNhat = null;
        long tienKhuyenMai = 0;
        for (KhuyenMai km : duLieuKhuyenMai.findByHoatDongTrue()) {
            if (!km.dangTrongThoiGian(bayGio) || (km.isChiThanhVien() && !thanhVien)) {
                continue;
            }
            long giaTriApDung = thanhTienTheoSanPham.entrySet().stream()
                    .filter(dong -> km.apDungCho(dong.getKey()))
                    .mapToLong(Map.Entry::getValue)
                    .sum();
            if (giaTriApDung == 0 || giaTriApDung < km.getDonToiThieu()) {
                continue;
            }
            long tien = km.tinhTienGiam(giaTriApDung);
            if (tien > tienKhuyenMai) {
                tienKhuyenMai = tien;
                khuyenMaiTotNhat = km;
            }
        }

        if (ma == null || ma.isBlank()) {
            return new BangGia(tamTinh, khuyenMaiTotNhat, tienKhuyenMai, null, 0, null);
        }
        if (!thanhVien) {
            return new BangGia(tamTinh, khuyenMaiTotNhat, tienKhuyenMai, null, 0,
                    "Vui lòng đăng nhập tài khoản thành viên để dùng mã giảm giá");
        }
        MaGiamGia maGiamGia = duLieuMa.findByMaIgnoreCase(ma.trim()).orElse(null);
        String loi = kiemTraMa(maGiamGia, khachHang, tamTinh, bayGio);
        if (loi != null) {
            return new BangGia(tamTinh, khuyenMaiTotNhat, tienKhuyenMai, null, 0, loi);
        }
        long tienGiamMa = maGiamGia.tinhTienGiam(tamTinh - tienKhuyenMai);
        return new BangGia(tamTinh, khuyenMaiTotNhat, tienKhuyenMai, maGiamGia, tienGiamMa, null);
    }

    private String kiemTraMa(MaGiamGia ma, KhachHang khachHang, long tamTinh, LocalDateTime bayGio) {
        if (ma == null || !ma.isHoatDong()) {
            return "Mã giảm giá không tồn tại hoặc đã ngừng áp dụng";
        }
        if (bayGio.isBefore(ma.getBatDau())) {
            return "Mã giảm giá chưa đến thời gian áp dụng";
        }
        if (!ma.dangTrongThoiGian(bayGio)) {
            return "Mã giảm giá đã hết hạn";
        }
        if (ma.isHetLuot()) {
            return "Mã giảm giá đã hết lượt sử dụng";
        }
        if (tamTinh < ma.getDonToiThieu()) {
            return "Mã này chỉ áp dụng cho " + ma.getMoTaDieuKien().toLowerCase();
        }
        if (ma.isMoiKhachMotLan() && duLieuDonHang.countByKhachHangIdAndMaGiamGiaIgnoreCaseAndTrangThaiNot(
                khachHang.getId(), ma.getMa(), DonHang.DA_HUY) > 0) {
            return "Bạn đã dùng mã này cho một đơn hàng trước đó";
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaGiamGia> maDangMoChoKhach() {
        LocalDateTime bayGio = LocalDateTime.now();
        return duLieuMa.findByHoatDongTrueAndHienChoKhachTrueOrderByGiaTriDesc().stream()
                .filter(ma -> ma.dangTrongThoiGian(bayGio) && !ma.isHetLuot())
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhuyenMai> khuyenMaiDangChay(boolean chiThanhVien) {
        LocalDateTime bayGio = LocalDateTime.now();
        return duLieuKhuyenMai.findByHoatDongTrue().stream()
                .filter(km -> km.dangTrongThoiGian(bayGio) && (!chiThanhVien || km.isChiThanhVien()))
                .sorted(Comparator.comparing(KhuyenMai::getBatDau).reversed())
                .toList();
    }

    @Override
    @Transactional
    public void ghiNhanDungMa(String ma) {
        MaGiamGia maGiamGia = duLieuMa.khoaTheoMa(ma)
                .orElseThrow(() -> new LoiNghiepVu("Mã giảm giá không tồn tại"));
        if (maGiamGia.isHetLuot()) {
            throw new LoiNghiepVu("Mã giảm giá vừa hết lượt sử dụng, vui lòng bỏ mã và đặt lại");
        }
        maGiamGia.setSoLuotDaDung(maGiamGia.getSoLuotDaDung() + 1);
        duLieuMa.save(maGiamGia);
    }

    @Override
    @Transactional
    public void hoanLuotDungMa(String ma) {
        if (ma == null) {
            return;
        }
        duLieuMa.khoaTheoMa(ma).ifPresent(maGiamGia -> {
            maGiamGia.setSoLuotDaDung(Math.max(0, maGiamGia.getSoLuotDaDung() - 1));
            duLieuMa.save(maGiamGia);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<MaGiamGia> timMaGiamGia(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return duLieuMa.findAllByOrderByNgayTaoDesc();
        }
        return duLieuMa.findByMaContainingIgnoreCaseOrMoTaContainingIgnoreCaseOrderByNgayTaoDesc(tuKhoa.trim(), tuKhoa.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public MaGiamGia layMaGiamGia(Long id) {
        return duLieuMa.findById(id).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy mã giảm giá"));
    }

    @Override
    @Transactional
    public MaGiamGia luuMaGiamGia(MaGiamGia maGiamGia) {
        boolean trung = maGiamGia.getId() == null
                ? duLieuMa.existsByMaIgnoreCase(maGiamGia.getMa())
                : duLieuMa.existsByMaIgnoreCaseAndIdNot(maGiamGia.getMa(), maGiamGia.getId());
        if (trung) {
            throw new LoiNghiepVu("Mã " + maGiamGia.getMa() + " đã tồn tại, vui lòng chọn mã khác");
        }
        if (MaGiamGia.SO_TIEN.equals(maGiamGia.getLoaiGiam())) {
            maGiamGia.setGiamToiDa(null);
        }
        if (maGiamGia.getId() != null) {
            MaGiamGia cu = layMaGiamGia(maGiamGia.getId());
            maGiamGia.setSoLuotDaDung(cu.getSoLuotDaDung());
            maGiamGia.setNgayTao(cu.getNgayTao());
        } else {
            maGiamGia.setSoLuotDaDung(0);
            maGiamGia.setNgayTao(LocalDateTime.now());
        }
        return duLieuMa.save(maGiamGia);
    }

    @Override
    @Transactional
    public void doiTrangThaiMa(Long id) {
        MaGiamGia ma = layMaGiamGia(id);
        ma.setHoatDong(!ma.isHoatDong());
        duLieuMa.save(ma);
    }

    @Override
    @Transactional
    public void xoaMaGiamGia(Long id) {
        MaGiamGia ma = layMaGiamGia(id);
        if (duLieuDonHang.countByMaGiamGiaIgnoreCase(ma.getMa()) > 0) {
            throw new LoiNghiepVu("Mã " + ma.getMa() + " đã được dùng trong đơn hàng nên không thể xoá. Hãy tạm dừng mã thay vì xoá.");
        }
        duLieuMa.delete(ma);
    }

    @Override
    public String taoMaNgauNhien() {
        String ma;
        do {
            StringBuilder chuoi = new StringBuilder("BN");
            for (int i = 0; i < 8; i++) {
                chuoi.append(KY_TU_MA.charAt(ngauNhien.nextInt(KY_TU_MA.length())));
            }
            ma = chuoi.toString();
        } while (duLieuMa.existsByMaIgnoreCase(ma));
        return ma;
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhuyenMai> timKhuyenMai(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return duLieuKhuyenMai.findAllByOrderByBatDauDesc();
        }
        return duLieuKhuyenMai.findByTenContainingIgnoreCaseOrderByBatDauDesc(tuKhoa.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public KhuyenMai layKhuyenMai(Long id) {
        return duLieuKhuyenMai.findById(id).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy chương trình khuyến mãi"));
    }

    @Override
    @Transactional
    public KhuyenMai luuKhuyenMai(KhuyenMai khuyenMai) {
        if (KhuyenMai.SO_TIEN.equals(khuyenMai.getLoaiGiam())) {
            khuyenMai.setGiamToiDa(null);
        }
        if (!KhuyenMai.THEO_LOAI.equals(khuyenMai.getPhamVi())) {
            khuyenMai.setLoaiSanPham(null);
        }
        if (!KhuyenMai.THEO_HANG.equals(khuyenMai.getPhamVi())) {
            khuyenMai.setThuongHieu(null);
        }
        return duLieuKhuyenMai.save(khuyenMai);
    }

    @Override
    @Transactional
    public void doiTrangThaiKhuyenMai(Long id) {
        KhuyenMai km = layKhuyenMai(id);
        km.setHoatDong(!km.isHoatDong());
        duLieuKhuyenMai.save(km);
    }

    @Override
    @Transactional
    public void xoaKhuyenMai(Long id) {
        duLieuKhuyenMai.delete(layKhuyenMai(id));
    }
}
