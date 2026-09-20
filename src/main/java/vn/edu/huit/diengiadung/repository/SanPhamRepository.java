package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.SanPham;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    List<SanPham> findByDangBanTrueOrderByIdDesc();

    List<SanPham> findTop8ByDangBanTrueOrderByIdDesc();

    List<SanPham> findByLoaiSanPhamIdAndDangBanTrue(Long loaiSanPhamId);

    List<SanPham> findByThuongHieuIdAndDangBanTrue(Long thuongHieuId);

    List<SanPham> findByTenSanPhamContainingIgnoreCaseAndDangBanTrue(String tuKhoa);

    List<SanPham> findBySoLuongTonLessThanEqual(int nguong);

    long countByLoaiSanPhamId(Long loaiSanPhamId);

    long countByThuongHieuId(Long thuongHieuId);
}
