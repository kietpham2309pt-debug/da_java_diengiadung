package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.DanhGia;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.TongDanhGia;
import vn.edu.huit.diengiadung.repository.ChiTietDonHangRepository;
import vn.edu.huit.diengiadung.repository.DanhGiaRepository;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.DanhGiaService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DanhGiaServiceImpl implements DanhGiaService {

    private final DanhGiaRepository duLieuDanhGia;
    private final KhachHangRepository duLieuKhachHang;
    private final SanPhamRepository duLieuSanPham;
    private final ChiTietDonHangRepository duLieuChiTiet;

    public DanhGiaServiceImpl(DanhGiaRepository duLieuDanhGia, KhachHangRepository duLieuKhachHang,
                              SanPhamRepository duLieuSanPham, ChiTietDonHangRepository duLieuChiTiet) {
        this.duLieuDanhGia = duLieuDanhGia;
        this.duLieuKhachHang = duLieuKhachHang;
        this.duLieuSanPham = duLieuSanPham;
        this.duLieuChiTiet = duLieuChiTiet;
    }

    private KhachHang timKhach(TaiKhoan taiKhoan) {
        return taiKhoan == null ? null : duLieuKhachHang.findByTaiKhoan(taiKhoan).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DanhGia> layDanhGiaHien(Long idSanPham) {
        return duLieuDanhGia.findBySanPhamIdAndHienTrueOrderByNgayTaoDesc(idSanPham);
    }

    @Override
    @Transactional(readOnly = true)
    public TongDanhGia tongHop(Long idSanPham) {
        TongDanhGia tong = new TongDanhGia();
        for (Object[] dong : duLieuDanhGia.demTheoSao(idSanPham)) {
            tong.cong(((Number) dong[0]).intValue(), ((Number) dong[1]).longValue());
        }
        return tong;
    }

    @Override
    @Transactional(readOnly = true)
    public DanhGia layCuaToi(TaiKhoan taiKhoan, Long idSanPham) {
        KhachHang khach = timKhach(taiKhoan);
        return khach == null ? null : duLieuDanhGia.findBySanPhamIdAndKhachHangId(idSanPham, khach.getId()).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean duocDanhGia(TaiKhoan taiKhoan, Long idSanPham) {
        KhachHang khach = timKhach(taiKhoan);
        return khach != null && duLieuChiTiet.existsByDonHangKhachHangIdAndSanPhamIdAndDonHangTrangThai(
                khach.getId(), idSanPham, DonHang.HOAN_THANH);
    }

    @Override
    @Transactional
    public DanhGia guiDanhGia(TaiKhoan taiKhoan, Long idSanPham, Integer soSao, String noiDung) {
        KhachHang khach = timKhach(taiKhoan);
        if (khach == null) {
            throw new LoiNghiepVu("Vui lòng đăng nhập tài khoản khách hàng để đánh giá");
        }
        if (!duocDanhGia(taiKhoan, idSanPham)) {
            throw new LoiNghiepVu("Chỉ khách đã mua và nhận hàng thành công mới đánh giá được sản phẩm này");
        }
        if (soSao == null || soSao < 1 || soSao > 5) {
            throw new LoiNghiepVu("Vui lòng chọn số sao từ 1 đến 5");
        }
        String noiDungSach = noiDung == null ? "" : noiDung.trim();
        if (noiDungSach.length() > 1000) {
            throw new LoiNghiepVu("Nhận xét tối đa 1000 ký tự");
        }
        SanPham sanPham = duLieuSanPham.findById(idSanPham)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy sản phẩm"));
        DanhGia danhGia = duLieuDanhGia.findBySanPhamIdAndKhachHangId(idSanPham, khach.getId()).orElseGet(DanhGia::new);
        danhGia.setSanPham(sanPham);
        danhGia.setKhachHang(khach);
        danhGia.setSoSao(soSao);
        danhGia.setNoiDung(noiDungSach.isEmpty() ? null : noiDungSach);
        danhGia.setNgayTao(LocalDateTime.now());
        return duLieuDanhGia.save(danhGia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DanhGia> layTatCa(Integer soSao, Boolean hien) {
        return duLieuDanhGia.findAllByOrderByNgayTaoDesc().stream()
                .filter(d -> soSao == null || d.getSoSao() == soSao)
                .filter(d -> hien == null || d.isHien() == hien)
                .toList();
    }

    @Override
    @Transactional
    public void doiHien(Long id) {
        DanhGia danhGia = duLieuDanhGia.findById(id).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đánh giá"));
        danhGia.setHien(!danhGia.isHien());
        duLieuDanhGia.save(danhGia);
    }

    @Override
    @Transactional
    public void xoa(Long id) {
        if (!duLieuDanhGia.existsById(id)) {
            throw new LoiNghiepVu("Không tìm thấy đánh giá");
        }
        duLieuDanhGia.deleteById(id);
    }
}
