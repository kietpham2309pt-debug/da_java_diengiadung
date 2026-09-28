package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.diengiadung.service.DonHangService;

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
}