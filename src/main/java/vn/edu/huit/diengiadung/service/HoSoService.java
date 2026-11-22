package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.dto.HoSoKhachHang;

public interface HoSoService {

    HoSoKhachHang layHoSo(TaiKhoan taiKhoan);

    TaiKhoan capNhatHoSo(TaiKhoan taiKhoan, HoSoKhachHang hoSo);
}
