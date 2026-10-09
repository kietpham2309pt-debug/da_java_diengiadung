package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.BoLocSanPham;
import vn.edu.huit.diengiadung.service.KieuSapXep;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.MucGia;
import vn.edu.huit.diengiadung.service.SanPhamService;

@Controller
public class SanPhamController {

    private static final int SO_SAN_PHAM_CUNG_LOAI = 6;

    private final SanPhamService sanPhamService;
    private final LoaiSanPhamService loaiSanPhamService;
    private final ThuongHieuRepository thuongHieuRepository;

    public SanPhamController(SanPhamService sanPhamService,
                             LoaiSanPhamService loaiSanPhamService,
                             ThuongHieuRepository thuongHieuRepository) {
        this.sanPhamService = sanPhamService;
        this.loaiSanPhamService = loaiSanPhamService;
        this.thuongHieuRepository = thuongHieuRepository;
    }

    @GetMapping("/san-pham")
    public String danhSach(@ModelAttribute("boLoc") BoLocSanPham boLoc,
                           HttpServletRequest yeuCau,
                           Model moHinh) {
        moHinh.addAttribute("danhSachLoai", loaiSanPhamService.layTatCa());

        String chuoiThamSo = yeuCau.getQueryString();
        boolean chonDanhMuc = chuoiThamSo == null || chuoiThamSo.isBlank();
        moHinh.addAttribute("chonDanhMuc", chonDanhMuc);
        if (chonDanhMuc) {
            moHinh.addAttribute("tieuDe", "Sản phẩm");
            return "san-pham";
        }

        LoaiSanPham loaiHienTai = null;
        if (boLoc.getLoai() != null) {
            loaiHienTai = timLoai(boLoc.getLoai());
        }

        moHinh.addAttribute("ketQua", sanPhamService.timKiemPhiaKhach(boLoc));
        moHinh.addAttribute("loaiHienTai", loaiHienTai);
        moHinh.addAttribute("tieuDe", taoTieuDe(boLoc, loaiHienTai));
        moHinh.addAttribute("danhSachThuongHieu", thuongHieuRepository.findAllByOrderByTenThuongHieuAsc());
        moHinh.addAttribute("danhSachMucGia", MucGia.values());
        moHinh.addAttribute("danhSachKieuSapXep", KieuSapXep.values());
        moHinh.addAttribute("tuKhoa", boLoc.getTuKhoaDaGon());
        return "san-pham";
    }

    /** Trả về riêng khối kết quả để site.js thay tại chỗ khi khách tick bộ lọc, đổi sắp xếp hoặc sang trang. */
    @GetMapping("/san-pham/ket-qua")
    public String ketQuaLoc(@ModelAttribute("boLoc") BoLocSanPham boLoc, Model moHinh) {
        moHinh.addAttribute("ketQua", sanPhamService.timKiemPhiaKhach(boLoc));
        return "san-pham :: ketQua";
    }

    @GetMapping("/san-pham/{id}")
    public String chiTiet(@PathVariable Long id, Model moHinh) {
        SanPham sanPham;
        try {
            sanPham = sanPhamService.laySanPhamDangBan(id);
        } catch (LoiNghiepVu loi) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, loi.getMessage());
        }
        moHinh.addAttribute("sanPham", sanPham);
        moHinh.addAttribute("danhSachCungLoai", sanPhamService.laySanPhamCungLoai(sanPham, SO_SAN_PHAM_CUNG_LOAI));
        return "chi-tiet-san-pham";
    }

    private LoaiSanPham timLoai(Long idLoai) {
        try {
            return loaiSanPhamService.layTheoId(idLoai);
        } catch (LoiNghiepVu loi) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, loi.getMessage());
        }
    }

    private String taoTieuDe(BoLocSanPham boLoc, LoaiSanPham loaiHienTai) {
        if (loaiHienTai != null) {
            return loaiHienTai.getTenLoai();
        }
        if (boLoc.getTuKhoaDaGon() != null) {
            return "Kết quả tìm kiếm";
        }
        if (boLoc.getHang().size() == 1) {
            ThuongHieu thuongHieu = thuongHieuRepository.findById(boLoc.getHang().get(0)).orElse(null);
            if (thuongHieu != null) {
                return "Sản phẩm " + thuongHieu.getTenThuongHieu();
            }
        }
        return "Tất cả sản phẩm";
    }
}
