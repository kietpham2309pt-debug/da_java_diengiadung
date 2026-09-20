package vn.edu.huit.diengiadung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;

import java.util.List;

@Controller
public class TrangChuController {

    private final SanPhamRepository sanPhamRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final LoaiSanPhamService loaiSanPhamService;

    public TrangChuController(SanPhamRepository sanPhamRepository,
                              ThuongHieuRepository thuongHieuRepository,
                              LoaiSanPhamService loaiSanPhamService) {
        this.sanPhamRepository = sanPhamRepository;
        this.thuongHieuRepository = thuongHieuRepository;
        this.loaiSanPhamService = loaiSanPhamService;
    }

    @GetMapping("/")
    public String trangChu(Model model) {
        model.addAttribute("sanPhamMoi", sanPhamRepository.findTop8ByDangBanTrueOrderByIdDesc());
        model.addAttribute("danhSachLoai", loaiSanPhamService.layTatCa());
        model.addAttribute("danhSachThuongHieu", thuongHieuRepository.findAllByOrderByTenThuongHieuAsc());
        return "index";
    }

    @GetMapping("/san-pham")
    public String danhSachSanPham(@RequestParam(required = false) Long loai,
                                  @RequestParam(required = false) String tuKhoa,
                                  Model model) {
        List<SanPham> danhSach;
        String tieuDe = "Tất cả sản phẩm";

        if (loai != null) {
            danhSach = sanPhamRepository.findByLoaiSanPhamIdAndDangBanTrue(loai);
            tieuDe = loaiSanPhamService.layTheoId(loai).getTenLoai();
        } else if (tuKhoa != null && !tuKhoa.isBlank()) {
            danhSach = sanPhamRepository.findByTenSanPhamContainingIgnoreCaseAndDangBanTrue(tuKhoa.trim());
            tieuDe = "Kết quả tìm kiếm cho \"" + tuKhoa.trim() + "\"";
        } else {
            danhSach = sanPhamRepository.findByDangBanTrueOrderByIdDesc();
        }

        model.addAttribute("danhSachSanPham", danhSach);
        model.addAttribute("tieuDe", tieuDe);
        model.addAttribute("tuKhoa", tuKhoa);
        return "san-pham";
    }
}
