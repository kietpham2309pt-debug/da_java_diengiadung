package vn.edu.huit.diengiadung.service;

import java.util.List;

public final class LyDoHuyDon {

    public static final String KHAC = "Lý do khác";

    public static final List<String> CUA_KHACH = List.of(
            "Tôi muốn thay đổi địa chỉ hoặc số điện thoại nhận hàng",
            "Tôi muốn đổi sản phẩm hoặc số lượng",
            "Tôi muốn dùng mã giảm giá hoặc đổi phương thức thanh toán",
            "Tôi tìm được nơi khác giá tốt hơn",
            "Tôi không còn nhu cầu mua",
            KHAC);

    public static final List<String> CUA_CUA_HANG = List.of(
            "Khách hàng yêu cầu huỷ",
            "Không liên lạc được với khách hàng",
            "Sản phẩm hết hàng hoặc không giao được",
            "Đơn đặt trùng hoặc đơn ảo",
            "Khách chưa chuyển khoản sau thời gian chờ",
            KHAC);

    private LyDoHuyDon() {
    }

    public static String chuanHoa(String lyDo, String lyDoKhac, List<String> danhSach) {
        if (lyDo == null || lyDo.isBlank()) {
            throw new LoiNghiepVu("Vui lòng chọn lý do huỷ đơn");
        }
        if (!danhSach.contains(lyDo)) {
            throw new LoiNghiepVu("Lý do huỷ đơn không hợp lệ");
        }
        if (!KHAC.equals(lyDo)) {
            return lyDo;
        }
        String noiDung = lyDoKhac == null ? "" : lyDoKhac.trim();
        if (noiDung.length() < 5) {
            throw new LoiNghiepVu("Vui lòng ghi rõ lý do huỷ (ít nhất 5 ký tự)");
        }
        if (noiDung.length() > 250) {
            throw new LoiNghiepVu("Lý do huỷ tối đa 250 ký tự");
        }
        return noiDung;
    }
}
