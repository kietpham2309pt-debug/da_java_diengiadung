package vn.edu.huit.diengiadung.model;

import vn.edu.huit.diengiadung.entity.SanPham;

public class CartItem {
    private Long idSanPham;
    private String tenSanPham;
    private String hinhAnh;
    private Long donGia;
    private int soLuong;

    public CartItem() {}

    // Constructor tự động trích xuất dữ liệu từ SanPham entity
    public CartItem(SanPham sp, int soLuong) {
        this.idSanPham = sp.getId();
        this.tenSanPham = sp.getTenSanPham();
        this.hinhAnh = sp.getDuongDanAnh(); // Tận dụng hàm bạn đã viết trong entity
        this.donGia = sp.getGiaHienTai();   // Lấy giá thực tế (có hoặc ko có khuyến mãi)
        this.soLuong = soLuong;
    }

    public Long getThanhTien() {
        return donGia * soLuong;
    }

    public Long getIdSanPham() { return idSanPham; }
    public void setIdSanPham(Long idSanPham) { this.idSanPham = idSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public Long getDonGia() { return donGia; }
    public void setDonGia(Long donGia) { this.donGia = donGia; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}