package vn.edu.huit.diengiadung.tichhop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CauHinhGhn {

    private final boolean bat;
    private final String url;
    private final String token;
    private final String shopId;
    private final String khoaWebhook;
    private final int khoiLuongMoiSanPham;
    private final int kichThuocGoi;
    private final String ghiChuBatBuoc;

    public CauHinhGhn(@Value("${ghn.bat:false}") boolean bat,
                      @Value("${ghn.url:https://dev-online-gateway.ghn.vn}") String url,
                      @Value("${ghn.token:}") String token,
                      @Value("${ghn.shop-id:}") String shopId,
                      @Value("${ghn.khoa-webhook:}") String khoaWebhook,
                      @Value("${ghn.khoi-luong-moi-san-pham-gram:3000}") int khoiLuongMoiSanPham,
                      @Value("${ghn.kich-thuoc-goi-cm:40}") int kichThuocGoi,
                      @Value("${ghn.ghi-chu-bat-buoc:CHOXEMHANGKHONGTHU}") String ghiChuBatBuoc) {
        this.bat = bat;
        this.url = url == null ? "" : url.trim();
        this.token = token == null ? "" : token.trim();
        this.shopId = shopId == null ? "" : shopId.trim();
        this.khoaWebhook = khoaWebhook == null ? "" : khoaWebhook.trim();
        this.khoiLuongMoiSanPham = khoiLuongMoiSanPham;
        this.kichThuocGoi = kichThuocGoi;
        this.ghiChuBatBuoc = ghiChuBatBuoc;
    }

    public boolean isBat() {
        return bat;
    }

    public boolean isSanSang() {
        return bat && !token.isEmpty() && !shopId.isEmpty();
    }

    public String getUrl() {
        return url;
    }

    public String getToken() {
        return token;
    }

    public String getShopId() {
        return shopId;
    }

    public String getKhoaWebhook() {
        return khoaWebhook;
    }

    public int getKhoiLuongMoiSanPham() {
        return khoiLuongMoiSanPham;
    }

    public int getKichThuocGoi() {
        return kichThuocGoi;
    }

    public String getGhiChuBatBuoc() {
        return ghiChuBatBuoc;
    }

    public boolean isMoiTruongThu() {
        return url.contains("dev-online-gateway");
    }
}
