package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vn.edu.huit.diengiadung.service.ThongKeService;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin/thong-ke")
public class AdminThongKeController {

    private final ThongKeService thongKeService;

    public AdminThongKeController(ThongKeService thongKeService) {
        this.thongKeService = thongKeService;
    }

    @GetMapping
    public String hienThi(
            @RequestParam(required = false) Integer thang,
            @RequestParam(required = false) Integer nam,
            Model model) {

        LocalDate homNay = LocalDate.now();

        int thangChon = thang != null && thang >= 1 && thang <= 12
                ? thang
                : homNay.getMonthValue();

        int namChon = nam != null && nam >= 2000 && nam <= 2100
                ? nam
                : homNay.getYear();

        model.addAttribute("thang", thangChon);
        model.addAttribute("nam", namChon);
        model.addAttribute("namBatDau", homNay.getYear() - 5);
        model.addAttribute("namKetThuc", homNay.getYear() + 1);
        model.addAttribute(
                "doanhThu",
                thongKeService.tinhDoanhThu(thangChon, namChon)
        );
        model.addAttribute(
                "soDonHoanThanh",
                thongKeService.demDonHoanThanh(thangChon, namChon)
        );
        model.addAttribute(
                "sanPhamBanChay",
                thongKeService.laySanPhamBanChay(thangChon, namChon)
        );

        return "admin/thong-ke/danh-sach";
    }
}