package vn.edu.huit.diengiadung.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.TaiKhoanService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final KhachHangRepository khachHangRepository;
    private final PasswordEncoder maHoaMatKhau;

    public TaiKhoanServiceImpl(TaiKhoanRepository taiKhoanRepository,
                               KhachHangRepository khachHangRepository,
                               PasswordEncoder maHoaMatKhau) {
        this.taiKhoanRepository = taiKhoanRepository;
        this.khachHangRepository = khachHangRepository;
        this.maHoaMatKhau = maHoaMatKhau;
    }

    @Override
    @Transactional
    public TaiKhoan dangKy(TaiKhoan taiKhoan, String soDienThoai, String diaChi) {
        if (taiKhoanRepository.existsByTenDangNhap(taiKhoan.getTenDangNhap())) {
            throw new LoiNghiepVu("Tên đăng nhập đã có người dùng");
        }
        taiKhoan.setId(null);
        taiKhoan.setMatKhau(maHoaMatKhau.encode(taiKhoan.getMatKhau()));
        taiKhoan.setVaiTro("KHACHHANG");
        taiKhoan.setNgayTao(LocalDateTime.now());
        taiKhoan.setHoatDong(true);
        TaiKhoan daLuu = taiKhoanRepository.save(taiKhoan);

        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(daLuu.getHoTen());
        khachHang.setEmail(daLuu.getEmail());
        khachHang.setSoDienThoai(soDienThoai);
        khachHang.setDiaChi(diaChi);
        khachHang.setTaiKhoan(daLuu);
        khachHangRepository.save(khachHang);

        return daLuu;
    }

    @Override
    public TaiKhoan dangNhap(String tenDangNhap, String matKhau) {
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() -> new LoiNghiepVu("Sai tên đăng nhập hoặc mật khẩu"));
        if (!maHoaMatKhau.matches(matKhau, taiKhoan.getMatKhau())) {
            throw new LoiNghiepVu("Sai tên đăng nhập hoặc mật khẩu");
        }
        if (!taiKhoan.isHoatDong()) {
            throw new LoiNghiepVu("Tài khoản đang bị khoá");
        }
        return taiKhoan;
    }

    @Override
    @Transactional(readOnly = true)
    public Long xacMinhQuenMatKhau(String tenDangNhap, String lienHe) {
        if (tenDangNhap == null || tenDangNhap.isBlank() || lienHe == null || lienHe.isBlank()) {
            throw new LoiNghiepVu("Vui lòng nhập tên đăng nhập và email hoặc số điện thoại đã đăng ký");
        }
        String khoa = lienHe.trim().replaceAll("\\s", "");
        LoiNghiepVu khongKhop = new LoiNghiepVu("Thông tin xác minh không khớp với tài khoản nào. Vui lòng kiểm tra lại.");
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap.trim()).orElseThrow(() -> khongKhop);
        if (taiKhoan.laQuanTri() || !taiKhoan.isHoatDong()) {
            throw khongKhop;
        }
        KhachHang khachHang = khachHangRepository.findByTaiKhoan(taiKhoan).orElse(null);
        boolean khop = khoa.equalsIgnoreCase(taiKhoan.getEmail() == null ? "" : taiKhoan.getEmail().trim())
                || (khachHang != null && (khoa.equalsIgnoreCase(khachHang.getEmail() == null ? "" : khachHang.getEmail().trim())
                || khoa.equals(khachHang.getSoDienThoai())));
        if (!khop) {
            throw khongKhop;
        }
        return taiKhoan.getId();
    }

    @Override
    public List<TaiKhoan> layTatCa() {
        return taiKhoanRepository.findAll();
    }

    @Override
    public List<TaiKhoan> timKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return layTatCa();
        }
        return taiKhoanRepository
                .findByHoTenContainingIgnoreCaseOrTenDangNhapContainingIgnoreCase(tuKhoa, tuKhoa);
    }

    @Override
    public TaiKhoan layTheoId(Long id) {
        return taiKhoanRepository.findById(id)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy tài khoản có mã " + id));
    }

    @Override
    @Transactional
    public void doiTrangThai(Long id) {
        TaiKhoan taiKhoan = layTheoId(id);
        if (taiKhoan.laQuanTri() && taiKhoan.isHoatDong()) {
            throw new LoiNghiepVu("Không được khoá tài khoản quản trị");
        }
        taiKhoan.setHoatDong(!taiKhoan.isHoatDong());
        taiKhoanRepository.save(taiKhoan);
    }

    @Override
    @Transactional
    public void datLaiMatKhau(Long id, String matKhauMoi) {
        if (matKhauMoi == null || matKhauMoi.length() < 6) {
            throw new LoiNghiepVu("Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        TaiKhoan taiKhoan = layTheoId(id);
        taiKhoan.setMatKhau(maHoaMatKhau.encode(matKhauMoi));
        taiKhoanRepository.save(taiKhoan);
    }

    @Override
    @Transactional
    public void doiMatKhau(Long id, String matKhauCu, String matKhauMoi, String nhapLai) {
        TaiKhoan taiKhoan = layTheoId(id);
        if (matKhauCu == null || !maHoaMatKhau.matches(matKhauCu, taiKhoan.getMatKhau())) {
            throw new LoiNghiepVu("Mật khẩu hiện tại không đúng");
        }
        if (matKhauMoi == null || matKhauMoi.length() < 6) {
            throw new LoiNghiepVu("Mật khẩu mới phải có ít nhất 6 ký tự");
        }
        if (!matKhauMoi.equals(nhapLai)) {
            throw new LoiNghiepVu("Hai lần nhập mật khẩu mới không khớp nhau");
        }
        if (maHoaMatKhau.matches(matKhauMoi, taiKhoan.getMatKhau())) {
            throw new LoiNghiepVu("Mật khẩu mới phải khác mật khẩu hiện tại");
        }
        taiKhoan.setMatKhau(maHoaMatKhau.encode(matKhauMoi));
        taiKhoanRepository.save(taiKhoan);
    }
}
