package vn.edu.huit.diengiadung.model;

import vn.edu.huit.diengiadung.entity.SanPham;

public class DongGioHang {

    private final SanPham sanPham;
    private final int soLuong;

    public DongGioHang(SanPham sanPham, int soLuong) {
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public long getDonGia() {
        return sanPham.getGiaHienTai();
    }

    public long getThanhTien() {
        return getDonGia() * soLuong;
    }
}
