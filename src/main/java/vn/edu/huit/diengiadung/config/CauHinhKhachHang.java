package vn.edu.huit.diengiadung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CauHinhKhachHang implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new KhachHangInterceptor())
                .addPathPatterns("/dat-hang/**", "/don-hang/**", "/ho-so/**");
    }
}
