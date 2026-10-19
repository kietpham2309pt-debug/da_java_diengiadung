package vn.edu.huit.diengiadung.service;

import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

public class KetQuaPhanTrang<T> {

    private static final int SO_NUT_TRANG_TOI_DA = 5;

    private final List<T> danhSach;
    private final int trangHienTai;
    private final int soTrang;
    private final long tongSo;

    public KetQuaPhanTrang(Page<T> trangDuLieu) {
        this.danhSach = trangDuLieu.getContent();
        this.trangHienTai = trangDuLieu.getNumber() + 1;
        this.soTrang = trangDuLieu.getTotalPages();
        this.tongSo = trangDuLieu.getTotalElements();
    }

    public List<T> getDanhSach() {
        return danhSach;
    }

    public int getTrangHienTai() {
        return trangHienTai;
    }

    public int getSoTrang() {
        return soTrang;
    }

    public long getTongSo() {
        return tongSo;
    }

    public boolean isTrong() {
        return danhSach.isEmpty();
    }

    public boolean isCoTrangTruoc() {
        return trangHienTai > 1;
    }

    public boolean isCoTrangSau() {
        return trangHienTai < soTrang;
    }

    public List<Integer> getDanhSachSoTrang() {
        int batDau = Math.max(1, trangHienTai - SO_NUT_TRANG_TOI_DA / 2);
        int ketThuc = Math.min(soTrang, batDau + SO_NUT_TRANG_TOI_DA - 1);
        batDau = Math.max(1, ketThuc - SO_NUT_TRANG_TOI_DA + 1);

        List<Integer> danhSachSo = new ArrayList<>();
        for (int so = batDau; so <= ketThuc; so++) {
            danhSachSo.add(so);
        }
        return danhSachSo;
    }
}
