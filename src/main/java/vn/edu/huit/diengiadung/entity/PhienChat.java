package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PhienChat {

    public static final String BOT = "BOT";
    public static final String CHO_NHAN_VIEN = "CHO_NHAN_VIEN";
    public static final String DANG_TU_VAN = "DANG_TU_VAN";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, unique = true)
    private String maKhach;

    @ManyToOne
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @Column(length = 80)
    private String tenKhach;

    @Column(length = 20)
    private String soDienThoai;

    @Column(length = 20, nullable = false)
    private String trangThai = BOT;

    @Column(length = 80)
    private String tenNhanVien;

    private int soChuaDocQuanTri;

    private int soChuaDocKhach;

    private LocalDateTime ngayTao = LocalDateTime.now();

    private LocalDateTime capNhatLuc = LocalDateTime.now();

    @OneToMany(mappedBy = "phienChat")
    @OrderBy("id ASC")
    private List<TinNhanChat> danhSachTinNhan = new ArrayList<>();

    public boolean laKhachVangLai() {
        return khachHang == null;
    }

    public String getTenHienThi() {
        if (khachHang != null) {
            return khachHang.getHoTen();
        }
        if (tenKhach != null && !tenKhach.isBlank()) {
            return tenKhach;
        }
        return "Khách vãng lai #" + id;
    }

    public String getSoDienThoaiLienHe() {
        if (soDienThoai != null && !soDienThoai.isBlank()) {
            return soDienThoai;
        }
        return khachHang == null ? null : khachHang.getSoDienThoai();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaKhach() {
        return maKhach;
    }

    public void setMaKhach(String maKhach) {
        this.maKhach = maKhach;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public String getTenKhach() {
        return tenKhach;
    }

    public void setTenKhach(String tenKhach) {
        this.tenKhach = tenKhach;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenNhanVien() {
        return tenNhanVien;
    }

    public void setTenNhanVien(String tenNhanVien) {
        this.tenNhanVien = tenNhanVien;
    }

    public int getSoChuaDocQuanTri() {
        return soChuaDocQuanTri;
    }

    public void setSoChuaDocQuanTri(int soChuaDocQuanTri) {
        this.soChuaDocQuanTri = soChuaDocQuanTri;
    }

    public int getSoChuaDocKhach() {
        return soChuaDocKhach;
    }

    public void setSoChuaDocKhach(int soChuaDocKhach) {
        this.soChuaDocKhach = soChuaDocKhach;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getCapNhatLuc() {
        return capNhatLuc;
    }

    public void setCapNhatLuc(LocalDateTime capNhatLuc) {
        this.capNhatLuc = capNhatLuc;
    }

    public List<TinNhanChat> getDanhSachTinNhan() {
        return danhSachTinNhan;
    }

    public void setDanhSachTinNhan(List<TinNhanChat> danhSachTinNhan) {
        this.danhSachTinNhan = danhSachTinNhan;
    }
}
