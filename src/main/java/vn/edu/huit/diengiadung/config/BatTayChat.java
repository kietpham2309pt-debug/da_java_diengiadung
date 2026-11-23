package vn.edu.huit.diengiadung.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.mang.ChatKhachHandler;
import vn.edu.huit.diengiadung.mang.ChatQuanTriHandler;
import vn.edu.huit.diengiadung.service.ChatService;

import java.util.Map;
import java.util.Optional;

public class BatTayChat implements HandshakeInterceptor {

    public static final String COOKIE_KHACH = "bepne_chat";

    private final ChatService chatService;
    private final boolean choQuanTri;

    public BatTayChat(ChatService chatService, boolean choQuanTri) {
        this.chatService = chatService;
        this.choQuanTri = choQuanTri;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest yeuCau, ServerHttpResponse phanHoi,
                                   WebSocketHandler xuLy, Map<String, Object> thuocTinh) {
        if (!(yeuCau instanceof ServletServerHttpRequest servlet)) {
            return false;
        }
        HttpServletRequest http = servlet.getServletRequest();
        HttpSession phienWeb = http.getSession(false);
        TaiKhoan taiKhoan = phienWeb == null ? null : (TaiKhoan) phienWeb.getAttribute("taiKhoan");
        if (choQuanTri) {
            if (taiKhoan == null || !taiKhoan.laQuanTri()) {
                phanHoi.setStatusCode(HttpStatus.FORBIDDEN);
                return false;
            }
            thuocTinh.put(ChatQuanTriHandler.KHOA_NHAN_VIEN, taiKhoan);
            return true;
        }
        if (taiKhoan != null && taiKhoan.laQuanTri()) {
            phanHoi.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
        Optional<PhienChat> phien = chatService.timPhienKhach(taiKhoan, docCookie(http));
        if (phien.isEmpty()) {
            phanHoi.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }
        thuocTinh.put(ChatKhachHandler.KHOA_PHIEN, phien.get().getId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest yeuCau, ServerHttpResponse phanHoi, WebSocketHandler xuLy, Exception loi) {
    }

    public static String docCookie(HttpServletRequest http) {
        if (http.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : http.getCookies()) {
            if (COOKIE_KHACH.equals(cookie.getName()) && cookie.getValue().matches("[a-f0-9-]{36}")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
