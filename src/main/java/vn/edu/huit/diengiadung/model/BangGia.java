package vn.edu.huit.diengiadung.model;

import vn.edu.huit.diengiadung.entity.KhuyenMai;
import vn.edu.huit.diengiadung.entity.MaGiamGia;

public class BangGia {

    private final long tamTinh;
    private final KhuyenMai khuyenMai;
    private final long tienKhuyenMai;
    private final MaGiamGia maGiamGia;
    private final long tienGiamMa;
    private final String loiMa;

    public BangGia(long tamTinh, KhuyenMai khuyenMai, long tienKhuyenMai,
                   MaGiamGia maGiamGia, long tienGiamMa, String loiMa) {
        this.tamTinh = tamTinh;
        this.khuyenMai = khuyenMai;
        this.tienKhuyenMai = tienKhuyenMai;
        this.maGiamGia = maGiamGia;
        this.tienGiamMa = tienGiamMa;
        this.loiMa = loiMa;
    }

    public long getTamTinh() {
        return tamTinh;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public long getTienKhuyenMai() {
        return tienKhuyenMai;
    }

    public MaGiamGia getMaGiamGia() {
        return maGiamGia;
    }

    public long getTienGiamMa() {
        return tienGiamMa;
    }

    public String getLoiMa() {
        return loiMa;
    }

    public long getSauUuDai() {
        return tamTinh - tienKhuyenMai - tienGiamMa;
    }
}
