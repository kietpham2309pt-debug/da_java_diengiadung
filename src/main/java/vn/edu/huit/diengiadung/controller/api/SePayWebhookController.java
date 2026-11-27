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
import vn.edu.huit.diengiadung.tichhop.CauHinhSePay;
import vn.edu.huit.diengiadung.tichhop.ThanhToanSePay;

import java.util.Map;

@RestController
@RequestMapping("/api/thanh-toan")
public class SePayWebhookController {

    private static final Logger log = LoggerFactory.getLogger(SePayWebhookController.class);

    private final CauHinhSePay cauHinh;
    private final ThanhToanSePay thanhToanSePay;
    private final ObjectMapper doiJson;

    public SePayWebhookController(CauHinhSePay cauHinh, ThanhToanSePay thanhToanSePay, ObjectMapper doiJson) {
        this.cauHinh = cauHinh;
        this.thanhToanSePay = thanhToanSePay;
        this.doiJson = doiJson;
    }

    @PostMapping("/sepay")
    public ResponseEntity<Map<String, Object>> nhanWebhook(@RequestBody(required = false) byte[] noiDung,
                                                           @RequestHeader(value = "Authorization", required = false) String authorization,
                                                           @RequestHeader(value = "X-SePay-Signature", required = false) String chuKy,
                                                           @RequestHeader(value = "X-SePay-Timestamp", required = false) String thoiGian) {
        if (!cauHinh.isBat()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("success", false, "message", "SePay chưa được bật (sepay.bat=false)"));
        }
        if (!cauHinh.isDaCoKhoa()) {
            log.warn("Nhận webhook SePay nhưng chưa cấu hình sepay.api-key hoặc sepay.khoa-hmac");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(Map.of("success", false, "message", "Chưa cấu hình khoá xác thực"));
        }
        byte[] than = noiDung == null ? new byte[0] : noiDung;
        if (!thanhToanSePay.xacThuc(authorization, chuKy, thoiGian, than)) {
            log.warn("Từ chối webhook SePay vì sai khoá xác thực");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Sai khoá xác thực"));
        }
        JsonNode duLieu;
        try {
            duLieu = doiJson.readTree(than);
        } catch (Exception e) {
            duLieu = null;
        }
        if (duLieu == null || !duLieu.isObject()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Dữ liệu không phải JSON"));
        }
        try {
            thanhToanSePay.xuLy(duLieu);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
        return ResponseEntity.ok(Map.of("success", true));
    }
}
