package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;

import java.util.List;

public interface YeuThichService {

    List<SanPham> layDanhSach(TaiKhoan taiKhoan);

    boolean daYeuThich(TaiKhoan taiKhoan, Long idSanPham);

    boolean doiTrangThai(TaiKhoan taiKhoan, Long idSanPham);
}
