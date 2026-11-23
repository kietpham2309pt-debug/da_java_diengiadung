package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.diengiadung.service.ChatService;
import vn.edu.huit.diengiadung.service.KhachHangService;

@Controller
@RequestMapping("/admin/khach-hang")
public class AdminKhachHangController {

    private final KhachHangService khachHangService;
    private final ChatService chatService;

    public AdminKhachHangController(KhachHangService khachHangService, ChatService chatService) {
        this.khachHangService = khachHangService;
        this.chatService = chatService;
    }

    @GetMapping
    public String danhSach(
            @RequestParam(required = false) String tuKhoa,
            Model model) {

        model.addAttribute("danhSach", khachHangService.timKiem(tuKhoa));
        model.addAttribute("tuKhoa", tuKhoa);
        model.addAttribute("phienChat", chatService.phienTheoKhachHang());

        return "admin/khach-hang/danh-sach";
    }
}
