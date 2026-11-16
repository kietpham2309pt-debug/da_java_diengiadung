package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.service.DonHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.LyDoHuyDon;

@Controller
@RequestMapping("/admin/don-hang")
public class AdminDonHangController {

    private final DonHangService donHangService;

    public AdminDonHangController(DonHangService donHangService) {
        this.donHangService = donHangService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String trangThai,
                           @RequestParam(required = false) String tuKhoa,
                           @RequestParam(required = false) String thanhToan,
                           Model model) {
        String khoa = tuKhoa == null ? "" : tuKhoa.trim().toLowerCase();
        String khoaSo = khoa.replaceAll("\\s", "");
        java.util.List<DonHang> danhSach = donHangService.timTheoTrangThai(trangThai).stream()
                .filter(don -> khoa.isEmpty()
                        || don.getMaDon().toLowerCase().contains(khoaSo)
                        || String.valueOf(don.getId()).equals(khoaSo)
                        || (don.getSoDienThoaiNhan() != null && don.getSoDienThoaiNhan().contains(khoaSo))
                        || (don.getTenNguoiNhan() != null && don.getTenNguoiNhan().toLowerCase().contains(khoa)))
                .filter(don -> thanhToan == null || thanhToan.isBlank()
                        || ("CHO_TIEN".equals(thanhToan) && don.isChoThanhToan())
                        || ("CK".equals(thanhToan) && don.isThanhToanTruoc())
                        || ("COD".equals(thanhToan) && !don.isThanhToanTruoc()))
                .toList();
        model.addAttribute("danhSach", danhSach);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("thanhToan", thanhToan);
        return "admin/don-hang/danh-sach";
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("donHang", donHangService.layTheoId(id));
            model.addAttribute("dsLyDoHuy", LyDoHuyDon.CUA_CUA_HANG);
            return "admin/don-hang/chi-tiet";
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/don-hang";
        }
    }

    @GetMapping("/{id}/in")
    public String inHoaDon(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("donHang", donHangService.layTheoId(id));
            return "admin/don-hang/in";
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/don-hang";
        }
    }

    @PostMapping("/{id}/cap-nhat-trang-thai")
    public String capNhatTrangThai(@PathVariable Long id, @RequestParam String trangThai, RedirectAttributes thongBao) {
        try {
            donHangService.capNhatTrangThai(id, trangThai);
            thongBao.addFlashAttribute("thongBao", "Đã cập nhật trạng thái đơn hàng.");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/don-hang/" + id;
    }

    @PostMapping("/{id}/da-thanh-toan")
    public String xacNhanDaThanhToan(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            donHangService.xacNhanDaThanhToan(id);
            thongBao.addFlashAttribute("thongBao", "Đã xác nhận nhận tiền chuyển khoản cho đơn hàng.");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/don-hang/" + id;
    }

    @PostMapping("/{id}/huy")
    public String huyDonHang(@PathVariable Long id,
                             @RequestParam(required = false) String lyDo,
                             @RequestParam(required = false) String lyDoKhac,
                             RedirectAttributes thongBao) {
        try {
            donHangService.huyDonHang(id, LyDoHuyDon.chuanHoa(lyDo, lyDoKhac, LyDoHuyDon.CUA_CUA_HANG), "CUA_HANG");
            thongBao.addFlashAttribute("thongBao", "Đã hủy đơn hàng và hoàn lại tồn kho.");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/don-hang/" + id;
    }
}
