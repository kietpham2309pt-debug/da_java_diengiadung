package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class DonHang {

    public static final String CHO_XAC_NHAN = "CHO_XAC_NHAN";
    public static final String DA_XAC_NHAN = "DA_XAC_NHAN";
    public static final String DANG_GIAO = "DANG_GIAO";
    public static final String HOAN_THANH = "HOAN_THANH";
    public static final String DA_HUY = "DA_HUY";
    public static final String THANH_TOAN_TRUOC = "CK";
    public static final String THANH_TOAN_KHI_NHAN = "COD";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ngayDat = LocalDateTime.now();

    private String tenNguoiNhan;

    private String soDienThoaiNhan;

    private String diaChiGiao;

    @Column(length = 120)
    private String emailNhan;

    @Column(length = 80)
    private String tinhThanh;

    @Column(length = 80)
    private String phuongXa;

    @Column(length = 200)
    private String diaChiCuThe;

    @Column(length = 10)
    private String phuongThucThanhToan;

    private Boolean daThanhToan;

    private Long tamTinh;

    private Long soTienGiam;

    @Column(length = 30)
    private String maGiamGia;

    private Long tienGiamMa;

    @Column(length = 120)
    private String tenKhuyenMai;

    private Long tienKhuyenMai;

    private Long tongTien = 0L;

    private String trangThai = CHO_XAC_NHAN;

    @Column(length = 500)
    private String ghiChu;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChiTietDonHang> danhSachChiTiet = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getTenNguoiNhan() {
        return tenNguoiNhan;
    }

    public void setTenNguoiNhan(String tenNguoiNhan) {
        this.tenNguoiNhan = tenNguoiNhan;
    }

    public String getSoDienThoaiNhan() {
        return soDienThoaiNhan;
    }

    public void setSoDienThoaiNhan(String soDienThoaiNhan) {
        this.soDienThoaiNhan = soDienThoaiNhan;
    }

    public String getDiaChiGiao() {
        return diaChiGiao;
    }

    public void setDiaChiGiao(String diaChiGiao) {
        this.diaChiGiao = diaChiGiao;
    }

    public String getMaDon() {
        return id == null ? "" : String.format("BN%06d", id);
    }

    public String getEmailNhan() {
        return emailNhan;
    }

    public void setEmailNhan(String emailNhan) {
        this.emailNhan = emailNhan;
    }

    public String getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(String tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public String getPhuongXa() {
        return phuongXa;
    }

    public void setPhuongXa(String phuongXa) {
        this.phuongXa = phuongXa;
    }

    public String getDiaChiCuThe() {
        return diaChiCuThe;
    }

    public void setDiaChiCuThe(String diaChiCuThe) {
        this.diaChiCuThe = diaChiCuThe;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan == null ? THANH_TOAN_KHI_NHAN : phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public boolean isThanhToanTruoc() {
        return THANH_TOAN_TRUOC.equals(getPhuongThucThanhToan());
    }

    public String getTenPhuongThucThanhToan() {
        return isThanhToanTruoc() ? "Thanh toán trước (chuyển khoản VietQR)" : "Thanh toán khi nhận hàng (COD)";
    }

    public boolean isDaThanhToan() {
        return Boolean.TRUE.equals(daThanhToan);
    }

    public void setDaThanhToan(boolean daThanhToan) {
        this.daThanhToan = daThanhToan;
    }

    public Long getTamTinh() {
        return tamTinh == null ? tongTien : tamTinh;
    }

    public void setTamTinh(Long tamTinh) {
        this.tamTinh = tamTinh;
    }

    public long getSoTienGiam() {
        return soTienGiam == null ? 0L : soTienGiam;
    }

    public void setSoTienGiam(Long soTienGiam) {
        this.soTienGiam = soTienGiam;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia;
    }

    public long getTienGiamMa() {
        return tienGiamMa == null ? 0L : tienGiamMa;
    }

    public void setTienGiamMa(Long tienGiamMa) {
        this.tienGiamMa = tienGiamMa;
    }

    public String getTenKhuyenMai() {
        return tenKhuyenMai;
    }

    public void setTenKhuyenMai(String tenKhuyenMai) {
        this.tenKhuyenMai = tenKhuyenMai;
    }

    public long getTienKhuyenMai() {
        return tienKhuyenMai == null ? 0L : tienKhuyenMai;
    }

    public void setTienKhuyenMai(Long tienKhuyenMai) {
        this.tienKhuyenMai = tienKhuyenMai;
    }

    public long getTongUuDai() {
        return getTienKhuyenMai() + getTienGiamMa() + getSoTienGiam();
    }

    public boolean isKhachVangLai() {
        return khachHang == null;
    }

    public boolean isChoThanhToan() {
        return isThanhToanTruoc() && !isDaThanhToan() && !DA_HUY.equals(trangThai);
    }

    public Long getTongTien() {
        return tongTien;
    }

    public void setTongTien(Long tongTien) {
        this.tongTien = tongTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public List<ChiTietDonHang> getDanhSachChiTiet() {
        return danhSachChiTiet;
    }

    public void setDanhSachChiTiet(List<ChiTietDonHang> danhSachChiTiet) {
        this.danhSachChiTiet = danhSachChiTiet;
    }

    public void themChiTiet(ChiTietDonHang chiTiet) {
        chiTiet.setDonHang(this);
        danhSachChiTiet.add(chiTiet);
    }

    public String getTenTrangThai() {
        if (DA_XAC_NHAN.equals(trangThai)) {
            return "Đã xác nhận";
        }
        if (DANG_GIAO.equals(trangThai)) {
            return "Đang giao";
        }
        if (HOAN_THANH.equals(trangThai)) {
            return "Hoàn thành";
        }
        if (DA_HUY.equals(trangThai)) {
            return "Đã huỷ";
        }
        return "Chờ xác nhận";
    }
}
