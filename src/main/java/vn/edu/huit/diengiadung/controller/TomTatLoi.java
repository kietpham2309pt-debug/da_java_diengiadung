package vn.edu.huit.diengiadung.controller;

import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.util.DetailedError;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component("tomTatLoi")
public class TomTatLoi {

    private static final Set<String> MA_THIEU = Set.of("NotBlank", "NotNull", "NotEmpty");

    public record LoiTruong(String truong, boolean thieu, String thongBao) {

        public String getTruong() {
            return truong;
        }

        public boolean isThieu() {
            return thieu;
        }

        public String getThongBao() {
            return thongBao;
        }
    }

    public List<LoiTruong> gom(List<DetailedError> danhSachLoi) {
        Map<String, LoiTruong> theoTruong = new LinkedHashMap<>();
        List<LoiTruong> loiChung = new ArrayList<>();
        for (DetailedError loi : danhSachLoi) {
            if (loi.isGlobal() || loi.getFieldName() == null) {
                loiChung.add(new LoiTruong("", false, loi.getMessage()));
                continue;
            }
            boolean thieu = MA_THIEU.contains(loi.getCode())
                    || ("AssertTrue".equals(loi.getCode()) && "dongY".equals(loi.getFieldName()));
            LoiTruong moi = new LoiTruong(loi.getFieldName(), thieu, loi.getMessage());
            LoiTruong cu = theoTruong.get(loi.getFieldName());
            if (cu == null || (thieu && !cu.thieu())) {
                theoTruong.put(loi.getFieldName(), moi);
            }
        }
        List<LoiTruong> ketQua = new ArrayList<>(theoTruong.values());
        ketQua.addAll(loiChung);
        return ketQua;
    }

    public List<LoiTruong> thieu(List<LoiTruong> ds) {
        return ds.stream().filter(LoiTruong::thieu).toList();
    }

    public List<LoiTruong> sai(List<LoiTruong> ds) {
        return ds.stream().filter(loi -> !loi.thieu()).toList();
    }

    public String thongBaoCua(List<DetailedError> danhSachLoi, String truong) {
        return gom(danhSachLoi).stream()
                .filter(loi -> truong.equals(loi.truong()))
                .map(LoiTruong::thongBao)
                .findFirst()
                .orElse(null);
    }
}
