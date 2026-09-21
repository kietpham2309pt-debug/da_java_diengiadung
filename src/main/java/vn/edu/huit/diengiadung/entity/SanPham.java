package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Column(unique = true, nullable = false, length = 50)
    private String maSanPham;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Column(nullable = false, length = 200)
    private String tenSanPham;

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 1, message = "Giá bán phải lớn hơn 0")
    private Long giaBan;

    private Long giaKhuyenMai;

    @Min(value = 0, message = "Số lượng tồn không được âm")
    private int soLuongTon;

    private String hinhAnh;

    private String congSuat;

    private int baoHanhThang;

    @Column(length = 1000)
    private String moTa;

    @Column(length = 2000)
    private String thongSoKyThuat;

    private boolean dangBan = true;

    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id")
    private LoaiSanPham loaiSanPham;

    @ManyToOne
    @JoinColumn(name = "thuong_hieu_id")
    private ThuongHieu thuongHieu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Long giaBan) {
        this.giaBan = giaBan;
    }

    public Long getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(Long giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getCongSuat() {
        return congSuat;
    }

    public void setCongSuat(String congSuat) {
        this.congSuat = congSuat;
    }

    public int getBaoHanhThang() {
        return baoHanhThang;
    }

    public void setBaoHanhThang(int baoHanhThang) {
        this.baoHanhThang = baoHanhThang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getThongSoKyThuat() {
        return thongSoKyThuat;
    }

    public void setThongSoKyThuat(String thongSoKyThuat) {
        this.thongSoKyThuat = thongSoKyThuat;
    }

    public boolean isDangBan() {
        return dangBan;
    }

    public void setDangBan(boolean dangBan) {
        this.dangBan = dangBan;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public ThuongHieu getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(ThuongHieu thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public String getDuongDanAnh() {
        if (hinhAnh != null && !hinhAnh.isBlank()) {
            return hinhAnh.startsWith("/") ? hinhAnh : "/uploads/" + hinhAnh;
        }
        if (loaiSanPham != null && loaiSanPham.getMaLoai() != null) {
            return "/img/sp-" + loaiSanPham.getMaLoai() + ".jpg";
        }
        return "/img/hero-san-pham.jpg";
    }

    public int getPhanTramGiam() {
        if (giaKhuyenMai == null || giaBan == null || giaBan <= 0 || giaKhuyenMai >= giaBan) {
            return 0;
        }
        return (int) Math.round((giaBan - giaKhuyenMai) * 100.0 / giaBan);
    }

    public Long getGiaHienTai() {
        if (giaKhuyenMai != null && giaKhuyenMai > 0) {
            return giaKhuyenMai;
        }
        return giaBan;
    }

    public boolean isConHang() {
        return soLuongTon > 0;
    }
}
