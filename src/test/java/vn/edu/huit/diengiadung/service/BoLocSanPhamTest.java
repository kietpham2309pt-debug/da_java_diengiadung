package vn.edu.huit.diengiadung.service;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoLocSanPhamTest {

    @Test
    void duongDanTrangGiuNguyenBoLoc() {
        BoLocSanPham boLoc = new BoLocSanPham();
        boLoc.setLoai(1L);
        boLoc.setTuKhoa("  bếp từ ");
        boLoc.setHang(Arrays.asList(2L, null, 2L, 3L));
        boLoc.setMucGia(List.of("duoi-5-trieu", "khong-co"));
        boLoc.setSapXep("gia-tang");

        assertEquals("/san-pham?loai=1&tuKhoa=b%E1%BA%BFp+t%E1%BB%AB&hang=2&hang=3"
                + "&mucGia=duoi-5-trieu&sapXep=gia-tang&trang=3", boLoc.duongDanTrang(3));
        assertTrue(boLoc.isDangLoc());
    }

    @Test
    void giaTriMacDinhKhiThieuThamSo() {
        BoLocSanPham boLoc = new BoLocSanPham();
        boLoc.setTuKhoa("   ");
        boLoc.setTrang(-4);
        boLoc.setSapXep("khong-hop-le");

        assertNull(boLoc.getTuKhoaDaGon());
        assertEquals(1, boLoc.getTrangCanXem());
        assertEquals(KieuSapXep.MOI_NHAT, boLoc.getKieuSapXep());
        assertFalse(boLoc.isDangLoc());
    }

    @Test
    void thanhPhanTrangToiDaNamNut() {
        List<Integer> duLieu = new ArrayList<>();
        for (int so = 0; so < 95; so++) {
            duLieu.add(so);
        }
        KetQuaPhanTrang<Integer> ketQua = new KetQuaPhanTrang<>(
                new PageImpl<>(duLieu.subList(0, 10), PageRequest.of(5, 10), duLieu.size()));

        assertEquals(6, ketQua.getTrangHienTai());
        assertEquals(10, ketQua.getSoTrang());
        assertEquals(List.of(4, 5, 6, 7, 8), ketQua.getDanhSachSoTrang());
        assertTrue(ketQua.isCoTrangTruoc());
        assertTrue(ketQua.isCoTrangSau());
    }

    @Test
    void mucGiaTimTheoMa() {
        assertEquals(MucGia.TREN_20_TRIEU, MucGia.timTheoMa("tren-20-trieu"));
        assertNull(MucGia.timTheoMa("tren-100-trieu"));
        assertNull(MucGia.DUOI_5_TRIEU.getGiaTu());
        assertEquals(5_000_000L, MucGia.DUOI_5_TRIEU.getGiaDuoi());
    }
}
