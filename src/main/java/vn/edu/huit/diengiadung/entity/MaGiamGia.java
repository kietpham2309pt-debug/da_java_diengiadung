package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class MaGiamGia extends UuDaiCoBan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vui lòng nhập mã giảm giá")
    @Pattern(regexp = "^[A-Za-z0-9_-]{3,30}$", message = "Mã gồm 3-30 ký tự chữ, số, gạch ngang hoặc gạch dưới, không dấu cách")
    @Column(nullable = false, unique = true, length = 30)
    private String ma;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    private String moTa;

    @Min(value = 1, message = "Giới hạn lượt dùng phải lớn hơn 0")
    private Integer gioiHanLuotDung;

    private int soLuotDaDung;

    private boolean moiKhachMotLan = true;

    private boolean hienChoKhach = true;

    private LocalDateTime ngayTao = LocalDateTime.now();

    public boolean isHetLuot() {
        return gioiHanLuotDung != null && soLuotDaDung >= gioiHanLuotDung;
    }

    @Override
    public String getTrangThai() {
        String trangThai = super.getTrangThai();
        return "DANG_CHAY".equals(trangThai) && isHetLuot() ? "HET_LUOT" : trangThai;
    }

    @Override
    public String getTenTrangThai() {
        return "HET_LUOT".equals(getTrangThai()) ? "Hết lượt" : super.getTenTrangThai();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma == null ? null : ma.trim().toUpperCase();
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getGioiHanLuotDung() {
        return gioiHanLuotDung;
    }

    public void setGioiHanLuotDung(Integer gioiHanLuotDung) {
        this.gioiHanLuotDung = gioiHanLuotDung;
    }

    public int getSoLuotDaDung() {
        return soLuotDaDung;
    }

    public void setSoLuotDaDung(int soLuotDaDung) {
        this.soLuotDaDung = soLuotDaDung;
    }

    public boolean isMoiKhachMotLan() {
        return moiKhachMotLan;
    }

    public void setMoiKhachMotLan(boolean moiKhachMotLan) {
        this.moiKhachMotLan = moiKhachMotLan;
    }

    public boolean isHienChoKhach() {
        return hienChoKhach;
    }

    public void setHienChoKhach(boolean hienChoKhach) {
        this.hienChoKhach = hienChoKhach;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }
}
