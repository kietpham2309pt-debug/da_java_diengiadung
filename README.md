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

Lần chạy đầu Hibernate tạo 10 bảng, `data.sql` nạp danh mục, thương hiệu, nhà cung cấp và 24 sản phẩm mẫu.

## Tài khoản có sẵn

| Vai trò | Tên đăng nhập | Mật khẩu |
|---|---|---|
| Quản trị | `admin` | `admin123` |
| Khách hàng | `khach1` | `khach123` |

Mật khẩu lưu trong cơ sở dữ liệu ở dạng mã hoá BCrypt. Khi triển khai thật phải đổi tài khoản này.

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

## Phần đã làm và phần còn lại

Đã có trong nhánh `main`:

- 10 lớp thực thể và repository, đủ các kiểu quan hệ `@OneToOne`, `@OneToMany`, `@ManyToOne`.
- Layout Thymeleaf dùng chung: thanh menu, danh mục, khung thông báo, menu quản trị, chân trang.
- Đăng ký, đăng nhập, đăng xuất, phân quyền chặn khu vực `/admin`.
- Quản lý tài khoản: tìm kiếm, khoá hoặc mở khoá, đặt lại mật khẩu.
- Quản lý loại sản phẩm: danh sách, tìm kiếm, thêm, sửa, xoá, chặn xoá khi còn sản phẩm. **Đây là khuôn mẫu cho các module còn lại.**
- Trang chủ và trang danh sách sản phẩm ở mức tối thiểu để xem được dữ liệu mẫu.

Mỗi thành viên làm tiếp phần của mình theo đúng khuôn của `LoaiSanPham`
(`Entity` → `JpaRepository` → `Service` + `ServiceImpl` → `Controller` → view Thymeleaf):

- **TV2**: thương hiệu, nhà cung cấp, phiếu nhập và cập nhật tồn kho, cảnh báo sắp hết hàng.
- **TV3**: quản lý sản phẩm có upload ảnh; trang chủ, lọc, sắp xếp, phân trang, chi tiết sản phẩm.
- **TV4**: giỏ hàng lưu trong session, đặt hàng, lịch sử đơn, huỷ đơn, hồ sơ khách hàng.
- **TV5**: danh sách và chi tiết đơn hàng, cập nhật trạng thái, huỷ đơn kèm hoàn tồn kho, quản lý khách hàng, thống kê.

## Quy ước làm việc

- Nhánh riêng cho từng chức năng: `tvX-ten-chuc-nang`, không đẩy thẳng lên `main`.
- Mỗi chức năng một Pull Request, có người khác xem lại, nhóm trưởng gộp.
- Commit bằng tài khoản GitHub của chính mình.
- **Không viết comment trong code.** Đặt tên biến và phương thức rõ nghĩa thay cho chú thích.
- Quy tắc đầy đủ về code, thiết kế cơ sở dữ liệu và nghiệp vụ: xem `../HUONG_DAN_CHO_AI.md`.
