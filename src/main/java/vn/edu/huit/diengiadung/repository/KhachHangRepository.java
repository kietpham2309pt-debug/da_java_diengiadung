package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.KhachHang;
import vn.edu.huit.diengiadung.entity.TaiKhoan;

import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {

    Optional<KhachHang> findByTaiKhoan(TaiKhoan taiKhoan);
}
