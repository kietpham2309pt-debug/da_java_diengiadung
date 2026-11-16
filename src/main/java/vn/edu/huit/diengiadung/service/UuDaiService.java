package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.KhuyenMai;
import vn.edu.huit.diengiadung.entity.MaGiamGia;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.model.BangGia;

import java.util.List;
import java.util.Map;

public interface UuDaiService {

    BangGia tinhGia(Map<SanPham, Long> thanhTienTheoSanPham, KhachHang khachHang, String ma);

    List<MaGiamGia> maDangMoChoKhach();

    List<KhuyenMai> khuyenMaiDangChay(boolean chiThanhVien);

    void ghiNhanDungMa(String ma);

    void hoanLuotDungMa(String ma);

    List<MaGiamGia> timMaGiamGia(String tuKhoa);

    MaGiamGia layMaGiamGia(Long id);

    MaGiamGia luuMaGiamGia(MaGiamGia maGiamGia);

    void doiTrangThaiMa(Long id);

    void xoaMaGiamGia(Long id);

    String taoMaNgauNhien();

    List<KhuyenMai> timKhuyenMai(String tuKhoa);

    KhuyenMai layKhuyenMai(Long id);

    KhuyenMai luuKhuyenMai(KhuyenMai khuyenMai);

    void doiTrangThaiKhuyenMai(Long id);

    void xoaKhuyenMai(Long id);
}
