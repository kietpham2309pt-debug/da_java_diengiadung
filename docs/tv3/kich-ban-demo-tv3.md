# Kịch bản demo phần TV3 (tối đa 1 phút) và câu hỏi chuẩn bị

## 1. Chuẩn bị trước buổi demo

1. Chạy ứng dụng bằng `mvnw spring-boot:run`, mở <http://localhost:8080>.
2. Mở sẵn hai tab: tab 1 là trang chủ, tab 2 đã đăng nhập `admin` / `admin123` ở `/admin/san-pham`.
3. Để sẵn một ảnh bếp hoặc lò (JPG, dưới 5 MB) ở Desktop để upload.
4. Nếu muốn dữ liệu sạch như lúc đầu: xoá cơ sở dữ liệu `dien_gia_dung` rồi chạy lại ứng dụng.

## 2. Lời nói kèm thao tác

| Thời gian | Thao tác | Nói |
|---|---|---|
| 0 tới 10 giây | Tab 1, cuộn trang chủ tới khối "Đang giảm giá" và "Sản phẩm mới" | "Em làm module Sản phẩm và trưng bày, sở hữu bảng SanPham. Trang chủ lấy 6 sản phẩm giảm sâu nhất và 6 sản phẩm mới nhất từ cơ sở dữ liệu." |
| 10 tới 25 giây | Bấm logo Bosch ở dải đối tác, rồi tick "Trên 20 triệu", đổi sắp xếp "Giá giảm dần", bấm trang 2 nếu có | "Danh sách lọc theo loại, thương hiệu, mức giá, sắp xếp và phân trang 9 sản phẩm. Kết quả đổi tại chỗ, không tải lại trang." |
| 25 tới 35 giây | Gõ "bep dien tu" vào ô tìm kiếm | "Tìm theo từng từ trên tên, mã và thương hiệu, gõ không dấu vẫn ra." |
| 35 tới 45 giây | Bấm một sản phẩm, đổi ảnh nhỏ, bấm tab Thông số kỹ thuật, cuộn xuống Sản phẩm cùng loại | "Trang chi tiết có thông số kỹ thuật tách từ dữ liệu và sản phẩm cùng loại." |
| 45 tới 60 giây | Tab 2, Thêm sản phẩm, để trống bấm Lưu cho thấy lỗi, rồi điền nhanh, chọn ảnh, Lưu; mở sản phẩm vừa thêm ở cửa hàng | "Phía quản trị có thêm, sửa, xoá, ngừng bán, upload ảnh tối đa 5 MB; sản phẩm đã có trong đơn hàng thì không xoá được." |

## 3. Câu hỏi thầy hoặc nhóm có thể hỏi

**Phân trang làm thế nào?**
Service tạo `PageRequest.of(trang - 1, 9, cachSapXep)` rồi gọi `duLieuSanPham.findAll(dieuKien, pageRequest)`.
Spring Data JPA sinh câu SQL lấy 9 dòng bằng `LIMIT ... OFFSET` và thêm một câu `COUNT` khi cần biết tổng số. Kết quả
`Page<SanPham>` được gói vào `KetQuaPhanTrang` để giao diện dùng số trang bắt đầu từ 1.

**Vì sao dùng Specification mà không viết `findBy...` hay `@Query`?**
Bộ lọc có 5 điều kiện tuỳ chọn (loại, nhiều thương hiệu, nhiều mức giá, từ khoá, trạng thái). Viết `findBy...` cho
mọi tổ hợp thì quá nhiều phương thức. Specification cho mỗi điều kiện một hàm nhỏ trong `DieuKienSanPham`, ghép bằng
`and()`; điều kiện nào không chọn thì trả về "luôn đúng". Repository chỉ cần `extends JpaSpecificationExecutor<SanPham>`.

**`@Formula` trên `giaBanThucTe` để làm gì?**
Giá khách trả là giá khuyến mãi nếu có, không thì giá bán. Muốn lọc theo mức giá và sắp xếp theo giá ngay trong SQL
(để phân trang đúng) thì cần một cột như vậy. `@Formula` cho Hibernate tự chèn biểu thức `case when ... end`
vào câu SQL, không phải thêm cột thật vào bảng.

**Quan hệ của bảng SanPham?**
`@ManyToOne` tới `LoaiSanPham` và `ThuongHieu` (nhiều sản phẩm thuộc một loại, một hãng). Ngược lại `LoaiSanPham` và
`ThuongHieu` có `@OneToMany(mappedBy = ...)`. `ChiTietDonHang` và `ChiTietPhieuNhap` cũng `@ManyToOne` tới `SanPham`.

**Upload ảnh lưu ở đâu, vì sao không lưu ảnh trong cơ sở dữ liệu?**
Lưu tệp vào thư mục `uploads/` (cấu hình `duongdan.anh`), cột `hinh_anh` chỉ lưu tên tệp. `WebConfig` ánh xạ
`/uploads/**` tới thư mục đó để trình duyệt tải được. Lưu tệp ngoài giúp cơ sở dữ liệu nhẹ, ảnh được trình duyệt
cache như tệp tĩnh.

**Làm sao chặn người dùng upload tệp nguy hiểm?**
Chỉ nhận đuôi JPG, PNG, WEBP, GIF và `Content-Type` bắt đầu bằng `image/`; tối đa 5 MB (vượt thì `XuLyLoiTaiAnh` bắt
`MaxUploadSizeExceededException`). Tên tệp do hệ thống đặt ngẫu nhiên nên không ghi đè được tệp khác và không chứa
đường dẫn lạ. Khu `/admin/**` chỉ quản trị viên vào được nhờ `QuanTriInterceptor`.

**Validation nằm ở đâu?**
Hai lớp. Ràng buộc từng ô (`@NotBlank`, `@Min`, `@Pattern`, `@NotNull`) đặt trên entity, controller dùng `@Valid` và
`BindingResult`, Thymeleaf hiện lỗi bằng `th:errors`. Ràng buộc nghiệp vụ (giá khuyến mãi nhỏ hơn giá bán, mã không
trùng, tệp đúng loại) nằm trong `SanPhamServiceImpl`, ném `LoiNghiepVu` và controller hiện thông báo.

**Vì sao sản phẩm đã bán không xoá được?**
Dòng `ChiTietDonHang` tham chiếu tới sản phẩm bằng khoá ngoại. Xoá sản phẩm sẽ làm đơn cũ mất thông tin (hoặc MySQL báo
lỗi khoá ngoại). Nên service đếm số dòng đơn hàng, phiếu nhập trước khi xoá; nếu có thì báo và gợi ý "Ngừng bán".

**Lọc đổi kết quả tại chỗ hoạt động thế nào?**
Form lọc có `data-lay="/san-pham/ket-qua"`. Khi tick ô hoặc đổi sắp xếp, `site.js` gọi `fetch` tới địa chỉ đó; controller
trả về đúng một fragment Thymeleaf `san-pham :: ketQua` (lưới sản phẩm và thanh phân trang); JavaScript thay khối cũ
bằng khối mới và đổi địa chỉ trên thanh địa chỉ bằng `history.pushState`. Tắt JavaScript thì form vẫn gửi theo cách thường.

**Thymeleaf fragment dùng ở đâu?**
`layout/the-san-pham.html :: theSanPham(sp)` là thẻ sản phẩm dùng chung cho trang chủ, danh sách và sản phẩm cùng loại.
Các phần chung của nhóm (đầu trang, thanh menu, chân trang, menu quản trị) cũng là fragment trong `layout/fragments.html`.

**Tìm kiếm không dấu làm sao?**
MySQL so sánh chuỗi theo collation không phân biệt dấu (ê, ư, ơ...). Riêng "đ" có collation coi khác "d", nên câu SQL
dùng `replace(lower(ten), 'đ', 'd')` và từ khoá cũng đổi "đ" thành "d" trước khi so.
