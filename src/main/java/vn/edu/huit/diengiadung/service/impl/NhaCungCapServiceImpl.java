package vn.edu.huit.diengiadung.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.NhaCungCap;
import vn.edu.huit.diengiadung.repository.NhaCungCapRepository;
import vn.edu.huit.diengiadung.service.NhaCungCapService;

import java.util.List;

@Service
public class NhaCungCapServiceImpl implements NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;

    @Autowired
    public NhaCungCapServiceImpl(NhaCungCapRepository nhaCungCapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
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
        nhaCungCapRepository.deleteById(id);
    }
}