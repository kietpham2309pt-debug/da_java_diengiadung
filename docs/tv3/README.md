# Tài liệu TV3, Nguyễn Thanh Tuấn: Sản phẩm và trưng bày

| Tệp | Nội dung |
|---|---|
| `bao-cao-tv3.md` | Phần báo cáo tiến độ của TV3: use case, thiết kế bảng `SanPham`, mô tả chức năng, màn hình thực thi, dữ liệu mẫu, giao diện phía khách, kiểm thử, mức độ hoàn thành, kế hoạch tuần 9 tới 14 |
| `bao-cao-tv3.docx` | Bản Word của báo cáo trên, kèm đặc tả use case ở cuối, để ghép vào báo cáo chung theo mẫu Khoa |
| `use-case-tv3.png`, `use-case-tv3.puml` | Sơ đồ use case và mã nguồn PlantUML |
| `dac-ta-use-case-tv3.md` | Đặc tả UC-SP-03 Tìm kiếm sản phẩm và UC-SP-08 Thêm sản phẩm |
| `mockup/` | Mockup 3 màn hình chính: danh sách sản phẩm, chi tiết sản phẩm, quản lý sản phẩm |
| `man-hinh/` | 23 ảnh chụp màn hình chạy thật, dùng cho báo cáo |
| `kiem-thu-cheo-tv4.md` | Kết quả và bộ ca kiểm thử chéo module TV4 |
| `kich-ban-demo-tv3.md` | Kịch bản demo 1 phút và câu hỏi chuẩn bị |

Danh sách chức năng của TV3 nằm trong `docs/chuc-nang.md` chung của nhóm.

Vẽ lại sơ đồ use case sau khi sửa `.puml`:

```bash
java -jar plantuml.jar -charset UTF-8 -tpng docs/tv3/use-case-tv3.puml
```
