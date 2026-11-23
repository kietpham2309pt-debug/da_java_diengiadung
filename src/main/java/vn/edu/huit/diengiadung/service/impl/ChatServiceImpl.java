package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.entity.TinNhanChat;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.PhienChatRepository;
import vn.edu.huit.diengiadung.repository.TinNhanChatRepository;
import vn.edu.huit.diengiadung.service.ChatService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.TraLoiTuDong;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChatServiceImpl implements ChatService {

    private static final int DO_DAI_TOI_DA = 1000;

    private final PhienChatRepository phienChatRepository;
    private final TinNhanChatRepository tinNhanChatRepository;
    private final KhachHangRepository khachHangRepository;
    private final TraLoiTuDong traLoiTuDong;

    public ChatServiceImpl(PhienChatRepository phienChatRepository,
                           TinNhanChatRepository tinNhanChatRepository,
                           KhachHangRepository khachHangRepository,
                           TraLoiTuDong traLoiTuDong) {
        this.phienChatRepository = phienChatRepository;
        this.tinNhanChatRepository = tinNhanChatRepository;
        this.khachHangRepository = khachHangRepository;
        this.traLoiTuDong = traLoiTuDong;
    }

    @Override
    @Transactional
    public PhienChat layHoacTaoPhienKhach(TaiKhoan taiKhoan, String maKhach) {
        Optional<PhienChat> daCo = timPhienKhach(taiKhoan, maKhach);
        if (daCo.isPresent()) {
            return daCo.get();
        }
        KhachHang khachHang = layKhachHang(taiKhoan);
        if (khachHang == null && (maKhach == null || maKhach.isBlank())) {
            throw new LoiNghiepVu("Không xác định được khách hàng của phiên chat");
        }
        PhienChat phien = new PhienChat();
        if (khachHang != null) {
            phien.setKhachHang(khachHang);
        } else {
            phien.setMaKhach(maKhach);
        }
        phien = phienChatRepository.save(phien);
        them(phien, TinNhanChat.BOT, "Trợ lý Bep Nè",
                traLoiTuDong.loiChao(khachHang == null ? null : khachHang.getHoTen()), traLoiTuDong.goiYMacDinh(), false);
        return phien;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PhienChat> timPhienKhach(TaiKhoan taiKhoan, String maKhach) {
        KhachHang khachHang = layKhachHang(taiKhoan);
        if (khachHang != null) {
            return phienChatRepository.findFirstByKhachHangIdOrderByIdDesc(khachHang.getId());
        }
        if (maKhach == null || maKhach.isBlank()) {
            return Optional.empty();
        }
        return phienChatRepository.findByMaKhach(maKhach);
    }

    @Override
    @Transactional(readOnly = true)
    public PhienChat layTheoId(Long id) {
        return phienChatRepository.findById(id).orElseThrow(() -> new LoiNghiepVu("Không tìm thấy cuộc trò chuyện"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TinNhanChat> lichSu(Long phienId) {
        List<TinNhanChat> ds = new ArrayList<>(tinNhanChatRepository.findTop100ByPhienChatIdOrderByIdDesc(phienId));
        Collections.reverse(ds);
        return ds;
    }

    @Override
    @Transactional
    public KetQuaChat khachGuiTin(Long phienId, String noiDung, boolean coNhanVienTrucTuyen) {
        String sach = lamSach(noiDung);
        PhienChat phien = khoa(phienId);
        List<TinNhanChat> tinMoi = new ArrayList<>();
        tinMoi.add(them(phien, TinNhanChat.KHACH, phien.getTenHienThi(), sach, null, false));
        if (dangGapNhanVien(phien)) {
            phien.setSoChuaDocQuanTri(phien.getSoChuaDocQuanTri() + 1);
            return new KetQuaChat(phien, tinMoi);
        }
        String ma = traLoiTuDong.nhanDien(sach);
        if (TraLoiTuDong.GAP_NHAN_VIEN.equals(ma)) {
            tinMoi.addAll(yeuCauNhanVien(phien, coNhanVienTrucTuyen));
        } else {
            tinMoi.add(them(phien, TinNhanChat.BOT, "Trợ lý Bep Nè", traLoiTuDong.traLoi(ma == null ? "" : ma, phien.getKhachHang()),
                    traLoiTuDong.goiYMacDinh(), false));
        }
        return new KetQuaChat(phien, tinMoi);
    }

    @Override
    @Transactional
    public KetQuaChat khachChonGoiY(Long phienId, String ma, boolean coNhanVienTrucTuyen) {
        String nhan = traLoiTuDong.nhanCuaChuDe(ma);
        if (nhan == null) {
            throw new LoiNghiepVu("Chủ đề không hợp lệ");
        }
        PhienChat phien = khoa(phienId);
        List<TinNhanChat> tinMoi = new ArrayList<>();
        tinMoi.add(them(phien, TinNhanChat.KHACH, phien.getTenHienThi(), nhan, null, false));
        if (TraLoiTuDong.GAP_NHAN_VIEN.equals(ma)) {
            tinMoi.addAll(yeuCauNhanVien(phien, coNhanVienTrucTuyen));
        } else if (dangGapNhanVien(phien)) {
            phien.setSoChuaDocQuanTri(phien.getSoChuaDocQuanTri() + 1);
        } else {
            tinMoi.add(them(phien, TinNhanChat.BOT, "Trợ lý Bep Nè", traLoiTuDong.traLoi(ma, phien.getKhachHang()),
                    traLoiTuDong.goiYMacDinh(), false));
        }
        return new KetQuaChat(phien, tinMoi);
    }

    @Override
    @Transactional
    public KetQuaChat khachGuiThongTin(Long phienId, String ten, String soDienThoai, boolean coNhanVienTrucTuyen) {
        PhienChat phien = khoa(phienId);
        String tenSach = ten == null ? "" : ten.trim();
        String sdtSach = soDienThoai == null ? "" : soDienThoai.replaceAll("[\\s.]", "");
        if (!tenSach.isEmpty() || !sdtSach.isEmpty()) {
            if (tenSach.length() < 2 || tenSach.length() > 80) {
                throw new LoiNghiepVu("Họ tên cần từ 2 đến 80 ký tự");
            }
            if (!sdtSach.matches("^0\\d{9}$")) {
                throw new LoiNghiepVu("Số điện thoại phải gồm đúng 10 chữ số và bắt đầu bằng 0");
            }
            phien.setTenKhach(tenSach);
            phien.setSoDienThoai(sdtSach);
        }
        List<TinNhanChat> tinMoi = new ArrayList<>();
        if (!tenSach.isEmpty()) {
            tinMoi.add(them(phien, TinNhanChat.KHACH, phien.getTenHienThi(), tenSach + " - " + sdtSach, null, false));
        }
        tinMoi.addAll(chuyenNhanVien(phien, coNhanVienTrucTuyen));
        return new KetQuaChat(phien, tinMoi);
    }

    @Override
    @Transactional
    public PhienChat khachDaXem(Long phienId) {
        PhienChat phien = khoa(phienId);
        phien.setSoChuaDocKhach(0);
        return phien;
    }

    @Override
    @Transactional
    public KetQuaChat nhanVienMo(Long phienId, TaiKhoan nhanVien) {
        PhienChat phien = khoa(phienId);
        phien.setSoChuaDocQuanTri(0);
        List<TinNhanChat> tinMoi = new ArrayList<>();
        if (PhienChat.CHO_NHAN_VIEN.equals(phien.getTrangThai())) {
            batDauTuVan(phien, nhanVien, tinMoi);
        }
        return new KetQuaChat(phien, tinMoi);
    }

    @Override
    @Transactional
    public KetQuaChat nhanVienGui(Long phienId, TaiKhoan nhanVien, String noiDung) {
        String sach = lamSach(noiDung);
        PhienChat phien = khoa(phienId);
        List<TinNhanChat> tinMoi = new ArrayList<>();
        if (!PhienChat.DANG_TU_VAN.equals(phien.getTrangThai())) {
            batDauTuVan(phien, nhanVien, tinMoi);
        }
        tinMoi.add(them(phien, TinNhanChat.NHAN_VIEN, nhanVien.getHoTen(), sach, null, false));
        phien.setSoChuaDocQuanTri(0);
        phien.setSoChuaDocKhach(phien.getSoChuaDocKhach() + 1);
        return new KetQuaChat(phien, tinMoi);
    }

    @Override
    @Transactional
    public KetQuaChat nhanVienKetThuc(Long phienId, TaiKhoan nhanVien) {
        PhienChat phien = khoa(phienId);
        List<TinNhanChat> tinMoi = new ArrayList<>();
        if (PhienChat.BOT.equals(phien.getTrangThai())) {
            return new KetQuaChat(phien, tinMoi);
        }
        phien.setTrangThai(PhienChat.BOT);
        phien.setTenNhanVien(null);
        phien.setSoChuaDocQuanTri(0);
        tinMoi.add(them(phien, TinNhanChat.HE_THONG, null,
                "Nhân viên " + nhanVien.getHoTen() + " đã kết thúc phiên tư vấn. Cảm ơn bạn đã liên hệ Bep Nè!", null, false));
        tinMoi.add(them(phien, TinNhanChat.BOT, "Trợ lý Bep Nè",
                "Bạn cần hỗ trợ thêm gì không? Chọn chủ đề bên dưới hoặc gặp lại nhân viên bất cứ lúc nào.",
                traLoiTuDong.goiYMacDinh(), false));
        phien.setSoChuaDocKhach(phien.getSoChuaDocKhach() + 1);
        return new KetQuaChat(phien, tinMoi);
    }

    @Override
    @Transactional
    public PhienChat layHoacTaoPhienChoKhachHang(Long khachHangId) {
        Optional<PhienChat> daCo = phienChatRepository.findFirstByKhachHangIdOrderByIdDesc(khachHangId);
        if (daCo.isPresent()) {
            return daCo.get();
        }
        KhachHang khachHang = khachHangRepository.findById(khachHangId)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy khách hàng"));
        PhienChat phien = new PhienChat();
        phien.setKhachHang(khachHang);
        phien = phienChatRepository.save(phien);
        them(phien, TinNhanChat.BOT, "Trợ lý Bep Nè", traLoiTuDong.loiChao(khachHang.getHoTen()), traLoiTuDong.goiYMacDinh(), false);
        return phien;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PhienChat> danhSachQuanTri() {
        return phienChatRepository.layDanhSachQuanTri();
    }

    @Override
    @Transactional(readOnly = true)
    public long tongChuaDocQuanTri() {
        return phienChatRepository.tongChuaDocQuanTri();
    }

    @Override
    @Transactional(readOnly = true)
    public Map<Long, PhienChat> phienTheoKhachHang() {
        Map<Long, PhienChat> ketQua = new LinkedHashMap<>();
        for (PhienChat phien : phienChatRepository.findByKhachHangIsNotNull()) {
            ketQua.merge(phien.getKhachHang().getId(), phien, (cu, moi) -> moi.getId() > cu.getId() ? moi : cu);
        }
        return ketQua;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TinNhanChat> tinCuoi(Long phienId) {
        return tinNhanChatRepository.findFirstByPhienChatIdOrderByIdDesc(phienId);
    }

    private List<TinNhanChat> yeuCauNhanVien(PhienChat phien, boolean coNhanVienTrucTuyen) {
        List<TinNhanChat> tinMoi = new ArrayList<>();
        if (dangGapNhanVien(phien)) {
            tinMoi.add(them(phien, TinNhanChat.HE_THONG, null,
                    "Yêu cầu của bạn đã được chuyển tới nhân viên, bạn cứ nhắn câu hỏi ở đây nhé.", null, false));
            phien.setSoChuaDocQuanTri(phien.getSoChuaDocQuanTri() + 1);
            return tinMoi;
        }
        if (phien.laKhachVangLai() && (phien.getSoDienThoai() == null || phien.getSoDienThoai().isBlank())) {
            tinMoi.add(them(phien, TinNhanChat.BOT, "Trợ lý Bep Nè",
                    "Để nhân viên tiện liên hệ lại khi cần, bạn cho Bep Nè xin họ tên và số điện thoại nhé. "
                            + "Bạn cũng có thể bỏ qua bước này.", null, true));
            return tinMoi;
        }
        return chuyenNhanVien(phien, coNhanVienTrucTuyen);
    }

    private List<TinNhanChat> chuyenNhanVien(PhienChat phien, boolean coNhanVienTrucTuyen) {
        List<TinNhanChat> tinMoi = new ArrayList<>();
        if (dangGapNhanVien(phien)) {
            return tinMoi;
        }
        phien.setTrangThai(PhienChat.CHO_NHAN_VIEN);
        phien.setSoChuaDocQuanTri(phien.getSoChuaDocQuanTri() + 1);
        String loi = "Đã chuyển yêu cầu tới nhân viên tư vấn. Bạn vui lòng chờ trong giây lát, nhân viên sẽ trả lời ngay "
                + "tại khung chat này.";
        if (!coNhanVienTrucTuyen) {
            loi += " Hiện chưa có nhân viên trực tuyến, Bep Nè sẽ phản hồi sớm nhất trong giờ làm việc (8:00 tới 21:00).";
        }
        tinMoi.add(them(phien, TinNhanChat.HE_THONG, null, loi, null, false));
        return tinMoi;
    }

    private void batDauTuVan(PhienChat phien, TaiKhoan nhanVien, List<TinNhanChat> tinMoi) {
        phien.setTrangThai(PhienChat.DANG_TU_VAN);
        phien.setTenNhanVien(nhanVien.getHoTen());
        tinMoi.add(them(phien, TinNhanChat.HE_THONG, null,
                "Nhân viên " + nhanVien.getHoTen() + " đã tham gia cuộc trò chuyện.", null, false));
    }

    private boolean dangGapNhanVien(PhienChat phien) {
        return PhienChat.CHO_NHAN_VIEN.equals(phien.getTrangThai()) || PhienChat.DANG_TU_VAN.equals(phien.getTrangThai());
    }

    private PhienChat khoa(Long phienId) {
        return phienChatRepository.khoaTheoId(phienId)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy cuộc trò chuyện"));
    }

    private TinNhanChat them(PhienChat phien, String nguoiGui, String tenNguoiGui, String noiDung, String goiY, boolean canThongTin) {
        TinNhanChat tin = new TinNhanChat();
        tin.setPhienChat(phien);
        tin.setNguoiGui(nguoiGui);
        tin.setTenNguoiGui(tenNguoiGui);
        tin.setNoiDung(noiDung);
        tin.setGoiY(goiY);
        tin.setCanThongTin(canThongTin);
        phien.setCapNhatLuc(LocalDateTime.now());
        return tinNhanChatRepository.save(tin);
    }

    private String lamSach(String noiDung) {
        String sach = noiDung == null ? "" : noiDung.strip();
        if (sach.isEmpty()) {
            throw new LoiNghiepVu("Tin nhắn không được để trống");
        }
        if (sach.length() > DO_DAI_TOI_DA) {
            throw new LoiNghiepVu("Tin nhắn tối đa " + DO_DAI_TOI_DA + " ký tự");
        }
        return sach;
    }

    private KhachHang layKhachHang(TaiKhoan taiKhoan) {
        if (taiKhoan == null || taiKhoan.laQuanTri()) {
            return null;
        }
        return khachHangRepository.findByTaiKhoan(taiKhoan).orElse(null);
    }
}
