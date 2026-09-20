package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ngayNhap = LocalDateTime.now();

    private Long tongTien = 0L;

    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "nha_cung_cap_id")
    private NhaCungCap nhaCungCap;

    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietPhieuNhap> danhSachChiTiet = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getNgayNhap() {
        return ngayNhap;
    }

    public void setNgayNhap(LocalDateTime ngayNhap) {
        this.ngayNhap = ngayNhap;
    }

    public Long getTongTien() {
        return tongTien;
    }

    public void setTongTien(Long tongTien) {
        this.tongTien = tongTien;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public List<ChiTietPhieuNhap> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietPhieuNhap> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }

    public void themChiTiet(ChiTietPhieuNhap chiTiet) {
        chiTiet.setPhieuNhap(this);
        danhSachChiTiet.add(chiTiet);
    }
}
