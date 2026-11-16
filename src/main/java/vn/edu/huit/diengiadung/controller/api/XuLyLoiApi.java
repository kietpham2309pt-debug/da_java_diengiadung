package vn.edu.huit.diengiadung.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.edu.huit.diengiadung.service.LoiNghiepVu;

import java.util.Map;

@RestControllerAdvice(assignableTypes = SanPhamApiController.class)
public class XuLyLoiApi {

    @ExceptionHandler(LoiNghiepVu.class)
    public ResponseEntity<Map<String, Object>> khongTimThay(LoiNghiepVu loi) {
        return traLoi(HttpStatus.NOT_FOUND, loi.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> saiThamSo(MethodArgumentTypeMismatchException loi) {
        return traLoi(HttpStatus.BAD_REQUEST, "Tham số «" + loi.getName() + "» không đúng kiểu dữ liệu");
    }

    private ResponseEntity<Map<String, Object>> traLoi(HttpStatus maTrangThai, String thongDiep) {
        return ResponseEntity.status(maTrangThai).body(Map.of("trangThai", maTrangThai.value(), "loi", thongDiep));
    }
}
