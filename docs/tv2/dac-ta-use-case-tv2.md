# Đặc tả use case, module Quản lý đối tác và nhập kho (TV2)

Sơ đồ use case: `use-case-tv2.png` (nguồn `use-case-tv2.puml`).

## Tác nhân của module

| Tác nhân | Mô tả |
|---|---|
| Quản trị viên | Người dùng có vai trò `ADMIN`, truy cập khu vực quản trị để quản lý thương hiệu, nhà cung cấp, lập phiếu nhập và xem cảnh báo kho. |

---

## UC-TV2-01. Quản lý nhà cung cấp

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TV2-01 |
| **Tên** | Quản lý nhà cung cấp |
| **Tác nhân chính** | Quản trị viên |
| **Mô tả** | Cho phép quản trị viên xem danh sách, thêm mới, sửa, xóa và tìm kiếm nhà cung cấp nhằm quản lý thông tin đối tác cung ứng hàng hóa. |
| **Điều kiện trước** | Quản trị viên đã đăng nhập và được phép truy cập khu vực quản trị. Hệ thống có thể kết nối cơ sở dữ liệu. |
| **Điều kiện sau** | Dữ liệu nhà cung cấp được thêm, sửa hoặc xóa hợp lệ và được lưu trong hệ thống. Nhà cung cấp đã có phiếu nhập không bị xóa; nếu thao tác không hợp lệ thì dữ liệu không bị thay đổi. |

**Luồng chính (thêm mới / sửa)**

1. Quản trị viên mở chức năng **Nhà cung cấp** tại khu vực quản trị.
2. Hệ thống truy xuất và hiển thị danh sách nhà cung cấp.
3. Quản trị viên chọn thao tác **Thêm mới** hoặc **Sửa** một nhà cung cấp, sau đó nhập hoặc chỉnh sửa thông tin.
4. Hệ thống kiểm tra tính hợp lệ của dữ liệu (tên nhà cung cấp không được để trống).
5. Hệ thống lưu dữ liệu nhà cung cấp.
6. Hệ thống thông báo thành công và hiển thị lại danh sách đã cập nhật.

**Luồng thay thế / ngoại lệ**

- **2a. Quản trị viên tìm kiếm nhà cung cấp:** quản trị viên nhập từ khóa tìm kiếm; hệ thống hiển thị các nhà cung cấp phù hợp. Nếu không có kết quả, hệ thống hiển thị danh sách rỗng hoặc thông báo không có dữ liệu.
- **2b. Không tải được danh sách do lỗi hệ thống hoặc cơ sở dữ liệu:** hệ thống thông báo lỗi và cho phép quản trị viên thử lại.
- **4a. Dữ liệu không hợp lệ (tên để trống):** hệ thống không lưu, hiển thị thông báo lỗi tại trường nhập và giữ nguyên form để quản trị viên chỉnh sửa.
- **5a. Lỗi khi lưu dữ liệu:** hệ thống thông báo thao tác thất bại; dữ liệu nhà cung cấp không bị thay đổi.
- **3a. Quản trị viên chọn Xóa nhà cung cấp:** hệ thống kiểm tra số phiếu nhập đã gắn với nhà cung cấp này.
  - **3a.1. Nhà cung cấp chưa có phiếu nhập:** hệ thống tiến hành xóa, thông báo xóa thành công và hiển thị lại danh sách.
  - **3a.2. Nhà cung cấp đã có phiếu nhập:** hệ thống ném `LoiNghiepVu`, hiển thị thông báo lỗi màu đỏ từ chối xóa để bảo vệ toàn vẹn dữ liệu; dữ liệu giữ nguyên.
- **3b. Nhà cung cấp cần sửa hoặc xóa không tồn tại:** hệ thống thông báo không tìm thấy nhà cung cấp và quay lại danh sách.
- **1a. Người dùng chưa đăng nhập hoặc không có quyền quản trị:** hệ thống từ chối truy cập theo cơ chế phân quyền của module TV1.

**Quy tắc nghiệp vụ**

- Tên nhà cung cấp là thông tin bắt buộc, không được để trống.
- Không được xóa nhà cung cấp đã phát sinh phiếu nhập, nhằm bảo toàn lịch sử nhập hàng và tính toàn vẹn dữ liệu tham chiếu.
- Việc kiểm tra điều kiện xóa được thực hiện ở tầng service, không chỉ dựa vào giao diện.
- Thông báo lỗi nghiệp vụ được hiển thị cho quản trị viên (màu đỏ) thay vì để lỗi hệ thống phát sinh.

**Nơi cài đặt / đối chiếu**

- `AdminNhaCungCapController`
- `NhaCungCapServiceImpl.xoa`

---

## UC-TV2-02. Lập phiếu nhập kho và cộng tồn kho

| Mục | Nội dung |
|---|---|
| **Mã** | UC-TV2-02 |
| **Tên** | Lập phiếu nhập kho và cộng tồn kho |
| **Tác nhân chính** | Quản trị viên |
| **Mô tả** | Cho phép quản trị viên lập phiếu nhập hàng, quản lý danh sách sản phẩm nhập trên giao diện động, tự động tính thành tiền và tổng tiền, đồng thời tự động cộng số lượng nhập vào tồn kho của từng sản phẩm. |
| **Điều kiện trước** | Quản trị viên đã đăng nhập và có quyền quản trị. Hệ thống đã có dữ liệu nhà cung cấp và sản phẩm. |
| **Điều kiện sau** | Nếu lập phiếu hợp lệ, phiếu nhập và chi tiết phiếu nhập được lưu trữ, số lượng tồn kho của các sản phẩm trong phiếu được cộng dồn chính xác. Nếu có lỗi, toàn bộ thao tác bị hoàn tác, phiếu nhập không được lưu và tồn kho không thay đổi. |

**Luồng chính**

1. Quản trị viên chọn **Lập phiếu nhập** tại khu vực quản trị.
2. Hệ thống hiển thị form nhập thông tin chung và bảng chi tiết sản phẩm nhập.
3. Quản trị viên chọn nhà cung cấp, bấm **Thêm sản phẩm**, chọn sản phẩm, nhập số lượng và giá nhập.
4. Hệ thống tự động tính thành tiền của từng dòng và tổng tiền của phiếu.
5. Quản trị viên bấm **Lưu phiếu nhập**.
6. Hệ thống xác thực dữ liệu đầu vào.
7. Hệ thống lưu `PhieuNhap` và các `ChiTietPhieuNhap`.
8. Hệ thống truy xuất từng sản phẩm trong phiếu và cộng thêm `soLuong` vào `soLuongTon`.
9. Hệ thống thông báo lập phiếu nhập thành công.

**Luồng thay thế / ngoại lệ**

- **3a. Quản trị viên thêm nhiều sản phẩm hoặc xóa một dòng sản phẩm:** quản trị viên lặp lại thao tác thêm sản phẩm hoặc xóa dòng không cần nữa; hệ thống tính lại thành tiền và tổng tiền ngay trên giao diện.
- **6a. Form thiếu hoặc sai dữ liệu:** JavaScript chặn gửi form nếu quản trị viên chưa chọn nhà cung cấp, hoặc nhập số lượng hoặc giá nhỏ hơn hoặc bằng 0; hệ thống thông báo lỗi và giữ nguyên dữ liệu đã nhập.
- **6b. Dữ liệu không hợp lệ khi xác thực phía máy chủ:** hệ thống không lưu phiếu, thông báo lỗi nghiệp vụ và hiển thị lại form.
- **7a. Lỗi khi lưu phiếu nhập hoặc chi tiết phiếu nhập:** hệ thống thông báo thao tác thất bại và hoàn tác toàn bộ giao dịch; không có dữ liệu nào được lưu.
- **8a. Lỗi cập nhật tồn kho:** hệ thống rollback toàn bộ transaction, không lưu phiếu nhập để đảm bảo tính nhất quán (ACID).
- **1a. Người dùng chưa đăng nhập hoặc không có quyền quản trị:** hệ thống từ chối truy cập theo cơ chế phân quyền của module TV1.

**Quy tắc nghiệp vụ**

- Phiếu nhập phải gắn với một nhà cung cấp và có ít nhất một dòng chi tiết sản phẩm.
- Số lượng nhập và giá nhập của mỗi dòng phải lớn hơn 0.
- Thành tiền của mỗi dòng bằng số lượng nhân giá nhập; tổng tiền của phiếu bằng tổng các thành tiền.
- Tồn kho chỉ được cộng khi phiếu nhập được lưu thành công; mỗi phiếu chỉ cộng tồn kho một lần.
- Việc lưu phiếu nhập, lưu chi tiết và cộng tồn kho phải nằm trong cùng một transaction, hoặc thành công toàn bộ, hoặc hoàn tác toàn bộ.
- Kiểm tra bằng JavaScript ở giao diện chỉ hỗ trợ người dùng; hệ thống vẫn phải xác thực dữ liệu ở phía máy chủ.

**Nơi cài đặt / đối chiếu**

- `AdminPhieuNhapController.luuPhieuNhap`
- `PhieuNhapServiceImpl.luuQuyTrinhNhapHang`
- `templates/admin/phieu-nhap/form.html`

---

## Các Use Case liên quan trong module TV2

- Xem danh sách nhà cung cấp.
- Thêm mới nhà cung cấp.
- Sửa thông tin nhà cung cấp.
- Xóa nhà cung cấp (có kiểm tra ràng buộc phiếu nhập).
- Tìm kiếm nhà cung cấp.
- Lập phiếu nhập kho.
- Cộng tồn kho tự động khi lưu phiếu nhập.
- Quản lý thương hiệu và xem cảnh báo kho (thuộc phạm vi module theo mô tả tác nhân; chưa được đặc tả chi tiết trong tài liệu này).

## Tài liệu đối chiếu

- `docs/chuc-nang.md`
- `docs/dac-ta-use-case-tv1.md`
- `AdminNhaCungCapController`, `AdminPhieuNhapController` và các service tương ứng (`NhaCungCapServiceImpl`, `PhieuNhapServiceImpl`).