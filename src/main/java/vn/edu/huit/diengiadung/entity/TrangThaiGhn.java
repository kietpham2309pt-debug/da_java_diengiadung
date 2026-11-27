package vn.edu.huit.diengiadung.entity;

import java.util.Map;
import java.util.Set;

public final class TrangThaiGhn {

    public static final String DA_HUY = "cancel";
    public static final String DA_GIAO = "delivered";

    private static final Map<String, String> TEN = Map.ofEntries(
            Map.entry("ready_to_pick", "Chờ lấy hàng"),
            Map.entry("picking", "Đang lấy hàng"),
            Map.entry("money_collect_picking", "Đang lấy hàng"),
            Map.entry("picked", "Đã lấy hàng"),
            Map.entry("storing", "Đang lưu kho"),
            Map.entry("sorting", "Đang phân loại"),
            Map.entry("transporting", "Đang luân chuyển"),
            Map.entry("delivering", "Đang giao hàng"),
            Map.entry("money_collect_delivering", "Đang giao hàng"),
            Map.entry("delivery_fail", "Giao thất bại"),
            Map.entry("waiting_to_return", "Chờ trả hàng"),
            Map.entry("return", "Trả hàng"),
            Map.entry("return_transporting", "Đang trả hàng"),
            Map.entry("return_sorting", "Đang trả hàng"),
            Map.entry("returning", "Đang trả hàng"),
            Map.entry("return_fail", "Trả hàng thất bại"),
            Map.entry("returned", "Đã trả hàng về shop"),
            Map.entry("delivered", "Giao thành công"),
            Map.entry("cancel", "Đã huỷ vận đơn"),
            Map.entry("exception", "Ngoại lệ"),
            Map.entry("lost", "Thất lạc"),
            Map.entry("damage", "Hư hỏng"),
            Map.entry("scrap", "Hàng hỏng huỷ"));

    private static final Set<String> DA_ROI_KHO = Set.of("picked", "storing", "sorting", "transporting",
            "delivering", "money_collect_delivering", "delivery_fail");

    private TrangThaiGhn() {
    }

    public static String tenTiengViet(String ma) {
        if (ma == null || ma.isBlank()) {
            return "Chưa tạo vận đơn";
        }
        return TEN.getOrDefault(ma, ma);
    }

    public static boolean daRoiKho(String ma) {
        return ma != null && DA_ROI_KHO.contains(ma);
    }
}
