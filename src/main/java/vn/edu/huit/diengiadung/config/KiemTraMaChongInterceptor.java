package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class KiemTraMaChongInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        if (MaChongGiaMao.hopLe(request.getSession(false), request.getParameter(MaChongGiaMao.TEN_TRUONG))) {
            return true;
        }
        response.sendError(HttpServletResponse.SC_FORBIDDEN,
                "Phiên làm việc đã hết hạn hoặc yêu cầu không hợp lệ. Hãy tải lại trang rồi thử lại");
        return false;
    }
}
