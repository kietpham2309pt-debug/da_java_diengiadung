package vn.edu.huit.diengiadung.mang;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.ConcurrentWebSocketSessionDecorator;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class KenhChat {

    private static final Logger log = LoggerFactory.getLogger(KenhChat.class);

    private final ObjectMapper chuyenJson;
    private final Map<Long, Set<WebSocketSession>> khachTheoPhien = new ConcurrentHashMap<>();
    private final Set<WebSocketSession> nhanVien = ConcurrentHashMap.newKeySet();
    private final Map<String, WebSocketSession> boc = new ConcurrentHashMap<>();

    public KenhChat(ObjectMapper chuyenJson) {
        this.chuyenJson = chuyenJson;
    }

    public void themKhach(Long phienId, WebSocketSession phien) {
        khachTheoPhien.computeIfAbsent(phienId, k -> ConcurrentHashMap.newKeySet()).add(bocAnToan(phien));
        log.info("Khách của cuộc trò chuyện {} kết nối, đang có {} cửa sổ", phienId, khachTheoPhien.get(phienId).size());
    }

    public void boKhach(Long phienId, WebSocketSession phien) {
        Set<WebSocketSession> ds = khachTheoPhien.get(phienId);
        if (ds != null) {
            ds.remove(boc.remove(phien.getId()));
            if (ds.isEmpty()) {
                khachTheoPhien.remove(phienId, ds);
            }
        }
    }

    public void themNhanVien(WebSocketSession phien) {
        nhanVien.add(bocAnToan(phien));
        log.info("Nhân viên kết nối kênh tư vấn, đang trực tuyến {}", nhanVien.size());
    }

    public void boNhanVien(WebSocketSession phien) {
        nhanVien.remove(boc.remove(phien.getId()));
    }

    public boolean coNhanVienTrucTuyen() {
        return !nhanVien.isEmpty();
    }

    public boolean khachDangTrucTuyen(Long phienId) {
        Set<WebSocketSession> ds = khachTheoPhien.get(phienId);
        return ds != null && !ds.isEmpty();
    }

    public Set<Long> phienDangTrucTuyen() {
        return khachTheoPhien.keySet();
    }

    public void guiKhach(Long phienId, Object duLieu) {
        Set<WebSocketSession> ds = khachTheoPhien.get(phienId);
        if (ds != null) {
            gui(ds, duLieu);
        }
    }

    public void guiNhanVien(Object duLieu) {
        gui(nhanVien, duLieu);
    }

    public void guiRieng(WebSocketSession phien, Object duLieu) {
        WebSocketSession daBoc = boc.getOrDefault(phien.getId(), phien);
        gui(Set.of(daBoc), duLieu);
    }

    private void gui(Set<WebSocketSession> ds, Object duLieu) {
        TextMessage tin;
        try {
            tin = new TextMessage(chuyenJson.writeValueAsString(duLieu));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
        for (WebSocketSession phien : ds) {
            if (!phien.isOpen()) {
                continue;
            }
            try {
                phien.sendMessage(tin);
            } catch (IOException | IllegalStateException e) {
                log.warn("Không gửi được tới kết nối {}: {}", phien.getId(), e.getMessage());
            }
        }
    }

    private WebSocketSession bocAnToan(WebSocketSession phien) {
        return boc.computeIfAbsent(phien.getId(), k -> new ConcurrentWebSocketSessionDecorator(phien, 5000, 128 * 1024));
    }
}
