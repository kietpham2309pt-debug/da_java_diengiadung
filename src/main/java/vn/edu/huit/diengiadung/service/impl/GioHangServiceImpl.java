package vn.edu.huit.diengiadung.service.impl;

import org.springframework.stereotype.Service;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.model.ChiTietGioHang;
import vn.edu.huit.diengiadung.model.DongGioHang;
import vn.edu.huit.diengiadung.model.GioHang;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.GioHangService;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GioHangServiceImpl implements GioHangService {

    private final SanPhamRepository duLieuSanPham;

    public GioHangServiceImpl(SanPhamRepository duLieuSanPham) {
        this.duLieuSanPham = duLieuSanPham;
    }

    @Override
    public void them(GioHang gioHang, Long idSanPham, int soLuong) {
        kiemTraSoLuong(soLuong);
        SanPham sanPham = laySanPhamDangBan(idSanPham);
        int tongSauKhiThem = gioHang.laySoLuong(idSanPham) + soLuong;
        kiemTraTonKho(sanPham, tongSauKhiThem);
        gioHang.datSoLuong(idSanPham, tongSauKhiThem);
    }

    @Override
    public void capNhat(GioHang gioHang, Long idSanPham, int soLuong) {
        if (soLuong <= 0) {
            gioHang.xoa(idSanPham);
            return;
        }
        SanPham sanPham = laySanPhamDangBan(idSanPham);
        kiemTraTonKho(sanPham, soLuong);
        gioHang.datSoLuong(idSanPham, soLuong);
    }

    @Override
    public void xoa(GioHang gioHang, Long idSanPham) {
        gioHang.xoa(idSanPham);
    }

    @Override
    public ChiTietGioHang xemChiTiet(GioHang gioHang) {
        List<DongGioHang> danhSachDong = new ArrayList<>();
        List<String> canhBao = new ArrayList<>();
        for (Map.Entry<Long, Integer> muc : new ArrayList<>(gioHang.getSoLuongTheoSanPham().entrySet())) {
            Long idSanPham = muc.getKey();
            int soLuong = muc.getValue();
            SanPham sanPham = duLieuSanPham.findById(idSanPham).filter(SanPham::isDangBan).orElse(null);
            if (sanPham == null) {
                gioHang.xoa(idSanPham);
                canhBao.add("Một sản phẩm trong giỏ đã ngừng kinh doanh nên được bỏ khỏi giỏ");
                continue;
            }
            if (sanPham.getSoLuongTon() <= 0) {
                gioHang.xoa(idSanPham);
                canhBao.add(sanPham.getTenSanPham() + " đã hết hàng nên được bỏ khỏi giỏ");
                continue;
            }
            if (soLuong > sanPham.getSoLuongTon()) {
                soLuong = sanPham.getSoLuongTon();
                gioHang.datSoLuong(idSanPham, soLuong);
                canhBao.add(sanPham.getTenSanPham() + " chỉ còn " + soLuong + " sản phẩm nên số lượng đã được điều chỉnh");
            }
            danhSachDong.add(new DongGioHang(sanPham, soLuong));
        }
        return new ChiTietGioHang(danhSachDong, canhBao);
    }

    private SanPham laySanPhamDangBan(Long idSanPham) {
        return duLieuSanPham.findById(idSanPham)
                .filter(SanPham::isDangBan)
                .orElseThrow(() -> new LoiNghiepVu("Sản phẩm không tồn tại hoặc đã ngừng kinh doanh"));
    }

    private void kiemTraSoLuong(int soLuong) {
        if (soLuong < 1) {
            throw new LoiNghiepVu("Số lượng phải là số nguyên từ 1 trở lên");
        }
    }

    private void kiemTraTonKho(SanPham sanPham, int soLuongCan) {
        if (sanPham.getSoLuongTon() <= 0) {
            throw new LoiNghiepVu(sanPham.getTenSanPham() + " đã hết hàng");
        }
        if (soLuongCan > sanPham.getSoLuongTon()) {
            throw new LoiNghiepVu(sanPham.getTenSanPham() + " chỉ còn " + sanPham.getSoLuongTon() + " sản phẩm");
        }
    }
}
