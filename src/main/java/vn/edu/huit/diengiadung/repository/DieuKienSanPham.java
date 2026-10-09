package vn.edu.huit.diengiadung.repository;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.ThuongHieu;
import vn.edu.huit.diengiadung.service.MucGia;

import java.util.ArrayList;
import java.util.List;

/**
 * Các điều kiện lọc sản phẩm, ghép lại bằng and() để ra câu truy vấn động.
 * Điều kiện nào không được chọn thì trả về "luôn đúng" để không ảnh hưởng kết quả.
 */
public final class DieuKienSanPham {

    private static final char KY_TU_THOAT = '!';

    private DieuKienSanPham() {
    }

    public static Specification<SanPham> dangBan() {
        return (goc, truyVan, boDung) -> boDung.isTrue(goc.get("dangBan"));
    }

    public static Specification<SanPham> theoTrangThaiBan(Boolean dangBan) {
        return (goc, truyVan, boDung) -> dangBan == null
                ? boDung.conjunction()
                : boDung.equal(goc.get("dangBan"), dangBan);
    }

    public static Specification<SanPham> thuocLoai(Long idLoai) {
        return (goc, truyVan, boDung) -> idLoai == null
                ? boDung.conjunction()
                : boDung.equal(goc.get("loaiSanPham").get("id"), idLoai);
    }

    public static Specification<SanPham> thuocCacHang(List<Long> danhSachIdHang) {
        return (goc, truyVan, boDung) -> danhSachIdHang == null || danhSachIdHang.isEmpty()
                ? boDung.conjunction()
                : goc.get("thuongHieu").get("id").in(danhSachIdHang);
    }

    /** Mỗi từ trong từ khoá phải xuất hiện ở tên, mã sản phẩm hoặc tên thương hiệu. */
    public static Specification<SanPham> chuaTuKhoa(String tuKhoa) {
        return (goc, truyVan, boDung) -> {
            if (tuKhoa == null || tuKhoa.isBlank()) {
                return boDung.conjunction();
            }
            Join<SanPham, ThuongHieu> thuongHieu = goc.join("thuongHieu", JoinType.LEFT);
            Expression<String> ten = boDung.lower(goc.get("tenSanPham"));
            Expression<String> ma = boDung.lower(goc.get("maSanPham"));
            Expression<String> tenHang = boDung.lower(thuongHieu.get("tenThuongHieu"));

            List<Predicate> dieuKienTungTu = new ArrayList<>();
            for (String tu : tuKhoa.trim().toLowerCase().split("\\s+")) {
                String mau = "%" + boKyTuDacBiet(tu) + "%";
                dieuKienTungTu.add(boDung.or(
                        boDung.like(ten, mau, KY_TU_THOAT),
                        boDung.like(ma, mau, KY_TU_THOAT),
                        boDung.like(tenHang, mau, KY_TU_THOAT)));
            }
            return boDung.and(dieuKienTungTu.toArray(new Predicate[0]));
        };
    }

    /** Chọn nhiều mức giá thì lấy sản phẩm thuộc bất kỳ mức nào trong số đó. */
    public static Specification<SanPham> trongCacMucGia(List<MucGia> danhSachMuc) {
        return (goc, truyVan, boDung) -> {
            if (danhSachMuc == null || danhSachMuc.isEmpty()) {
                return boDung.conjunction();
            }
            Expression<Long> gia = goc.get("giaBanThucTe");
            List<Predicate> dieuKienTungMuc = new ArrayList<>();
            for (MucGia muc : danhSachMuc) {
                List<Predicate> canGia = new ArrayList<>();
                if (muc.getGiaTu() != null) {
                    canGia.add(boDung.greaterThanOrEqualTo(gia, muc.getGiaTu()));
                }
                if (muc.getGiaDuoi() != null) {
                    canGia.add(boDung.lessThan(gia, muc.getGiaDuoi()));
                }
                dieuKienTungMuc.add(boDung.and(canGia.toArray(new Predicate[0])));
            }
            return boDung.or(dieuKienTungMuc.toArray(new Predicate[0]));
        };
    }

    // Khách gõ % hoặc _ thì tìm đúng ký tự đó, không để LIKE hiểu thành ký tự đại diện.
    // Không dùng dấu \ làm ký tự thoát vì MySQL coi \ là ký tự thoát trong chuỗi.
    private static String boKyTuDacBiet(String tu) {
        return tu.replace("!", "!!").replace("%", "!%").replace("_", "!_");
    }
}
