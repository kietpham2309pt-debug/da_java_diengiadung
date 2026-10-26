package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.service.ThongKeSanPham;
import vn.edu.huit.diengiadung.service.ThongKeService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ThongKeServiceImpl implements ThongKeService {

    private final DonHangRepository donHangRepository;

    public ThongKeServiceImpl(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
    }

    @Override
    public long tinhDoanhThu(int thang, int nam) {
        return donHangRepository.findAll().stream()
                .filter(donHang -> laDonHoanThanhTrongThang(donHang, thang, nam))
                .map(DonHang::getTongTien)
                .filter(tongTien -> tongTien != null)
                .mapToLong(Long::longValue)
                .sum();
    }

    @Override
    public long demDonHoanThanh(int thang, int nam) {
        return donHangRepository.findAll().stream()
                .filter(donHang -> laDonHoanThanhTrongThang(donHang, thang, nam))
                .count();
    }

    @Override
    public List<ThongKeSanPham> laySanPhamBanChay(int thang, int nam) {
        Map<Long, ThongKeSanPham> ketQua = new HashMap<>();

        List<DonHang> danhSachDon = donHangRepository.findAll();

        for (DonHang donHang : danhSachDon) {
            if (!laDonHoanThanhTrongThang(donHang, thang, nam)) {
                continue;
            }

            for (ChiTietDonHang chiTiet : donHang.getDanhSachChiTiet()) {
                SanPham sanPham = chiTiet.getSanPham();

                if (sanPham == null || sanPham.getId() == null) {
                    continue;
                }

                long soLuong = chiTiet.getSoLuong();
                Long thanhTien = chiTiet.getThanhTien();
                long doanhThu = thanhTien == null ? 0L : thanhTien;

                ThongKeSanPham thongKe = ketQua.get(sanPham.getId());

                if (thongKe == null) {
                    thongKe = new ThongKeSanPham(
                            sanPham.getMaSanPham(),
                            sanPham.getTenSanPham(),
                            0L,
                            0L
                    );
                    ketQua.put(sanPham.getId(), thongKe);
                }

                thongKe.themSoLuong(soLuong);
                thongKe.themDoanhThu(doanhThu);
            }
        }

        List<ThongKeSanPham> danhSach = new ArrayList<>(ketQua.values());

        danhSach.sort(
                Comparator.comparingLong(ThongKeSanPham::getSoLuongBan)
                        .reversed()
                        .thenComparing(
                                Comparator.comparingLong(ThongKeSanPham::getDoanhThu)
                                        .reversed()
                        )
        );

        return danhSach.stream().limit(10).toList();
    }

    private boolean laDonHoanThanhTrongThang(
            DonHang donHang,
            int thang,
            int nam) {

        return DonHang.HOAN_THANH.equals(donHang.getTrangThai())
                && donHang.getNgayDat() != null
                && donHang.getNgayDat().getMonthValue() == thang
                && donHang.getNgayDat().getYear() == nam;
    }
}