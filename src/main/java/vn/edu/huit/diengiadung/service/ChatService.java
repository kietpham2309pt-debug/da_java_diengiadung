package vn.edu.huit.diengiadung.service;

import vn.edu.huit.diengiadung.entity.PhienChat;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.entity.TinNhanChat;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ChatService {

    record KetQuaChat(PhienChat phien, List<TinNhanChat> tinMoi) {
    }

    PhienChat layHoacTaoPhienKhach(TaiKhoan taiKhoan, String maKhach);

    Optional<PhienChat> timPhienKhach(TaiKhoan taiKhoan, String maKhach);

    PhienChat layTheoId(Long id);

    List<TinNhanChat> lichSu(Long phienId);

    KetQuaChat khachGuiTin(Long phienId, String noiDung, boolean coNhanVienTrucTuyen);

    KetQuaChat khachChonGoiY(Long phienId, String ma, boolean coNhanVienTrucTuyen);

    KetQuaChat khachGuiThongTin(Long phienId, String ten, String soDienThoai, boolean coNhanVienTrucTuyen);

    PhienChat khachDaXem(Long phienId);

    KetQuaChat nhanVienMo(Long phienId, TaiKhoan nhanVien);

    KetQuaChat nhanVienGui(Long phienId, TaiKhoan nhanVien, String noiDung);

    KetQuaChat nhanVienKetThuc(Long phienId, TaiKhoan nhanVien);

    PhienChat layHoacTaoPhienChoKhachHang(Long khachHangId);

    List<PhienChat> danhSachQuanTri();

    long tongChuaDocQuanTri();

    Map<Long, PhienChat> phienTheoKhachHang();

    Optional<TinNhanChat> tinCuoi(Long phienId);
}
