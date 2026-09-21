package vn.edu.huit.diengiadung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;

import java.util.ArrayList;
import java.util.Comparator;
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
        model.addAttribute("danhSachLoai", loaiSanPhamService.layTatCa());
        model.addAttribute("danhSachThuongHieu", thuongHieuRepository.findAllByOrderByTenThuongHieuAsc());
        return "index";
    }

    @GetMapping("/san-pham")
    public String danhSachSanPham(@RequestParam(required = false) Long loai,
                                  @RequestParam(required = false) String tuKhoa,
                                  @RequestParam(required = false) List<Long> hang,
                                  @RequestParam(required = false) String sapXep,
                                  Model model) {

        boolean chonDanhMuc = loai == null && (tuKhoa == null || tuKhoa.isBlank());
        model.addAttribute("chonDanhMuc", chonDanhMuc);
        model.addAttribute("danhSachLoai", loaiSanPhamService.layTatCa());

        if (chonDanhMuc) {
            model.addAttribute("tieuDe", "Sản phẩm");
            return "san-pham";
        }

        List<SanPham> danhSach;
        LoaiSanPham loaiHienTai = null;
        String tieuDe;

        if (loai != null) {
            loaiHienTai = loaiSanPhamService.layTheoId(loai);
            danhSach = sanPhamRepository.findByLoaiSanPhamIdAndDangBanTrue(loai);
            tieuDe = loaiHienTai.getTenLoai();
        } else {
            danhSach = sanPhamRepository.findByTenSanPhamContainingIgnoreCaseAndDangBanTrue(tuKhoa.trim());
            tieuDe = "Kết quả tìm kiếm";
        }

        if (hang != null && !hang.isEmpty()) {
            List<SanPham> loc = new ArrayList<>();
            for (SanPham sp : danhSach) {
                if (sp.getThuongHieu() != null && hang.contains(sp.getThuongHieu().getId())) {
                    loc.add(sp);
                }
            }
            danhSach = loc;
        }

        danhSach = new ArrayList<>(danhSach);
        if ("gia-tang".equals(sapXep)) {
            danhSach.sort(Comparator.comparing(SanPham::getGiaHienTai));
        } else if ("gia-giam".equals(sapXep)) {
            danhSach.sort(Comparator.comparing(SanPham::getGiaHienTai).reversed());
        } else if ("ten".equals(sapXep)) {
            danhSach.sort(Comparator.comparing(SanPham::getTenSanPham));
        }

        List<ThuongHieu> danhSachHang = thuongHieuRepository.findAllByOrderByTenThuongHieuAsc();

        model.addAttribute("danhSachSanPham", danhSach);
        model.addAttribute("danhSachThuongHieu", danhSachHang);
        model.addAttribute("loaiHienTai", loaiHienTai);
        model.addAttribute("tieuDe", tieuDe);
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("hangDangChon", hang == null ? new ArrayList<Long>() : hang);
        model.addAttribute("sapXepDangChon", sapXep == null ? "moi-nhat" : sapXep);
        return "san-pham";
    }
}
