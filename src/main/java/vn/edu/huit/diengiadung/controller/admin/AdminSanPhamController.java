package vn.edu.huit.diengiadung.controller.admin;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.SanPhamService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/admin/san-pham")
public class AdminSanPhamController {

    private final SanPhamService dichVuSanPham;
    private final LoaiSanPhamService dichVuLoaiSanPham;
    private final ThuongHieuRepository duLieuThuongHieu;

    public AdminSanPhamController(SanPhamService dichVuSanPham,
                                  LoaiSanPhamService dichVuLoaiSanPham,
                                  ThuongHieuRepository duLieuThuongHieu) {
        this.dichVuSanPham = dichVuSanPham;
        this.dichVuLoaiSanPham = dichVuLoaiSanPham;
        this.duLieuThuongHieu = duLieuThuongHieu;
    }

    @GetMapping
    public String danhSach(@RequestParam(required = false) String tuKhoa,
                           @RequestParam(required = false) Long loai,
                           @RequestParam(required = false) Long hang,
                           @RequestParam(required = false) String trangThai,
                           @RequestParam(defaultValue = "1") int trang,
                           Model moHinh) {
        moHinh.addAttribute("ketQua", dichVuSanPham.timKiemQuanTri(tuKhoa, loai, hang, trangThai, trang));
        moHinh.addAttribute("tuKhoa", tuKhoa);
        moHinh.addAttribute("loaiDangChon", loai);
        moHinh.addAttribute("hangDangChon", hang);
        moHinh.addAttribute("trangThaiDangChon", trangThai);
        moHinh.addAttribute("thamSoLoc", taoThamSoLoc(tuKhoa, loai, hang, trangThai));
        themDanhSachDeChon(moHinh);
        return "admin/san-pham/danh-sach";
    }

    @GetMapping("/them")
    public String formThem(Model moHinh) {
        SanPham sanPham = new SanPham();
        sanPham.setBaoHanhThang(24);
        moHinh.addAttribute("sanPham", sanPham);
        themDanhSachDeChon(moHinh);
        return "admin/san-pham/form";
    }

    @GetMapping("/sua/{id}")
    public String formSua(@PathVariable Long id, Model moHinh, RedirectAttributes thongBao) {
        try {
            moHinh.addAttribute("sanPham", dichVuSanPham.layTheoId(id));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/admin/san-pham";
        }
        themDanhSachDeChon(moHinh);
        return "admin/san-pham/form";
    }

    @PostMapping("/luu")
    public String luu(@Valid @ModelAttribute("sanPham") SanPham sanPham,
                      BindingResult ketQua,
                      @RequestParam(value = "tepAnh", required = false) MultipartFile tepAnh,
                      @RequestParam(value = "xoaAnh", defaultValue = "false") boolean xoaAnh,
                      RedirectAttributes thongBao,
                      Model moHinh) {
        if (ketQua.hasErrors()) {
            themDanhSachDeChon(moHinh);
            return "admin/san-pham/form";
        }
        boolean themMoi = sanPham.getId() == null;
        SanPham daLuu;
        try {
            daLuu = dichVuSanPham.luu(sanPham, tepAnh, xoaAnh);
        } catch (LoiNghiepVu loi) {
            moHinh.addAttribute("thongBaoLoi", loi.getMessage());
            themDanhSachDeChon(moHinh);
            return "admin/san-pham/form";
        }
        thongBao.addFlashAttribute("thongBao", (themMoi ? "Đã thêm sản phẩm " : "Đã cập nhật sản phẩm ")
                + daLuu.getMaSanPham());
        return "redirect:/admin/san-pham";
    }

    @PostMapping("/xoa/{id}")
    public String xoa(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            dichVuSanPham.xoa(id);
            thongBao.addFlashAttribute("thongBao", "Đã xoá sản phẩm");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/san-pham";
    }

    @PostMapping("/doi-trang-thai/{id}")
    public String doiTrangThai(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            dichVuSanPham.doiTrangThaiBan(id);
            thongBao.addFlashAttribute("thongBao", "Đã đổi trạng thái bán của sản phẩm");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/admin/san-pham";
    }

    private void themDanhSachDeChon(Model moHinh) {
        moHinh.addAttribute("danhSachLoai", dichVuLoaiSanPham.layTatCa());
        moHinh.addAttribute("danhSachThuongHieu", duLieuThuongHieu.findAllByOrderByTenThuongHieuAsc());
    }

    private String taoThamSoLoc(String tuKhoa, Long loai, Long hang, String trangThai) {
        StringBuilder chuoi = new StringBuilder();
        if (tuKhoa != null && !tuKhoa.isBlank()) {
            chuoi.append("tuKhoa=").append(URLEncoder.encode(tuKhoa.trim(), StandardCharsets.UTF_8)).append('&');
        }
        if (loai != null) {
            chuoi.append("loai=").append(loai).append('&');
        }
        if (hang != null) {
            chuoi.append("hang=").append(hang).append('&');
        }
        if (trangThai != null && !trangThai.isBlank()) {
            chuoi.append("trangThai=").append(URLEncoder.encode(trangThai, StandardCharsets.UTF_8)).append('&');
        }
        return chuoi.toString();
    }
}
