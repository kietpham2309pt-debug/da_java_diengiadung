package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.GiaoDichSePay;

import java.util.List;

public interface GiaoDichSePayRepository extends JpaRepository<GiaoDichSePay, Long> {

    boolean existsByMaGiaoDich(Long maGiaoDich);

    List<GiaoDichSePay> findByDonHangIdOrderByNhanLucDesc(Long donHangId);

    List<GiaoDichSePay> findTop100ByOrderByNhanLucDesc();
}
