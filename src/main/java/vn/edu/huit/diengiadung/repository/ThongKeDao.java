package vn.edu.huit.diengiadung.repository;

import vn.edu.huit.diengiadung.service.ThongKeSanPham;

import java.time.LocalDateTime;
import java.util.List;

public interface ThongKeDao {

    long tongDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay);

    long demDonHoanThanh(LocalDateTime tuNgay, LocalDateTime denNgay);

    List<ThongKeSanPham> sanPhamBanChay(LocalDateTime tuNgay, LocalDateTime denNgay, int soLuong);
}
