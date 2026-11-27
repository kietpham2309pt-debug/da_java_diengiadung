package vn.edu.huit.diengiadung.tichhop;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.GiaoDichSePay;
import vn.edu.huit.diengiadung.repository.GiaoDichSePayRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ThanhToanSePay {

    private static final Logger log = LoggerFactory.getLogger(ThanhToanSePay.class);
    private static final Pattern MA_DON = Pattern.compile("BN\\s?(\\d{6,9})(?!\\d)", Pattern.CASE_INSENSITIVE);

    private final CauHinhSePay cauHinh;
    private final GiaoDichSePayRepository giaoDichRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ThanhToanSePay(CauHinhSePay cauHinh, GiaoDichSePayRepository giaoDichRepository) {
        this.cauHinh = cauHinh;
        this.giaoDichRepository = giaoDichRepository;
    }

    public boolean xacThuc(String authorization, String chuKy, String thoiGian, byte[] noiDung) {
        if (!cauHinh.getApiKey().isEmpty() && authorization != null
                && bangNhau(authorization.trim(), "Apikey " + cauHinh.getApiKey())) {
            return true;
        }
        if (!cauHinh.getKhoaHmac().isEmpty() && chuKy != null && thoiGian != null) {
            return dungChuKyHmac(chuKy.trim(), thoiGian.trim(), noiDung);
        }
        return false;
    }

    boolean dungChuKyHmac(String chuKy, String thoiGian, byte[] noiDung) {
        long giay;
        try {
            giay = Long.parseLong(thoiGian);
        } catch (NumberFormatException e) {
            return false;
        }
        if (Math.abs(Instant.now().getEpochSecond() - giay) > cauHinh.getLechGioToiDaGiay()) {
            return false;
        }
        String mongDoi = "sha256=" + kyHmac(cauHinh.getKhoaHmac(), thoiGian, noiDung);
        return bangNhau(chuKy.toLowerCase(), mongDoi);
    }

    public static String kyHmac(String khoa, String thoiGian, byte[] noiDung) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(khoa.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            mac.update((thoiGian + ".").getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(mac.doFinal(noiDung));
        } catch (Exception e) {
            throw new IllegalStateException("Không tạo được chữ ký HMAC", e);
        }
    }

    private static boolean bangNhau(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(StandardCharsets.UTF_8), b.getBytes(StandardCharsets.UTF_8));
    }

    public static Long timIdDon(String maThanhToan, String noiDung) {
        for (String nguon : new String[]{maThanhToan, noiDung}) {
            if (nguon == null || nguon.isBlank()) {
                continue;
            }
            Matcher m = MA_DON.matcher(nguon);
            if (m.find()) {
                return Long.parseLong(m.group(1));
            }
        }
        return null;
    }

    @Transactional
    public GiaoDichSePay xuLy(JsonNode duLieu) {
        long maGiaoDich = duLieu.path("id").asLong(0);
        if (maGiaoDich <= 0) {
            throw new IllegalArgumentException("Thiếu id giao dịch SePay");
        }
        if (giaoDichRepository.existsByMaGiaoDich(maGiaoDich)) {
            log.info("SePay gửi lại giao dịch {} đã xử lý, bỏ qua", maGiaoDich);
            return null;
        }
        GiaoDichSePay gd = new GiaoDichSePay();
        gd.setMaGiaoDich(maGiaoDich);
        gd.setNganHang(chu(duLieu, "gateway"));
        gd.setSoTaiKhoan(chu(duLieu, "accountNumber"));
        gd.setLoai(chu(duLieu, "transferType"));
        gd.setSoTien(duLieu.path("transferAmount").asLong(0));
        gd.setNoiDung(chu(duLieu, "content"));
        gd.setMaThanhToan(chu(duLieu, "code"));
        gd.setMaThamChieu(chu(duLieu, "referenceCode"));
        gd.setThoiGianGiaoDich(chu(duLieu, "transactionDate"));
        gd.setKetQua(doiChieu(gd));
        giaoDichRepository.save(gd);
        log.info("SePay giao dịch {}: {} đ, nội dung \"{}\" -> {}", maGiaoDich, gd.getSoTien(), gd.getNoiDung(), gd.getKetQua());
        return gd;
    }

    private String doiChieu(GiaoDichSePay gd) {
        if (!"in".equalsIgnoreCase(gd.getLoai())) {
            return "Bỏ qua: giao dịch tiền ra";
        }
        Long idDon = timIdDon(gd.getMaThanhToan(), gd.getNoiDung());
        if (idDon == null) {
            return "Không tìm thấy mã đơn BNxxxxxx trong nội dung chuyển khoản";
        }
        DonHang don = entityManager.find(DonHang.class, idDon);
        if (don == null) {
            return "Không có đơn " + String.format("BN%06d", idDon);
        }
        entityManager.refresh(don, LockModeType.PESSIMISTIC_WRITE);
        gd.setDonHang(don);
        if (!don.isThanhToanTruoc()) {
            return "Đơn " + don.getMaDon() + " là COD, không cần chuyển khoản";
        }
        if (DonHang.DA_HUY.equals(don.getTrangThai())) {
            return "Đơn " + don.getMaDon() + " đã huỷ: cần hoàn tiền cho khách";
        }
        if (don.isDaThanhToan()) {
            return "Đơn " + don.getMaDon() + " đã thanh toán trước đó";
        }
        long canTra = don.getTongTien() == null ? 0 : don.getTongTien();
        if (gd.getSoTien() < canTra) {
            return "Chuyển thiếu " + (canTra - gd.getSoTien()) + " đ so với đơn " + don.getMaDon();
        }
        don.setDaThanhToan(true);
        gd.setDaGhiNhan(true);
        return gd.getSoTien() > canTra
                ? "Đã xác nhận thanh toán (khách chuyển dư " + (gd.getSoTien() - canTra) + " đ)"
                : "Đã xác nhận thanh toán";
    }

    private static String chu(JsonNode duLieu, String truong) {
        JsonNode n = duLieu.get(truong);
        return n == null || n.isNull() ? null : n.asText();
    }
}
