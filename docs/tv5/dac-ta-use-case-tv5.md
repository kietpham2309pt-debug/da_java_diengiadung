# Đặc tả use case, module Quản lý đơn hàng, khách hàng và thống kê (TV5)

Sơ đồ use case: `use-case-tv5.png` (nguồn `use-case-tv5.puml`).

## Tác nhân của module

| Tác nhân | Mô tả |
|---|---|
| Quản trị viên | Người dùng có vai trò `ADMIN`, truy cập khu vực quản trị để tra cứu đơn hàng, theo dõi xử lý đơn, tìm kiếm khách hàng và xem thống kê. |

---

## UC-TV5-01. Quản lý đơn hàng

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TV5-01 |
| **Tên** | Quản lý đơn hàng |
| **Tác nhân chính** | Quản trị viên |
| **Mô tả** | Cho phép quản trị viên xem danh sách đơn hàng, lọc theo trạng thái và xem thông tin chi tiết nhằm theo dõi tình hình đơn hàng. |
| **Điều kiện trước** | Quản trị viên đã đăng nhập và được phép truy cập khu vực quản trị. Hệ thống có thể kết nối cơ sở dữ liệu. |
| **Điều kiện sau** | Danh sách hoặc thông tin đơn hàng phù hợp được hiển thị. Việc tra cứu không làm thay đổi dữ liệu đơn hàng. |

**Luồng chính**

1. Quản trị viên mở chức năng **Đơn hàng** tại khu vực quản trị.
2. Hệ thống truy xuất và hiển thị danh sách đơn hàng, sắp xếp theo ngày đặt mới nhất.
3. Hệ thống hiển thị các thông tin tổng quan của đơn hàng như mã đơn, khách hàng, ngày đặt, tổng tiền và trạng thái.
4. Quản trị viên chọn trạng thái muốn lọc hoặc giữ lựa chọn **Tất cả trạng thái**.
5. Hệ thống truy xuất và hiển thị các đơn hàng phù hợp với trạng thái đã chọn.
6. Quản trị viên chọn một đơn hàng để xem chi tiết.
7. Hệ thống hiển thị thông tin chi tiết của đơn hàng nếu chức năng chi tiết đã được cung cấp trong phiên bản triển khai.

**Luồng thay thế / ngoại lệ**

- **4a. Quản trị viên không chọn trạng thái:** hệ thống hiển thị tất cả đơn hàng.
- **5a. Không có đơn hàng phù hợp:** hệ thống hiển thị danh sách rỗng hoặc thông báo không có dữ liệu.
- **2a. Không tải được danh sách do lỗi hệ thống hoặc cơ sở dữ liệu:** hệ thống thông báo lỗi và cho phép quản trị viên thử lại.
- **6a. Đơn hàng không tồn tại hoặc không truy xuất được:** hệ thống thông báo không tìm thấy đơn hàng và quay lại danh sách.
- **1a. Người dùng chưa đăng nhập hoặc không có quyền quản trị:** hệ thống từ chối truy cập theo cơ chế phân quyền của module TV1.

**Quy tắc nghiệp vụ**

- Chức năng lọc dựa trên trạng thái đơn hàng được lưu trong hệ thống.
- Nếu không truyền trạng thái lọc hoặc trạng thái để trống, hệ thống trả về toàn bộ đơn hàng.
- Use Case này chỉ phục vụ tra cứu; cập nhật trạng thái và hủy đơn được mô tả riêng trong Use Case xử lý đơn hàng.

**Nơi cài đặt / đối chiếu**

- `AdminDonHangController.danhSach`
- `DonHangService.timTheoTrangThai`, `DonHangService.layTheoId`
- `templates/admin/don-hang/danh-sach.html`

**Ghi chú tình trạng triển khai:** trong mã nguồn ZIP được rà soát, controller đơn hàng hiện có đường dẫn hiển thị danh sách và lọc trạng thái. Luồng xem chi tiết cần được xác nhận với route và giao diện thực tế trước khi mô tả là đã hoàn thiện.

---

## UC-TV5-02. Xử lý đơn hàng

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TV5-02 |
| **Tên** | Xử lý đơn hàng |
| **Tác nhân chính** | Quản trị viên |
| **Mô tả** | Cho phép quản trị viên cập nhật trạng thái xử lý của đơn hàng hoặc hủy đơn theo điều kiện nghiệp vụ; khi hủy đơn hợp lệ, hệ thống hoàn lại số lượng tồn kho tương ứng. |
| **Điều kiện trước** | Quản trị viên đã đăng nhập và có quyền quản trị. Đơn hàng cần xử lý tồn tại trong hệ thống và có trạng thái hiện tại xác định. |
| **Điều kiện sau** | Nếu cập nhật hợp lệ, trạng thái mới được lưu. Nếu hủy đơn hợp lệ, đơn hàng chuyển sang trạng thái đã hủy và tồn kho được hoàn lại theo số lượng trong chi tiết đơn hàng. Nếu thao tác không hợp lệ, dữ liệu không bị thay đổi. |

**Luồng chính**

1. Quản trị viên mở danh sách đơn hàng và chọn đơn hàng cần xử lý.
2. Hệ thống hiển thị thông tin đơn hàng, các sản phẩm, số lượng và trạng thái hiện tại.
3. Quản trị viên chọn thao tác cập nhật trạng thái.
4. Hệ thống kiểm tra trạng thái hiện tại và tính hợp lệ của trạng thái đích theo quy trình nghiệp vụ.
5. Hệ thống lưu trạng thái mới của đơn hàng.
6. Hệ thống thông báo cập nhật thành công và hiển thị trạng thái mới.

**Luồng thay thế / ngoại lệ**

- **3a. Quản trị viên chọn hủy đơn:** hệ thống kiểm tra đơn hàng có đang ở trạng thái cho phép hủy hay không.
- **3a.1. Đơn hàng được phép hủy:** hệ thống chuyển đơn hàng sang trạng thái đã hủy, hoàn lại tồn kho theo chi tiết đơn hàng và thông báo kết quả.
- **3a.2. Đơn hàng không được phép hủy:** hệ thống từ chối thao tác, thông báo lý do và giữ nguyên trạng thái cũng như tồn kho.
- **4a. Trạng thái đích không hợp lệ theo quy trình:** hệ thống không lưu thay đổi và thông báo lỗi nghiệp vụ.
- **1a. Không tìm thấy đơn hàng:** hệ thống thông báo không tìm thấy đơn hàng; không thực hiện cập nhật.
- **5a. Lỗi khi lưu dữ liệu:** hệ thống thông báo thao tác thất bại; trạng thái và tồn kho phải được giữ nhất quán, không để xảy ra trường hợp chỉ cập nhật một phần.
- **1b. Người dùng không có quyền quản trị:** hệ thống từ chối truy cập.

**Quy tắc nghiệp vụ**

- Trạng thái đơn hàng phải tuân theo quy trình mà nhóm thống nhất, ví dụ: **Chờ xác nhận → Đã xác nhận → Đang giao → Hoàn thành**; trường hợp hủy chuyển sang **Đã hủy** nếu trạng thái hiện tại cho phép.
- Chỉ hoàn tồn kho khi thao tác hủy được chấp nhận; không hoàn tồn kho lặp lại cho cùng một đơn hàng.
- Việc cập nhật trạng thái và hoàn tồn kho cần được xử lý nhất quán trong cơ sở dữ liệu.

**Nơi cài đặt / đối chiếu**

- Phạm vi dự kiến thuộc module TV5: controller, service xử lý đơn hàng và phần giao diện quản trị đơn hàng.
- Trong mã nguồn ZIP được rà soát, `AdminDonHangController` hiện mới có xử lý danh sách/lọc; chưa thấy endpoint cập nhật trạng thái hoặc hủy đơn. Vì vậy, đặc tả này mô tả nghiệp vụ mục tiêu theo phân công, không khẳng định các luồng xử lý đã được triển khai hoàn chỉnh.

---

## Các Use Case liên quan trong module TV5

- Xem danh sách đơn hàng.
- Lọc đơn hàng theo trạng thái.
- Xem chi tiết đơn hàng.
- Cập nhật trạng thái đơn hàng.
- Hủy đơn hàng và hoàn tồn kho.
- Xem và tìm kiếm khách hàng.
- Xem doanh thu theo tháng và sản phẩm bán chạy.

## Tài liệu đối chiếu

- `docs/chuc-nang.md`
- `docs/dac-ta-use-case-tv1.md`
- `AdminDonHangController`, `AdminKhachHangController`, `AdminThongKeController` và các service tương ứng.
