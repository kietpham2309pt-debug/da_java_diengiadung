package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.model.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.DonHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.List;
import java.util.Map;

@Service
public class DatHangServiceImpl implements DatHangService {

    private final DonHangRepository duLieuDonHang;
    private final KhachHangRepository duLieuKhachHang;
    private final SanPhamRepository duLieuSanPham;
    private final DonHangService dichVuDonHang;

    public DatHangServiceImpl(DonHangRepository duLieuDonHang,
                              KhachHangRepository duLieuKhachHang,
                              SanPhamRepository duLieuSanPham,
                              DonHangService dichVuDonHang) {
        this.duLieuDonHang = duLieuDonHang;
        this.duLieuKhachHang = duLieuKhachHang;
        this.duLieuSanPham = duLieuSanPham;
        this.dichVuDonHang = dichVuDonHang;
    }

    @Override
    @Transactional(readOnly = true)
    public ThongTinGiaoHang layThongTinGiaoHangMacDinh(TaiKhoan taiKhoan) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        ThongTinGiaoHang thongTin = new ThongTinGiaoHang();
        thongTin.setTenNguoiNhan(khachHang.getHoTen());
        thongTin.setSoDienThoaiNhan(khachHang.getSoDienThoai());
        thongTin.setDiaChiGiao(khachHang.getDiaChi());
        return thongTin;
    }

    @Override
    @Transactional
    public DonHang datHang(TaiKhoan taiKhoan, GioHang gioHang, ThongTinGiaoHang thongTin) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        if (gioHang.isTrong()) {
            throw new LoiNghiepVu("Giỏ hàng đang trống nên chưa thể đặt hàng");
        }
        DonHang donHang = new DonHang();
        donHang.setKhachHang(khachHang);
        donHang.setTenNguoiNhan(thongTin.getTenNguoiNhan().trim());
        donHang.setSoDienThoaiNhan(thongTin.getSoDienThoaiNhan().trim());
        donHang.setDiaChiGiao(thongTin.getDiaChiGiao().trim());
        donHang.setGhiChu(laySachGhiChu(thongTin.getGhiChu()));

        long tongTien = 0;
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
            tongTien += chiTiet.getThanhTien();
        }
        donHang.setTongTien(tongTien);
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
    @Transactional
    public void huyDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang) {
        DonHang donHang = layDonCuaKhach(taiKhoan, idDonHang);
        if (!DonHang.CHO_XAC_NHAN.equals(donHang.getTrangThai())) {
            throw new LoiNghiepVu("Chỉ huỷ được đơn đang chờ xác nhận. Với đơn khác, vui lòng gọi cửa hàng để được hỗ trợ");
        }
        dichVuDonHang.huyDonHang(idDonHang);
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
