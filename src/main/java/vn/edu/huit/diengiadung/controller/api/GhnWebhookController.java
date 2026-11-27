package vn.edu.huit.diengiadung.controller.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.huit.diengiadung.tichhop.CauHinhGhn;
import vn.edu.huit.diengiadung.tichhop.VanChuyenGhn;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

@RestController
@RequestMapping("/api/van-chuyen")
public class GhnWebhookController {

    public static final String TEN_KHOA = "X-BepNe-Key";

    private static final Logger log = LoggerFactory.getLogger(GhnWebhookController.class);

    private final CauHinhGhn cauHinh;
    private final VanChuyenGhn vanChuyenGhn;
    private final ObjectMapper doiJson;

    public GhnWebhookController(CauHinhGhn cauHinh, VanChuyenGhn vanChuyenGhn, ObjectMapper doiJson) {
        this.cauHinh = cauHinh;
        this.vanChuyenGhn = vanChuyenGhn;
        this.doiJson = doiJson;
    }

    @PostMapping("/ghn")
    public ResponseEntity<Map<String, Object>> nhanWebhook(@RequestBody(required = false) byte[] noiDung,
                                                           @RequestHeader(value = TEN_KHOA, required = false) String khoa) {
        if (!cauHinh.isBat()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "GHN chưa được bật"));
        }
        if (!cauHinh.getKhoaWebhook().isEmpty() && (khoa == null || !MessageDigest.isEqual(
                khoa.trim().getBytes(StandardCharsets.UTF_8), cauHinh.getKhoaWebhook().getBytes(StandardCharsets.UTF_8)))) {
            log.warn("Từ chối webhook GHN vì sai header {}", TEN_KHOA);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", "Sai khoá webhook"));
        }
        JsonNode duLieu;
        try {
            duLieu = doiJson.readTree(noiDung == null ? new byte[0] : noiDung);
        } catch (Exception e) {
            duLieu = null;
        }
        if (duLieu == null || !duLieu.isObject()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Dữ liệu không phải JSON"));
        }
        String maVanDon = duLieu.path("OrderCode").asText("");
        String trangThai = duLieu.path("Status").asText("");
        String loai = duLieu.path("Type").asText("");
        boolean coDon = vanChuyenGhn.nhanWebhook(maVanDon, trangThai);
        log.info("Webhook GHN {} vận đơn {} trạng thái {} -> {}", loai, maVanDon, trangThai,
                coDon ? "đã cập nhật" : "không khớp đơn nào");
        return ResponseEntity.ok(Map.of("success", true, "capNhat", coDon));
    }
}
