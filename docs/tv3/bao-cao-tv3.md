# Báo cáo tiến độ, phần TV3: Sản phẩm và trưng bày

| Mục | Nội dung |
|---|---|
| Thành viên | Nguyễn Thanh Tuấn |
| MSSV | 2001240536 |
| Vai trò | TV3, module Sản phẩm và trưng bày |
| Bảng sở hữu | `SanPham` |
| Nhánh làm việc | `tv3-san-pham` |
| Kiểm thử chéo | Module của TV4 Ngô Hoàng Anh Phát (Giỏ hàng và Đặt hàng) |

## 1. Phạm vi công việc

Chức năng phải chạy được, theo bảng phân công của nhóm:

- CRUD sản phẩm có upload ảnh và thông số kỹ thuật.
- Trang chủ, danh sách sản phẩm theo loại hoặc theo hãng.
- Tìm kiếm, lọc theo giá, sắp xếp, phân trang.
- Chi tiết sản phẩm và sản phẩm cùng loại.

Phần báo cáo được giao: **Dữ liệu mẫu** (mục 6) và **Giao diện phía khách hàng** (mục 7).

Module không có quy trình nghiệp vụ riêng; bù lại là phần giao diện phía khách nặng nhất của website.

## 2. Use case

![Hình 1. Sơ đồ use case module Sản phẩm và trưng bày](use-case-tv3.png)

| Mã | Tên use case | Tác nhân |
|---|---|---|
| UC-SP-01 | Xem trang chủ | Khách |
| UC-SP-02 | Xem sản phẩm theo loại hoặc thương hiệu | Khách |
| UC-SP-03 | Tìm kiếm sản phẩm | Khách |
| UC-SP-04 | Lọc, sắp xếp và phân trang | Khách |
| UC-SP-05 | Xem chi tiết sản phẩm, gồm xem sản phẩm cùng loại | Khách |
| UC-SP-07 | Xem và tìm sản phẩm phía quản trị | Quản trị viên |
| UC-SP-08 | Thêm sản phẩm, có tải ảnh | Quản trị viên |
| UC-SP-09 | Sửa sản phẩm, có thay hoặc bỏ ảnh | Quản trị viên |
| UC-SP-10 | Xoá sản phẩm | Quản trị viên |
| UC-SP-11 | Ngừng bán hoặc mở bán lại | Quản trị viên |

Đặc tả chi tiết hai use case chính **UC-SP-03 Tìm kiếm sản phẩm** và **UC-SP-08 Thêm sản phẩm**
nằm trong tệp `dac-ta-use-case-tv3.md` cùng thư mục.

## 3. Thiết kế dữ liệu bảng SanPham

Quan hệ: `SanPham` n–1 `LoaiSanPham` và n–1 `ThuongHieu` (`@ManyToOne`). Bảng được `ChiTietDonHang` (TV4)
và `ChiTietPhieuNhap` (TV2) tham chiếu nên sản phẩm đã bán hoặc đã nhập kho không được xoá.

| Cột | Kiểu | Ràng buộc | Ý nghĩa |
|---|---|---|---|
| `id` | bigint | Khoá chính, tự tăng | Mã nội bộ |
| `ma_san_pham` | varchar(50) | Duy nhất, bắt buộc, chỉ chữ không dấu, số, gạch ngang | Mã hàng, ví dụ `BDT-BOS-001` |
| `ten_san_pham` | varchar(200) | Bắt buộc | Tên hiển thị |
| `gia_ban` | bigint | Bắt buộc, lớn hơn 0 | Giá niêm yết (đồng) |
| `gia_khuyen_mai` | bigint | Có thể trống, nhỏ hơn giá bán | Giá sau giảm |
| `so_luong_ton` | int | Không âm | Tồn kho |
| `hinh_anh` | varchar(255) | Có thể trống | Tên tệp ảnh trong `uploads/`; trống thì dùng ảnh nhóm hàng |
| `cong_suat` | varchar(100) | Có thể trống | Công suất hoặc dung tích |
| `bao_hanh_thang` | int | 0 tới 120 | Số tháng bảo hành |
| `mo_ta` | varchar(1000) | Có thể trống | Mô tả, xuống dòng để tách đoạn |
| `thong_so_ky_thuat` | varchar(2000) | Có thể trống | Mỗi dòng `Tên: Giá trị`; dòng không có dấu hai chấm là một tính năng |
| `dang_ban` | bit | Mặc định 1 | 0 thì ẩn khỏi cửa hàng |
| `loai_san_pham_id` | bigint | Khoá ngoại, bắt buộc | Thuộc loại nào |
| `thuong_hieu_id` | bigint | Khoá ngoại, bắt buộc | Thuộc thương hiệu nào |

Ngoài ra lớp `SanPham` có thuộc tính `giaBanThucTe` dùng `@Formula`: Hibernate tính ngay trong câu SQL
"có giá khuyến mãi thì lấy giá khuyến mãi, không thì lấy giá bán", không lưu thành cột. Nhờ vậy lọc theo
mức giá và sắp xếp theo giá đều chạy trong cơ sở dữ liệu, kể cả khi có phân trang.

## 4. Mô tả chức năng và nơi cài đặt

Mỗi chức năng đi đúng khuôn Spring MVC CRUD của nhóm:
`Entity` → `JpaRepository` → `Service` + `ServiceImpl` → `Controller` → view Thymeleaf.

| Chức năng | Đường dẫn | Cài đặt chính |
|---|---|---|
| Trang chủ: danh mục, sản phẩm giảm giá nhiều nhất, sản phẩm mới, logo đối tác dẫn tới sản phẩm theo hãng | `GET /` | `TrangChuController`, `SanPhamService.laySanPhamGiamGia`, `laySanPhamMoi`, `index.html` |
| Chọn danh mục | `GET /san-pham` | `SanPhamController.danhSach`, `san-pham.html` |
| Danh sách theo loại, theo hãng | `GET /san-pham?loai=1`, `?hang=1` | `SanPhamController.danhSach`, `DieuKienSanPham.thuocLoai`, `thuocCacHang` |
| Tìm kiếm theo tên, mã, thương hiệu | `GET /san-pham?tuKhoa=...` | `DieuKienSanPham.chuaTuKhoa` |
| Lọc mức giá, sắp xếp, phân trang 9 sản phẩm | `?mucGia=...&sapXep=...&trang=...` | `MucGia`, `KieuSapXep`, `BoLocSanPham`, `KetQuaPhanTrang`, `SanPhamServiceImpl.timKiemPhiaKhach` |
| Đổi kết quả tại chỗ khi tick lọc hoặc bấm trang | `GET /san-pham/ket-qua` | `SanPhamController.ketQuaLoc` trả fragment `san-pham :: ketQua`; `site.js` thay vào trang |
| Chi tiết sản phẩm, tab mô tả, tính năng, thông số, sản phẩm cùng loại | `GET /san-pham/{id}` | `SanPhamController.chiTiet`, `chi-tiet-san-pham.html` |
| Danh sách quản trị, tìm, lọc, phân trang 10 dòng | `GET /admin/san-pham` | `AdminSanPhamController.danhSach`, `SanPhamServiceImpl.timKiemQuanTri` |
| Thêm, sửa, upload ảnh | `GET /admin/san-pham/them`, `/sua/{id}`, `POST /luu` | `AdminSanPhamController.luu`, `SanPhamServiceImpl.luu`, `XuLyLoiTaiAnh` |
| Xoá, ngừng bán, mở bán lại | `POST /admin/san-pham/xoa/{id}`, `/doi-trang-thai/{id}` | `SanPhamServiceImpl.xoa`, `doiTrangThaiBan` |

Một số điểm kỹ thuật đáng chú ý:

- **Lọc động bằng Specification.** Mỗi điều kiện (loại, thương hiệu, mức giá, từ khoá, trạng thái) là một
  `Specification<SanPham>` trong `DieuKienSanPham`, ghép bằng `and()`. Điều kiện không chọn trả về "luôn đúng",
  nên không phải viết hàng chục câu truy vấn cho từng tổ hợp bộ lọc. `findAll(dieuKien, PageRequest)` vừa lọc,
  vừa sắp xếp, vừa phân trang ngay trong MySQL.
- **Tìm theo từng từ.** "bep tu bosch" tách thành 3 từ, sản phẩm phải chứa đủ cả 3 ở tên, mã hoặc tên hãng.
  Collation của MySQL bỏ qua dấu thanh và dấu mũ; riêng chữ "đ" một số collation coi khác "d" nên hệ thống
  đổi "đ" thành "d" ở cả từ khoá lẫn dữ liệu trước khi so. Nhờ vậy gõ "bep dien tu" vẫn ra "Bếp điện từ".
- **Upload ảnh an toàn.** Chỉ nhận JPG, PNG, WEBP, GIF tối đa 5 MB; tên tệp do hệ thống đặt lại; sửa ảnh thì
  xoá ảnh cũ; lưu cơ sở dữ liệu lỗi thì xoá ảnh vừa lưu.
- **Không xoá mất lịch sử.** Sản phẩm đã có trong đơn hàng hoặc phiếu nhập thì không xoá được, quản trị viên
  chuyển sang "Ngừng bán": sản phẩm biến mất khỏi cửa hàng nhưng đơn cũ vẫn còn đủ thông tin.
- **Sửa lỗi cấu hình chung phát hiện khi làm.** Tomcat bản mới chỉ nhận 10 phần trong một form
  `multipart/form-data`, form sản phẩm có 17 ô nên lưu bị lỗi 500. Đã thêm `server.tomcat.max-part-count=50`
  vào `application.properties`. Các ô lọc ở trang danh sách cũ có thuộc tính `data-tu-gui` nhưng `site.js`
  không xử lý thuộc tính này nên tick không có tác dụng; nay form dùng `data-lay` đúng như `site.js` cần.

## 5. Màn hình thực thi

Ảnh chụp từ ứng dụng chạy thật bằng Chrome, khung 1440px (trừ Hình 15 là khung 520px).

### 5.1. Phía cửa hàng

![Hình 2. Trang chủ: danh mục, sản phẩm đang giảm giá, sản phẩm mới, đối tác](man-hinh/01-trang-chu.jpg)

![Hình 3. Trang chọn danh mục /san-pham](man-hinh/02-chon-danh-muc.jpg)

![Hình 4. Danh sách sản phẩm theo loại Bếp điện từ](man-hinh/03-danh-sach-theo-loai.jpg)

![Hình 5. Danh sách sản phẩm theo thương hiệu Bosch, bấm từ logo đối tác trên trang chủ](man-hinh/04-danh-sach-theo-hang.jpg)

![Hình 6. Lọc Bosch, mức giá trên 20 triệu, sắp xếp giá giảm dần, kết quả đổi tại chỗ](man-hinh/05-loc-thuong-hieu-muc-gia-sap-xep.jpg)

![Hình 7. Phân trang, đang ở trang 2 của danh sách sắp theo giá tăng dần](man-hinh/06-phan-trang-trang-2.jpg)

![Hình 8. Tìm kiếm không dấu "lo vi song" từ ô tìm trên thanh menu](man-hinh/07-tim-kiem.jpg)

![Hình 9. Tìm kiếm không có kết quả, hiện khung trống và nút quay lại danh mục](man-hinh/08-tim-kiem-khong-co-ket-qua.jpg)

![Hình 10. Chi tiết sản phẩm và sản phẩm cùng loại](man-hinh/09-chi-tiet-san-pham.jpg)

![Hình 11. Tab Tính năng, icon chọn theo từ khoá của tính năng](man-hinh/10-chi-tiet-tab-tinh-nang.jpg)

![Hình 12. Tab Thông số kỹ thuật](man-hinh/11-chi-tiet-tab-thong-so.jpg)

![Hình 13. Sản phẩm tạm hết hàng, nút thêm vào giỏ bị khoá](man-hinh/12-chi-tiet-het-hang.jpg)

![Hình 14. Sản phẩm vừa thêm ở phía quản trị hiện ngay trên cửa hàng với ảnh đã upload](man-hinh/23-chi-tiet-san-pham-vua-them.jpg)

![Hình 15. Danh sách sản phẩm ở khung điện thoại 520px, bộ lọc tự thu gọn](man-hinh/13-man-hep-danh-sach.jpg)

### 5.2. Phía quản trị

![Hình 16. Danh sách sản phẩm phía quản trị](man-hinh/14-qt-danh-sach-san-pham.jpg)

![Hình 17. Lọc theo từ khoá "bosch", loại Máy rửa chén, trạng thái đang bán](man-hinh/15-qt-loc-san-pham.jpg)

![Hình 18. Biểu mẫu thêm sản phẩm](man-hinh/16-qt-form-them.jpg)

![Hình 19. Bấm Lưu khi bỏ trống, lỗi hiện dưới từng ô](man-hinh/17-qt-form-bao-loi.jpg)

![Hình 20. Biểu mẫu đã điền đủ, có chọn ảnh](man-hinh/18-qt-form-dien-du-lieu.jpg)

![Hình 21. Thêm thành công, sản phẩm mới nằm đầu danh sách](man-hinh/19-qt-them-thanh-cong.jpg)

![Hình 22. Báo trùng mã sản phẩm](man-hinh/20-qt-bao-trung-ma.jpg)

![Hình 23. Biểu mẫu sửa, hiện ảnh đang dùng và tuỳ chọn bỏ ảnh](man-hinh/21-qt-form-sua.jpg)

![Hình 24. Chặn xoá sản phẩm đã nằm trong đơn hàng](man-hinh/22-qt-chan-xoa.jpg)

## 6. Dữ liệu mẫu

Tệp `src/main/resources/data.sql` chạy mỗi lần khởi động (`spring.sql.init.mode=always`), dùng
`INSERT IGNORE` theo khoá chính nên chạy lại nhiều lần không bị trùng. Muốn nạp lại từ đầu thì xoá cơ sở dữ liệu
`dien_gia_dung` rồi chạy lại ứng dụng.

| Bảng | Số dòng | Ghi chú |
|---|---|---|
| `loai_san_pham` | 6 | Bếp điện từ, bếp hồng ngoại, máy hút mùi, máy rửa chén, lò nướng, lò vi sóng; mỗi loại có sẵn ảnh danh mục và ảnh banner |
| `thuong_hieu` | 4 | Bosch, Kaff, Kocher (Đức), Chef's (Việt Nam) |
| `nha_cung_cap` | 3 | Dùng cho phiếu nhập của TV2 |
| `san_pham` | 36 | 35 đang bán, 1 ngừng bán |
| `tai_khoan` | 2 | `admin` / `admin123` và `khach1` / `khach123`, tạo bởi `KhoiTaoDuLieu` (TV1) |

Phân bố 36 sản phẩm, chọn để mọi bộ lọc và mọi trạng thái giao diện đều có dữ liệu để thử:

| Theo loại | Số SP | | Theo hãng | Số SP | | Theo mức giá (35 SP đang bán) | Số SP |
|---|---|---|---|---|---|---|---|
| Bếp điện từ | 6 | | Bosch | 11 | | Dưới 5 triệu | 6 |
| Bếp hồng ngoại | 5 | | Kaff | 11 | | Từ 5 tới 10 triệu | 10 |
| Máy hút mùi | 6 | | Kocher | 7 | | Từ 10 tới 20 triệu | 13 |
| Máy rửa chén | 6 | | Chef's | 7 | | Trên 20 triệu | 6 |
| Lò nướng | 6 | | | | | | |
| Lò vi sóng | 7 | | | | | | |

Các trường hợp đặc biệt cố ý đưa vào:

- Sản phẩm **hết hàng**: `BHN-KOC-002` (tồn 0), hiện nhãn "Tạm hết hàng" và khoá nút thêm vào giỏ.
- Sản phẩm **sắp hết hàng** (tồn từ 1 tới 5): 8 sản phẩm, ví dụ `HM-BOS-002` còn 2, dùng cho cảnh báo của TV2.
- Sản phẩm **ngừng bán**: `LVS-CHE-002`, không hiện ở cửa hàng, vẫn thấy ở trang quản trị.
- Sản phẩm **không giảm giá**: `BHN-KAF-002`, `BHN-KOC-002`, `LVS-BOS-002`, chỉ hiện một giá.
- Thông số kỹ thuật viết dạng `Tên: Giá trị`, xen vài dòng tính năng không có dấu hai chấm
  (ví dụ "Khoá an toàn trẻ em", "Hẹn giờ 99 phút") để tab Tính năng có nội dung.
- Có đủ 4 thương hiệu trong hầu hết các loại để thử lọc nhiều hãng cùng lúc.

## 7. Giao diện phía khách hàng

Giao diện dùng chung hệ thống của nhóm (`site.css`, `ung-dung.css`, các fragment trong `layout/fragments.html`),
không thêm thư viện nào. Nền tối `#131313`, chữ kem, nhấn vàng đồng `#CBAA7D`, font Montserrat nhúng sẵn.

Các trang phía khách do TV3 làm:

| Trang | Thành phần chính |
|---|---|
| Trang chủ `/` | Banner trình chiếu, dải danh mục đổi ảnh nền khi rê chuột, khối "Đang giảm giá" (6 sản phẩm giảm sâu nhất), khối "Sản phẩm mới" (6 sản phẩm), dải logo đối tác bấm được, banner cuối |
| Chọn danh mục `/san-pham` | Lưới 6 ô danh mục có ảnh, nút "Xem tất cả sản phẩm" |
| Danh sách `/san-pham?...` | Banner nhóm hàng và đường dẫn phân cấp, cột bộ lọc (danh mục, thương hiệu, mức giá), thanh đếm kết quả và sắp xếp, lưới 3 cột, thanh phân trang |
| Chi tiết `/san-pham/{id}` | Bộ ảnh có ảnh nhỏ để đổi, nhãn giảm giá, giá và số tiền tiết kiệm, tình trạng kho, điểm nổi bật, ô số lượng và nút thêm vào giỏ, cam kết bảo hành, 3 tab nội dung, khối sản phẩm cùng loại |

Thẻ sản phẩm được viết một lần ở `layout/the-san-pham.html` và dùng lại ở cả ba trang, nên sửa một chỗ là
mọi nơi đổi theo. Nhãn phần trăm giảm giá, giá gạch ngang và nhãn "Tạm hết hàng" đều tự tính từ dữ liệu.

Những điểm đã kiểm tra theo mục 8 của `QUY_TAC_GIAO_DIEN.md`:

- Chạy đúng ở khung 1440px và khi thu còn 520px; ở khung hẹp bộ lọc tự thu gọn, lưới còn 2 cột, không tràn ngang.
- Trang nào cũng có thanh menu, chân trang và ngăn kéo menu điện thoại.
- Mọi danh sách có nhánh rỗng: không có kết quả tìm kiếm, chưa có sản phẩm giảm giá, nhóm hàng chỉ có một sản phẩm.
- Biểu mẫu báo lỗi dưới từng ô; câu lỗi nói rõ vì sao và làm gì tiếp.
- Không có class lạ ngoài hai tệp CSS chung; lớp mới của TV3 nằm cuối `ung-dung.css` dưới dòng chú thích `TV3`.
- Đường dẫn phân cấp có `aria-current`, ảnh có `alt`, nút phân trang có `aria-label`; người dùng bật
  "giảm chuyển động" thì hiệu ứng tắt.
- Không có lỗi JavaScript trên console khi thử lọc, phân trang, đổi tab, đổi ảnh.

## 8. Kiểm thử phần TV3

Ngoài 8 unit test chạy bằng `mvnw test` (tách thông số, tính giá, đường dẫn ảnh, bộ lọc, phân trang),
các ca kiểm thử dưới đây được chạy trên ứng dụng thật với cơ sở dữ liệu mới tạo.

| Mã | Ca kiểm thử | Kết quả mong đợi | Kết quả |
|---|---|---|---|
| TC-SP-01 | Mở `/san-pham?sapXep=gia-tang` | 35 sản phẩm, 4 trang, giá tăng dần, không có sản phẩm ngừng bán | Đạt |
| TC-SP-02 | Tick Bosch, rồi tick "Trên 20 triệu" | Còn 11 rồi 6 sản phẩm, không tải lại trang, địa chỉ đổi theo | Đạt |
| TC-SP-03 | Chọn mức "Từ 10 tới 20 triệu" và "Trên 20 triệu" trong loại Bếp điện từ, giá giảm dần | 5 sản phẩm, giá giảm dần | Đạt |
| TC-SP-04 | Tìm "bep tu bosch" và "bep dien tu" (không dấu, có chữ đ) | 2 bếp điện từ Bosch; 6 bếp điện từ | Đạt |
| TC-SP-05 | Tìm "%" | Không lỗi, không trả về toàn bộ sản phẩm | Đạt |
| TC-SP-06 | Gõ tay `trang=99` | Trả về trang cuối, không trang rỗng | Đạt |
| TC-SP-07 | Mở `/san-pham/9999`, `/san-pham?loai=999`, sản phẩm ngừng bán | Trang 404 của website | Đạt |
| TC-SP-08 | Đổi ảnh nhỏ, đổi tab ở trang chi tiết | Ảnh lớn đổi, tab đổi, không lỗi console | Đạt |
| TC-SP-09 | Tài khoản `khach1` mở hoặc gửi POST tới `/admin/san-pham` | Chuyển về `/dang-nhap?loi=quyen` | Đạt |
| TC-SP-10 | Lưu biểu mẫu trống | Lỗi dưới tên, mã, loại, thương hiệu, giá bán | Đạt |
| TC-SP-11 | Mã có dấu và khoảng trắng, giá là chữ, bảo hành 200 tháng | 3 lỗi tiếng Việt dưới từng ô | Đạt |
| TC-SP-12 | Mã trùng `bdt-bos-001` (chữ thường) | Báo trùng mã | Đạt |
| TC-SP-13 | Giá khuyến mãi bằng giá bán | Báo giá khuyến mãi phải nhỏ hơn giá bán | Đạt |
| TC-SP-14 | Chọn tệp `.txt` làm ảnh | Báo sai định dạng, không lưu | Đạt |
| TC-SP-15 | Chọn ảnh 7 MB | Quay về biểu mẫu, báo ảnh vượt quá 5 MB | Đạt |
| TC-SP-16 | Thêm sản phẩm đủ thông tin, có ảnh PNG | Lưu thành công, ảnh nằm trong `uploads/`, hiện trên cửa hàng | Đạt |
| TC-SP-17 | Sửa không chọn ảnh, rồi chọn ảnh mới, rồi tick bỏ ảnh | Giữ ảnh, thay ảnh và xoá tệp cũ, quay về ảnh nhóm hàng và xoá tệp | Đạt |
| TC-SP-18 | Ngừng bán rồi mở bán lại | Khách mở chi tiết nhận 404, mở bán lại thì xem được | Đạt |
| TC-SP-19 | Xoá sản phẩm đã có trong đơn hàng | Báo không xoá được, gợi ý ngừng bán | Đạt |
| TC-SP-20 | Xoá sản phẩm chưa bán | Xoá thành công, tệp ảnh bị xoá theo | Đạt |

## 9. Mức độ hoàn thành

| Chức năng | Mức độ | Ghi chú |
|---|---|---|
| CRUD sản phẩm có upload ảnh và thông số kỹ thuật | 100% | Thêm, sửa, xoá, ngừng bán, mở bán, upload và thay ảnh |
| Trang chủ, danh sách theo loại, theo hãng | 100% | |
| Tìm kiếm, lọc theo giá, sắp xếp, phân trang | 100% | Lọc đổi kết quả tại chỗ, giữ được nút Quay lại của trình duyệt |
| Chi tiết sản phẩm và sản phẩm cùng loại | 100% | Nút "Thêm vào giỏ hàng" gửi `POST /gio-hang/them` với `id` và `soLuong`, chờ TV4 làm phần nhận |
| Dữ liệu mẫu | 100% | 36 sản phẩm, đủ trường hợp đặc biệt |
| Sơ đồ use case, đặc tả 2 use case | 100% | `use-case-tv3.png`, `dac-ta-use-case-tv3.md` |
| Mockup 3 màn hình chính | 100% | Thư mục `mockup/` |

Việc theo tiến độ tuần của nhóm:

| Tuần | Việc | Trạng thái |
|---|---|---|
| 5 | Cài môi trường và chạy được project khung | Xong |
| 5 | Liệt kê chức năng của module vào `docs/chuc-nang.md` | Xong |
| 6 | Vẽ mockup 2 tới 3 màn hình chính | Xong |
| 6 | Viết Entity và Repository cho bảng sở hữu | Xong |
| 6 | Sơ đồ use case và đặc tả 2 use case chính | Xong |
| 7 | Có chức năng chạy thật, tạo Pull Request vào `main` | Xong, chờ nhóm trưởng duyệt |
| 7 | Kiểm thử chéo module TV4 | Đã viết bộ ca kiểm thử; module TV4 chưa có trên `main` nên chưa chạy được, xem `kiem-thu-cheo-tv4.md` |
| 8 | Chụp màn hình và viết phần của mình | Xong (tài liệu này) |
| 8 | Tập demo | Kịch bản trong `kich-ban-demo-tv3.md` |

## 10. Kế hoạch tuần 9 tới 14

| Tuần | Việc dự kiến |
|---|---|
| 9 | Sửa lỗi do TV2 báo khi kiểm thử chéo module TV3; cùng TV4 nối nút "Thêm vào giỏ hàng" |
| 10 | Chạy bộ ca kiểm thử chéo cho module TV4 khi TV4 merge, ghi lỗi thành GitHub Issue |
| 11 | Cho phép nhiều ảnh cho một sản phẩm, bộ ảnh ở trang chi tiết dùng ảnh thật thay cho ảnh nhóm hàng |
| 12 | Khối "Sản phẩm bán chạy" trên trang chủ dựa vào số liệu đơn hàng (phối hợp TV5) |
| 13 | Rà soát giao diện toàn website theo `QUY_TAC_GIAO_DIEN.md`, viết mục giao diện cho báo cáo cuối kỳ |
| 14 | Hoàn thiện báo cáo, tập demo cả nhóm |
