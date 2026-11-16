package vn.edu.huit.diengiadung.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.diengiadung.entity.MaGiamGia;

import java.util.List;
import java.util.Optional;

public interface MaGiamGiaRepository extends JpaRepository<MaGiamGia, Long> {

    Optional<MaGiamGia> findByMaIgnoreCase(String ma);

    boolean existsByMaIgnoreCase(String ma);

    boolean existsByMaIgnoreCaseAndIdNot(String ma, Long id);

    List<MaGiamGia> findAllByOrderByNgayTaoDesc();

    List<MaGiamGia> findByMaContainingIgnoreCaseOrMoTaContainingIgnoreCaseOrderByNgayTaoDesc(String ma, String moTa);

    List<MaGiamGia> findByHoatDongTrueAndHienChoKhachTrueOrderByGiaTriDesc();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select m from MaGiamGia m where upper(m.ma) = upper(:ma)")
    Optional<MaGiamGia> khoaTheoMa(@Param("ma") String ma);
}
