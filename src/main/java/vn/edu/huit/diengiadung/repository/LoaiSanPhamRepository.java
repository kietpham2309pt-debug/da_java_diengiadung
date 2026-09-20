package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.LoaiSanPham;

import java.util.List;
import java.util.Optional;

public interface LoaiSanPhamRepository extends JpaRepository<LoaiSanPham, Long> {

    List<LoaiSanPham> findAllByOrderByTenLoaiAsc();

    Optional<LoaiSanPham> findByTenLoaiIgnoreCase(String tenLoai);

    List<LoaiSanPham> findByTenLoaiContainingIgnoreCaseOrderByTenLoaiAsc(String tuKhoa);
}
