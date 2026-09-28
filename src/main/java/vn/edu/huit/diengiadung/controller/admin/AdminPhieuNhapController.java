package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.huit.diengiadung.entity.NhaCungCap;
import vn.edu.huit.diengiadung.entity.PhieuNhap;
import vn.edu.huit.diengiadung.repository.PhieuNhapRepository;
import vn.edu.huit.diengiadung.service.NhaCungCapService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/phieu-nhap")
public class AdminPhieuNhapController {


    private final NhaCungCapService nhaCungCapService;
    private final PhieuNhapRepository phieuNhapRepository;

    @Autowired
    public AdminPhieuNhapController(NhaCungCapService nhaCungCapService,PhieuNhapRepository phieuNhapRepository) {
        this.nhaCungCapService = nhaCungCapService;
        this.phieuNhapRepository=phieuNhapRepository;
    }

    @GetMapping
    public String danhSach(Model model) {

        List<PhieuNhap> danhSach = phieuNhapRepository.findAllByOrderByNgayNhapDesc();

        model.addAttribute("danhSach", danhSach);
        return "admin/phieu-nhap/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {

        List<NhaCungCap> danhSachNCC = nhaCungCapService.layDanhSach();

        model.addAttribute("phieuNhap", new PhieuNhap());
        model.addAttribute("danhSachNCC", danhSachNCC);
        return "admin/phieu-nhap/form";
    }
}