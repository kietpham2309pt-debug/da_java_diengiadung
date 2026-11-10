# Tài liệu TV4, Ngô Hoàng Anh Phát: Giỏ hàng, đặt hàng, lịch sử đơn và hồ sơ

| Tệp | Nội dung |
|---|---|
| `use-case-tv4.png`, `use-case-tv4.puml` | Sơ đồ use case và mã nguồn PlantUML |
| `dac-ta-use-case-tv4.md` | Đặc tả UC-TV4-01 Đặt hàng và UC-TV4-02 Huỷ đơn hàng |
| `hoat-dong-dat-hang.png`, `hoat-dong-dat-hang.puml` | Sơ đồ hoạt động quy trình Đặt hàng |
| `mockup/` | Mockup giỏ hàng, đặt hàng, đơn hàng của tôi |
| `man-hinh/` | Ảnh chụp màn hình chạy thật |
| `khao-sat.md` | Chương khảo sát website điện máy |

Danh sách chức năng của TV4 nằm trong `docs/chuc-nang.md` chung của nhóm.

Vẽ lại sơ đồ sau khi sửa `.puml`:

```bash
java -jar plantuml.jar -charset UTF-8 -tpng docs/tv4/use-case-tv4.puml docs/tv4/hoat-dong-dat-hang.puml
```
