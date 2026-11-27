# Hướng dẫn kết nối SePay (thử chuyển khoản QR) và GHN (giao hàng)

Phần code đã làm xong và đã gộp vào `main`. Phần các bạn cần làm:

1. Đăng ký tài khoản **thử nghiệm** của SePay và GHN.
2. Điền khoá vào file cấu hình.
3. Chạy thử theo từng bước bên dưới.
4. Chụp ảnh minh chứng.

Không cần sửa dòng code nào.

> Cả hai đều dùng **môi trường thử** nên không có tiền thật và không có shipper thật.
> Token và khoá API chỉ ghi vào `tich-hop.properties`. File này đã nằm trong `.gitignore`, **tuyệt đối không commit**.

## Mục lục

- [0. Chuẩn bị chung](#0-chuẩn-bị-chung)
- [1. SePay: tự xác nhận chuyển khoản](#1-sepay-tự-xác-nhận-chuyển-khoản)
- [2. GHN: tạo vận đơn giao hàng](#2-ghn-tạo-vận-đơn-giao-hàng)
- [3. Danh sách kiểm thử phải nộp](#3-danh-sách-kiểm-thử-phải-nộp)
- [4. Lỗi thường gặp](#4-lỗi-thường-gặp)
- [5. Code nằm ở đâu (để trả lời thầy)](#5-code-nằm-ở-đâu-để-trả-lời-thầy)
- [6. Gợi ý phân công](#6-gợi-ý-phân-công)
- [Nguồn tài liệu đã đối chiếu](#nguồn-tài-liệu-đã-đối-chiếu)

---

## 0. Chuẩn bị chung

1. Lấy code mới nhất: `git pull origin main`.
2. Trong thư mục gốc project (cùng chỗ với `pom.xml`), **sao chép** `tich-hop.mau.properties` thành `tich-hop.properties`.
3. Chạy web như thường lệ (IntelliJ hoặc `mvnw spring-boot:run`). Khi khởi động, web tự đọc `tich-hop.properties`.
4. Sửa file này thì phải **chạy lại web**.

Khi chưa có file `tich-hop.properties` hoặc để `sepay.bat=false` / `ghn.bat=false`, web chạy y như cũ: admin tự bấm "Xác nhận đã nhận tiền" và tự chuyển trạng thái đơn.

Cột và bảng mới (`don_hang.ma_van_don`…, bảng `giao_dich_sepay`) do Hibernate tự thêm khi chạy (`ddl-auto=update`). Người tạo CSDL mới bằng `database/01_tao_csdl.sql` cũng đã có sẵn.

---

## 1. SePay: tự xác nhận chuyển khoản

### 1.1 Luồng hoạt động

```
Khách đặt đơn "Thanh toán trước" ──► trang đơn hiện mã QR, nội dung CK = BN000123
                                     (trang tự hỏi server mỗi vài giây)
Trên my.sepay.vn bấm "Mô phỏng giao dịch" (giả làm khách chuyển khoản)
        │
        ▼
SePay gọi webhook  POST /api/thanh-toan/sepay  (kèm header Authorization: Apikey ...)
        │
        ▼
Web kiểm tra khoá → tìm "BN000123" trong nội dung → so số tiền với tổng đơn
        │
        ├─ đủ tiền  → đơn chuyển "Đã nhận tiền", trang khách tự đổi sang "Đã nhận thanh toán"
        ├─ thiếu    → ghi lại "Chuyển thiếu ... đ", đơn vẫn chờ
        └─ gửi lại cùng id giao dịch → bỏ qua, không xử lý 2 lần
```

Mọi giao dịch nhận được (cả giao dịch không khớp đơn) đều xem ở **Quản trị → Giao dịch SePay**.

### 1.2 Đăng ký và bật Test mode

1. Vào https://my.sepay.vn, đăng ký và đăng nhập.
   - Tài liệu không ghi giá cho Test mode. Nếu SePay đòi trả phí, báo lại nhóm trưởng, **đừng tự nạp tiền**.
2. Ở thanh bên trái, bấm **Test mode** (biểu tượng ống nghiệm) và bật lên.
   - Hoặc nhấn `Ctrl+K`, gõ "Test mode".
   - Khi bật, trên cùng hiện dải vàng "Test mode: data does not affect Live accounts".
   - Tài khoản thường mà không thấy mục này thì cần quyền "Cho phép truy cập Test mode". Chủ công ty cấp quyền ở *Cấu hình Công ty → Người dùng*.

### 1.3 Tạo tài khoản ngân hàng ảo

1. Vào **Test mode → Tài khoản ngân hàng → Thêm tài khoản**, chọn tab **Tạo thủ công**.
2. Chọn ngân hàng **MBBank**, loại tài khoản **Cá nhân**, rồi lưu.
   - Dùng MBBank để khỏi phải tạo tài khoản ảo phụ (VA).
3. Ghi lại **số tài khoản** và **tên chủ tài khoản** SePay vừa sinh ra.

### 1.4 Khai báo cấu trúc mã thanh toán

Vào **Test mode → Cấu trúc mã thanh toán** (bên Live là *Cấu hình Công ty → Cấu hình chung → Cấu trúc mã thanh toán*) và đặt:

| Ô | Giá trị |
|---|---|
| Tiền tố | `BN` |
| Độ dài hậu tố tối thiểu | `6` |
| Độ dài hậu tố tối đa | `6` |
| Kiểu ký tự | Số nguyên |

Nhờ vậy SePay tự tách `BN000123` ra trường `code` của webhook. Bỏ qua bước này web vẫn chạy được, vì web còn tự tìm `BNxxxxxx` trong nội dung chuyển khoản.

### 1.5 Cho SePay gọi được vào máy mình

SePay **không gọi được `localhost`**. Có 2 cách, nên dùng **cách A**.

**Cách A: SePay CLI (không cần mở cổng, không cần ngrok)**

Mở PowerShell:

```powershell
irm https://get.sepay.vn/sepay-cli-install.ps1 | iex
sepay version
sepay login
```

`sepay login` mở trình duyệt để đăng nhập; tài khoản phải đang ở Test mode.

Sau đó tạo webhook kiểu "Nhận qua SePay CLI":

1. Vào **Test mode → Tích hợp Webhook → Thêm webhook**.
2. Bước **Cơ bản**:
   - Tên: `Bep Ne local`.
   - Cách nhận sự kiện: **Nhận qua SePay CLI**.
   - Loại sự kiện: **Tiền vào**.
   - Định dạng: **JSON**.
   - Bật "Tự động gửi lại khi server trả lỗi".
3. Bước **Tài khoản**: chọn tài khoản MBBank ở bước 1.3. Bật **Dùng để xác thực thanh toán**.
4. Bước **Bảo mật**: chọn **API Key**. **Sao chép khoá ngay**, vì SePay chỉ hiện đầy đủ một lần.
5. Bấm **Thêm**.

Rồi để cửa sổ này chạy suốt lúc thử:

```powershell
sepay webhook listen --forward localhost:8080/api/thanh-toan/sepay --print
```

(Không chọn được "Nhận qua SePay CLI" trên web thì dùng lệnh `sepay webhook create --listen-only --bank-mode all --event-type in`.)

**Cách B: đường hầm công khai (ngrok hoặc cloudflared)**

```powershell
cloudflared tunnel --url http://localhost:8080
```

Lệnh in ra địa chỉ dạng `https://xxxx.trycloudflare.com`. Tạo webhook kiểu **Gửi tới URL của tôi** với URL `https://xxxx.trycloudflare.com/api/thanh-toan/sepay`. Các bước còn lại giống cách A.

### 1.6 Điền `tich-hop.properties`

```properties
sepay.bat=true
sepay.api-key=DAN_KHOA_API_O_BUOC_BAO_MAT
cuahang.ngan-hang.ma-bin=970422
cuahang.ngan-hang.ten=MBBank
cuahang.ngan-hang.so-tai-khoan=SO_TAI_KHOAN_AO_O_BUOC_1.3
cuahang.ngan-hang.chu-tai-khoan=TEN_CHU_TK_KHONG_DAU
```

- Chỉ dán **phần khoá**, không kèm chữ `Apikey`.
- Nếu ở bước Bảo mật chọn **HMAC-SHA256**, để trống `sepay.api-key` và điền `sepay.khoa-hmac=SECRET_KEY`. Web kiểm tra chữ ký `X-SePay-Signature` và `X-SePay-Timestamp` (lệch giờ tối đa 5 phút).
- Mã QR trên trang đơn là ảnh VietQR vẽ theo số tài khoản ảo. App ngân hàng thật quét sẽ báo "không tìm thấy tài khoản". Điều này **đúng như mong đợi**, vì ta chuyển khoản bằng nút mô phỏng.

Chạy lại web.

### 1.7 Kiểm tra nhanh trước khi dùng SePay

Kiểm tra webhook trong máy mình đã nhận đúng khoá chưa:

```powershell
sepay webhook test --forward localhost:8080/api/thanh-toan/sepay --amount 50000
```

Lệnh trả mã 0 là web đã trả HTTP 200. Giao dịch thử này không có mã `BN...`, nên trong *Quản trị → Giao dịch SePay* sẽ thấy một dòng "Không tìm thấy mã đơn". Như vậy là đúng.

> Lệnh `sepay webhook test` không gửi header khoá API thì web trả 401. Khi đó bỏ qua bước này, sang thẳng 1.8.

### 1.8 Kịch bản demo chính

1. Mở web như khách: thêm một sản phẩm vào giỏ, rồi **Đặt hàng** và chọn **Thanh toán trước (chuyển khoản)**.
2. Ghi lại **mã đơn** (ví dụ `BN000123`) và **số tiền** trên trang. Để nguyên trang này.
3. Trên my.sepay.vn, vào **Test mode → Mô phỏng giao dịch** và điền:
   - Tài khoản: MBBank ảo.
   - Loại: **Tiền vào**.
   - Số tiền: **đúng bằng** tổng đơn.
   - Nội dung: `BN000123 thanh toan`.
   - Bấm gửi.
   - Cách khác: **Mô phỏng tạo mã VietQR**, rồi bấm **Mô phỏng chuyển khoản**.
   - Hoặc dùng CLI: `sepay sandbox simulate payment_in --bank-account <ma-tk> --amount <so-tien> --code BN000123`.
4. Trong vài giây, trang của khách **tự đổi** sang "Đã nhận thanh toán" mà không cần F5.
5. Đăng nhập admin, mở đơn đó:
   - Trạng thái thanh toán là **Đã nhận tiền**.
   - Khối **Giao dịch chuyển khoản (SePay)** có dòng "Đã xác nhận thanh toán".
6. Thử thêm các trường hợp:
   - Chuyển **thiếu** 1.000 đ: đơn vẫn chờ, kết quả "Chuyển thiếu 1000 đ…".
   - Nội dung **không có** mã đơn: chỉ hiện trong *Giao dịch SePay* với "Không tìm thấy mã đơn".
   - Trong *Lịch sử gửi* của SePay, bấm **Gửi lại** (Replay) một giao dịch đã xử lý: web không xác nhận lần hai.

Lịch sử SePay đã gửi gì và web trả gì xem ở **Test mode → Tích hợp Webhook → Lịch sử gửi**.

---

## 2. GHN: tạo vận đơn giao hàng

### 2.1 Luồng hoạt động

```
Admin "Xác nhận đơn"  ──►  khối "Vận chuyển GHN" trong trang chi tiết đơn
   ├─ [Xem phí GHN]           gọi /v2/shipping-order/preview  (không tạo đơn, chỉ báo phí và ngày giao)
   ├─ [Tạo vận đơn GHN]       gọi /v2/shipping-order/create   → lưu mã vận đơn, phí, ngày giao dự kiến
   ├─ [Cập nhật trạng thái]   gọi /v2/shipping-order/detail   → đổi trạng thái đơn theo GHN
   └─ [Huỷ vận đơn GHN]       gọi /v2/switch-status/cancel    (chỉ khi GHN chưa lấy hàng)
GHN tự gọi webhook  POST /api/van-chuyen/ghn  khi trạng thái đổi (nếu cấu hình bước 2.5)
```

Quy tắc đổi trạng thái đơn của web theo GHN:

| Trạng thái GHN | Đơn của web |
|---|---|
| `picked`, `storing`, `sorting`, `transporting`, `delivering`, `delivery_fail` | Đã xác nhận → **Đang giao** |
| `delivered` | → **Hoàn thành** |
| `cancel` (huỷ vận đơn) | Đơn giữ nguyên, admin có thể tạo vận đơn mới |

Thêm vài điểm về nghiệp vụ:

- Đơn đang có vận đơn GHN thì **không huỷ đơn được**. Phải huỷ vận đơn trước.
- Đơn COD thì GHN thu hộ đúng tổng tiền (`cod_amount`); đơn đã chuyển khoản thì thu hộ 0.
- Phí giao do **cửa hàng trả** (`payment_type_id=1`), vì web đang miễn phí giao cho khách.
- Địa chỉ gửi GHN theo **mô hình mới 2 cấp** (`is_new_to_address=true`, tên phường/xã và tỉnh/thành). Web tự đối chiếu tên trong `dia-chi-2025.json` với danh mục của GHN. Ví dụ "Thành phố Hồ Chí Minh" khớp với "Hồ Chí Minh".
- Sản phẩm chưa có cân nặng, nên mỗi sản phẩm tính **3000 g**, kiện **40×40×40 cm**. Đổi được trong `tich-hop.properties`. Vận đơn tối đa 50 kg.

### 2.2 Đăng ký tài khoản staging (miễn phí)

1. Vào https://developer.ghn.dev (môi trường **thử**, khác hẳn developer.ghn.vn thật) và đăng nhập bằng **số điện thoại + mã OTP**.
2. Tạo cửa hàng (shop) nếu hệ thống yêu cầu. Địa chỉ shop là **địa chỉ lấy hàng**, nên nhập một địa chỉ ở TP.HCM.

### 2.3 Lấy Token và ShopId

- **Token**: menu tài khoản → **Quản lý token** (`/account`) → thẻ *Token API* → bấm biểu tượng con mắt → nhập OTP gửi về điện thoại → sao chép.
  - Ô "IP được phép" để **trống**. Nếu thêm IP thì chỉ IP đó gọi được.
- **ShopId**: số ID cửa hàng, hiện trong phần thông tin tài khoản hoặc danh sách cửa hàng. Tài liệu GHN không ghi rõ vị trí, nên các bạn tìm trên giao diện và **chụp lại vị trí để bổ sung vào file này**.

### 2.4 Điền `tich-hop.properties`

```properties
ghn.bat=true
ghn.url=https://dev-online-gateway.ghn.vn
ghn.token=DAN_TOKEN
ghn.shop-id=DAN_SHOP_ID
```

Chạy lại web. Trang chi tiết đơn của admin hiện khối **Vận chuyển GHN** có nhãn "Môi trường thử".

> Đã kiểm tra: khi điền token sai, web gọi tới server thử của GHN và nhận về "GHN báo lỗi: Token is not valid!". Nghĩa là đường kết nối đã đúng, chỉ cần token thật.

### 2.5 Webhook GHN (không bắt buộc)

Không làm bước này thì dùng nút **Cập nhật trạng thái từ GHN** để kéo trạng thái về. Nút này đủ để demo.

Muốn GHN tự báo về thì cần URL công khai: dùng cloudflared như mục 1.5 cách B.

1. Trên developer.ghn.dev, vào menu tài khoản → **Cấu hình webhook** (`/account/webhook`) → tab **Đơn hàng**.
2. Dán URL `https://xxxx.trycloudflare.com/api/van-chuyen/ghn`.
3. Thêm **header tuỳ chỉnh** `X-BepNe-Key` với giá trị bí mật tự đặt, rồi khai báo cùng giá trị đó vào `ghn.khoa-webhook=...`.
4. Bấm tạo. GHN ghi chú: **khoảng 15 phút** sau cấu hình mới có hiệu lực.

Thử webhook không cần GHN. Thay `MA_VAN_DON` bằng mã thật trong trang admin; nếu có đặt khoá thì thêm `"X-BepNe-Key"="..."` vào `-Headers`:

```powershell
$than = '{"OrderCode":"MA_VAN_DON","Status":"picked","Type":"switch_status","ShopID":0,"Time":"2026-11-27T10:00:00Z"}'
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/van-chuyen/ghn -ContentType 'application/json' -Body $than -Headers @{}
```

Kết quả `capNhat: True`, và đơn chuyển sang **Đang giao**. Gửi tiếp `"Status":"delivered"` thì đơn sang **Hoàn thành**.

### 2.6 Kịch bản demo chính

1. Khách đặt một đơn **COD**, giao về một địa chỉ thật, ví dụ Phường Sài Gòn, TP.HCM.
2. Admin mở đơn và bấm **Xác nhận đơn hàng**.
3. Bấm **Xem phí GHN**. Thông báo hiện phí giao và ngày dự kiến, chưa tạo đơn.
4. Bấm **Tạo vận đơn GHN**. Khối GHN hiện mã vận đơn (dạng `LxxxxX`), phí và ngày giao dự kiến. Trang đơn của khách cũng hiện mã vận đơn.
5. Mở developer.ghn.dev (hoặc trang quản lý đơn của tài khoản staging) và **chụp ảnh đơn vừa tạo** để chứng minh đơn đã sang GHN.
6. Bấm **Cập nhật trạng thái từ GHN**. Staging thường giữ đơn ở `ready_to_pick` ("Chờ lấy hàng").
7. Bấm **Huỷ vận đơn GHN**. Trạng thái GHN thành "Đã huỷ vận đơn", và lúc này mới huỷ được đơn.
8. Muốn demo đơn **Đang giao → Hoàn thành**: dùng lệnh PowerShell ở mục 2.5. Tài liệu GHN không nói staging có nút tự chuyển trạng thái. Nếu các bạn tìm thấy nút như vậy trên developer.ghn.dev thì ghi bổ sung vào đây.

---

## 3. Danh sách kiểm thử phải nộp

Mỗi dòng chụp 1 ảnh, lưu vào `docs/minh-chung-tich-hop/` (đặt tên không dấu, ví dụ `sepay_01_dat_don.png`).

| # | Việc | Kết quả mong đợi |
|---|---|---|
| S1 | Đặt đơn chuyển khoản | Trang đơn có QR, nội dung `BNxxxxxx` |
| S2 | Mô phỏng chuyển đủ tiền | Trang khách tự đổi "Đã nhận thanh toán" |
| S3 | Admin xem đơn | "Đã nhận tiền" và có dòng giao dịch SePay |
| S4 | Mô phỏng chuyển thiếu | Đơn vẫn chờ, kết quả "Chuyển thiếu" |
| S5 | Nội dung sai mã | Có trong *Giao dịch SePay*, không gắn đơn nào |
| S6 | Replay giao dịch cũ | Không xác nhận lần 2 (SePay vẫn báo thành công) |
| S7 | Lịch sử gửi trên SePay | HTTP 200, body `{"success":true}` |
| G1 | Xem phí GHN | Có phí và ngày dự kiến |
| G2 | Tạo vận đơn | Có mã vận đơn; đơn hiện trên developer.ghn.dev |
| G3 | Cập nhật trạng thái | Trạng thái GHN hiện đúng |
| G4 | Huỷ đơn khi còn vận đơn | Bị chặn, báo phải huỷ vận đơn trước |
| G5 | Huỷ vận đơn | "Đã huỷ vận đơn", sau đó huỷ đơn được |
| G6 | Webhook `picked` rồi `delivered` | Đơn Đang giao → Hoàn thành |

---

## 4. Lỗi thường gặp

| Hiện tượng | Nguyên nhân / cách sửa |
|---|---|
| SePay *Lịch sử gửi* báo **401** | Sai `sepay.api-key`: dán thừa chữ `Apikey`, thừa dấu cách, hoặc chưa chạy lại web |
| SePay báo **404** "SePay chưa được bật" | Thiếu `sepay.bat=true`, hoặc `tich-hop.properties` đặt sai thư mục (phải cạnh `pom.xml`) |
| SePay báo **503** | Đã bật nhưng chưa khai báo khoá nào (`api-key` hoặc `khoa-hmac`) |
| SePay báo không kết nối được | Chưa chạy `sepay webhook listen` hoặc cloudflared, hoặc web chưa chạy |
| Chuyển rồi nhưng đơn không đổi | Xem *Quản trị → Giao dịch SePay*, cột Kết quả ghi rõ lý do (thiếu tiền, sai mã, đơn COD, đơn đã huỷ…) |
| GHN "Token is not valid!" | Token sai, hoặc dùng token của developer.ghn.vn (thật) cho server thử |
| GHN "không có tỉnh/thành …" hoặc "không có Phường …" | Tên địa chỉ của web không khớp danh mục GHN. Thử địa chỉ khác và báo nhóm trưởng tên bị lệch |
| GHN "route not found service" | Shop chưa có địa chỉ lấy hàng hợp lệ, hoặc tuyến chưa hỗ trợ. Đổi địa chỉ shop hoặc địa chỉ nhận |
| GHN "CLIENT_NOT_OWNER_OF_SHOP" | `ghn.shop-id` không thuộc tài khoản của token |
| Nút GHN không hiện | Đơn chưa ở trạng thái "Đã xác nhận" |

Nhật ký của web (cửa sổ chạy server) có các dòng `SePay giao dịch ...`, `Tạo vận đơn GHN ...`, `Webhook GHN ...` để dò lỗi.

---

## 5. Code nằm ở đâu (để trả lời thầy)

| File | Vai trò |
|---|---|
| `tichhop/CauHinhSePay`, `tichhop/CauHinhGhn` | Đọc cấu hình `sepay.*`, `ghn.*` |
| `controller/api/SePayWebhookController` | Nhận webhook SePay, kiểm tra khoá, luôn trả `{"success": true}` khi hợp lệ |
| `tichhop/ThanhToanSePay` | Xác thực API Key / HMAC-SHA256; tìm mã `BNxxxxxx`; chống xử lý trùng theo `id`; khoá dòng đơn (PESSIMISTIC_WRITE) khi xác nhận |
| `entity/GiaoDichSePay` + bảng `giao_dich_sepay` | Lưu mọi giao dịch nhận được (`ma_giao_dich` UNIQUE) |
| `tichhop/KhachHangGhn` | Gọi REST API GHN bằng `RestClient` (header `Token`, `ShopId`); đối chiếu địa chỉ 2 cấp; dựng body vận đơn |
| `tichhop/VanChuyenGhn` | Nghiệp vụ tạo/huỷ/đồng bộ vận đơn, áp trạng thái GHN vào đơn |
| `controller/api/GhnWebhookController` | Nhận webhook GHN (kiểm tra header `X-BepNe-Key` nếu có) |
| `controller/admin/AdminVanChuyenController` | 4 nút GHN trong trang chi tiết đơn |
| `entity/TrangThaiGhn` | Bảng dịch trạng thái GHN sang tiếng Việt |
| `test/.../SePayWebhookTest`, `test/.../KhachHangGhnTest` | Kiểm thử tự động (đủ tiền, thiếu tiền, sai khoá, HMAC, gửi trùng; địa chỉ GHN, body vận đơn, lỗi GHN, chuyển trạng thái) |

Ý liên hệ môn học:

- Chương lập trình mạng: web vừa là **HTTP client** (gọi REST GHN) vừa là **HTTP server** nhận webhook (SePay và GHN chủ động gọi vào).
- Bảo mật:
  - Webhook xác thực bằng khoá và chữ ký HMAC.
  - So khoá bằng `MessageDigest.isEqual` để chống dò theo thời gian.
  - Khoá không nằm trong mã nguồn.
- Đồng thời: SePay có thể gửi lại nhiều lần, nên dùng cột UNIQUE cùng khoá dòng để không xác nhận trùng.

---

## 6. Gợi ý phân công

| Người | Việc |
|---|---|
| Thành viên A | Mục 1 (SePay): đăng ký, cấu hình, chạy S1–S7, chụp ảnh |
| Thành viên B | Mục 2 (GHN): đăng ký, lấy Token/ShopId, chạy G1–G6, chụp ảnh, bổ sung vị trí ShopId vào mục 2.3 |
| Cả nhóm | Gặp lỗi ngoài bảng mục 4 thì gửi ảnh và dòng log cho nhóm trưởng |

Khi xong: commit **chỉ thư mục ảnh minh chứng** và phần sửa trong file hướng dẫn này. Không commit `tich-hop.properties`.

---

## Nguồn tài liệu đã đối chiếu

SePay:

- https://developer.sepay.vn/en/tien-ich-khac/test-mode
- https://developer.sepay.vn/en/tien-ich-khac/test-mode/mo-phong-giao-dich
- https://developer.sepay.vn/en/tien-ich-khac/test-mode/mo-phong-tao-ma-vietqr
- https://developer.sepay.vn/en/tien-ich-khac/test-mode/tao-webhook
- https://developer.sepay.vn/en/tien-ich-khac/test-mode/han-muc
- https://developer.sepay.vn/en/sepay-webhooks/tich-hop-webhook
- https://developer.sepay.vn/en/sepay-webhooks/xac-thuc
- https://developer.sepay.vn/en/sepay-webhooks/xu-ly-loi
- https://developer.sepay.vn/en/sepay-webhooks/cau-hinh-ma-thanh-toan
- https://developer.sepay.vn/en/agents/cli/cai-dat
- https://developer.sepay.vn/en/agents/cli/webhook-listen
- https://developer.sepay.vn/en/agents/cli/sandbox

GHN:

- https://developer.ghn.vn/en/docs/getting-started/overview
- https://developer.ghn.vn/en/docs/token/get-token
- https://developer.ghn.vn/en/docs/order/create.md
- https://developer.ghn.vn/en/docs/order/preview.md
- https://developer.ghn.vn/en/docs/order/info.md
- https://developer.ghn.vn/en/docs/order/cancel.md
- https://developer.ghn.vn/en/docs/master-data/get-province-new.md
- https://developer.ghn.vn/en/docs/master-data/get-ward-new.md
- https://developer.ghn.vn/en/docs/master-data/order-status.md
- https://developer.ghn.vn/en/docs/webhook/callback-order-status.md

Những điểm tài liệu **chưa ghi rõ** (cần nhóm xác nhận khi làm thật):

- Test mode SePay có miễn phí hay không.
- Vị trí hiển thị ShopId trên developer.ghn.dev.
- Staging GHN có cho tự chuyển trạng thái đơn hay không.
