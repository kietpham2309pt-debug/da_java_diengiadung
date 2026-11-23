package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.ChatService;

@ControllerAdvice(annotations = org.springframework.stereotype.Controller.class)
public class ThongTinChat {

    private final ChatService chatService;

    public ThongTinChat(ChatService chatService) {
        this.chatService = chatService;
    }

    @ModelAttribute("soTinChoTuVan")
    public long soTinChoTuVan(HttpSession phien) {
        if (phien.getAttribute("taiKhoan") instanceof TaiKhoan taiKhoan && taiKhoan.laQuanTri()) {
            return chatService.tongChuaDocQuanTri();
        }
        return 0;
    }
}
