package vn.edu.huit.diengiadung.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;

/**
 * Tệp vượt spring.servlet.multipart.max-file-size bị chặn trước khi vào controller,
 * nên phải bắt ở đây rồi quay về đúng form kèm thông báo dễ hiểu.
 */
@ControllerAdvice
public class XuLyLoiTaiAnh {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String anhQuaLon(HttpServletRequest yeuCau, RedirectAttributes thongBao) {
        thongBao.addFlashAttribute("thongBaoLoi", "Ảnh vượt quá 5 MB nên chưa lưu được. Hãy chọn ảnh nhỏ hơn rồi lưu lại");
        return "redirect:" + layTrangQuayVe(yeuCau.getHeader("Referer"));
    }

    private String layTrangQuayVe(String trangTruoc) {
        String macDinh = "/admin/san-pham";
        if (trangTruoc == null) {
            return macDinh;
        }
        try {
            String duongDan = URI.create(trangTruoc).getRawPath();
            if (duongDan != null && duongDan.startsWith("/admin/")) {
                return duongDan;
            }
        } catch (IllegalArgumentException loi) {
            return macDinh;
        }
        return macDinh;
    }
}
