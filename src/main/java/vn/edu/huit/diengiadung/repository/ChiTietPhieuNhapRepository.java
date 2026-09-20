package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.ChiTietPhieuNhap;

import java.util.List;

public interface ChiTietPhieuNhapRepository extends JpaRepository<ChiTietPhieuNhap, Long> {

    List<ChiTietPhieuNhap> findByPhieuNhapId(Long phieuNhapId);
}
