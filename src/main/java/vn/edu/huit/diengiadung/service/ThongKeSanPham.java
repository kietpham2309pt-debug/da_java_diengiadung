package vn.edu.huit.diengiadung.service;

public class ThongKeSanPham {

    private final String maSanPham;
    private final String tenSanPham;
    private long soLuongBan;
    private long doanhThu;

    public ThongKeSanPham(
            String maSanPham,
            String tenSanPham,
            long soLuongBan,
            long doanhThu) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuongBan = soLuongBan;
        this.doanhThu = doanhThu;
    }

    public String getMaSanPham() {
        return maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public long getSoLuongBan() {
        return soLuongBan;
    }

    public long getDoanhThu() {
        return doanhThu;
    }

    public void themSoLuong(long soLuong) {
        this.soLuongBan += soLuong;
    }

    public void themDoanhThu(long doanhThu) {
        this.doanhThu += doanhThu;
    }
}