package vn.edu.huit.diengiadung.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.KhuyenMai;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;
import vn.edu.huit.diengiadung.entity.MaGiamGia;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.dto.BangGia;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.KhuyenMaiRepository;
import vn.edu.huit.diengiadung.repository.MaGiamGiaRepository;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UuDaiServiceImplTest {

    private MaGiamGiaRepository duLieuMa;
    private KhuyenMaiRepository duLieuKhuyenMai;
    private DonHangRepository duLieuDonHang;
    private UuDaiServiceImpl dichVu;
    private Map<SanPham, Long> gio;
    private KhachHang thanhVien;

    @BeforeEach
    void chuanBi() {
        duLieuMa = mock(MaGiamGiaRepository.class);
        duLieuKhuyenMai = mock(KhuyenMaiRepository.class);
        duLieuDonHang = mock(DonHangRepository.class);
        dichVu = new UuDaiServiceImpl(duLieuMa, duLieuKhuyenMai, duLieuDonHang);

        LoaiSanPham bepTu = new LoaiSanPham();
        bepTu.setId(1L);
        LoaiSanPham hutMui = new LoaiSanPham();
        hutMui.setId(3L);
        SanPham bep = new SanPham();
        bep.setLoaiSanPham(bepTu);
        SanPham may = new SanPham();
        may.setLoaiSanPham(hutMui);
        gio = new LinkedHashMap<>();
        gio.put(bep, 10_000_000L);
        gio.put(may, 5_000_000L);

        thanhVien = new KhachHang();
        thanhVien.setId(7L);
    }

    private KhuyenMai khuyenMai(String ten, long giaTri, String loai, boolean chiThanhVien) {
        KhuyenMai km = new KhuyenMai();
        km.setTen(ten);
        km.setGiaTri(giaTri);
        km.setLoaiGiam(loai);
        km.setChiThanhVien(chiThanhVien);
        km.setBatDau(LocalDateTime.now().minusDays(1));
        return km;
    }

    private MaGiamGia ma(String chuoi, long giaTri) {
        MaGiamGia ma = new MaGiamGia();
        ma.setMa(chuoi);
        ma.setGiaTri(giaTri);
        ma.setLoaiGiam(MaGiamGia.PHAN_TRAM);
        ma.setBatDau(LocalDateTime.now().minusDays(1));
        return ma;
    }

    @Test
    void chonKhuyenMaiCoLoiNhatVaChiTinhTrenSanPhamDungPhamVi() {
        KhuyenMai theoLoai = khuyenMai("Bếp từ 10%", 10, KhuyenMai.PHAN_TRAM, false);
        theoLoai.setPhamVi(KhuyenMai.THEO_LOAI);
        LoaiSanPham bepTu = new LoaiSanPham();
        bepTu.setId(1L);
        theoLoai.setLoaiSanPham(bepTu);
        KhuyenMai coDinh = khuyenMai("Giảm 500k", 500_000, KhuyenMai.SO_TIEN, false);
        when(duLieuKhuyenMai.findByHoatDongTrue()).thenReturn(List.of(coDinh, theoLoai));

        BangGia bangGia = dichVu.tinhGia(gio, null, null);

        assertEquals("Bếp từ 10%", bangGia.getKhuyenMai().getTen());
        assertEquals(1_000_000L, bangGia.getTienKhuyenMai());
        assertEquals(14_000_000L, bangGia.getSauUuDai());
    }

    @Test
    void khachVangLaiKhongNhanKhuyenMaiThanhVienVaKhongDungDuocMa() {
        when(duLieuKhuyenMai.findByHoatDongTrue()).thenReturn(List.of(khuyenMai("Thành viên", 5, KhuyenMai.PHAN_TRAM, true)));

        BangGia bangGia = dichVu.tinhGia(gio, null, "CHAOBANMOI");

        assertNull(bangGia.getKhuyenMai());
        assertNull(bangGia.getMaGiamGia());
        assertNotNull(bangGia.getLoiMa());
        assertEquals(15_000_000L, bangGia.getSauUuDai());
    }

    @Test
    void maPhanTramBiChanBoiMucGiamToiDaVaTinhSauKhuyenMai() {
        when(duLieuKhuyenMai.findByHoatDongTrue()).thenReturn(List.of(khuyenMai("Giảm 1 triệu", 1_000_000, KhuyenMai.SO_TIEN, true)));
        MaGiamGia ma = ma("CHAOBANMOI", 5);
        ma.setGiamToiDa(500_000L);
        when(duLieuMa.findByMaIgnoreCase("CHAOBANMOI")).thenReturn(Optional.of(ma));

        BangGia bangGia = dichVu.tinhGia(gio, thanhVien, "CHAOBANMOI");

        assertNull(bangGia.getLoiMa());
        assertEquals(500_000L, bangGia.getTienGiamMa());
        assertEquals(13_500_000L, bangGia.getSauUuDai());
    }

    @Test
    void tuChoiMaHetLuotChuaDuDonHoacDaDung() {
        when(duLieuKhuyenMai.findByHoatDongTrue()).thenReturn(List.of());
        MaGiamGia hetLuot = ma("HETLUOT", 5);
        hetLuot.setGioiHanLuotDung(1);
        hetLuot.setSoLuotDaDung(1);
        MaGiamGia donLon = ma("DONLON", 5);
        donLon.setDonToiThieu(20_000_000L);
        MaGiamGia daDung = ma("DADUNG", 5);
        when(duLieuMa.findByMaIgnoreCase(anyString())).thenAnswer(goi -> switch ((String) goi.getArgument(0)) {
            case "HETLUOT" -> Optional.of(hetLuot);
            case "DONLON" -> Optional.of(donLon);
            case "DADUNG" -> Optional.of(daDung);
            default -> Optional.empty();
        });
        when(duLieuDonHang.countByKhachHangIdAndMaGiamGiaIgnoreCaseAndTrangThaiNot(anyLong(), any(), any())).thenReturn(1L);

        assertEquals("Mã giảm giá đã hết lượt sử dụng", dichVu.tinhGia(gio, thanhVien, "HETLUOT").getLoiMa());
        assertNotNull(dichVu.tinhGia(gio, thanhVien, "DONLON").getLoiMa());
        assertEquals("Bạn đã dùng mã này cho một đơn hàng trước đó", dichVu.tinhGia(gio, thanhVien, "DADUNG").getLoiMa());
        assertEquals("Mã giảm giá không tồn tại hoặc đã ngừng áp dụng", dichVu.tinhGia(gio, thanhVien, "KHONGCO").getLoiMa());
    }
}
