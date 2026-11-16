package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.KhuyenMai;

import java.util.List;

public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Long> {

    List<KhuyenMai> findAllByOrderByBatDauDesc();

    List<KhuyenMai> findByTenContainingIgnoreCaseOrderByBatDauDesc(String ten);

    List<KhuyenMai> findByHoatDongTrue();

    boolean existsByLoaiSanPhamId(Long loaiSanPhamId);

    boolean existsByThuongHieuId(Long thuongHieuId);
}
