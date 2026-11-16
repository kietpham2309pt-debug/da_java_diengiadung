package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;

public class KhachHangInterceptor implements HandlerInterceptor {

    private final TaiKhoanRepository duLieuTaiKhoan;
    private final boolean batBuocDangNhap;

    public KhachHangInterceptor(TaiKhoanRepository duLieuTaiKhoan, boolean batBuocDangNhap) {
        this.duLieuTaiKhoan = duLieuTaiKhoan;
        this.batBuocDangNhap = batBuocDangNhap;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession phien = request.getSession();
        Object nguoiDung = phien.getAttribute("taiKhoan");
        if (!(nguoiDung instanceof TaiKhoan taiKhoan)) {
            if (!batBuocDangNhap) {
                return true;
            }
            response.sendRedirect(request.getContextPath() + "/dang-nhap");
            return false;
        }
        boolean conHoatDong = duLieuTaiKhoan.findById(taiKhoan.getId())
                .map(TaiKhoan::isHoatDong)
                .orElse(false);
        if (!conHoatDong) {
            phien.invalidate();
            response.sendRedirect(request.getContextPath() + "/dang-nhap?loi=khoa");
            return false;
        }
        return true;
    }
}
