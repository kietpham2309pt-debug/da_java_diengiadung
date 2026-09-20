package vn.edu.huit.diengiadung.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;

@Component
public class KhoiTaoDuLieu implements CommandLineRunner {

    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder maHoaMatKhau;

    public KhoiTaoDuLieu(TaiKhoanRepository taiKhoanRepository,
                         KhachHangRepository khachHangRepository,
                         PasswordEncoder maHoaMatKhau) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.khachHangRepository = khachHangRepository;
        this.maHoaMatKhau = maHoaMatKhau;
    }

    @Override
    public void run(String... args) {
        if (!taiKhoanRepository.existsByTenDangNhap("admin")) {
            TaiKhoan quanTri = new TaiKhoan();
            quanTri.setTenDangNhap("admin");
            quanTri.setMatKhau(maHoaMatKhau.encode("admin123"));
            quanTri.setHoTen("Quản trị viên");
            quanTri.setEmail("admin@diengiadung.vn");
            quanTri.setVaiTro("ADMIN");
            taiKhoanRepository.save(quanTri);
        }

        if (!taiKhoanRepository.existsByTenDangNhap("khach1")) {
            TaiKhoan taiKhoan = new TaiKhoan();
            taiKhoan.setTenDangNhap("khach1");
            taiKhoan.setMatKhau(maHoaMatKhau.encode("khach123"));
            taiKhoan.setHoTen("Nguyễn Văn Khách");
            taiKhoan.setEmail("khach1@gmail.com");
            taiKhoan.setVaiTro("KHACHHANG");
            TaiKhoan daLuu = taiKhoanRepository.save(taiKhoan);

            KhachHang khachHang = new KhachHang();
            khachHang.setHoTen(daLuu.getHoTen());
            khachHang.setEmail(daLuu.getEmail());
            khachHang.setSoDienThoai("0901234567");
            khachHang.setDiaChi("140 Lê Trọng Tấn, Tân Phú, TP.HCM");
            khachHang.setTaiKhoan(daLuu);
            khachHangRepository.save(khachHang);
        }
    }
}
