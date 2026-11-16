package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestDataValueProcessor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;

@Component("requestDataValueProcessor")
public class MaChongGiaMao implements RequestDataValueProcessor {

    public static final String TEN_TRUONG = "_maChong";
    private static final String KHOA_PHIEN = "maChongGiaMao";
    private static final SecureRandom NGAU_NHIEN = new SecureRandom();
    private static final String KHOA_PHUONG_THUC = MaChongGiaMao.class.getName() + ".phuongThuc";

    public static String layMa(HttpSession phien) {
        Object ma = phien.getAttribute(KHOA_PHIEN);
        if (ma instanceof String chuoi) {
            return chuoi;
        }
        byte[] byteNgauNhien = new byte[32];
        NGAU_NHIEN.nextBytes(byteNgauNhien);
        String maMoi = Base64.getUrlEncoder().withoutPadding().encodeToString(byteNgauNhien);
        phien.setAttribute(KHOA_PHIEN, maMoi);
        return maMoi;
    }

    public static boolean hopLe(HttpSession phien, String maGuiLen) {
        if (phien == null || maGuiLen == null) {
            return false;
        }
        Object ma = phien.getAttribute(KHOA_PHIEN);
        return ma instanceof String chuoi && MessageDigest.isEqual(
                chuoi.getBytes(StandardCharsets.UTF_8), maGuiLen.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String processAction(HttpServletRequest request, String action, String httpMethod) {
        request.setAttribute(KHOA_PHUONG_THUC, httpMethod);
        return action;
    }

    @Override
    public String processFormFieldValue(HttpServletRequest request, String name, String value, String type) {
        return value;
    }

    @Override
    public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
        Object phuongThuc = request.getAttribute(KHOA_PHUONG_THUC);
        request.removeAttribute(KHOA_PHUONG_THUC);
        if (phuongThuc instanceof String chuoi && "get".equalsIgnoreCase(chuoi)) {
            return Map.of();
        }
        return Map.of(TEN_TRUONG, layMa(request.getSession()));
    }

    @Override
    public String processUrl(HttpServletRequest request, String url) {
        return url;
    }
}
