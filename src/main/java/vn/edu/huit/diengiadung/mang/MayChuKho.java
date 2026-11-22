package vn.edu.huit.diengiadung.mang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ConditionalOnProperty(name = "kho.socket.bat", havingValue = "true", matchIfMissing = true)
public class MayChuKho implements SmartLifecycle {

    private static final Logger NHAT_KY = LoggerFactory.getLogger(MayChuKho.class);

    private final XuLyLenhKho xuLy;
    private final int cong;
    private final int soLuongToiDa;
    private final int thoiGianCho;
    private final AtomicInteger dangPhucVu = new AtomicInteger();
    private final AtomicInteger soThuTu = new AtomicInteger();
    private final Set<Socket> dangMo = ConcurrentHashMap.newKeySet();

    private ServerSocket mayChu;
    private ExecutorService nhomLuong;
    private Thread luongLangNghe;
    private volatile boolean dangChay;

    public MayChuKho(XuLyLenhKho xuLy,
                     @Value("${kho.socket.cong:9090}") int cong,
                     @Value("${kho.socket.so-luong-toi-da:10}") int soLuongToiDa,
                     @Value("${kho.socket.thoi-gian-cho-ms:300000}") int thoiGianCho) {
        this.xuLy = xuLy;
        this.cong = cong;
        this.soLuongToiDa = soLuongToiDa;
        this.thoiGianCho = thoiGianCho;
    }

    @Override
    public void start() {
        try {
            mayChu = new ServerSocket(cong);
        } catch (IOException e) {
            NHAT_KY.error("Không mở được cổng {} cho máy chủ kho: {}", cong, e.getMessage());
            return;
        }
        nhomLuong = Executors.newFixedThreadPool(soLuongToiDa,
                r -> {
                    Thread t = new Thread(r, "kho-client-" + soThuTu.incrementAndGet());
                    t.setDaemon(true);
                    return t;
                });
        dangChay = true;
        luongLangNghe = new Thread(this::langNghe, "kho-lang-nghe");
        luongLangNghe.setDaemon(true);
        luongLangNghe.start();
        NHAT_KY.info("Máy chủ kho đang lắng nghe cổng {}, phục vụ tối đa {} client cùng lúc", getCong(), soLuongToiDa);
    }

    private void langNghe() {
        while (dangChay) {
            try {
                Socket ketNoi = mayChu.accept();
                if (dangPhucVu.incrementAndGet() > soLuongToiDa) {
                    dangPhucVu.decrementAndGet();
                    tuChoi(ketNoi);
                    continue;
                }
                dangMo.add(ketNoi);
                nhomLuong.execute(new PhienKho(ketNoi, xuLy, thoiGianCho, () -> {
                    dangMo.remove(ketNoi);
                    dangPhucVu.decrementAndGet();
                }));
            } catch (SocketException e) {
                if (dangChay) {
                    NHAT_KY.warn("Lỗi socket khi chờ client: {}", e.getMessage());
                }
            } catch (IOException e) {
                NHAT_KY.warn("Không nhận được kết nối: {}", e.getMessage());
            }
        }
    }

    private void tuChoi(Socket ketNoi) {
        try (Socket s = ketNoi;
             BufferedWriter ghi = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8))) {
            ghi.write("LOI|Máy chủ đang bận, đã đủ " + soLuongToiDa + " client, vui lòng thử lại sau");
            ghi.newLine();
            ghi.flush();
            NHAT_KY.warn("Từ chối client {} vì đã đủ {} kết nối", s.getRemoteSocketAddress(), soLuongToiDa);
        } catch (IOException e) {
            NHAT_KY.warn("Không gửi được thông báo từ chối: {}", e.getMessage());
        }
    }

    @Override
    public void stop() {
        dangChay = false;
        try {
            if (mayChu != null) {
                mayChu.close();
            }
        } catch (IOException e) {
            NHAT_KY.warn("Lỗi khi đóng cổng máy chủ kho: {}", e.getMessage());
        }
        for (Socket s : dangMo) {
            try {
                s.close();
            } catch (IOException e) {
                NHAT_KY.warn("Lỗi khi đóng kết nối client: {}", e.getMessage());
            }
        }
        if (nhomLuong != null) {
            nhomLuong.shutdownNow();
            try {
                nhomLuong.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        NHAT_KY.info("Máy chủ kho đã dừng");
    }

    @Override
    public boolean isRunning() {
        return dangChay;
    }

    public int getCong() {
        return mayChu == null ? cong : mayChu.getLocalPort();
    }

    public int getDangPhucVu() {
        return dangPhucVu.get();
    }
}
