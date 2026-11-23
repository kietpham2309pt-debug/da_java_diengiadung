package vn.edu.huit.diengiadung.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;
import vn.edu.huit.diengiadung.service.ThuDongThoi;

import java.util.Map;

@Controller
@RequestMapping("/admin/thu-dong-thoi")
public class AdminThuDongThoiController {

    private final ThuDongThoi thuDongThoi;

    public AdminThuDongThoiController(ThuDongThoi thuDongThoi) {
        this.thuDongThoi = thuDongThoi;
    }

    @GetMapping
    public String trang() {
        return "admin/thu-dong-thoi";
    }

    @PostMapping("/chay")
    @ResponseBody
    public ResponseEntity<?> chay(@RequestParam(defaultValue = "10") int soKhach,
                                  @RequestParam(defaultValue = "3") int tonBanDau,
                                  @RequestParam(defaultValue = "true") boolean coKhoa) throws InterruptedException {
        try {
            return ResponseEntity.ok(thuDongThoi.chay(soKhach, tonBanDau, coKhoa));
        } catch (LoiNghiepVu loi) {
            return ResponseEntity.badRequest().body(Map.of("thongBao", loi.getMessage()));
        }
    }
}
