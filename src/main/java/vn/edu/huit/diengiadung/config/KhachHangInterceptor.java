package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;

@Component
public class KhachHangInterceptor implements HandlerInterceptor {

    private final TaiKhoanRepository duLieuTaiKhoan;

    public KhachHangInterceptor(TaiKhoanRepository duLieuTaiKhoan) {
        this.duLieuTaiKhoan = duLieuTaiKhoan;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession phien = request.getSession();
        Object nguoiDung = phien.getAttribute("taiKhoan");
        if (!(nguoiDung instanceof TaiKhoan taiKhoan)) {
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
