# Đặc tả use case, module Tài khoản và phân quyền (TV1)

Sơ đồ use case: `use-case-tv1.png` (nguồn `use-case-tv1.puml`).

Tác nhân của module:

| Tác nhân | Mô tả |
|---|---|
| Khách | Người chưa đăng nhập, chỉ xem được phần cửa hàng, đăng ký và đăng nhập |
| Khách hàng | Tài khoản vai trò `KHACHHANG`, đăng nhập rồi mua hàng, đăng xuất |
| Quản trị viên | Tài khoản vai trò `ADMIN`, vào được khu `/admin`, quản lý tài khoản |

---

## UC-TK-02. Đăng nhập

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TK-02 |
| **Tên** | Đăng nhập |
| **Tác nhân chính** | Khách |
| **Mô tả** | Người dùng nhập tên đăng nhập và mật khẩu để vào hệ thống với đúng vai trò của mình |
| **Điều kiện trước** | Tài khoản đã tồn tại trong bảng `tai_khoan` |
| **Điều kiện sau** | Đối tượng `TaiKhoan` được lưu vào `HttpSession` với khoá `taiKhoan`; thanh menu hiện tên người dùng |

**Luồng chính**

1. Khách mở `/dang-nhap`.
2. Hệ thống hiện biểu mẫu gồm tên đăng nhập và mật khẩu.
3. Khách nhập thông tin rồi bấm **Đăng nhập**.
4. Hệ thống tìm tài khoản theo tên đăng nhập.
5. Hệ thống so mật khẩu vừa nhập với mã BCrypt đang lưu.
6. Hệ thống kiểm tra tài khoản còn hoạt động.
7. Hệ thống lưu tài khoản vào session.
8. Nếu là quản trị viên, hệ thống chuyển tới `/admin`; nếu là khách hàng thì về trang chủ.

**Luồng thay thế**

- **4a. Không có tài khoản nào trùng tên đăng nhập:** hệ thống báo "Sai tên đăng nhập hoặc mật khẩu" và giữ nguyên trang. Thông báo cố ý không nói rõ sai ở đâu để người lạ không dò được tên đăng nhập có thật hay không.
- **5a. Mật khẩu không khớp:** báo cùng một câu như 4a.
- **6a. Tài khoản đã bị khoá:** hệ thống báo "Tài khoản đang bị khoá" và không tạo session.
- **3a. Bỏ trống một ô:** trình duyệt chặn gửi biểu mẫu vì ô có thuộc tính `required`.

**Quy tắc nghiệp vụ**

- Mật khẩu chỉ lưu dưới dạng mã BCrypt, hệ thống không bao giờ đọc lại được mật khẩu gốc.
- Mỗi lần đăng nhập thành công đều tạo session mới, đăng xuất thì huỷ toàn bộ session.

**Nơi cài đặt**

`TaiKhoanController.dangNhap`, `TaiKhoanServiceImpl.dangNhap`, `templates/dang-nhap.html`.

---

## UC-TK-06. Khoá hoặc mở khoá tài khoản

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TK-06 |
| **Tên** | Khoá hoặc mở khoá tài khoản |
| **Tác nhân chính** | Quản trị viên |
| **Mô tả** | Quản trị viên tạm ngưng quyền đăng nhập của một tài khoản, hoặc mở lại |
| **Điều kiện trước** | Quản trị viên đã đăng nhập; tài khoản cần xử lý đang có trong danh sách |
| **Điều kiện sau** | Cột `hoat_dong` của tài khoản đó đổi giá trị; tài khoản bị khoá không đăng nhập được nữa |

**Luồng chính**

1. Quản trị viên mở `/admin/tai-khoan`.
2. Hệ thống kiểm tra quyền quản trị (use case *Kiểm tra quyền quản trị*), rồi hiện danh sách tài khoản kèm trạng thái.
3. Quản trị viên bấm **Khoá tài khoản** ở dòng cần xử lý.
4. Hệ thống đảo giá trị `hoatDong` của tài khoản đó và lưu lại.
5. Hệ thống quay về danh sách và báo "Đã đổi trạng thái tài khoản".

**Luồng thay thế**

- **2a. Người dùng chưa đăng nhập hoặc không phải quản trị:** `QuanTriInterceptor` chặn và chuyển tới `/dang-nhap?loi=quyen`.
- **4a. Tài khoản đang xử lý là quản trị viên và đang hoạt động:** hệ thống từ chối với thông báo "Không được khoá tài khoản quản trị", tránh trường hợp khoá nhầm rồi không còn ai vào được khu quản trị.
- **4b. Không tìm thấy tài khoản theo mã:** hệ thống báo "Không tìm thấy tài khoản có mã ..." và quay về danh sách.

**Quy tắc nghiệp vụ**

- Chỉ vai trò `ADMIN` được thực hiện.
- Luôn phải còn ít nhất một tài khoản quản trị hoạt động.
- Khoá tài khoản không xoá dữ liệu, đơn hàng cũ của người đó vẫn giữ nguyên.

**Nơi cài đặt**

`AdminTaiKhoanController.doiTrangThai`, `TaiKhoanServiceImpl.doiTrangThai`, `QuanTriInterceptor`, `templates/admin/tai-khoan/danh-sach.html`.
