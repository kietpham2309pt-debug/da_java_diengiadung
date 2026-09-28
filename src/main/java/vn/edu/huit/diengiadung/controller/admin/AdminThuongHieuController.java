package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.service.ThuongHieuService;

import java.util.List;

@Controller
@RequestMapping("/admin/thuong-hieu")
public class AdminThuongHieuController {

    private final ThuongHieuService thuongHieuService;

    @Autowired
    public AdminThuongHieuController(ThuongHieuService thuongHieuService) {
        this.thuongHieuService = thuongHieuService;
    }

    @GetMapping
    public String danhSach(Model model) {

        List<ThuongHieu> danhSach = thuongHieuService.layDanhSach();

        model.addAttribute("danhSach", danhSach);
        model.addAttribute("tuKhoa", "");
        return "admin/thuong-hieu/danh-sach";
    }

    @GetMapping("/them")
    public String hienThiFormThem(Model model) {
        model.addAttribute("thuongHieu", new ThuongHieu());
        return "admin/thuong-hieu/form";
    }
}