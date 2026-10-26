package vn.edu.huit.diengiadung.model;

import java.util.List;

public class ChiTietGioHang {

    private final List<DongGioHang> danhSachDong;
    private final List<String> canhBao;

    public ChiTietGioHang(List<DongGioHang> danhSachDong, List<String> canhBao) {
        this.danhSachDong = danhSachDong;
        this.canhBao = canhBao;
    }

    public List<DongGioHang> getDanhSachDong() {
        return danhSachDong;
    }

    public List<String> getCanhBao() {
        return canhBao;
    }

    public long getTongTien() {
        return danhSachDong.stream().mapToLong(DongGioHang::getThanhTien).sum();
    }

    public int getTongSoLuong() {
        return danhSachDong.stream().mapToInt(DongGioHang::getSoLuong).sum();
    }

    public boolean isTrong() {
        return danhSachDong.isEmpty();
    }
}
