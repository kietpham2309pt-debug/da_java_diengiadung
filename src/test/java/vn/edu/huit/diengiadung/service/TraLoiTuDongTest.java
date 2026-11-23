package vn.edu.huit.diengiadung.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.LoaiSanPhamRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraLoiTuDongTest {

    private DonHangRepository duLieuDonHang;
    private TraLoiTuDong boTraLoi;

    @BeforeEach
    void chuanBi() {
        duLieuDonHang = mock(DonHangRepository.class);
        LoaiSanPhamRepository duLieuLoai = mock(LoaiSanPhamRepository.class);
        LoaiSanPham bepTu = new LoaiSanPham();
        bepTu.setTenLoai("Bếp điện từ");
        when(duLieuLoai.findAllByOrderByTenLoaiAsc()).thenReturn(List.of(bepTu));
        CauHinhThanhToan thanhToan = mock(CauHinhThanhToan.class);
        when(thanhToan.getCodToiDa()).thenReturn(20_000_000L);
        when(thanhToan.getPhanTramGiamThanhToanTruoc()).thenReturn(2);
        boTraLoi = new TraLoiTuDong(duLieuDonHang, duLieuLoai, thanhToan, 24);
    }

    @Test
    void boDauVaChuThuong() {
        assertEquals("ship ve dong thap co lau khong", TraLoiTuDong.boDau("  Ship về   Đồng Tháp có LÂU không "));
    }

    @Test
    void nhanDienChuDeKhiGoCoDauHoacKhongDau() {
        assertEquals("giao-hang", boTraLoi.nhanDien("ship về tỉnh có lâu không"));
        assertEquals("thanh-toan", boTraLoi.nhanDien("cho hoi thanh toan chuyen khoan the nao"));
        assertEquals("bao-hanh", boTraLoi.nhanDien("Bếp bị lỗi thì bảo hành ra sao?"));
        assertEquals("nhan-vien", boTraLoi.nhanDien("cho mình gặp nhân viên tư vấn"));
        assertNull(boTraLoi.nhanDien("hôm nay trời đẹp quá"));
    }

    @Test
    void traLoiThanhToanDungCauHinh() {
        String cau = boTraLoi.traLoi("thanh-toan", null);
        assertTrue(cau.contains("20.000.000 ₫"), cau);
        assertTrue(cau.contains("2%"), cau);
        assertTrue(cau.contains("24 giờ"), cau);
    }

    @Test
    void thanhVienTraCuuDonThayToiDaBaDonGanNhat() {
        KhachHang khach = new KhachHang();
        khach.setId(7L);
        when(duLieuDonHang.findByKhachHangIdOrderByNgayDatDesc(7L))
                .thenReturn(List.of(don(41L), don(40L), don(39L), don(38L)));
        String cau = boTraLoi.traLoi("don-hang", khach);
        assertTrue(cau.contains("BN000041"), cau);
        assertTrue(cau.contains("BN000039"), cau);
        assertTrue(!cau.contains("BN000038"), cau);
    }

    @Test
    void khachVangLaiDuocHuongDanTraCuu() {
        assertTrue(boTraLoi.traLoi("don-hang", null).contains("/don-hang/tra-cuu"));
    }

    private DonHang don(Long id) {
        DonHang d = new DonHang();
        d.setId(id);
        d.setTrangThai(DonHang.CHO_XAC_NHAN);
        d.setNgayDat(LocalDateTime.of(2026, 11, 20, 9, 0));
        d.setTongTien(15_240_000L);
        return d;
    }
}
