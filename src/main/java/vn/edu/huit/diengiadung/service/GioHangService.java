package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.GioHang;

public interface GioHangService {

    void them(GioHang gioHang, Long idSanPham, int soLuong);

    void capNhat(GioHang gioHang, Long idSanPham, int soLuong);

    void xoa(GioHang gioHang, Long idSanPham);

    ChiTietGioHang xemChiTiet(GioHang gioHang);
}
