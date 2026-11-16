package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.YeuThichService;

@Controller
@RequestMapping("/yeu-thich")
public class YeuThichController {

    private final YeuThichService dichVuYeuThich;

    public YeuThichController(YeuThichService dichVuYeuThich) {
        this.dichVuYeuThich = dichVuYeuThich;
    }

    @GetMapping
    public String danhSach(HttpSession phien, Model moHinh, RedirectAttributes thongBao) {
        try {
            moHinh.addAttribute("danhSach", dichVuYeuThich.layDanhSach(layTaiKhoan(phien)));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/";
        }
        return "yeu-thich";
    }

    @PostMapping("/{id}")
    public String doiTrangThai(@PathVariable Long id,
                               @RequestParam(defaultValue = "chi-tiet") String quayVe,
                               HttpSession phien,
                               RedirectAttributes thongBao) {
        try {
            boolean daThem = dichVuYeuThich.doiTrangThai(layTaiKhoan(phien), id);
            thongBao.addFlashAttribute("thongBao", daThem
                    ? "Đã thêm sản phẩm vào danh sách yêu thích"
                    : "Đã bỏ sản phẩm khỏi danh sách yêu thích");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "danh-sach".equals(quayVe) ? "redirect:/yeu-thich" : "redirect:/san-pham/" + id;
    }

    private TaiKhoan layTaiKhoan(HttpSession phien) {
        return (TaiKhoan) phien.getAttribute("taiKhoan");
    }
}
