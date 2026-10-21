# Đặc tả use case, module Sản phẩm và trưng bày (TV3)

Sơ đồ use case: `use-case-tv3.png` (nguồn `use-case-tv3.puml`).

Tác nhân của module:

| Tác nhân | Mô tả |
|---|---|
| Khách | Người vào website, chưa cần đăng nhập. Xem trang chủ, xem, tìm, lọc sản phẩm và xem chi tiết |
| Khách hàng | Tài khoản vai trò `KHACHHANG`, làm được mọi việc của Khách (kế thừa) |
| Quản trị viên | Tài khoản vai trò `ADMIN`, quản lý sản phẩm trong khu `/admin/san-pham` |

Danh sách use case:

| Mã | Tên | Tác nhân |
|---|---|---|
| UC-SP-01 | Xem trang chủ | Khách |
| UC-SP-02 | Xem sản phẩm theo loại hoặc thương hiệu | Khách |
| UC-SP-03 | Tìm kiếm sản phẩm | Khách |
| UC-SP-04 | Lọc, sắp xếp và phân trang (mở rộng UC-SP-02, UC-SP-03) | Khách |
| UC-SP-05 | Xem chi tiết sản phẩm (gồm UC-SP-06 Xem sản phẩm cùng loại) | Khách |
| UC-SP-07 | Xem và tìm sản phẩm phía quản trị | Quản trị viên |
| UC-SP-08 | Thêm sản phẩm (mở rộng bởi UC-SP-12 Tải ảnh sản phẩm) | Quản trị viên |
| UC-SP-09 | Sửa sản phẩm (mở rộng bởi UC-SP-12) | Quản trị viên |
| UC-SP-10 | Xoá sản phẩm | Quản trị viên |
| UC-SP-11 | Ngừng bán hoặc mở bán lại | Quản trị viên |

Mọi use case phía quản trị đều gồm use case *Kiểm tra quyền quản trị* của module TV1.

---

## UC-SP-03. Tìm kiếm sản phẩm

| Mục | Nội dung |
|---|---|
| **Mã** | UC-SP-03 |
| **Tên** | Tìm kiếm sản phẩm |
| **Tác nhân chính** | Khách |
| **Mô tả** | Khách gõ từ khoá để tìm sản phẩm theo tên, mã sản phẩm hoặc tên thương hiệu, sau đó có thể lọc, sắp xếp và chuyển trang kết quả |
| **Điều kiện trước** | Không cần đăng nhập. Cơ sở dữ liệu có sản phẩm đang bán (`dang_ban = 1`) |
| **Điều kiện sau** | Hệ thống hiện danh sách sản phẩm khớp từ khoá, kèm số lượng kết quả. Dữ liệu không thay đổi |

**Luồng chính**

1. Khách bấm biểu tượng kính lúp trên thanh menu ở bất kỳ trang nào.
2. Hệ thống mở ô tìm kiếm.
3. Khách gõ từ khoá, ví dụ `lo vi song`, rồi nhấn Enter.
4. Hệ thống gửi yêu cầu `GET /san-pham?tuKhoa=lo+vi+song`.
5. Hệ thống tách từ khoá thành từng từ. Một sản phẩm khớp khi **mỗi từ** đều xuất hiện ở tên, mã sản phẩm hoặc tên thương hiệu.
6. Hệ thống chỉ lấy sản phẩm đang bán, sắp xếp mới nhất trước, lấy 9 sản phẩm của trang 1.
7. Hệ thống hiện trang kết quả: dòng "Kết quả cho “lo vi song”, (6 sản phẩm)", lưới thẻ sản phẩm, bộ lọc bên trái và thanh phân trang nếu có hơn 9 kết quả.

**Luồng thay thế**

- **3a. Từ khoá có ký tự `%` hoặc `_`:** hệ thống coi đó là ký tự thường, không để câu `LIKE` hiểu thành ký tự đại diện.
- **5a. Không có sản phẩm nào khớp:** hệ thống hiện khung trống "Không tìm thấy sản phẩm nào khớp với lựa chọn của bạn" kèm nút "Xem tất cả danh mục".
- **7a. Khách tick thương hiệu hoặc mức giá, hoặc đổi kiểu sắp xếp (UC-SP-04):** `site.js` gọi `GET /san-pham/ket-qua?...` và thay khối kết quả tại chỗ, không tải lại cả trang. Đường dẫn trên thanh địa chỉ đổi theo nên bấm Quay lại của trình duyệt vẫn về đúng bộ lọc trước.
- **7b. Khách bấm số trang:** tương tự 7a, giữ nguyên từ khoá và bộ lọc, chỉ đổi tham số `trang`.
- **7c. Tham số `trang` lớn hơn số trang thật (gõ tay trên địa chỉ):** hệ thống trả về trang cuối cùng thay vì trang rỗng.
- **7d. Trình duyệt tắt JavaScript:** bộ lọc hiện nút "Áp dụng bộ lọc", bấm nút này gửi form theo cách thường.

**Quy tắc nghiệp vụ**

- Sản phẩm ngừng bán không bao giờ xuất hiện ở phía cửa hàng.
- Lọc nhiều thương hiệu là phép "hoặc" (Bosch hoặc Kaff); lọc nhiều mức giá cũng là "hoặc"; giữa thương hiệu, mức giá, từ khoá và loại là phép "và".
- Giá dùng để lọc và sắp xếp là giá khách thật sự trả: có giá khuyến mãi thì lấy giá khuyến mãi, không thì lấy giá bán.
- Bốn mức giá: dưới 5 triệu, 5 tới 10 triệu, 10 tới 20 triệu, trên 20 triệu. Cận dưới tính cả bằng, cận trên không tính.

**Nơi cài đặt**

`SanPhamController.danhSach`, `SanPhamController.ketQuaLoc`, `SanPhamServiceImpl.timKiemPhiaKhach`,
`DieuKienSanPham` (các điều kiện lọc động bằng Specification), `BoLocSanPham`, `KieuSapXep`, `MucGia`,
`SanPham.giaBanThucTe` (`@Formula`), `templates/san-pham.html` (fragment `ketQua`).

---

## UC-SP-08. Thêm sản phẩm

| Mục | Nội dung |
|---|---|
| **Mã** | UC-SP-08 |
| **Tên** | Thêm sản phẩm |
| **Tác nhân chính** | Quản trị viên |
| **Mô tả** | Quản trị viên nhập thông tin một sản phẩm mới, kèm ảnh và thông số kỹ thuật, để sản phẩm xuất hiện trên cửa hàng |
| **Điều kiện trước** | Quản trị viên đã đăng nhập. Đã có ít nhất một loại sản phẩm và một thương hiệu |
| **Điều kiện sau** | Bảng `san_pham` có thêm một dòng; nếu có ảnh thì tệp ảnh nằm trong thư mục `uploads/` và cột `hinh_anh` lưu tên tệp. Sản phẩm hiện ngay ở trang danh sách và trang chi tiết nếu đang bán |

**Luồng chính**

1. Quản trị viên mở `/admin/san-pham` rồi bấm **Thêm sản phẩm**.
2. Hệ thống kiểm tra quyền quản trị (use case *Kiểm tra quyền quản trị*), rồi hiện biểu mẫu trống, bảo hành mặc định 24 tháng, ô "Đang bán" đã tick.
3. Quản trị viên nhập tên, mã sản phẩm, công suất hoặc dung tích, chọn loại và thương hiệu, nhập giá bán, giá khuyến mãi, số lượng tồn, bảo hành, mô tả.
4. Quản trị viên nhập thông số kỹ thuật, mỗi dòng một thông số dạng `Tên: Giá trị`. Dòng không có dấu hai chấm được hiểu là một tính năng.
5. Quản trị viên chọn ảnh sản phẩm (use case mở rộng *Tải ảnh sản phẩm*).
6. Quản trị viên bấm **Lưu**.
7. Hệ thống kiểm tra các ràng buộc nhập liệu trên `SanPham` (`@NotBlank`, `@Min`, `@Pattern`, `@NotNull`...).
8. Hệ thống chuẩn hoá dữ liệu: mã viết hoa, bỏ khoảng trắng thừa, giá khuyến mãi bằng 0 coi như không giảm giá.
9. Hệ thống kiểm tra giá khuyến mãi nhỏ hơn giá bán và mã sản phẩm chưa có ai dùng.
10. Hệ thống kiểm tra tệp ảnh, đặt tên mới dạng `sp-<12 ký tự ngẫu nhiên>.<đuôi>` và lưu vào `uploads/`.
11. Hệ thống lưu sản phẩm vào cơ sở dữ liệu.
12. Hệ thống quay về danh sách và báo "Đã thêm sản phẩm BDT-KAF-DEMO".

**Luồng thay thế**

- **2a. Người dùng chưa đăng nhập hoặc không phải quản trị:** `QuanTriInterceptor` chặn và chuyển tới `/dang-nhap?loi=quyen`.
- **5a. Không chọn ảnh:** sản phẩm dùng ảnh của nhóm hàng (`/img/sp-<mã loại>.jpg`), hệ thống vẫn lưu bình thường.
- **7a. Bỏ trống ô bắt buộc, nhập chữ vào ô số, mã có dấu hoặc khoảng trắng, bảo hành quá 120 tháng:** hệ thống giữ nguyên biểu mẫu, hiện lỗi màu đỏ ngay dưới từng ô, ví dụ "Giá bán không được để trống", "Vui lòng nhập một số hợp lệ".
- **9a. Giá khuyến mãi lớn hơn hoặc bằng giá bán:** hệ thống báo "Giá khuyến mãi phải nhỏ hơn giá bán. Bỏ trống ô này nếu sản phẩm không giảm giá".
- **9b. Mã sản phẩm đã tồn tại (không phân biệt hoa thường):** hệ thống báo "Mã sản phẩm "BDT-BOS-001" đã được dùng cho sản phẩm khác".
- **10a. Tệp không phải ảnh JPG, PNG, WEBP, GIF:** hệ thống báo "Ảnh sản phẩm phải là tệp JPG, PNG, WEBP hoặc GIF" và không lưu gì.
- **10b. Ảnh lớn hơn 5 MB:** Spring chặn ngay khi nhận tệp; `XuLyLoiTaiAnh` bắt lỗi, quay về biểu mẫu và báo "Ảnh vượt quá 5 MB nên chưa lưu được. Hãy chọn ảnh nhỏ hơn rồi lưu lại".
- **11a. Lưu cơ sở dữ liệu thất bại:** hệ thống xoá tệp ảnh vừa lưu để không để lại ảnh rác, giao dịch được rollback.

**Quy tắc nghiệp vụ**

- Mã sản phẩm là duy nhất, chỉ gồm chữ không dấu, số và dấu gạch ngang.
- Giá khuyến mãi để trống hoặc nhỏ hơn giá bán; phần trăm giảm hiện trên thẻ sản phẩm được tính tự động.
- Chỉ nhận ảnh tối đa 5 MB. Tên tệp do hệ thống đặt, không dùng tên tệp gốc của người dùng.
- Khi sửa sản phẩm (UC-SP-09) và chọn ảnh mới, ảnh cũ bị xoá khỏi ổ đĩa; tick "Bỏ ảnh này" thì quay về ảnh của nhóm hàng.
- Số lượng tồn nhập ở đây là tồn ban đầu; về sau tồn kho tăng qua phiếu nhập (module TV2) và giảm khi đặt hàng (module TV4).

**Nơi cài đặt**

`AdminSanPhamController.formThem`, `AdminSanPhamController.luu`, `SanPhamServiceImpl.luu`
(`chuanHoa`, `kiemTraGia`, `kiemTraTrungMa`, `kiemTraTepAnh`, `luuTepAnh`, `xoaTepAnh`),
`XuLyLoiTaiAnh`, `templates/admin/san-pham/form.html`, cấu hình `spring.servlet.multipart.*` và
`server.tomcat.max-part-count` trong `application.properties`.
