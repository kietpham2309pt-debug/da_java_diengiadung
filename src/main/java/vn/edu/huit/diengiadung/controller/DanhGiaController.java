package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.DanhGiaService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
public class DanhGiaController {

    private final DanhGiaService danhGiaService;

    public DanhGiaController(DanhGiaService danhGiaService) {
        this.danhGiaService = danhGiaService;
    }

    @PostMapping("/san-pham/{id}/danh-gia")
    public String guiDanhGia(@PathVariable Long id,
                             @RequestParam(required = false) Integer soSao,
                             @RequestParam(required = false) String noiDung,
                             HttpSession phien,
                             RedirectAttributes thongBao) {
        try {
            danhGiaService.guiDanhGia((TaiKhoan) phien.getAttribute("taiKhoan"), id, soSao, noiDung);
            thongBao.addFlashAttribute("thongBao", "Cảm ơn bạn đã đánh giá sản phẩm");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            thongBao.addFlashAttribute("noiDungCu", noiDung);
        }
        return "redirect:/san-pham/" + id + "#danh-gia";
    }
}
