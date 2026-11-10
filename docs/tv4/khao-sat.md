# Khảo sát website bán điện máy: giỏ hàng, đặt hàng, lịch sử đơn

## 1. Mục tiêu và phạm vi
Khảo sát 3 website bán điện máy (Điện Máy Xanh, Nguyễn Kim, FPT Shop) để thiết kế luồng giỏ hàng, đặt hàng và theo dõi đơn phù hợp cho đồ án, loại bỏ các nghiệp vụ phức tạp ngoài phạm vi (trả góp qua thẻ, giao vận bên thứ ba).

## 2. Kết quả khảo sát thực tế

### 2.1. Điện Máy Xanh (dienmayxanh.com)
- **Đặt hàng:** Điền thông tin người nhận, chọn phương thức thanh toán (COD, thẻ, ví điện tử). Hỗ trợ mua nhanh không cần tạo tài khoản.
- **Theo dõi & Huỷ đơn:** Khách tra cứu bằng số điện thoại. Có nút "Huỷ đơn hàng" trực tiếp trên web đối với các đơn đang chờ xử lý. 

### 2.2. Nguyễn Kim (nguyenkim.com)
- **Giỏ hàng:** Cho phép lưu giỏ hàng tạm thời bằng LocalStorage khi chưa đăng nhập. Dễ dàng tăng giảm số lượng hoặc xoá sản phẩm.
- **Xác nhận đơn:** Đơn hàng online thường có SMS/Zalo thông báo hoặc nhân viên gọi xác nhận trước khi chuyển trạng thái Đang giao.

### 2.3. FPT Shop (fptshop.com.vn)
- **Giỏ hàng & Khuyến mãi:** Tích hợp ô nhập mã giảm giá ngay trong giỏ hàng.
- **Lịch sử đơn:** Tài khoản có mục quản lý lịch sử đơn chi tiết, hiển thị rõ dòng thời gian (Tiếp nhận -> Đang xử lý -> Đang giao).

## 3. Bảng so sánh tính năng

| Tiêu chí | Điện Máy Xanh | Nguyễn Kim | FPT Shop | Đồ án (TV4) |
|---|---|---|---|---|
| Giỏ hàng | Có | Có | Có | Có, lưu trong Session |
| Nhập thông tin nhận hàng | Có | Có | Có | Có, điền sẵn từ hồ sơ |
| Mã giảm giá | Có | Có | Có | Không làm |
| Thanh toán | Nhiều hình thức | Nhiều hình thức | Nhiều hình thức | Chỉ thanh toán khi nhận hàng (COD) |
| Trả góp | Có | Có | Có | Không làm |
| Xác nhận đơn | Gọi/Tin nhắn | Gọi điện | Tin nhắn/Gọi | Quản trị viên duyệt trên web |
| Lịch sử đơn | Có | Có | Có | Có |
| Khách tự huỷ đơn | Có (khi mới đặt) | Có | Có | Có (khi Chờ xác nhận) |

## 4. Áp dụng vào đồ án
1. **Giỏ hàng:** Lưu trên phiên làm việc (Session), kiểm tra số lượng tồn kho ngay khi bấm thêm.
2. **Đặt hàng:** Form điền sẵn từ hồ sơ khách hàng để tăng tốc độ đặt, cho phép sửa trực tiếp.
3. **Trạng thái:** Đơn mới mặc định là "Chờ xác nhận".
4. **Huỷ đơn:** Cho phép khách tự bấm huỷ khi đơn chưa được quản trị viên xử lý để tự động hoàn lại tồn kho.