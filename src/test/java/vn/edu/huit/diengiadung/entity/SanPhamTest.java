package vn.edu.huit.diengiadung.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SanPhamTest {

    @Test
    void tachThongSoVaTinhNangTheoDauHaiCham() {
        SanPham sanPham = new SanPham();
        sanPham.setThongSoKyThuat("Kích thước khoét đá: 560 x 490 mm; Số mức công suất: 17\nKhoá an toàn trẻ em\n\nHẹn giờ 99 phút");

        List<SanPham.ThongSo> danhSachThongSo = sanPham.getDanhSachThongSo();
        assertEquals(2, danhSachThongSo.size());
        assertEquals("Kích thước khoét đá", danhSachThongSo.get(0).getTen());
        assertEquals("560 x 490 mm", danhSachThongSo.get(0).getGiaTri());

        List<SanPham.TinhNang> danhSachTinhNang = sanPham.getDanhSachTinhNang();
        assertEquals(2, danhSachTinhNang.size());
        assertEquals("Khoá an toàn trẻ em", danhSachTinhNang.get(0).getTen());
        assertEquals("ic-tn-khoa", danhSachTinhNang.get(0).getBieuTuong());
        assertEquals("ic-dong-ho", danhSachTinhNang.get(1).getBieuTuong());
    }

    @Test
    void giaHienTaiVaPhanTramGiam() {
        SanPham sanPham = new SanPham();
        sanPham.setGiaBan(10_000_000L);
        assertEquals(10_000_000L, sanPham.getGiaHienTai());
        assertEquals(0, sanPham.getPhanTramGiam());
        assertEquals(0, sanPham.getSoTienTietKiem());

        sanPham.setGiaKhuyenMai(7_500_000L);
        assertEquals(7_500_000L, sanPham.getGiaHienTai());
        assertEquals(25, sanPham.getPhanTramGiam());
        assertEquals(2_500_000L, sanPham.getSoTienTietKiem());
    }

    @Test
    void duongDanAnhUuTienAnhUploadRoiToiAnhDanhMuc() {
        LoaiSanPham loai = new LoaiSanPham();
        loai.setMaLoai("lo-nuong");
        SanPham sanPham = new SanPham();
        sanPham.setLoaiSanPham(loai);

        assertFalse(sanPham.coAnhRieng());
        assertEquals("/img/sp-lo-nuong.jpg", sanPham.getDuongDanAnh());

        sanPham.setHinhAnh("sp-abc.png");
        assertTrue(sanPham.coAnhRieng());
        assertEquals("/uploads/sp-abc.png", sanPham.getDuongDanAnh());
    }

    @Test
    void tinhTrangKho() {
        SanPham sanPham = new SanPham();
        assertFalse(sanPham.isConHang());
        sanPham.setSoLuongTon(3);
        assertTrue(sanPham.isConHang());
        assertTrue(sanPham.isSapHetHang());
        sanPham.setSoLuongTon(12);
        assertFalse(sanPham.isSapHetHang());
    }
}
