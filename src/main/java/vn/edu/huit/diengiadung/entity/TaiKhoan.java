package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Entity
public class TaiKhoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Vui lòng nhập tên đăng nhập")
    @Pattern(regexp = "^$|^.{4,50}$", message = "Tên đăng nhập phải từ 4 đến 50 ký tự")
    @Column(unique = true, nullable = false, length = 50)
    private String tenDangNhap;

    @NotBlank(message = "Vui lòng nhập mật khẩu")
    @Pattern(regexp = "^$|^.{6,}$", message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String matKhau;

    @NotBlank(message = "Vui lòng nhập họ tên")
    private String hoTen;

    @Email(message = "Email chưa đúng định dạng, ví dụ: ten@gmail.com")
    private String email;

    private String vaiTro = "KHACHHANG";

    private boolean hoatDong = true;

    private LocalDateTime ngayTao = LocalDateTime.now();

    @OneToOne(mappedBy = "taiKhoan")
    private KhachHang khachHang;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public boolean laQuanTri() {
        return "ADMIN".equals(vaiTro);
    }
}
