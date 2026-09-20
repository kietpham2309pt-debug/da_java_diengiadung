package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.ChiTietDonHang;

import java.util.List;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {

    List<ChiTietDonHang> findByDonHangId(Long donHangId);
}
