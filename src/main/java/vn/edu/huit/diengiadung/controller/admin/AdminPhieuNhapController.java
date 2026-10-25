package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.NhaCungCap;
import vn.edu.huit.diengiadung.entity.PhieuNhap;
import vn.edu.huit.diengiadung.repository.PhieuNhapRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.NhaCungCapService;
import vn.edu.huit.diengiadung.service.PhieuNhapService;

import java.util.List;

@Controller
@RequestMapping("/admin/phieu-nhap")
public class AdminPhieuNhapController {

    private final NhaCungCapService nhaCungCapService;
    private final PhieuNhapRepository phieuNhapRepository;
    private final SanPhamRepository sanPhamRepository;
    private final PhieuNhapService phieuNhapService;

    @Autowired
    public AdminPhieuNhapController(NhaCungCapService nhaCungCapService,
                                    PhieuNhapRepository phieuNhapRepository,
                                    SanPhamRepository sanPhamRepository,
                                    PhieuNhapService phieuNhapService) {
        this.nhaCungCapService = nhaCungCapService;
        this.phieuNhapRepository = phieuNhapRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.phieuNhapService = phieuNhapService;
    }

    @GetMapping
    public String danhSach(Model model) {

        List<PhieuNhap> danhSach = phieuNhapRepository.findAllByOrderByNgayNhapDesc();
        model.addAttribute("danhSach", danhSach);
        return "admin/phieu-nhap/danh-sach";
    }

    @GetMapping("/xem/{id}")
    public String xemChiTiet(@PathVariable Long id, Model model, RedirectAttributes thongBao) {

        PhieuNhap phieuNhap = phieuNhapRepository.findById(id).orElse(null);
        if (phieuNhap == null) {
            thongBao.addFlashAttribute("thongBaoLoi", "Không tìm thấy phiếu nhập này");
            return "redirect:/admin/phieu-nhap";
        }
        model.addAttribute("phieuNhap", phieuNhap);
        return "admin/phieu-nhap/xem";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        List<NhaCungCap> danhSachNCC = nhaCungCapService.layDanhSach();

        model.addAttribute("phieuNhap", new PhieuNhap());
        model.addAttribute("danhSachNCC", danhSachNCC);


        model.addAttribute("danhSachSP", sanPhamRepository.findAll());

        return "admin/phieu-nhap/form";
    }

    @PostMapping("/luu")
    public String luuPhieuNhap(@ModelAttribute("phieuNhap") PhieuNhap phieuNhap, RedirectAttributes redirectAttributes) {
        try {

            phieuNhapService.luuQuyTrinhNhapHang(phieuNhap);
            redirectAttributes.addFlashAttribute("thongBao", "Lập phiếu nhập thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", e.getMessage());
            return "redirect:/admin/phieu-nhap/them";
        }
        return "redirect:/admin/phieu-nhap";
    }
}