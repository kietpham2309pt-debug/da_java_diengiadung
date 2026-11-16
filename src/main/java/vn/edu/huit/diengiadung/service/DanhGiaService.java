package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.DanhGia;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.TongDanhGia;

import java.util.List;

public interface DanhGiaService {

    List<DanhGia> layDanhGiaHien(Long idSanPham);

    TongDanhGia tongHop(Long idSanPham);

    DanhGia layCuaToi(TaiKhoan taiKhoan, Long idSanPham);

    boolean duocDanhGia(TaiKhoan taiKhoan, Long idSanPham);

    DanhGia guiDanhGia(TaiKhoan taiKhoan, Long idSanPham, Integer soSao, String noiDung);

    List<DanhGia> layTatCa(Integer soSao, Boolean hien);

    void doiHien(Long id);

    void xoa(Long id);
}
