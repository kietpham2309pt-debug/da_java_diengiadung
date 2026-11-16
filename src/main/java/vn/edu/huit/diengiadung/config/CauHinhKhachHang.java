package vn.edu.huit.diengiadung.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;

@Configuration
public class CauHinhKhachHang implements WebMvcConfigurer {

    private final TaiKhoanRepository duLieuTaiKhoan;

    public CauHinhKhachHang(TaiKhoanRepository duLieuTaiKhoan) {
        this.duLieuTaiKhoan = duLieuTaiKhoan;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new KhachHangInterceptor(duLieuTaiKhoan, true))
                .addPathPatterns("/don-hang", "/don-hang/*/huy", "/ho-so/**", "/yeu-thich/**");
        registry.addInterceptor(new KhachHangInterceptor(duLieuTaiKhoan, false))
                .addPathPatterns("/dat-hang/**", "/dat-hang", "/don-hang/*", "/don-hang/*/thanh-toan");
    }
}
