package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.repository.LoaiSanPhamRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.List;
import java.util.Optional;

@Service
public class LoaiSanPhamServiceImpl implements LoaiSanPhamService {

    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final SanPhamRepository sanPhamRepository;

    public LoaiSanPhamServiceImpl(LoaiSanPhamRepository loaiSanPhamRepository,
                                  SanPhamRepository sanPhamRepository) {
        this.loaiSanPhamRepository = loaiSanPhamRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public List<LoaiSanPham> layTatCa() {
        return loaiSanPhamRepository.findAllByOrderByTenLoaiAsc();
    }

    @Override
    public List<LoaiSanPham> timKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return layTatCa();
        }
        return loaiSanPhamRepository.findByTenLoaiContainingIgnoreCaseOrderByTenLoaiAsc(tuKhoa.trim());
    }

    @Override
    public LoaiSanPham layTheoId(Long id) {
        return loaiSanPhamRepository.findById(id)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy loại sản phẩm có mã " + id));
    }

    @Override
    @Transactional
    public LoaiSanPham luu(LoaiSanPham loaiSanPham) {
        String tenLoai = loaiSanPham.getTenLoai().trim();
        String maLoai = loaiSanPham.getMaLoai().trim().toLowerCase();
        loaiSanPham.setTenLoai(tenLoai);
        loaiSanPham.setMaLoai(maLoai);

        Optional<LoaiSanPham> trungTen = loaiSanPhamRepository.findByTenLoaiIgnoreCase(tenLoai);
        if (trungTen.isPresent() && !trungTen.get().getId().equals(loaiSanPham.getId())) {
            throw new LoiNghiepVu("Loại sản phẩm \"" + tenLoai + "\" đã tồn tại");
        }

        Optional<LoaiSanPham> trungMa = loaiSanPhamRepository.findByMaLoaiIgnoreCase(maLoai);
        if (trungMa.isPresent() && !trungMa.get().getId().equals(loaiSanPham.getId())) {
            throw new LoiNghiepVu("Mã loại \"" + maLoai + "\" đã được dùng cho loại khác");
        }

        return loaiSanPhamRepository.save(loaiSanPham);
    }

    @Override
    @Transactional
    public void xoa(Long id) {
        LoaiSanPham loaiSanPham = layTheoId(id);
        long soSanPham = sanPhamRepository.countByLoaiSanPhamId(id);
        if (soSanPham > 0) {
            throw new LoiNghiepVu("Không xoá được vì loại này còn " + soSanPham + " sản phẩm");
        }
        loaiSanPhamRepository.delete(loaiSanPham);
    }

    @Override
    public long demSanPham(Long id) {
        return sanPhamRepository.countByLoaiSanPhamId(id);
    }
}
