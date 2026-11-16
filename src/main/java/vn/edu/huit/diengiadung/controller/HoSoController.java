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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.HoSoKhachHang;
import vn.edu.huit.diengiadung.service.HoSoService;
import vn.edu.huit.diengiadung.service.TaiKhoanService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/ho-so")
public class HoSoController {

    private final HoSoService dichVuHoSo;
    private final TaiKhoanService dichVuTaiKhoan;

    public HoSoController(HoSoService dichVuHoSo, TaiKhoanService dichVuTaiKhoan) {
        this.dichVuHoSo = dichVuHoSo;
        this.dichVuTaiKhoan = dichVuTaiKhoan;
    }

    @GetMapping("/doi-mat-khau")
    public String formDoiMatKhau() {
        return "doi-mat-khau";
    }

    @PostMapping("/doi-mat-khau")
    public String doiMatKhau(@RequestParam String matKhauCu,
                             @RequestParam String matKhauMoi,
                             @RequestParam String nhapLai,
                             HttpSession phien,
                             Model moHinh,
                             RedirectAttributes thongBao) {
        try {
            dichVuTaiKhoan.doiMatKhau(layTaiKhoan(phien).getId(), matKhauCu, matKhauMoi, nhapLai);
        } catch (LoiNghiepVu loi) {
            moHinh.addAttribute("loiDoiMatKhau", loi.getMessage());
            return "doi-mat-khau";
        }
        thongBao.addFlashAttribute("thongBao", "Đã đổi mật khẩu. Lần đăng nhập sau hãy dùng mật khẩu mới");
        return "redirect:/ho-so";
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
