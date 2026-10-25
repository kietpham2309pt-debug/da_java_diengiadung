package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.HoSoKhachHang;
import vn.edu.huit.diengiadung.service.HoSoService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/ho-so")
public class HoSoController {

    private final HoSoService dichVuHoSo;

    public HoSoController(HoSoService dichVuHoSo) {
        this.dichVuHoSo = dichVuHoSo;
    }

    @GetMapping
    public String xemHoSo(HttpSession phien, Model moHinh, RedirectAttributes thongBao) {
        try {
            moHinh.addAttribute("hoSo", dichVuHoSo.layHoSo(layTaiKhoan(phien)));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/";
        }
        return "ho-so";
    }

    @PostMapping
    public String capNhat(@Valid @ModelAttribute("hoSo") HoSoKhachHang hoSo,
                          BindingResult ketQuaKiemTra,
                          HttpSession phien,
                          RedirectAttributes thongBao) {
        if (ketQuaKiemTra.hasErrors()) {
            return "ho-so";
        }
        try {
            TaiKhoan taiKhoanMoi = dichVuHoSo.capNhatHoSo(layTaiKhoan(phien), hoSo);
            phien.setAttribute("taiKhoan", taiKhoanMoi);
            thongBao.addFlashAttribute("thongBao", "Đã cập nhật hồ sơ");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/ho-so";
    }

    private TaiKhoan layTaiKhoan(HttpSession phien) {
        return (TaiKhoan) phien.getAttribute("taiKhoan");
    }
}
