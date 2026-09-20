package vn.edu.huit.diengiadung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.huit.diengiadung.entity.NhaCungCap;

import java.util.List;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Long> {

    List<NhaCungCap> findAllByOrderByTenNhaCungCapAsc();
}
