# Website quản lý bán hàng điện gia dụng

Đồ án học phần **Công nghệ Java** — Khoa Công nghệ Thông tin, Trường Đại học Công Thương TP.HCM, năm học 2026-2027.

## Thành viên nhóm

| Vai | MSSV | Họ tên | Mảng phụ trách |
|---|---|---|---|
| TV1 | 2001240237 | Phạm Tuấn Kiệt (nhóm trưởng) | Nền tảng, layout chung, tài khoản và phân quyền |
| TV2 | 2001240555 | Lê Quốc Việt | Loại sản phẩm, thương hiệu, nhà cung cấp, nhập kho |
| TV3 | 2001240536 | Nguyễn Thanh Tuấn | Sản phẩm, trang chủ, tìm kiếm, chi tiết sản phẩm |
| TV4 | 2001240258 | Ngô Hoàng Anh Phát | Giỏ hàng, đặt hàng, lịch sử đơn, hồ sơ khách hàng |
| TV5 | 2001240351 | Phạm Kim Hoàng Oanh | Quản lý đơn hàng, khách hàng, thống kê |

## Công nghệ

Java **JDK 17** · Spring Boot 3.5.3 · Spring MVC + Thymeleaf · **Bootstrap 5.3** (webjar) · REST API (JSON) ·
Spring Data JPA và `EntityManager` (Hibernate 6) · Bean Validation · **Microsoft SQL Server 2019** (driver `mssql-jdbc`) ·
BCrypt · `@Scheduled` + khoá bi quan JPA · `ServerSocket`/`Socket` · JUnit 5 + Mockito.

## Cài đặt và chạy

### 1. Chuẩn bị

- **JDK 17** (kiểm tra bằng `java -version`). Không cần cài Maven, dự án có sẵn `mvnw`.
- **SQL Server 2019** trở lên (bản Developer hoặc Express) và SQL Server Management Studio (SSMS).
- Trong *SQL Server Configuration Manager* bật **TCP/IP** cho instance, cổng **1433**, rồi khởi động lại dịch vụ SQL Server.
  Chế độ xác thực phải là *SQL Server and Windows Authentication mode*.

### 2. Tạo cơ sở dữ liệu

Mở SSMS bằng tài khoản quản trị, chạy lần lượt hai script trong thư mục `database/`:

| Script | Nội dung |
|---|---|
| `database/01_tao_csdl.sql` | Tạo CSDL `dien_gia_dung` (collation `Vietnamese_CI_AI`), login `bepne` / `BepNe@2026`, 14 bảng kèm khoá chính, khoá ngoại, ràng buộc duy nhất |
| `database/02_du_lieu_mau.sql` | Dữ liệu mẫu (bảng dưới) |

Nếu bỏ qua bước này thì chỉ cần tạo CSDL rỗng `dien_gia_dung` và login `bepne`: lần chạy đầu Hibernate tự tạo bảng,
`src/main/resources/data.sql` tự nạp đúng bộ dữ liệu mẫu trên (bảng nào đã có dữ liệu thì bỏ qua).

| Bảng chính | Số bản ghi mẫu |
|---|---|
| `san_pham` | 40 |
| `khach_hang` / `tai_khoan` | 20 / 20 (thêm `admin`, `khach1` tạo khi khởi động) |
| `don_hang` / `chi_tiet_don_hang` | 30 / 41 |
| `nha_cung_cap` | 20 |
| `phieu_nhap` / `chi_tiet_phieu_nhap` | 20 / 38 |
| `danh_gia` | 23 |
| Bảng danh mục `loai_san_pham`, `thuong_hieu`, `ma_giam_gia`, `khuyen_mai` | 6, 4, 4, 3 |

Muốn dùng tài khoản SQL khác thì đặt biến môi trường `DB_USER`, `DB_PASS` (hoặc `DB_URL`) trước khi chạy.

### 3. Chạy server (ứng dụng web + máy chủ kho)

```bash
mvnw spring-boot:run
```

- Web: <http://localhost:8080>
- Máy chủ kho (Socket): cổng **9090**, log ghi `Máy chủ kho đang lắng nghe cổng 9090`.

### 4. Chạy client kho (chương trình tách biệt)

Client nằm trong thư mục `client-kho/`, chỉ cần JDK 17, không phụ thuộc Spring:

```bat
client-kho\chay-client.bat                          :: một client, menu tiếng Việt
client-kho\chay-3-client.bat                        :: mở 3 cửa sổ client cùng lúc
client-kho\thu-nhieu-client.bat localhost 9090 5 4  :: 5 client song song, mỗi client nhập kho 4 lần rồi đối chiếu tồn kho
```

Hoặc chạy trực tiếp: `java -Dfile.encoding=UTF-8 client-kho/KhoClient.java localhost 9090`.

## Tài khoản có sẵn

| Vai trò | Tên đăng nhập | Mật khẩu |
|---|---|---|
| Quản trị | `admin` | `admin123` |
| Khách hàng | `khach1` | `khach123` |
| Khách hàng mẫu | `kh101` … `kh120` | `khach123` |

Mật khẩu lưu trong cơ sở dữ liệu ở dạng mã hoá BCrypt. Khi triển khai thật phải đổi các tài khoản này.

## Xử lý đồng thời

| Chức năng | Cách làm | Mã nguồn |
|---|---|---|
| Nhiều khách cùng mua món hàng cuối | Khoá bi quan `@Lock(PESSIMISTIC_WRITE)` trên dòng sản phẩm khi trừ tồn (SQL Server sinh `WITH (UPDLOCK, HOLDLOCK, ROWLOCK)`), khoá theo thứ tự id tăng dần để tránh deadlock | `SanPhamRepository.khoaTheoId`, `DatHangServiceImpl.datHang` |
| Huỷ đơn, nhập kho chạy cùng lúc với đặt hàng | Cùng khoá dòng sản phẩm trước khi cộng tồn | `DonHangServiceImpl.huyDonHang`, `PhieuNhapServiceImpl` |
| Quản trị viên, khách và tác vụ nền cùng đổi trạng thái một đơn | Khoá bi quan dòng đơn (`refresh(don, PESSIMISTIC_WRITE)`) trước khi kiểm và đổi trạng thái: đơn chỉ bị huỷ, hoàn kho một lần | `DonHangServiceImpl.khoaDon` |
| Nhiều khách cùng dùng một mã giảm giá có giới hạn lượt | Khoá bi quan dòng mã giảm giá | `MaGiamGiaRepository.khoaTheoMa` |
| Tự huỷ đơn chuyển khoản quá 24 giờ chưa thanh toán | Tác vụ nền `@Scheduled` chạy mỗi 10 phút trên luồng `lich-*`, hoàn tồn kho và lượt mã | `service/TuHuyDonQuaHan` |
| Phục vụ nhiều client kho | Mỗi client một luồng trong `ExecutorService` (luồng `kho-client-N`), đếm kết nối bằng `AtomicInteger` | `mang/MayChuKho` |

Kiểm thử nhiều luồng nằm ở `src/test/java/vn/edu/huit/diengiadung/dongthoi/`, chạy trên CSDL riêng `dien_gia_dung_test`:

- `DatHangDongThoiTest`: 20 luồng cùng đặt mua một sản phẩm chỉ còn 5 cái → đúng 5 đơn thành công, 15 luồng báo hết hàng, tồn kho về 0.
- `DatHangDongThoiTest`: đơn chuyển khoản quá hạn bị tác vụ định kỳ huỷ, tồn kho được hoàn.
- `DatHangDongThoiTest`: hai luồng cùng huỷ một đơn → đúng một luồng huỷ được, tồn kho chỉ hoàn một lần.
- `MayChuKhoTest`: 3 client kết nối cùng lúc, mỗi client nhập kho 5 lần → tồn kho tăng đúng 15; lệnh sai, sai mật khẩu, chưa đăng nhập đều bị báo lỗi.

## Lập trình mạng: máy chủ kho qua Socket

Nhân viên kho dùng chương trình console `client-kho/KhoClient.java` để tra tồn kho và nhập hàng mà không cần mở web.
Dữ liệu đọc và ghi thẳng vào CSDL của ứng dụng: phiếu nhập tạo từ client hiện ngay trong trang quản trị *Phiếu nhập*.

Giao thức văn bản UTF-8, mỗi lệnh một dòng, các trường cách nhau bằng dấu `|`.
Máy chủ trả lời dòng đầu `OK|n` rồi `n` dòng dữ liệu, hoặc một dòng `LOI|thông báo`.

| Lệnh | Cần đăng nhập | Kết quả |
|---|---|---|
| `XINCHAO` | Không | Tên và phiên bản máy chủ |
| `DANGNHAP` · tên · mật khẩu | Không | Họ tên, vai trò (chỉ nhận tài khoản ADMIN) |
| `TIM` · từ khoá | Không | Tối đa 20 dòng mã · tên · tồn · giá |
| `TONKHO` · mã sản phẩm | Không | mã · tên · tồn · giá |
| `NCC` | Không | Danh sách id · tên nhà cung cấp |
| `NHAPKHO` · mã sp · số lượng · giá nhập · id NCC | Có | id phiếu · mã sp · tồn mới |
| `DONCHO` | Có | Đơn chờ xác nhận: mã đơn · người nhận · tổng tiền · ngày đặt |
| `THOAT` | Không | Đóng kết nối |

Ví dụ: `NHAPKHO|BDT-BOS-001|5|11000000|1` → `OK|1` rồi `45|BDT-BOS-001|11`.

Máy chủ phục vụ tối đa 10 client cùng lúc (client thứ 11 nhận `LOI|Máy chủ đang bận`), ngắt client im lặng quá 5 phút,
đóng socket bằng `try-with-resources` và đóng toàn bộ kết nối khi ứng dụng dừng. Cấu hình ở `application.properties` (`kho.socket.*`).

## Chức năng đã hoàn thành

Toàn bộ chức năng theo phân công đã nằm trong nhánh `main` và đã kiểm thử chéo giữa các module.

- **TV1**: project khung, layout Thymeleaf dùng chung, đăng ký, đăng nhập, đăng xuất, phân quyền chặn `/admin`,
  quản lý tài khoản (tìm kiếm, khoá hoặc mở khoá, đặt lại mật khẩu), quản lý loại sản phẩm, trang lỗi 400/404/500.
  Tài khoản bị khoá khi đang đăng nhập sẽ bị đăng xuất ở lần thao tác mua hàng kế tiếp.
- **TV2**: thương hiệu, nhà cung cấp, lập phiếu nhập nhiều dòng và cộng tồn kho, xem phiếu nhập, cảnh báo sản phẩm sắp hết hàng.
  Tài liệu trong `docs/tv2/`, ERD trong `ERD/ERD.png`, từ điển dữ liệu `docs/tv2/CSDL-he-thong.docx`.
- **TV3**: quản lý sản phẩm có upload ảnh và thông số kỹ thuật, ngừng bán hoặc mở bán lại; trang chủ, danh sách theo loại và thương hiệu,
  tìm kiếm, lọc theo mức giá, sắp xếp, phân trang, chi tiết sản phẩm. Tài liệu trong `docs/tv3/`.
- **TV4**: giỏ hàng lưu trong session, đặt hàng kiểm tồn và trừ tồn trong một giao dịch, lịch sử đơn, huỷ đơn chờ xác nhận, hồ sơ khách hàng.
  Tài liệu trong `docs/tv4/`.
- **TV5**: quản lý đơn hàng (lọc, chi tiết, cập nhật trạng thái, huỷ và hoàn tồn kho), quản lý khách hàng,
  thống kê doanh thu theo tháng và 10 sản phẩm bán chạy. Tài liệu trong `docs/tv5/`.


### Phần hoàn thiện để chạy thực tế

- **Sản phẩm yêu thích** (`/yeu-thich`): khách lưu sản phẩm để xem lại. Quan hệ `@ManyToMany` hai chiều
  giữa `KhachHang` và `SanPham` qua bảng nối `san_pham_yeu_thich` (bài 09 · advanced mappings).
- **Đổi mật khẩu** (`/ho-so/doi-mat-khau`): kiểm tra mật khẩu hiện tại, mật khẩu mới ≥ 6 ký tự, nhập lại khớp, khác mật khẩu cũ.
- **In hoá đơn** (`/admin/don-hang/{id}/in`): trang khổ A4 có sẵn nút in, ẩn nút khi in.
- **Thống kê bằng DAO `EntityManager` + JPQL** (`ThongKeDao`, bài 03): tính doanh thu, số đơn, 10 sản phẩm bán chạy ngay trong
  câu truy vấn thay vì nạp mọi đơn hàng vào bộ nhớ.
- **Bộ ảnh sản phẩm**: ảnh chụp thật, mỗi sản phẩm có thể có nhiều ảnh (cột `anh_phu`).
- **Bảo mật**: chống giả mạo yêu cầu (CSRF) cho mọi form POST bằng `RequestDataValueProcessor` + interceptor
  (`MaChongGiaMao`, `KiemTraMaChongInterceptor`); đổi mã phiên khi đăng nhập; cookie phiên `HttpOnly`, `SameSite=Lax`;
  form đăng ký chỉ nhận đúng 4 trường để không ghi đè được tài khoản khác; kiểm tra chữ ký tệp ảnh khi upload;
  tài khoản bị khoá giữa phiên bị đăng xuất ở thao tác mua hàng kế tiếp.

## Cấu trúc thư mục

```
src/main/java/vn/edu/huit/diengiadung/
├── config/       WebConfig, interceptor phân quyền, KhoiTaoDuLieu
├── controller/   phía khách; controller/admin phía quản trị; controller/api REST
├── service/      interface + impl, TuHuyDonQuaHan (@Scheduled)
├── repository/   JpaRepository, truy vấn @Query, khoá bi quan, DAO EntityManager
├── entity/       13 thực thể + lớp cha UuDaiCoBan
├── dto/          dữ liệu form và giỏ hàng (GioHang, ThongTinGiaoHang, BangGia...)
└── mang/         MayChuKho (ServerSocket), PhienKho, XuLyLenhKho
src/main/resources/
├── templates/    Thymeleaf, layout/fragments.html dùng chung (header, menu, footer)
├── static/css/   site.css (giao diện gốc), ung-dung.css (khu quản trị + điều chỉnh Bootstrap)
├── application.properties
└── data.sql      dữ liệu mẫu cho SQL Server
client-kho/       chương trình client tách biệt
database/         script SQL Server tạo bảng và dữ liệu mẫu
```

## Giao diện

Bootstrap 5.3 nhúng qua webjar (`/webjars/bootstrap/5.3.3/...`), dùng cho lưới bố cục form (`row`, `col-md-*`),
ô nhập (`form-control`, `form-select`, `form-label`, `invalid-feedback`), bảng (`table`, `table-hover`, `table-responsive`),
nút (`btn`), thông báo (`alert`) và phân trang (`pagination`, `page-link`). Màu nền tối `#131313`, chữ kem, nhấn vàng đồng `#CBAA7D`
được giữ bằng cách đặt lại biến CSS của Bootstrap trong `ung-dung.css`. Ở khổ điện thoại (390px) bảng chuyển thành dạng thẻ,
menu thu vào nút ☰.

**Trước khi viết HTML, đọc `QUY_TAC_GIAO_DIEN.md`.**

## REST API (bài 01 · REST Controller)

Chỉ đọc, trả JSON, không cần đăng nhập. Lỗi trả về dạng `{"trangThai": 404, "loi": "..."}`.

| Đường dẫn | Kết quả |
|---|---|
| `GET /api/loai-san-pham` | Danh sách loại sản phẩm |
| `GET /api/thuong-hieu` | Danh sách thương hiệu |
| `GET /api/san-pham?loai=1&hang=2&tuKhoa=bosch&sapXep=gia-tang&trang=1` | Sản phẩm đang bán, có lọc và phân trang |
| `GET /api/san-pham/{id}` | Chi tiết sản phẩm: giá, tồn kho, mô tả, thông số, bộ ảnh |

## Chạy bản thật (profile `prod`)

```bash
mvnw -DskipTests package
java -jar target/diengiadung-1.0.0.jar --spring.profiles.active=prod
```

Biến môi trường dùng được: `DB_URL`, `DB_USER`, `DB_PASS`, `PORT`, `THU_MUC_ANH` (thư mục lưu ảnh upload),
`COOKIE_SECURE=true` khi chạy sau HTTPS, `DB_NAP_MAU=always` nếu muốn nạp dữ liệu mẫu (mặc định không nạp).

## Kiểm thử

`mvnw test` chạy 23 ca: 18 unit test (lọc sản phẩm, thực thể, giỏ hàng, tài khoản, ưu đãi) và 5 kiểm thử tích hợp
đồng thời và mạng trên SQL Server (tự tạo CSDL `dien_gia_dung_test`, login cần quyền `dbcreator`).

## Quy ước làm việc

- Nhánh riêng cho từng chức năng: `tvX-ten-chuc-nang`, không đẩy thẳng lên `main`.
- Mỗi chức năng một Pull Request, có người khác xem lại, nhóm trưởng gộp.
- Commit bằng tài khoản GitHub của chính mình.
- **Không viết comment trong code.** Đặt tên biến và phương thức rõ nghĩa thay cho chú thích.
- Quy tắc đầy đủ về code, thiết kế cơ sở dữ liệu và nghiệp vụ: xem `../HUONG_DAN_CHO_AI.md`.
