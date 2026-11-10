# Website quản lý bán hàng điện gia dụng

Đồ án học phần **Công nghệ Java** — Khoa Công nghệ Thông tin, Trường Đại học Công Thương TP.HCM, năm học 2026-2027.

## Thành viên nhóm

| Vai | MSSV | Họ tên | Mảng phụ trách |
|---|---|---|---|
| TV1 | 2001240237 | Phạm Tuấn Kiệt (nhóm trưởng) | Nền tảng, layout chung, tài khoản và phân quyền |
| TV2 | 2001240555 | Lê Quốc Việt | Loại sản phẩm, thương hiệu, nhà cung cấp, nhập kho |
| TV3 | 2001240536 | Nguyễn Thanh Tuấn | Sản phẩm, trang chủ, tìm kiếm, chi tiết sản phẩm |
| TV4 | 2001240258 | Ngô Hoàng Anh Phát | Giỏ hàng, đặt hàng, lịch sử đơn, hồ sơ khách hàng |
| TV5 | 2001240351 | Phạm Kim Hoàng Oanh | Quản lý đơn hàng, khách hàng, thống kê |

## Công nghệ

Java 17 · Spring Boot 3.5.3 · Spring MVC + Thymeleaf · Spring Data JPA (Hibernate 6) · Bean Validation · MySQL 8 · Bootstrap 5.

## Cách chạy

1. Cài **JDK 17** và **MySQL 8**. Không cần tạo cơ sở dữ liệu trước, ứng dụng tự tạo `dien_gia_dung`.
2. Mở `src/main/resources/application.properties`, sửa `DB_USER` và `DB_PASS` cho khớp MySQL trên máy mình
   (mặc định là `root` và mật khẩu rỗng).
3. Chạy:

```bash
mvnw spring-boot:run
```

4. Mở trình duyệt: <http://localhost:8080>

Lần chạy đầu Hibernate tạo 10 bảng, `data.sql` nạp 6 danh mục (bếp điện từ, bếp hồng ngoại, máy hút mùi, máy rửa chén, lò nướng, lò vi sóng), 4 thương hiệu, 3 nhà cung cấp, 40 sản phẩm mẫu, 5 khách hàng và 10 đơn hàng mẫu
(39 đang bán, 1 ngừng bán; có sản phẩm hết hàng và sắp hết hàng để thử giao diện).
Ảnh sản phẩm do quản trị viên upload được lưu ở thư mục `uploads/` cạnh project, thư mục này không đưa lên git.

## Tài khoản có sẵn

| Vai trò | Tên đăng nhập | Mật khẩu |
|---|---|---|
| Quản trị | `admin` | `admin123` |
| Khách hàng | `khach1` | `khach123` |

Mật khẩu lưu trong cơ sở dữ liệu ở dạng mã hoá BCrypt. Khi triển khai thật phải đổi tài khoản này.

## Giao diện

Giao diện lấy từ nhánh `giao-dien-moi` của repo Lập trình Web (`kietpham2309pt-debug/bepne`),
chuyển từ Razor sang Thymeleaf. Nền tối `#131313`, chữ kem, nhấn vàng đồng `#CBAA7D`, font Montserrat nhúng sẵn.
Gồm `static/css/site.css` (hệ thống gốc, không sửa) và `static/css/ung-dung.css` (phần bổ sung cho khu quản trị).
Không dùng Bootstrap hay bất kỳ thư viện giao diện nào.

**Trước khi viết HTML, đọc `QUY_TAC_GIAO_DIEN.md`**: khung trang chuẩn, 10 fragment dùng chung,
bộ 44 icon, quy ước ảnh theo mã loại, tên lớp cho thẻ sản phẩm, bảng, biểu mẫu, thông báo và trạng thái rỗng.

## Cấu trúc thư mục

```
src/main/java/vn/edu/huit/diengiadung/
├── config/       WebConfig, QuanTriInterceptor, KhoiTaoDuLieu
├── entity/       10 lớp thực thể
├── repository/   10 interface JpaRepository
├── service/      interface + impl
└── controller/   phía khách và controller/admin phía quản trị
src/main/resources/
├── templates/    giao diện Thymeleaf, layout/fragments.html dùng chung
├── static/css/   style.css
└── data.sql      dữ liệu mẫu
```

## Chức năng đã hoàn thành

Toàn bộ chức năng theo phân công đã nằm trong nhánh `main` và đã kiểm thử chéo giữa các module.

- **TV1**: project khung, layout Thymeleaf dùng chung, đăng ký, đăng nhập, đăng xuất, phân quyền chặn `/admin`,
  quản lý tài khoản (tìm kiếm, khoá hoặc mở khoá, đặt lại mật khẩu), quản lý loại sản phẩm, trang lỗi 400/404/500.
  Tài khoản bị khoá khi đang đăng nhập sẽ bị đăng xuất ở lần thao tác mua hàng kế tiếp.
- **TV2**: thương hiệu, nhà cung cấp, lập phiếu nhập nhiều dòng và cộng tồn kho, xem phiếu nhập, cảnh báo sản phẩm sắp hết hàng.
  Tài liệu trong `docs/tv2/`, ERD trong `ERD/ERD.png`, từ điển dữ liệu `docs/tv2/CSDL-he-thong.docx`.
- **TV3**: quản lý sản phẩm có upload ảnh và thông số kỹ thuật, ngừng bán hoặc mở bán lại; trang chủ, danh sách theo loại và thương hiệu,
  tìm kiếm, lọc theo mức giá, sắp xếp, phân trang, chi tiết sản phẩm. Tài liệu trong `docs/tv3/`.
- **TV4**: giỏ hàng lưu trong session, đặt hàng kiểm tồn và trừ tồn trong một giao dịch, lịch sử đơn, huỷ đơn chờ xác nhận, hồ sơ khách hàng.
  Tài liệu trong `docs/tv4/`.
- **TV5**: quản lý đơn hàng (lọc, chi tiết, cập nhật trạng thái, huỷ và hoàn tồn kho), quản lý khách hàng,
  thống kê doanh thu theo tháng và 10 sản phẩm bán chạy. Tài liệu trong `docs/tv5/`.

Dữ liệu mẫu trong `data.sql`: 6 loại, 4 thương hiệu, 3 nhà cung cấp, 40 sản phẩm, 5 khách hàng và 10 đơn hàng ở đủ các trạng thái.
Tài khoản thử: `admin / admin123` (quản trị) và `khach1 / khach123` (khách hàng).

## Quy ước làm việc

- Nhánh riêng cho từng chức năng: `tvX-ten-chuc-nang`, không đẩy thẳng lên `main`.
- Mỗi chức năng một Pull Request, có người khác xem lại, nhóm trưởng gộp.
- Commit bằng tài khoản GitHub của chính mình.
- **Không viết comment trong code.** Đặt tên biến và phương thức rõ nghĩa thay cho chú thích.
- Quy tắc đầy đủ về code, thiết kế cơ sở dữ liệu và nghiệp vụ: xem `../HUONG_DAN_CHO_AI.md`.
