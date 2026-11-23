package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.huit.diengiadung.config.BatTayChat;
import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.mang.DuLieuChat;
import vn.edu.huit.diengiadung.service.ChatService;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/phien")
    public ResponseEntity<Map<String, Object>> phien(@RequestParam(defaultValue = "false") boolean tao,
                                                     HttpSession phienWeb,
                                                     HttpServletRequest yeuCau,
                                                     HttpServletResponse phanHoi) {
        TaiKhoan taiKhoan = (TaiKhoan) phienWeb.getAttribute("taiKhoan");
        if (taiKhoan != null && taiKhoan.laQuanTri()) {
            return ResponseEntity.status(403).body(Map.of("thongBao", "Tài khoản quản trị dùng trang Tin nhắn tư vấn"));
        }
        String maKhach = BatTayChat.docCookie(yeuCau);
        Optional<PhienChat> daCo = chatService.timPhienKhach(taiKhoan, maKhach);
        if (daCo.isEmpty() && !tao) {
            return ResponseEntity.ok(Map.of("coPhien", false));
        }
        if (daCo.isEmpty() && taiKhoan == null && maKhach == null) {
            maKhach = UUID.randomUUID().toString();
            ResponseCookie cookie = ResponseCookie.from(BatTayChat.COOKIE_KHACH, maKhach)
                    .httpOnly(true).sameSite("Lax").path("/").maxAge(Duration.ofDays(30)).build();
            phanHoi.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }
        PhienChat phien = daCo.isPresent() ? daCo.get() : chatService.layHoacTaoPhienKhach(taiKhoan, maKhach);
        Map<String, Object> ketQua = DuLieuChat.phienChoKhach(phien);
        ketQua.put("coPhien", true);
        ketQua.put("tinNhan", DuLieuChat.danhSachTin(chatService.lichSu(phien.getId())));
        return ResponseEntity.ok(ketQua);
    }
}
