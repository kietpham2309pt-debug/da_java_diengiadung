package vn.edu.huit.diengiadung.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class GioHang implements Serializable {

    private final Map<Long, Integer> soLuongTheoSanPham = new LinkedHashMap<>();

    public int laySoLuong(Long idSanPham) {
        return soLuongTheoSanPham.getOrDefault(idSanPham, 0);
    }

    public void datSoLuong(Long idSanPham, int soLuong) {
        if (soLuong <= 0) {
            soLuongTheoSanPham.remove(idSanPham);
        } else {
            soLuongTheoSanPham.put(idSanPham, soLuong);
        }
    }

    public void xoa(Long idSanPham) {
        soLuongTheoSanPham.remove(idSanPham);
    }

    public void xoaHet() {
        soLuongTheoSanPham.clear();
    }

    public Map<Long, Integer> getSoLuongTheoSanPham() {
        return soLuongTheoSanPham;
    }

    public int getTongSoLuong() {
        return soLuongTheoSanPham.values().stream().mapToInt(Integer::intValue).sum();
    }

    public boolean isTrong() {
        return soLuongTheoSanPham.isEmpty();
    }
}
