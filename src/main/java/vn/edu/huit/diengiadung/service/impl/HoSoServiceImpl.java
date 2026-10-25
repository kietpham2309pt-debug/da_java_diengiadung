package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.HoSoKhachHang;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;
import vn.edu.huit.diengiadung.service.HoSoService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

@Service
public class HoSoServiceImpl implements HoSoService {

    private final KhachHangRepository duLieuKhachHang;
    private final TaiKhoanRepository duLieuTaiKhoan;

    public HoSoServiceImpl(KhachHangRepository duLieuKhachHang, TaiKhoanRepository duLieuTaiKhoan) {
        this.duLieuKhachHang = duLieuKhachHang;
        this.duLieuTaiKhoan = duLieuTaiKhoan;
    }

    @Override
    @Transactional(readOnly = true)
    public HoSoKhachHang layHoSo(TaiKhoan taiKhoan) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        HoSoKhachHang hoSo = new HoSoKhachHang();
        hoSo.setHoTen(khachHang.getHoTen());
        hoSo.setEmail(khachHang.getEmail());
        hoSo.setSoDienThoai(khachHang.getSoDienThoai());
        hoSo.setDiaChi(khachHang.getDiaChi());
        return hoSo;
    }

    @Override
    @Transactional
    public TaiKhoan capNhatHoSo(TaiKhoan taiKhoan, HoSoKhachHang hoSo) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        String email = hoSo.getEmail() == null || hoSo.getEmail().isBlank() ? null : hoSo.getEmail().trim();
        khachHang.setHoTen(hoSo.getHoTen().trim());
        khachHang.setEmail(email);
        khachHang.setSoDienThoai(hoSo.getSoDienThoai().trim());
        khachHang.setDiaChi(hoSo.getDiaChi() == null ? null : hoSo.getDiaChi().trim());
        duLieuKhachHang.save(khachHang);

        TaiKhoan taiKhoanDaLuu = duLieuTaiKhoan.findById(taiKhoan.getId())
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy tài khoản"));
        taiKhoanDaLuu.setHoTen(khachHang.getHoTen());
        taiKhoanDaLuu.setEmail(email);
        return duLieuTaiKhoan.save(taiKhoanDaLuu);
    }

    private KhachHang layKhachHang(TaiKhoan taiKhoan) {
        return duLieuKhachHang.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new LoiNghiepVu("Tài khoản này không có hồ sơ khách hàng"));
    }
}
