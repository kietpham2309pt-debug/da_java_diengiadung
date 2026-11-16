package vn.edu.huit.diengiadung.service;

import org.springframework.stereotype.Component;
import vn.edu.huit.diengiadung.repository.DanhGiaRepository;

import java.util.HashMap;
import java.util.Map;

@Component("saoSanPham")
public class SaoSanPham {

    public record DiemSao(double trungBinh, long soLuong) {

        public double getTrungBinh() {
            return trungBinh;
        }

        public long getSoLuong() {
            return soLuong;
        }

        public int getPhanTram() {
            return (int) Math.round(trungBinh * 20);
        }

        public String getHienThi() {
            return String.format("%.1f", trungBinh).replace('.', ',');
        }
    }

    private final DanhGiaRepository duLieuDanhGia;
    private volatile Map<Long, DiemSao> boNho;

    public SaoSanPham(DanhGiaRepository duLieuDanhGia) {
        this.duLieuDanhGia = duLieuDanhGia;
    }

    public DiemSao cua(Long idSanPham) {
        Map<Long, DiemSao> bang = boNho;
        if (bang == null) {
            bang = new HashMap<>();
            for (Object[] dong : duLieuDanhGia.tongHopTheoSanPham()) {
                bang.put(((Number) dong[0]).longValue(),
                        new DiemSao(Math.round(((Number) dong[1]).doubleValue() * 10) / 10.0, ((Number) dong[2]).longValue()));
            }
            boNho = bang;
        }
        return bang.get(idSanPham);
    }

    public void lamMoi() {
        boNho = null;
    }
}
