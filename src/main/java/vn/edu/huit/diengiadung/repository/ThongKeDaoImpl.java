package vn.edu.huit.diengiadung.repository;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.service.ThongKeSanPham;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ThongKeDaoImpl implements ThongKeDao {

    private final EntityManager entityManager;

    public ThongKeDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public long tongDoanhThu(LocalDateTime tuNgay, LocalDateTime denNgay) {
        Long tong = entityManager.createQuery(
                        "select coalesce(sum(d.tongTien), 0) from DonHang d "
                                + "where d.trangThai = :trangThai and d.ngayDat >= :tuNgay and d.ngayDat < :denNgay",
                        Long.class)
                .setParameter("trangThai", DonHang.HOAN_THANH)
                .setParameter("tuNgay", tuNgay)
                .setParameter("denNgay", denNgay)
                .getSingleResult();
        return tong == null ? 0L : tong;
    }

    @Override
    public long demDonHoanThanh(LocalDateTime tuNgay, LocalDateTime denNgay) {
        return entityManager.createQuery(
                        "select count(d) from DonHang d "
                                + "where d.trangThai = :trangThai and d.ngayDat >= :tuNgay and d.ngayDat < :denNgay",
                        Long.class)
                .setParameter("trangThai", DonHang.HOAN_THANH)
                .setParameter("tuNgay", tuNgay)
                .setParameter("denNgay", denNgay)
                .getSingleResult();
    }

    @Override
    public List<ThongKeSanPham> sanPhamBanChay(LocalDateTime tuNgay, LocalDateTime denNgay, int soLuong) {
        List<Object[]> dong = entityManager.createQuery(
                        "select sp.maSanPham, sp.tenSanPham, sum(ct.soLuong), sum(ct.soLuong * coalesce(ct.donGia, 0)) "
                                + "from ChiTietDonHang ct join ct.donHang d join ct.sanPham sp "
                                + "where d.trangThai = :trangThai and d.ngayDat >= :tuNgay and d.ngayDat < :denNgay "
                                + "group by sp.id, sp.maSanPham, sp.tenSanPham "
                                + "order by sum(ct.soLuong) desc, sum(ct.soLuong * coalesce(ct.donGia, 0)) desc",
                        Object[].class)
                .setParameter("trangThai", DonHang.HOAN_THANH)
                .setParameter("tuNgay", tuNgay)
                .setParameter("denNgay", denNgay)
                .setMaxResults(soLuong)
                .getResultList();
        return dong.stream()
                .map(cot -> new ThongKeSanPham((String) cot[0], (String) cot[1],
                        ((Number) cot[2]).longValue(), ((Number) cot[3]).longValue()))
                .toList();
    }
}
