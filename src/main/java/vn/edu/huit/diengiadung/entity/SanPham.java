package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Formula;

import java.util.ArrayList;
import java.util.List;

@Entity
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã sản phẩm không được để trống")
    @Size(max = 50, message = "Mã sản phẩm tối đa 50 ký tự")
    @Pattern(regexp = "[A-Za-z0-9-]*", message = "Mã sản phẩm chỉ gồm chữ không dấu, số và dấu gạch ngang")
    @Column(unique = true, nullable = false, length = 50)
    private String maSanPham;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 200, message = "Tên sản phẩm tối đa 200 ký tự")
    @Column(nullable = false, length = 200)
    private String tenSanPham;

    @NotNull(message = "Giá bán không được để trống")
    @Min(value = 1, message = "Giá bán phải lớn hơn 0")
    private Long giaBan;

    @Min(value = 0, message = "Giá khuyến mãi không được âm")
    private Long giaKhuyenMai;

    @Formula("case when gia_khuyen_mai is not null and gia_khuyen_mai > 0 then gia_khuyen_mai else gia_ban end")
    private Long giaBanThucTe;

    @Min(value = 0, message = "Số lượng tồn không được âm")
    private int soLuongTon;

    private String hinhAnh;

    @Size(max = 100, message = "Công suất hoặc dung tích tối đa 100 ký tự")
    private String congSuat;

    @Min(value = 0, message = "Số tháng bảo hành không được âm")
    @Max(value = 120, message = "Bảo hành tối đa 120 tháng")
    private int baoHanhThang;

    @Size(max = 1000, message = "Mô tả tối đa 1000 ký tự")
    @Column(length = 1000)
    private String moTa;

    @Size(max = 2000, message = "Thông số kỹ thuật tối đa 2000 ký tự")
    @Column(length = 2000)
    private String thongSoKyThuat;

    private boolean dangBan = true;

    @NotNull(message = "Chọn loại sản phẩm")
    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id")
    private LoaiSanPham loaiSanPham;

    @NotNull(message = "Chọn thương hiệu")
    @ManyToOne
    @JoinColumn(name = "thuong_hieu_id")
    private ThuongHieu thuongHieu;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Long giaBan) {
        this.giaBan = giaBan;
    }

    public Long getGiaKhuyenMai() {
        return giaKhuyenMai;
    }

    public void setGiaKhuyenMai(Long giaKhuyenMai) {
        this.giaKhuyenMai = giaKhuyenMai;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getCongSuat() {
        return congSuat;
    }

    public void setCongSuat(String congSuat) {
        this.congSuat = congSuat;
    }

    public int getBaoHanhThang() {
        return baoHanhThang;
    }

    public void setBaoHanhThang(int baoHanhThang) {
        this.baoHanhThang = baoHanhThang;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getThongSoKyThuat() {
        return thongSoKyThuat;
    }

    public void setThongSoKyThuat(String thongSoKyThuat) {
        this.thongSoKyThuat = thongSoKyThuat;
    }

    public boolean isDangBan() {
        return dangBan;
    }

    public void setDangBan(boolean dangBan) {
        this.dangBan = dangBan;
    }

    public LoaiSanPham getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public ThuongHieu getThuongHieu() {
        return thuongHieu;
    }

    public void setThuongHieu(ThuongHieu thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public String getDuongDanAnh() {
        if (coAnhRieng()) {
            return hinhAnh.startsWith("/") ? hinhAnh : "/uploads/" + hinhAnh;
        }
        if (loaiSanPham != null && loaiSanPham.getMaLoai() != null) {
            return "/img/sp-" + loaiSanPham.getMaLoai() + ".jpg";
        }
        return "/img/hero-san-pham.jpg";
    }

    public boolean coAnhRieng() {
        return hinhAnh != null && !hinhAnh.isBlank();
    }

    public int getPhanTramGiam() {
        if (giaKhuyenMai == null || giaBan == null || giaBan <= 0 || giaKhuyenMai >= giaBan) {
            return 0;
        }
        return (int) Math.round((giaBan - giaKhuyenMai) * 100.0 / giaBan);
    }

    public Long getGiaHienTai() {
        if (giaKhuyenMai != null && giaKhuyenMai > 0) {
            return giaKhuyenMai;
        }
        return giaBan;
    }

    public long getSoTienTietKiem() {
        if (getPhanTramGiam() == 0) {
            return 0;
        }
        return giaBan - giaKhuyenMai;
    }

    public boolean isConHang() {
        return soLuongTon > 0;
    }

    public boolean isSapHetHang() {
        return soLuongTon > 0 && soLuongTon <= 5;
    }

    public List<String> getDanhSachDoanMoTa() {
        List<String> danhSachDoan = new ArrayList<>();
        if (moTa == null) {
            return danhSachDoan;
        }
        for (String doan : moTa.split("\\r?\\n")) {
            if (!doan.isBlank()) {
                danhSachDoan.add(doan.trim());
            }
        }
        return danhSachDoan;
    }

    public List<ThongSo> getDanhSachThongSo() {
        List<ThongSo> danhSach = new ArrayList<>();
        for (String dong : tachDongThongSo()) {
            int viTriHaiCham = dong.indexOf(':');
            if (viTriHaiCham > 0 && viTriHaiCham < dong.length() - 1) {
                danhSach.add(new ThongSo(dong.substring(0, viTriHaiCham).trim(),
                        dong.substring(viTriHaiCham + 1).trim()));
            }
        }
        return danhSach;
    }

    public List<TinhNang> getDanhSachTinhNang() {
        List<TinhNang> danhSach = new ArrayList<>();
        for (String dong : tachDongThongSo()) {
            int viTriHaiCham = dong.indexOf(':');
            if (viTriHaiCham <= 0 || viTriHaiCham == dong.length() - 1) {
                danhSach.add(new TinhNang(viTriHaiCham > 0 ? dong.substring(0, viTriHaiCham).trim() : dong));
            }
        }
        return danhSach;
    }

    private List<String> tachDongThongSo() {
        List<String> danhSachDong = new ArrayList<>();
        if (thongSoKyThuat == null) {
            return danhSachDong;
        }
        for (String dong : thongSoKyThuat.split("[;\\r\\n]+")) {
            if (!dong.isBlank()) {
                danhSachDong.add(dong.trim());
            }
        }
        return danhSachDong;
    }

    public static class ThongSo {

        private final String ten;
        private final String giaTri;

        public ThongSo(String ten, String giaTri) {
            this.ten = ten;
            this.giaTri = giaTri;
        }

        public String getTen() {
            return ten;
        }

        public String getGiaTri() {
            return giaTri;
        }
    }

    public static class TinhNang {

        private static final String[][] BANG_BIEU_TUONG = {
                {"khoá", "ic-tn-khoa"}, {"khóa", "ic-tn-khoa"},
                {"hẹn giờ", "ic-dong-ho"},
                {"cảm ứng", "ic-tn-cam-ung"}, {"cảm biến", "ic-tn-cam-ung"},
                {"đèn", "ic-tn-den"},
                {"lọc", "ic-tn-loc"},
                {"sấy", "ic-tn-say"},
                {"quạt", "ic-tn-quat"}, {"đối lưu", "ic-tn-quat"},
                {"nhiệt dư", "ic-tn-canh-bao"}, {"quá nhiệt", "ic-tn-canh-bao"}, {"chống tràn", "ic-tn-canh-bao"},
                {"nước", "ic-tn-nuoc"},
                {"rã đông", "ic-tn-tuyet"},
                {"nướng", "ic-tn-nhiet-tren"},
                {"inverter", "ic-tn-nhanh"}, {"nhanh", "ic-tn-nhanh"},
                {"nồi", "ic-tn-noi"}, {"chảo", "ic-tn-chao"}
        };

        private final String ten;

        public TinhNang(String ten) {
            this.ten = ten;
        }

        public String getTen() {
            return ten;
        }

        public String getBieuTuong() {
            String tenThuong = ten.toLowerCase();
            for (String[] dong : BANG_BIEU_TUONG) {
                if (tenThuong.contains(dong[0])) {
                    return dong[1];
                }
            }
            return "ic-bao-hanh";
        }
    }
}
