import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KhoClient implements AutoCloseable {

    private static final PrintStream OUT = new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

    private final Socket ketNoi;
    private final BufferedReader doc;
    private final BufferedWriter ghi;

    public KhoClient(String may, int cong) throws IOException {
        ketNoi = new Socket();
        ketNoi.connect(new InetSocketAddress(may, cong), 5000);
        ketNoi.setSoTimeout(15000);
        doc = new BufferedReader(new InputStreamReader(ketNoi.getInputStream(), StandardCharsets.UTF_8));
        ghi = new BufferedWriter(new OutputStreamWriter(ketNoi.getOutputStream(), StandardCharsets.UTF_8));
    }

    public List<String> gui(String lenh) throws IOException {
        ghi.write(lenh);
        ghi.newLine();
        ghi.flush();
        String dau = doc.readLine();
        if (dau == null) {
            throw new IOException("Máy chủ đã đóng kết nối");
        }
        List<String> ra = new ArrayList<>();
        ra.add(dau);
        if (dau.startsWith("OK|")) {
            int n = Integer.parseInt(dau.substring(3).trim());
            for (int i = 0; i < n; i++) {
                ra.add(doc.readLine());
            }
        }
        return ra;
    }

    @Override
    public void close() throws IOException {
        ketNoi.close();
    }

    public static void main(String[] args) {
        String may = args.length > 0 ? args[0] : "localhost";
        int cong = args.length > 1 ? Integer.parseInt(args[1]) : 9090;
        Scanner ban = new Scanner(System.in, StandardCharsets.UTF_8);
        OUT.println("=== MÁY KHO BEP NÈ - kết nối " + may + ":" + cong + " ===");
        try (KhoClient client = new KhoClient(may, cong)) {
            inKetQua(client.gui("XINCHAO"));
            while (true) {
                OUT.println();
                OUT.println("1. Đăng nhập   2. Tìm sản phẩm   3. Xem tồn kho   4. Danh sách nhà cung cấp");
                OUT.println("5. Nhập kho    6. Đơn chờ xác nhận   0. Thoát");
                OUT.print("Chọn: ");
                if (!ban.hasNextLine()) {
                    break;
                }
                String chon = ban.nextLine().trim();
                String lenh;
                switch (chon) {
                    case "1" -> lenh = "DANGNHAP|" + hoi(ban, "Tên đăng nhập") + "|" + hoi(ban, "Mật khẩu");
                    case "2" -> lenh = "TIM|" + hoi(ban, "Từ khoá (tên hoặc mã)");
                    case "3" -> lenh = "TONKHO|" + hoi(ban, "Mã sản phẩm");
                    case "4" -> lenh = "NCC";
                    case "5" -> lenh = "NHAPKHO|" + hoi(ban, "Mã sản phẩm") + "|" + hoi(ban, "Số lượng")
                            + "|" + hoi(ban, "Giá nhập (đồng)") + "|" + hoi(ban, "Mã nhà cung cấp");
                    case "6" -> lenh = "DONCHO";
                    case "0" -> lenh = "THOAT";
                    default -> {
                        OUT.println("Lựa chọn không hợp lệ");
                        continue;
                    }
                }
                inKetQua(client.gui(lenh));
                if ("0".equals(chon)) {
                    break;
                }
            }
        } catch (ConnectException e) {
            OUT.println("Không kết nối được tới máy chủ " + may + ":" + cong + ". Hãy chạy ứng dụng web trước.");
        } catch (SocketTimeoutException e) {
            OUT.println("Máy chủ không trả lời trong 15 giây, kết nối bị huỷ.");
        } catch (IOException e) {
            OUT.println("Mất kết nối: " + e.getMessage());
        }
    }

    private static String hoi(Scanner ban, String nhan) {
        OUT.print(nhan + ": ");
        return ban.nextLine().trim().replace("|", "/");
    }

    private static void inKetQua(List<String> ketQua) {
        String dau = ketQua.get(0);
        if (dau.startsWith("LOI|")) {
            OUT.println("[LỖI] " + dau.substring(4));
            return;
        }
        if (ketQua.size() == 1) {
            OUT.println("(không có dữ liệu)");
        }
        for (int i = 1; i < ketQua.size(); i++) {
            OUT.println("  " + ketQua.get(i).replace("|", "  |  "));
        }
    }
}
