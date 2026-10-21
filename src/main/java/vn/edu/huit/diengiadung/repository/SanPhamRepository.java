package vn.edu.huit.diengiadung.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.diengiadung.entity.SanPham;

import java.util.List;
import java.util.Optional;

public interface SanPhamRepository extends JpaRepository<SanPham, Long>, JpaSpecificationExecutor<SanPham> {

    List<SanPham> findByDangBanTrueOrderByIdDesc();

    List<SanPham> findTop8ByDangBanTrueOrderByIdDesc();

    List<SanPham> findByLoaiSanPhamIdAndDangBanTrue(Long loaiSanPhamId);

    List<SanPham> findByThuongHieuIdAndDangBanTrue(Long thuongHieuId);

    List<SanPham> findByTenSanPhamContainingIgnoreCaseAndDangBanTrue(String tuKhoa);

    List<SanPham> findBySoLuongTonLessThanEqual(int nguong);

    long countByLoaiSanPhamId(Long loaiSanPhamId);

    long countByThuongHieuId(Long thuongHieuId);

    @Query("select sp from SanPham sp where sp.dangBan = true order by sp.id desc")
    List<SanPham> laySanPhamMoiNhat(Pageable gioiHan);

    @Query("""
            select sp from SanPham sp
            where sp.dangBan = true
              and sp.giaKhuyenMai > 0 and sp.giaKhuyenMai < sp.giaBan
            order by (sp.giaBan - sp.giaKhuyenMai) * 1.0 / sp.giaBan desc, sp.id desc
            """)
    List<SanPham> laySanPhamGiamNhieuNhat(Pageable gioiHan);

    @Query("""
            select sp from SanPham sp
            where sp.dangBan = true
              and sp.loaiSanPham.id = :idLoai
              and sp.id <> :idBoQua
            order by sp.id desc
            """)
    List<SanPham> laySanPhamCungLoai(@Param("idLoai") Long idLoai,
                                     @Param("idBoQua") Long idBoQua,
                                     Pageable gioiHan);

    @Query("select sp from SanPham sp where lower(sp.maSanPham) = lower(:maSanPham)")
    Optional<SanPham> timTheoMaSanPham(@Param("maSanPham") String maSanPham);

    @Query("select count(ct) from ChiTietDonHang ct where ct.sanPham.id = :idSanPham")
    long demSoDongDonHang(@Param("idSanPham") Long idSanPham);

    @Query("select count(ct) from ChiTietPhieuNhap ct where ct.sanPham.id = :idSanPham")
    long demSoDongPhieuNhap(@Param("idSanPham") Long idSanPham);
}
