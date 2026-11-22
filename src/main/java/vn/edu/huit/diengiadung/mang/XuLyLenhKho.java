package vn.edu.huit.diengiadung.mang;

import org.springframework.stereotype.Service;
import vn.edu.huit.diengiadung.entity.ChiTietPhieuNhap;
import vn.edu.huit.diengiadung.entity.DonHang;
import vn.edu.huit.diengiadung.entity.NhaCungCap;
import vn.edu.huit.diengiadung.entity.PhieuNhap;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.entity.TaiKhoan;
import vn.edu.huit.diengiadung.repository.DonHangRepository;
import vn.edu.huit.diengiadung.repository.NhaCungCapRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.PhieuNhapService;
import vn.edu.huit.diengiadung.service.TaiKhoanService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class XuLyLenhKho {

    public static final String TACH = "|";
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final TaiKhoanService taiKhoanService;
    private final SanPhamRepository sanPhamRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final DonHangRepository donHangRepository;
    private final PhieuNhapService phieuNhapService;

    public XuLyLenhKho(TaiKhoanService taiKhoanService,
                       SanPhamRepository sanPhamRepository,
                       NhaCungCapRepository nhaCungCapRepository,
                       DonHangRepository donHangRepository,
                       PhieuNhapService phieuNhapService) {
        this.taiKhoanService = taiKhoanService;
        this.sanPhamRepository = sanPhamRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.donHangRepository = donHangRepository;
        this.phieuNhapService = phieuNhapService;
    }

    public List<String> xuLy(PhienKho phien, String dong) {
        if (dong == null || dong.isBlank()) {
            return loi("Lệnh rỗng");
        }
        String[] phan = dong.trim().split("\\|", -1);
        String lenh = phan[0].trim().toUpperCase();
        try {
            return switch (lenh) {
                case "XINCHAO" -> ok(List.of("Máy chủ kho Bep Nè" + TACH + "1.0"));
                case "DANGNHAP" -> dangNhap(phien, phan);
                case "TIM" -> tim(phan);
                case "TONKHO" -> tonKho(phan);
                case "NCC" -> nhaCungCap();
                case "NHAPKHO" -> nhapKho(phien, phan);
                case "DONCHO" -> donCho(phien);
                case "THOAT" -> {
                    phien.ketThuc();
                    yield ok(List.of("Tạm biệt"));
                }
                default -> loi("Không hiểu lệnh " + lenh);
            };
        } catch (LoiNghiepVu e) {
            return loi(e.getMessage());
        } catch (NumberFormatException e) {
            return loi("Số lượng, giá hoặc mã nhà cung cấp phải là số");
        }
    }

    private List<String> dangNhap(PhienKho phien, String[] phan) {
        canDu(phan, 3, "DANGNHAP|tên đăng nhập|mật khẩu");
        TaiKhoan taiKhoan = taiKhoanService.dangNhap(phan[1].trim(), phan[2]);
        if (!"ADMIN".equals(taiKhoan.getVaiTro())) {
            throw new LoiNghiepVu("Chỉ tài khoản quản trị mới được dùng máy kho");
        }
        phien.setNguoiDung(taiKhoan.getTenDangNhap());
        return ok(List.of(taiKhoan.getHoTen() + TACH + taiKhoan.getVaiTro()));
    }

    private List<String> tim(String[] phan) {
        canDu(phan, 2, "TIM|từ khoá");
        String tuKhoa = phan[1].trim();
        List<String> dong = new ArrayList<>();
        for (SanPham sp : sanPhamRepository
                .findTop20ByTenSanPhamContainingIgnoreCaseOrMaSanPhamContainingIgnoreCaseOrderByIdAsc(tuKhoa, tuKhoa)) {
            dong.add(dongSanPham(sp));
        }
        return ok(dong);
    }

    private List<String> tonKho(String[] phan) {
        canDu(phan, 2, "TONKHO|mã sản phẩm");
        SanPham sp = sanPhamRepository.timTheoMaSanPham(phan[1].trim())
                .orElseThrow(() -> new LoiNghiepVu("Không có sản phẩm mã " + phan[1].trim()));
        return ok(List.of(dongSanPham(sp)));
    }

    private List<String> nhaCungCap() {
        List<String> dong = new ArrayList<>();
        for (NhaCungCap ncc : nhaCungCapRepository.findAll()) {
            dong.add(ncc.getId() + TACH + sach(ncc.getTenNhaCungCap()));
        }
        return ok(dong);
    }

    private List<String> nhapKho(PhienKho phien, String[] phan) {
        canDangNhap(phien);
        canDu(phan, 5, "NHAPKHO|mã sản phẩm|số lượng|giá nhập|mã nhà cung cấp");
        SanPham sp = sanPhamRepository.timTheoMaSanPham(phan[1].trim())
                .orElseThrow(() -> new LoiNghiepVu("Không có sản phẩm mã " + phan[1].trim()));
        NhaCungCap ncc = nhaCungCapRepository.findById(Long.parseLong(phan[4].trim()))
                .orElseThrow(() -> new LoiNghiepVu("Không có nhà cung cấp mã " + phan[4].trim()));
        ChiTietPhieuNhap chiTiet = new ChiTietPhieuNhap();
        chiTiet.setSanPham(sp);
        chiTiet.setSoLuong(Integer.parseInt(phan[2].trim()));
        chiTiet.setGiaNhap(Long.parseLong(phan[3].trim()));
        PhieuNhap phieu = new PhieuNhap();
        phieu.setNhaCungCap(ncc);
        phieu.setGhiChu("Nhập qua máy kho bởi " + phien.getNguoiDung());
        phieu.setDanhSachChiTiet(new ArrayList<>(List.of(chiTiet)));
        phieuNhapService.luuQuyTrinhNhapHang(phieu);
        int tonMoi = sanPhamRepository.findById(sp.getId()).map(SanPham::getSoLuongTon).orElse(0);
        return ok(List.of(phieu.getId() + TACH + sp.getMaSanPham() + TACH + tonMoi));
    }

    private List<String> donCho(PhienKho phien) {
        canDangNhap(phien);
        List<String> dong = new ArrayList<>();
        for (DonHang dh : donHangRepository.findByTrangThaiOrderByNgayDatDesc(DonHang.CHO_XAC_NHAN)) {
            dong.add(dh.getMaDon() + TACH + sach(dh.getTenNguoiNhan()) + TACH + dh.getTongTien() + TACH + dh.getNgayDat().format(NGAY));
        }
        return ok(dong);
    }

    private String dongSanPham(SanPham sp) {
        return sp.getMaSanPham() + TACH + sach(sp.getTenSanPham()) + TACH + sp.getSoLuongTon() + TACH + sp.getGiaHienTai();
    }

    private void canDangNhap(PhienKho phien) {
        if (phien.getNguoiDung() == null) {
            throw new LoiNghiepVu("Cần đăng nhập bằng tài khoản quản trị trước (DANGNHAP|tên|mật khẩu)");
        }
    }

    private void canDu(String[] phan, int soPhan, String cuPhap) {
        if (phan.length < soPhan) {
            throw new LoiNghiepVu("Sai cú pháp, đúng là " + cuPhap);
        }
    }

    private static String sach(String s) {
        return s == null ? "" : s.replace(TACH, "/").replace("\n", " ").replace("\r", " ");
    }

    static List<String> ok(List<String> dong) {
        List<String> ra = new ArrayList<>();
        ra.add("OK" + TACH + dong.size());
        ra.addAll(dong);
        return ra;
    }

    static List<String> loi(String thongBao) {
        return List.of("LOI" + TACH + sach(thongBao));
    }
}
