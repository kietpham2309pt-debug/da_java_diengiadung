package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;

import java.util.List;

@ControllerAdvice
public class ThongTinChung {

    private final LoaiSanPhamService loaiSanPhamService;

    public ThongTinChung(LoaiSanPhamService loaiSanPhamService) {
        this.loaiSanPhamService = loaiSanPhamService;
    }

    @ModelAttribute("taiKhoanHienTai")
    public TaiKhoan taiKhoanHienTai(HttpSession session) {
        Object nguoiDung = session.getAttribute("taiKhoan");
        if (nguoiDung instanceof TaiKhoan taiKhoan) {
            return taiKhoan;
        }
        return null;
    }

    @ModelAttribute("loaiTrenMenu")
    public List<LoaiSanPham> loaiTrenMenu() {
        return loaiSanPhamService.layTatCa();
    }
}
