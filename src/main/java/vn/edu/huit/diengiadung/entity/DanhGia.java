package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "uk_danh_gia_sp_kh", columnNames = {"san_pham_id", "khach_hang_id"}))
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "san_pham_id")
    private SanPham sanPham;

    @ManyToOne(optional = false)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    private int soSao;

    @Column(length = 1000)
    private String noiDung;

    private LocalDateTime ngayTao = LocalDateTime.now();

    private boolean hien = true;

    public String getTenHienThi() {
        String ten = khachHang == null || khachHang.getHoTen() == null ? "Khách hàng" : khachHang.getHoTen().trim();
        String[] tu = ten.split("\\s+");
        if (tu.length < 2) {
            return ten;
        }
        StringBuilder ketQua = new StringBuilder(tu[0]);
        for (int i = 1; i < tu.length - 1; i++) {
            ketQua.append(' ').append(tu[i].charAt(0)).append('.');
        }
        return ketQua.append(' ').append(tu[tu.length - 1]).toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public int getSoSao() {
        return soSao;
    }

    public void setSoSao(int soSao) {
        this.soSao = soSao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public boolean isHien() {
        return hien;
    }

    public void setHien(boolean hien) {
        this.hien = hien;
    }
}
