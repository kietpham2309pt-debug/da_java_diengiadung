package vn.edu.huit.diengiadung.tichhop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CauHinhSePay {

    private final boolean bat;
    private final String apiKey;
    private final String khoaHmac;
    private final long lechGioToiDaGiay;

    public CauHinhSePay(@Value("${sepay.bat:false}") boolean bat,
                        @Value("${sepay.api-key:}") String apiKey,
                        @Value("${sepay.khoa-hmac:}") String khoaHmac,
                        @Value("${sepay.lech-gio-toi-da-giay:300}") long lechGioToiDaGiay) {
        this.bat = bat;
        this.apiKey = apiKey == null ? "" : apiKey.trim();
        this.khoaHmac = khoaHmac == null ? "" : khoaHmac.trim();
        this.lechGioToiDaGiay = lechGioToiDaGiay;
    }

    public boolean isBat() {
        return bat;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getKhoaHmac() {
        return khoaHmac;
    }

    public long getLechGioToiDaGiay() {
        return lechGioToiDaGiay;
    }

    public boolean isDaCoKhoa() {
        return !apiKey.isEmpty() || !khoaHmac.isEmpty();
    }
}
