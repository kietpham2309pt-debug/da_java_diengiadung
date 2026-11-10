package vn.edu.huit.diengiadung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CauHinhKhachHang implements WebMvcConfigurer {

    private final KhachHangInterceptor khachHangInterceptor;

    public CauHinhKhachHang(KhachHangInterceptor khachHangInterceptor) {
        this.khachHangInterceptor = khachHangInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(khachHangInterceptor)
                .addPathPatterns("/dat-hang/**", "/don-hang/**", "/ho-so/**");
    }
}
