package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.service.KhachHangService;

import java.util.List;
import java.util.Locale;

@Service
public class KhachHangServiceImpl implements KhachHangService {

    private final KhachHangRepository khachHangRepository;

    public KhachHangServiceImpl(KhachHangRepository khachHangRepository) {
        this.khachHangRepository = khachHangRepository;
    }

    @Override
    public List<KhachHang> timKiem(String tuKhoa) {
        List<KhachHang> danhSach = khachHangRepository.findAll();

        if (tuKhoa == null || tuKhoa.isBlank()) {
            return danhSach;
        }

        String tuKhoaNho = tuKhoa.trim().toLowerCase(Locale.ROOT);

        return danhSach.stream()
                .filter(khachHang ->
                        (khachHang.getHoTen() != null
                                && khachHang.getHoTen().toLowerCase(Locale.ROOT)
                                .contains(tuKhoaNho))
                                || (khachHang.getSoDienThoai() != null
                                && khachHang.getSoDienThoai().contains(tuKhoa.trim()))
                                || (khachHang.getEmail() != null
                                && khachHang.getEmail().toLowerCase(Locale.ROOT)
                                .contains(tuKhoaNho))
                )
                .toList();
    }
}
