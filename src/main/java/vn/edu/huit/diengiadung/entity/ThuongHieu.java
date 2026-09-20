package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class ThuongHieu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Column(unique = true, nullable = false, length = 100)
    private String tenThuongHieu;

    private String xuatXu;

    @OneToMany(mappedBy = "thuongHieu")
    private List<SanPham> danhSachSanPham = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenThuongHieu() {
        return tenThuongHieu;
    }

    public void setTenThuongHieu(String tenThuongHieu) {
        this.tenThuongHieu = tenThuongHieu;
    }

    public String getXuatXu() {
        return xuatXu;
    }

    public void setXuatXu(String xuatXu) {
        this.xuatXu = xuatXu;
    }

    public List<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }
}
