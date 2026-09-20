package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.repository.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final LoaiSanPhamRepository loaiSanPhamRepository;
    private final ThuongHieuRepository thuongHieuRepository;
    private final SanPhamRepository sanPhamRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final DonHangRepository donHangRepository;

    public AdminController(LoaiSanPhamRepository loaiSanPhamRepository,
                           ThuongHieuRepository thuongHieuRepository,
                           SanPhamRepository sanPhamRepository,
                           TaiKhoanRepository taiKhoanRepository,
                           DonHangRepository donHangRepository) {
        this.loaiSanPhamRepository = loaiSanPhamRepository;
        this.thuongHieuRepository = thuongHieuRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.taiKhoanRepository = taiKhoanRepository;
        this.donHangRepository = donHangRepository;
    }

    @GetMapping
    public String tongQuan(Model model) {
        model.addAttribute("soLoai", loaiSanPhamRepository.count());
        model.addAttribute("soThuongHieu", thuongHieuRepository.count());
        model.addAttribute("soSanPham", sanPhamRepository.count());
        model.addAttribute("soTaiKhoan", taiKhoanRepository.count());
        model.addAttribute("soDonChoXacNhan", donHangRepository.countByTrangThai(DonHang.CHO_XAC_NHAN));
        model.addAttribute("sanPhamSapHet", sanPhamRepository.findBySoLuongTonLessThanEqual(5));
        return "admin/index";
    }
}
