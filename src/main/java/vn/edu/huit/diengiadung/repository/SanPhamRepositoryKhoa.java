package vn.edu.huit.diengiadung.repository;

import vn.edu.huit.diengiadung.entity.SanPham;

import java.util.Optional;

public interface SanPhamRepositoryKhoa {

    Optional<SanPham> khoaTheoId(Long id);
}
