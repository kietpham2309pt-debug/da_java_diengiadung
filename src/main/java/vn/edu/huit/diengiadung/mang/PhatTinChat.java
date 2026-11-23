package vn.edu.huit.diengiadung.mang;

import org.springframework.stereotype.Component;
import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.entity.TinNhanChat;
import vn.edu.huit.diengiadung.service.ChatService;

@Component
public class PhatTinChat {

    private final KenhChat kenhChat;
    private final ChatService chatService;

    public PhatTinChat(KenhChat kenhChat, ChatService chatService) {
        this.kenhChat = kenhChat;
        this.chatService = chatService;
    }

    public void phat(ChatService.KetQuaChat ketQua) {
        PhienChat phien = ketQua.phien();
        for (TinNhanChat tin : ketQua.tinMoi()) {
            kenhChat.guiKhach(phien.getId(), DuLieuChat.suKien("TIN", "tin", DuLieuChat.tin(tin)));
            kenhChat.guiNhanVien(DuLieuChat.suKien("TIN", "phienId", phien.getId(), "tin", DuLieuChat.tin(tin)));
        }
        capNhatTrangThai(phien);
    }

    public void capNhatTrangThai(PhienChat phien) {
        kenhChat.guiKhach(phien.getId(), DuLieuChat.suKien("PHIEN", "phien", DuLieuChat.phienChoKhach(phien)));
        capNhatChoNhanVien(phien);
    }

    public void capNhatChoNhanVien(PhienChat phien) {
        TinNhanChat cuoi = chatService.tinCuoi(phien.getId()).orElse(null);
        kenhChat.guiNhanVien(DuLieuChat.suKien("PHIEN",
                "phien", DuLieuChat.phienChoQuanTri(phien, cuoi, kenhChat.khachDangTrucTuyen(phien.getId()))));
        kenhChat.guiNhanVien(DuLieuChat.suKien("TONG", "soChuaDoc", chatService.tongChuaDocQuanTri()));
    }
}
