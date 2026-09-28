package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.service.DonHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.List;

@Service
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository donHangRepository;

    public DonHangServiceImpl(DonHangRepository donHangRepository) {
        this.donHangRepository = donHangRepository;
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
    public DonHang layTheoId(Long id) {
        return donHangRepository.findById(id)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng có mã " + id));
    }
}