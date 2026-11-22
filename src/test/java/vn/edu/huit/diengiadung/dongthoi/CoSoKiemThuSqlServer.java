package vn.edu.huit.diengiadung.dongthoi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vn.edu.huit.diengiadung.entity.SanPham;
import vn.edu.huit.diengiadung.repository.LoaiSanPhamRepository;
import vn.edu.huit.diengiadung.repository.SanPhamRepository;
import vn.edu.huit.diengiadung.repository.ThuongHieuRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
abstract class CoSoKiemThuSqlServer {

    private static Boolean sanSang;

    static synchronized boolean coSqlServer() {
        if (sanSang == null) {
            String url = System.getenv().getOrDefault("DB_URL_MASTER",
                    "jdbc:sqlserver://localhost:1433;databaseName=master;encrypt=true;trustServerCertificate=true;loginTimeout=5");
            String nguoiDung = System.getenv().getOrDefault("DB_USER", "bepne");
            String matKhau = System.getenv().getOrDefault("DB_PASS", "BepNe@2026");
            try (Connection c = DriverManager.getConnection(url, nguoiDung, matKhau);
                 Statement s = c.createStatement()) {
                s.execute("IF DB_ID('dien_gia_dung_test') IS NULL CREATE DATABASE dien_gia_dung_test COLLATE Vietnamese_CI_AI");
                sanSang = true;
            } catch (Exception e) {
                System.out.println("Bỏ qua kiểm thử tích hợp vì chưa kết nối được SQL Server: " + e.getMessage());
                sanSang = false;
            }
        }
        return sanSang;
    }

    @Autowired
    protected SanPhamRepository sanPhamRepository;

    @Autowired
    protected LoaiSanPhamRepository loaiSanPhamRepository;

    @Autowired
    protected ThuongHieuRepository thuongHieuRepository;

    protected SanPham taoSanPham(int tonKho, long gia) {
        SanPham sp = new SanPham();
        sp.setMaSanPham("TEST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        sp.setTenSanPham("Sản phẩm kiểm thử đồng thời");
        sp.setGiaBan(gia);
        sp.setSoLuongTon(tonKho);
        sp.setBaoHanhThang(12);
        sp.setDangBan(true);
        sp.setLoaiSanPham(loaiSanPhamRepository.findAll().get(0));
        sp.setThuongHieu(thuongHieuRepository.findAll().get(0));
        return sanPhamRepository.save(sp);
    }

    protected int tonKho(Long id) {
        return sanPhamRepository.findById(id).orElseThrow().getSoLuongTon();
    }
}
