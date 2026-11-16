package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.model.BangGia;
import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.model.ThongTinGiaoHang;
import vn.edu.huit.diengiadung.service.CauHinhThanhToan;
import vn.edu.huit.diengiadung.service.DatHangService;
import vn.edu.huit.diengiadung.service.DiaChiHanhChinh;
import vn.edu.huit.diengiadung.service.GioHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.UuDaiService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/dat-hang")
public class DatHangController {

    public static final String DON_TRONG_PHIEN = "donTrongPhien";

    private final DatHangService dichVuDatHang;
    private final GioHangService dichVuGioHang;
    private final DiaChiHanhChinh diaChiHanhChinh;
    private final CauHinhThanhToan cauHinhThanhToan;
    private final UuDaiService uuDaiService;

    public DatHangController(DatHangService dichVuDatHang, GioHangService dichVuGioHang,
                             DiaChiHanhChinh diaChiHanhChinh, CauHinhThanhToan cauHinhThanhToan,
                             UuDaiService uuDaiService) {
        this.dichVuDatHang = dichVuDatHang;
        this.dichVuGioHang = dichVuGioHang;
        this.diaChiHanhChinh = diaChiHanhChinh;
        this.cauHinhThanhToan = cauHinhThanhToan;
        this.uuDaiService = uuDaiService;
    }

    private void nhoiDuLieuTrang(Model moHinh, ChiTietGioHang chiTiet, ThongTinGiaoHang thongTin, HttpSession phien) {
        TaiKhoan taiKhoan = layTaiKhoan(phien);
        boolean thanhVien = dichVuDatHang.timKhachHang(taiKhoan) != null;
        BangGia bangGia = dichVuDatHang.tinhGia(chiTiet, taiKhoan, thanhVien ? thongTin.getMaGiamGia() : null);
        long sauUuDai = bangGia.getSauUuDai();
        moHinh.addAttribute("chiTiet", chiTiet);
        moHinh.addAttribute("bangGia", bangGia);
        moHinh.addAttribute("thanhVien", thanhVien);
        moHinh.addAttribute("daDangNhap", taiKhoan != null);
        moHinh.addAttribute("danhSachMa", thanhVien ? uuDaiService.maDangMoChoKhach() : List.of());
        moHinh.addAttribute("khuyenMaiThanhVien", thanhVien ? List.of() : uuDaiService.khuyenMaiDangChay(true));
        moHinh.addAttribute("danhSachTinh", diaChiHanhChinh.danhSachTinhThanh());
        moHinh.addAttribute("danhSachPhuong", diaChiHanhChinh.danhSachPhuongXa(thongTin.getTinhThanh()));
        moHinh.addAttribute("phanTramGiam", cauHinhThanhToan.getPhanTramGiamThanhToanTruoc());
        moHinh.addAttribute("tienGiamNeuCk", cauHinhThanhToan.tinhTienGiam(sauUuDai));
        moHinh.addAttribute("codDuocPhep", cauHinhThanhToan.choPhepCod(sauUuDai));
        moHinh.addAttribute("codToiDa", cauHinhThanhToan.getCodToiDa());
    }

    @GetMapping
    public String formDatHang(HttpSession phien, Model moHinh, RedirectAttributes thongBao) {
        ChiTietGioHang chiTiet = dichVuGioHang.xemChiTiet(GioHangController.layGioHang(phien));
        if (chiTiet.isTrong()) {
            thongBao.addFlashAttribute("thongBaoLoi", "Giỏ hàng đang trống nên chưa thể đặt hàng");
            return "redirect:/gio-hang";
        }
        ThongTinGiaoHang thongTin = dichVuDatHang.layThongTinGiaoHangMacDinh(layTaiKhoan(phien));
        moHinh.addAttribute("thongTin", thongTin);
        nhoiDuLieuTrang(moHinh, chiTiet, thongTin, phien);
        return "dat-hang";
    }

    @GetMapping("/ma-giam-gia")
    @ResponseBody
    public Map<String, Object> kiemTraMa(@RequestParam(required = false) String ma, HttpSession phien) {
        ChiTietGioHang chiTiet = dichVuGioHang.xemChiTiet(GioHangController.layGioHang(phien));
        TaiKhoan taiKhoan = layTaiKhoan(phien);
        String maChuan = ma == null || ma.isBlank() ? null : ma.trim().toUpperCase();
        BangGia bangGia = dichVuDatHang.tinhGia(chiTiet, taiKhoan, maChuan);
        long sauUuDai = bangGia.getSauUuDai();
        Map<String, Object> ketQua = new LinkedHashMap<>();
        ketQua.put("ok", maChuan != null && bangGia.getLoiMa() == null);
        ketQua.put("loi", bangGia.getLoiMa());
        ketQua.put("ma", bangGia.getMaGiamGia() == null ? null : bangGia.getMaGiamGia().getMa());
        ketQua.put("moTaMa", bangGia.getMaGiamGia() == null ? null : bangGia.getMaGiamGia().getMoTaMucGiam());
        ketQua.put("tienGiamMa", bangGia.getTienGiamMa());
        ketQua.put("tienKhuyenMai", bangGia.getTienKhuyenMai());
        ketQua.put("sauUuDai", sauUuDai);
        ketQua.put("giamCk", cauHinhThanhToan.tinhTienGiam(sauUuDai));
        ketQua.put("codDuocPhep", cauHinhThanhToan.choPhepCod(sauUuDai));
        return ketQua;
    }

    @PostMapping("/ap-ma")
    public String apMa(@ModelAttribute("thongTin") ThongTinGiaoHang thongTin, HttpSession phien, Model moHinh) {
        ChiTietGioHang chiTiet = dichVuGioHang.xemChiTiet(GioHangController.layGioHang(phien));
        if (chiTiet.isTrong()) {
            return "redirect:/gio-hang";
        }
        nhoiDuLieuTrang(moHinh, chiTiet, thongTin, phien);
        return "dat-hang";
    }

    @PostMapping
    public String datHang(@Valid @ModelAttribute("thongTin") ThongTinGiaoHang thongTin,
                          BindingResult ketQuaKiemTra,
                          HttpSession phien,
                          Model moHinh,
                          RedirectAttributes thongBao) {
        GioHang gioHang = GioHangController.layGioHang(phien);
        ChiTietGioHang chiTiet = dichVuGioHang.xemChiTiet(gioHang);
        if (chiTiet.isTrong()) {
            thongBao.addFlashAttribute("thongBaoLoi", "Giỏ hàng đang trống nên chưa thể đặt hàng");
            return "redirect:/gio-hang";
        }
        TaiKhoan taiKhoan = layTaiKhoan(phien);
        if (thongTin.getTinhThanh() != null && !thongTin.getTinhThanh().isBlank()
                && !diaChiHanhChinh.coTinhThanh(thongTin.getTinhThanh())) {
            ketQuaKiemTra.rejectValue("tinhThanh", "khongHopLe", "Tỉnh / thành phố không hợp lệ");
        } else if (thongTin.getPhuongXa() != null && !thongTin.getPhuongXa().isBlank()
                && !diaChiHanhChinh.hopLe(thongTin.getTinhThanh(), thongTin.getPhuongXa())) {
            ketQuaKiemTra.rejectValue("phuongXa", "khongHopLe", "Phường / xã không thuộc tỉnh / thành phố đã chọn");
        }
        BangGia bangGia = dichVuDatHang.tinhGia(chiTiet, taiKhoan, thongTin.getMaGiamGia());
        if (thongTin.getMaGiamGia() != null && bangGia.getLoiMa() != null) {
            ketQuaKiemTra.rejectValue("maGiamGia", "khongHopLe", bangGia.getLoiMa());
        }
        if (DonHang.THANH_TOAN_KHI_NHAN.equals(thongTin.getPhuongThuc())
                && !cauHinhThanhToan.choPhepCod(bangGia.getSauUuDai())) {
            ketQuaKiemTra.rejectValue("phuongThuc", "codVuotMuc", "Đơn hàng này không áp dụng thanh toán khi nhận hàng");
        }
        if (ketQuaKiemTra.hasErrors()) {
            String loiChung = "Vui lòng kiểm tra lại các ô được đánh dấu.";
            if (ketQuaKiemTra.hasFieldErrors("maGiamGia")) {
                loiChung = ketQuaKiemTra.getFieldError("maGiamGia").getDefaultMessage() + ". " + loiChung;
                thongTin.setMaGiamGia(null);
            }
            nhoiDuLieuTrang(moHinh, chiTiet, thongTin, phien);
            moHinh.addAttribute("loiChung", loiChung);
            return "dat-hang";
        }
        try {
            DonHang donHang = dichVuDatHang.datHang(taiKhoan, gioHang, thongTin);
            gioHang.xoaHet();
            ghiNhoDonTrongPhien(phien, donHang.getId());
            thongBao.addFlashAttribute("vuaDatHang", true);
            return "redirect:/don-hang/" + donHang.getId();
        } catch (LoiNghiepVu loi) {
            nhoiDuLieuTrang(moHinh, dichVuGioHang.xemChiTiet(gioHang), thongTin, phien);
            moHinh.addAttribute("loiChung", loi.getMessage());
            return "dat-hang";
        }
    }

    @SuppressWarnings("unchecked")
    public static void ghiNhoDonTrongPhien(HttpSession phien, Long idDonHang) {
        Object daCo = phien.getAttribute(DON_TRONG_PHIEN);
        Set<Long> danhSach = daCo instanceof Set<?> ? (Set<Long>) daCo : new LinkedHashSet<>();
        danhSach.add(idDonHang);
        phien.setAttribute(DON_TRONG_PHIEN, danhSach);
    }

    public static boolean coDonTrongPhien(HttpSession phien, Long idDonHang) {
        return phien.getAttribute(DON_TRONG_PHIEN) instanceof Set<?> danhSach && danhSach.contains(idDonHang);
    }

    private TaiKhoan layTaiKhoan(HttpSession phien) {
        return (TaiKhoan) phien.getAttribute("taiKhoan");
    }
}
