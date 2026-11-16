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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.KhuyenMai;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.ThuongHieuService;
import vn.edu.huit.diengiadung.service.UuDaiService;

import java.util.List;

@Controller
@RequestMapping("/admin/khuyen-mai")
public class AdminKhuyenMaiController {

    private final UuDaiService uuDaiService;
    private final LoaiSanPhamService loaiSanPhamService;
    private final ThuongHieuService thuongHieuService;

    public AdminKhuyenMaiController(UuDaiService uuDaiService, LoaiSanPhamService loaiSanPhamService,
                                    ThuongHieuService thuongHieuService) {
        this.uuDaiService = uuDaiService;
        this.loaiSanPhamService = loaiSanPhamService;
        this.thuongHieuService = thuongHieuService;
    }

    @ModelAttribute
    public void danhMucChon(Model model) {
        model.addAttribute("dsLoai", loaiSanPhamService.layTatCa());
        model.addAttribute("dsHang", thuongHieuService.layDanhSach());
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa,
                           @RequestParam(required = false) String trangThai,
                           Model model) {
        List<KhuyenMai> tatCa = uuDaiService.timKhuyenMai(tuKhoa);
        List<KhuyenMai> danhSach = trangThai == null || trangThai.isBlank()
                ? tatCa
                : tatCa.stream().filter(km -> trangThai.equals(km.getTrangThai())).toList();
        model.addAttribute("danhSach", danhSach);
        model.addAttribute("tongSo", tatCa.size());
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("trangThai", trangThai);
        return "admin/khuyen-mai/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model model) {
        model.addAttribute("khuyenMai", new KhuyenMai());
        return "admin/khuyen-mai/form";
    }

    @GetMapping("/sua/{id}")
    public String formSua(@PathVariable Long id, Model model, RedirectAttributes thongBao) {
        try {
            model.addAttribute("khuyenMai", uuDaiService.layKhuyenMai(id));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/khuyen-mai";
        }
        return "admin/khuyen-mai/form";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("khuyenMai") KhuyenMai khuyenMai,
                      BindingResult ketQua,
                      RedirectAttributes thongBao) {
        if (ketQua.hasErrors()) {
            return "admin/khuyen-mai/form";
        }
        boolean themMoi = khuyenMai.getId() == null;
        uuDaiService.luuKhuyenMai(khuyenMai);
        thongBao.addFlashAttribute("thongBao", (themMoi ? "Đã tạo chương trình " : "Đã cập nhật chương trình ") + khuyenMai.getTen());
        return "redirect:/admin/khuyen-mai";
    }

    @PostMapping("/doi-trang-thai/{id}")
    public String doiTrangThai(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            uuDaiService.doiTrangThaiKhuyenMai(id);
            thongBao.addFlashAttribute("thongBao", "Đã cập nhật trạng thái chương trình");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/khuyen-mai";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            uuDaiService.xoaKhuyenMai(id);
            thongBao.addFlashAttribute("thongBao", "Đã xoá chương trình khuyến mãi");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/khuyen-mai";
    }
}
