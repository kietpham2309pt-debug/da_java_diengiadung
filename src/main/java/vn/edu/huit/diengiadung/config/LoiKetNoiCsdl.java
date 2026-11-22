package vn.edu.huit.diengiadung.config;

import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.sql.SQLException;

public class LoiKetNoiCsdl extends AbstractFailureAnalyzer<SQLException> {

    private static final String CHAY_SCRIPT = "Mở SQL Server Management Studio, đăng nhập bằng Windows Authentication, "
            + "mở và chạy (F5) lần lượt database/01_tao_csdl.sql rồi database/02_du_lieu_mau.sql trong thư mục dự án.";

    @Override
    protected FailureAnalysis analyze(Throwable goc, SQLException loi) {
        String thongBao = loi.getMessage() == null ? "" : loi.getMessage();
        if (thongBao.contains("Login failed")) {
            return new FailureAnalysis(
                    "Không đăng nhập được SQL Server bằng tài khoản trong application.properties (mặc định bepne / BepNe@2026).",
                    "1. " + CHAY_SCRIPT + " Script tạo sẵn login bepne.\n"
                            + "2. Trong SSMS: chuột phải tên server > Properties > Security > chọn \"SQL Server and Windows "
                            + "Authentication mode\" > OK, rồi khởi động lại dịch vụ SQL Server.\n"
                            + "3. Nếu muốn dùng tài khoản khác (ví dụ sa): đặt biến môi trường DB_USER và DB_PASS trước khi chạy.",
                    loi);
        }
        if (thongBao.contains("Cannot open database")) {
            return new FailureAnalysis(
                    "SQL Server chưa có cơ sở dữ liệu dien_gia_dung hoặc login bepne chưa được cấp quyền vào CSDL này.",
                    CHAY_SCRIPT, loi);
        }
        if (thongBao.contains("TCP/IP connection")) {
            return new FailureAnalysis(
                    "Không kết nối được SQL Server qua TCP (mặc định localhost cổng 1433).",
                    "1. Kiểm tra dịch vụ SQL Server đang chạy (services.msc).\n"
                            + "2. Mở SQL Server Configuration Manager > SQL Server Network Configuration > Protocols for "
                            + "<tên instance> > bật TCP/IP; trong TCP/IP > IP Addresses > IPAll đặt TCP Port = 1433, xoá "
                            + "TCP Dynamic Ports; rồi khởi động lại dịch vụ SQL Server.\n"
                            + "3. Máy cài bản Express (instance SQLEXPRESS) mà không muốn đổi cổng: đặt biến môi trường "
                            + "DB_URL=jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=dien_gia_dung;encrypt=true;"
                            + "trustServerCertificate=true và bật dịch vụ SQL Server Browser.",
                    loi);
        }
        return new FailureAnalysis("Lỗi khi làm việc với SQL Server: " + thongBao,
                "Xem README mục \"Cài đặt và chạy\". " + CHAY_SCRIPT, loi);
    }
}
