package vn.edu.huit.diengiadung.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CauHinhThanhToan {

    private final int phanTramGiamThanhToanTruoc;
    private final long codToiDa;
    private final String maNganHang;
    private final String tenNganHang;
    private final String soTaiKhoan;
    private final String chuTaiKhoan;

    public CauHinhThanhToan(@Value("${cuahang.thanh-toan-truoc.phan-tram-giam:2}") int phanTramGiamThanhToanTruoc,
                            @Value("${cuahang.cod.toi-da:20000000}") long codToiDa,
                            @Value("${cuahang.ngan-hang.ma-bin:970436}") String maNganHang,
                            @Value("${cuahang.ngan-hang.ten:Vietcombank}") String tenNganHang,
                            @Value("${cuahang.ngan-hang.so-tai-khoan:1027899999}") String soTaiKhoan,
                            @Value("${cuahang.ngan-hang.chu-tai-khoan:CONG TY TNHH BEP NE}") String chuTaiKhoan) {
        this.phanTramGiamThanhToanTruoc = phanTramGiamThanhToanTruoc;
        this.codToiDa = codToiDa;
        this.maNganHang = maNganHang;
        this.tenNganHang = tenNganHang;
        this.soTaiKhoan = soTaiKhoan;
        this.chuTaiKhoan = chuTaiKhoan;
    }

    public long tinhTienGiam(long tamTinh) {
        long phanVan = phanTramGiamThanhToanTruoc * 100L;
        return (tamTinh * phanVan + 5_000) / 10_000;
    }

    public boolean choPhepCod(long tamTinh) {
        return tamTinh <= codToiDa;
    }

    public String linkMaQr(long soTien, String noiDung) {
        return "https://img.vietqr.io/image/" + maNganHang + "-" + soTaiKhoan + "-compact2.png"
                + "?amount=" + soTien
                + "&addInfo=" + URLEncoder.encode(noiDung, StandardCharsets.UTF_8)
                + "&accountName=" + URLEncoder.encode(chuTaiKhoan, StandardCharsets.UTF_8);
    }

    public int getPhanTramGiamThanhToanTruoc() {
        return phanTramGiamThanhToanTruoc;
    }

    public long getCodToiDa() {
        return codToiDa;
    }

    public String getTenNganHang() {
        return tenNganHang;
    }

    public String getSoTaiKhoan() {
        return soTaiKhoan;
    }

    public String getChuTaiKhoan() {
        return chuTaiKhoan;
    }
}
