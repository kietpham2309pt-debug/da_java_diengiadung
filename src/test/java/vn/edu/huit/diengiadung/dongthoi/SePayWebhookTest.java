package vn.edu.huit.diengiadung.dongthoi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import vn.edu.huit.diengiadung.dto.GioHang;
import vn.edu.huit.diengiadung.dto.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.GiaoDichSePayRepository;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.DiaChiHanhChinh;
import vn.edu.huit.diengiadung.tichhop.ThanhToanSePay;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnabledIf("vn.edu.huit.diengiadung.dongthoi.CoSoKiemThuSqlServer#coSqlServer")
@AutoConfigureMockMvc
@TestPropertySource(properties = {"sepay.bat=true", "sepay.api-key=khoa-thu-nghiem", "sepay.khoa-hmac=bi-mat-hmac"})
class SePayWebhookTest extends CoSoKiemThuSqlServer {

    private static final AtomicLong MA_GD = new AtomicLong(System.currentTimeMillis() % 1_000_000_000L);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DatHangService datHangService;

    @Autowired
    private DonHangRepository donHangRepository;

    @Autowired
    private GiaoDichSePayRepository giaoDichSePayRepository;

    @Autowired
    private DiaChiHanhChinh diaChiHanhChinh;

    @Test
    void chuyenDuTienThiDonTuDongDaThanhToanVaGuiLaiKhongXuLyLan2() throws Exception {
        DonHang don = taoDonChuyenKhoan();
        long ma = MA_GD.incrementAndGet();
        String than = webhook(ma, don.getTongTien(), "MBVCB.123 " + don.getMaDon() + " chuyen tien", null);

        mockMvc.perform(post("/api/thanh-toan/sepay").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Apikey khoa-thu-nghiem").content(than))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"success\":true}", true));
        assertTrue(donHangRepository.findById(don.getId()).orElseThrow().isDaThanhToan());

        mockMvc.perform(post("/api/thanh-toan/sepay").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Apikey khoa-thu-nghiem").content(than))
                .andExpect(status().isOk());
        assertEquals(1, giaoDichSePayRepository.findByDonHangIdOrderByNhanLucDesc(don.getId()).size());
    }

    @Test
    void chuyenThieuTienThiChuaXacNhan() throws Exception {
        DonHang don = taoDonChuyenKhoan();
        String than = webhook(MA_GD.incrementAndGet(), don.getTongTien() - 1000, "thanh toan", don.getMaDon());
        mockMvc.perform(post("/api/thanh-toan/sepay").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Apikey khoa-thu-nghiem").content(than))
                .andExpect(status().isOk());
        assertFalse(donHangRepository.findById(don.getId()).orElseThrow().isDaThanhToan());
        assertTrue(giaoDichSePayRepository.findByDonHangIdOrderByNhanLucDesc(don.getId()).get(0).getKetQua().startsWith("Chuyển thiếu"));
    }

    @Test
    void saiKhoaThiTuChoiVaKhongDoiDon() throws Exception {
        DonHang don = taoDonChuyenKhoan();
        String than = webhook(MA_GD.incrementAndGet(), don.getTongTien(), don.getMaDon(), null);
        mockMvc.perform(post("/api/thanh-toan/sepay").contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Apikey doan-bua").content(than))
                .andExpect(status().isUnauthorized());
        mockMvc.perform(post("/api/thanh-toan/sepay").contentType(MediaType.APPLICATION_JSON).content(than))
                .andExpect(status().isUnauthorized());
        assertFalse(donHangRepository.findById(don.getId()).orElseThrow().isDaThanhToan());
    }

    @Test
    void chuKyHmacDungThiChapNhan() throws Exception {
        DonHang don = taoDonChuyenKhoan();
        String than = webhook(MA_GD.incrementAndGet(), don.getTongTien(), don.getMaDon(), null);
        String thoiGian = String.valueOf(Instant.now().getEpochSecond());
        String chuKy = "sha256=" + ThanhToanSePay.kyHmac("bi-mat-hmac", thoiGian, than.getBytes(StandardCharsets.UTF_8));
        mockMvc.perform(post("/api/thanh-toan/sepay").contentType(MediaType.APPLICATION_JSON)
                        .header("X-SePay-Signature", chuKy).header("X-SePay-Timestamp", thoiGian).content(than))
                .andExpect(status().isOk());
        assertTrue(donHangRepository.findById(don.getId()).orElseThrow().isDaThanhToan());
    }

    private DonHang taoDonChuyenKhoan() {
        SanPham sp = taoSanPham(5, 1_500_000L);
        GioHang gio = new GioHang();
        gio.datSoLuong(sp.getId(), 1);
        String tinh = diaChiHanhChinh.danhSachTinhThanh().get(0);
        ThongTinGiaoHang tt = new ThongTinGiaoHang();
        tt.setTenNguoiNhan("Khách thử SePay");
        tt.setSoDienThoaiNhan("0909000111");
        tt.setTinhThanh(tinh);
        tt.setPhuongXa(diaChiHanhChinh.danhSachPhuongXa(tinh).get(0));
        tt.setDiaChiCuThe("1 Đường kiểm thử");
        tt.setPhuongThuc(DonHang.THANH_TOAN_TRUOC);
        tt.setDongY(true);
        return datHangService.datHang(null, gio, tt);
    }

    private static String webhook(long ma, long soTien, String noiDung, String maThanhToan) {
        return "{\"id\":" + ma + ",\"gateway\":\"MBBank\",\"transactionDate\":\"2026-11-27 10:00:00\","
                + "\"accountNumber\":\"0123499999\",\"subAccount\":null,"
                + "\"code\":" + (maThanhToan == null ? "null" : "\"" + maThanhToan + "\"") + ","
                + "\"content\":\"" + noiDung + "\",\"transferType\":\"in\",\"description\":\"\","
                + "\"transferAmount\":" + soTien + ",\"accumulated\":0,\"referenceCode\":\"SB" + ma + "\"}";
    }
}
