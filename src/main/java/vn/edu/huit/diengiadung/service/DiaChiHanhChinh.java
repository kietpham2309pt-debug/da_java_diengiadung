package vn.edu.huit.diengiadung.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class DiaChiHanhChinh {

    private final Map<String, List<String>> phuongXaTheoTinh;

    public DiaChiHanhChinh(ObjectMapper boDocJson) {
        try (InputStream luong = new ClassPathResource("static/data/dia-chi-2025.json").getInputStream()) {
            phuongXaTheoTinh = Collections.unmodifiableMap(
                    boDocJson.readValue(luong, new TypeReference<LinkedHashMap<String, List<String>>>() {
                    }));
        } catch (IOException loi) {
            throw new UncheckedIOException("Không đọc được dữ liệu đơn vị hành chính", loi);
        }
    }

    public List<String> danhSachTinhThanh() {
        return new ArrayList<>(phuongXaTheoTinh.keySet());
    }

    public List<String> danhSachPhuongXa(String tinhThanh) {
        if (tinhThanh == null) {
            return List.of();
        }
        return phuongXaTheoTinh.getOrDefault(tinhThanh, List.of());
    }

    public boolean coTinhThanh(String tinhThanh) {
        return tinhThanh != null && phuongXaTheoTinh.containsKey(tinhThanh);
    }

    public boolean hopLe(String tinhThanh, String phuongXa) {
        return phuongXa != null && danhSachPhuongXa(tinhThanh).contains(phuongXa);
    }
}
