package vn.edu.huit.diengiadung.mang;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.service.ChatService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.Map;

@Component
public class ChatKhachHandler extends TextWebSocketHandler {

    public static final String KHOA_PHIEN = "phienChatId";
    private static final long KHOANG_CACH_TOI_THIEU_MS = 300;

    private final ChatService chatService;
    private final KenhChat kenhChat;
    private final PhatTinChat phatTinChat;
    private final ObjectMapper chuyenJson;

    public ChatKhachHandler(ChatService chatService, KenhChat kenhChat, PhatTinChat phatTinChat, ObjectMapper chuyenJson) {
        this.chatService = chatService;
        this.kenhChat = kenhChat;
        this.phatTinChat = phatTinChat;
        this.chuyenJson = chuyenJson;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession phien) {
        Long phienId = (Long) phien.getAttributes().get(KHOA_PHIEN);
        kenhChat.themKhach(phienId, phien);
        phatTinChat.capNhatChoNhanVien(chatService.layTheoId(phienId));
    }

    @Override
    protected void handleTextMessage(WebSocketSession phien, TextMessage tinNhan) throws Exception {
        Long phienId = (Long) phien.getAttributes().get(KHOA_PHIEN);
        Map<?, ?> yeuCau = chuyenJson.readValue(tinNhan.getPayload(), Map.class);
        String loai = String.valueOf(yeuCau.get("loai"));
        try {
            switch (loai) {
                case "GUI" -> {
                    if (quaNhanh(phien)) {
                        return;
                    }
                    phatTinChat.phat(chatService.khachGuiTin(phienId, chuoi(yeuCau, "noiDung"), kenhChat.coNhanVienTrucTuyen()));
                }
                case "CHON" -> phatTinChat.phat(chatService.khachChonGoiY(phienId, chuoi(yeuCau, "ma"), kenhChat.coNhanVienTrucTuyen()));
                case "THONG_TIN" -> phatTinChat.phat(chatService.khachGuiThongTin(phienId,
                        chuoi(yeuCau, "ten"), chuoi(yeuCau, "soDienThoai"), kenhChat.coNhanVienTrucTuyen()));
                case "DA_XEM" -> {
                    PhienChat daXem = chatService.khachDaXem(phienId);
                    kenhChat.guiNhanVien(DuLieuChat.suKien("KHACH_DA_XEM", "phienId", daXem.getId()));
                }
                case "DANG_GO" -> kenhChat.guiNhanVien(DuLieuChat.suKien("DANG_GO", "phienId", phienId));
                default -> kenhChat.guiRieng(phien, DuLieuChat.suKien("LOI", "thongBao", "Lệnh không hợp lệ"));
            }
        } catch (LoiNghiepVu loi) {
            kenhChat.guiRieng(phien, DuLieuChat.suKien("LOI", "thongBao", loi.getMessage()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession phien, CloseStatus trangThai) {
        Long phienId = (Long) phien.getAttributes().get(KHOA_PHIEN);
        kenhChat.boKhach(phienId, phien);
        phatTinChat.capNhatChoNhanVien(chatService.layTheoId(phienId));
    }

    private boolean quaNhanh(WebSocketSession phien) {
        long bayGio = System.currentTimeMillis();
        Object truoc = phien.getAttributes().put("lanGuiCuoi", bayGio);
        return truoc instanceof Long lan && bayGio - lan < KHOANG_CACH_TOI_THIEU_MS;
    }

    private String chuoi(Map<?, ?> yeuCau, String khoa) {
        Object giaTri = yeuCau.get(khoa);
        return giaTri == null ? null : giaTri.toString();
    }
}
