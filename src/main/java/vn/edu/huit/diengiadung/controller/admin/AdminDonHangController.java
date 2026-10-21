
package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.service.DonHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/admin/don-hang")
public class AdminDonHangController {

    private final DonHangService donHangService;

    public AdminDonHangController(DonHangService donHangService) {
        this.donHangService = donHangService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String trangThai, Model model) {
        model.addAttribute("danhSach", donHangService.timTheoTrangThai(trangThai));
        model.addAttribute("trangThai", trangThai);
        return "admin/don-hang/danh-sach";
    }

    @GetMapping("/{id}")
    public String chiTiet(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("donHang", donHangService.layTheoId(id));
            return "admin/don-hang/chi-tiet";
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

    @PostMapping("/{id}/huy")
    public String huyDonHang(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            donHangService.huyDonHang(id);
            thongBao.addFlashAttribute("thongBao", "Đã hủy đơn hàng và hoàn lại tồn kho.");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/don-hang/" + id;
    }
}
