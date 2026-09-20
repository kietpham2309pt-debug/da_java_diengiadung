package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class NhaCungCap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên nhà cung cấp không được để trống")
    @Column(nullable = false, length = 150)
    private String tenNhaCungCap;

    private String soDienThoai;

    private String diaChi;

    private String email;

    @OneToMany(mappedBy = "nhaCungCap")
    private List<PhieuNhap> danhSachPhieuNhap = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenNhaCungCap() {
        return tenNhaCungCap;
    }

    public void setTenNhaCungCap(String tenNhaCungCap) {
        this.tenNhaCungCap = tenNhaCungCap;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PhieuNhap> getDanhSachPhieuNhap() {
        return danhSachPhieuNhap;
    }

    public void setDanhSachPhieuNhap(List<PhieuNhap> danhSachPhieuNhap) {
        this.danhSachPhieuNhap = danhSachPhieuNhap;
    }
}
