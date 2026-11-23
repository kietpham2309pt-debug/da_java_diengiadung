package vn.edu.huit.diengiadung.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import vn.edu.huit.diengiadung.entity.SanPham;

import java.util.Optional;

public class SanPhamRepositoryKhoaImpl implements SanPhamRepositoryKhoa {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<SanPham> khoaTheoId(Long id) {
        SanPham sanPham = entityManager.find(SanPham.class, id);
        if (sanPham == null) {
            return Optional.empty();
        }
        entityManager.flush();
        entityManager.refresh(sanPham, LockModeType.PESSIMISTIC_WRITE);
        return Optional.of(sanPham);
    }
}
