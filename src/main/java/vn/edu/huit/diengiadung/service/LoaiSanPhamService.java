package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.LoaiSanPham;

import java.util.List;

public interface LoaiSanPhamService {

    List<LoaiSanPham> layTatCa();

    List<LoaiSanPham> timKiem(String tuKhoa);

    LoaiSanPham layTheoId(Long id);

    LoaiSanPham luu(LoaiSanPham loaiSanPham);

    void xoa(Long id);

    long demSanPham(Long id);
}
