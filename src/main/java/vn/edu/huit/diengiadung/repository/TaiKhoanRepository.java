package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.TaiKhoan;

import java.util.List;
import java.util.Optional;

public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {

    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);

    boolean existsByTenDangNhap(String tenDangNhap);

    List<TaiKhoan> findByVaiTro(String vaiTro);

    List<TaiKhoan> findByHoTenContainingIgnoreCaseOrTenDangNhapContainingIgnoreCase(String hoTen, String tenDangNhap);
}
