package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.YeuThichService;

import java.util.ArrayList;
import java.util.List;

@Service
public class YeuThichServiceImpl implements YeuThichService {

    private final KhachHangRepository duLieuKhachHang;
    private final SanPhamRepository duLieuSanPham;

    public YeuThichServiceImpl(KhachHangRepository duLieuKhachHang, SanPhamRepository duLieuSanPham) {
        this.duLieuKhachHang = duLieuKhachHang;
        this.duLieuSanPham = duLieuSanPham;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SanPham> layDanhSach(TaiKhoan taiKhoan) {
        return new ArrayList<>(layKhachHang(taiKhoan).getSanPhamYeuThich());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean daYeuThich(TaiKhoan taiKhoan, Long idSanPham) {
        if (taiKhoan == null) {
            return false;
        }
        return duLieuKhachHang.findByTaiKhoan(taiKhoan)
                .map(khachHang -> khachHang.getSanPhamYeuThich().stream()
                        .anyMatch(sanPham -> sanPham.getId().equals(idSanPham)))
                .orElse(false);
    }

    @Override
    @Transactional
    public boolean doiTrangThai(TaiKhoan taiKhoan, Long idSanPham) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        SanPham sanPham = duLieuSanPham.findById(idSanPham)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy sản phẩm có mã " + idSanPham));
        boolean daCo = khachHang.getSanPhamYeuThich().removeIf(sp -> sp.getId().equals(idSanPham));
        if (!daCo) {
            khachHang.getSanPhamYeuThich().add(sanPham);
        }
        duLieuKhachHang.save(khachHang);
        return !daCo;
    }

    private KhachHang layKhachHang(TaiKhoan taiKhoan) {
        return duLieuKhachHang.findByTaiKhoan(taiKhoan)
                .orElseThrow(() -> new LoiNghiepVu("Tài khoản này không có hồ sơ khách hàng"));
    }
}
