package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class UuDaiCoBan {

    public static final String PHAN_TRAM = "PHAN_TRAM";
    public static final String SO_TIEN = "SO_TIEN";

    @NotNull(message = "Vui lòng chọn loại giảm giá")
    @Pattern(regexp = "PHAN_TRAM|SO_TIEN", message = "Loại giảm giá không hợp lệ")
    @Column(length = 10, nullable = false)
    private String loaiGiam = PHAN_TRAM;

    @NotNull(message = "Vui lòng nhập giá trị giảm")
    @Min(value = 1, message = "Giá trị giảm phải lớn hơn 0")
    private Long giaTri;

    @Min(value = 1, message = "Mức giảm tối đa phải lớn hơn 0")
    private Long giamToiDa;

    @NotNull(message = "Vui lòng nhập giá trị đơn tối thiểu (0 nếu không giới hạn)")
    @Min(value = 0, message = "Đơn tối thiểu không được âm")
    private Long donToiThieu = 0L;

    @NotNull(message = "Vui lòng chọn thời gian bắt đầu")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime batDau = LocalDateTime.now().withSecond(0).withNano(0);

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime ketThuc;

    private boolean hoatDong = true;

    @AssertTrue(message = "Giảm theo phần trăm chỉ nhận giá trị từ 1 đến 100")
    public boolean isPhanTramHopLe() {
        return !PHAN_TRAM.equals(loaiGiam) || giaTri == null || giaTri <= 100;
    }

    @AssertTrue(message = "Thời gian kết thúc phải sau thời gian bắt đầu")
    public boolean isThoiGianHopLe() {
        return ketThuc == null || batDau == null || ketThuc.isAfter(batDau);
    }

    public long tinhTienGiam(long giaTriApDung) {
        if (giaTri == null || giaTriApDung <= 0) {
            return 0L;
        }
        long tien = PHAN_TRAM.equals(loaiGiam) ? (giaTriApDung * giaTri + 50) / 100 : giaTri;
        if (PHAN_TRAM.equals(loaiGiam) && giamToiDa != null) {
            tien = Math.min(tien, giamToiDa);
        }
        return Math.min(tien, giaTriApDung);
    }

    public boolean dangTrongThoiGian(LocalDateTime luc) {
        return batDau != null && !luc.isBefore(batDau) && (ketThuc == null || luc.isBefore(ketThuc));
    }

    public String getTrangThai() {
        LocalDateTime bayGio = LocalDateTime.now();
        if (!hoatDong) {
            return "TAM_DUNG";
        }
        if (batDau != null && bayGio.isBefore(batDau)) {
            return "SAP_DIEN_RA";
        }
        if (ketThuc != null && !bayGio.isBefore(ketThuc)) {
            return "HET_HAN";
        }
        return "DANG_CHAY";
    }

    public String getTenTrangThai() {
        return switch (getTrangThai()) {
            case "TAM_DUNG" -> "Tạm dừng";
            case "SAP_DIEN_RA" -> "Sắp diễn ra";
            case "HET_HAN" -> "Hết hạn";
            default -> "Đang chạy";
        };
    }

    public String getMoTaMucGiam() {
        if (giaTri == null) {
            return "";
        }
        if (PHAN_TRAM.equals(loaiGiam)) {
            return "Giảm " + giaTri + "%" + (giamToiDa != null ? " (tối đa " + dinhDangTien(giamToiDa) + ")" : "");
        }
        return "Giảm " + dinhDangTien(giaTri);
    }

    public String getMoTaDieuKien() {
        return donToiThieu == null || donToiThieu == 0 ? "Mọi đơn hàng" : "Đơn từ " + dinhDangTien(donToiThieu);
    }

    protected static String dinhDangTien(long soTien) {
        return String.format("%,d", soTien).replace(',', '.') + " ₫";
    }

    public String getLoaiGiam() {
        return loaiGiam;
    }

    public void setLoaiGiam(String loaiGiam) {
        this.loaiGiam = loaiGiam;
    }

    public Long getGiaTri() {
        return giaTri;
    }

    public void setGiaTri(Long giaTri) {
        this.giaTri = giaTri;
    }

    public Long getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(Long giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public Long getDonToiThieu() {
        return donToiThieu;
    }

    public void setDonToiThieu(Long donToiThieu) {
        this.donToiThieu = donToiThieu;
    }

    public LocalDateTime getBatDau() {
        return batDau;
    }

    public void setBatDau(LocalDateTime batDau) {
        this.batDau = batDau;
    }

    public LocalDateTime getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(LocalDateTime ketThuc) {
        this.ketThuc = ketThuc;
    }

    public boolean isHoatDong() {
        return hoatDong;
    }

    public void setHoatDong(boolean hoatDong) {
        this.hoatDong = hoatDong;
    }
}
