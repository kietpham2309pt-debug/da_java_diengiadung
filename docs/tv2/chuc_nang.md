# Mô tả chức năng

## 2. Quản lý đối tác và nhập kho (TV2 - Lê Quốc Việt)

**Vai trò:** Xây dựng module quản lý nguồn cung (Thương hiệu, Nhà cung cấp) và quy trình nhập hàng, kiểm soát tồn kho.

**Tác nhân sử dụng:** Quản trị viên (vai trò `ADMIN`) và thủ kho thông qua khu vực quản trị. Việc phân quyền truy cập do module TV1 đảm nhiệm.

### Tổng quan chức năng

| Mã | Nhóm | Chức năng | Mục đích |
|---|---|---|---|
| TV2-F01 | Thương hiệu & Nhà cung cấp | Xem danh sách Nhà cung cấp | Theo dõi các đối tác cung ứng hàng hóa |
| TV2-F02 | Thương hiệu & Nhà cung cấp | Tìm kiếm Nhà cung cấp theo từ khóa | Tra cứu nhanh nhà cung cấp |
| TV2-F03 | Thương hiệu & Nhà cung cấp | Thêm mới, chỉnh sửa Nhà cung cấp | Duy trì thông tin đối tác chính xác |
| TV2-F04 | Thương hiệu & Nhà cung cấp | Thêm mới, chỉnh sửa Thương hiệu | Duy trì danh mục thương hiệu của sản phẩm |
| TV2-F05 | Thương hiệu & Nhà cung cấp | Xóa Nhà cung cấp (có ràng buộc) | Xóa an toàn, bảo vệ lịch sử nhập hàng |
| TV2-F06 | Thương hiệu & Nhà cung cấp | Xóa Thương hiệu (có ràng buộc) | Xóa an toàn, bảo vệ dữ liệu sản phẩm |
| TV2-F07 | Nhập hàng & Tồn kho | Lập phiếu nhập bằng form động | Ghi nhận hàng nhập từ nhà cung cấp |
| TV2-F08 | Nhập hàng & Tồn kho | Lưu phiếu nhập theo giao dịch | Đảm bảo phiếu và chi tiết được lưu nhất quán |
| TV2-F09 | Nhập hàng & Tồn kho | Tự động cộng tồn kho | Cập nhật `soLuongTon` ngay khi nhập hàng |
| TV2-F10 | Nhập hàng & Tồn kho | Cảnh báo sản phẩm sắp hết hàng | Nhắc thủ kho nhập thêm hàng kịp thời |

---

### 2.1. Quản lý Thương hiệu và Nhà cung cấp

#### 2.1.1. Tra cứu và quản lý (TV2-F01 đến TV2-F04)

**Xem danh sách và tìm kiếm Nhà cung cấp**

- Quản trị viên mở chức năng **Nhà cung cấp** để xem toàn bộ danh sách nhà cung cấp hiện có.
- Quản trị viên nhập **từ khóa** vào ô tìm kiếm để lọc danh sách; hệ thống chỉ hiển thị các nhà cung cấp phù hợp với từ khóa.
- Khi không có kết quả phù hợp, hệ thống hiển thị danh sách rỗng hoặc thông báo không có dữ liệu.
- Khi từ khóa để trống, hệ thống hiển thị toàn bộ nhà cung cấp.

**Thêm mới và chỉnh sửa Nhà cung cấp, Thương hiệu**

- Quản trị viên chọn **Thêm mới** để nhập thông tin một nhà cung cấp hoặc một thương hiệu mới, hoặc chọn **Sửa** trên một bản ghi có sẵn để chỉnh sửa.
- Hệ thống hiển thị form với dữ liệu hiện tại (khi sửa) hoặc form trống (khi thêm mới).
- Sau khi quản trị viên bấm lưu, hệ thống xác thực dữ liệu, lưu vào cơ sở dữ liệu, thông báo thành công và hiển thị lại danh sách đã cập nhật.

#### 2.1.2. Ràng buộc nghiệp vụ (TV2-F05, TV2-F06)

**Xác thực dữ liệu đầu vào**

- Dữ liệu đầu vào được xác thực bằng `@Valid` (Bean Validation) trước khi lưu, ví dụ tên không được để trống.
- Khi dữ liệu không hợp lệ, hệ thống không lưu, hiển thị thông báo lỗi tại trường tương ứng và giữ nguyên dữ liệu quản trị viên đã nhập.

**Ràng buộc khi xóa**

| Đối tượng | Điều kiện chặn xóa | Kết quả khi vi phạm |
|---|---|---|
| Thương hiệu | Thương hiệu đang có Sản phẩm | Từ chối xóa, hiển thị cảnh báo, dữ liệu giữ nguyên |
| Nhà cung cấp | Nhà cung cấp đã có Phiếu nhập | Từ chối xóa, hiển thị cảnh báo, dữ liệu giữ nguyên |

- Khi đối tượng không vướng ràng buộc, hệ thống tiến hành xóa và thông báo thành công.
- Việc kiểm tra ràng buộc được thực hiện ở tầng service; khi vi phạm, hệ thống phát sinh `LoiNghiepVu`.
- Thông báo lỗi nghiệp vụ được hiển thị trực quan trên giao diện (cảnh báo màu đỏ) để quản trị viên biết lý do bị từ chối.
- Mục đích: bảo toàn toàn vẹn dữ liệu, tránh mất lịch sử nhập hàng hoặc làm sản phẩm mất liên kết thương hiệu.

---

### 2.2. Lập Phiếu nhập và Kiểm soát Tồn kho

#### 2.2.1. Giao diện lập phiếu động (TV2-F07)

- Form **Lập phiếu nhập** gồm phần thông tin chung (chọn nhà cung cấp) và bảng chi tiết các sản phẩm nhập.
- Quản trị viên bấm **Thêm sản phẩm** để thêm một dòng chi tiết; trên mỗi dòng chọn sản phẩm, nhập số lượng và giá nhập.
- Quản trị viên có thể **xóa dòng** chi tiết không cần thiết ngay trên form, không cần tải lại trang.
- JavaScript **tự động tính thành tiền** của từng dòng (số lượng nhân giá nhập) và **tổng tiền** của phiếu, cập nhật tức thời khi dữ liệu thay đổi.
- JavaScript chặn gửi form khi chưa chọn nhà cung cấp, hoặc khi số lượng hoặc giá nhập nhỏ hơn hoặc bằng 0, đồng thời thông báo lỗi cho người dùng.
- Kiểm tra bằng JavaScript chỉ hỗ trợ trải nghiệm người dùng; hệ thống vẫn xác thực lại dữ liệu ở phía máy chủ.

#### 2.2.2. Xử lý giao dịch (TV2-F08)

- Khi quản trị viên bấm **Lưu phiếu nhập**, hệ thống lưu **đồng thời** `PhieuNhap` và các `ChiTietPhieuNhap` vào cơ sở dữ liệu trong cùng một transaction.
- Nếu bất kỳ bước nào thất bại, toàn bộ giao dịch được hoàn tác (rollback): không có phiếu nhập hoặc chi tiết nào được lưu.
- Cơ chế này bảo đảm tính nhất quán dữ liệu theo nguyên tắc ACID.

#### 2.2.3. Tự động cộng tồn kho (TV2-F09)

- Khi phiếu nhập được lưu thành công, hệ thống **quét từng dòng chi tiết** của phiếu.
- Với mỗi dòng, hệ thống truy xuất sản phẩm tương ứng và **cộng dồn** `soLuong` nhập vào `soLuongTon` của sản phẩm.
- Bước cộng tồn kho nằm trong cùng transaction với việc lưu phiếu: nếu cập nhật tồn kho lỗi thì phiếu nhập cũng không được lưu.
- Mỗi phiếu nhập chỉ cộng tồn kho một lần, vào thời điểm lưu thành công.

#### 2.2.4. Cảnh báo sắp hết hàng (TV2-F10)

- Hệ thống cung cấp một **màn hình riêng** liệt kê các sản phẩm có tồn kho thấp.
- **Ngưỡng cảnh báo:** sản phẩm có `soLuongTon` ≤ 10.
- Mục đích: giúp thủ kho nhanh chóng nhận biết mặt hàng cần bổ sung và chủ động lập phiếu nhập mới.

---

### Tóm tắt quy tắc nghiệp vụ của module

1. Dữ liệu nhập vào Thương hiệu và Nhà cung cấp phải qua xác thực `@Valid`.
2. Không được xóa Thương hiệu đang có Sản phẩm.
3. Không được xóa Nhà cung cấp đã có Phiếu nhập.
4. Phiếu nhập phải gắn với một nhà cung cấp và có số lượng, giá nhập lớn hơn 0 trên mỗi dòng chi tiết.
5. Phiếu nhập và chi tiết phiếu nhập được lưu trong cùng một transaction với bước cộng tồn kho.
6. Tồn kho (`soLuongTon`) chỉ tăng khi phiếu nhập được lưu thành công.
7. Sản phẩm có tồn kho ≤ 10 được đưa vào danh sách cảnh báo sắp hết hàng.

### Đối chiếu

- Đặc tả chi tiết: `docs/dac-ta-use-case-tv2.md`
- Các thành phần liên quan: `AdminNhaCungCapController`, `NhaCungCapServiceImpl`, `AdminPhieuNhapController.luuPhieuNhap`, `PhieuNhapServiceImpl.luuQuyTrinhNhapHang`, `templates/admin/phieu-nhap/form.html`