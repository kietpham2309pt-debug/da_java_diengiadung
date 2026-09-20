package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.ThuongHieu;

import java.util.List;

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {

    List<ThuongHieu> findAllByOrderByTenThuongHieuAsc();
}
