package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.TaiKhoan;

import java.util.List;

public interface TaiKhoanService {

    TaiKhoan dangKy(TaiKhoan taiKhoan, String soDienThoai, String diaChi);

    TaiKhoan dangNhap(String tenDangNhap, String matKhau);

    List<TaiKhoan> layTatCa();

    List<TaiKhoan> timKiem(String tuKhoa);

    TaiKhoan layTheoId(Long id);

    void doiTrangThai(Long id);

    void datLaiMatKhau(Long id, String matKhauMoi);
}
