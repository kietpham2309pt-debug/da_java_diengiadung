package vn.edu.huit.diengiadung.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ThongTinGiaoHang {

    @NotBlank(message = "Vui lòng nhập họ tên người nhận")
    @Pattern(regexp = "^\\s*$|^[\\s\\S]{2,80}$", message = "Họ tên phải từ 2 đến 80 ký tự")
    private String tenNguoiNhan;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    @Pattern(regexp = "^$|^0\\d{9}$", message = "Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0")
    private String soDienThoaiNhan;

    @Email(message = "Email chưa đúng định dạng, ví dụ: ten@gmail.com")
    @Size(max = 120, message = "Email tối đa 120 ký tự")
    private String email;

    @NotBlank(message = "Vui lòng chọn tỉnh / thành phố")
    private String tinhThanh;

    @NotBlank(message = "Vui lòng chọn phường / xã")
    private String phuongXa;

    @NotBlank(message = "Vui lòng nhập số nhà, tên đường")
    @Pattern(regexp = "^\\s*$|^[\\s\\S]{3,200}$", message = "Số nhà, tên đường phải từ 3 đến 200 ký tự")
    private String diaChiCuThe;

    @Size(max = 500, message = "Ghi chú tối đa 500 ký tự")
    private String ghiChu;

    @NotBlank(message = "Vui lòng chọn phương thức thanh toán")
    @Pattern(regexp = "^$|CK|COD", message = "Phương thức thanh toán không hợp lệ")
    private String phuongThuc;

    @Size(max = 30, message = "Mã giảm giá tối đa 30 ký tự")
    private String maGiamGia;

    @AssertTrue(message = "Vui lòng đồng ý với chính sách bảo vệ dữ liệu cá nhân để đặt hàng")
    private boolean dongY;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public String getMaGiamGia() {
        return maGiamGia;
    }

    public void setMaGiamGia(String maGiamGia) {
        this.maGiamGia = maGiamGia == null || maGiamGia.isBlank() ? null : maGiamGia.trim().toUpperCase();
    }

    public boolean isDongY() {
        return dongY;
    }

    public void setDongY(boolean dongY) {
        this.dongY = dongY;
    }
}
