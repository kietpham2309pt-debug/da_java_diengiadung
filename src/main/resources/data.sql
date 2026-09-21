INSERT IGNORE INTO loai_san_pham (id, ma_loai, ten_loai, mo_ta) VALUES
(1, 'bep-dien-tu', 'Bếp điện từ', 'Ghép vùng từ với vùng hồng ngoại, dùng được cả nồi đất và nồi nhôm.'),
(2, 'bep-hong-ngoai', 'Bếp hồng ngoại', 'Nhiệt toả đều từ vòng sợi đốt, rất hợp món kho và món nướng lâu.'),
(3, 'hut-mui', 'Máy hút mùi', 'Ống khói, âm tủ và đảo. Hút sạch khói dầu, chạy êm dưới 45 dB.'),
(4, 'rua-chen', 'Máy rửa chén', 'Loại độc lập và âm tủ, 8 tới 14 bộ, sấy khô bằng khoáng zeolite.'),
(5, 'lo-nuong', 'Lò nướng', 'Âm tủ 60 cm, nướng đối lưu nhiều tầng, có chế độ tự vệ sinh.'),
(6, 'lo-vi-song', 'Lò vi sóng', 'Âm tủ và để bàn, kèm nướng thanh nhiệt, khay xoay tháo rời được.');

INSERT IGNORE INTO thuong_hieu (id, ten_thuong_hieu, xuat_xu) VALUES
(1, 'Bosch', 'Đức'),
(2, 'Kaff', 'Đức'),
(3, 'Kocher', 'Đức'),
(4, 'Chef''s', 'Việt Nam');

INSERT IGNORE INTO nha_cung_cap (id, ten_nha_cung_cap, so_dien_thoai, dia_chi, email) VALUES
(1, 'Công ty TNHH Thiết bị bếp Minh Long', '02838123456', '25 Trường Chinh, Tân Bình, TP.HCM', 'minhlong@gmail.com'),
(2, 'Công ty CP Điện máy Hoàng Gia', '02839987654', '178 Cộng Hòa, Tân Bình, TP.HCM', 'hoanggia@gmail.com'),
(3, 'Nhà phân phối Bếp Việt', '02837654321', '450 Nguyễn Thị Thập, Quận 7, TP.HCM', 'bepviet@gmail.com');

INSERT IGNORE INTO san_pham
(id, ma_san_pham, ten_san_pham, gia_ban, gia_khuyen_mai, so_luong_ton, hinh_anh, cong_suat, bao_hanh_thang, mo_ta, thong_so_ky_thuat, dang_ban, loai_san_pham_id, thuong_hieu_id) VALUES
(1, 'BDT-BOS-001', 'Bếp điện từ Bosch PPI82560MS', 26900000, 22900000, 6, NULL, '7000W', 36, 'Một vùng từ, một vùng hồng ngoại, mặt kính Schott Ceran', 'Khoét đá 560x490 mm; 17 mức công suất; báo nhiệt dư; khoá an toàn trẻ em', 1, 1, 1),
(2, 'BDT-KAF-001', 'Bếp điện từ Kaff KF-IC3801', 14900000, 11900000, 14, NULL, '4000W', 24, 'Bếp đôi kết hợp vùng từ và vùng hồng ngoại', 'Khoét đá 680x390 mm; mặt kính Ceramic; hẹn giờ 99 phút; 9 mức công suất', 1, 1, 2),
(3, 'BDT-KOC-001', 'Bếp điện từ Kocher DIB-888', 16500000, 13900000, 7, NULL, '4300W', 24, 'Vùng từ inverter và vùng hồng ngoại sợi carbon', 'Khoét đá 690x390 mm; cảm ứng trượt; tự ngắt khi quá nhiệt', 1, 1, 3),
(4, 'BDT-CHE-001', 'Bếp điện từ Chef''s EH-MIX321', 11900000, 8990000, 16, NULL, '4000W', 24, 'Giá hợp lý, phù hợp căn hộ chung cư', 'Khoét đá 680x390 mm; mặt kính Schott Ceran; khoá bàn phím', 1, 1, 4),

(5, 'BHN-KAF-001', 'Bếp hồng ngoại đôi Kaff KF-073IC', 9900000, 7990000, 11, NULL, '3600W', 24, 'Dùng được mọi loại nồi, kể cả nồi đất', 'Khoét đá 680x390 mm; sợi đốt carbon; báo nhiệt dư; hẹn giờ', 1, 2, 2),
(6, 'BHN-KAF-002', 'Bếp hồng ngoại Kaff KF-330CC', 7900000, NULL, 9, NULL, '3200W', 24, 'Hai vùng nấu, viền inox chống xước', 'Khoét đá 660x380 mm; 9 mức công suất; khoá trẻ em', 1, 2, 2),
(7, 'BHN-KOC-001', 'Bếp hồng ngoại đôi Kocher DC-666', 8900000, 6990000, 13, NULL, '3400W', 24, 'Mặt kính chịu lực, viền inox', 'Khoét đá 690x390 mm; hẹn giờ 180 phút; cảm biến nhiệt', 1, 2, 3),
(8, 'BHN-CHE-001', 'Bếp hồng ngoại đơn Chef''s EH-HL2000A', 3990000, 2990000, 25, NULL, '2000W', 24, 'Bếp đơn để bàn, tiện khi nấu thêm món', 'Sợi đốt carbon; 6 chế độ nấu; mặt kính chịu nhiệt', 1, 2, 4),

(9, 'HM-BOS-001', 'Máy hút mùi Bosch DWK98JQ60B', 32900000, 27900000, 4, NULL, '730 m3/h', 36, 'Hút mùi áp tường dáng kính cong', 'Lưu lượng 730 m3/h; độ ồn 58 dB; đèn LED; 3 mức tốc độ', 1, 3, 1),
(10, 'HM-KAF-001', 'Máy hút mùi âm tủ Kaff KF-GB705', 8900000, 6990000, 15, NULL, '1000 m3/h', 24, 'Hút mùi âm tủ 70 cm, giấu gọn trong tủ bếp', 'Lưu lượng 1000 m3/h; độ ồn 52 dB; lọc than hoạt tính', 1, 3, 2),
(11, 'HM-KAF-002', 'Máy hút mùi đảo Kaff KF-IS900H', 12900000, 10500000, 8, NULL, '1200 m3/h', 24, 'Dành cho bếp đảo giữa nhà', 'Lưu lượng 1200 m3/h; điều khiển cảm ứng; đèn LED 2x3W', 1, 3, 2),
(12, 'HM-KOC-001', 'Máy hút mùi Kocher K-8270', 7900000, 5990000, 19, NULL, '950 m3/h', 24, 'Hút mùi cổ điển 70 cm, dễ lắp thay thế', 'Lưu lượng 950 m3/h; 3 mức tốc độ; lưới lọc mỡ inox', 1, 3, 3),
(13, 'HM-CHE-001', 'Máy hút mùi kính cong Chef''s EH-R906E7', 6900000, 4990000, 21, NULL, '850 m3/h', 24, 'Kính cong 90 cm, hợp bếp hiện đại', 'Lưu lượng 850 m3/h; độ ồn 55 dB; điều khiển phím bấm', 1, 3, 4),

(14, 'MRC-BOS-001', 'Máy rửa chén Bosch SMS4HVI33E', 25900000, 21900000, 5, NULL, '2400W', 36, 'Máy rửa chén độc lập 13 bộ', '13 bộ chén; 6 chương trình; sấy PerfectDry; cảm biến AquaSensor', 1, 4, 1),
(15, 'MRC-BOS-002', 'Máy rửa chén âm tủ Bosch SMV4HVX33E', 29900000, 26500000, 3, NULL, '2400W', 36, 'Máy rửa chén âm tủ toàn phần', '13 bộ chén; 6 chương trình; khoá trẻ em; đèn báo sàn', 1, 4, 1),
(16, 'MRC-KAF-001', 'Máy rửa chén bán âm Kaff KF-BISW12', 18900000, 15900000, 6, NULL, '1900W', 24, 'Máy rửa chén bán âm 12 bộ', '12 bộ chén; 5 chương trình rửa; sấy nhiệt; chống tràn', 1, 4, 2),
(17, 'MRC-CHE-001', 'Máy rửa chén mini Chef''s EH-DW401E', 14900000, 11900000, 10, NULL, '1800W', 24, 'Máy rửa chén để bàn 8 bộ cho gia đình nhỏ', '8 bộ chén; 5 chương trình; tiết kiệm nước; vỏ thép sơn tĩnh điện', 1, 4, 4),

(18, 'LN-BOS-001', 'Lò nướng âm tủ Bosch HBF534ES0A', 19900000, 16900000, 7, NULL, '66 lít', 36, 'Lò nướng đối lưu 3D, nướng đều nhiều tầng', 'Dung tích 66 lít; 7 chế độ nướng; cửa kính 2 lớp; khay men chống dính', 1, 5, 1),
(19, 'LN-KAF-001', 'Lò nướng âm tủ Kaff KF-BJ01', 12900000, 10900000, 9, NULL, '70 lít', 24, 'Lò nướng 70 lít, có chế độ tự vệ sinh', 'Dung tích 70 lít; 9 chế độ; hẹn giờ 120 phút; đèn chiếu trong lò', 1, 5, 2),
(20, 'LN-KOC-001', 'Lò nướng Kocher KO-72T', 10900000, 8900000, 12, NULL, '72 lít', 24, 'Lò nướng âm tủ 60 cm, ray trượt 3 tầng', 'Dung tích 72 lít; 8 chế độ; quạt đối lưu; khay xoay gà', 1, 5, 3),
(21, 'LN-CHE-001', 'Lò nướng Chef''s EH-BO70', 8900000, 6990000, 14, NULL, '70 lít', 24, 'Lò nướng gia đình, dễ dùng', 'Dung tích 70 lít; 6 chế độ; núm cơ; khay nướng và vỉ inox', 1, 5, 4),

(22, 'LVS-BOS-001', 'Lò vi sóng âm tủ Bosch BFL523MS0', 15900000, 13500000, 5, NULL, '20 lít', 36, 'Lò vi sóng âm tủ, mặt thép không gỉ', 'Dung tích 20 lít; 800W; 5 mức công suất; khay xoay 25 cm', 1, 6, 1),
(23, 'LVS-KAF-001', 'Lò vi sóng Kaff KF-MW21', 6900000, 5490000, 11, NULL, '21 lít', 24, 'Lò vi sóng kèm nướng thanh nhiệt', 'Dung tích 21 lít; 900W; có nướng; bảng cảm ứng', 1, 6, 2),
(24, 'LVS-KOC-001', 'Lò vi sóng Kocher KO-25MW', 5900000, 4590000, 13, NULL, '25 lít', 24, 'Lò vi sóng để bàn dung tích lớn', 'Dung tích 25 lít; 900W; 8 thực đơn nấu nhanh', 1, 6, 3),
(25, 'LVS-CHE-001', 'Lò vi sóng Chef''s EH-MW25', 4590000, 3490000, 18, NULL, '25 lít', 24, 'Lò vi sóng giá tốt cho gia đình', 'Dung tích 25 lít; 800W; núm cơ; rã đông theo cân nặng', 1, 6, 4);
