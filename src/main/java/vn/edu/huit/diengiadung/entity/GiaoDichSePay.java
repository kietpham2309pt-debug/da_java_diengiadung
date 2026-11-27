package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "giao_dich_sepay")
public class GiaoDichSePay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Long maGiaoDich;

    @Column(length = 60)
    private String nganHang;

    @Column(length = 40)
    private String soTaiKhoan;

    @Column(length = 10)
    private String loai;

    private Long soTien;

    @Column(length = 500)
    private String noiDung;

    @Column(length = 40)
    private String maThanhToan;

    @Column(length = 60)
    private String maThamChieu;

    @Column(length = 30)
    private String thoiGianGiaoDich;

    private LocalDateTime nhanLuc = LocalDateTime.now();

    private boolean daGhiNhan;

    @Column(length = 200)
    private String ketQua;

    @ManyToOne
    @JoinColumn(name = "don_hang_id")
    private DonHang donHang;

    public Long getId() {
        return id;
    }

    public Long getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(Long maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public String getNganHang() {
        return nganHang;
    }

    public void setNganHang(String nganHang) {
        this.nganHang = catBot(nganHang, 60);
    }

    public String getSoTaiKhoan() {
        return soTaiKhoan;
    }

    public void setSoTaiKhoan(String soTaiKhoan) {
        this.soTaiKhoan = catBot(soTaiKhoan, 40);
    }

    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = catBot(loai, 10);
    }

    public Long getSoTien() {
        return soTien;
    }

    public void setSoTien(Long soTien) {
        this.soTien = soTien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = catBot(noiDung, 500);
    }

    public String getMaThanhToan() {
        return maThanhToan;
    }

    public void setMaThanhToan(String maThanhToan) {
        this.maThanhToan = catBot(maThanhToan, 40);
    }

    public String getMaThamChieu() {
        return maThamChieu;
    }

    public void setMaThamChieu(String maThamChieu) {
        this.maThamChieu = catBot(maThamChieu, 60);
    }

    public String getThoiGianGiaoDich() {
        return thoiGianGiaoDich;
    }

    public void setThoiGianGiaoDich(String thoiGianGiaoDich) {
        this.thoiGianGiaoDich = catBot(thoiGianGiaoDich, 30);
    }

    public LocalDateTime getNhanLuc() {
        return nhanLuc;
    }

    public boolean isDaGhiNhan() {
        return daGhiNhan;
    }

    public void setDaGhiNhan(boolean daGhiNhan) {
        this.daGhiNhan = daGhiNhan;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = catBot(ketQua, 200);
    }

    public DonHang getDonHang() {
        return donHang;
    }

    public void setDonHang(DonHang donHang) {
        this.donHang = donHang;
    }

    private static String catBot(String chuoi, int toiDa) {
        if (chuoi == null) {
            return null;
        }
        return chuoi.length() <= toiDa ? chuoi : chuoi.substring(0, toiDa);
    }
}
