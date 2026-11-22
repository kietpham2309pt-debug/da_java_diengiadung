package vn.edu.huit.diengiadung.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.repository.DonHangRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TuHuyDonQuaHan {

    public static final String LY_DO = "Quá hạn chuyển khoản, hệ thống tự huỷ";

    private static final Logger NHAT_KY = LoggerFactory.getLogger(TuHuyDonQuaHan.class);

    private final DonHangRepository donHangRepository;
    private final DonHangService donHangService;
    private final long soGio;

    public TuHuyDonQuaHan(DonHangRepository donHangRepository,
                          DonHangService donHangService,
                          @Value("${cuahang.tu-huy.sau-gio:24}") long soGio) {
        this.donHangRepository = donHangRepository;
        this.donHangService = donHangService;
        this.soGio = soGio;
    }

    @Scheduled(initialDelayString = "${cuahang.tu-huy.tre-ms:60000}", fixedDelayString = "${cuahang.tu-huy.chu-ky-ms:600000}")
    public void chay() {
        huyDonQuaHan(LocalDateTime.now().minusHours(soGio));
    }

    public int huyDonQuaHan(LocalDateTime moc) {
        List<DonHang> quaHan = donHangRepository.findByTrangThaiAndPhuongThucThanhToanAndDaThanhToanFalseAndNgayDatBefore(
                DonHang.CHO_XAC_NHAN, DonHang.THANH_TOAN_TRUOC, moc);
        int daHuy = 0;
        for (DonHang donHang : quaHan) {
            try {
                donHangService.huyDonHang(donHang.getId(), LY_DO, "HE_THONG");
                daHuy++;
            } catch (LoiNghiepVu loi) {
                NHAT_KY.warn("[{}] Bỏ qua đơn BN{}: {}", Thread.currentThread().getName(), donHang.getId(), loi.getMessage());
            }
        }
        NHAT_KY.info("[{}] Quét đơn chuyển khoản quá {} giờ: {} đơn quá hạn, đã huỷ {} đơn",
                Thread.currentThread().getName(), soGio, quaHan.size(), daHuy);
        return daHuy;
    }
}
