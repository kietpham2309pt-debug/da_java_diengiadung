package vn.edu.huit.diengiadung.service;

public enum MucGia {

    DUOI_5_TRIEU("duoi-5-trieu", "Dưới 5 triệu", null, 5_000_000L),
    TU_5_DEN_10_TRIEU("5-10-trieu", "Từ 5 tới 10 triệu", 5_000_000L, 10_000_000L),
    TU_10_DEN_20_TRIEU("10-20-trieu", "Từ 10 tới 20 triệu", 10_000_000L, 20_000_000L),
    TREN_20_TRIEU("tren-20-trieu", "Trên 20 triệu", 20_000_000L, null);

    private final String ma;
    private final String ten;
    private final Long giaTu;
    private final Long giaDuoi;

    MucGia(String ma, String ten, Long giaTu, Long giaDuoi) {
        this.ma = ma;
        this.ten = ten;
        this.giaTu = giaTu;
        this.giaDuoi = giaDuoi;
    }

    public String getMa() {
        return ma;
    }

    public String getTen() {
        return ten;
    }

    /** Giá thấp nhất của mức, tính cả bằng. Null nghĩa là không chặn dưới. */
    public Long getGiaTu() {
        return giaTu;
    }

    /** Giá phải nhỏ hơn số này. Null nghĩa là không chặn trên. */
    public Long getGiaDuoi() {
        return giaDuoi;
    }

    public static MucGia timTheoMa(String ma) {
        for (MucGia muc : values()) {
            if (muc.ma.equals(ma)) {
                return muc;
            }
        }
        return null;
    }
}
