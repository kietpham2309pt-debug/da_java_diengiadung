package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;

@Controller
@RequestMapping("/admin/canh-bao")
public class AdminCanhBaoController {

    private final SanPhamRepository sanPhamRepository;

    public AdminCanhBaoController(SanPhamRepository sanPhamRepository) {
        this.sanPhamRepository = sanPhamRepository;
    }

    @GetMapping
    public String danhSachCanhBao(Model model) {

        int nguongCanhBao = 10;
        model.addAttribute("danhSach", sanPhamRepository.findBySoLuongTonLessThanEqual(nguongCanhBao));
        return "admin/canh-bao/danh-sach";
    }
}