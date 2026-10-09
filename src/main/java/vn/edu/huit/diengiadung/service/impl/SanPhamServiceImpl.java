package vn.edu.huit.diengiadung.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.DieuKienSanPham;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.BoLocSanPham;
import vn.edu.huit.diengiadung.service.KetQuaPhanTrang;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.SanPhamService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class SanPhamServiceImpl implements SanPhamService {

    public static final int SO_SAN_PHAM_MOI_TRANG = 9;
    public static final int SO_DONG_QUAN_TRI_MOI_TRANG = 10;
    private static final long DUNG_LUONG_ANH_TOI_DA = 5L * 1024 * 1024;
    private static final List<String> DUOI_ANH_HOP_LE = List.of(".jpg", ".jpeg", ".png", ".webp", ".gif");

    private final SanPhamRepository sanPhamRepository;
    private final Path thuMucAnh;

    public SanPhamServiceImpl(SanPhamRepository sanPhamRepository,
                              @Value("${duongdan.anh}") String duongDanThuMucAnh) {
        this.sanPhamRepository = sanPhamRepository;
        this.thuMucAnh = Paths.get(duongDanThuMucAnh).toAbsolutePath().normalize();
    }

    @Override
    public KetQuaPhanTrang<SanPham> timKiemPhiaKhach(BoLocSanPham boLoc) {
        Specification<SanPham> dieuKien = DieuKienSanPham.dangBan()
                .and(DieuKienSanPham.thuocLoai(boLoc.getLoai()))
                .and(DieuKienSanPham.thuocCacHang(boLoc.getHang()))
                .and(DieuKienSanPham.chuaTuKhoa(boLoc.getTuKhoaDaGon()))
                .and(DieuKienSanPham.trongCacMucGia(boLoc.getDanhSachMucGia()));
        return phanTrang(dieuKien, boLoc.getTrangCanXem(), SO_SAN_PHAM_MOI_TRANG,
                boLoc.getKieuSapXep().taoCachSapXep());
    }

    @Override
    public List<SanPham> laySanPhamMoi(int soLuong) {
        return sanPhamRepository.laySanPhamMoiNhat(PageRequest.of(0, soLuong));
    }

    @Override
    public List<SanPham> laySanPhamGiamGia(int soLuong) {
        return sanPhamRepository.laySanPhamGiamNhieuNhat(PageRequest.of(0, soLuong));
    }

    @Override
    public SanPham laySanPhamDangBan(Long id) {
        return sanPhamRepository.findById(id)
                .filter(SanPham::isDangBan)
                .orElseThrow(() -> new LoiNghiepVu("Sản phẩm không tồn tại hoặc đã ngừng kinh doanh"));
    }

    @Override
    public List<SanPham> laySanPhamCungLoai(SanPham sanPham, int soLuong) {
        if (sanPham.getLoaiSanPham() == null) {
            return new ArrayList<>();
        }
        return sanPhamRepository.laySanPhamCungLoai(sanPham.getLoaiSanPham().getId(), sanPham.getId(),
                PageRequest.of(0, soLuong));
    }

    @Override
    public KetQuaPhanTrang<SanPham> timKiemQuanTri(String tuKhoa, Long loai, Long hang, String trangThai, int trang) {
        Boolean dangBan = null;
        if ("dang-ban".equals(trangThai)) {
            dangBan = Boolean.TRUE;
        } else if ("ngung-ban".equals(trangThai)) {
            dangBan = Boolean.FALSE;
        }
        List<Long> danhSachHang = hang == null ? List.of() : List.of(hang);

        Specification<SanPham> dieuKien = DieuKienSanPham.theoTrangThaiBan(dangBan)
                .and(DieuKienSanPham.thuocLoai(loai))
                .and(DieuKienSanPham.thuocCacHang(danhSachHang))
                .and(DieuKienSanPham.chuaTuKhoa(tuKhoa));
        return phanTrang(dieuKien, Math.max(1, trang), SO_DONG_QUAN_TRI_MOI_TRANG,
                Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public SanPham layTheoId(Long id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new LoiNghiepVu("Không tìm thấy sản phẩm có mã " + id));
    }

    @Override
    @Transactional
    public SanPham luu(SanPham sanPham, MultipartFile tepAnh, boolean xoaAnhCu) {
        chuanHoa(sanPham);
        kiemTraGia(sanPham);
        kiemTraTrungMa(sanPham);

        boolean coTepMoi = tepAnh != null && !tepAnh.isEmpty();
        if (coTepMoi) {
            kiemTraTepAnh(tepAnh);
        }

        String anhCu = null;
        if (sanPham.getId() != null) {
            anhCu = layTheoId(sanPham.getId()).getHinhAnh();
        }

        String anhMoi = anhCu;
        if (coTepMoi) {
            anhMoi = luuTepAnh(tepAnh);
        } else if (xoaAnhCu) {
            anhMoi = null;
        }
        sanPham.setHinhAnh(anhMoi);

        SanPham daLuu;
        try {
            daLuu = sanPhamRepository.saveAndFlush(sanPham);
        } catch (RuntimeException loi) {
            if (coTepMoi) {
                xoaTepAnh(anhMoi);
            }
            throw loi;
        }

        if (anhCu != null && !Objects.equals(anhCu, anhMoi)) {
            xoaTepAnh(anhCu);
        }
        return daLuu;
    }

    @Override
    @Transactional
    public void xoa(Long id) {
        SanPham sanPham = layTheoId(id);

        long soDongDonHang = sanPhamRepository.demSoDongDonHang(id);
        if (soDongDonHang > 0) {
            throw new LoiNghiepVu("Không xoá được vì sản phẩm đã nằm trong " + soDongDonHang
                    + " dòng đơn hàng. Hãy chuyển sang ngừng bán để giữ lịch sử đơn");
        }
        long soDongPhieuNhap = sanPhamRepository.demSoDongPhieuNhap(id);
        if (soDongPhieuNhap > 0) {
            throw new LoiNghiepVu("Không xoá được vì sản phẩm đã nằm trong " + soDongPhieuNhap
                    + " dòng phiếu nhập. Hãy chuyển sang ngừng bán để giữ lịch sử nhập kho");
        }

        String anhCanXoa = sanPham.getHinhAnh();
        sanPhamRepository.delete(sanPham);
        sanPhamRepository.flush();
        xoaTepAnh(anhCanXoa);
    }

    @Override
    @Transactional
    public void doiTrangThaiBan(Long id) {
        SanPham sanPham = layTheoId(id);
        sanPham.setDangBan(!sanPham.isDangBan());
        sanPhamRepository.save(sanPham);
    }

    private KetQuaPhanTrang<SanPham> phanTrang(Specification<SanPham> dieuKien, int trang, int kichThuocTrang,
                                               Sort cachSapXep) {
        Page<SanPham> ketQua = sanPhamRepository.findAll(dieuKien, PageRequest.of(trang - 1, kichThuocTrang, cachSapXep));
        if (ketQua.isEmpty() && trang > ketQua.getTotalPages() && ketQua.getTotalPages() > 0) {
            int trangCuoi = ketQua.getTotalPages() - 1;
            ketQua = sanPhamRepository.findAll(dieuKien, PageRequest.of(trangCuoi, kichThuocTrang, cachSapXep));
        }
        return new KetQuaPhanTrang<>(ketQua);
    }

    private void chuanHoa(SanPham sanPham) {
        sanPham.setMaSanPham(sanPham.getMaSanPham().trim().toUpperCase(Locale.ROOT));
        sanPham.setTenSanPham(sanPham.getTenSanPham().trim());
        sanPham.setCongSuat(gonHoacNull(sanPham.getCongSuat()));
        sanPham.setMoTa(gonHoacNull(sanPham.getMoTa()));
        sanPham.setThongSoKyThuat(gonHoacNull(sanPham.getThongSoKyThuat()));
        if (sanPham.getGiaKhuyenMai() != null && sanPham.getGiaKhuyenMai() == 0) {
            sanPham.setGiaKhuyenMai(null);
        }
    }

    private void kiemTraGia(SanPham sanPham) {
        Long giaKhuyenMai = sanPham.getGiaKhuyenMai();
        if (giaKhuyenMai != null && giaKhuyenMai >= sanPham.getGiaBan()) {
            throw new LoiNghiepVu("Giá khuyến mãi phải nhỏ hơn giá bán. Bỏ trống ô này nếu sản phẩm không giảm giá");
        }
    }

    private void kiemTraTrungMa(SanPham sanPham) {
        Optional<SanPham> trungMa = sanPhamRepository.timTheoMaSanPham(sanPham.getMaSanPham());
        if (trungMa.isPresent() && !trungMa.get().getId().equals(sanPham.getId())) {
            throw new LoiNghiepVu("Mã sản phẩm \"" + sanPham.getMaSanPham() + "\" đã được dùng cho sản phẩm khác");
        }
    }

    private void kiemTraTepAnh(MultipartFile tepAnh) {
        String duoiTep = layDuoiTep(tepAnh.getOriginalFilename());
        String kieuNoiDung = tepAnh.getContentType();
        if (!DUOI_ANH_HOP_LE.contains(duoiTep) || kieuNoiDung == null || !kieuNoiDung.startsWith("image/")) {
            throw new LoiNghiepVu("Ảnh sản phẩm phải là tệp JPG, PNG, WEBP hoặc GIF");
        }
        if (tepAnh.getSize() > DUNG_LUONG_ANH_TOI_DA) {
            throw new LoiNghiepVu("Ảnh sản phẩm tối đa 5 MB, hãy chọn ảnh nhỏ hơn");
        }
    }

    private String luuTepAnh(MultipartFile tepAnh) {
        String tenTep = "sp-" + UUID.randomUUID().toString().replace("-", "").substring(0, 12)
                + layDuoiTep(tepAnh.getOriginalFilename());
        try (InputStream luongDoc = tepAnh.getInputStream()) {
            Files.createDirectories(thuMucAnh);
            Files.copy(luongDoc, thuMucAnh.resolve(tenTep));
        } catch (IOException loi) {
            throw new LoiNghiepVu("Không lưu được ảnh lên máy chủ, vui lòng thử lại");
        }
        return tenTep;
    }

    /** Chỉ xoá ảnh do quản trị viên upload, không đụng tới ảnh có sẵn trong static/img. */
    private void xoaTepAnh(String tenTep) {
        if (tenTep == null || tenTep.isBlank() || tenTep.startsWith("/")) {
            return;
        }
        Path tep = thuMucAnh.resolve(tenTep).normalize();
        if (!tep.startsWith(thuMucAnh)) {
            return;
        }
        try {
            Files.deleteIfExists(tep);
        } catch (IOException loi) {
            // Ảnh cũ còn sót lại trên đĩa không ảnh hưởng dữ liệu, bỏ qua.
        }
    }

    private static String layDuoiTep(String tenTepGoc) {
        if (tenTepGoc == null || tenTepGoc.lastIndexOf('.') < 0) {
            return "";
        }
        return tenTepGoc.substring(tenTepGoc.lastIndexOf('.')).toLowerCase(Locale.ROOT);
    }

    private static String gonHoacNull(String chuoi) {
        if (chuoi == null || chuoi.isBlank()) {
            return null;
        }
        return chuoi.trim();
    }
}
