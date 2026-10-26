package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/don-hang")
public class DonHangKhachController {

    private final DatHangService dichVuDatHang;

    public DonHangKhachController(DatHangService dichVuDatHang) {
        this.dichVuDatHang = dichVuDatHang;
    }

    @GetMapping
    public String lichSuDon(HttpSession phien, Model moHinh, RedirectAttributes thongBao) {
        try {
            moHinh.addAttribute("danhSachDon", dichVuDatHang.layDonCuaKhach(layTaiKhoan(phien)));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/";
        }
        return "don-hang/danh-sach";
    }

    @GetMapping("/{id}")
    public String chiTietDon(@PathVariable Long id, HttpSession phien, Model moHinh) {
        try {
            moHinh.addAttribute("donHang", dichVuDatHang.layDonCuaKhach(layTaiKhoan(phien), id));
        } catch (LoiNghiepVu loi) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, loi.getMessage());
        }
        return "don-hang/chi-tiet";
    }

    @PostMapping("/{id}/huy")
    public String huyDon(@PathVariable Long id, HttpSession phien, RedirectAttributes thongBao) {
        try {
            dichVuDatHang.huyDonCuaKhach(layTaiKhoan(phien), id);
            thongBao.addFlashAttribute("thongBao", "Đã huỷ đơn hàng #" + id);
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/don-hang/" + id;
    }

    private TaiKhoan layTaiKhoan(HttpSession phien) {
        return (TaiKhoan) phien.getAttribute("taiKhoan");
    }
}
