# Kiểm thử chéo: TV3 kiểm thử module của TV4

Người kiểm thử: TV3 Nguyễn Thanh Tuấn.
Module được kiểm thử: Giỏ hàng và Đặt hàng của TV4 Ngô Hoàng Anh Phát
(bảng `KhachHang`, `DonHang`, `ChiTietDonHang`).

## 1. Kết quả lần kiểm thử ngày 09/10/2026

Đã `git fetch` toàn bộ repo. Trên `main` và trên mọi nhánh của repo hiện **chưa có mã nguồn module TV4**
(không có controller, service hay view cho giỏ hàng, đặt hàng, lịch sử đơn, hồ sơ khách hàng),
nên chưa chạy được các ca kiểm thử ở mục 3. Chạy ứng dụng ở nhánh `main` và ghi nhận:

| Đường dẫn | Kết quả | Nơi dẫn tới đường dẫn này |
|---|---|---|
| `/gio-hang` | Trang 404 | Icon giỏ hàng trên thanh menu, mục "Giỏ hàng" trong ngăn kéo menu điện thoại |
| `/dat-hang` | Trang 404 | Chưa có liên kết |
| `POST /gio-hang/them` | Trang 404 | Nút "Thêm vào giỏ hàng" ở trang chi tiết sản phẩm (TV3) |

Đây là phần chưa làm theo kế hoạch, chưa phải lỗi, nên chưa mở GitHub Issue.
Khi TV4 tạo Pull Request, TV3 chạy lại toàn bộ mục 3 trên nhánh của TV4 và ghi **mỗi lỗi thành một Issue có ảnh chụp**,
gắn nhãn `tv4`, tiêu đề dạng `[TV4] <mô tả ngắn>`.

## 2. Những điểm nối giữa module TV3 và TV4

Để hai module ghép vào nhau chạy được ngay, TV3 đã làm sẵn phía mình như sau, TV4 chỉ cần làm phía nhận:

- Trang `/san-pham/{id}` gửi form `POST /gio-hang/them` với hai tham số:
  `id` (mã sản phẩm, kiểu `Long`) và `soLuong` (kiểu `int`, từ 1 tới số nhỏ hơn giữa 20 và tồn kho).
  Sản phẩm hết hàng thì nút bị khoá, không gửi được.
- Sản phẩm ngừng bán (`dangBan = false`) không hiện ở cửa hàng. Khi thêm vào giỏ hoặc đặt hàng, TV4 nên kiểm tra
  lại `dangBan` và `soLuongTon` vì giỏ lưu trong session có thể đã cũ.
- Giá nên lấy bằng `sanPham.getGiaHienTai()` (có khuyến mãi thì lấy giá khuyến mãi), đúng với giá khách thấy.
- Lớp `SanPham` có ràng buộc Bean Validation (`@NotNull` loại và thương hiệu, `@Min(0)` tồn kho...). Hibernate kiểm
  tra các ràng buộc này cả khi trừ tồn kho rồi `save`, nên tồn kho không bao giờ âm được: nếu trừ quá thì sẽ ra
  `ConstraintViolationException`. Nên kiểm tra đủ tồn trước khi trừ, trong cùng `@Transactional`.
- Sản phẩm đã nằm trong `ChiTietDonHang` thì trang quản trị của TV3 không cho xoá, chỉ cho ngừng bán,
  nên đơn hàng cũ không bao giờ mất sản phẩm.
- Thanh menu đã chờ sẵn biến `soMonTrongGio` để hiện số món trên icon giỏ hàng (xem `QUY_TAC_GIAO_DIEN.md`).

## 3. Bộ ca kiểm thử sẽ chạy cho module TV4

Tài khoản thử: `khach1` / `khach123`, `admin` / `admin123`. Dữ liệu: 36 sản phẩm mẫu của TV3, trong đó
`BHN-KOC-002` hết hàng, `HM-BOS-002` còn 2, `LVS-CHE-002` ngừng bán.

### 3.1. Giỏ hàng lưu trong HttpSession

| Mã | Các bước | Kết quả mong đợi |
|---|---|---|
| KTC-TV4-01 | Mở chi tiết `BDT-BOS-001`, chọn số lượng 2, bấm Thêm vào giỏ hàng | Giỏ có 1 dòng, số lượng 2, icon giỏ hàng hiện 2 |
| KTC-TV4-02 | Thêm tiếp cùng sản phẩm số lượng 1 | Vẫn 1 dòng, số lượng cộng dồn thành 3 |
| KTC-TV4-03 | Thêm `HM-BOS-002` (còn 2) số lượng 3 bằng cách sửa số trên form | Báo không đủ hàng hoặc chỉ cho tối đa 2 |
| KTC-TV4-04 | Gửi tay `POST /gio-hang/them` với `id` của `LVS-CHE-002` (ngừng bán) | Từ chối, báo sản phẩm không còn bán |
| KTC-TV4-05 | Gửi tay `id=9999` hoặc `soLuong=0`, `soLuong=-1`, `soLuong=abc` | Báo lỗi dễ hiểu, không lỗi 500 |
| KTC-TV4-06 | Trong giỏ, sửa số lượng, xoá một dòng | Thành tiền và tổng tiền tính lại đúng |
| KTC-TV4-07 | Giỏ trống | Có khung trống và nút quay lại mua hàng |
| KTC-TV4-08 | Đóng trình duyệt rồi mở lại (session mới) | Giỏ trống, đúng tính chất lưu trong session |
| KTC-TV4-09 | Đăng xuất khi giỏ có hàng | Giỏ bị xoá theo session |
| KTC-TV4-10 | Giá sản phẩm có khuyến mãi trong giỏ | Bằng giá khuyến mãi, khớp trang chi tiết |

### 3.2. Đặt hàng: kiểm tồn và tạo đơn trong một `@Transactional`

| Mã | Các bước | Kết quả mong đợi |
|---|---|---|
| KTC-TV4-11 | Chưa đăng nhập, bấm Đặt hàng | Chuyển tới đăng nhập, đăng nhập xong quay lại đặt hàng |
| KTC-TV4-12 | Bỏ trống tên người nhận, số điện thoại, địa chỉ | Lỗi dưới từng ô, không tạo đơn |
| KTC-TV4-13 | Số điện thoại sai định dạng | Báo lỗi |
| KTC-TV4-14 | Đặt hàng hợp lệ 2 sản phẩm | Tạo 1 `DonHang` trạng thái chờ xác nhận, 2 `ChiTietDonHang`, tồn kho mỗi sản phẩm giảm đúng, giỏ trống |
| KTC-TV4-15 | Mở 2 tab, cùng đặt `HM-BOS-002` số lượng 2 | Chỉ 1 đơn thành công, đơn còn lại báo hết hàng, tồn không âm |
| KTC-TV4-16 | Giỏ có 1 sản phẩm đủ hàng và 1 sản phẩm thiếu hàng | Không tạo đơn nào, không trừ tồn sản phẩm nào (rollback toàn bộ) |
| KTC-TV4-17 | Trang đặt hàng thành công | Hiện mã đơn, tổng tiền, danh sách sản phẩm |

### 3.3. Lịch sử đơn, huỷ đơn, hồ sơ khách hàng

| Mã | Các bước | Kết quả mong đợi |
|---|---|---|
| KTC-TV4-18 | `khach1` mở lịch sử đơn | Chỉ thấy đơn của mình, mới nhất trước |
| KTC-TV4-19 | Đổi mã đơn trên địa chỉ sang đơn của người khác | Bị chặn, không xem được |
| KTC-TV4-20 | Huỷ đơn đang chờ xác nhận | Trạng thái thành Đã huỷ, tồn kho được cộng trả lại |
| KTC-TV4-21 | Huỷ đơn đã xác nhận hoặc đang giao | Không có nút huỷ; gửi tay cũng bị từ chối |
| KTC-TV4-22 | Sửa hồ sơ: họ tên trống, email sai | Lỗi dưới từng ô |
| KTC-TV4-23 | Sửa hồ sơ hợp lệ | Lưu được, lần đặt hàng sau tự điền thông tin mới |
| KTC-TV4-24 | Trang giỏ hàng, đặt hàng, lịch sử ở khung 520px | Không tràn ngang, có thanh menu và chân trang |

## 4. Mẫu ghi lỗi thành GitHub Issue

```
Tiêu đề: [TV4] Đặt hàng khi thiếu hàng vẫn trừ tồn sản phẩm đầu tiên
Ca kiểm thử: KTC-TV4-16
Các bước: 1. ... 2. ... 3. ...
Kết quả mong đợi: ...
Kết quả thực tế: ...
Ảnh chụp: (kéo thả ảnh vào ô bình luận)
Nhánh, commit: tv4-..., abc1234
```
