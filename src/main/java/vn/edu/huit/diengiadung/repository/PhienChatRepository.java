package vn.edu.huit.diengiadung.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.huit.diengiadung.entity.PhienChat;

import java.util.List;
import java.util.Optional;

public interface PhienChatRepository extends JpaRepository<PhienChat, Long> {

    Optional<PhienChat> findByMaKhach(String maKhach);

    Optional<PhienChat> findFirstByKhachHangIdOrderByIdDesc(Long khachHangId);

    List<PhienChat> findByKhachHangIsNotNull();

    @Query("select p from PhienChat p where size(p.danhSachTinNhan) > 1 or p.trangThai <> 'BOT' order by p.capNhatLuc desc")
    List<PhienChat> layDanhSachQuanTri();

    @Query("select coalesce(sum(p.soChuaDocQuanTri), 0) from PhienChat p")
    long tongChuaDocQuanTri();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from PhienChat p where p.id = :id")
    Optional<PhienChat> khoaTheoId(@Param("id") Long id);
}
