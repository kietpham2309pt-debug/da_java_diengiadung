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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime ngayDat = LocalDateTime.now();

    private String tenNguoiNhan;

    private String soDienThoaiNhan;

    private String diaChiGiao;

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
