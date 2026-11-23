package vn.edu.huit.diengiadung.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.LoaiSanPhamRepository;

import java.text.Normalizer;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TraLoiTuDong {

    public static final String GAP_NHAN_VIEN = "nhan-vien";

    private static final Map<String, String> CHU_DE = new LinkedHashMap<>();
    private static final Map<String, List<String>> TU_KHOA = new LinkedHashMap<>();

    static {
        CHU_DE.put("don-hang", "Tra cứu đơn hàng");
        CHU_DE.put("giao-hang", "Giao hàng, lắp đặt");
        CHU_DE.put("thanh-toan", "Hình thức thanh toán");
        CHU_DE.put("bao-hanh", "Bảo hành, đổi trả");
        CHU_DE.put("uu-dai", "Khuyến mãi, mã giảm giá");
        CHU_DE.put("san-pham", "Tư vấn chọn sản phẩm");
        CHU_DE.put("lien-he", "Địa chỉ, hotline");
        CHU_DE.put(GAP_NHAN_VIEN, "Gặp nhân viên tư vấn");

        TU_KHOA.put(GAP_NHAN_VIEN, List.of("nhan vien", "nguoi that", "tu van vien", "gap nguoi", "admin", "tong dai"));
        TU_KHOA.put("don-hang", List.of("don hang", "don cua", "tra cuu", "ma don", "bn0", "trang thai don", "huy don"));
        TU_KHOA.put("giao-hang", List.of("giao", "ship", "van chuyen", "lap dat", "bao lau"));
        TU_KHOA.put("thanh-toan", List.of("thanh toan", "chuyen khoan", "cod", "qr", "tra tien", "tra gop"));
        TU_KHOA.put("bao-hanh", List.of("bao hanh", "doi tra", "hu hong", "bi loi", "sua chua", "tra hang"));
        TU_KHOA.put("uu-dai", List.of("khuyen mai", "giam gia", "ma giam", "uu dai", "voucher", "sale"));
        TU_KHOA.put("san-pham", List.of("tu van", "nen mua", "chon", "bep tu", "hut mui", "rua chen", "lo nuong", "vi song", "hong ngoai", "gia bao nhieu"));
        TU_KHOA.put("lien-he", List.of("dia chi", "hotline", "so dien thoai", "lien he", "gio mo", "cua hang o dau"));
        TU_KHOA.put("chao", List.of("xin chao", "chao", "hello", "alo", "hi shop", "shop oi"));
    }

    private final DonHangRepository donHangRepository;
    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final CauHinhThanhToan cauHinhThanhToan;
    private final long soGioTuHuy;

    public TraLoiTuDong(DonHangRepository donHangRepository,
                        LoaiSanPhamRepository loaiSanPhamRepository,
                        CauHinhThanhToan cauHinhThanhToan,
                        @Value("${cuahang.tu-huy.sau-gio:24}") long soGioTuHuy) {
        this.donHangRepository = donHangRepository;
        this.loaiSanPhamRepository = loaiSanPhamRepository;
        this.cauHinhThanhToan = cauHinhThanhToan;
        this.soGioTuHuy = soGioTuHuy;
    }

    public String goiYMacDinh() {
        return CHU_DE.entrySet().stream()
                .map(muc -> muc.getKey() + ":" + muc.getValue())
                .collect(Collectors.joining("|"));
    }

    public String loiChao(String tenKhach) {
        String xung = tenKhach == null || tenKhach.isBlank() ? "" : " " + tenKhach.trim();
        return "Xin chào" + xung + "! Mình là trợ lý tự động của Bep Nè. "
                + "Bạn chọn một chủ đề bên dưới hoặc nhập câu hỏi, mình trả lời ngay nhé.";
    }

    public String nhanCuaChuDe(String ma) {
        return CHU_DE.get(ma);
    }

    public String nhanDien(String cauHoi) {
        String chuan = boDau(cauHoi);
        for (Map.Entry<String, List<String>> muc : TU_KHOA.entrySet()) {
            for (String tu : muc.getValue()) {
                if (chuan.contains(tu)) {
                    return muc.getKey();
                }
            }
        }
        return null;
    }

    public String traLoi(String ma, KhachHang khachHang) {
        return switch (ma) {
            case "don-hang" -> traLoiDonHang(khachHang);
            case "giao-hang" -> "Bep Nè giao hàng toàn quốc và lắp đặt miễn phí tại nhà. Sau khi đặt, nhân viên gọi xác nhận "
                    + "đơn và hẹn ngày giao phù hợp với bạn. Bạn theo dõi trạng thái đơn ở trang Tra cứu đơn: /don-hang/tra-cuu";
            case "thanh-toan" -> "Bep Nè có hai cách thanh toán: trả tiền mặt khi nhận hàng (COD) cho đơn tới "
                    + dinhDangTien(cauHinhThanhToan.getCodToiDa()) + ", hoặc chuyển khoản trước bằng mã VietQR và được giảm thêm "
                    + cauHinhThanhToan.getPhanTramGiamThanhToanTruoc() + "%. Đơn chuyển khoản chưa thanh toán sau "
                    + soGioTuHuy + " giờ sẽ tự huỷ.";
            case "bao-hanh" -> "Sản phẩm tại Bep Nè là hàng chính hãng, thời gian bảo hành ghi ở trang chi tiết từng sản phẩm. "
                    + "Sản phẩm lỗi do nhà sản xuất được đổi trả trong 7 ngày. Cần hỗ trợ bảo hành, bạn bấm Gặp nhân viên tư vấn.";
            case "uu-dai" -> "Thành viên đăng nhập được hưởng ưu đãi thành viên khi đặt hàng, và có thể nhập mã giảm giá ở bước "
                    + "đặt hàng. Sản phẩm đang giảm giá hiển thị ở trang chủ và trang Sản phẩm: /san-pham";
            case "san-pham" -> traLoiSanPham();
            case "lien-he" -> "Cửa hàng Bep Nè: 140 Lê Trọng Tấn, phường Tây Thạnh, TP. Hồ Chí Minh. "
                    + "Hotline 0339 999 999, mở cửa 8:00 tới 21:00 cả tuần.";
            case "chao" -> "Chào bạn! Bạn cần Bep Nè hỗ trợ gì ạ? Chọn chủ đề bên dưới cho nhanh nhé.";
            default -> "Mình chưa hiểu rõ câu hỏi. Bạn chọn một chủ đề bên dưới, hoặc bấm Gặp nhân viên tư vấn để "
                    + "được nhân viên trả lời trực tiếp.";
        };
    }

    private String traLoiDonHang(KhachHang khachHang) {
        if (khachHang == null) {
            return "Bạn tra cứu đơn tại trang /don-hang/tra-cuu bằng mã đơn (dạng BN000123) và số điện thoại đặt hàng. "
                    + "Nếu có tài khoản, đăng nhập rồi vào mục Đơn hàng để xem toàn bộ lịch sử.";
        }
        List<DonHang> ds = donHangRepository.findByKhachHangIdOrderByNgayDatDesc(khachHang.getId());
        if (ds.isEmpty()) {
            return "Bạn chưa có đơn hàng nào. Xem sản phẩm tại /san-pham nhé.";
        }
        DateTimeFormatter ngay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dong = ds.stream().limit(3)
                .map(d -> "• " + d.getMaDon() + ": " + d.getTenTrangThai() + ", đặt ngày " + d.getNgayDat().format(ngay)
                        + ", " + dinhDangTien(d.getTongTien() == null ? 0 : d.getTongTien()))
                .collect(Collectors.joining("\n"));
        return "Các đơn gần đây của bạn:\n" + dong + "\nXem chi tiết tại /don-hang";
    }

    private String traLoiSanPham() {
        String nhom = loaiSanPhamRepository.findAllByOrderByTenLoaiAsc().stream()
                .map(LoaiSanPham::getTenLoai)
                .collect(Collectors.joining(", "));
        return "Bep Nè có các nhóm: " + nhom + ". Bạn xem và lọc theo giá, thương hiệu tại /san-pham. "
                + "Muốn được tư vấn theo diện tích bếp và ngân sách, bấm Gặp nhân viên tư vấn nhé.";
    }

    private String dinhDangTien(long soTien) {
        return String.format(Locale.US, "%,d", soTien).replace(',', '.') + " ₫";
    }

    public static String boDau(String chuoi) {
        if (chuoi == null) {
            return "";
        }
        String tach = Normalizer.normalize(chuoi.toLowerCase(Locale.ROOT), Normalizer.Form.NFD);
        return tach.replaceAll("\\p{M}", "").replace('đ', 'd').replaceAll("\\s+", " ").trim();
    }
}
