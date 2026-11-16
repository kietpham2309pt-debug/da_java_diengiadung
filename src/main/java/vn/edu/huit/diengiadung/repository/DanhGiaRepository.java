package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.diengiadung.entity.DanhGia;

import java.util.List;
import java.util.Optional;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {

    List<DanhGia> findBySanPhamIdAndHienTrueOrderByNgayTaoDesc(Long sanPhamId);

    Optional<DanhGia> findBySanPhamIdAndKhachHangId(Long sanPhamId, Long khachHangId);

    List<DanhGia> findAllByOrderByNgayTaoDesc();

    @Query("select d.soSao, count(d) from DanhGia d where d.sanPham.id = :sanPhamId and d.hien = true group by d.soSao")
    List<Object[]> demTheoSao(@Param("sanPhamId") Long sanPhamId);

    @Query("select d.sanPham.id, avg(d.soSao), count(d) from DanhGia d where d.hien = true group by d.sanPham.id")
    List<Object[]> tongHopTheoSanPham();
}
