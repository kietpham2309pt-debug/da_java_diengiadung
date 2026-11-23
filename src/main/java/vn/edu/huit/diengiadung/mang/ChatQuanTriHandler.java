package vn.edu.huit.diengiadung.mang;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.service.ChatService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.Map;

@Component
public class ChatQuanTriHandler extends TextWebSocketHandler {

    public static final String KHOA_NHAN_VIEN = "nhanVienChat";

    private final ChatService chatService;
    private final KenhChat kenhChat;
    private final PhatTinChat phatTinChat;
    private final ObjectMapper chuyenJson;

    public ChatQuanTriHandler(ChatService chatService, KenhChat kenhChat, PhatTinChat phatTinChat, ObjectMapper chuyenJson) {
        this.chatService = chatService;
        this.kenhChat = kenhChat;
        this.phatTinChat = phatTinChat;
        this.chuyenJson = chuyenJson;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession phien) {
        kenhChat.themNhanVien(phien);
        kenhChat.guiRieng(phien, DuLieuChat.suKien("TONG", "soChuaDoc", chatService.tongChuaDocQuanTri()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession phien, TextMessage tinNhan) throws Exception {
        TaiKhoan nhanVien = (TaiKhoan) phien.getAttributes().get(KHOA_NHAN_VIEN);
        Map<?, ?> yeuCau = chuyenJson.readValue(tinNhan.getPayload(), Map.class);
        String loai = String.valueOf(yeuCau.get("loai"));
        Long phienId = yeuCau.get("phienId") instanceof Number so ? so.longValue() : null;
        if (phienId == null) {
            kenhChat.guiRieng(phien, DuLieuChat.suKien("LOI", "thongBao", "Thiếu mã cuộc trò chuyện"));
            return;
        }
        try {
            switch (loai) {
                case "MO" -> {
                    phatTinChat.phat(chatService.nhanVienMo(phienId, nhanVien));
                    kenhChat.guiKhach(phienId, DuLieuChat.suKien("DA_XEM"));
                }
                case "GUI" -> phatTinChat.phat(chatService.nhanVienGui(phienId, nhanVien,
                        yeuCau.get("noiDung") == null ? null : yeuCau.get("noiDung").toString()));
                case "KET_THUC" -> phatTinChat.phat(chatService.nhanVienKetThuc(phienId, nhanVien));
                case "DANG_GO" -> kenhChat.guiKhach(phienId, DuLieuChat.suKien("DANG_GO", "ten", nhanVien.getHoTen()));
                default -> kenhChat.guiRieng(phien, DuLieuChat.suKien("LOI", "thongBao", "Lệnh không hợp lệ"));
            }
        } catch (LoiNghiepVu loi) {
            kenhChat.guiRieng(phien, DuLieuChat.suKien("LOI", "thongBao", loi.getMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession phien, CloseStatus trangThai) {
        kenhChat.boNhanVien(phien);
    }
}
