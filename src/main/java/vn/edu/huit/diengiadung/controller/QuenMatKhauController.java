package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.TaiKhoanService;

import java.time.Instant;

@Controller
@RequestMapping("/quen-mat-khau")
public class QuenMatKhauController {

    private static final String PHIEN_DAT_LAI = "datLaiMatKhau";
    private static final String PHIEN_DEM_SAI = "demSaiQuenMatKhau";
    private static final int SO_LAN_TOI_DA = 5;
    private static final long HIEU_LUC_GIAY = 600;

    private final TaiKhoanService taiKhoanService;

    public QuenMatKhauController(TaiKhoanService taiKhoanService) {
        this.taiKhoanService = taiKhoanService;
    }

    private record PhieuDatLai(Long idTaiKhoan, Instant hetHan) implements java.io.Serializable {
    }

    @GetMapping
    public String formXacMinh() {
        return "quen-mat-khau";
    }

    @PostMapping
    public String xacMinh(@RequestParam(required = false) String tenDangNhap,
                          @RequestParam(required = false) String lienHe,
                          HttpSession phien, Model model) {
        int daSai = phien.getAttribute(PHIEN_DEM_SAI) instanceof Integer so ? so : 0;
        model.addAttribute("tenDangNhapCu", tenDangNhap);
        model.addAttribute("lienHeCu", lienHe);
        if (daSai >= SO_LAN_TOI_DA) {
            model.addAttribute("loiQuenMatKhau", "Bạn đã nhập sai quá " + SO_LAN_TOI_DA
                    + " lần. Vui lòng gọi hotline 0339 999 999 để được hỗ trợ đặt lại mật khẩu.");
            return "quen-mat-khau";
        }
        try {
            Long id = taiKhoanService.xacMinhQuenMatKhau(tenDangNhap, lienHe);
            phien.removeAttribute(PHIEN_DEM_SAI);
            phien.setAttribute(PHIEN_DAT_LAI, new PhieuDatLai(id, Instant.now().plusSeconds(HIEU_LUC_GIAY)));
            return "redirect:/quen-mat-khau/dat-lai";
        } catch (LoiNghiepVu loi) {
            phien.setAttribute(PHIEN_DEM_SAI, daSai + 1);
            model.addAttribute("loiQuenMatKhau", loi.getMessage());
            return "quen-mat-khau";
        }
    }

    private PhieuDatLai layPhieu(HttpSession phien) {
        if (phien.getAttribute(PHIEN_DAT_LAI) instanceof PhieuDatLai phieu && phieu.hetHan().isAfter(Instant.now())) {
            return phieu;
        }
        phien.removeAttribute(PHIEN_DAT_LAI);
        return null;
    }

    @GetMapping("/dat-lai")
    public String formDatLai(HttpSession phien, RedirectAttributes thongBao) {
        if (layPhieu(phien) == null) {
            thongBao.addFlashAttribute("thongBaoLoi", "Phiên đặt lại mật khẩu đã hết hạn, vui lòng xác minh lại");
            return "redirect:/quen-mat-khau";
        }
        return "dat-lai-mat-khau";
    }

    @PostMapping("/dat-lai")
    public String datLai(@RequestParam(required = false) String matKhauMoi,
                         @RequestParam(required = false) String nhapLai,
                         HttpSession phien, Model model, RedirectAttributes thongBao) {
        PhieuDatLai phieu = layPhieu(phien);
        if (phieu == null) {
            thongBao.addFlashAttribute("thongBaoLoi", "Phiên đặt lại mật khẩu đã hết hạn, vui lòng xác minh lại");
            return "redirect:/quen-mat-khau";
        }
        if (matKhauMoi == null || !matKhauMoi.equals(nhapLai)) {
            model.addAttribute("loiDatLai", "Mật khẩu nhập lại chưa khớp với mật khẩu mới");
            return "dat-lai-mat-khau";
        }
        try {
            taiKhoanService.datLaiMatKhau(phieu.idTaiKhoan(), matKhauMoi);
        } catch (LoiNghiepVu loi) {
            model.addAttribute("loiDatLai", loi.getMessage());
            return "dat-lai-mat-khau";
        }
        phien.removeAttribute(PHIEN_DAT_LAI);
        thongBao.addFlashAttribute("thongBao", "Đã đặt lại mật khẩu. Mời bạn đăng nhập bằng mật khẩu mới.");
        return "redirect:/dang-nhap";
    }
}
