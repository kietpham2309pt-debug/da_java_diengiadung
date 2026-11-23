package vn.edu.huit.diengiadung.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class TinNhanChat {

    public static final String KHACH = "KHACH";
    public static final String BOT = "BOT";
    public static final String NHAN_VIEN = "NHAN_VIEN";
    public static final String HE_THONG = "HE_THONG";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "phien_chat_id")
    private PhienChat phienChat;

    @Column(length = 20, nullable = false)
    private String nguoiGui;

    @Column(length = 80)
    private String tenNguoiGui;

    @Column(length = 2000, nullable = false)
    private String noiDung;

    @Column(length = 500)
    private String goiY;

    private boolean canThongTin;

    private LocalDateTime thoiGian = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PhienChat getPhienChat() {
        return phienChat;
    }

    public void setPhienChat(PhienChat phienChat) {
        this.phienChat = phienChat;
    }

    public String getNguoiGui() {
        return nguoiGui;
    }

    public void setNguoiGui(String nguoiGui) {
        this.nguoiGui = nguoiGui;
    }

    public String getTenNguoiGui() {
        return tenNguoiGui;
    }

    public void setTenNguoiGui(String tenNguoiGui) {
        this.tenNguoiGui = tenNguoiGui;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getGoiY() {
        return goiY;
    }

    public void setGoiY(String goiY) {
        this.goiY = goiY;
    }

    public boolean isCanThongTin() {
        return canThongTin;
    }

    public void setCanThongTin(boolean canThongTin) {
        this.canThongTin = canThongTin;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
}
