package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.diengiadung.service.KhachHangService;

@Controller
@RequestMapping("/admin/khach-hang")
public class AdminKhachHangController {

    private final KhachHangService khachHangService;

    public AdminKhachHangController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    @GetMapping
    public String danhSach(
            @RequestParam(required = false) String tuKhoa,
            Model model) {

        model.addAttribute("danhSach", khachHangService.timKiem(tuKhoa));
        model.addAttribute("tuKhoa", tuKhoa);

        return "admin/khach-hang/danh-sach";
    }
}
