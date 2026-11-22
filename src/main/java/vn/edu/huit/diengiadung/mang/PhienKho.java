package vn.edu.huit.diengiadung.mang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class PhienKho implements Runnable {

    private static final Logger NHAT_KY = LoggerFactory.getLogger(PhienKho.class);

    private final Socket ketNoi;
    private final XuLyLenhKho xuLy;
    private final int thoiGianCho;
    private final Runnable khiKetThuc;
    private volatile boolean dangChay = true;
    private String nguoiDung;

    public PhienKho(Socket ketNoi, XuLyLenhKho xuLy, int thoiGianCho, Runnable khiKetThuc) {
        this.ketNoi = ketNoi;
        this.xuLy = xuLy;
        this.thoiGianCho = thoiGianCho;
        this.khiKetThuc = khiKetThuc;
    }

    @Override
    public void run() {
        String diaChi = ketNoi.getRemoteSocketAddress().toString();
        String luong = Thread.currentThread().getName();
        NHAT_KY.info("[{}] Client {} đã kết nối", luong, diaChi);
        try (Socket s = ketNoi;
             BufferedReader doc = new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter ghi = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {
            s.setSoTimeout(thoiGianCho);
            String dong;
            while (dangChay && (dong = doc.readLine()) != null) {
                List<String> traLoi = xuLy.xuLy(this, dong);
                NHAT_KY.info("[{}] {} -> {}", luong, anMatKhau(dong), traLoi.get(0));
                for (String d : traLoi) {
                    ghi.write(d);
                    ghi.newLine();
                }
                ghi.flush();
            }
        } catch (SocketTimeoutException e) {
            NHAT_KY.warn("[{}] Client {} không gửi lệnh quá {} giây, đóng kết nối", luong, diaChi, thoiGianCho / 1000);
        } catch (IOException e) {
            NHAT_KY.warn("[{}] Mất kết nối với client {}: {}", luong, diaChi, e.getMessage());
        } catch (RuntimeException e) {
            NHAT_KY.error("[{}] Lỗi khi phục vụ client {}", luong, diaChi, e);
        } finally {
            khiKetThuc.run();
            NHAT_KY.info("[{}] Client {} đã ngắt kết nối", luong, diaChi);
        }
    }

    private static String anMatKhau(String dong) {
        if (dong.toUpperCase().startsWith("DANGNHAP|")) {
            int cuoi = dong.lastIndexOf('|');
            return cuoi > 9 ? dong.substring(0, cuoi) + "|***" : dong;
        }
        return dong;
    }

    public void ketThuc() {
        dangChay = false;
    }

    public String getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(String nguoiDung) {
        this.nguoiDung = nguoiDung;
    }
}
