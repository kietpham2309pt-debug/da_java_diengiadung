package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
public class LoaiSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên loại không được để trống")
    @Size(max = 100, message = "Tên loại tối đa 100 ký tự")
    @Column(unique = true, nullable = false, length = 100)
    private String tenLoai;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    private String moTa;

    @OneToMany(mappedBy = "loaiSanPham")
    private List<SanPham> danhSachSanPham = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public List<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }
}
