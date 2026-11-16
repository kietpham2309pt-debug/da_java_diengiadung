package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.BangGia;
import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.DongGioHang;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.model.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.CauHinhThanhToan;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.DiaChiHanhChinh;
import vn.edu.huit.diengiadung.service.DonHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.LyDoHuyDon;
import vn.edu.huit.diengiadung.service.UuDaiService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DatHangServiceImpl implements DatHangService {

    private final DonHangRepository duLieuDonHang;
    private final KhachHangRepository duLieuKhachHang;
    private final SanPhamRepository duLieuSanPham;
    private final DonHangService dichVuDonHang;
    private final DiaChiHanhChinh diaChiHanhChinh;
    private final CauHinhThanhToan cauHinhThanhToan;
    private final UuDaiService uuDaiService;

    public DatHangServiceImpl(DonHangRepository duLieuDonHang,
                              KhachHangRepository duLieuKhachHang,
                              SanPhamRepository duLieuSanPham,
                              DonHangService dichVuDonHang,
                              DiaChiHanhChinh diaChiHanhChinh,
                              CauHinhThanhToan cauHinhThanhToan,
                              UuDaiService uuDaiService) {
        this.duLieuDonHang = duLieuDonHang;
        this.duLieuKhachHang = duLieuKhachHang;
        this.duLieuSanPham = duLieuSanPham;
        this.dichVuDonHang = dichVuDonHang;
        this.diaChiHanhChinh = diaChiHanhChinh;
        this.cauHinhThanhToan = cauHinhThanhToan;
        this.uuDaiService = uuDaiService;
    }

    @Override
    @Transactional(readOnly = true)
    public KhachHang timKhachHang(TaiKhoan taiKhoan) {
        if (taiKhoan == null) {
            return null;
        }
        return duLieuKhachHang.findByTaiKhoan(taiKhoan).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public ThongTinGiaoHang layThongTinGiaoHangMacDinh(TaiKhoan taiKhoan) {
        ThongTinGiaoHang thongTin = new ThongTinGiaoHang();
        KhachHang khachHang = timKhachHang(taiKhoan);
        if (khachHang == null) {
            return thongTin;
        }
        thongTin.setTenNguoiNhan(khachHang.getHoTen());
        thongTin.setSoDienThoaiNhan(khachHang.getSoDienThoai());
        thongTin.setEmail(khachHang.getEmail());
        duLieuDonHang.findByKhachHangIdOrderByNgayDatDesc(khachHang.getId()).stream()
                .filter(donCu -> diaChiHanhChinh.hopLe(donCu.getTinhThanh(), donCu.getPhuongXa()))
                .findFirst()
                .ifPresent(donCu -> {
                    thongTin.setTinhThanh(donCu.getTinhThanh());
                    thongTin.setPhuongXa(donCu.getPhuongXa());
                    thongTin.setDiaChiCuThe(donCu.getDiaChiCuThe());
                });
        return thongTin;
    }

    @Override
    @Transactional(readOnly = true)
    public BangGia tinhGia(ChiTietGioHang chiTiet, TaiKhoan taiKhoan, String maGiamGia) {
        Map<SanPham, Long> thanhTien = new LinkedHashMap<>();
        for (DongGioHang dong : chiTiet.getDanhSachDong()) {
            thanhTien.put(dong.getSanPham(), dong.getThanhTien());
        }
        return uuDaiService.tinhGia(thanhTien, timKhachHang(taiKhoan), maGiamGia);
    }

    @Override
    @Transactional
    public DonHang datHang(TaiKhoan taiKhoan, GioHang gioHang, ThongTinGiaoHang thongTin) {
        KhachHang khachHang = timKhachHang(taiKhoan);
        if (gioHang.isTrong()) {
            throw new LoiNghiepVu("Giỏ hàng đang trống nên chưa thể đặt hàng");
        }
        if (!diaChiHanhChinh.hopLe(thongTin.getTinhThanh(), thongTin.getPhuongXa())) {
            throw new LoiNghiepVu("Phường / xã đã chọn không thuộc tỉnh / thành phố đã chọn, vui lòng chọn lại địa chỉ");
        }
        boolean thanhToanTruoc = DonHang.THANH_TOAN_TRUOC.equals(thongTin.getPhuongThuc());
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTenNguoiNhan(thongTin.getTenNguoiNhan().trim());
        donHang.setSoDienThoaiNhan(thongTin.getSoDienThoaiNhan().trim());
        donHang.setEmailNhan(laySachGhiChu(thongTin.getEmail()));
        donHang.setTinhThanh(thongTin.getTinhThanh());
        donHang.setPhuongXa(thongTin.getPhuongXa());
        donHang.setDiaChiCuThe(thongTin.getDiaChiCuThe().trim());
        donHang.setDiaChiGiao(thongTin.getDiaChiCuThe().trim() + ", " + thongTin.getPhuongXa() + ", " + thongTin.getTinhThanh());
        donHang.setGhiChu(laySachGhiChu(thongTin.getGhiChu()));
        donHang.setPhuongThucThanhToan(thanhToanTruoc ? DonHang.THANH_TOAN_TRUOC : DonHang.THANH_TOAN_KHI_NHAN);
        donHang.setDaThanhToan(false);

        Map<SanPham, Long> thanhTienTheoSanPham = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> muc : gioHang.getSoLuongTheoSanPham().entrySet()) {
            SanPham sanPham = duLieuSanPham.findById(muc.getKey())
                    .filter(SanPham::isDangBan)
                    .orElseThrow(() -> new LoiNghiepVu("Có sản phẩm trong giỏ đã ngừng kinh doanh, hãy kiểm tra lại giỏ hàng"));
            int soLuong = muc.getValue();
            if (soLuong > sanPham.getSoLuongTon()) {
                throw new LoiNghiepVu(sanPham.getTenSanPham() + " chỉ còn " + sanPham.getSoLuongTon() + " sản phẩm, hãy điều chỉnh giỏ hàng");
            }
            sanPham.setSoLuongTon(sanPham.getSoLuongTon() - soLuong);
            duLieuSanPham.save(sanPham);

            ChiTietDonHang chiTiet = new ChiTietDonHang();
            chiTiet.setSanPham(sanPham);
            chiTiet.setSoLuong(soLuong);
            chiTiet.setDonGia(sanPham.getGiaHienTai());
            donHang.themChiTiet(chiTiet);
            thanhTienTheoSanPham.put(sanPham, chiTiet.getThanhTien());
        }

        BangGia bangGia = uuDaiService.tinhGia(thanhTienTheoSanPham, khachHang, thongTin.getMaGiamGia());
        if (bangGia.getLoiMa() != null) {
            throw new LoiNghiepVu(bangGia.getLoiMa());
        }
        long sauUuDai = bangGia.getSauUuDai();
        if (!thanhToanTruoc && !cauHinhThanhToan.choPhepCod(sauUuDai)) {
            throw new LoiNghiepVu("Đơn hàng trên " + String.format("%,d", cauHinhThanhToan.getCodToiDa()).replace(',', '.')
                    + " ₫ không áp dụng thanh toán khi nhận hàng, vui lòng chọn thanh toán trước");
        }
        if (bangGia.getKhuyenMai() != null) {
            donHang.setTenKhuyenMai(bangGia.getKhuyenMai().getTen());
            donHang.setTienKhuyenMai(bangGia.getTienKhuyenMai());
        }
        if (bangGia.getMaGiamGia() != null) {
            uuDaiService.ghiNhanDungMa(bangGia.getMaGiamGia().getMa());
            donHang.setMaGiamGia(bangGia.getMaGiamGia().getMa());
            donHang.setTienGiamMa(bangGia.getTienGiamMa());
        }
        long tienGiamCk = thanhToanTruoc ? cauHinhThanhToan.tinhTienGiam(sauUuDai) : 0L;
        donHang.setTamTinh(bangGia.getTamTinh());
        donHang.setSoTienGiam(tienGiamCk);
        donHang.setTongTien(sauUuDai - tienGiamCk);
        return duLieuDonHang.save(donHang);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DonHang> layDonCuaKhach(TaiKhoan taiKhoan) {
        return duLieuDonHang.findByKhachHangIdOrderByNgayDatDesc(layKhachHang(taiKhoan).getId());
    }

    @Override
    @Transactional(readOnly = true)
    public DonHang layDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        return duLieuDonHang.findById(idDonHang)
                .filter(donHang -> donHang.getKhachHang() != null
                        && donHang.getKhachHang().getId().equals(khachHang.getId()))
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng có mã " + idDonHang));
    }

    @Override
    @Transactional(readOnly = true)
    public DonHang layDonTheoId(Long idDonHang) {
        return duLieuDonHang.findById(idDonHang)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng có mã " + idDonHang));
    }

    @Override
    @Transactional(readOnly = true)
    public DonHang traCuuDon(String maDon, String soDienThoai) {
        String ma = maDon == null ? "" : maDon.trim().toUpperCase();
        String sdt = soDienThoai == null ? "" : soDienThoai.replaceAll("\\s", "");
        if (!ma.matches("^BN\\d{6,}$") || sdt.isEmpty()) {
            throw new LoiNghiepVu("Không tìm thấy đơn hàng khớp mã đơn và số điện thoại đã nhập");
        }
        return duLieuDonHang.findById(Long.parseLong(ma.substring(2)))
                .filter(donHang -> sdt.equals(donHang.getSoDienThoaiNhan()))
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng khớp mã đơn và số điện thoại đã nhập"));
    }

    @Override
    @Transactional
    public void huyDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang, String lyDo, String lyDoKhac) {
        DonHang donHang = layDonCuaKhach(taiKhoan, idDonHang);
        if (!DonHang.CHO_XAC_NHAN.equals(donHang.getTrangThai())) {
            throw new LoiNghiepVu("Chỉ huỷ được đơn đang chờ xác nhận. Với đơn khác, vui lòng gọi cửa hàng để được hỗ trợ");
        }
        dichVuDonHang.huyDonHang(idDonHang, LyDoHuyDon.chuanHoa(lyDo, lyDoKhac, LyDoHuyDon.CUA_KHACH), "KHACH");
    }

    private KhachHang layKhachHang(TaiKhoan taiKhoan) {
        return duLieuKhachHang.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new LoiNghiepVu("Tài khoản này không có hồ sơ khách hàng nên không thể mua hàng"));
    }

    private String laySachGhiChu(String ghiChu) {
        if (ghiChu == null || ghiChu.isBlank()) {
            return null;
        }
        return ghiChu.trim();
    }
}
