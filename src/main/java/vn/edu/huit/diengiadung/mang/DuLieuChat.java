package vn.edu.huit.diengiadung.mang;

import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.entity.TinNhanChat;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DuLieuChat {

    private static final DateTimeFormatter THOI_GIAN = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private DuLieuChat() {
    }

    public static Map<String, Object> tin(TinNhanChat tin) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", tin.getId());
        m.put("nguoiGui", tin.getNguoiGui());
        m.put("tenNguoiGui", tin.getTenNguoiGui());
        m.put("noiDung", tin.getNoiDung());
        m.put("thoiGian", tin.getThoiGian().format(THOI_GIAN));
        m.put("canThongTin", tin.isCanThongTin());
        List<Map<String, String>> goiY = new ArrayList<>();
        if (tin.getGoiY() != null) {
            for (String muc : tin.getGoiY().split("\\|")) {
                String[] cap = muc.split(":", 2);
                if (cap.length == 2) {
                    goiY.add(Map.of("ma", cap[0], "nhan", cap[1]));
                }
            }
        }
        m.put("goiY", goiY);
        return m;
    }

    public static List<Map<String, Object>> danhSachTin(List<TinNhanChat> ds) {
        return ds.stream().map(DuLieuChat::tin).toList();
    }

    public static Map<String, Object> phienChoKhach(PhienChat phien) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("phienId", phien.getId());
        m.put("trangThai", phien.getTrangThai());
        m.put("tenNhanVien", phien.getTenNhanVien());
        m.put("soChuaDoc", phien.getSoChuaDocKhach());
        m.put("canThongTin", phien.laKhachVangLai() && phien.getSoDienThoai() == null);
        return m;
    }

    public static Map<String, Object> phienChoQuanTri(PhienChat phien, TinNhanChat tinCuoi, boolean dangTrucTuyen) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", phien.getId());
        m.put("ten", phien.getTenHienThi());
        m.put("soDienThoai", phien.getSoDienThoaiLienHe());
        m.put("khachHangId", phien.getKhachHang() == null ? null : phien.getKhachHang().getId());
        m.put("vangLai", phien.laKhachVangLai());
        m.put("trangThai", phien.getTrangThai());
        m.put("tenNhanVien", phien.getTenNhanVien());
        m.put("soChuaDoc", phien.getSoChuaDocQuanTri());
        m.put("dangTrucTuyen", dangTrucTuyen);
        m.put("capNhatLuc", phien.getCapNhatLuc().format(THOI_GIAN));
        if (tinCuoi != null) {
            m.put("tinCuoi", tinCuoi.getNoiDung());
            m.put("nguoiGuiCuoi", tinCuoi.getNguoiGui());
        }
        return m;
    }

    public static Map<String, Object> suKien(String loai, Object... cap) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("loai", loai);
        for (int i = 0; i + 1 < cap.length; i += 2) {
            m.put(String.valueOf(cap[i]), cap[i + 1]);
        }
        return m;
    }
}
