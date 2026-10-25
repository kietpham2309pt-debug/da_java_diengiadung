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
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.PhieuNhapService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
        if (phieuNhap.getNhaCungCap() == null || phieuNhap.getNhaCungCap().getId() == null) {
            throw new LoiNghiepVu("Vui lòng chọn nhà cung cấp!");
        }

        if (phieuNhap.getDanhSachChiTiet() == null || phieuNhap.getDanhSachChiTiet().isEmpty()) {
            throw new LoiNghiepVu("Phiếu nhập phải có ít nhất một sản phẩm!");
        }

        List<ChiTietPhieuNhap> chiTietHopLe = new ArrayList<>();
        long tongTien = 0L;

        for (ChiTietPhieuNhap chiTiet : phieuNhap.getDanhSachChiTiet()) {
            if (chiTiet.getSanPham() == null || chiTiet.getSanPham().getId() == null) {
                continue;
            }
            if (chiTiet.getSoLuong() <= 0) {
                throw new LoiNghiepVu("Số lượng nhập phải lớn hơn 0!");
            }
            if (chiTiet.getGiaNhap() == null || chiTiet.getGiaNhap() <= 0) {
                throw new LoiNghiepVu("Giá nhập phải lớn hơn 0!");
            }

            SanPham sanPham = sanPhamRepository.findById(chiTiet.getSanPham().getId())
                    .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy sản phẩm!"));


            sanPham.setSoLuongTon(sanPham.getSoLuongTon() + chiTiet.getSoLuong());
            sanPhamRepository.save(sanPham);


            chiTiet.setSanPham(sanPham);
            chiTiet.setPhieuNhap(phieuNhap);

            tongTien += (long) chiTiet.getSoLuong() * chiTiet.getGiaNhap();
            chiTietHopLe.add(chiTiet);
        }

        if (chiTietHopLe.isEmpty()) {
            throw new LoiNghiepVu("Vui lòng chọn ít nhất một sản phẩm hợp lệ!");
        }

        phieuNhap.setNgayNhap(LocalDateTime.now());
        phieuNhap.setTongTien(tongTien);
        phieuNhap.setDanhSachChiTiet(chiTietHopLe);


        phieuNhapRepository.save(phieuNhap);
    }
}