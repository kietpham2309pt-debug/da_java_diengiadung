package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.service.DanhGiaService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/admin/danh-gia")
public class AdminDanhGiaController {

    private final DanhGiaService danhGiaService;

    public AdminDanhGiaController(DanhGiaService danhGiaService) {
        this.danhGiaService = danhGiaService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) Integer soSao,
                           @RequestParam(required = false) Boolean hien,
                           Model model) {
        model.addAttribute("danhSach", danhGiaService.layTatCa(soSao, hien));
        model.addAttribute("soSao", soSao);
        model.addAttribute("hien", hien);
        return "admin/danh-gia/danh-sach";
    }

    @PostMapping("/doi-hien/{id}")
    public String doiHien(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            danhGiaService.doiHien(id);
            thongBao.addFlashAttribute("thongBao", "Đã cập nhật trạng thái hiển thị của đánh giá");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/danh-gia";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            danhGiaService.xoa(id);
            thongBao.addFlashAttribute("thongBao", "Đã xoá đánh giá");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/danh-gia";
    }
}
