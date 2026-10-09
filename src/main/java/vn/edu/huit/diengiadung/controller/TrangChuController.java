package vn.edu.huit.diengiadung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.SanPhamService;

@Controller
public class TrangChuController {

    private static final int SO_SAN_PHAM_MOI_KHOI = 6;

    private final SanPhamService dichVuSanPham;
    private final ThuongHieuRepository duLieuThuongHieu;
    private final LoaiSanPhamService dichVuLoaiSanPham;

    public TrangChuController(SanPhamService dichVuSanPham,
                              ThuongHieuRepository duLieuThuongHieu,
                              LoaiSanPhamService dichVuLoaiSanPham) {
        this.dichVuSanPham = dichVuSanPham;
        this.duLieuThuongHieu = duLieuThuongHieu;
        this.dichVuLoaiSanPham = dichVuLoaiSanPham;
    }

    @GetMapping("/")
    public String trangChu(Model moHinh) {
        moHinh.addAttribute("danhSachLoai", dichVuLoaiSanPham.layTatCa());
        moHinh.addAttribute("danhSachThuongHieu", duLieuThuongHieu.findAllByOrderByTenThuongHieuAsc());
        moHinh.addAttribute("sanPhamMoi", dichVuSanPham.laySanPhamMoi(SO_SAN_PHAM_MOI_KHOI));
        moHinh.addAttribute("sanPhamGiamGia", dichVuSanPham.laySanPhamGiamGia(SO_SAN_PHAM_MOI_KHOI));
        return "index";
    }
}
