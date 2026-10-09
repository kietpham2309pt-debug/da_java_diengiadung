package vn.edu.huit.diengiadung.service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Gom các tham số trên đường dẫn /san-pham: loại, thương hiệu, mức giá, từ khoá, kiểu sắp xếp và trang.
 * Spring tự gán từng tham số vào thuộc tính cùng tên, ví dụ ?hang=1&hang=2 thành danh sách [1, 2].
 */
public class BoLocSanPham {

    private Long loai;
    private List<Long> hang = new ArrayList<>();
    private List<String> mucGia = new ArrayList<>();
    private String tuKhoa;
    private String sapXep;
    private Integer trang;

    public Long getLoai() {
        return loai;
    }

    public void setLoai(Long loai) {
        this.loai = loai;
    }

    public List<Long> getHang() {
        return hang;
    }

    public void setHang(List<Long> hang) {
        this.hang = new ArrayList<>();
        if (hang == null) {
            return;
        }
        for (Long idHang : hang) {
            if (idHang != null && !this.hang.contains(idHang)) {
                this.hang.add(idHang);
            }
        }
    }

    public List<String> getMucGia() {
        return mucGia;
    }

    public void setMucGia(List<String> mucGia) {
        this.mucGia = mucGia == null ? new ArrayList<>() : mucGia;
    }

    public String getTuKhoa() {
        return tuKhoa;
    }

    public void setTuKhoa(String tuKhoa) {
        this.tuKhoa = tuKhoa;
    }

    public String getSapXep() {
        return getKieuSapXep().getMa();
    }

    public void setSapXep(String sapXep) {
        this.sapXep = sapXep;
    }

    public Integer getTrang() {
        return trang;
    }

    public void setTrang(Integer trang) {
        this.trang = trang;
    }

    public String getTuKhoaDaGon() {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return null;
        }
        return tuKhoa.trim();
    }

    public KieuSapXep getKieuSapXep() {
        return KieuSapXep.timTheoMa(sapXep);
    }

    public int getTrangCanXem() {
        if (trang == null || trang < 1) {
            return 1;
        }
        return trang;
    }

    public List<MucGia> getDanhSachMucGia() {
        List<MucGia> danhSach = new ArrayList<>();
        for (String ma : mucGia) {
            MucGia muc = MucGia.timTheoMa(ma);
            if (muc != null && !danhSach.contains(muc)) {
                danhSach.add(muc);
            }
        }
        return danhSach;
    }

    public boolean coChonHang(Long idHang) {
        return hang.contains(idHang);
    }

    public boolean coChonMucGia(String maMuc) {
        return mucGia.contains(maMuc);
    }

    public boolean isDangLoc() {
        return !hang.isEmpty() || !getDanhSachMucGia().isEmpty();
    }

    /** Đường dẫn tới một trang kết quả, giữ nguyên mọi bộ lọc đang chọn. */
    public String duongDanTrang(int soTrang) {
        List<String> thamSo = new ArrayList<>();
        if (loai != null) {
            thamSo.add("loai=" + loai);
        }
        if (getTuKhoaDaGon() != null) {
            thamSo.add("tuKhoa=" + maHoa(getTuKhoaDaGon()));
        }
        for (Long idHang : hang) {
            thamSo.add("hang=" + idHang);
        }
        for (MucGia muc : getDanhSachMucGia()) {
            thamSo.add("mucGia=" + muc.getMa());
        }
        thamSo.add("sapXep=" + getSapXep());
        thamSo.add("trang=" + soTrang);
        return "/san-pham?" + String.join("&", thamSo);
    }

    private static String maHoa(String giaTri) {
        return URLEncoder.encode(giaTri, StandardCharsets.UTF_8);
    }
}
