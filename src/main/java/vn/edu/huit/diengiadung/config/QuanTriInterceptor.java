package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.edu.huit.diengiadung.entity.TaiKhoan;

public class QuanTriInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Object nguoiDung = session.getAttribute("taiKhoan");
        if (nguoiDung instanceof TaiKhoan taiKhoan && taiKhoan.laQuanTri()) {
            return true;
        }
        response.sendRedirect(request.getContextPath() + "/dang-nhap?loi=quyen");
        return false;
    }
}
