package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.ThuongHieuService;

import java.util.List;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {

    private final ThuongHieuRepository thuongHieuRepository;
    private final SanPhamRepository sanPhamRepository;

    public ThuongHieuServiceImpl(ThuongHieuRepository thuongHieuRepository, SanPhamRepository sanPhamRepository) {
        this.thuongHieuRepository = thuongHieuRepository;
        this.sanPhamRepository = sanPhamRepository;
    }

    @Override
    public List<ThuongHieu> layDanhSach() {
        return thuongHieuRepository.findAllByOrderByTenThuongHieuAsc();
    }

    @Override
    public List<ThuongHieu> timKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.isBlank()) {
            return layDanhSach();
        }
        return thuongHieuRepository.findByTenThuongHieuContainingIgnoreCaseOrderByTenThuongHieuAsc(tuKhoa.trim());
    }

    @Override
    public ThuongHieu layTheoId(Long id) {
        return thuongHieuRepository.findById(id)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy thương hiệu có mã " + id));
    }

    @Override
    @Transactional
    public ThuongHieu luu(ThuongHieu thuongHieu) {
        String tenThuongHieu = thuongHieu.getTenThuongHieu().trim();
        thuongHieu.setTenThuongHieu(tenThuongHieu);
        if (thuongHieu.getXuatXu() != null) {
            thuongHieu.setXuatXu(thuongHieu.getXuatXu().trim());
        }


        List<ThuongHieu> danhSachTonTai = thuongHieuRepository.findByTenThuongHieuContainingIgnoreCaseOrderByTenThuongHieuAsc(tenThuongHieu);


        for (ThuongHieu th : danhSachTonTai) {
            if (th.getTenThuongHieu().equalsIgnoreCase(tenThuongHieu) && !th.getId().equals(thuongHieu.getId())) {
                throw new LoiNghiepVu("Thương hiệu \"" + tenThuongHieu + "\" đã tồn tại.");
            }
        }

        return thuongHieuRepository.save(thuongHieu);
    }

    @Override
    @Transactional
    public void xoa(Long id) {
        ThuongHieu thuongHieu = layTheoId(id);


        long soSanPham = sanPhamRepository.countByThuongHieuId(id);
        if (soSanPham > 0) {
            throw new LoiNghiepVu("Không thể xoá! Thương hiệu này đang chứa " + soSanPham + " sản phẩm.");
        }

        thuongHieuRepository.delete(thuongHieu);
    }
}