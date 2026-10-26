package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.model.ThongTinGiaoHang;

import java.util.List;

public interface DatHangService {

    ThongTinGiaoHang layThongTinGiaoHangMacDinh(TaiKhoan taiKhoan);

    DonHang datHang(TaiKhoan taiKhoan, GioHang gioHang, ThongTinGiaoHang thongTin);

    List<DonHang> layDonCuaKhach(TaiKhoan taiKhoan);

    DonHang layDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang);

    void huyDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang);
}
