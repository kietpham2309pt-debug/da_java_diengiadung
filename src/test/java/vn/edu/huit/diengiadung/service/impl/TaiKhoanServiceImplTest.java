package vn.edu.huit.diengiadung.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.KhachHangRepository;
import vn.edu.huit.diengiadung.repository.TaiKhoanRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaiKhoanServiceImplTest {

    private TaiKhoanRepository duLieuTaiKhoan;
    private KhachHangRepository duLieuKhachHang;
    private BCryptPasswordEncoder maHoa;
    private TaiKhoanServiceImpl dichVu;

    @BeforeEach
    void chuanBi() {
        duLieuTaiKhoan = mock(TaiKhoanRepository.class);
        duLieuKhachHang = mock(KhachHangRepository.class);
        maHoa = new BCryptPasswordEncoder();
        dichVu = new TaiKhoanServiceImpl(duLieuTaiKhoan, duLieuKhachHang, maHoa);
        when(duLieuTaiKhoan.save(any(TaiKhoan.class))).thenAnswer(goi -> goi.getArgument(0));
        when(duLieuKhachHang.save(any(KhachHang.class))).thenAnswer(goi -> goi.getArgument(0));
    }

    @Test
    void dangKyLuonTaoTaiKhoanKhachMoi() {
        TaiKhoan guiLen = new TaiKhoan();
        guiLen.setId(1L);
        guiLen.setTenDangNhap("khachmoi");
        guiLen.setMatKhau("matkhau1");
        guiLen.setHoTen("Khách Mới");
        guiLen.setVaiTro("ADMIN");

        TaiKhoan daLuu = dichVu.dangKy(guiLen, "0901234567", "Q1");

        assertNull(daLuu.getId());
        assertEquals("KHACHHANG", daLuu.getVaiTro());
        assertTrue(daLuu.isHoatDong());
        assertTrue(maHoa.matches("matkhau1", daLuu.getMatKhau()));
    }

    @Test
    void dangKyTrungTenDangNhapBiChan() {
        when(duLieuTaiKhoan.existsByTenDangNhap("khach1")).thenReturn(true);
        TaiKhoan guiLen = new TaiKhoan();
        guiLen.setTenDangNhap("khach1");
        guiLen.setMatKhau("matkhau1");

        assertThrows(LoiNghiepVu.class, () -> dichVu.dangKy(guiLen, null, null));
        verify(duLieuTaiKhoan, never()).save(any());
    }

    @Test
    void doiMatKhauKiemTraDuCacTruongHop() {
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setId(5L);
        taiKhoan.setMatKhau(maHoa.encode("cu123456"));
        when(duLieuTaiKhoan.findById(5L)).thenReturn(Optional.of(taiKhoan));

        assertThrows(LoiNghiepVu.class, () -> dichVu.doiMatKhau(5L, "sai", "moi123456", "moi123456"));
        assertThrows(LoiNghiepVu.class, () -> dichVu.doiMatKhau(5L, "cu123456", "123", "123"));
        assertThrows(LoiNghiepVu.class, () -> dichVu.doiMatKhau(5L, "cu123456", "moi123456", "khac12345"));
        assertThrows(LoiNghiepVu.class, () -> dichVu.doiMatKhau(5L, "cu123456", "cu123456", "cu123456"));

        dichVu.doiMatKhau(5L, "cu123456", "moi123456", "moi123456");
        assertTrue(maHoa.matches("moi123456", taiKhoan.getMatKhau()));
    }
}
