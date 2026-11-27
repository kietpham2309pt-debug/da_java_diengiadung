package vn.edu.huit.diengiadung.tichhop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.io.IOException;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KhachHangGhn {

    private static final String GOC = "/shiip/public-api";
    private static final int TRANG = 200;
    private static final int KHOI_LUONG_TOI_DA = 50_000;
    private static final long BAO_HIEM_TOI_DA = 5_000_000L;
    private static final ZoneId GIO_VN = ZoneId.of("Asia/Ho_Chi_Minh");

    public record DonViHanhChinh(int id, String ten, List<String> tenKhac) {
    }

    public record DiaChiGhn(String tenTinh, String tenPhuong, int idPhuong) {
    }

    public record KetQuaVanDon(String maVanDon, long phi, LocalDateTime ngayGiaoDuKien) {
    }

    private final CauHinhGhn cauHinh;
    private final RestClient may;
    private final ObjectMapper doiJson = new ObjectMapper();
    private volatile List<DonViHanhChinh> boNhoTinh;
    private final Map<Integer, List<DonViHanhChinh>> boNhoPhuong = new ConcurrentHashMap<>();

    @Autowired
    public KhachHangGhn(CauHinhGhn cauHinh, RestClient.Builder khuon) {
        this(cauHinh, khuon.requestFactory(nhaMayCoHanGio()), true);
    }

    KhachHangGhn(CauHinhGhn cauHinh, RestClient.Builder khuon, boolean daCauHinh) {
        this.cauHinh = cauHinh;
        this.may = khuon.baseUrl(cauHinh.getUrl())
                .defaultHeader("Token", cauHinh.getToken())
                .defaultHeader("ShopId", cauHinh.getShopId())
                .build();
    }

    private static SimpleClientHttpRequestFactory nhaMayCoHanGio() {
        SimpleClientHttpRequestFactory nhaMay = new SimpleClientHttpRequestFactory();
        nhaMay.setConnectTimeout(10_000);
        nhaMay.setReadTimeout(20_000);
        return nhaMay;
    }

    public List<DonViHanhChinh> danhSachTinh() {
        List<DonViHanhChinh> ds = boNhoTinh;
        if (ds == null) {
            ds = docPhanTrang(GOC + "/v3/master-data/province/all", null);
            boNhoTinh = ds;
        }
        return ds;
    }

    public List<DonViHanhChinh> danhSachPhuong(int idTinh) {
        return boNhoPhuong.computeIfAbsent(idTinh,
                id -> docPhanTrang(GOC + "/v3/master-data/ward/all-by-province-id", id));
    }

    public DiaChiGhn timDiaChi(String tinh, String phuong) {
        DonViHanhChinh t = timTheoTen(danhSachTinh(), tinh, "thanh pho ", "tinh ");
        if (t == null) {
            throw new LoiNghiepVu("GHN không có tỉnh/thành \"" + tinh + "\" trong danh mục địa chỉ mới");
        }
        DonViHanhChinh p = timTheoTen(danhSachPhuong(t.id()), phuong, "phuong ", "xa ", "dac khu ", "thi tran ");
        if (p == null) {
            throw new LoiNghiepVu("GHN không có \"" + phuong + "\" thuộc " + t.ten() + " trong danh mục địa chỉ mới");
        }
        return new DiaChiGhn(t.ten(), p.ten(), p.id());
    }

    public Map<String, Object> taoNoiDungDon(DonHang don, String maDonGuiGhn) {
        DiaChiGhn diaChi = timDiaChi(don.getTinhThanh(), don.getPhuongXa());
        int moiSanPham = Math.max(1, cauHinh.getKhoiLuongMoiSanPham());
        int canh = Math.max(1, Math.min(200, cauHinh.getKichThuocGoi()));
        List<Map<String, Object>> hang = new ArrayList<>();
        int tongKhoiLuong = 0;
        for (ChiTietDonHang ct : don.getDanhSachChiTiet()) {
            Map<String, Object> mon = new LinkedHashMap<>();
            String ten = ct.getSanPham() == null ? "Sản phẩm" : ct.getSanPham().getTenSanPham();
            mon.put("name", ten.length() > 500 ? ten.substring(0, 500) : ten);
            if (ct.getSanPham() != null) {
                mon.put("code", ct.getSanPham().getMaSanPham());
            }
            mon.put("quantity", ct.getSoLuong());
            mon.put("price", ct.getDonGia() == null ? 0 : ct.getDonGia());
            mon.put("length", canh);
            mon.put("width", canh);
            mon.put("height", canh);
            mon.put("weight", moiSanPham);
            hang.add(mon);
            tongKhoiLuong += moiSanPham * ct.getSoLuong();
        }
        if (tongKhoiLuong > KHOI_LUONG_TOI_DA) {
            throw new LoiNghiepVu("Đơn nặng khoảng " + tongKhoiLuong / 1000 + " kg, vượt giới hạn 50 kg của GHN cho một vận đơn");
        }
        long tong = don.getTongTien() == null ? 0 : don.getTongTien();
        Map<String, Object> than = new LinkedHashMap<>();
        than.put("payment_type_id", 1);
        than.put("required_note", cauHinh.getGhiChuBatBuoc());
        than.put("client_order_code", maDonGuiGhn);
        than.put("to_name", don.getTenNguoiNhan());
        than.put("to_phone", don.getSoDienThoaiNhan());
        than.put("to_address", diaChiDayDu(don));
        than.put("is_new_to_address", true);
        than.put("to_ward_name", diaChi.tenPhuong());
        than.put("to_province_name", diaChi.tenTinh());
        than.put("cod_amount", don.isThanhToanTruoc() ? 0 : tong);
        than.put("insurance_value", Math.min(tong, BAO_HIEM_TOI_DA));
        than.put("content", "Đơn hàng " + don.getMaDon() + " - Bep Nè");
        if (don.getGhiChu() != null && !don.getGhiChu().isBlank()) {
            than.put("note", don.getGhiChu());
        }
        than.put("weight", Math.max(1, tongKhoiLuong));
        than.put("length", canh);
        than.put("width", canh);
        than.put("height", canh);
        than.put("service_type_id", tongKhoiLuong >= 20_000 ? 5 : 2);
        than.put("items", hang);
        return than;
    }

    public KetQuaVanDon xemTruoc(DonHang don) {
        JsonNode dl = goi(GOC + "/v2/shipping-order/preview", taoNoiDungDon(don, don.getMaDon()));
        return docKetQua(dl);
    }

    public KetQuaVanDon taoVanDon(DonHang don, String maDonGuiGhn) {
        JsonNode dl = goi(GOC + "/v2/shipping-order/create", taoNoiDungDon(don, maDonGuiGhn));
        KetQuaVanDon kq = docKetQua(dl);
        if (kq.maVanDon() == null || kq.maVanDon().isBlank()) {
            throw new LoiNghiepVu("GHN không trả về mã vận đơn");
        }
        return kq;
    }

    public void huyVanDon(String maVanDon) {
        Map<String, Object> than = new LinkedHashMap<>();
        than.put("order_codes", List.of(maVanDon));
        than.put("reason_code", "GHN-CANCEL-OTHER");
        than.put("reason", "Cửa hàng huỷ vận đơn");
        JsonNode dl = goi(GOC + "/v2/switch-status/cancel", than);
        JsonNode dong = dl.path(0);
        if (!dong.path("result").asBoolean(false)) {
            throw new LoiNghiepVu("GHN không cho huỷ vận đơn " + maVanDon + ": " + dong.path("message").asText("đơn đã được lấy"));
        }
    }

    public String trangThaiVanDon(String maVanDon) {
        JsonNode dl = xuLyTraVe(() -> may.get()
                .uri(u -> u.path(GOC + "/v2/shipping-order/detail").queryParam("order_code", maVanDon).build())
                .retrieve()
                .onStatus(HttpStatusCode::isError, (yeuCau, phanHoi) -> { })
                .body(String.class));
        String trangThai = dl.path("status").asText("");
        if (trangThai.isBlank()) {
            throw new LoiNghiepVu("GHN không trả về trạng thái của vận đơn " + maVanDon);
        }
        return trangThai;
    }

    private KetQuaVanDon docKetQua(JsonNode dl) {
        String ma = dl.path("order_code").asText(null);
        long phi = dl.path("total_fee").asLong(0);
        LocalDateTime ngay = null;
        String chuNgay = dl.path("expected_delivery_time").asText("");
        if (!chuNgay.isBlank()) {
            try {
                ngay = OffsetDateTime.parse(chuNgay).atZoneSameInstant(GIO_VN).toLocalDateTime();
            } catch (Exception e) {
                ngay = null;
            }
        }
        return new KetQuaVanDon(ma, phi, ngay);
    }

    private List<DonViHanhChinh> docPhanTrang(String duongDan, Integer idTinh) {
        List<DonViHanhChinh> ds = new ArrayList<>();
        for (int batDau = 0; batDau < 5_000; batDau += TRANG) {
            int tu = batDau;
            JsonNode dl = xuLyTraVe(() -> may.get()
                    .uri(u -> {
                        u.path(duongDan).queryParam("offset", tu).queryParam("limit", TRANG);
                        if (idTinh != null) {
                            u.queryParam("province_id", idTinh);
                        }
                        return u.build();
                    })
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (yeuCau, phanHoi) -> { })
                    .body(String.class));
            if (!dl.isArray() || dl.isEmpty()) {
                break;
            }
            for (JsonNode n : dl) {
                if (n.path("status").asInt(1) != 1) {
                    continue;
                }
                List<String> khac = new ArrayList<>();
                n.path("extension_names").forEach(x -> khac.add(x.asText()));
                ds.add(new DonViHanhChinh(n.path("_id").asInt(), n.path("name").asText(), khac));
            }
            if (dl.size() < TRANG) {
                break;
            }
        }
        if (ds.isEmpty()) {
            throw new LoiNghiepVu("GHN trả về danh mục địa chỉ rỗng, kiểm tra lại Token và ShopId");
        }
        return ds;
    }

    private JsonNode goi(String duongDan, Object than) {
        return xuLyTraVe(() -> may.post()
                .uri(duongDan)
                .contentType(MediaType.APPLICATION_JSON)
                .body(than)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (yeuCau, phanHoi) -> { })
                .body(String.class));
    }

    private interface LoiGoi {
        String chay();
    }

    private JsonNode xuLyTraVe(LoiGoi loiGoi) {
        String chu;
        try {
            chu = loiGoi.chay();
        } catch (ResourceAccessException e) {
            throw new LoiNghiepVu("Không kết nối được tới GHN (" + cauHinh.getUrl() + "): " + e.getMessage());
        }
        JsonNode goc;
        try {
            goc = doiJson.readTree(chu == null ? "" : chu);
        } catch (IOException e) {
            throw new LoiNghiepVu("GHN trả về dữ liệu không đọc được");
        }
        if (goc == null || goc.isMissingNode()) {
            throw new LoiNghiepVu("GHN không trả về dữ liệu");
        }
        int ma = goc.path("code").asInt(0);
        if (ma != 200) {
            String loi = goc.path("message").asText("không rõ lỗi");
            String giaiThich = goc.path("code_message_value").asText("");
            throw new LoiNghiepVu("GHN báo lỗi: " + loi + (giaiThich.isBlank() ? "" : " (" + giaiThich + ")"));
        }
        return goc.path("data");
    }

    private static String diaChiDayDu(DonHang don) {
        StringBuilder sb = new StringBuilder();
        for (String phan : new String[]{don.getDiaChiCuThe(), don.getPhuongXa(), don.getTinhThanh()}) {
            if (phan != null && !phan.isBlank()) {
                if (!sb.isEmpty()) {
                    sb.append(", ");
                }
                sb.append(phan.trim());
            }
        }
        return sb.isEmpty() ? don.getDiaChiGiao() : sb.toString();
    }

    static DonViHanhChinh timTheoTen(List<DonViHanhChinh> ds, String ten, String... tienTo) {
        if (ten == null || ten.isBlank()) {
            return null;
        }
        String can = chuanHoa(ten);
        for (int lan = 0; lan < 2; lan++) {
            String mau = lan == 0 ? can : boTienTo(can, tienTo);
            for (DonViHanhChinh dv : ds) {
                List<String> tatCa = new ArrayList<>(dv.tenKhac());
                tatCa.add(0, dv.ten());
                for (String t : tatCa) {
                    String c = lan == 0 ? chuanHoa(t) : boTienTo(chuanHoa(t), tienTo);
                    if (c.equals(mau)) {
                        return dv;
                    }
                }
            }
        }
        return null;
    }

    static String chuanHoa(String s) {
        String khongDau = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "")
                .replace('đ', 'd').replace('Đ', 'D').toLowerCase(Locale.ROOT);
        return khongDau.replaceAll("[^a-z0-9]+", " ").trim();
    }

    private static String boTienTo(String s, String... tienTo) {
        for (String t : tienTo) {
            if (s.startsWith(t)) {
                return s.substring(t.length()).trim();
            }
        }
        return s;
    }
}
