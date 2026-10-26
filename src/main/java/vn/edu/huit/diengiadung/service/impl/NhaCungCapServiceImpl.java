package vn.edu.huit.diengiadung.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.NhaCungCap;
import vn.edu.huit.diengiadung.repository.NhaCungCapRepository;
import vn.edu.huit.diengiadung.repository.PhieuNhapRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.NhaCungCapService;

import java.util.List;

@Service
public class NhaCungCapServiceImpl implements NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;
    private final PhieuNhapRepository phieuNhapRepository;

    public NhaCungCapServiceImpl(NhaCungCapRepository nhaCungCapRepository, PhieuNhapRepository phieuNhapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.phieuNhapRepository = phieuNhapRepository;
    }

    @Override
    public List<NhaCungCap> layDanhSach() {
        return nhaCungCapRepository.findAllByOrderByTenNhaCungCapAsc();
    }

    @Override
    public List<NhaCungCap> timKiem(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layDanhSach();
        }
        return nhaCungCapRepository.findByTenNhaCungCapContainingIgnoreCaseOrSoDienThoaiContaining(tuKhoa.trim(), tuKhoa.trim());
    }

    @Override
    public NhaCungCap layTheoId(Long id) {
        return nhaCungCapRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public NhaCungCap luu(NhaCungCap nhaCungCap) {
        return nhaCungCapRepository.save(nhaCungCap);
    }

    @Override
    @Transactional
    public void xoa(Long id) {
        NhaCungCap nhaCungCap = layTheoId(id);
        if (nhaCungCap == null) {
            throw new LoiNghiepVu("Không tìm thấy nhà cung cấp có mã " + id);
        }


        long soPhieuNhap = phieuNhapRepository.countByNhaCungCapId(id);
        if (soPhieuNhap > 0) {
            throw new LoiNghiepVu("Không thể xoá! Nhà cung cấp này đã có " + soPhieuNhap + " phiếu nhập kho.");
        }

        nhaCungCapRepository.delete(nhaCungCap);
    }
}