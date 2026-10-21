package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.DonHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.List;

@Service
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;
    private final SanPhamRepository sanPhamRepository;

    public DonHangServiceImpl(DonHangRepository donHangRepository, SanPhamRepository sanPhamRepository) {
        this.donHangRepository = donHangRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public List<DonHang> layTatCa() {
        return donHangRepository.findAllByOrderByNgayDatDesc();
    }

    @Override
    public List<DonHang> timTheoTrangThai(String trangThai) {
        if (trangThai == null || trangThai.isBlank()) {
            return layTatCa();
        }
        return donHangRepository.findByTrangThaiOrderByNgayDatDesc(trangThai);
    }

    @Override
    @Transactional(readOnly = true)
    public DonHang layTheoId(Long id) {
        return donHangRepository.findById(id).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng có mã " + id));
    }

    @Override
    @Transactional
    public void capNhatTrangThai(Long id, String trangThaiMoi) {
        DonHang donHang = layTheoId(id);
        String trangThaiCu = donHang.getTrangThai();
        boolean hopLe = (DonHang.CHO_XAC_NHAN.equals(trangThaiCu)
                        && DonHang.DA_XAC_NHAN.equals(trangThaiMoi))
                        || (DonHang.DA_XAC_NHAN.equals(trangThaiCu)
                        && DonHang.DANG_GIAO.equals(trangThaiMoi))
                        || (DonHang.DANG_GIAO.equals(trangThaiCu)
                        && DonHang.HOAN_THANH.equals(trangThaiMoi));
        if (!hopLe) {
            throw new LoiNghiepVu("Không thể chuyển đơn hàng từ trạng thái " + donHang.getTenTrangThai() + " sang trạng thái được chọn.");
        }

        donHang.setTrangThai(trangThaiMoi);
        donHangRepository.save(donHang);
    }

    @Override
    @Transactional
    public void huyDonHang(Long id) {
        DonHang donHang = layTheoId(id);
        String trangThai = donHang.getTrangThai();

        if (DonHang.DA_HUY.equals(trangThai)) {
            throw new LoiNghiepVu("Đơn hàng này đã được hủy trước đó.");
        }
        if (!DonHang.CHO_XAC_NHAN.equals(trangThai)
                && !DonHang.DA_XAC_NHAN.equals(trangThai)) {
            throw new LoiNghiepVu("Chỉ có thể hủy đơn đang chờ xác nhận hoặc đã xác nhận.");
        }
        for (ChiTietDonHang chiTiet : donHang.getDanhSachChiTiet()) {
            SanPham sanPham = chiTiet.getSanPham();
            if (sanPham == null) {
                throw new LoiNghiepVu("Không tìm thấy sản phẩm trong chi tiết đơn hàng.");
            }
            sanPham.setSoLuongTon(sanPham.getSoLuongTon() + chiTiet.getSoLuong());
            sanPhamRepository.save(sanPham);
        }
        donHang.setTrangThai(DonHang.DA_HUY);
        donHangRepository.save(donHang);
    }
}
