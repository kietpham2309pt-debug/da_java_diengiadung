package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.edu.huit.diengiadung.entity.TaiKhoan;

public class KhachHangInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Object nguoiDung = request.getSession().getAttribute("taiKhoan");
        if (nguoiDung instanceof TaiKhoan) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/dang-nhap");
        return false;
    }
}
