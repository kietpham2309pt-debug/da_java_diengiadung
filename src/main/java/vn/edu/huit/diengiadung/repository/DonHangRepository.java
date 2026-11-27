package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.DonHang;

import java.time.LocalDateTime;
import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Long> {

    List<DonHang> findAllByOrderByNgayDatDesc();

    List<DonHang> findByTrangThaiOrderByNgayDatDesc(String trangThai);

    java.util.Optional<DonHang> findByMaVanDon(String maVanDon);

    List<DonHang> findByKhachHangIdOrderByNgayDatDesc(Long khachHangId);

    long countByTrangThai(String trangThai);

    List<DonHang> findByTrangThaiAndPhuongThucThanhToanAndDaThanhToanFalseAndNgayDatBefore(String trangThai, String phuongThuc, LocalDateTime moc);

    long countByKhachHangIdAndMaGiamGiaIgnoreCaseAndTrangThaiNot(Long khachHangId, String maGiamGia, String trangThai);

    long countByMaGiamGiaIgnoreCase(String maGiamGia);
}
