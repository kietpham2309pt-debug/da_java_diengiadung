# Quy tắc giao diện

Giao diện đồ án lấy từ nhánh **`giao-dien-moi`** của repo Lập trình Web (`kietpham2309pt-debug/bepne`),
chuyển từ Razor sang Thymeleaf. Đây là giao diện **nền tối**: nền `#131313`, chữ kem `#EDE7E0`,
màu nhấn vàng đồng `#CBAA7D`, font **Montserrat** nhúng sẵn trong repo.

Mục tiêu: 5 người làm 5 mảng khác nhau mà ghép lại vẫn ra một website duy nhất.
Rubric đồ án các môn cùng Khoa dành 20% điểm cho giao diện.

---

## 1. Ba luật bắt buộc

1. **Không thêm thư viện giao diện.** Không Bootstrap, không Tailwind, không template tải trên mạng.
   Toàn bộ giao diện nằm trong `static/css/site.css` và `static/css/ung-dung.css`.
2. **Không sửa `site.css`.** Đó là hệ thống gốc. Muốn đổi gì trong đó thì báo TV1.
3. **Cần kiểu mới thì thêm vào `ung-dung.css`**, đặt tên tiếng Việt không dấu, báo cả nhóm biết.
   Chỉ dùng `style="..."` cho một hai thuộc tính lặt vặt, không viết cả khối CSS trong HTML.

---

## 2. Khung của mọi trang

Chép nguyên khung này rồi thay phần giữa:

```html
<!DOCTYPE html>
<html lang="vi" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{layout/fragments :: dauTrang('Tiêu đề trang', false)}"></head>
<body>
<div th:replace="~{layout/bo-icon :: boIcon}"></div>
<div class="luoi-chuyen" id="luoi-chuyen" aria-hidden="true"></div>
<div th:replace="~{layout/fragments :: thanh}"></div>

<main>
    <div th:replace="~{layout/fragments :: banner('Tiêu đề', 'Một câu mô tả', 'banner-tai-khoan.jpg', 'Tên trang')}"></div>

    <section class="muc muc--sat-banner">
        <div class="khung">
            <div th:replace="~{layout/fragments :: thongBao}"></div>

            ... nội dung ...

        </div>
    </section>
</main>

<div th:replace="~{layout/fragments :: chanTrang}"></div>
<div th:replace="~{layout/fragments :: menuNho}"></div>
</body>
</html>
```

Vài điểm bắt buộc:
- Tham số thứ hai của `dauTrang` là cờ chạy màn mở đầu, **chỉ trang chủ để `true`**, các trang khác để `false`.
- `<div class="luoi-chuyen">` tạo hiệu ứng mở trang. Thiếu nó trang vẫn chạy nhưng vào hơi cụt.
- Trang **không có banner** thì dùng `<section class="muc muc--dau-trang">` để chừa chỗ cho thanh menu cố định.
- `menuNho` phải nằm cuối `body` vì nó kéo theo thẻ `<script>` của `site.js`.

Trang quản trị bọc thêm một lớp lưới hai cột:

```html
<div class="qt">
    <div th:replace="~{layout/fragments :: menuQuanTri('ten-trang')}"></div>
    <div>
        ... nội dung ...
    </div>
</div>
```

`'ten-trang'` là khoá tô sáng mục đang mở: `tong-quan`, `loai-san-pham`, `tai-khoan`.
Làm xong module của mình thì sửa `menuQuanTri` trong `fragments.html`: đổi dòng `qt-ben__cho` của mình thành thẻ `<a>`.

---

## 3. Các mảnh dùng chung

| Fragment | Dùng để làm gì |
|---|---|
| `layout/fragments :: dauTrang(tieuDe, coMoMan)` | Thẻ `<head>`: font, `site.css`, `ung-dung.css`, favicon, script hiệu ứng |
| `layout/bo-icon :: boIcon` | Bộ 44 icon SVG. Phải có trên mọi trang, thiếu thì icon trống |
| `layout/fragments :: thanh` | Thanh menu cố định trên đầu |
| `layout/fragments :: banner(tieuDe, moTa, anh, tenTrang)` | Banner đầu trang kèm breadcrumb. `anh` là tên file trong `static/img` |
| `layout/fragments :: thongBao` | Khung thông báo thành công và lỗi |
| `layout/fragments :: menuQuanTri(trang)` | Menu dọc khu quản trị |
| `layout/fragments :: chanTrang` | Chân trang kèm bản đồ Việt Nam |
| `layout/fragments :: menuNho` | Ngăn kéo menu cho điện thoại, kèm thẻ `<script>` |
| `layout/logo :: logo` | Logo Bep Nè dạng SVG |
| `layout/mo-man :: moMan` | Màn mở đầu, **chỉ trang chủ dùng** |

Biến có sẵn ở mọi trang, do `ThongTinChung` (`@ControllerAdvice`) đưa vào:
- `loaiTrenMenu`: danh sách loại sản phẩm cho menu và chân trang.
- `taiKhoanHienTai`: tài khoản đang đăng nhập, `null` nếu chưa đăng nhập.
- `soMonTrongGio`: số món trong giỏ, hiện trên icon giỏ hàng. TV4 đưa vào model khi làm giỏ hàng.

---

## 4. Ảnh và icon

**Ảnh theo mã loại.** Mỗi loại sản phẩm có mã (`maLoai`) và hai ảnh đặt sẵn trong `static/img`:
- `sp-<mã>.jpg`: ảnh ô danh mục, gọi bằng `${loai.anhDanhMuc}`.
- `banner-<mã>.jpg`: ảnh banner đầu trang danh mục, gọi bằng `${loai.anhBanner}`.

Mã đã có đủ ảnh: `bep-dien-tu`, `bep-hong-ngoai`, `hut-mui`, `rua-chen`, `lo-nuong`, `lo-vi-song`.

**Ảnh sản phẩm** gọi bằng `${sp.duongDanAnh}`. Phương thức này tự chọn: ảnh upload nếu có,
không thì lấy ảnh của danh mục, không nữa thì ảnh mặc định. TV3 làm upload chỉ cần lưu tên file vào `hinhAnh`.

**Icon** gọi như sau:

```html
<svg class="ic"><use xlink:href="#ic-tim-kiem"></use></svg>
```

Mã icon lấy từ dữ liệu thì dùng `th:href`, **không** dùng `th:attr="xlink:href=..."` (Thymeleaf báo lỗi):

```html
<svg class="ic"><use th:href="'#ic-' + ${loai.maLoai}"></use></svg>
```

- Danh mục: `ic-bep-dien-tu` · `ic-bep-hong-ngoai` · `ic-hut-mui` · `ic-rua-chen` · `ic-lo-nuong` · `ic-lo-vi-song`
- Dịch vụ: `ic-bao-hanh` · `ic-lap-dat` · `ic-giao-hang` · `ic-tu-van`
- Giao diện: `ic-tim-kiem` · `ic-gio-hang` · `ic-nguoi-dung` · `ic-menu` · `ic-dong` · `ic-mui-ten-phai` · `ic-chevron` · `ic-bo-loc`
- Liên hệ: `ic-dien-thoai` · `ic-thu` · `ic-dia-diem` · `ic-dong-ho`
- Thông số kỹ thuật (dùng cho trang chi tiết): các mã bắt đầu bằng `ic-tn-`, ví dụ `ic-tn-nhanh`, `ic-tn-khoa`, `ic-tn-canh-bao`, `ic-tn-say`, `ic-tn-quat`.

Cần icon mới thì báo TV1 vẽ thêm vào `templates/layout/bo-icon.html`, đừng chèn ảnh PNG hay icon font.

---

## 5. Bảng lớp có sẵn

### Bố cục
| Lớp | Dùng khi |
|---|---|
| `khung` | Giới hạn bề ngang, canh giữa. `khung--hep` cho form và trang chữ |
| `muc` | Một khối nội dung. `muc--sat-banner` khi nằm ngay dưới banner, `muc--dau-trang` khi trang không có banner, `muc--vien` có đường kẻ trên |
| `dau-sp`, `dau-sp__td`, `dau-sp__mt`, `nhan` | Cụm tiêu đề khối: nhãn nhỏ, tiêu đề lớn, mô tả |
| `duong-dan` | Breadcrumb |
| `hien-dan`, `hien-dan-nhom` | Hiệu ứng hiện dần khi cuộn tới. Thêm vào khối muốn có hiệu ứng |

### Nút
| Lớp | Kiểu |
|---|---|
| `nut` | Nút viền, dùng cho hành động phụ |
| `nut nut--dac` | Nút nền vàng đồng, dùng cho hành động chính |
| `nut nut--nho` | Cỡ nhỏ, dùng trong bảng và thanh lọc |
| `nut nut--do` | Nút xoá |
| `nut-tron` | Nút tròn chỉ có icon |

### Sản phẩm và danh mục
- Lưới sản phẩm: `luoi-sp` bọc các thẻ `sp`. Bên trong: `sp__anh` (bọc `img` và nhãn giảm giá `sp__giam`),
  `sp__hieu`, `sp__ten`, `sp__gia` (giá hiện tại trong `<b>`, giá cũ trong `<del>`).
- Lưới danh mục: `luoi-dm` bọc các thẻ `dm` với `dm__anh`, `dm__phu`, `dm__mui`, `dm__ten`.
- Trang danh sách có bộ lọc: `ds` (form) chứa `ds__thanh` (đếm, sắp xếp) và `ds__than` (gồm `loc` bên trái, `ds__phai` bên phải).

### Bảng dữ liệu
```html
<div class="cuon-ngang">
    <table class="bang-dl">
        <thead><tr><th>Cột</th><th class="canh-giua">Số</th></tr></thead>
        <tbody><tr><td>Giá trị</td><td class="canh-giua">1</td></tr></tbody>
    </table>
</div>
```
Cột mô tả dài thì thêm `class="o-dai"` cho ô đó. Nhãn trạng thái dùng `the-nhan` kèm
`the-nhan--ok`, `the-nhan--vang`, `the-nhan--tat`, `the-nhan--do`.

### Biểu mẫu
```html
<form th:action="@{/admin/...}" th:object="${doiTuong}" method="post">
    <div class="o-nhap">
        <label for="ten">Tên</label>
        <input type="text" id="ten" th:field="*{ten}">
        <span class="goi-y">Câu hướng dẫn ngắn.</span>
        <span class="loi-nhap" th:if="${#fields.hasErrors('ten')}" th:errors="*{ten}"></span>
    </div>
    <div class="hang-nut">
        <button class="nut nut--dac" type="submit">Lưu</button>
        <a class="nut" th:href="@{/admin/...}">Quay lại</a>
    </div>
</form>
```
`o-nhap` là lớp bọc cả nhãn và ô nhập, không phải đặt trên thẻ `<input>`.
Form đăng nhập, đăng ký đặt trong `the-tk` và nút gửi thêm lớp `the-tk__gui`.

### Trạng thái rỗng
```html
<div class="trong" th:if="${#lists.isEmpty(danhSach)}">
    <svg class="ic ic--to"><use xlink:href="#ic-tim-kiem"></use></svg>
    <p>Không tìm thấy sản phẩm nào khớp với lựa chọn của bạn.</p>
    <a class="nut" th:href="@{/san-pham}">Xem tất cả danh mục</a>
</div>
```
**Mọi danh sách đều phải có nhánh rỗng.**

### Khu quản trị (trong `ung-dung.css`)
`qt` (lưới hai cột) · `qt-ben` (menu dọc) · `qt-dau` (tiêu đề trang) · `o-so-ds` và `o-so` (ô số liệu) ·
`khoi` (khối trắng bọc bảng, form) · `thanh-loc` (hàng tìm kiếm) · `hang-thao-tac` (nút trong bảng).

---

## 6. Thông báo sau khi lưu, xoá

Controller đặt thông báo rồi chuyển hướng, không tự vẽ chữ trong HTML:

```java
thongBao.addFlashAttribute("thongBao", "Đã thêm loại sản phẩm");
thongBao.addFlashAttribute("thongBaoLoi", loi.getMessage());
```

Trang hiển thị bằng đúng một dòng: `<div th:replace="~{layout/fragments :: thongBao}"></div>`.

---

## 7. Viết chữ trên giao diện

- Tiếng Việt có dấu, viết hoa chữ đầu câu. **Không tự viết hoa toàn bộ**, chữ hoa do CSS lo.
- Nút ghi đúng việc nó làm: "Lưu", "Xoá", "Thêm loại". Không dùng "Submit", "OK".
- Câu lỗi nói rõ **vì sao** và **làm gì tiếp**: "Không xoá được vì loại này còn 6 sản phẩm".
- Không dùng dấu gạch ngang dài trong câu hiển thị; dùng dấu phẩy hoặc dấu chấm.
- Tiền hiển thị bằng `${#numbers.formatInteger(sp.giaHienTai, 0, 'POINT')} + ' ₫'`.

---

## 8. Kiểm tra trước khi tạo Pull Request

1. Trang chạy đúng ở khung 1440px **và** khi thu cửa sổ còn khoảng 520px.
2. Có thanh menu trên đầu, chân trang, và ngăn kéo menu điện thoại như các trang khác.
3. Danh sách có nhánh rỗng; form có thông báo lỗi dưới từng ô.
4. Không thêm thư viện, không có class lạ ngoài hai file CSS chung.
5. Chụp màn hình chức năng, lưu lại để ghép vào báo cáo.

---

## 9. Phần giao diện còn lại

Nhánh `giao-dien-moi` của repo `bepne` đã có sẵn markup Razor cho những trang dưới đây.
Người phụ trách mở file tương ứng ra xem rồi chuyển sang Thymeleaf theo đúng khuôn ở mục 2:

| Trang | File Razor tham khảo | Người làm |
|---|---|---|
| Chi tiết sản phẩm `/san-pham/{id}` | `Views/SanPham/ChiTiet.cshtml` | TV3 |
| Giỏ hàng `/gio-hang` | `Views/GioHang/Index.cshtml` | TV4 |
| Đặt hàng `/dat-hang` | `Views/DatHang/Index.cshtml` | TV4 |
| Đặt hàng thành công | `Views/DatHang/HoanTat.cshtml` | TV4 |
| Phân trang danh sách | `Views/SanPham/_KetQua.cshtml` (lớp `trang`, `trang__nut`) | TV3 |

Các trang quản trị của TV2 và TV5 làm theo mẫu `admin/loai-san-pham` trong repo này.
