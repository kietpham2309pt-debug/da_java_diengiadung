package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;

@Entity
public class ChiTietPhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int soLuong;

    private Long giaNhap;

    @ManyToOne
    @JoinColumn(name = "phieu_nhap_id")
    private PhieuNhap phieuNhap;

    @ManyToOne
    @JoinColumn(name = "san_pham_id")
    private SanPham sanPham;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Long getGiaNhap() {
        return giaNhap;
    }

    public void setGiaNhap(Long giaNhap) {
        this.giaNhap = giaNhap;
    }

    public PhieuNhap getPhieuNhap() {
        return phieuNhap;
    }

    public void setPhieuNhap(PhieuNhap phieuNhap) {
        this.phieuNhap = phieuNhap;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Long getThanhTien() {
        if (giaNhap == null) {
            return 0L;
        }
        return giaNhap * soLuong;
    }
}
