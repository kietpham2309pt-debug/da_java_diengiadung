package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.entity.TrangThaiGhn;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.tichhop.KhachHangGhn;
import vn.edu.huit.diengiadung.tichhop.VanChuyenGhn;

import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin/don-hang/{id}/ghn")
public class AdminVanChuyenController {

    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final VanChuyenGhn vanChuyenGhn;

    public AdminVanChuyenController(VanChuyenGhn vanChuyenGhn) {
        this.vanChuyenGhn = vanChuyenGhn;
    }

    @PostMapping("/xem-phi")
    public String xemPhi(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            KhachHangGhn.KetQuaVanDon kq = vanChuyenGhn.xemPhi(id);
            thongBao.addFlashAttribute("thongBao", "GHN báo phí giao " + tien(kq.phi())
                    + (kq.ngayGiaoDuKien() == null ? "" : ", dự kiến giao ngày " + kq.ngayGiaoDuKien().format(NGAY))
                    + ". Chưa tạo vận đơn.");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return quayLai(id);
    }

    @PostMapping("/tao")
    public String taoVanDon(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            KhachHangGhn.KetQuaVanDon kq = vanChuyenGhn.taoVanDon(id);
            thongBao.addFlashAttribute("thongBao", "Đã tạo vận đơn GHN " + kq.maVanDon() + ", phí " + tien(kq.phi()) + ".");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return quayLai(id);
    }

    @PostMapping("/huy")
    public String huyVanDon(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            vanChuyenGhn.huyVanDon(id);
            thongBao.addFlashAttribute("thongBao", "Đã huỷ vận đơn GHN.");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return quayLai(id);
    }

    @PostMapping("/dong-bo")
    public String dongBo(@PathVariable Long id, RedirectAttributes thongBao) {
        try {
            String trangThai = vanChuyenGhn.dongBo(id);
            thongBao.addFlashAttribute("thongBao", "Trạng thái GHN hiện tại: " + TrangThaiGhn.tenTiengViet(trangThai)
                    + " (" + trangThai + ").");
        } catch (LoiNghiepVu loi) {
            thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
        }
        return quayLai(id);
    }

    private static String quayLai(Long id) {
        return "redirect:/admin/don-hang/" + id;
    }

    private static String tien(long so) {
        return String.format("%,d", so).replace(',', '.') + " ₫";
    }
}
