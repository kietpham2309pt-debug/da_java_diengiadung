INSERT IGNORE INTO loai_san_pham (id, ten_loai, mo_ta) VALUES
(1, 'Bếp từ', 'Bếp nấu bằng từ trường, tiết kiệm điện, an toàn'),
(2, 'Bếp điện từ', 'Kết hợp vùng nấu từ và vùng nấu hồng ngoại'),
(3, 'Bếp hồng ngoại', 'Dùng được mọi loại nồi, làm nóng bằng bức xạ nhiệt'),
(4, 'Máy hút mùi', 'Hút mùi và khói cho khu vực bếp'),
(5, 'Máy rửa chén', 'Rửa và sấy chén bát tự động'),
(6, 'Máy giặt', 'Máy giặt lồng ngang cho gia đình');

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
(1, 'BT-BOS-001', 'Bếp từ đôi Bosch PID675DC1E', 24500000, 19900000, 12, NULL, '7400W', 36, 'Bếp từ đôi âm bàn, mặt kính Schott Ceran', 'Kích thước khoét đá 560x490 mm; 17 mức công suất; chức năng PowerBoost', 1, 1, 1),
(2, 'BT-BOS-002', 'Bếp từ ba Bosch PXY875KW1E', 42900000, 37500000, 5, NULL, '11100W', 36, 'Bếp từ ba vùng nấu, vùng linh hoạt FlexInduction', 'Kích thước khoét đá 780x500 mm; cảm ứng TFT; khoá an toàn trẻ em', 1, 1, 1),
(3, 'BT-KAF-001', 'Bếp từ đôi Kaff KF-FL101II', 13800000, 11500000, 18, NULL, '4000W', 24, 'Bếp từ đôi nhập khẩu, điều khiển cảm ứng trượt', 'Kích thước khoét đá 680x390 mm; 9 mức công suất; hẹn giờ 99 phút', 1, 1, 2),
(4, 'BT-KOC-001', 'Bếp từ đôi Kocher DI-889GE', 15900000, 12900000, 9, NULL, '4200W', 24, 'Mặt kính Kanger, inverter tiết kiệm điện', 'Kích thước khoét đá 690x390 mm; cảm biến nhiệt; tự ngắt khi quá nhiệt', 1, 1, 3),
(5, 'BT-CHE-001', 'Bếp từ đôi Chef''s EH-DIH330', 12900000, 9990000, 22, NULL, '4000W', 24, 'Bếp từ đôi dành cho gia đình Việt', 'Kích thước khoét đá 680x390 mm; 9 mức công suất; khoá bàn phím', 1, 1, 4),
(6, 'BT-CHE-002', 'Bếp từ đơn Chef''s EH-IH25A', 4590000, NULL, 30, NULL, '2000W', 24, 'Bếp từ đơn nhỏ gọn, dễ di chuyển', 'Mặt kính chịu nhiệt; 8 chế độ nấu; hẹn giờ 180 phút', 1, 1, 4),
(7, 'BDT-BOS-001', 'Bếp điện từ Bosch PPI82560MS', 26900000, 22900000, 6, NULL, '7000W', 36, 'Một vùng từ, một vùng hồng ngoại', 'Kích thước khoét đá 560x490 mm; báo nhiệt dư; khoá trẻ em', 1, 2, 1),
(8, 'BDT-KAF-001', 'Bếp điện từ Kaff KF-IC3801', 14900000, 11900000, 14, NULL, '4000W', 24, 'Bếp đôi kết hợp từ và hồng ngoại', 'Kích thước khoét đá 680x390 mm; mặt kính Ceramic; hẹn giờ 99 phút', 1, 2, 2),
(9, 'BDT-KOC-001', 'Bếp điện từ Kocher DIB-888', 16500000, 13900000, 7, NULL, '4300W', 24, 'Vùng từ inverter và vùng hồng ngoại sợi carbon', 'Kích thước khoét đá 690x390 mm; cảm ứng trượt; 9 mức công suất', 1, 2, 3),
(10, 'BDT-CHE-001', 'Bếp điện từ Chef''s EH-MIX321', 11900000, 8990000, 16, NULL, '4000W', 24, 'Giá hợp lý, phù hợp căn hộ', 'Kích thước khoét đá 680x390 mm; mặt kính Schott Ceran', 1, 2, 4),
(11, 'BHN-KAF-001', 'Bếp hồng ngoại đôi Kaff KF-073IC', 9900000, 7990000, 11, NULL, '3600W', 24, 'Dùng được mọi loại nồi', 'Kích thước khoét đá 680x390 mm; sợi đốt carbon; báo nhiệt dư', 1, 3, 2),
(12, 'BHN-KOC-001', 'Bếp hồng ngoại đôi Kocher DC-666', 8900000, 6990000, 13, NULL, '3400W', 24, 'Mặt kính chịu lực, viền inox', 'Kích thước khoét đá 690x390 mm; 9 mức công suất; hẹn giờ', 1, 3, 3),
(13, 'BHN-CHE-001', 'Bếp hồng ngoại đơn Chef''s EH-HL2000A', 3990000, 2990000, 25, NULL, '2000W', 24, 'Bếp đơn để bàn, tiện lợi', 'Sợi đốt carbon; 6 chế độ nấu; mặt kính chịu nhiệt', 1, 3, 4),
(14, 'HM-BOS-001', 'Máy hút mùi Bosch DWK98JQ60B', 32900000, 27900000, 4, NULL, '730 m3/h', 36, 'Hút mùi áp tường dáng kính cong', 'Lưu lượng hút 730 m3/h; độ ồn 58 dB; đèn LED; 3 mức tốc độ', 1, 4, 1),
(15, 'HM-KAF-001', 'Máy hút mùi Kaff KF-GB705', 8900000, 6990000, 15, NULL, '1000 m3/h', 24, 'Hút mùi âm tủ 70 cm', 'Lưu lượng hút 1000 m3/h; độ ồn 52 dB; lọc than hoạt tính', 1, 4, 2),
(16, 'HM-KAF-002', 'Máy hút mùi Kaff KF-IS900H', 12900000, 10500000, 8, NULL, '1200 m3/h', 24, 'Hút mùi đảo dành cho bếp giữa nhà', 'Lưu lượng hút 1200 m3/h; điều khiển cảm ứng; đèn LED', 1, 4, 2),
(17, 'HM-KOC-001', 'Máy hút mùi Kocher K-8270', 7900000, 5990000, 19, NULL, '950 m3/h', 24, 'Hút mùi cổ điển 70 cm', 'Lưu lượng hút 950 m3/h; 3 mức tốc độ; lưới lọc mỡ inox', 1, 4, 3),
(18, 'HM-CHE-001', 'Máy hút mùi Chef''s EH-R906E7', 6900000, 4990000, 21, NULL, '850 m3/h', 24, 'Hút mùi kính cong 90 cm', 'Lưu lượng hút 850 m3/h; độ ồn 55 dB; điều khiển phím bấm', 1, 4, 4),
(19, 'MRC-BOS-001', 'Máy rửa chén Bosch SMS4HVI33E', 25900000, 21900000, 5, NULL, '2400W', 36, 'Máy rửa chén độc lập 13 bộ', 'Số bộ chén 13; 6 chương trình rửa; công nghệ sấy PerfectDry', 1, 5, 1),
(20, 'MRC-BOS-002', 'Máy rửa chén âm tủ Bosch SMV4HVX33E', 29900000, 26500000, 3, NULL, '2400W', 36, 'Máy rửa chén âm tủ toàn phần', 'Số bộ chén 13; 6 chương trình; cảm biến độ bẩn AquaSensor', 1, 5, 1),
(21, 'MRC-KAF-001', 'Máy rửa chén Kaff KF-BISW12', 18900000, 15900000, 6, NULL, '1900W', 24, 'Máy rửa chén bán âm 12 bộ', 'Số bộ chén 12; 5 chương trình rửa; sấy nhiệt', 1, 5, 2),
(22, 'MRC-CHE-001', 'Máy rửa chén Chef''s EH-DW401E', 14900000, 11900000, 10, NULL, '1800W', 24, 'Máy rửa chén mini để bàn 8 bộ', 'Số bộ chén 8; 5 chương trình; tiết kiệm nước', 1, 5, 4),
(23, 'MG-BOS-001', 'Máy giặt Bosch WGG244A0SG', 21900000, 18900000, 4, NULL, '9 kg', 36, 'Máy giặt lồng ngang 9 kg', 'Khối lượng giặt 9 kg; tốc độ vắt 1400 vòng/phút; động cơ EcoSilence', 1, 6, 1),
(24, 'MG-BOS-002', 'Máy giặt sấy Bosch WNA254U0SG', 32900000, 28900000, 2, NULL, '10.5 kg', 36, 'Máy giặt sấy kết hợp', 'Giặt 10.5 kg, sấy 6 kg; tốc độ vắt 1400 vòng/phút; AutoDry', 1, 6, 1);
