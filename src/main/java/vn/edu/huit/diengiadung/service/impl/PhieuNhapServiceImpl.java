package vn.edu.huit.diengiadung.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.ChiTietPhieuNhap;
import vn.edu.huit.diengiadung.entity.PhieuNhap;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.ChiTietPhieuNhapRepository;
import vn.edu.huit.diengiadung.repository.PhieuNhapRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.PhieuNhapService;

import java.time.LocalDateTime;

@Service
public class PhieuNhapServiceImpl implements PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final SanPhamRepository sanPhamRepository;

    @Autowired
    public PhieuNhapServiceImpl(PhieuNhapRepository phieuNhapRepository,
                                ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
                                SanPhamRepository sanPhamRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    @Transactional
    public void luuQuyTrinhNhapHang(PhieuNhap phieuNhap) {
        phieuNhap.setNgayNhap(LocalDateTime.now());

        long tongTien = 0;
        if (phieuNhap.getDanhSachChiTiet() != null) {
            for (ChiTietPhieuNhap chiTiet : phieuNhap.getDanhSachChiTiet()) {
                tongTien += chiTiet.getSoLuong() * chiTiet.getGiaNhap();
            }
        }
        phieuNhap.setTongTien(tongTien);

        PhieuNhap phieuNhapDaLuu = phieuNhapRepository.save(phieuNhap);

        if (phieuNhap.getDanhSachChiTiet() != null) {
            for (ChiTietPhieuNhap chiTiet : phieuNhap.getDanhSachChiTiet()) {
                chiTiet.setPhieuNhap(phieuNhapDaLuu);
                chiTietPhieuNhapRepository.save(chiTiet);

                SanPham sanPham = sanPhamRepository.findById(chiTiet.getSanPham().getId())
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

                sanPham.setSoLuongTon(sanPham.getSoLuongTon() + chiTiet.getSoLuong());
                sanPhamRepository.save(sanPham);
            }
        }
    }
}