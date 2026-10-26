package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.service.GioHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/gio-hang")
public class GioHangController {

    public static final String KHOA_GIO_HANG = "gioHang";

    private final GioHangService dichVuGioHang;

    public GioHangController(GioHangService dichVuGioHang) {
        this.dichVuGioHang = dichVuGioHang;
    }

    @GetMapping
    public String xemGioHang(HttpSession phien, Model moHinh) {
        moHinh.addAttribute("chiTiet", dichVuGioHang.xemChiTiet(layGioHang(phien)));
        return "gio-hang";
    }

    @PostMapping("/them")
    public String them(@RequestParam Long id,
                       @RequestParam(defaultValue = "1") String soLuong,
                       HttpSession phien,
                       RedirectAttributes thongBao) {
        try {
            dichVuGioHang.them(layGioHang(phien), id, docSoLuong(soLuong));
            thongBao.addFlashAttribute("thongBao", "Đã thêm sản phẩm vào giỏ hàng");
            return "redirect:/gio-hang";
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/gio-hang";
        }
    }

    @PostMapping("/cap-nhat")
    public String capNhat(@RequestParam Long id,
                          @RequestParam String soLuong,
                          HttpSession phien,
                          RedirectAttributes thongBao) {
        try {
            dichVuGioHang.capNhat(layGioHang(phien), id, docSoLuong(soLuong));
            thongBao.addFlashAttribute("thongBao", "Đã cập nhật giỏ hàng");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/gio-hang";
    }

    @PostMapping("/xoa")
    public String xoa(@RequestParam Long id, HttpSession phien, RedirectAttributes thongBao) {
        dichVuGioHang.xoa(layGioHang(phien), id);
        thongBao.addFlashAttribute("thongBao", "Đã xoá sản phẩm khỏi giỏ hàng");
        return "redirect:/gio-hang";
    }

    public static GioHang layGioHang(HttpSession phien) {
        if (phien.getAttribute(KHOA_GIO_HANG) instanceof GioHang gioHang) {
            return gioHang;
        }
        GioHang gioHangMoi = new GioHang();
        phien.setAttribute(KHOA_GIO_HANG, gioHangMoi);
        return gioHangMoi;
    }

    private int docSoLuong(String giaTri) {
        try {
            return Integer.parseInt(giaTri.trim());
        } catch (NumberFormatException loi) {
            throw new LoiNghiepVu("Số lượng phải là số nguyên từ 1 trở lên");
        }
    }
}
