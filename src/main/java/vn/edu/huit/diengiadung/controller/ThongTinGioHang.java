package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.huit.diengiadung.model.GioHang;

@ControllerAdvice
public class ThongTinGioHang {

    @ModelAttribute("soMonTrongGio")
    public int soMonTrongGio(HttpSession phien) {
        if (phien.getAttribute(GioHangController.KHOA_GIO_HANG) instanceof GioHang gioHang) {
            return gioHang.getTongSoLuong();
        }
        return 0;
    }
}
