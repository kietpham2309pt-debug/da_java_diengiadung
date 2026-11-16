package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
public class KhuyenMai extends UuDaiCoBan {

    public static final String TAT_CA = "TAT_CA";
    public static final String THEO_LOAI = "LOAI_SAN_PHAM";
    public static final String THEO_HANG = "THUONG_HIEU";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vui lòng nhập tên chương trình")
    @Size(max = 120, message = "Tên chương trình tối đa 120 ký tự")
    @Column(nullable = false, length = 120)
    private String ten;

    @Size(max = 255, message = "Mô tả tối đa 255 ký tự")
    private String moTa;

    @Pattern(regexp = "TAT_CA|LOAI_SAN_PHAM|THUONG_HIEU", message = "Phạm vi áp dụng không hợp lệ")
    @Column(length = 20, nullable = false)
    private String phamVi = TAT_CA;

    @ManyToOne
    @JoinColumn(name = "loai_san_pham_id")
    private LoaiSanPham loaiSanPham;

    @ManyToOne
    @JoinColumn(name = "thuong_hieu_id")
    private ThuongHieu thuongHieu;

    private boolean chiThanhVien = true;

    @AssertTrue(message = "Vui lòng chọn loại sản phẩm / thương hiệu được áp dụng")
    public boolean isPhamViHopLe() {
        return !(THEO_LOAI.equals(phamVi) && loaiSanPham == null) && !(THEO_HANG.equals(phamVi) && thuongHieu == null);
    }

    public boolean apDungCho(SanPham sanPham) {
        if (THEO_LOAI.equals(phamVi)) {
            return loaiSanPham != null && sanPham.getLoaiSanPham() != null
                    && loaiSanPham.getId().equals(sanPham.getLoaiSanPham().getId());
        }
        if (THEO_HANG.equals(phamVi)) {
            return thuongHieu != null && sanPham.getThuongHieu() != null
                    && thuongHieu.getId().equals(sanPham.getThuongHieu().getId());
        }
        return true;
    }

    public String getMoTaPhamVi() {
        if (THEO_LOAI.equals(phamVi) && loaiSanPham != null) {
            return "Loại: " + loaiSanPham.getTenLoai();
        }
        if (THEO_HANG.equals(phamVi) && thuongHieu != null) {
            return "Hãng: " + thuongHieu.getTenThuongHieu();
        }
        return "Tất cả sản phẩm";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getPhamVi() {
        return phamVi;
    }

    public void setPhamVi(String phamVi) {
        this.phamVi = phamVi;
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

    public boolean isChiThanhVien() {
        return chiThanhVien;
    }

    public void setChiThanhVien(boolean chiThanhVien) {
        this.chiThanhVien = chiThanhVien;
    }
}
