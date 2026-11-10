# Đặc tả use case, module Giỏ hàng, đặt hàng, lịch sử đơn và hồ sơ (TV4)

Sơ đồ use case: `use-case-tv4.png` (nguồn `use-case-tv4.puml`). Sơ đồ hoạt động quy trình Đặt hàng: `hoat-dong-dat-hang.png` (nguồn `hoat-dong-dat-hang.puml`).

## Tác nhân của module

| Tác nhân | Mô tả |
|---|---|
| Khách hàng | Người dùng đã đăng nhập bằng tài khoản có hồ sơ khách hàng. Thêm sản phẩm vào giỏ, đặt hàng, theo dõi và huỷ đơn, cập nhật hồ sơ. |

---

## UC-TV4-01. Đặt hàng

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TV4-01 |
| **Tên** | Đặt hàng |
| **Tác nhân chính** | Khách hàng |
| **Mô tả** | Cho phép khách hàng chuyển các sản phẩm trong giỏ thành một đơn hàng, nhập thông tin nhận hàng và xác nhận đặt mua. |
| **Điều kiện trước** | Khách hàng đã đăng nhập bằng tài khoản có hồ sơ khách hàng. Giỏ hàng có ít nhất một sản phẩm. |
| **Điều kiện sau** | Đơn hàng được lưu với trạng thái Chờ xác nhận, tồn kho các sản phẩm được trừ tương ứng, giỏ hàng được làm trống. |

**Luồng chính**

1. Khách hàng mở trang giỏ hàng và bấm **Đặt hàng**.
2. Hệ thống kiểm tra khách đã đăng nhập và giỏ hàng không trống.
3. Hệ thống hiển thị tóm tắt đơn hàng (sản phẩm, số lượng, thành tiền, tổng cộng) và form thông tin nhận hàng, điền sẵn họ tên, số điện thoại, địa chỉ từ hồ sơ khách hàng.
4. Khách hàng kiểm tra hoặc sửa tên người nhận, số điện thoại, địa chỉ giao hàng, nhập ghi chú nếu cần.
5. Khách hàng bấm **Xác nhận đặt hàng**.
6. Hệ thống kiểm tra tính hợp lệ của thông tin nhập.
7. Hệ thống bắt đầu một giao dịch. Với từng sản phẩm trong giỏ, hệ thống kiểm tra sản phẩm còn bán và đủ tồn kho, trừ tồn kho, tạo dòng chi tiết đơn với đơn giá tại thời điểm đặt.
8. Hệ thống tính tổng tiền, lưu đơn hàng với trạng thái Chờ xác nhận và kết thúc giao dịch.
9. Hệ thống xoá giỏ hàng, chuyển sang trang chi tiết đơn và thông báo đặt hàng thành công.

**Luồng thay thế / ngoại lệ**

- **2a. Khách hàng chưa đăng nhập:** hệ thống chuyển về trang đăng nhập.
- **2b. Giỏ hàng trống:** hệ thống báo giỏ hàng trống và quay về trang giỏ hàng.
- **2c. Tài khoản không có hồ sơ khách hàng (ví dụ tài khoản quản trị):** hệ thống báo không thể mua hàng và quay về giỏ hàng.
- **6a. Thông tin không hợp lệ (để trống, số điện thoại sai định dạng, vượt độ dài):** hệ thống hiển thị lỗi ở từng ô, giữ nguyên dữ liệu đã nhập để khách sửa lại.
- **7a. Có sản phẩm đã ngừng bán hoặc không đủ tồn kho:** hệ thống huỷ toàn bộ giao dịch (không trừ kho, không tạo đơn), báo tên sản phẩm và số lượng còn lại, quay về giỏ hàng để khách điều chỉnh.

**Quy tắc nghiệp vụ**

- Số điện thoại gồm 10 chữ số và bắt đầu bằng 0.
- Việc trừ tồn kho và lưu đơn nằm trong cùng một giao dịch, nếu một bước lỗi thì toàn bộ được hoàn lại.
- Đơn giá trong chi tiết đơn là giá tại thời điểm đặt (đã tính giảm giá nếu có), không thay đổi khi giá sản phẩm đổi sau này.
- Giỏ hàng chỉ lưu trong phiên làm việc, chỉ khi xác nhận đặt hàng dữ liệu mới được ghi vào cơ sở dữ liệu.

**Nơi cài đặt / đối chiếu**

- `DatHangController` (`/dat-hang`)
- `DatHangService.datHang`, `GioHangService.xemChiTiet`
- `templates/dat-hang.html`

---

## UC-TV4-02. Huỷ đơn hàng

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TV4-02 |
| **Tên** | Huỷ đơn hàng |
| **Tác nhân chính** | Khách hàng |
| **Mô tả** | Cho phép khách hàng tự huỷ đơn mình đã đặt khi đơn chưa được cửa hàng xác nhận. |
| **Điều kiện trước** | Khách hàng đã đăng nhập. Đơn hàng thuộc về khách hàng đó và đang ở trạng thái Chờ xác nhận. |
| **Điều kiện sau** | Đơn chuyển sang trạng thái Đã huỷ, tồn kho các sản phẩm trong đơn được hoàn lại. |

**Luồng chính**

1. Khách hàng mở **Đơn hàng của tôi** và chọn một đơn để xem chi tiết.
2. Hệ thống hiển thị chi tiết đơn, có nút **Huỷ đơn** khi đơn đang Chờ xác nhận.
3. Khách hàng bấm **Huỷ đơn** và xác nhận trong hộp thoại hỏi lại.
4. Hệ thống kiểm tra đơn thuộc về khách hàng và đang Chờ xác nhận.
5. Hệ thống chuyển đơn sang trạng thái Đã huỷ và hoàn lại tồn kho cho từng sản phẩm trong đơn (dùng chức năng huỷ đơn của module TV5).
6. Hệ thống quay về trang chi tiết đơn và thông báo đã huỷ.

**Luồng thay thế / ngoại lệ**

- **2a. Đơn đã được xác nhận, đang giao, hoàn thành hoặc đã huỷ:** không hiển thị nút huỷ.
- **4a. Đơn không thuộc về khách hàng đang đăng nhập hoặc không tồn tại:** hệ thống trả về không tìm thấy đơn hàng, không tiết lộ đơn của người khác.
- **4b. Đơn đã đổi sang trạng thái khác trong lúc khách chờ (ví dụ cửa hàng vừa xác nhận):** hệ thống báo chỉ huỷ được đơn Chờ xác nhận và không thay đổi dữ liệu.

**Quy tắc nghiệp vụ**

- Khách hàng chỉ huỷ được đơn Chờ xác nhận. Với các đơn đã xác nhận, khách liên hệ cửa hàng để được hỗ trợ.
- Việc hoàn tồn kho dùng chung một đoạn xử lý với chức năng huỷ đơn phía quản trị để hai bên không lệch nhau.

**Nơi cài đặt / đối chiếu**

- `DonHangKhachController` (`/don-hang/{id}/huy`)
- `DatHangService.huyDonCuaKhach`, `DonHangService.huyDonHang`
- `templates/don-hang/chi-tiet.html`
