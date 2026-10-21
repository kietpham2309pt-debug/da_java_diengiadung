package vn.edu.huit.diengiadung.service;

import org.springframework.data.domain.Sort;

public enum KieuSapXep {

    MOI_NHAT("moi-nhat", "Mới nhất"),
    GIA_TANG("gia-tang", "Giá tăng dần"),
    GIA_GIAM("gia-giam", "Giá giảm dần"),
    TEN("ten", "Tên A đến Z");

    private final String ma;
    private final String ten;

    KieuSapXep(String ma, String ten) {
        this.ma = ma;
        this.ten = ten;
    }

    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    public Sort taoCachSapXep() {
        Sort moiNhatTruoc = Sort.by(Sort.Direction.DESC, "id");
        return switch (this) {
            case GIA_TANG -> Sort.by(Sort.Direction.ASC, "giaBanThucTe").and(moiNhatTruoc);
            case GIA_GIAM -> Sort.by(Sort.Direction.DESC, "giaBanThucTe").and(moiNhatTruoc);
            case TEN -> Sort.by(Sort.Direction.ASC, "tenSanPham").and(moiNhatTruoc);
            default -> moiNhatTruoc;
        };
    }

    public static KieuSapXep timTheoMa(String ma) {
        for (KieuSapXep kieu : values()) {
            if (kieu.ma.equals(ma)) {
                return kieu;
            }
        }
        return MOI_NHAT;
    }
}
