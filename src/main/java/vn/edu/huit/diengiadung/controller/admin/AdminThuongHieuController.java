package vn.edu.huit.diengiadung.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.ThuongHieuService;

@Controller
@RequestMapping("/admin/thuong-hieu")
public class AdminThuongHieuController {

    private final ThuongHieuService thuongHieuService;

    public AdminThuongHieuController(ThuongHieuService thuongHieuService) {
        this.thuongHieuService = thuongHieuService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa, Model model) {
        model.addAttribute("danhSach", thuongHieuService.timKiem(tuKhoa));
        model.addAttribute("tuKhoa", tuKhoa);
        return "admin/thuong-hieu/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("thuongHieu", new ThuongHieu());
        return "admin/thuong-hieu/form";
    }

    @GetMapping("/sua/{id}")
    public String formSua(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("thuongHieu", thuongHieuService.layTheoId(id));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/thuong-hieu";
        }
        return "admin/thuong-hieu/form";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("thuongHieu") ThuongHieu thuongHieu,
                      BindingResult ketQua,
                      RedirectAttributes thongBao,
                      Model model) {


        if (ketQua.hasErrors()) {
            return "admin/thuong-hieu/form";
        }

        boolean themMoi = thuongHieu.getId() == null;
        try {
            thuongHieuService.luu(thuongHieu);
        } catch (LoiNghiepVu loi) {
            model.addAttribute("thongBaoLoi", loi.getMessage());
            return "admin/thuong-hieu/form";
        }

        thongBao.addFlashAttribute("thongBao", themMoi ? "Đã thêm thương hiệu thành công" : "Đã cập nhật thương hiệu");
        return "redirect:/admin/thuong-hieu";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            thuongHieuService.xoa(id);
            thongBao.addFlashAttribute("thongBao", "Đã xoá thương hiệu thành công");
        } catch (LoiNghiepVu loi) {

            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/thuong-hieu";
    }
}