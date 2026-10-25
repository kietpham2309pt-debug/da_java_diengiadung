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
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.model.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.GioHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Controller
@RequestMapping("/dat-hang")
public class DatHangController {

    private final DatHangService dichVuDatHang;
    private final GioHangService dichVuGioHang;

    public DatHangController(DatHangService dichVuDatHang, GioHangService dichVuGioHang) {
        this.dichVuDatHang = dichVuDatHang;
        this.dichVuGioHang = dichVuGioHang;
    }

    @GetMapping
    public String formDatHang(HttpSession phien, Model moHinh, RedirectAttributes thongBao) {
        GioHang gioHang = GioHangController.layGioHang(phien);
        ChiTietGioHang chiTiet = dichVuGioHang.xemChiTiet(gioHang);
        if (chiTiet.isTrong()) {
            thongBao.addFlashAttribute("thongBaoLoi", "Giỏ hàng đang trống nên chưa thể đặt hàng");
            return "redirect:/gio-hang";
        }
        try {
            moHinh.addAttribute("thongTin", dichVuDatHang.layThongTinGiaoHangMacDinh(layTaiKhoan(phien)));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/gio-hang";
        }
        moHinh.addAttribute("chiTiet", chiTiet);
        return "dat-hang";
    }

    @PostMapping
    public String datHang(@Valid @ModelAttribute("thongTin") ThongTinGiaoHang thongTin,
                          BindingResult ketQuaKiemTra,
                          HttpSession phien,
                          Model moHinh,
                          RedirectAttributes thongBao) {
        GioHang gioHang = GioHangController.layGioHang(phien);
        if (ketQuaKiemTra.hasErrors()) {
            moHinh.addAttribute("chiTiet", dichVuGioHang.xemChiTiet(gioHang));
            return "dat-hang";
        }
        try {
            DonHang donHang = dichVuDatHang.datHang(layTaiKhoan(phien), gioHang, thongTin);
            gioHang.xoaHet();
            thongBao.addFlashAttribute("thongBao", "Đặt hàng thành công. Cửa hàng sẽ sớm liên hệ xác nhận đơn");
            return "redirect:/don-hang/" + donHang.getId();
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/gio-hang";
        }
    }

    private TaiKhoan layTaiKhoan(HttpSession phien) {
        return (TaiKhoan) phien.getAttribute("taiKhoan");
    }
}
