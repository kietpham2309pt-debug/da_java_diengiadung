package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.DonHang;

import java.util.List;

public interface DonHangRepository extends JpaRepository<DonHang, Long> {

    List<DonHang> findAllByOrderByNgayDatDesc();

    List<DonHang> findByTrangThaiOrderByNgayDatDesc(String trangThai);

    List<DonHang> findByKhachHangIdOrderByNgayDatDesc(Long khachHangId);

    long countByTrangThai(String trangThai);
}
