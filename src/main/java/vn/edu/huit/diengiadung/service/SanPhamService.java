package vn.edu.huit.diengiadung.service;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.huit.diengiadung.entity.SanPham;

import java.util.List;

public interface SanPhamService {

    KetQuaPhanTrang<SanPham> timKiemPhiaKhach(BoLocSanPham boLoc);

    List<SanPham> laySanPhamMoi(int soLuong);

    List<SanPham> laySanPhamGiamGia(int soLuong);

    SanPham laySanPhamDangBan(Long id);

    List<SanPham> laySanPhamCungLoai(SanPham sanPham, int soLuong);

    KetQuaPhanTrang<SanPham> timKiemQuanTri(String tuKhoa, Long loai, Long hang, String trangThai, int trang);

    SanPham layTheoId(Long id);

    SanPham luu(SanPham sanPham, MultipartFile tepAnh, boolean xoaAnhCu);

    void xoa(Long id);

    void doiTrangThaiBan(Long id);
}
