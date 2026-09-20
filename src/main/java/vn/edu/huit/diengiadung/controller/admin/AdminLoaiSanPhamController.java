package vn.edu.huit.diengiadung.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/admin/loai-san-pham")
public class AdminLoaiSanPhamController {

    private final LoaiSanPhamService loaiSanPhamService;

    public AdminLoaiSanPhamController(LoaiSanPhamService loaiSanPhamService) {
        this.loaiSanPhamService = loaiSanPhamService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa, Model model) {
        model.addAttribute("danhSach", loaiSanPhamService.timKiem(tuKhoa));
        model.addAttribute("tuKhoa", tuKhoa);
        return "admin/loai-san-pham/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("loaiSanPham", new LoaiSanPham());
        return "admin/loai-san-pham/form";
    }

    @GetMapping("/sua/{id}")
    public String formSua(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("loaiSanPham", loaiSanPhamService.layTheoId(id));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/loai-san-pham";
        }
        return "admin/loai-san-pham/form";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("loaiSanPham") LoaiSanPham loaiSanPham,
                      BindingResult ketQua,
                      RedirectAttributes thongBao,
                      Model model) {
        if (ketQua.hasErrors()) {
            return "admin/loai-san-pham/form";
        }
        boolean themMoi = loaiSanPham.getId() == null;
        try {
            loaiSanPhamService.luu(loaiSanPham);
        } catch (LoiNghiepVu loi) {
            model.addAttribute("thongBaoLoi", loi.getMessage());
            return "admin/loai-san-pham/form";
        }
        thongBao.addFlashAttribute("thongBao", themMoi ? "Đã thêm loại sản phẩm" : "Đã cập nhật loại sản phẩm");
        return "redirect:/admin/loai-san-pham";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            loaiSanPhamService.xoa(id);
            thongBao.addFlashAttribute("thongBao", "Đã xoá loại sản phẩm");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/loai-san-pham";
    }
}
