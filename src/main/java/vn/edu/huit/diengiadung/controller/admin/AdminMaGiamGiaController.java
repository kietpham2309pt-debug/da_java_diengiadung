package vn.edu.huit.diengiadung.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.MaGiamGia;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.UuDaiService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/ma-giam-gia")
public class AdminMaGiamGiaController {

    private final UuDaiService uuDaiService;

    public AdminMaGiamGiaController(UuDaiService uuDaiService) {
        this.uuDaiService = uuDaiService;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa,
                           @RequestParam(required = false) String trangThai,
                           Model model) {
        List<MaGiamGia> tatCa = uuDaiService.timMaGiamGia(tuKhoa);
        List<MaGiamGia> danhSach = trangThai == null || trangThai.isBlank()
                ? tatCa
                : tatCa.stream().filter(ma -> trangThai.equals(ma.getTrangThai())).toList();
        model.addAttribute("danhSach", danhSach);
        model.addAttribute("tongSo", tatCa.size());
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        return "admin/ma-giam-gia/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model model) {
        MaGiamGia ma = new MaGiamGia();
        ma.setMa(uuDaiService.taoMaNgauNhien());
        model.addAttribute("maGiamGia", ma);
        return "admin/ma-giam-gia/form";
    }

    @GetMapping("/ma-ngau-nhien")
    @ResponseBody
    public Map<String, String> maNgauNhien() {
        return Map.of("ma", uuDaiService.taoMaNgauNhien());
    }

    @GetMapping("/sua/{id}")
    public String formSua(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("maGiamGia", uuDaiService.layMaGiamGia(id));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/ma-giam-gia";
        }
        return "admin/ma-giam-gia/form";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("maGiamGia") MaGiamGia maGiamGia,
                      BindingResult ketQua,
                      RedirectAttributes thongBao,
                      Model model) {
        if (ketQua.hasErrors()) {
            return "admin/ma-giam-gia/form";
        }
        boolean themMoi = maGiamGia.getId() == null;
        try {
            uuDaiService.luuMaGiamGia(maGiamGia);
        } catch (LoiNghiepVu loi) {
            model.addAttribute("thongBaoLoi", loi.getMessage());
            return "admin/ma-giam-gia/form";
        }
        thongBao.addFlashAttribute("thongBao", (themMoi ? "Đã tạo mã " : "Đã cập nhật mã ") + maGiamGia.getMa());
        return "redirect:/admin/ma-giam-gia";
    }

    @PostMapping("/doi-trang-thai/{id}")
    public String doiTrangThai(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            uuDaiService.doiTrangThaiMa(id);
            thongBao.addFlashAttribute("thongBao", "Đã cập nhật trạng thái mã giảm giá");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/ma-giam-gia";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            uuDaiService.xoaMaGiamGia(id);
            thongBao.addFlashAttribute("thongBao", "Đã xoá mã giảm giá");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/ma-giam-gia";
    }
}
