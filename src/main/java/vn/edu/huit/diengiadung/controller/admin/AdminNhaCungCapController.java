package vn.edu.huit.diengiadung.controller.admin;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.NhaCungCap;
import vn.edu.huit.diengiadung.service.NhaCungCapService;

import java.util.List;

@Controller
@RequestMapping("/admin/nha-cung-cap")
public class AdminNhaCungCapController {

    private final NhaCungCapService nhaCungCapService;

    @Autowired
    public AdminNhaCungCapController(NhaCungCapService nhaCungCapService) {
        this.nhaCungCapService = nhaCungCapService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa, Model model) {
        List<NhaCungCap> danhSach = nhaCungCapService.timKiem(tuKhoa);
        model.addAttribute("danhSach", danhSach);
        model.addAttribute("tuKhoa", tuKhoa);
        return "admin/nha-cung-cap/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("nhaCungCap", new NhaCungCap());
        return "admin/nha-cung-cap/form";
    }

    @GetMapping("/sua/{id}")
    public String hienThiFormSua(@PathVariable Long id, Model model) {
        NhaCungCap nhaCungCap = nhaCungCapService.layTheoId(id);
        if (nhaCungCap == null) {
            return "redirect:/admin/nha-cung-cap";
        }
        model.addAttribute("nhaCungCap", nhaCungCap);
        return "admin/nha-cung-cap/form";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("nhaCungCap") NhaCungCap nhaCungCap, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/nha-cung-cap/form";
        }
        nhaCungCapService.luu(nhaCungCap);
        redirectAttributes.addFlashAttribute("thongBao", "Đã lưu thông tin nhà cung cấp.");
        return "redirect:/admin/nha-cung-cap";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            nhaCungCapService.xoa(id);
            redirectAttributes.addFlashAttribute("thongBao", "Đã xoá nhà cung cấp.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Không thể xoá vì nhà cung cấp này đã có giao dịch phiếu nhập.");
        }
        return "redirect:/admin/nha-cung-cap";
    }
}