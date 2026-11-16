package vn.edu.huit.diengiadung.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GioHangServiceImplTest {

    private SanPham bep;
    private GioHangServiceImpl dichVu;

    @BeforeEach
    void chuanBi() {
        SanPhamRepository duLieuSanPham = mock(SanPhamRepository.class);
        bep = new SanPham();
        bep.setId(1L);
        bep.setTenSanPham("Bếp từ thử");
        bep.setGiaBan(10_000_000L);
        bep.setSoLuongTon(5);
        bep.setDangBan(true);
        when(duLieuSanPham.findById(1L)).thenReturn(Optional.of(bep));
        when(duLieuSanPham.findById(99L)).thenReturn(Optional.empty());
        dichVu = new GioHangServiceImpl(duLieuSanPham);
    }

    @Test
    void themCongDonVaKhongVuotTonKho() {
        GioHang gioHang = new GioHang();
        dichVu.them(gioHang, 1L, 2);
        dichVu.them(gioHang, 1L, 3);
        assertEquals(5, gioHang.laySoLuong(1L));
        assertThrows(LoiNghiepVu.class, () -> dichVu.them(gioHang, 1L, 1));
        assertThrows(LoiNghiepVu.class, () -> dichVu.them(gioHang, 1L, 0));
        assertThrows(LoiNghiepVu.class, () -> dichVu.them(gioHang, 99L, 1));
    }

    @Test
    void capNhatVeKhongThiXoaKhoiGio() {
        GioHang gioHang = new GioHang();
        dichVu.them(gioHang, 1L, 2);
        dichVu.capNhat(gioHang, 1L, 0);
        assertTrue(gioHang.isTrong());
    }

    @Test
    void xemChiTietTuChinhKhiTonKhoGiamHoacNgungBan() {
        GioHang gioHang = new GioHang();
        dichVu.them(gioHang, 1L, 4);
        bep.setSoLuongTon(2);
        ChiTietGioHang chiTiet = dichVu.xemChiTiet(gioHang);
        assertEquals(2, chiTiet.getTongSoLuong());
        assertEquals(20_000_000L, chiTiet.getTongTien());
        assertFalse(chiTiet.getCanhBao().isEmpty());

        bep.setDangBan(false);
        assertTrue(dichVu.xemChiTiet(gioHang).isTrong());
    }
}
