# Quy tắc giao diện

Giao diện của đồ án lấy lại hệ thống **Bếp Nè** (nâu `#48301F` trên nền trắng) từ project Lập trình Web của nhóm trưởng,
chuyển từ Razor sang Thymeleaf. Mục tiêu: 5 người code 5 màn khác nhau mà ghép lại vẫn ra **một website duy nhất**.

Đọc file này **trước khi viết dòng HTML đầu tiên**. Rubric đồ án các môn cùng Khoa dành 20% điểm cho giao diện.

---

## 1. Ba luật bắt buộc

1. **Không thêm thư viện giao diện.** Không Bootstrap, không Tailwind, không jQuery UI, không template tải trên mạng.
   Toàn bộ giao diện nằm trong `static/css/site.css` và `static/css/ung-dung.css`.
2. **Không sửa `site.css`.** Đó là hệ thống nền (token màu, nút, thanh menu, chân trang). Muốn đổi gì trong đó thì báo TV1.
3. **Cần một kiểu mới thì thêm vào `ung-dung.css`**, đặt tên tiếng Việt không dấu, và nói cho cả nhóm biết.
   Cấm viết `style="..."` dài trong HTML; chỉ được dùng `style` cho một hai thuộc tính lặt vặt như `margin-top`.

---

## 2. Khung của mọi trang

Chép nguyên khung này rồi thay phần giữa. Không tự viết lại `<head>`, thanh menu hay chân trang.

```html
<!DOCTYPE html>
<html lang="vi" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{layout/fragments :: dauTrang('Tiêu đề trang')}"></head>
<body>
<div th:replace="~{layout/bo-icon :: boIcon}"></div>
<div th:replace="~{layout/fragments :: menu}"></div>

<main class="than">
    <div class="khung">
        <div th:replace="~{layout/fragments :: thongBao}"></div>

        ... nội dung trang ...

    </div>
</main>

<div th:replace="~{layout/fragments :: chanTrang}"></div>
<div th:replace="~{layout/fragments :: menuDienThoai}"></div>
</body>
</html>
```

Trang quản trị thì `main` đổi thành `<main class="than than--kem">` và bọc thêm một lớp:

```html
<div class="bo-cuc-qt">
    <div th:replace="~{layout/fragments :: menuQuanTri('ten-trang')}"></div>
    <div>
        ... nội dung ...
    </div>
</div>
```

`'ten-trang'` là khoá để tô sáng mục đang mở: `tong-quan`, `loai-san-pham`, `tai-khoan`,
và khoá mới của bạn khi làm xong module (nhớ mở dòng tương ứng trong `menuQuanTri`, bỏ class `chua-lam`).

---

## 3. Các mảnh dùng chung

| Fragment | Dùng để làm gì |
|---|---|
| `layout/fragments :: dauTrang(tieuDe)` | Thẻ `<head>`: font Roboto, `site.css`, `ung-dung.css`, favicon |
| `layout/bo-icon :: boIcon` | Bộ 44 icon SVG. Phải có trên mọi trang, nếu không icon sẽ trống |
| `layout/fragments :: menu` | Thanh menu trên đầu: logo, danh mục, ô tìm kiếm, giỏ hàng, tài khoản |
| `layout/fragments :: thongBao` | Khung thông báo xanh (thành công) và đỏ (lỗi) |
| `layout/fragments :: menuQuanTri(trang)` | Menu dọc của khu quản trị |
| `layout/fragments :: chanTrang` | Chân trang |
| `layout/fragments :: menuDienThoai` | Ngăn kéo menu cho điện thoại, nút lên đầu trang, thẻ `<script>` |

Thanh menu và chân trang tự lấy danh mục từ biến `loaiTrenMenu`, do `ThongTinChung` (`@ControllerAdvice`) đưa vào mọi trang.
Bạn **không phải** thêm danh mục vào model của controller mình.

Biến dùng chung khác:
- `taiKhoanHienTai`: tài khoản đang đăng nhập, `null` nếu chưa đăng nhập.
- `soMonTrongGio`: số món trong giỏ, hiện trên icon giỏ hàng. TV4 đưa vào model khi làm giỏ hàng.

---

## 4. Icon

Gọi icon:

```html
<svg class="ic"><use xlink:href="#ic-tim-kiem"></use></svg>
```

Nếu mã icon lấy từ dữ liệu thì dùng `th:href` (không dùng `th:attr="xlink:href=..."`, Thymeleaf báo lỗi):

```html
<svg class="ic"><use th:href="'#ic-' + ${loai.maLoai}"></use></svg>
```

Danh mục: `ic-bep-tu` · `ic-bep-dien-tu` · `ic-bep-hong-ngoai` · `ic-hut-mui` · `ic-rua-chen` · `ic-may-giat` · `ic-combo`

Thao tác: `ic-them` · `ic-sua` · `ic-xoa` · `ic-kiem` · `ic-canh-bao` · `ic-khoa` · `ic-mo-khoa` · `ic-hop-trong` · `ic-tim-kiem`

Điều hướng: `ic-mui-ten-phai` · `ic-mui-ten-trai` · `ic-len` · `ic-chevron` · `ic-menu` · `ic-dong`

Tài khoản và hệ thống: `ic-tai-khoan` · `ic-dang-xuat` · `ic-bang-dieu-khien` · `ic-gio-hang`

Liên hệ: `ic-dien-thoai` · `ic-thu` · `ic-dong-ho` · `ic-dia-diem` · `ic-bao-hanh` · `ic-lap-dat` · `ic-ho-tro` · `ic-hoi-dap`

Cần icon mới thì báo TV1 vẽ thêm vào `templates/layout/bo-icon.html`, đừng chèn ảnh PNG hay icon font.

---

## 5. Bảng lớp có sẵn

### Bố cục
| Lớp | Dùng khi |
|---|---|
| `khung` | Giới hạn bề ngang và canh giữa nội dung. Luôn nằm trong `main` |
| `than` | Vùng nội dung chính của trang. `than--kem` cho nền kem (dùng cho khu quản trị) |
| `dau-trang` | Đầu trang: tiêu đề `h1` bên trái, nút bên phải (`dau-trang__nut`) |
| `duong-dan` | Breadcrumb |
| `the-trang` | Khối trắng bo góc bọc nội dung (form, bảng) |
| `bo-cuc-qt` | Lưới hai cột của khu quản trị |

### Nút
| Lớp | Kiểu |
|---|---|
| `nut` | Nút chính, nền nâu |
| `nut nut--vien` | Nút phụ, viền nâu nhạt |
| `nut nut--do` | Nút xoá |
| `nut nut--trang` / `nut nut--kinh` | Dùng trên nền tối (banner) |
| thêm `nut--nho` | Cỡ nhỏ, dùng trong bảng và thanh lọc |
| `nut-tron` | Nút tròn chỉ có icon |
| `nut-lien` | Trông như một liên kết gạch chân |

### Sản phẩm
`luoi-sp` bọc các thẻ `the-sp`. Bên trong: `the-sp__anh` (ảnh) hoặc `the-sp__hoa` (ô icon khi chưa có ảnh),
`the-sp__than`, `the-sp__hang`, `the-sp__ten`, `the-sp__ma`, `the-sp__gia` với `gia` và `gia-cu`, `the-sp__kho`.
Danh mục dùng `luoi-dm` với các thẻ `the-dm`.

### Bảng dữ liệu
```html
<div class="khung-bang">
    <table class="bang-dl">
        <thead><tr><th>Cột</th><th class="canh-giua">Số</th></tr></thead>
        <tbody><tr><td>Giá trị</td><td class="canh-giua">1</td></tr></tbody>
    </table>
</div>
```
`khung-bang` lo phần cuộn ngang trên điện thoại. Nhãn trạng thái dùng `the-nhan` kèm
`the-nhan--ok`, `the-nhan--cho`, `the-nhan--tat`, `the-nhan--do`.

### Biểu mẫu
```html
<form class="form-dl" th:action="@{/admin/...}" th:object="${doiTuong}" method="post">
    <div class="o-truong">
        <label class="nhan" for="ten">Tên</label>
        <input class="o-nhap" id="ten" th:field="*{ten}">
        <span class="goi-y">Câu hướng dẫn ngắn.</span>
        <span class="loi-nhap" th:if="${#fields.hasErrors('ten')}" th:errors="*{ten}"></span>
    </div>
    <div class="hang-nut">
        <button class="nut" type="submit">Lưu</button>
        <a class="nut nut--vien" th:href="@{/admin/...}">Quay lại</a>
    </div>
</form>
```

### Trạng thái rỗng
```html
<div class="trang-trong" th:if="${#lists.isEmpty(danhSach)}">
    <svg class="ic"><use xlink:href="#ic-hop-trong"></use></svg>
    <strong>Chưa có dữ liệu</strong>
    <span>Một câu gợi ý người dùng làm gì tiếp theo.</span>
</div>
```
**Mọi danh sách đều phải có nhánh rỗng.** Bảng trống không có dòng nào là lỗi giao diện.

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

- Tiếng Việt có dấu, viết hoa chữ đầu câu, **không viết hoa toàn bộ** (chữ hoa do CSS lo).
- Nút ghi đúng việc nó làm: "Lưu", "Xoá", "Thêm loại", không dùng "Submit", "OK".
- Câu lỗi phải nói **vì sao** và **làm gì tiếp**: "Không xoá được vì loại này còn 6 sản phẩm".
- Không dùng dấu gạch ngang dài (— hoặc –) trong câu hiển thị; dùng dấu phẩy hoặc dấu chấm.
- Tiền hiển thị bằng `${#numbers.formatInteger(sp.giaHienTai, 0, 'POINT')} + ' đ'`.

---

## 8. Kiểm tra trước khi tạo Pull Request

1. Trang hiển thị đúng trên khung 1440px **và** khi thu cửa sổ còn khoảng 400px.
2. Có thanh menu trên đầu và chân trang giống các trang khác.
3. Danh sách có nhánh rỗng; form có thông báo lỗi dưới từng ô.
4. Không có class lạ ngoài hai file CSS chung; không thêm thư viện.
5. Chụp màn hình chức năng, lưu lại để ghép vào báo cáo.

## 9. Việc còn lại của giao diện

- TV3: trang chi tiết sản phẩm `/san-pham/{id}`, lọc theo giá và hãng, phân trang, upload ảnh.
  Hiện thẻ sản phẩm trỏ tới `/san-pham/{id}` nhưng trang đó chưa có nên ra 404.
- TV4: giỏ hàng `/gio-hang`, đặt hàng, lịch sử đơn `/don-hang-cua-toi`. Icon giỏ trên menu đã sẵn sàng, chỉ cần đưa `soMonTrongGio` vào model.
- TV2 và TV5: các trang quản trị còn ghi "Đang xây dựng" trong menu quản trị.
