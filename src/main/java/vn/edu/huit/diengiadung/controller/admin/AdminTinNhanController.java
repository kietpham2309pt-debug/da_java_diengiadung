package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.mang.DuLieuChat;
import vn.edu.huit.diengiadung.mang.KenhChat;
import vn.edu.huit.diengiadung.service.ChatService;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/khach-hang/tin-nhan")
public class AdminTinNhanController {

    private final ChatService chatService;
    private final KenhChat kenhChat;

    public AdminTinNhanController(ChatService chatService, KenhChat kenhChat) {
        this.chatService = chatService;
        this.kenhChat = kenhChat;
    }

    @GetMapping
    public String trang(@RequestParam(required = false) Long phien,
                        @RequestParam(required = false) Long khachHang,
                        Model model) {
        Long dangMo = phien;
        if (dangMo == null && khachHang != null) {
            dangMo = chatService.layHoacTaoPhienChoKhachHang(khachHang).getId();
        }
        model.addAttribute("phienDangMo", dangMo);
        model.addAttribute("danhSachPhien", danhSach());
        return "admin/khach-hang/tin-nhan";
    }

    @GetMapping("/danh-sach")
    @ResponseBody
    public List<Map<String, Object>> danhSach() {
        return chatService.danhSachQuanTri().stream()
                .map(p -> DuLieuChat.phienChoQuanTri(p, chatService.tinCuoi(p.getId()).orElse(null),
                        kenhChat.khachDangTrucTuyen(p.getId())))
                .toList();
    }

    @GetMapping("/{id}/lich-su")
    @ResponseBody
    public Map<String, Object> lichSu(@PathVariable Long id) {
        PhienChat phien = chatService.layTheoId(id);
        return Map.of(
                "phien", DuLieuChat.phienChoQuanTri(phien, chatService.tinCuoi(id).orElse(null), kenhChat.khachDangTrucTuyen(id)),
                "tinNhan", DuLieuChat.danhSachTin(chatService.lichSu(id)));
    }
}
