package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.BangGia;
import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.model.ThongTinGiaoHang;

import java.util.List;

public interface DatHangService {

    KhachHang timKhachHang(TaiKhoan taiKhoan);

    ThongTinGiaoHang layThongTinGiaoHangMacDinh(TaiKhoan taiKhoan);

    BangGia tinhGia(ChiTietGioHang chiTiet, TaiKhoan taiKhoan, String maGiamGia);

    DonHang datHang(TaiKhoan taiKhoan, GioHang gioHang, ThongTinGiaoHang thongTin);

    List<DonHang> layDonCuaKhach(TaiKhoan taiKhoan);

    DonHang layDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang);

    DonHang layDonTheoId(Long idDonHang);

    DonHang traCuuDon(String maDon, String soDienThoai);

    void huyDonCuaKhach(TaiKhoan taiKhoan, Long idDonHang, String lyDo, String lyDoKhac);
}
