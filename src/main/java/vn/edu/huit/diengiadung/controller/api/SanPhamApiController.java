package vn.edu.huit.diengiadung.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;
import vn.edu.huit.diengiadung.service.BoLocSanPham;
import vn.edu.huit.diengiadung.service.KetQuaPhanTrang;
import vn.edu.huit.diengiadung.service.LoaiSanPhamService;
import vn.edu.huit.diengiadung.service.SanPhamService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SanPhamApiController {

    private final SanPhamService dichVuSanPham;
    private final LoaiSanPhamService dichVuLoaiSanPham;
    private final ThuongHieuRepository duLieuThuongHieu;

    public SanPhamApiController(SanPhamService dichVuSanPham,
                                LoaiSanPhamService dichVuLoaiSanPham,
                                ThuongHieuRepository duLieuThuongHieu) {
        this.dichVuSanPham = dichVuSanPham;
        this.dichVuLoaiSanPham = dichVuLoaiSanPham;
        this.duLieuThuongHieu = duLieuThuongHieu;
    }

    @GetMapping("/loai-san-pham")
    public List<DanhMucDto> danhSachLoai() {
        return dichVuLoaiSanPham.layTatCa().stream()
                .map(loai -> new DanhMucDto(loai.getId(), loai.getMaLoai(), loai.getTenLoai()))
                .toList();
    }

    @GetMapping("/thuong-hieu")
    public List<ThuongHieuDto> danhSachThuongHieu() {
        return duLieuThuongHieu.findAllByOrderByTenThuongHieuAsc().stream()
                .map(hang -> new ThuongHieuDto(hang.getId(), hang.getTenThuongHieu(), hang.getXuatXu()))
                .toList();
    }

    @GetMapping("/san-pham")
    public TrangSanPhamDto danhSachSanPham(@ModelAttribute BoLocSanPham boLoc) {
        KetQuaPhanTrang<SanPham> ketQua = dichVuSanPham.timKiemPhiaKhach(boLoc);
        return new TrangSanPhamDto(ketQua.getTrangHienTai(), ketQua.getSoTrang(), ketQua.getTongSo(),
                ketQua.getDanhSach().stream().map(SanPhamTomTatDto::tu).toList());
    }

    @GetMapping("/san-pham/{id}")
    public SanPhamChiTietDto chiTietSanPham(@PathVariable Long id) {
        return SanPhamChiTietDto.tu(dichVuSanPham.laySanPhamDangBan(id));
    }

    public record DanhMucDto(Long id, String ma, String ten) {
    }

    public record ThuongHieuDto(Long id, String ten, String xuatXu) {
    }

    public record TrangSanPhamDto(int trang, int soTrang, long tongSo, List<SanPhamTomTatDto> danhSach) {
    }

    public record SanPhamTomTatDto(Long id, String maSanPham, String tenSanPham, String thuongHieu, String loai,
                                   Long giaBan, Long giaHienTai, int phanTramGiam, boolean conHang, String anh) {

        static SanPhamTomTatDto tu(SanPham sp) {
            return new SanPhamTomTatDto(sp.getId(), sp.getMaSanPham(), sp.getTenSanPham(),
                    sp.getThuongHieu() != null ? sp.getThuongHieu().getTenThuongHieu() : null,
                    sp.getLoaiSanPham() != null ? sp.getLoaiSanPham().getTenLoai() : null,
                    sp.getGiaBan(), sp.getGiaHienTai(), sp.getPhanTramGiam(), sp.isConHang(), sp.getDuongDanAnh());
        }
    }

    public record SanPhamChiTietDto(SanPhamTomTatDto tomTat, int soLuongTon, int baoHanhThang, String congSuat,
                                    List<String> moTa, List<ThongSoDto> thongSo, List<String> danhSachAnh) {

        static SanPhamChiTietDto tu(SanPham sp) {
            return new SanPhamChiTietDto(SanPhamTomTatDto.tu(sp), sp.getSoLuongTon(), sp.getBaoHanhThang(),
                    sp.getCongSuat(), sp.getDanhSachDoanMoTa(),
                    sp.getDanhSachThongSo().stream().map(ts -> new ThongSoDto(ts.getTen(), ts.getGiaTri())).toList(),
                    sp.getDanhSachAnh());
        }
    }

    public record ThongSoDto(String ten, String giaTri) {
    }
}
