package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.TaiKhoanService;

@Controller
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    public TaiKhoanController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    @GetMapping("/dang-nhap")
    public String formDangNhap(@RequestParam(required = false) String loi, Model model) {
        if ("quyen".equals(loi)) {
            model.addAttribute("loiDangNhap", "Bạn cần đăng nhập bằng tài khoản quản trị để vào khu vực này");
        }
        return "dang-nhap";
    }

    @PostMapping("/dang-nhap")
    public String dangNhap(@RequestParam String tenDangNhap,
                           @RequestParam String matKhau,
                           HttpSession session,
                           Model model) {
        try {
            TaiKhoan taiKhoan = taiKhoanService.dangNhap(tenDangNhap, matKhau);
            session.setAttribute("taiKhoan", taiKhoan);
            if (taiKhoan.laQuanTri()) {
                return "redirect:/admin";
            }
            return "redirect:/";
        } catch (LoiNghiepVu loi) {
            model.addAttribute("loiDangNhap", loi.getMessage());
            model.addAttribute("tenDangNhapCu", tenDangNhap);
            return "dang-nhap";
        }
    }

    @GetMapping("/dang-ky")
    public String formDangKy(Model model) {
        model.addAttribute("taiKhoan", new TaiKhoan());
        return "dang-ky";
    }

    @PostMapping("/dang-ky")
    public String dangKy(@Valid @ModelAttribute("taiKhoan") TaiKhoan taiKhoan,
                         BindingResult ketQua,
                         @RequestParam(required = false) String soDienThoai,
                         @RequestParam(required = false) String diaChi,
                         RedirectAttributes thongBao,
                         Model model) {
        if (ketQua.hasErrors()) {
            model.addAttribute("soDienThoai", soDienThoai);
            model.addAttribute("diaChi", diaChi);
            return "dang-ky";
        }
        try {
            taiKhoanService.dangKy(taiKhoan, soDienThoai, diaChi);
        } catch (LoiNghiepVu loi) {
            model.addAttribute("loiDangKy", loi.getMessage());
            model.addAttribute("soDienThoai", soDienThoai);
            model.addAttribute("diaChi", diaChi);
            return "dang-ky";
        }
        thongBao.addFlashAttribute("thongBao", "Đăng ký thành công, mời bạn đăng nhập");
        return "redirect:/dang-nhap";
    }

    @GetMapping("/dang-xuat")
    public String dangXuat(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
