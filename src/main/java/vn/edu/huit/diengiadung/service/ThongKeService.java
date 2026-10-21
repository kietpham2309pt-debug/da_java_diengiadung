package vn.edu.huit.diengiadung.service;

import java.util.List;

public interface ThongKeService {

    long tinhDoanhThu(int thang, int nam);

    long demDonHoanThanh(int thang, int nam);

    List<ThongKeSanPham> laySanPhamBanChay(int thang, int nam);
}