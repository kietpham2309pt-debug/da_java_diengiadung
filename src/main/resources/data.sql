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
(1, 'BDT-BOS-001', 'Bếp điện từ Bosch PPI82560MS', 26900000, 22900000, 6, NULL, '7000W', 36, 'Một vùng từ, một vùng hồng ngoại, mặt kính Schott Ceran', 'Kích thước khoét đá: 560 x 490 mm; Số mức công suất: 17; Mặt kính: Schott Ceran; Báo nhiệt dư; Khoá an toàn trẻ em', 1, 1, 1),
(2, 'BDT-KAF-001', 'Bếp điện từ Kaff KF-IC3801', 14900000, 11900000, 14, NULL, '4000W', 24, 'Bếp đôi kết hợp vùng từ và vùng hồng ngoại', 'Kích thước khoét đá: 680 x 390 mm; Mặt kính: Ceramic; Số mức công suất: 9; Hẹn giờ 99 phút; Điều khiển cảm ứng', 1, 1, 2),
(3, 'BDT-KOC-001', 'Bếp điện từ Kocher DIB-888', 16500000, 13900000, 7, NULL, '4300W', 24, 'Vùng từ inverter và vùng hồng ngoại sợi carbon', 'Kích thước khoét đá: 690 x 390 mm; Vùng nấu: Từ inverter và hồng ngoại carbon; Điều khiển cảm ứng trượt; Tự ngắt khi quá nhiệt', 1, 1, 3),
(4, 'BDT-CHE-001', 'Bếp điện từ Chef''s EH-MIX321', 11900000, 8990000, 16, NULL, '4000W', 24, 'Giá hợp lý, phù hợp căn hộ chung cư', 'Kích thước khoét đá: 680 x 390 mm; Mặt kính: Schott Ceran; Số vùng nấu: 2; Khoá bàn phím', 1, 1, 4),

(5, 'BHN-KAF-001', 'Bếp hồng ngoại đôi Kaff KF-073IC', 9900000, 7990000, 11, NULL, '3600W', 24, 'Dùng được mọi loại nồi, kể cả nồi đất', 'Kích thước khoét đá: 680 x 390 mm; Mâm nhiệt: Sợi đốt carbon; Báo nhiệt dư; Hẹn giờ tắt bếp', 1, 2, 2),
(6, 'BHN-KAF-002', 'Bếp hồng ngoại Kaff KF-330CC', 7900000, NULL, 9, NULL, '3200W', 24, 'Hai vùng nấu, viền inox chống xước', 'Kích thước khoét đá: 660 x 380 mm; Số mức công suất: 9; Viền bếp: Inox chống xước; Khoá trẻ em', 1, 2, 2),
(7, 'BHN-KOC-001', 'Bếp hồng ngoại đôi Kocher DC-666', 8900000, 6990000, 13, NULL, '3400W', 24, 'Mặt kính chịu lực, viền inox', 'Kích thước khoét đá: 690 x 390 mm; Mặt kính: Chịu lực, viền inox; Hẹn giờ 180 phút; Cảm biến nhiệt', 1, 2, 3),
(8, 'BHN-CHE-001', 'Bếp hồng ngoại đơn Chef''s EH-HL2000A', 3990000, 2990000, 25, NULL, '2000W', 24, 'Bếp đơn để bàn, tiện khi nấu thêm món', 'Kiểu lắp: Để bàn; Mâm nhiệt: Sợi đốt carbon; Số chế độ nấu: 6; Mặt kính chịu nhiệt', 1, 2, 4),

(9, 'HM-BOS-001', 'Máy hút mùi Bosch DWK98JQ60B', 32900000, 27900000, 4, NULL, '730 m3/h', 36, 'Hút mùi áp tường dáng kính cong', 'Lưu lượng hút: 730 m3/h; Độ ồn: 58 dB; Số mức tốc độ: 3; Chiều rộng: 90 cm; Đèn LED chiếu sáng', 1, 3, 1),
(10, 'HM-KAF-001', 'Máy hút mùi âm tủ Kaff KF-GB705', 8900000, 6990000, 15, NULL, '1000 m3/h', 24, 'Hút mùi âm tủ 70 cm, giấu gọn trong tủ bếp', 'Lưu lượng hút: 1000 m3/h; Độ ồn: 52 dB; Chiều rộng: 70 cm; Lọc than hoạt tính', 1, 3, 2),
(11, 'HM-KAF-002', 'Máy hút mùi đảo Kaff KF-IS900H', 12900000, 10500000, 8, NULL, '1200 m3/h', 24, 'Dành cho bếp đảo giữa nhà', 'Lưu lượng hút: 1200 m3/h; Kiểu lắp: Đảo giữa nhà; Đèn LED 2 x 3W; Điều khiển cảm ứng', 1, 3, 2),
(12, 'HM-KOC-001', 'Máy hút mùi Kocher K-8270', 7900000, 5990000, 19, NULL, '950 m3/h', 24, 'Hút mùi cổ điển 70 cm, dễ lắp thay thế', 'Lưu lượng hút: 950 m3/h; Chiều rộng: 70 cm; Số mức tốc độ: 3; Lưới lọc mỡ inox', 1, 3, 3),
(13, 'HM-CHE-001', 'Máy hút mùi kính cong Chef''s EH-R906E7', 6900000, 4990000, 21, NULL, '850 m3/h', 24, 'Kính cong 90 cm, hợp bếp hiện đại', 'Lưu lượng hút: 850 m3/h; Độ ồn: 55 dB; Chiều rộng: 90 cm; Điều khiển phím bấm', 1, 3, 4),

(14, 'MRC-BOS-001', 'Máy rửa chén Bosch SMS4HVI33E', 25900000, 21900000, 5, NULL, '2400W', 36, 'Máy rửa chén độc lập 13 bộ', 'Sức chứa: 13 bộ chén; Số chương trình: 6; Kiểu lắp: Độc lập; Sấy PerfectDry; Cảm biến nước AquaSensor', 1, 4, 1),
(15, 'MRC-BOS-002', 'Máy rửa chén âm tủ Bosch SMV4HVX33E', 29900000, 26500000, 3, NULL, '2400W', 36, 'Máy rửa chén âm tủ toàn phần', 'Sức chứa: 13 bộ chén; Số chương trình: 6; Kiểu lắp: Âm tủ toàn phần; Khoá trẻ em; Đèn báo chiếu sàn', 1, 4, 1),
(16, 'MRC-KAF-001', 'Máy rửa chén bán âm Kaff KF-BISW12', 18900000, 15900000, 6, NULL, '1900W', 24, 'Máy rửa chén bán âm 12 bộ', 'Sức chứa: 12 bộ chén; Số chương trình: 5; Kiểu lắp: Bán âm; Sấy nhiệt; Chống tràn nước', 1, 4, 2),
(17, 'MRC-CHE-001', 'Máy rửa chén mini Chef''s EH-DW401E', 14900000, 11900000, 10, NULL, '1800W', 24, 'Máy rửa chén để bàn 8 bộ cho gia đình nhỏ', 'Sức chứa: 8 bộ chén; Số chương trình: 5; Kiểu lắp: Để bàn; Tiết kiệm nước', 1, 4, 4),

(18, 'LN-BOS-001', 'Lò nướng âm tủ Bosch HBF534ES0A', 19900000, 16900000, 7, NULL, '66 lít', 36, 'Lò nướng đối lưu 3D, nướng đều nhiều tầng', 'Dung tích: 66 lít; Số chế độ nướng: 7; Cửa lò: Kính 2 lớp; Nướng đối lưu 3D; Khay men chống dính', 1, 5, 1),
(19, 'LN-KAF-001', 'Lò nướng âm tủ Kaff KF-BJ01', 12900000, 10900000, 9, NULL, '70 lít', 24, 'Lò nướng 70 lít, có chế độ tự vệ sinh', 'Dung tích: 70 lít; Số chế độ nướng: 9; Hẹn giờ 120 phút; Đèn chiếu trong lò; Tự vệ sinh', 1, 5, 2),
(20, 'LN-KOC-001', 'Lò nướng Kocher KO-72T', 10900000, 8900000, 12, NULL, '72 lít', 24, 'Lò nướng âm tủ 60 cm, ray trượt 3 tầng', 'Dung tích: 72 lít; Số chế độ nướng: 8; Ray trượt: 3 tầng; Quạt đối lưu; Xiên nướng gà', 1, 5, 3),
(21, 'LN-CHE-001', 'Lò nướng Chef''s EH-BO70', 8900000, 6990000, 14, NULL, '70 lít', 24, 'Lò nướng gia đình, dễ dùng', 'Dung tích: 70 lít; Số chế độ nướng: 6; Điều khiển: Núm vặn cơ; Kèm khay nướng và vỉ inox', 1, 5, 4),

(22, 'LVS-BOS-001', 'Lò vi sóng âm tủ Bosch BFL523MS0', 15900000, 13500000, 5, NULL, '20 lít', 36, 'Lò vi sóng âm tủ, mặt thép không gỉ', 'Dung tích: 20 lít; Công suất vi sóng: 800W; Số mức công suất: 5; Đường kính khay xoay: 25 cm', 1, 6, 1),
(23, 'LVS-KAF-001', 'Lò vi sóng Kaff KF-MW21', 6900000, 5490000, 11, NULL, '21 lít', 24, 'Lò vi sóng kèm nướng thanh nhiệt', 'Dung tích: 21 lít; Công suất vi sóng: 900W; Nướng thanh nhiệt; Bảng điều khiển cảm ứng', 1, 6, 2),
(24, 'LVS-KOC-001', 'Lò vi sóng Kocher KO-25MW', 5900000, 4590000, 13, NULL, '25 lít', 24, 'Lò vi sóng để bàn dung tích lớn', 'Dung tích: 25 lít; Công suất vi sóng: 900W; Thực đơn nấu nhanh: 8 món', 1, 6, 3),
(25, 'LVS-CHE-001', 'Lò vi sóng Chef''s EH-MW25', 4590000, 3490000, 18, NULL, '25 lít', 24, 'Lò vi sóng giá tốt cho gia đình', 'Dung tích: 25 lít; Công suất vi sóng: 800W; Điều khiển: Núm vặn cơ; Rã đông theo cân nặng', 1, 6, 4),

(26, 'BDT-BOS-002', 'Bếp điện từ ba vùng Bosch PUJ631BB2E', 21900000, 18900000, 9, NULL, '4600W', 36, 'Ba vùng từ, nấu nhanh PowerBoost cho nồi lẩu và chảo xào', 'Kích thước khoét đá: 560 x 490 mm; Số vùng nấu: 3; Số mức công suất: 17; Nấu nhanh PowerBoost; Hẹn giờ cho từng vùng; Khoá an toàn trẻ em', 1, 1, 1),
(27, 'BDT-KAF-002', 'Bếp điện từ ba vùng Kaff KF-IH6003', 19900000, 15900000, 5, NULL, '5800W', 24, 'Bếp ba vùng từ inverter, mặt kính rộng 75 cm', 'Kích thước khoét đá: 750 x 450 mm; Số vùng nấu: 3; Công nghệ: Inverter; Cảm ứng trượt; Báo nhiệt dư', 1, 1, 2),
(28, 'BHN-KOC-002', 'Bếp hồng ngoại đơn Kocher DC-111', 3290000, NULL, 0, NULL, '2000W', 12, 'Bếp đơn để bàn nhỏ gọn, dùng được mọi loại nồi', 'Kiểu lắp: Để bàn; Mâm nhiệt: Sợi đốt carbon; Số chế độ nấu: 5; Dùng được mọi loại nồi', 1, 2, 3),
(29, 'HM-BOS-002', 'Máy hút mùi âm tủ Bosch DFS067A51', 15900000, 13900000, 2, NULL, '700 m3/h', 36, 'Hút mùi kéo trượt 60 cm, giấu kín trong tủ trên', 'Lưu lượng hút: 700 m3/h; Chiều rộng: 60 cm; Kiểu lắp: Âm tủ kéo trượt; Đèn LED; Lọc mỡ kim loại rửa được', 1, 3, 1),
(30, 'MRC-KOC-001', 'Máy rửa chén Kocher KDEU-8835', 17900000, 13900000, 7, NULL, '2000W', 24, 'Máy rửa chén độc lập 14 bộ, hẹn giờ rửa ban đêm', 'Sức chứa: 14 bộ chén; Số chương trình: 8; Kiểu lắp: Độc lập; Sấy khí nóng; Hẹn giờ rửa 24 giờ', 1, 4, 3),
(31, 'MRC-BOS-003', 'Máy rửa chén bán âm Bosch SMI6ZCS49E', 34900000, 31900000, 4, NULL, '2400W', 36, 'Máy rửa chén bán âm cao cấp, sấy khoáng Zeolith', 'Sức chứa: 14 bộ chén; Số chương trình: 8; Kiểu lắp: Bán âm; Sấy khoáng Zeolith; Điều khiển qua ứng dụng Home Connect', 1, 4, 1),
(32, 'LN-BOS-002', 'Lò nướng kết hợp hấp Bosch HRG5785S6', 42900000, 37900000, 3, NULL, '71 lít', 36, 'Lò nướng có hấp bổ sung, tự vệ sinh nhiệt phân', 'Dung tích: 71 lít; Số chế độ nướng: 13; Hấp bổ sung: Có; Tự vệ sinh nhiệt phân; Cảm biến nhiệt độ lõi', 1, 5, 1),
(33, 'LN-KAF-002', 'Lò nướng để bàn Kaff KF-OT45', 3490000, 2790000, 20, NULL, '45 lít', 12, 'Lò nướng để bàn 45 lít cho gia đình 3 tới 4 người', 'Dung tích: 45 lít; Kiểu lắp: Để bàn; Nhiệt độ tối đa: 230 độ C; Hẹn giờ 60 phút; Xiên nướng gà', 1, 5, 2),
(34, 'LVS-BOS-002', 'Lò vi sóng kết hợp nướng Bosch BEL554MS0', 18900000, NULL, 6, NULL, '25 lít', 36, 'Lò vi sóng âm tủ kèm nướng, mặt thép không gỉ', 'Dung tích: 25 lít; Công suất vi sóng: 900W; Kiểu lắp: Âm tủ; Nướng thanh nhiệt 1200W; Rã đông tự động', 1, 6, 1),
(35, 'LVS-KAF-002', 'Lò vi sóng âm tủ Kaff KF-MW25B', 8900000, 7490000, 8, NULL, '25 lít', 24, 'Lò vi sóng âm tủ mặt kính đen', 'Dung tích: 25 lít; Công suất vi sóng: 900W; Kiểu lắp: Âm tủ; Mặt kính đen; Khoá trẻ em', 1, 6, 2),
(36, 'LVS-CHE-002', 'Lò vi sóng có nướng Chef''s EH-MW801B', 3290000, 2590000, 0, NULL, '20 lít', 12, 'Mẫu cũ đã ngừng kinh doanh, giữ lại để tra cứu đơn hàng', 'Dung tích: 20 lít; Công suất vi sóng: 700W; Nướng thanh nhiệt; Rã đông theo thời gian', 0, 6, 4);
