package vn.edu.huit.diengiadung.tichhop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.TrangThaiGhn;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VanChuyenGhn {

    private static final Logger log = LoggerFactory.getLogger(VanChuyenGhn.class);

    private final CauHinhGhn cauHinh;
    private final KhachHangGhn ghn;
    private final DonHangRepository donHangRepository;
    private final TransactionTemplate giaoDich;

    @PersistenceContext
    private EntityManager entityManager;

    public VanChuyenGhn(CauHinhGhn cauHinh, KhachHangGhn ghn, DonHangRepository donHangRepository,
                        PlatformTransactionManager quanLyGiaoDich) {
        this.cauHinh = cauHinh;
        this.ghn = ghn;
        this.donHangRepository = donHangRepository;
        this.giaoDich = new TransactionTemplate(quanLyGiaoDich);
    }

    public KhachHangGhn.KetQuaVanDon xemPhi(Long idDon) {
        kiemTraSanSang();
        return ghn.xemTruoc(layDonDeGui(idDon));
    }

    public KhachHangGhn.KetQuaVanDon taoVanDon(Long idDon) {
        kiemTraSanSang();
        DonHang don = layDonDeGui(idDon);
        String maGui = don.getTrangThaiVanChuyen() == null
                ? don.getMaDon()
                : don.getMaDon() + "-" + Long.toString(System.currentTimeMillis() / 1000, 36).toUpperCase();
        KhachHangGhn.KetQuaVanDon kq = ghn.taoVanDon(don, maGui);
        giaoDich.executeWithoutResult(t -> {
            DonHang khoa = khoaDon(idDon);
            khoa.setMaVanDon(kq.maVanDon());
            khoa.setTrangThaiVanChuyen("ready_to_pick");
            khoa.setPhiVanChuyen(kq.phi());
            khoa.setNgayGiaoDuKien(kq.ngayGiaoDuKien());
            khoa.setCapNhatVanChuyen(LocalDateTime.now());
        });
        log.info("Tạo vận đơn GHN {} cho đơn {}, phí {} đ", kq.maVanDon(), don.getMaDon(), kq.phi());
        return kq;
    }

    public void huyVanDon(Long idDon) {
        kiemTraSanSang();
        DonHang don = layDon(idDon);
        if (!don.isCoVanDon()) {
            throw new LoiNghiepVu("Đơn này chưa có vận đơn GHN đang hoạt động");
        }
        ghn.huyVanDon(don.getMaVanDon());
        giaoDich.executeWithoutResult(t -> {
            DonHang khoa = khoaDon(idDon);
            khoa.setTrangThaiVanChuyen(TrangThaiGhn.DA_HUY);
            khoa.setCapNhatVanChuyen(LocalDateTime.now());
        });
    }

    public String dongBo(Long idDon) {
        kiemTraSanSang();
        DonHang don = layDon(idDon);
        if (don.getMaVanDon() == null) {
            throw new LoiNghiepVu("Đơn này chưa có vận đơn GHN");
        }
        String trangThai = ghn.trangThaiVanDon(don.getMaVanDon());
        giaoDich.executeWithoutResult(t -> apDungTrangThai(khoaDon(idDon), trangThai));
        return trangThai;
    }

    public boolean nhanWebhook(String maVanDon, String trangThai) {
        if (maVanDon == null || maVanDon.isBlank() || trangThai == null || trangThai.isBlank()) {
            return false;
        }
        Boolean daCapNhat = giaoDich.execute(t -> {
            Optional<DonHang> tim = donHangRepository.findByMaVanDon(maVanDon.trim());
            if (tim.isEmpty()) {
                return false;
            }
            DonHang don = tim.get();
            entityManager.refresh(don, LockModeType.PESSIMISTIC_WRITE);
            apDungTrangThai(don, trangThai.trim());
            return true;
        });
        return Boolean.TRUE.equals(daCapNhat);
    }

    static void apDungTrangThai(DonHang don, String trangThai) {
        don.setTrangThaiVanChuyen(trangThai);
        don.setCapNhatVanChuyen(LocalDateTime.now());
        String hienTai = don.getTrangThai();
        if (TrangThaiGhn.daRoiKho(trangThai) && DonHang.DA_XAC_NHAN.equals(hienTai)) {
            don.setTrangThai(DonHang.DANG_GIAO);
        } else if (TrangThaiGhn.DA_GIAO.equals(trangThai)
                && (DonHang.DA_XAC_NHAN.equals(hienTai) || DonHang.DANG_GIAO.equals(hienTai))) {
            don.setTrangThai(DonHang.HOAN_THANH);
        }
    }

    private void kiemTraSanSang() {
        if (!cauHinh.isBat()) {
            throw new LoiNghiepVu("Chưa bật kết nối GHN (ghn.bat=false trong tich-hop.properties)");
        }
        if (!cauHinh.isSanSang()) {
            throw new LoiNghiepVu("Chưa khai báo ghn.token và ghn.shop-id trong tich-hop.properties");
        }
    }

    private DonHang layDon(Long idDon) {
        return donHangRepository.findById(idDon).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy đơn hàng " + idDon));
    }

    private DonHang layDonDeGui(Long idDon) {
        DonHang don = layDon(idDon);
        if (!DonHang.DA_XAC_NHAN.equals(don.getTrangThai())) {
            throw new LoiNghiepVu("Chỉ tạo vận đơn cho đơn đã xác nhận (hiện tại: " + don.getTenTrangThai() + ")");
        }
        if (don.isCoVanDon()) {
            throw new LoiNghiepVu("Đơn đã có vận đơn GHN " + don.getMaVanDon());
        }
        return don;
    }

    private DonHang khoaDon(Long idDon) {
        DonHang don = entityManager.find(DonHang.class, idDon);
        if (don == null) {
            throw new LoiNghiepVu("Không tìm thấy đơn hàng " + idDon);
        }
        entityManager.refresh(don, LockModeType.PESSIMISTIC_WRITE);
        return don;
    }
}
