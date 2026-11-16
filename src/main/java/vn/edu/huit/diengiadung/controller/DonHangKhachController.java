package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.CauHinhThanhToan;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.Map;

@Controller
@RequestMapping("/don-hang")
public class DonHangKhachController {

    private final DatHangService dichVuDatHang;
    private final CauHinhThanhToan cauHinhThanhToan;

    public DonHangKhachController(DatHangService dichVuDatHang, CauHinhThanhToan cauHinhThanhToan) {
        this.dichVuDatHang = dichVuDatHang;
        this.cauHinhThanhToan = cauHinhThanhToan;
    }

    @GetMapping
    public String lichSuDon(HttpSession phien, Model moHinh, RedirectAttributes thongBao) {
        try {
            moHinh.addAttribute("danhSachDon", dichVuDatHang.layDonCuaKhach(layTaiKhoan(phien)));
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
            return "redirect:/";
        }
        return "don-hang/danh-sach";
    }

    @GetMapping("/tra-cuu")
    public String formTraCuu() {
        return "don-hang/tra-cuu";
    }

    @PostMapping("/tra-cuu")
    public String traCuu(@RequestParam(required = false) String maDon,
                         @RequestParam(required = false) String soDienThoai,
                         HttpSession phien, Model moHinh) {
        try {
            DonHang donHang = dichVuDatHang.traCuuDon(maDon, soDienThoai);
            DatHangController.ghiNhoDonTrongPhien(phien, donHang.getId());
            return "redirect:/don-hang/" + donHang.getId();
        } catch (LoiNghiepVu loi) {
            moHinh.addAttribute("thongBaoLoi", loi.getMessage());
            moHinh.addAttribute("maDon", maDon);
            moHinh.addAttribute("soDienThoai", soDienThoai);
            return "don-hang/tra-cuu";
        }
    }

    private DonHang layDonDuocXem(HttpSession phien, Long id) {
        TaiKhoan taiKhoan = layTaiKhoan(phien);
        if (DatHangController.coDonTrongPhien(phien, id)) {
            return dichVuDatHang.layDonTheoId(id);
        }
        if (taiKhoan == null) {
            throw new LoiNghiepVu("Không tìm thấy đơn hàng");
        }
        return dichVuDatHang.layDonCuaKhach(taiKhoan, id);
    }

    @GetMapping("/{id}")
    public String chiTietDon(@PathVariable Long id, HttpSession phien, Model moHinh) {
        if (layTaiKhoan(phien) == null && !DatHangController.coDonTrongPhien(phien, id)) {
            return "redirect:/don-hang/tra-cuu";
        }
        try {
            DonHang donHang = layDonDuocXem(phien, id);
            moHinh.addAttribute("donHang", donHang);
            moHinh.addAttribute("laChuDon", layTaiKhoan(phien) != null && !donHang.isKhachVangLai());
            moHinh.addAttribute("nganHang", cauHinhThanhToan);
            moHinh.addAttribute("linkMaQr", cauHinhThanhToan.linkMaQr(donHang.getTongTien(), donHang.getMaDon()));
        } catch (LoiNghiepVu loi) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, loi.getMessage());
        }
        return "don-hang/chi-tiet";
    }

    @GetMapping("/{id}/thanh-toan")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> trangThaiThanhToan(@PathVariable Long id, HttpSession phien) {
        try {
            DonHang donHang = layDonDuocXem(phien, id);
            return ResponseEntity.ok(Map.of("daThanhToan", donHang.isDaThanhToan(), "trangThai", donHang.getTrangThai()));
        } catch (LoiNghiepVu loi) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("loi", loi.getMessage()));
        }
    }

    @PostMapping("/{id}/huy")
    public String huyDon(@PathVariable Long id, HttpSession phien, RedirectAttributes thongBao) {
        try {
            dichVuDatHang.huyDonCuaKhach(layTaiKhoan(phien), id);
            thongBao.addFlashAttribute("thongBao", "Đã huỷ đơn hàng #" + id);
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return "redirect:/don-hang/" + id;
    }

    private TaiKhoan layTaiKhoan(HttpSession phien) {
        return (TaiKhoan) phien.getAttribute("taiKhoan");
    }
}
