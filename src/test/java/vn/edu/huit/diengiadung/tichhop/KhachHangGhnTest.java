package vn.edu.huit.diengiadung.tichhop;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class KhachHangGhnTest {

    private static final String URL = "https://dev-online-gateway.ghn.vn";
    private static final String TINH = URL + "/shiip/public-api/v3/master-data/province/all?offset=0&limit=200";
    private static final String PHUONG = URL + "/shiip/public-api/v3/master-data/ward/all-by-province-id?offset=0&limit=200&province_id=202";

    private MockRestServiceServer mayGia;
    private KhachHangGhn ghn;
    private final ObjectMapper doiJson = new ObjectMapper();

    @BeforeEach
    void chuanBi() {
        CauHinhGhn cauHinh = new CauHinhGhn(true, URL, "token-thu", "12345", "", 3000, 40, "CHOXEMHANGKHONGTHU");
        RestClient.Builder khuon = RestClient.builder();
        mayGia = MockRestServiceServer.bindTo(khuon).build();
        ghn = new KhachHangGhn(cauHinh, khuon, true);
    }

    @Test
    void timDiaChiKhopTenCoTienToVaKhongDau() {
        moDanhMuc();
        KhachHangGhn.DiaChiGhn dc = ghn.timDiaChi("Thành phố Hồ Chí Minh", "Phường Sài Gòn");
        assertEquals("Hồ Chí Minh", dc.tenTinh());
        assertEquals("Phường Sài Gòn", dc.tenPhuong());
        assertEquals(26734, dc.idPhuong());
        mayGia.verify();
    }

    @Test
    void taoVanDonGuiDiaChiMoiHaiCapVaCodDungTien() throws Exception {
        moDanhMuc();
        mayGia.expect(requestTo(URL + "/shiip/public-api/v2/shipping-order/create"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("Token", "token-thu"))
                .andExpect(header("ShopId", "12345"))
                .andExpect(yeuCau -> {
                    String chu = ((org.springframework.mock.http.client.MockClientHttpRequest) yeuCau).getBodyAsString(StandardCharsets.UTF_8);
                    JsonNode than = doiJson.readTree(chu);
                    assertTrue(than.path("is_new_to_address").asBoolean());
                    assertEquals("Hồ Chí Minh", than.path("to_province_name").asText());
                    assertEquals("Phường Sài Gòn", than.path("to_ward_name").asText());
                    assertEquals(3_000_000, than.path("cod_amount").asLong());
                    assertEquals(6000, than.path("weight").asInt());
                    assertEquals(2, than.path("service_type_id").asInt());
                    assertEquals("BN000007", than.path("client_order_code").asText());
                    assertEquals(2, than.path("items").get(0).path("quantity").asInt());
                })
                .andRespond(withSuccess("{\"code\":200,\"message\":\"Success\",\"data\":{\"order_code\":\"LADFYR\","
                        + "\"total_fee\":20900,\"expected_delivery_time\":\"2026-07-15T16:59:59Z\"}}", MediaType.APPLICATION_JSON));

        KhachHangGhn.KetQuaVanDon kq = ghn.taoVanDon(donMau(), "BN000007");
        assertEquals("LADFYR", kq.maVanDon());
        assertEquals(20900, kq.phi());
        assertEquals(LocalDateTime.of(2026, 7, 15, 23, 59, 59), kq.ngayGiaoDuKien());
        mayGia.verify();
    }

    @Test
    void loiGhnDuocChuyenThanhThongBaoDeHieu() {
        moDanhMuc();
        mayGia.expect(requestTo(URL + "/shiip/public-api/v2/shipping-order/preview"))
                .andRespond(withStatus(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                        .body("{\"code\":400,\"message\":\"Sai thông tin Required Note\",\"data\":null,"
                                + "\"code_message\":\"USER_ERR_COMMON\",\"code_message_value\":\"Sai thông tin đầu vào.\"}"));
        LoiNghiepVu loi = assertThrows(LoiNghiepVu.class, () -> ghn.xemTruoc(donMau()));
        assertTrue(loi.getMessage().contains("Sai thông tin Required Note"), loi.getMessage());
    }

    @Test
    void khongCoPhuongTrongDanhMucThiBaoRo() {
        moDanhMuc();
        LoiNghiepVu loi = assertThrows(LoiNghiepVu.class, () -> ghn.timDiaChi("Hồ Chí Minh", "Phường Không Có"));
        assertTrue(loi.getMessage().contains("Phường Không Có"));
    }

    @Test
    void apDungTrangThaiChuyenDonTheoGhn() {
        DonHang don = new DonHang();
        don.setTrangThai(DonHang.DA_XAC_NHAN);
        VanChuyenGhn.apDungTrangThai(don, "ready_to_pick");
        assertEquals(DonHang.DA_XAC_NHAN, don.getTrangThai());
        VanChuyenGhn.apDungTrangThai(don, "picked");
        assertEquals(DonHang.DANG_GIAO, don.getTrangThai());
        VanChuyenGhn.apDungTrangThai(don, "delivered");
        assertEquals(DonHang.HOAN_THANH, don.getTrangThai());
        assertEquals("Giao thành công", don.getTenTrangThaiVanChuyen());
    }

    private void moDanhMuc() {
        mayGia.expect(requestTo(TINH)).andRespond(withSuccess("{\"code\":200,\"data\":["
                + "{\"_id\":201,\"name\":\"Hà Nội\",\"extension_names\":[\"Thành phố Hà Nội\"],\"status\":1},"
                + "{\"_id\":202,\"name\":\"Hồ Chí Minh\",\"extension_names\":[\"TP HCM\"],\"status\":1}]}", MediaType.APPLICATION_JSON));
        mayGia.expect(requestTo(PHUONG)).andRespond(withSuccess("{\"code\":200,\"data\":["
                + "{\"_id\":26734,\"name\":\"Phường Sài Gòn\",\"extension_names\":[],\"status\":1},"
                + "{\"_id\":26735,\"name\":\"Phường Bến Thành\",\"extension_names\":[],\"status\":1}]}", MediaType.APPLICATION_JSON));
    }

    private static DonHang donMau() {
        DonHang don = new DonHang();
        don.setId(7L);
        don.setTrangThai(DonHang.DA_XAC_NHAN);
        don.setPhuongThucThanhToan(DonHang.THANH_TOAN_KHI_NHAN);
        don.setTenNguoiNhan("Nguyễn Văn A");
        don.setSoDienThoaiNhan("0909000111");
        don.setTinhThanh("Thành phố Hồ Chí Minh");
        don.setPhuongXa("Phường Sài Gòn");
        don.setDiaChiCuThe("12 Lê Lợi");
        don.setTongTien(3_000_000L);
        SanPham sp = new SanPham();
        sp.setTenSanPham("Bếp từ đôi");
        sp.setMaSanPham("BT-01");
        ChiTietDonHang ct = new ChiTietDonHang();
        ct.setSanPham(sp);
        ct.setSoLuong(2);
        ct.setDonGia(1_500_000L);
        don.themChiTiet(ct);
        return don;
    }
}
