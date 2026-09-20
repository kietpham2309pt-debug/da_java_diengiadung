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
        taiKhoan.setMatKhau(maHoaMatKhau.encode(taiKhoan.getMatKhau()));
        taiKhoan.setVaiTro("KHACHHANG");
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
}
