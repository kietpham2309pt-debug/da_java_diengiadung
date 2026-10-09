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

    private final SanPhamService sanPhamService;
    private final ThuongHieuRepository thuongHieuRepository;
    private final LoaiSanPhamService loaiSanPhamService;

    public TrangChuController(SanPhamService sanPhamService,
                              ThuongHieuRepository thuongHieuRepository,
                              LoaiSanPhamService loaiSanPhamService) {
        this.sanPhamService = sanPhamService;
        this.thuongHieuRepository = thuongHieuRepository;
        this.loaiSanPhamService = loaiSanPhamService;
    }

    @GetMapping("/")
    public String trangChu(Model moHinh) {
        moHinh.addAttribute("danhSachLoai", loaiSanPhamService.layTatCa());
        moHinh.addAttribute("danhSachThuongHieu", thuongHieuRepository.findAllByOrderByTenThuongHieuAsc());
        moHinh.addAttribute("sanPhamMoi", sanPhamService.laySanPhamMoi(SO_SAN_PHAM_MOI_KHOI));
        moHinh.addAttribute("sanPhamGiamGia", sanPhamService.laySanPhamGiamGia(SO_SAN_PHAM_MOI_KHOI));
        return "index";
    }
}
