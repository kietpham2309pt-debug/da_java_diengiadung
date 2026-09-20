package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.PhieuNhap;

import java.util.List;

public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Long> {

    List<PhieuNhap> findAllByOrderByNgayNhapDesc();

    long countByNhaCungCapId(Long nhaCungCapId);
}
