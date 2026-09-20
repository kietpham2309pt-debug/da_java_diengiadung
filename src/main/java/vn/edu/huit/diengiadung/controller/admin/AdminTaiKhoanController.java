package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.TaiKhoanService;

@Controller
@RequestMapping("/admin/tai-khoan")
public class AdminTaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    public AdminTaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa, Model model) {
        model.addAttribute("danhSach", taiKhoanService.timKiem(tuKhoa));
        model.addAttribute("tuKhoa", tuKhoa);
        return "admin/tai-khoan/danh-sach";
    }

    @PostMapping("/doi-trang-thai/{id}")
    public String doiTrangThai(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            taiKhoanService.doiTrangThai(id);
            thongBao.addFlashAttribute("thongBao", "Đã đổi trạng thái tài khoản");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/tai-khoan";
    }

    @PostMapping("/dat-lai-mat-khau/{id}")
    public String datLaiMatKhau(@PathVariable Long id,
                                @RequestParam String matKhauMoi,
                                RedirectAttributes thongBao) {
        try {
            taiKhoanService.datLaiMatKhau(id, matKhauMoi);
            thongBao.addFlashAttribute("thongBao", "Đã đặt lại mật khẩu");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/tai-khoan";
    }
}
