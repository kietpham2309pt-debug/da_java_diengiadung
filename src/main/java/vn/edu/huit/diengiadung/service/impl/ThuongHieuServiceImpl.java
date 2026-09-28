package vn.edu.huit.diengiadung.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.ThuongHieuService;

import java.util.List;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {

    private final ThuongHieuRepository thuongHieuRepository;

    @Autowired
    public ThuongHieuServiceImpl(ThuongHieuRepository thuongHieuRepository) {
        this.thuongHieuRepository = thuongHieuRepository;
    }

    @Override
    public List<ThuongHieu> layDanhSach() {
        // Tận dụng hàm đã có sẵn trong ThuongHieuRepository để sắp xếp A-Z
        return thuongHieuRepository.findAllByOrderByTenThuongHieuAsc();
    }

    @Override
    public ThuongHieu layTheoId(Long id) {
        return thuongHieuRepository.findById(id).orElse(null);
    }

    @Override
    public ThuongHieu luu(ThuongHieu thuongHieu) {
        return thuongHieuRepository.save(thuongHieu);
    }

    @Override
    public void xoa(Long id) {
        thuongHieuRepository.deleteById(id);
    }
}