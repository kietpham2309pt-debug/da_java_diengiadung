USE dien_gia_dung;
GO

IF NOT EXISTS (SELECT 1 FROM loai_san_pham)
BEGIN
SET IDENTITY_INSERT loai_san_pham ON;
INSERT INTO loai_san_pham (id, ma_loai, ten_loai, mo_ta) VALUES
(1, N'bep-dien-tu', N'Bếp điện từ', N'Bếp từ đơn, đôi và ba vùng nấu, gia nhiệt nhanh, an toàn, mặt kính dễ lau chùi.'),
(2, N'bep-hong-ngoai', N'Bếp hồng ngoại', N'Bếp hồng ngoại và bếp đôi từ hồng ngoại, dùng được mọi loại nồi, kể cả nồi đất.'),
(3, N'hut-mui', N'Máy hút mùi', N'Ống khói, âm tủ và đảo. Hút sạch khói dầu, chạy êm dưới 45 dB.'),
(4, N'rua-chen', N'Máy rửa chén', N'Loại độc lập và âm tủ, 8 tới 14 bộ, sấy khô bằng khoáng zeolite.'),
(5, N'lo-nuong', N'Lò nướng', N'Âm tủ 60 cm, nướng đối lưu nhiều tầng, có chế độ tự vệ sinh.'),
(6, N'lo-vi-song', N'Lò vi sóng', N'Âm tủ và để bàn, kèm nướng thanh nhiệt, khay xoay tháo rời được.');
SET IDENTITY_INSERT loai_san_pham OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM thuong_hieu)
BEGIN
SET IDENTITY_INSERT thuong_hieu ON;
INSERT INTO thuong_hieu (id, ten_thuong_hieu, xuat_xu) VALUES
(1, N'Bosch', N'Đức'),
(2, N'Kaff', N'Đức'),
(3, N'Kocher', N'Đức'),
(4, N'Chef''s', N'Việt Nam');
SET IDENTITY_INSERT thuong_hieu OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM nha_cung_cap)
BEGIN
SET IDENTITY_INSERT nha_cung_cap ON;
INSERT INTO nha_cung_cap (id, ten_nha_cung_cap, so_dien_thoai, dia_chi, email) VALUES
(1, N'Công ty TNHH Thiết bị bếp Minh Long', N'02838123456', N'25 Trường Chinh, Tân Bình, TP.HCM', N'minhlong@gmail.com'),
(2, N'Công ty CP Điện máy Hoàng Gia', N'02839987654', N'178 Cộng Hòa, Tân Bình, TP.HCM', N'hoanggia@gmail.com'),
(3, N'Nhà phân phối Bếp Việt', N'02837654321', N'450 Nguyễn Thị Thập, Quận 7, TP.HCM', N'bepviet@gmail.com');
INSERT INTO nha_cung_cap (id, ten_nha_cung_cap, so_dien_thoai, dia_chi, email) VALUES
(4, N'Công ty TNHH Gia dụng Phương Nam', N'02838054316', N'38 Đường số 4, Quận 10, TP.HCM', N'ncc04@bepne-doitac.vn'),
(5, N'Công ty CP Thiết bị nhà bếp Sài Gòn', N'02838067895', N'45 Đường số 5, Quận 3, TP.HCM', N'ncc05@bepne-doitac.vn'),
(6, N'Đại lý Điện máy Thành Công', N'02838081474', N'52 Đường số 6, Bình Thạnh, TP.HCM', N'ncc06@bepne-doitac.vn'),
(7, N'Công ty TNHH Bếp Âu Việt', N'02838095053', N'59 Đường số 7, Gò Vấp, TP.HCM', N'ncc07@bepne-doitac.vn'),
(8, N'Nhà phân phối Gia Phát', N'02838108632', N'66 Đường số 8, Tân Phú, TP.HCM', N'ncc08@bepne-doitac.vn'),
(9, N'Công ty TNHH Kitchen Pro', N'02838122211', N'73 Đường số 9, Quận 7, TP.HCM', N'ncc09@bepne-doitac.vn'),
(10, N'Công ty CP Điện tử Đông Á', N'02838135790', N'80 Đường số 10, Quận 5, TP.HCM', N'ncc10@bepne-doitac.vn'),
(11, N'Tổng kho Thiết bị bếp Miền Nam', N'02838149369', N'87 Đường số 11, Bình Tân, TP.HCM', N'ncc11@bepne-doitac.vn'),
(12, N'Công ty TNHH Nhập khẩu Tâm Đức', N'02838162948', N'94 Đường số 12, Phú Nhuận, TP.HCM', N'ncc12@bepne-doitac.vn'),
(13, N'Đại lý Bếp Xanh', N'02838176527', N'101 Đường số 13, Thủ Đức, TP.HCM', N'ncc13@bepne-doitac.vn'),
(14, N'Công ty TNHH Thương mại An Khang', N'02838190106', N'108 Đường số 14, Quận 1, TP.HCM', N'ncc14@bepne-doitac.vn'),
(15, N'Công ty CP Gia dụng Hưng Thịnh', N'02838203685', N'115 Đường số 15, Tân Bình, TP.HCM', N'ncc15@bepne-doitac.vn'),
(16, N'Nhà phân phối Châu Âu Home', N'02838217264', N'122 Đường số 16, Quận 2, TP.HCM', N'ncc16@bepne-doitac.vn'),
(17, N'Công ty TNHH Thiết bị Minh Phát', N'02838230843', N'129 Đường số 17, Quận 11, TP.HCM', N'ncc17@bepne-doitac.vn'),
(18, N'Công ty CP Điện gia dụng Việt Nhật', N'02838244422', N'136 Đường số 18, Quận 6, TP.HCM', N'ncc18@bepne-doitac.vn'),
(19, N'Đại lý Bếp Đức Chính Hãng', N'02838258001', N'143 Đường số 19, Quận 4, TP.HCM', N'ncc19@bepne-doitac.vn'),
(20, N'Công ty TNHH Phân phối Tân Hoàng', N'02838271580', N'150 Đường số 20, Quận 8, TP.HCM', N'ncc20@bepne-doitac.vn');
SET IDENTITY_INSERT nha_cung_cap OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM san_pham)
BEGIN
SET IDENTITY_INSERT san_pham ON;
INSERT INTO san_pham (id, ma_san_pham, ten_san_pham, gia_ban, gia_khuyen_mai, so_luong_ton, hinh_anh, anh_phu, cong_suat, bao_hanh_thang, mo_ta, thong_so_ky_thuat, dang_ban, loai_san_pham_id, thuong_hieu_id) VALUES
(1, N'BDT-BOS-001', N'Bếp từ Bosch PID675DC1E', 20610000, 15240000, 6, N'/img/sp/bdt-bos-001/1.jpg', N'/img/sp/bdt-bos-001/2.jpg', N'7400W', 36, N'Bếp từ ba vùng nấu Bosch PID675DC1E là mẫu bếp lắp âm thuộc thương hiệu Bosch của Đức, sản xuất tại Tây Ban Nha, dành cho những gian bếp đề cao hiệu năng và độ chính xác khi nấu. Với tổng công suất 7400W chia cho ba vùng nấu, bếp cho phép chế biến nhiều món cùng lúc mà không phải chờ đợi giữa các lần nấu.
Thiết kế của PID675DC1E theo phong cách châu Âu tối giản với mặt kính đen sang trọng, vát cạnh tinh tế và bo viền thép không gỉ, dễ hoà hợp với nhiều phong cách nội thất. Mặt bếp làm từ kính Ceramic Schott Ceran của Đức, chịu lực và chịu nhiệt tốt, bề mặt bóng sáng nên việc vệ sinh sau khi nấu rất nhẹ nhàng.
Kích thước sản phẩm 51 x 606 x 527 mm với kích thước lắp đặt 51 x 560 x 490-500 mm. Ba vùng nấu được bố trí hợp lý: vùng bên trái linh hoạt với các đường kính 21, 26 và 32 cm cho công suất 2200/2600/3300W, vùng phải trên đường kính 14,5 cm và vùng phải dưới đường kính 21 cm, phù hợp với đủ loại nồi chảo trong bếp Việt.', N'Thương hiệu: Bosch (Đức); Model: PID675DC1E; Sản xuất tại: Tây Ban Nha; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V; Tổng công suất (W): 7400; Kiểu điều khiển: Cảm ứng; Số vùng nấu: 3; KT mặt kính (R×S mm): 606 x 527; KT khoét đá (R×S mm): 560 x 490; Công suất từng vùng (W): 3300 + 2200 + 1400; Booster: Có; CS Booster tối đa (W): 3700; Mặt kính: Schott Ceran (Đức); Công nghệ bo mạch: Inverter; An toàn: Khoá trẻ em, tự ngắt, cảnh báo nhiệt dư 2 mức, khoá chống lau, công tắc chính', 1, 1, 1),
(2, N'BDT-KAF-001', N'Bếp từ Kaff KF-IH202IC', 28800000, 20810000, 14, N'/img/sp/bdt-kaf-001/1.jpg', N'/img/sp/bdt-kaf-001/2.jpg|/img/sp/bdt-kaf-001/3.jpg', N'4500W', 36, N'Bếp điện từ Kaff KF-IH202IC là một sản phẩm cao cấp đến từ thương hiệu Kaff của Đức, được sản xuất trên dây chuyền công nghệ hiện đại theo tiêu chuẩn Châu Âu. Sản phẩm này sở hữu thiết kế 2 lò nấu từ cùng với công nghệ Booster tiên tiến, giúp rút ngắn thời gian nấu nướng và tiết kiệm thời gian cho người dùng.
Các tính năng an toàn và ưu việt của bếp điện từ Kaff KF-IH202IC bao gồm khóa trẻ em, tự động tắt bếp khi để quên và khi không có nồi, chức năng hẹn giờ, và cảm ứng chống tràn, cảm ứng quá nhiệt. Bếp điện từ Kaff KF-IH202IC có thiết kế mặt kính làm bằng kính Schott Ceran sang trọng, chống trơn trượt và có khả năng chịu nhiệt lên đến 1000 độ C, chịu sốc nhiệt tốt.
Mặt kính này là mặt kính chuyên dụng dành cho bếp điện, với chất lượng cao, rất cứng, bền và có khả năng truyền nhiệt và tản nhiệt nhanh.', N'Thương hiệu: Kaff (Đức); Model: KF-IH202IC; Sản xuất tại: Đức; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V; Tổng công suất (W): 4500; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 2; KT mặt kính (R×S mm): 730 x 430; KT khoét đá (R×S mm): 690 x 380; Công suất từng vùng (W): 2300 + 2200; Booster: Có; CS Booster tối đa (W): 3200; Mặt kính: Schott Ceran (Đức); Công nghệ bo mạch: Inverter; An toàn: Tự ngắt, nhận diện nồi, khóa trẻ em, chống tràn, chống quá nhiệt', 1, 1, 2),
(3, N'BDT-KOC-001', N'Bếp từ Kocher X-NANO 6 Plus', 19400000, 12380000, 7, N'/img/sp/bdt-koc-001/1.jpg', NULL, N'4000W', 36, N'Bếp từ Kocher X-NANO 6 Plus là mẫu bếp từ đôi lắp âm nổi bật với mặt kính X-Nano thế hệ mới, giải quyết trực tiếp nỗi lo lớn nhất của người dùng bếp từ là mặt kính bị xước dăm sau thời gian sử dụng. Sản phẩm hướng tới những gia đình muốn một chiếc bếp vừa mạnh, vừa giữ được vẻ đẹp lâu dài mà không phải nâng niu quá mức khi nấu hằng ngày.
Mặt kính tinh thể X-Nano được sản xuất theo công nghệ độc quyền từ Pháp, có khả năng kháng lại các vết xước dăm do đáy nồi chảo tốt gấp 5 lần so với kính bóng thông thường. Nhờ đặc tính bề mặt mờ, dầu mỡ và chất bẩn khó bám chặt nên việc vệ sinh nhanh hơn hẳn, đồng thời lớp hoàn thiện Black Matte sang trọng gần như không lưu lại dấu vân tay khi thao tác.
Cấu trúc vân nhám siêu vi còn tạo độ bám cơ học lý tưởng, giúp cố định nồi chảo tại tâm nhiệt và loại bỏ hiện tượng trơn trượt khi có nước hoặc dầu tràn ra mặt bếp.', N'Thương hiệu: Kocher (Đức); Model: X-NANO 6 Plus; Sản xuất tại: Thái Lan; Kiểu lắp: Âm tủ; Chất liệu: Kính / Nhôm; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 4000; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 2; KT mặt kính (R×S mm): 730 x 420; KT khoét đá (R×S mm): 695 x 395; Công suất từng vùng (W): 2000 + 2000; Booster: Có; CS Booster tối đa (W): 2300; Mặt kính: EuroKera (Pháp); Công nghệ bo mạch: Inverter; An toàn: Khoá trẻ em, cảnh báo chống tràn, tự ngắt khi không nồi, hẹn giờ', 1, 1, 3),
(4, N'BDT-CHE-001', N'Bếp từ Chef''s EH-DIH666', 15500000, 11340000, 16, N'/img/sp/bdt-che-001/1.jpg', NULL, N'4400W', 36, N'Bếp có hai vùng nấu, kích thước 720 x 430 x 60 mm và kích thước khoét đá 700 x 380 x 30 mm, dùng nguồn điện 220V thông dụng. Mặt bếp sử dụng kính Schott Ceran vát cạnh, bo viền hai cạnh bên nhằm hạn chế nứt vỡ do va chạm và tăng thẩm mỹ cho khu vực nấu.
Chất kính chịu nhiệt, chịu lực tốt, bề mặt bóng sáng nên việc lau chùi sau mỗi bữa ăn rất nhanh gọn. EH-DIH666 thuộc dòng bếp từ Inverter nên kiểm soát công suất mượt mà và tiết kiệm điện hơn so với bếp điều khiển đóng ngắt truyền thống.
Mỗi vùng nấu đạt công suất 2200W và có thể tăng lên 3000W khi kích hoạt Booster, đủ sức đun sôi nhanh, xào nấu ở lửa lớn. Tính năng chia sẻ công suất tự động giúp bếp phân bổ điện năng hợp lý khi bật đồng thời hai vùng.
Bảng điều khiển cảm ứng trượt Touch Control độ nhạy cao với 9 mức công suất, kèm màn hình hiển thị rõ ràng, dễ dùng cho cả người lớn tuổi.', N'Thương hiệu: Chef''s (Việt Nam); Model: EH-DIH666; Sản xuất tại: Việt Nam; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220V / 50Hz; Tổng công suất (W): 4400; Kiểu điều khiển: Cảm ứng; Số vùng nấu: 2; KT mặt kính (R×S mm): 730 x 430; KT khoét đá (R×S mm): 670 x 370; Công suất từng vùng (W): 2200 + 2200; Booster: Có; CS Booster tối đa (W): 3000; Mặt kính: Schott Ceran (Đức); Công nghệ bo mạch: Inverter; An toàn: Khoá trẻ em/bàn phím, cảnh báo nhiệt dư, tự ngắt khi quá nhiệt/quá tải, nhận diện nồi, tự tắt khi để quên', 1, 1, 4),
(5, N'BHN-KAF-001', N'Bếp đôi từ hồng ngoại Kaff KF-IC5801SB', 25580000, 20210000, 11, N'/img/sp/bhn-kaf-001/1.jpg', NULL, N'6000W', 36, N'Bếp điện từ Kaff KF-IC5801SB là mẫu bếp ba vùng nấu kết hợp hai vùng từ và một vùng hồng ngoại, nhập khẩu nguyên chiếc từ CHLB Đức. Với ba vùng nấu hoạt động độc lập, bếp giúp rút ngắn đáng kể thời gian chuẩn bị bữa ăn cho gia đình đông thành viên, đồng thời vẫn giữ được sự linh hoạt nhờ vùng hồng ngoại không kén nồi.
Mặt bếp sử dụng kính Schott Ceran sản xuất tại Đức, chất liệu gốm thủy tinh chịu nhiệt tới 1000°C và kháng sốc nhiệt 750°C. Bề mặt chịu được thay đổi nhiệt độ đột ngột, độ giãn nở vì nhiệt thấp nên bếp vận hành hiệu quả và cách nhiệt tốt.
Mặt kính liền khối, chống trầy xước, tản nhiệt nhanh và được cách điện an toàn với linh kiện điện tử bên trong thân bếp. Kích thước mặt kính 780x450 mm cùng kích thước khoét đá 750x380 mm phù hợp với mặt đá tủ bếp rộng rãi.
Ba vùng nấu có công suất lần lượt 1800W, 1500W và 2200W, hoạt động ở điện áp 220-240V/50-60Hz.', N'Thương hiệu: Kaff (Đức); Model: KF-IC5801SB; Sản xuất tại: Đức; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V; Tổng công suất (W): 6000; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 3; KT mặt kính (R×S mm): 780 x 450; KT khoét đá (R×S mm): 750 x 380; Công suất từng vùng (W): 2300 + 1500 + 2200; Booster: Có; CS Booster tối đa (W): 3700; Mặt kính: Schott Ceran (Đức); Công nghệ bo mạch: Inverter; An toàn: Có', 1, 2, 2),
(6, N'BHN-KAF-002', N'Bếp đôi từ hồng ngoại Kaff KF-IH68N', 22800000, 18020000, 9, N'/img/sp/bhn-kaf-002/1.jpg', N'/img/sp/bhn-kaf-002/2.jpg|/img/sp/bhn-kaf-002/3.jpg', N'4500W', 36, N'Bếp điện từ Kaff KF-IH68N là mẫu bếp hai vùng nấu kết hợp một bên từ và một bên hồng ngoại, xuất xứ Germany. Thiết kế này mang lại sự linh hoạt hiếm có: vùng từ đun nấu nhanh, tập trung nhiệt và tiết kiệm điện khi dùng nồi inox nhiễm từ, còn vùng hồng ngoại không kén nồi, phù hợp cho những món cần lửa nhỏ, giữ ấm hay nướng trực tiếp trên mặt kính.
Mặt bếp sử dụng kính gốm thủy tinh Schott Ceran sản xuất tại Đức, nổi bật với khả năng chịu nhiệt và chống sốc nhiệt, chống trầy xước, chịu va đập tốt. Độ giãn nở vì nhiệt thấp nên bếp giữ nhiệt hiệu quả, đồng thời tản nhiệt nhanh và cách điện an toàn với linh kiện điện tử bên trong thân máy.
Mặt kính màu xám liền nguyên khối vừa tăng tính thẩm mỹ cho không gian bếp, vừa rất tiện khi lau chùi. Kích thước sản phẩm 730x430 mm, khoét đá 680x380 mm, trọng lượng khoảng 14 kg.
Về công nghệ, KF-IH68N dùng bo mạch IGBT Siemens của Đức cùng công nghệ biến tần Inverter giúp tiết kiệm tới 40% điện năng.', N'Thương hiệu: Kaff (Đức); Model: KF-IH68N; Sản xuất tại: Đức; Kiểu lắp: Âm tủ; Chất liệu: Kính; Tổng công suất (W): 4500; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 2; KT mặt kính (R×S mm): 680 x 380; Công suất từng vùng (W): 2300 + 2200; Booster: Có; CS Booster tối đa (W): 4000; Mặt kính: Schott Ceran (Đức); An toàn: Child lock, Auto shutoff, Cookware detection', 1, 2, 2),
(7, N'BHN-KOC-001', N'Bếp đôi từ hồng ngoại Kocher EI-355', 21500000, 11640000, 13, N'/img/sp/bhn-koc-001/1.jpg', N'/img/sp/bhn-koc-001/2.jpg|/img/sp/bhn-koc-001/3.jpg', N'5100W', 36, N'Bếp từ Kocher EI-355 là một thiết bị nấu ăn hiện đại và thông minh, đáp ứng mọi nhu cầu của người nội trợ. Với thiết kế và 3 vùng nấu linh hoạt, bao gồm 2 vùng từ và 1 vùng hồng ngoại, bếp từ Kocher EI-355 mang đến khả năng chế biến đa dạng và tiện lợi.
Mặt kính Keramik cao cấp với khả năng chịu lực và chịu nhiệt vượt trội, hạn chế tối đa trầy xước trong quá trình nấu nướng. Bếp từ Kocher EI-355 được trang bị công nghệ Inverter tiên tiến, giúp duy trì nhiệt độ ổn định, giảm thất thoát nhiệt và tối ưu điện năng tiêu thụ.
Điều khiển cảm ứng Slider thông minh với 9 mức công suất cho phép tăng giảm nhiệt độ mượt mà chỉ bằng một thao tác chạm. Hệ thống an toàn toàn diện với các tính năng như tự động ngắt khi quá nhiệt, chống tràn thông minh, khóa an toàn trẻ em và chế độ hẹn giờ cho từng vùng nấu, giúp quá trình nấu nướng luôn an toàn.', N'Thương hiệu: Kocher (Đức); Model: EI-355; Sản xuất tại: Hàn Quốc; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 5100; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 3; KT mặt kính (R×S mm): 780 x 450; KT khoét đá (R×S mm): 680 x 380; Công suất từng vùng (W): 1800 + 1500 + 2300; Booster: Có; CS Booster tối đa (W): 3000; Mặt kính: Kính ceramic; Công nghệ bo mạch: Inverter; An toàn: Khoá trẻ em, chống tràn, báo nhiệt dư, tự tắt quá nhiệt, hẹn giờ 1-99 phút/vùng', 1, 2, 3),
(8, N'BHN-CHE-001', N'Bếp đôi từ hồng ngoại Chef''s EH-MIX333', 13900000, 10170000, 25, N'/img/sp/bhn-che-001/1.jpg', NULL, N'5200W', 36, N'Bếp điện từ Chef''s EH-MIX333 là mẫu bếp hỗn hợp lắp âm gồm một vùng nấu từ và một vùng nấu điện hồng ngoại, giải pháp phù hợp cho những gia đình vẫn đang dùng song song nhiều loại nồi chảo khác nhau. Vùng nấu từ cho tốc độ làm nóng nhanh và hiệu suất cao, trong khi vùng nấu điện sử dụng được với hầu hết các loại nồi, kể cả nồi đất, nồi thủy tinh hay nồi nhôm không nhiễm từ.
Kích thước sản phẩm 720x420x60 mm với kích thước khoét đá 670x380 mm, phù hợp với đa số mặt đá bàn bếp thông dụng. Bên trong bếp sử dụng mâm nhiệt E.G.O, linh kiện quen thuộc trên các dòng bếp điện từ cao cấp, cho khả năng làm nóng nhanh và phân bố nhiệt đều.
Về công suất, vùng nấu điện có hai vòng nhiệt Ø14/22 cm với công suất tối đa 2000W, cho phép chọn vòng nhỏ khi dùng nồi đáy nhỏ và vòng lớn khi dùng nồi đáy rộng, tránh thất thoát nhiệt. Vùng nấu từ đường kính 22 cm đạt công suất tối đa 3000W, đủ mạnh cho các món cần lửa lớn như xào, áp chảo hay đun sôi nhanh.', N'Thương hiệu: Chef''s (Việt Nam); Model: EH-MIX333; Sản xuất tại: Việt Nam; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220V / 50Hz; Tổng công suất (W): 5200; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 2; KT mặt kính (R×S mm): 720 x 420; KT khoét đá (R×S mm): 670 x 380; Công suất từng vùng (W): 2200 + 3000; Booster: Có; CS Booster tối đa (W): 3000; Mặt kính: Schott Ceran (Đức); Thương hiệu mâm từ: E.G.O (Đức); An toàn: Khóa trẻ em, cảnh báo nhiệt dư, tự động tắt khi nhấc nồi, bảo vệ quá nhiệt/quá dòng', 1, 2, 4),
(9, N'HM-BOS-001', N'Máy hút mùi Bosch DWB97BK61T', 12990000, 10370000, 4, N'/img/sp/hm-bos-001/1.jpg', NULL, N'582 m3/h', 36, N'Máy hút mùi Bosch DWB97BK61T thuộc dòng Serie 4, là mẫu hút mùi gắn tường khổ 90 cm dành cho những căn bếp rộng rãi hoặc không gian bếp mở liền phòng khách. Sản phẩm mang thương hiệu Bosch của Đức, được sản xuất tại Thổ Nhĩ Kỳ, hướng tới người dùng cần lực hút lớn cùng độ hoàn thiện cao.
Máy có kiểu dáng chữ T hiện đại, thân làm từ thép không gỉ tông màu xám, dễ lau chùi và giữ được vẻ sáng bóng theo thời gian. Kích thước sản phẩm 550-830 x 898 x 500 mm với trọng lượng 21,8 kg, phần ống khói có thể điều chỉnh chiều cao để phù hợp với nhiều trần bếp khác nhau.
Chiều ngang 90 cm rất hợp khi lắp phía trên bếp từ hoặc bếp điện có từ 2 vùng nấu trở lên. Về vận hành, máy được trang bị động cơ EcoSilence Drive nổi tiếng của Bosch, cho khả năng chạy êm và tiết kiệm điện.', N'Thương hiệu: Bosch (Đức); Model: DWB97BK61T; Sản xuất tại: Thổ Nhĩ Kỳ; Kiểu lắp: Áp tường; Chất liệu: Inox / Thép sơn tĩnh điện; Điện áp: 220-240V; Tổng công suất (W): 275; Kiểu điều khiển: Cảm ứng; Kiểu máy: Ống khói; Lưu lượng hút (m³/h): 582; Độ ồn (dB): 66; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 275; Loại lọc: Lọc mỡ; Đèn chiếu sáng: LED; Bề rộng (mm): 898', 1, 3, 1),
(10, N'HM-KAF-001', N'Máy hút mùi Kaff KF-GB773', 7480000, 4930000, 15, N'/img/sp/hm-kaf-001/1.jpg', NULL, N'1000 m3/h', 36, N'Máy hút mùi Kaff KF-GB773 là mẫu hút mùi kính cong gắn tường khổ 70cm với thân inox sáng, thuộc nhóm sản phẩm bán chạy nhờ thiết kế cân đối, dễ lắp và chi phí vận hành thấp. Kiểu dáng toa inox kết hợp mặt kính cường lực tạo cảm giác chắc chắn, hiện đại mà không kén phong cách tủ bếp.
Thân máy được làm từ inox không gỉ, mặt trước là kính cường lực dày 8mm tráng men bóng cao cấp, bền với nhiệt độ và độ ẩm trong bếp, đồng thời dễ lau chùi. Kích thước sản phẩm 700 x 475 mm phù hợp lắp trên mặt bếp khổ 70cm phổ biến.
Bộ lọc gồm lưới lọc nhôm 5 lớp kết hợp than hoạt tính, tháo rời vệ sinh dễ dàng. Việc rửa lưới lọc định kỳ là yếu tố quan trọng giúp máy giữ được lực hút và tránh dầu mỡ đóng cặn trong buồng động cơ.
Máy sử dụng động cơ turbin đôi công suất 170W, cho công suất hút tối đa 1000 m3/h, xử lý nhanh khói và mùi từ các món chiên xào nhiều dầu mỡ.', N'Thương hiệu: Kaff (Đức); Model: KF-GB773; Sản xuất tại: Trung Quốc; Kiểu lắp: Áp tường; Chất liệu: Inox / Kính cường lực; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 170; Kiểu điều khiển: Cảm ứng; Kiểu máy: Kính cong; KT máy (R×S×C mm): 700 x 475; Lưu lượng hút (m³/h): 1000; Độ ồn (dB): 48; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 170; Loại lọc: Lọc mỡ / Lọc than hoạt tính (khử mùi); Đèn chiếu sáng: LED; Bề rộng (mm): 700; Chất liệu lưới lọc: Nhôm; An toàn: Kính cường lực cong', 1, 3, 2),
(11, N'HM-KAF-002', N'Máy hút mùi Kaff KF-GB705', 6480000, 4270000, 8, N'/img/sp/hm-kaf-002/1.jpg', NULL, N'1000 m3/h', 36, N'Máy hút mùi Kaff KF-GB705 là mẫu hút mùi kính cong gắn tường khổ 70cm điều khiển bằng phím bấm, dành cho những gia đình ưa sự đơn giản, chắc chắn trong thao tác hằng ngày. Kiểu dáng kính cong kết hợp thân inox sáng đẹp giúp máy dễ hòa hợp với hầu hết mẫu tủ bếp đang phổ biến.
Thân máy bằng inox sáng đẹp, mặt trước là kính cường lực dày 8mm tráng men bóng cao cấp, chịu nhiệt và chịu lực tốt, bề mặt dễ lau sau khi nấu. Kích thước sản phẩm 475 x 700 mm cùng ốp ống khói inox đi kèm giúp phần lắp đặt gọn gàng, cân đối với mặt bếp khổ 70cm.
Bộ lọc gồm lưới lọc nhôm 5 lớp kết hợp than hoạt tính, tháo rời để rửa định kỳ, đây là thao tác đơn giản nhưng quyết định nhiều tới việc máy có giữ được lực hút sau vài năm sử dụng hay không. Máy sử dụng động cơ turbin đôi công suất 170W, cho công suất hút tối đa 1000 m3/h, xử lý nhanh khói và mùi từ các món chiên rán nhiều dầu mỡ.', N'Thương hiệu: Kaff (Đức); Model: KF-GB705; Sản xuất tại: Malaysia; Kiểu lắp: Áp tường; Chất liệu: Inox / Kính cường lực; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 170; Kiểu điều khiển: Cơ (công tắc/nút gạt) / Phím bấm; Kiểu máy: Kính cong; KT máy (R×S×C mm): 700 x 475; Lưu lượng hút (m³/h): 1000; Độ ồn (dB): 48; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 170; Loại lọc: Lọc mỡ; Đèn chiếu sáng: LED; Bề rộng (mm): 700; Chất liệu lưới lọc: Nhôm; An toàn: chịu nhiệt, chịu lực cao', 1, 3, 2),
(12, N'HM-KOC-001', N'Máy hút mùi Kocher K-8872V đen-90cm', 8700000, 3780000, 19, N'/img/sp/hm-koc-001/1.jpg', N'/img/sp/hm-koc-001/2.jpg', N'1550 m3/h', 36, N'Máy hút mùi Kocher K-8872V bản màu đen, kích thước 90cm là mẫu hút mùi áp tường kính cong dành cho những căn bếp rộng mang phong cách hiện đại. Sản phẩm đảm nhiệm việc loại bỏ khói, mùi thức ăn và hơi dầu mỡ trong suốt quá trình nấu nướng, giúp căn bếp luôn thông thoáng và hạn chế mùi ám vào nội thất, rèm cửa hay quần áo.
Máy có thiết kế gắn tường với mặt kính cong dày 6.0mm kết hợp thân máy bằng hợp kim phủ sơn tĩnh điện màu đen nhám, tạo tổng thể sang trọng, dễ phối cùng các mẫu tủ bếp và thiết bị bếp tông tối đang được ưa chuộng. Kích thước tổng thể R900 x S470 x C500 (mm), phù hợp với mặt bếp khổ lớn.
Hệ thống lưới lọc nan inox có độ bền cao, giữ lại dầu mỡ hiệu quả đồng thời góp phần giảm tiếng ồn khi vận hành. lưới lọc tháo rời dễ dàng để vệ sinh định kỳ, giúp máy duy trì lực hút ổn định lâu dài. Hai đèn LED chiếu sáng tích hợp bảo đảm khu vực nấu luôn đủ sáng.', N'Thương hiệu: Kocher (Đức); Model: K-8872V đen-90cm; Sản xuất tại: Việt Nam; Kiểu lắp: Áp tường; Chất liệu: Kính cường lực; Điện áp: 220V / 50Hz; Tổng công suất (W): 195; Kiểu điều khiển: Phím bấm; Kiểu máy: Kính cong; Lưu lượng hút (m³/h): 1550; Độ ồn (dB): 46; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 195; Loại lọc: Lọc mỡ / Lọc than hoạt tính (khử mùi); Đèn chiếu sáng: LED; Bề rộng (mm): 900; Chất liệu lưới lọc: Inox', 1, 3, 3),
(13, N'HM-CHE-001', N'Máy hút mùi Chef''s EH-R107E3D', 7490000, 4350000, 21, N'/img/sp/hm-che-001/1.jpg', NULL, N'750 m3/h', 24, N'Máy hút mùi Chef’s model EH‑R107E3D mang đến sức hút mạnh mẽ 750 m³/h, giúp loại bỏ nhanh chóng khói, mùi và dầu mỡ trong không gian bếp, đồng thời tạo điểm nhấn hiện đại cho mọi thiết kế nội thất. Với thương hiệu Chef’s uy tín, sản phẩm được thiết kế để đáp ứng nhu cầu nấu nướng chuyên nghiệp và gia đình, mang lại không gian bếp luôn sạch sẽ và thoáng mát.
Kiểu dáng chữ T tinh tế, kích thước 700 × 350 × 445 mm, mặt trước phủ kính cường lực đen sang trọng và khung inox cao cấp 430, giúp máy hòa nhập hoàn hảo vào tủ bếp sâu 350 mm mà không nhô ra ngoài. Vỏ inox chịu nhiệt và dễ lau chùi, đồng thời bảo vệ các bộ phận bên trong khỏi ăn mòn, mang lại độ bền lâu dài cho sản phẩm.
Công nghệ động cơ không chổi than (BLDC) tiết kiệm điện năng tới 30‑50 % so với động cơ thông thường, đồng thời vận hành êm ái với độ ồn chỉ 42 dB.', N'Thương hiệu: Chef''s (Việt Nam); Model: EH-R107E3D; Sản xuất tại: Trung Quốc; Kiểu lắp: Áp tường; Chất liệu: Inox; Điện áp: 220V / 50Hz; Tổng công suất (W): 198; Kiểu điều khiển: Cảm ứng / Cảm ứng không chạm (vẫy tay); Kiểu máy: Chữ T; KT máy (R×S×C mm): 700 x 350; Lưu lượng hút (m³/h): 750; Độ ồn (dB): 42; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 198; Loại lọc: Lọc mỡ / Lọc than hoạt tính (khử mùi); Đèn chiếu sáng: LED; Bề rộng (mm): 700; Chất liệu lưới lọc: Inox', 1, 3, 4),
(14, N'MRC-BOS-001', N'Máy rửa chén Bosch SMV6ZCX16E', 33370000, 26220000, 5, N'/img/sp/mrc-bos-001/1.jpg', N'/img/sp/mrc-bos-001/2.jpg|/img/sp/mrc-bos-001/3.jpg', N'14 bộ', 36, N'Máy rửa bát âm toàn phần Bosch SMV6ZCX16E thuộc dòng Serie 6, là mẫu máy rửa bát cao cấp dung tích 14 bộ đồ ăn châu Âu. Thiết kế âm tủ hoàn toàn giúp máy ẩn mình gọn gàng sau cánh tủ bếp, giữ cho không gian bếp liền mạch và sang trọng, đồng thời mang lại hiệu quả làm sạch vượt trội cho gia đình đông thành viên.
Sản phẩm có kích thước cao 81,5 cm, rộng 59,8 cm và sâu 55 cm, dây nguồn dài 175 cm, phù hợp với hốc tủ tiêu chuẩn. Hệ thống RackMatic cho phép điều chỉnh độ cao giỏ rửa, kết hợp giàn rửa nhiều tầng linh hoạt giúp sắp xếp từ đĩa lớn, nồi niêu đến ly cốc mỏng manh một cách khoa học.
Bảng điều khiển cảm ứng đi kèm màn hình hiển thị cho thao tác trực quan và dễ theo dõi tiến trình rửa. SMV6ZCX16E được trang bị 8 chương trình rửa gồm tiết kiệm Eco 50°C, tự động 45-65°C, chương trình thông minh, rửa mạnh 70°C, rửa nhanh 60°C, rửa đồ thủy tinh, rửa yên tĩnh 50°C và chương trình ghi nhớ theo thói quen sử dụng.', N'Thương hiệu: Bosch (Đức); Model: SMV6ZCX16E; Sản xuất tại: Đức; Kiểu lắp: Âm toàn phần; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 2400; Độ ồn (dB): 40; Số bộ (place settings): 14; Số chương trình: 14; Công nghệ sấy: Sấy Zeolite; Điện/chu trình (kWh): 0,65; Nước/chu trình (lít): 9; KT (R×S×C mm): 598 x 550 x 815; An toàn: AquaStop chống rò rỉ nước (ống 2 lớp + van tự ngắt)', 1, 4, 1),
(15, N'MRC-BOS-002', N'Máy rửa chén Bosch SMS6ZCI02P', 34900000, 21710000, 3, N'/img/sp/mrc-bos-002/1.jpg', NULL, N'14 bộ', 36, N'Bosch SMS6ZCI02P thuộc dòng Serie 6 là máy rửa bát độc lập cao cấp, được lắp ráp và nhập khẩu trực tiếp từ Đức. Với dung tích 14 bộ bát đĩa chuẩn châu Âu, sản phẩm đáp ứng nhu cầu của gia đình từ 4‑6 thành viên, mang lại sự sạch sẽ và khô ráo tối ưu cho mọi loại đồ dùng bếp.
thiết kế với vỏ ngoài bằng thép không gỉ Inox, được phủ lớp chống vân tay AntiFingerprint, giúp bề mặt luôn sáng bóng và dễ vệ sinh. Kích thước tiêu chuẩn 845 mm × 600 mm × 600 mm cho phép lắp đặt linh hoạt ở hầu hết các không gian bếp, có thể đặt độc lập hoặc âm bán phần trong tủ.
Công nghệ sấy Zeolith độc quyền của Bosch chuyển đổi độ ẩm thành nhiệt, giúp bát đĩa khô hoàn toàn ngay cả đồ nhựa, đồng thời giảm tiêu thụ điện năng so với sấy nhiệt truyền thống. Động cơ EcoSilence Drive vận hành êm ái, độ ồn chỉ 40 dB, mang lại môi trường bếp yên tĩnh.', N'Thương hiệu: Bosch (Đức); Model: SMS6ZCI02P; Sản xuất tại: Đức; Kiểu lắp: Độc lập; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 2400; Độ ồn (dB): 40; Số bộ (place settings): 14; Số chương trình: 8; Công nghệ sấy: Sấy Zeolite; Điện/chu trình (kWh): 0,65; Nước/chu trình (lít): 9; KT (R×S×C mm): 600 x 600 x 845; An toàn: AquaStop chống rò rỉ nước, khóa trẻ em', 1, 4, 1),
(16, N'MRC-KAF-001', N'Máy rửa chén Kaff KF-SBL775B New Plus', 26800000, 21180000, 6, N'/img/sp/mrc-kaf-001/1.jpg', NULL, N'17 bộ', 36, N'Máy rửa chén Kaff KF-SBL775B New Plus là phiên bản màu đen của dòng New Plus cao cấp, dung tích lớn 17 bộ đồ ăn châu Âu, phù hợp cho gia đình đông người hoặc những bữa tiệc cuối tuần nhiều chén đĩa. Với kích thước 598R x 568S x 775mm khi tháo nắp trên, máy lắp âm vừa vặn dưới mặt bàn bếp Việt cao 810mm, đồng thời vẫn có thể đặt độc lập nếu tủ bếp không chừa sẵn khoang.
Thân máy bằng inox sơn tĩnh điện màu đen cao cấp, dáng chữ nhật đứng chắc chắn, tạo điểm nhấn sang trọng và hiện đại cho gian bếp. Khoang rửa bằng thép không gỉ chịu nhiệt tốt, chống ăn mòn và dễ vệ sinh.
Bảng điều khiển cảm ứng cùng màn hình LED hiển thị rõ ràng giúp chọn chương trình chỉ trong vài thao tác chạm. Sản phẩm tích hợp 8 chương trình rửa gồm rửa thông minh tự động, rửa kỹ, rửa thông thường, rửa tiết kiệm ECO, rửa ly, rửa nhanh tráng nước nóng, rửa hoa quả và chương trình rửa diệt khuẩn cho đồ trẻ em.', N'Thương hiệu: Kaff (Đức); Model: KF-SBL775B New Plus; Sản xuất tại: Thái Lan; Kiểu lắp: Độc lập; Điện áp: 220-240V / 50-60Hz; Độ ồn (dB): 48; Số bộ (place settings): 17; Số chương trình: 8; Công nghệ sấy: Sấy khí nóng (PTC); Điện/chu trình (kWh): 0,92; Nước/chu trình (lít): 11.2; KT phủ bì (R×S mm): 598 x 568; KT (R×S×C mm): 598 x 568 x 775; An toàn: Khóa trẻ em, tự khóa máy khi sự cố (ngập nước, va đập, yếu điện, thiếu nước), chống rò nước', 1, 4, 2),
(17, N'MRC-CHE-001', N'Máy rửa chén Chef''s EH-DW401D', 17900000, 13100000, 10, N'/img/sp/mrc-che-001/1.jpg', NULL, N'15 bộ', 36, N'Máy rửa chén Chef''s EH-DW401D là mẫu máy dung tích lớn của thương hiệu Việt Nam Chef''s, sản xuất tại Trung Quốc, hướng tới các gia đình cần rửa nhiều bát đĩa mỗi ngày nhưng vẫn muốn thiết bị vận hành êm và dễ sử dụng. Máy có dung tích 15 bộ châu Âu, tương đương 3 – 4 bữa cơm Việt, đủ cho lượng chén bát, xoong nồi của một gia đình đông người.
Máy có thể lắp âm tủ hoặc đặt độc lập với kích thước 845 x 600 x 600 mm, màu inox xám sáng bóng dễ phối với nhiều phong cách bếp, trọng lượng 49 kg. Khoang rửa làm bằng inox với 3 giàn rửa được bố trí riêng cho thìa đũa, bát đĩa và xoong nồi, giúp phân loại đồ hợp lý để nâng cao hiệu quả làm sạch.
Máy điều khiển bằng phím cơ kết hợp màn hình LED hiển thị chế độ rửa và thời gian hoạt động. EH-DW401D được trang bị 8 chương trình rửa tự động gồm Auto, Intensive, Universal, ECO, Glass, 90min, Rapid và Pre-wash, cho phép chọn chế độ đúng với loại đồ và mức độ bẩn thực tế.', N'Thương hiệu: Chef''s (Việt Nam); Model: EH-DW401D; Sản xuất tại: Trung Quốc; Kiểu lắp: Độc lập / Âm toàn phần; Điện áp: 220V / 50Hz; Tổng công suất (W): 2100; Độ ồn (dB): 42; Số bộ (place settings): 15; Số chương trình: 8; Công nghệ sấy: Sấy khí nóng, Tự động mở cửa; Điện/chu trình (kWh): 0,95; Nước/chu trình (lít): 12; KT (R×S×C mm): 600 x 600 x 845; An toàn: Khóa trẻ em, chỉ báo muối', 1, 4, 4),
(18, N'LN-BOS-001', N'Lò nướng Bosch HBG536ES3', 18900000, 15370000, 7, N'/img/sp/ln-bos-001/1.jpg', N'/img/sp/ln-bos-001/2.jpg|/img/sp/ln-bos-001/3.jpg', N'71 lít', 36, N'Lò nướng âm tủ Bosch HBG536ES3 thuộc dòng Serie 6, là bước nâng cấp rõ rệt về tiện nghi so với các dòng Series thấp hơn, dành cho những gia đình nấu nướng thường xuyên và muốn thiết bị hỗ trợ nhiều hơn. Sản phẩm mang thương hiệu Bosch xuất xứ Đức, được sản xuất tại Tây Ban Nha và bảo hành 36 tháng.
Về ngoại hình, lò hoàn thiện bằng thép không gỉ kết hợp mặt kính, tạo phong cách châu Âu hiện đại dễ phối hợp với các thiết bị bếp khác. Kiểu lắp âm tủ, đặt được ở đế tủ hoặc tủ cao tùy bố cục bếp.
Kích thước sản phẩm 595 x 594 x 548 mm với khoang khoét tủ 585-595 x 560-568 x 550 mm, khối lượng 34,7 kg. Bảng điều khiển gồm núm xoay kết hợp màn hình LCD trắng hiển thị rõ ràng, giúp người dùng nắm được nhiệt độ và thời gian mà không cần đoán.
Công nghệ khí nóng 3D là điểm cốt lõi, phân bổ nhiệt đồng đều trên nhiều tầng để thực phẩm chín đều từ trong ra ngoài, cho phép nướng nhiều khay cùng lúc.', N'Thương hiệu: Bosch (Đức); Model: HBG536ES3; Sản xuất tại: Tây Ban Nha; Kiểu lắp: Âm tủ; Chất liệu: Inox / Men chống dính (khoang lò); Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 3400; Kiểu điều khiển: Núm xoay; KT (R×S×C mm): 594 x 548 x 595; Chất liệu khoang lò: Men xúc tác (Catalytic); Yêu cầu đấu nối: Cầu chì 16 A, không có phích cắm — cần aptomat riêng; Công suất nướng (W): 2700; Dung tích (lít): 71; Số chức năng nướng: 10; Dải nhiệt (°C): 50-275; Công nghệ: 3D Hot-air phân bổ nhiệt đồng đều; Tự làm sạch: Xúc tác (Catalytic); Tên thương mại làm sạch: EcoClean Direct; Chức năng: Nướng trên/dưới, nướng pizza, làm nóng nhẹ, làm nóng thông thường, nướng thường, EcoClean Direct + thủy phân, Gia nhiệt nhanh, Khóa trẻ em, Kính cửa tháo rời, Cảm biến quá nhiệt, cửa kính 2 lớp cách nhiệt, hẹn giờ tự động ngắt; KT khoét (R×S×C mm): 560 x 550 x 585; An toàn: Cảm biến quá nhiệt, cửa kính 2 lớp cách nhiệt, hẹn giờ tự động ngắt', 1, 5, 1),
(19, N'LN-KAF-001', N'Lò nướng Kaff KF-T90S', 20800000, 15070000, 9, N'/img/sp/ln-kaf-001/1.jpg', N'/img/sp/ln-kaf-001/2.jpg', N'67 lít', 36, N'Lò nướng Kaff KF-T90S là mẫu lò nướng đối lưu lắp âm tủ dung tích 67 lít với 12 chức năng nướng, dành cho những gia đình có nhu cầu nấu nướng đa dạng và thường xuyên làm bánh tại nhà. Thiết kế âm tủ chỉ để hở mặt trước, toàn bộ khoang lắp chìm vào tủ bếp, giúp gian bếp gọn gàng và giữ nhiệt độ lò ổn định trong suốt quá trình hoạt động.
Về ngoại hình, lò dùng mặt kính đen phủ bên ngoài với viền thép chắc chắn bao quanh. Cửa lò gồm 3 lớp kính cách nhiệt, vừa cho phép quan sát rõ quá trình nướng bên trong, vừa hạn chế thất thoát nhiệt và giảm nguy cơ bỏng khi vô tình chạm vào bề mặt cửa.
Khoang lò được tráng men chịu nhiệt, ít bám dính nên việc vệ sinh sau khi dùng khá đơn giản. Kích thước sản phẩm 595C x 595R x 575S mm với kích thước âm tủ 590C x 560R x 595S mm là các thông số tiêu chuẩn, dễ phối vào hệ tủ bếp.
Về hiệu năng, tổng công suất 3450W kết hợp chế độ nướng đối lưu giúp luồng khí nóng tuần hoàn liên tục trong khoang.', N'Thương hiệu: Kaff (Đức); Model: KF-T90S; Sản xuất tại: Malaysia; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V; Tổng công suất (W): 3450; Kiểu điều khiển: Cảm ứng; KT (R×S×C mm): 595 x 575 x 595; Số lớp kính cửa: 3; Dung tích (lít): 67; Số chức năng nướng: 12; Dải nhiệt (°C): 50-250; Công nghệ: Nướng đối lưu; Tự làm sạch: Xúc tác (Catalytic); Chức năng: 12 chức năng nướng; KT khoét (R×S×C mm): 590 x 560 x 550; An toàn: Khóa trẻ em, Tự động tắt khi không sử dụng', 1, 5, 2),
(20, N'LN-KOC-001', N'Lò nướng Kocher KOEU-71S14F', 22650000, 13350000, 12, N'/img/sp/ln-koc-001/1.jpg', NULL, N'71 lít', 36, N'Lò nướng âm tủ Kocher KOEU-71S14F là phiên bản nâng cấp trong dòng lò 71 lít của Kocher, nổi bật với 14 chương trình nấu nướng tự động và khả năng tự làm sạch. Sản phẩm mang thương hiệu Kocher xuất xứ Đức, được sản xuất tại Việt Nam, bảo hành chính hãng 36 tháng.
Về thiết kế, lò mang phong cách châu Âu hiện đại với thân vỏ bằng thép không gỉ siêu bền, dễ vệ sinh và giữ được vẻ sáng bóng theo thời gian. Cửa lò sử dụng mặt kính 3 lớp cách nhiệt chịu lực, bảo vệ người dùng khỏi nguy cơ bỏng khi chạm phải và giúp giữ nhiệt bên trong hiệu quả hơn.
Kiểu mở phía trước cùng thiết kế lắp âm tủ tạo sự liền mạch và gọn gàng cho căn bếp. Kích thước sản phẩm 595 x 595 x 575 mm theo chiều cao, rộng và sâu.
Khoang lò 71 lít cho phép chế biến nhiều loại thực phẩm cùng lúc mà vẫn đảm bảo chín đều.', N'Thương hiệu: Kocher (Đức); Model: KOEU-71S14F; Sản xuất tại: Việt Nam; Kiểu lắp: Âm tủ; Chất liệu: Men chống dính (khoang lò); Điện áp: 220-240V / 50Hz; Tổng công suất (W): 2300; Kiểu điều khiển: Cảm ứng / Cơ; KT (R×S×C mm): 595 x 575 x 595; Chất liệu khoang lò: Thép tráng men; Số lớp kính cửa: 3; Dung tích (lít): 71; Số chức năng nướng: 14; Công nghệ: Khí nóng 3D (3D hot air); Chức năng: 14 chức năng tự động; An toàn: Khóa an toàn trẻ em, kính 3 lớp chịu nhiệt, quạt tự làm mát', 1, 5, 3),
(21, N'LN-CHE-001', N'Lò nướng Chef''s EH-BO600E', 16900000, 10660000, 14, N'/img/sp/ln-che-001/1.jpg', NULL, N'72 lít', 36, N'Lò nướng âm tủ Chef''s EH-BO600E là mẫu lò dung tích lớn dành cho những gia đình nấu nướng nhiều, mong muốn một thiết bị đa năng nhưng vẫn dễ thao tác. Sản phẩm mang thương hiệu Chef''s với xuất xứ thương hiệu Việt Nam, được sản xuất tại Trung Quốc và bảo hành chính hãng 36 tháng theo hình thức bảo hành điện tử.
Về thiết kế, lò được thiết kế theo tiêu chuẩn châu Âu với thiết kế âm tủ giúp tiết kiệm diện tích, đồng thời tăng tính thẩm mỹ và sự gọn gàng cho không gian bếp. Thân lò hoàn thiện bằng chất liệu inox chống gỉ cao cấp kết hợp mặt kính, được gia công tỉ mỉ tới từng chi tiết.
Cửa lò sử dụng kính 4 lớp, ngăn nhiệt lượng thất thoát ra ngoài, vừa giữ hiệu năng nấu nướng vừa hạn chế hao phí điện năng và giảm nguy cơ bỏng khi vô tình chạm phải. Kích thước sản phẩm khoảng 595 x 565 x 595 mm.
Bên trong lò có đèn chiếu sáng tiện lợi để quan sát món ăn trong suốt quá trình nướng.', N'Thương hiệu: Chef''s (Việt Nam); Model: EH-BO600E; Sản xuất tại: Trung Quốc; Kiểu lắp: Âm tủ; Chất liệu: Kính; Tổng công suất (W): 3200; Kiểu điều khiển: Cảm ứng / Núm xoay; KT (R×S×C mm): 595 x 565 x 595; Số lớp kính cửa: 4; Dung tích (lít): 72; Số chức năng nướng: 11; Dải nhiệt (°C): 30-250; Công nghệ: Tự làm sạch nhiệt phân (Pyrolytic); Chức năng: Tự làm sạch, Quick Heat, 11 chương trình, kính 4 lớp, Khóa trẻ em; An toàn: Khóa trẻ em', 1, 5, 4),
(22, N'LVS-BOS-001', N'Lò vi sóng Bosch BEL524MB2', 16900000, 14270000, 5, N'/img/sp/lvs-bos-001/1.jpg', N'/img/sp/lvs-bos-001/2.jpg|/img/sp/lvs-bos-001/3.jpg', N'20 lít', 24, N'Lò vi sóng âm tủ Bosch BEL524MB2 thuộc dòng Serie 6, là lựa chọn dành cho những căn bếp đề cao sự đồng bộ và gọn gàng. Với thiết kế âm tủ tiêu chuẩn, thiết bị được lắp liền mạch vào hệ tủ bếp, giúp mặt bằng bếp thoáng hơn so với lò đặt bàn, đồng thời giữ được tổng thể liền khối cho khu vực nấu nướng.
Về thẩm mỹ, sản phẩm sử dụng mặt kính đen kết hợp khoang lò bằng inox với đáy kính. Mặt kính đen dễ lau chùi và phù hợp với nhiều phong cách nội thất từ hiện đại đến tối giản, trong khi khoang inox giúp truyền nhiệt đều và hạn chế bám bẩn.
Kích thước sản phẩm 382 x 594 x 317 mm, kích thước hộc tủ yêu cầu 382 x 560 x 300 mm, khối lượng 17,2 kg. BEL524MB2 có dung tích 20 lít, phù hợp với nhu cầu hằng ngày của gia đình nhỏ: hâm nóng thức ăn, rã đông thực phẩm và chế biến các món đơn giản.', N'Thương hiệu: Bosch (Đức); Model: BEL524MB2; Sản xuất tại: Trung Quốc; Kiểu lắp: Âm tủ; Điện áp: 220-230V / 50Hz; Tổng công suất (W): 1270; Kiểu điều khiển: Cảm ứng trượt (Slider); KT (R×S×C mm): 594 x 317 x 382; Dung tích (lít): 20; Tự làm sạch: Có (thủy phân/Hydrolytic); Loại lò: Vi sóng + nướng; Kiểu khoang: Có đĩa quay; Công suất vi sóng (W): 800; Số mức công suất: 5; An toàn: Khóa trẻ em, tự động tắt', 1, 6, 1),
(23, N'LVS-KAF-001', N'Lò vi sóng Kaff KF-EM036ALMW', 9800000, 7100000, 11, N'/img/sp/lvs-kaf-001/1.jpg', NULL, N'36 lít', 36, N'Lò vi sóng Kaff KF-EM036ALMW là mẫu lò để bàn kết hợp chức năng nướng, hướng đến những gia đình muốn một thiết bị đa năng thay vì chỉ dùng để hâm nóng đơn thuần. Với dung tích 36 lít, đây là mức dung tích lớn trong nhóm lò vi sóng để bàn, đủ chỗ cho khay đĩa cỡ lớn và phù hợp với nhu cầu nấu nướng của gia đình đông thành viên.
Sản phẩm có kích thước 519 x 426 x 315 mm, thiết kế đặt bàn nên dễ bố trí trên mặt bếp hoặc kệ mà không cần cải tạo hệ tủ. Vòng ngoài hoàn thiện chỉn chu, bề mặt hạn chế bám dấu vân tay, giúp lò luôn trông sạch sẽ và góp phần tạo vẻ gọn gàng cho khu vực nấu nướng.
Bên trong, đĩa xoay thủy tinh chịu nhiệt đường kính 315 mm giúp thực phẩm quay đều và chín đồng nhất. Về khả năng chế biến, KF-EM036ALMW là lò 2 trong 1: vừa vi sóng vừa nướng, công suất 1000W.', N'Thương hiệu: Kaff (Đức); Model: KF-EM036ALMW; Sản xuất tại: Thái Lan; Kiểu lắp: Để bàn; Điện áp: 220-240V / 50-60Hz; KT (R×S×C mm): 519 x 426 x 315; Dung tích (lít): 36; Loại lò: Vi sóng + nướng; Công suất vi sóng (W): 1000; Số mức công suất: 11; An toàn: Khóa trẻ em', 1, 6, 2),
(24, N'LVS-KOC-001', N'Lò vi sóng Kocher KMEU-1025A', 13900000, 8130000, 13, N'/img/sp/lvs-koc-001/1.jpg', NULL, N'25 lít', 24, N'Lò vi sóng âm tủ Kocher KMEU-1025A là thiết bị dành cho những căn bếp hiện đại cần một giải pháp hâm nóng, rã đông và nấu nhanh gọn gàng mà không chiếm chỗ trên mặt bàn. Sản phẩm mang thương hiệu Kocher xuất xứ Đức, được sản xuất tại Malaysia và bảo hành 24 tháng.
Về thiết kế, lò có kiểu dáng âm tủ tinh gọn với mặt kính đen liền mạch và bảng điều khiển LCD hiển thị rõ ràng, dễ thao tác. Kích thước sản phẩm 595 rộng x 351 sâu x 390 cao mm với khoang lắp đặt 600 x 550 x 378 mm, phù hợp với các hệ tủ bếp tiêu chuẩn theo phong cách tối giản châu Âu.
Cửa lò bằng kính lớn giúp quan sát quá trình nấu thuận tiện mà không cần mở cửa làm thất thoát nhiệt. Khoang lò dung tích 25 lít đủ rộng để đặt các đĩa hoặc khay thực phẩm cỡ lớn, đáp ứng nhu cầu của gia đình đông người hoặc những lần chuẩn bị nhiều món cùng lúc.
Đĩa xoay thủy tinh giúp thực phẩm chín và nóng đều hơn.', N'Thương hiệu: Kocher (Đức); Model: KMEU-1025A; Sản xuất tại: Malaysia; Kiểu lắp: Âm tủ; Điện áp: 220-240V / 50Hz; Kiểu điều khiển: Cảm ứng; KT (R×S×C mm): 595 x 351 x 390; Dung tích (lít): 25; Loại lò: Vi sóng + nướng + đối lưu; Công suất vi sóng (W): 900; An toàn: Khóa an toàn trẻ em, cửa kính cách nhiệt, quạt làm mát', 1, 6, 3),
(25, N'LVS-CHE-001', N'Lò vi sóng Chef''s EH-MW801B', 9800000, 7170000, 18, N'/img/sp/lvs-che-001/1.jpg', NULL, N'25 lít', 36, N'Lò vi sóng kết hợp nướng Chef''s EH-MW801B là phiên bản tông đen sang trọng trong dòng lò âm tủ 25 lít của Chef''s, thương hiệu Việt Nam với sản phẩm được sản xuất tại Trung Quốc. thiết kế, mang hơi hướng châu Âu, phù hợp cho những căn bếp muốn có một thiết bị vừa hâm nóng nhanh vừa nướng được đa dạng món ăn.
Sản phẩm có kích thước 388 x 592 x 401 mm, khuyến khích lắp âm tủ để tiết kiệm diện tích, nhưng vẫn có thể đặt dương tùy nhu cầu bố trí. Mặt trước màu đen kết hợp cửa kính hai lớp cách nhiệt viền inox tạo tổng thể cân đối và chắc chắn.
Về chất liệu, khoang lò được tráng men inox an toàn và dễ vệ sinh, vỏ lò làm bằng thép không gỉ kết hợp kính trong suốt chịu lực, cho độ bền tốt theo thời gian. Đường kính đĩa xoay 31,5 cm cùng dung tích 25 lít đáp ứng nhu cầu ăn uống của hộ gia đình từ 4 đến 6 thành viên.
Trọng lượng sản phẩm 19,5 kg.', N'Thương hiệu: Chef''s (Việt Nam); Model: EH-MW801B; Sản xuất tại: Trung Quốc; Kiểu lắp: Âm tủ / Để bàn; Kiểu điều khiển: Cảm ứng; KT (R×S×C mm): 592 x 401 x 388; Công suất nướng (W): 1200; Dung tích (lít): 25; Dải nhiệt (°C): 30-250; Loại lò: Vi sóng + nướng; Công suất vi sóng (W): 900; Số mức công suất: 5; An toàn: đóng cửa tự động', 1, 6, 4),
(26, N'BDT-BOS-002', N'Bếp từ Bosch PID651DC5E', 20610000, 16460000, 9, N'/img/sp/bdt-bos-002/1.jpg', N'/img/sp/bdt-bos-002/2.jpg|/img/sp/bdt-bos-002/3.jpg', N'7400W', 36, N'Bếp từ Bosch PID651DC5E mang lại trải nghiệm nấu ăn hiện đại nhờ công nghệ điện từ tiên tiến và thiết kế. Với công suất tổng 7400 W và ba vùng nấu độc lập, sản phẩm đáp ứng nhanh chóng mọi nhu cầu từ hầm súp, chiên xào đến đun sôi, đồng thời giúp tiết kiệm năng lượng so với bếp gas truyền thống.
Mặt bếp được làm từ gốm thủy tinh Schott Ceran màu đen, chịu nhiệt lên tới 10 °C và có bề mặt vát nghiêng 4 cạnh, giúp nước bắn trượt nhanh và dễ dàng lau chùi. Kiểu lắp âm và kích thước khoét đá 560‑562 mm x 490‑502 mm x 59 mm phù hợp với hầu hết các bếp đá, trong khi bảng điều khiển cảm ứng DirectSelect dài 30 cm cho phép người dùng lựa chọn mức nhiệt chỉ bằng một chạm.
Công nghệ PerfectFry tích hợp cảm biến chiên xào liên tục điều chỉnh nhiệt độ, giúp thực phẩm đạt độ chín vàng giòn mà không bị cháy khét. DirectSelect Premium cung cấp 17 mức gia nhiệt chi tiết, cho phép tùy chỉnh chính xác từng vùng nấu.', N'Thương hiệu: Bosch (Đức); Model: PID651DC5E; Sản xuất tại: Tây Ban Nha; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220V; Tổng công suất (W): 7400; Kiểu điều khiển: Cảm ứng; Số vùng nấu: 3; KT mặt kính (R×S mm): 592 x 522; KT khoét đá (R×S mm): 560 x 490; Công suất từng vùng (W): 3300 + 2200 + 1400; Booster: Có; CS Booster tối đa (W): 4400; Mặt kính: Schott Ceran (Đức); Vùng nấu đặc biệt: Vùng nấu mở rộng (FlexZone); Công nghệ bo mạch: Inverter; An toàn: Khoá trẻ em, tự ngắt, cảnh báo nhiệt dư, an toàn tràn', 1, 1, 1),
(27, N'BDT-KAF-002', N'Bếp từ Kaff KF-FL68II NEW PLUS', 17800000, 11720000, 5, N'/img/sp/bdt-kaf-002/1.jpg', NULL, N'4400W', 36, N'Bếp từ đôi Kaff KF-FL68II New Plus mang đến trải nghiệm nấu nướng hiện đại với công suất tối đa 4400W, chia thành hai vùng 2200W mỗi vùng. Mặt kính Schott Ceran siêu bền, chịu lực và chịu sốc nhiệt, được thiết kế mỏng gọn, lắp âm tạo không gian bếp tinh tế, đồng thời dễ dàng vệ sinh.
Đặc biệt, bếp được trang bị công nghệ Inverter biến tần giúp tiết kiệm điện năng lên tới 40%, cùng với 9 dãy công suất và chức năng Booster nâng công suất lên 3200W khi cần nấu nhanh.\n\nThiết kế cảm ứng slider trực tiếp trên mặt bếp mang lại trải nghiệm thao tác mượt mà, nhanh nhạy. Hệ thống điều khiển thông minh cho phép người dùng tùy chỉnh nhiệt độ, thời gian, và các chế độ nấu như hâm, chiên xào, rã đông, sôi liu riu.', N'Thương hiệu: Kaff (Đức); Model: KF-FL68II NEW PLUS; Sản xuất tại: Malaysia; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 4400; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 2; KT mặt kính (R×S mm): 730 x 430; KT khoét đá (R×S mm): 680 x 390; Công suất từng vùng (W): 2200 + 2200; Booster: Có; CS Booster tối đa (W): 3200; Mặt kính: Schott Ceran (Đức); Công nghệ bo mạch: Inverter; An toàn: Khoá trẻ em, hẹn giờ 99 phút, tạm dừng, tự tắt khi quá nhiệt/tràn, cảnh báo nhiệt dư', 1, 1, 2),
(28, N'BHN-KAF-003', N'Bếp đôi từ hồng ngoại Kaff KF-FL108', 20800000, 16440000, 0, N'/img/sp/bhn-kaf-003/1.jpg', N'/img/sp/bhn-kaf-003/2.jpg|/img/sp/bhn-kaf-003/3.jpg', N'4300W', 36, N'Bếp điện từ Kaff KF-FL108 là mẫu bếp hai vùng nấu kết hợp một vùng từ và một vùng hồng ngoại, nhập khẩu nguyên chiếc từ CHLB Đức. Điểm nhận diện của model này là phần bo viền hợp kim nhôm cao cấp chạy quanh mặt kính, vừa tăng vẻ chắc chắn vừa bảo vệ cạnh kính khỏi va đập trong quá trình sử dụng.
Mặt bếp làm từ kính Schott Ceran của Đức, chất liệu gốm thủy tinh nổi tiếng với khả năng chịu lực, chịu nhiệt và chống sốc nhiệt tốt, bề mặt bóng sáng, bền bỉ và dễ lau chùi khi bám bẩn. Bếp có thể lắp âm hoặc đặt nổi tùy không gian, tông màu đen sang trọng dễ hòa hợp với nhiều phong cách tủ bếp.
Kích thước mặt kính 730x430 mm, kích thước khoét đá 680x380 mm là thông số phổ thông nên thuận tiện khi lắp mới hoặc thay thế bếp cũ. Hai vùng nấu có công suất 2200W mỗi bên, chia thành 10 mức gia nhiệt để người nấu chọn chính xác mức nhiệt cho từng món.', N'Thương hiệu: Kaff (Đức); Model: KF-FL108; Sản xuất tại: Đức; Kiểu lắp: Âm tủ; Chất liệu: Kính; Tổng công suất (W): 4300; Kiểu điều khiển: Cảm ứng trượt (Slider); Số vùng nấu: 2; KT mặt kính (R×S mm): 730 x 430; KT khoét đá (R×S mm): 680 x 380; Công suất từng vùng (W): 2300 + 2000; Booster: Có; CS Booster tối đa (W): 3700; Mặt kính: Schott Ceran (Đức); An toàn: Overflow, Overheat, Child lock, Timer', 1, 2, 2),
(29, N'HM-BOS-002', N'Máy hút mùi Bosch DFT63CA61B', 6900000, 5490000, 2, N'/img/sp/hm-bos-002/1.jpg', N'/img/sp/hm-bos-002/2.jpg|/img/sp/hm-bos-002/3.jpg', N'310 m3/h', 36, N'Máy hút mùi Bosch DFT63CA61B là mẫu hút mùi âm tủ khổ 60 cm, phù hợp với những căn bếp có tủ trên và cần một thiết bị gọn gàng nhưng vẫn đảm bảo hiệu quả khử khói mùi hằng ngày. Sản phẩm thuộc thương hiệu Bosch của Đức, được sản xuất tại Thổ Nhĩ Kỳ.
Máy có kiểu lắp âm tủ với kích thước thiết bị 175 x 598 x 300 mm, kích thước lắp đặt 135 x 598 x 280 mm nên dễ dàng giấu gọn vào khoang tủ bếp, chỉ lộ phần bề mặt hút. Thân máy làm từ thép không gỉ kết hợp kính đen, hoàn thiện tông inox bạc sang trọng, vừa bền trong môi trường nhiều hơi nước và dầu mỡ, vừa thuận tiện khi lau chùi.
Về khả năng hút, thiết bị sử dụng động cơ công suất 181 W, cho công suất hút 310 m3/h với 3 cấp độ để người dùng điều chỉnh theo lượng khói thực tế. Mức thấp phù hợp các món luộc, hấp. mức cao dành cho những bữa chiên xào nhiều dầu mỡ hoặc món nặng mùi.
Độ ồn khi hoạt động ở mức 67 dBA.', N'Thương hiệu: Bosch (Đức); Model: DFT63CA61B; Sản xuất tại: Thổ Nhĩ Kỳ; Kiểu lắp: Âm tủ; Chất liệu: Inox / Thép sơn tĩnh điện; Điện áp: 220-240V; Tổng công suất (W): 181; Kiểu điều khiển: Phím bấm; Lưu lượng hút (m³/h): 310; Độ ồn (dB): 67; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 181; Loại lọc: Lọc mỡ; Đèn chiếu sáng: LED; Bề rộng (mm): 600', 1, 3, 1),
(30, N'MRC-KOC-001', N'Máy rửa chén Kocher MRB-X15S', 17500000, 11950000, 7, N'/img/sp/mrc-koc-001/1.jpg', N'/img/sp/mrc-koc-001/2.jpg', N'17 bộ', 24, N'Máy rửa chén Kocher MRB-X15S là mẫu máy rửa chén dung tích 17 bộ, thuộc dòng MRB-X15 của Kocher, phiên bản hoàn thiện màu bạc. Dung tích 17 bộ phù hợp với gia đình đông người hoặc những bữa cơm nhiều món, cho phép rửa gọn lượng chén đĩa của cả ngày trong một chu trình.
Bản màu bạc là lựa chọn cho căn bếp dùng nhiều chi tiết inox hoặc tông sáng. Sản phẩm thuộc thương hiệu Kocher, xuất xứ thương hiệu Đức và được sản xuất tại Việt Nam.', N'Thương hiệu: Kocher (Đức); Model: MRB-X15S; Sản xuất tại: Việt Nam; Kiểu lắp: Độc lập / Âm toàn phần; Điện áp: 220-240V / 50Hz; Độ ồn (dB): 49; Số bộ (place settings): 17; Số chương trình: 7; Công nghệ sấy: Sấy tăng cường; Điện/chu trình (kWh): 0,86; Nước/chu trình (lít): 9; KT phủ bì (R×S mm): 598 x 590; KT (R×S×C mm): 598 x 590 x 805; An toàn: Khóa trẻ em, hẹn giờ 24h, cảm biến cảnh báo lỗi', 1, 4, 3),
(31, N'MRC-BOS-003', N'Máy rửa chén Bosch SMS4ECI01P', 25900000, 20610000, 4, N'/img/sp/mrc-bos-003/1.jpg', N'/img/sp/mrc-bos-003/2.jpg|/img/sp/mrc-bos-003/3.jpg', N'13 bộ', 36, N'Máy rửa chén độc lập Bosch SMS4ECI01P Serie 4 là giải pháp lý tưởng cho gia đình hiện đại mong muốn tối ưu thời gian và công sức nội trợ. Sở hữu thiết kế, khoang rửa rộng rãi cùng nhiều công nghệ rửa và sấy tiên tiến, sản phẩm giúp chén bát sạch sâu, khô ráo và an toàn cho sức khỏe người dùng.
Máy có kiểu dáng độc lập chắc chắn, lớp vỏ thép sơn tĩnh điện kết hợp cửa thép không gỉ sáng bóng, vừa bền bỉ vừa tạo điểm nhấn cho gian bếp. Nhờ thiết kế này, người dùng có thể bố trí máy ở nhiều vị trí khác nhau mà không phụ thuộc vào tủ bếp.
Kích thước tiêu chuẩn cao 84,5 cm, rộng 60 cm và sâu 60 cm giúp việc lắp đặt trở nên đơn giản. Hệ thống giỏ VarioFlex cùng khay trên điều chỉnh được độ cao cho phép linh hoạt thay đổi không gian chứa, thuận tiện khi cần rửa nồi chảo lớn hay ly cốc cao.', N'Thương hiệu: Bosch (Đức); Model: SMS4ECI01P; Sản xuất tại: Đức; Kiểu lắp: Độc lập; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 2400; Độ ồn (dB): 46; Số bộ (place settings): 13; Số chương trình: 6; Công nghệ sấy: Sấy tăng cường, Tự động mở cửa; Điện/chu trình (kWh): 0,84; Nước/chu trình (lít): 9.5; KT (R×S×C mm): 600 x 600 x 845; An toàn: AquaStop chống rò rỉ nước, khóa trẻ em, tự vệ sinh máy', 1, 4, 1),
(32, N'LN-BOS-002', N'Lò nướng Bosch HBA514BS3', 15900000, 13540000, 3, N'/img/sp/ln-bos-002/1.jpg', N'/img/sp/ln-bos-002/2.jpg|/img/sp/ln-bos-002/3.jpg', N'71 lít', 36, N'Lò nướng âm tủ Bosch HBA514BS3 thuộc dòng Series 4, là lựa chọn quen thuộc cho những căn bếp muốn nâng cấp lên chuẩn thiết bị châu Âu mà vẫn giữ được mức đầu tư hợp lý. Sản phẩm mang thương hiệu Bosch của Đức, được sản xuất tại Ba Lan và bảo hành 36 tháng.
Về thiết kế, HBA514BS3 hoàn thiện bằng thép không gỉ kết hợp cửa kính đen, tạo thiết kế và dễ hòa hợp với mọi phong cách bếp hiện đại. Kiểu lắp âm tủ giúp mặt bếp trở nên gọn gàng, liền mạch.
Kích thước sản phẩm 595 x 594 x 548 mm phù hợp khoang tủ tiêu chuẩn 585-595 x 560-568 x 550 mm. Hệ thống núm xoay dạng pop-out kết hợp phím bấm vừa thao tác nhanh, vừa dễ lau chùi mặt trước vì núm có thể thu gọn khi không dùng.
Khoang lò dung tích 71 lít với lớp tráng men bên trong cho không gian rộng rãi để chế biến nhiều món cùng lúc, từ gà nguyên con, pizza cỡ lớn tới các mẻ bánh nhiều tầng.', N'Thương hiệu: Bosch (Đức); Model: HBA514BS3; Sản xuất tại: Ba Lan; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 3400; Kiểu điều khiển: Phím bấm / Núm xoay; KT (R×S×C mm): 594 x 548 x 595; Số lớp kính cửa: 3; Yêu cầu đấu nối: Cầu chì 16 A, không có phích cắm — cần aptomat riêng; Công suất nướng (W): 2700; Dung tích (lít): 71; Số chức năng nướng: 8; Dải nhiệt (°C): 50-275; Công nghệ: 3D Hotair (đối lưu khí nóng 3D); Tự làm sạch: Thủy phân (Hydrolytic); Chức năng: 8 chế độ nướng; KT khoét (R×S×C mm): 560 x 550 x 585; An toàn: Khóa trẻ em, tự động ngắt khi đạt nhiệt độ, cửa kính cách nhiệt 3 lớp', 1, 5, 1),
(33, N'LN-KAF-002', N'Lò nướng Kaff KF-901', 14600000, 10580000, 20, N'/img/sp/ln-kaf-002/1.jpg', NULL, N'60 lít', 36, N'Lò nướng Kaff KF-901 là mẫu lò nướng âm tủ dung tích 60 lít với 9 chức năng nướng, dành cho những gia đình yêu thích làm bánh và chế biến các món nướng tại nhà. Thiết kế âm tủ chỉ để lộ mặt trước, toàn bộ phần khoang lắp chìm vào tủ bếp, giúp căn bếp gọn gàng đồng thời giữ nhiệt độ lò ổn định khi hoạt động.
Về ngoại hình, lò sử dụng mặt kính đen cao cấp kết hợp viền thép chắc chắn, cửa kính nhiều lớp cách nhiệt giúp quan sát bên trong mà vẫn hạn chế thất thoát nhiệt. Tay nắm cửa dạng thanh ngang bằng inox không gỉ cho thao tác mở nhẹ nhàng, chắc tay và bền theo thời gian.
Đèn chiếu sáng bên trong khoang giúp theo dõi món ăn mà không cần mở cửa lò, tránh làm tụt nhiệt giữa chừng. Về hiệu năng, KF-901 có tổng công suất 3600W cùng 9 chức năng nướng, trong đó nổi bật là chức năng nướng đối lưu.', N'Thương hiệu: Kaff (Đức); Model: KF-901; Sản xuất tại: Malaysia; Kiểu lắp: Âm tủ; Chất liệu: Inox; Điện áp: 220-240V; Tổng công suất (W): 3600; Kiểu điều khiển: Núm xoay / Cơ; KT (R×S×C mm): 595 x 550 x 595; Dung tích (lít): 60; Số chức năng nướng: 9; Công nghệ: Catalytic self-cleaning; Tự làm sạch: Xúc tác (Catalytic); Chức năng: 9 chức năng nướng; An toàn: Khóa trẻ em', 1, 5, 2),
(34, N'LVS-BOS-002', N'Lò vi sóng Bosch FEL053MS1M', 5900000, 5490000, 6, N'/img/sp/lvs-bos-002/1.jpg', N'/img/sp/lvs-bos-002/2.jpg|/img/sp/lvs-bos-002/3.jpg', N'25 lít', 24, N'Lò vi sóng Bosch FEL053MS1M thuộc dòng Serie 2, là mẫu lò vi sóng độc lập dành cho những gia đình cần một thiết bị linh hoạt về vị trí lắp đặt. Khác với lò âm tủ, sản phẩm có thể đặt trực tiếp trên bàn bếp, kệ hoặc bố trí trong tủ tùy theo không gian, rất phù hợp khi căn bếp đã hoàn thiện và không muốn cải tạo hệ tủ.
Về thiết kế, lò hoàn thiện bằng mặt kính đen phối inox, tạo vẻ ngoài chắc chắn và hiện đại, dễ phối với các thiết bị bếp khác. Bề mặt kính dễ lau chùi sau khi nấu, còn phần inox mang lại cảm giác cao cấp cho tổng thể sản phẩm.
Cửa lò sử dụng bản lề trái, mở sang phải, thuận tiện với đa số bố cục bếp thông dụng. Sản phẩm được trang bị công nghệ Inverter, giúp điều chỉnh công suất mượt hơn thay vì bật tắt liên tục như lò thông thường, nhờ đó thực phẩm được làm nóng đều và giữ được độ ẩm tốt hơn.', N'Thương hiệu: Bosch (Đức); Model: FEL053MS1M; Sản xuất tại: Trung Quốc; Kiểu lắp: Để bàn; Điện áp: 230-240V / 50Hz; Tổng công suất (W): 1270; Kiểu điều khiển: Cảm ứng / Núm xoay; KT (R×S×C mm): 495 x 370 x 290; Dung tích (lít): 25; Công nghệ: Inverter; Loại lò: Vi sóng + nướng; Kiểu khoang: Có đĩa quay; Công suất vi sóng (W): 800; Số mức công suất: 5', 1, 6, 1),
(35, N'LVS-KAF-002', N'Lò vi sóng Kaff KF-OMS44L', 21800000, 15790000, 8, N'/img/sp/lvs-kaf-002/1.jpg', NULL, N'44 lít', 36, N'Lò vi sóng Kaff KF-OMS44L là mẫu lò vi sóng âm tủ kết hợp nướng, mang đến giải pháp hai trong một cho những gian bếp muốn tối ưu số lượng thiết bị mà vẫn đảm bảo đầy đủ chức năng chế biến. Sản phẩm được lắp chìm vào hệ tủ bếp, giúp mặt bếp gọn gàng và tổng thể khu vực nấu trông đồng bộ hơn.
Về thiết kế và lắp đặt, lò có kích thước sản phẩm 455C x 595R x 550S mm với kích thước âm tủ 450C x 560-568R x 595S mm, tương thích với các hộc tủ tiêu chuẩn nên dễ phối vào tủ bếp đóng sẵn. Cửa lò thiết kế kín toàn phần, có thông gió và dễ dàng vệ sinh, hạn chế tình trạng bám bẩn lâu ngày ở các khe kẽ.
Hệ điều khiển cảm ứng kèm hẹn giờ điện tử cho thao tác nhanh gọn, đồng thời giữ bề mặt phẳng mịn dễ lau chùi. Về hiệu năng, máy có tổng công suất 3350W, trong đó mức công suất vi sóng đạt 900W phục vụ hâm nóng và rã đông, còn mức công suất nướng 1750W đảm nhiệm các món cần độ vàng giòn.', N'Thương hiệu: Kaff (Đức); Model: KF-OMS44L; Sản xuất tại: Malaysia; Kiểu lắp: Âm tủ; Điện áp: 220-240V / 50Hz; Tổng công suất (W): 3350; Kiểu điều khiển: Cảm ứng; KT (R×S×C mm): 595 x 548 x 454; Công suất nướng (W): 1750; Dung tích (lít): 44; Loại lò: Vi sóng + nướng + đối lưu; Công suất vi sóng (W): 900; Số mức công suất: 5; KT khoét tủ (R×S×C mm): 560 x 568 x 450; An toàn: Có', 1, 6, 2),
(36, N'LVS-BOS-003', N'Lò vi sóng Bosch BEL7321B1', 35900000, 30120000, 0, N'/img/sp/lvs-bos-003/1.jpg', N'/img/sp/lvs-bos-003/2.jpg|/img/sp/lvs-bos-003/3.jpg', N'21 lít', 36, N'Lò vi sóng kết hợp nướng âm tủ Bosch BEL7321B1 thuộc dòng Serie 8, là lựa chọn đáng cân nhắc cho những căn bếp hiện đại đề cao sự tinh gọn và công nghệ. Sản phẩm mang thương hiệu Bosch của Đức, được sản xuất tại Anh, kết hợp giữa khả năng hâm nóng, rã đông nhanh của vi sóng và chức năng nướng grill trong cùng một thiết bị, giúp rút ngắn đáng kể thời gian chuẩn bị bữa ăn hằng ngày.
Về thiết kế, BEL7321B1 sở hữu mặt kính đen sang trọng, lắp âm tủ để bề mặt lò phẳng đều với hệ tủ bếp, tạo cảm giác liền mạch và đồng bộ với các thiết bị âm tủ khác. Khoang lò dung tích 21 lít với bề mặt bên trong bằng thép không gỉ nhẵn bóng, hạn chế góc cạnh nên rất dễ lau chùi sau khi sử dụng.
Đáy khoang bằng kính cùng lớp hoàn thiện cao cấp giúp lò giữ được vẻ đẹp bền lâu. Kích thước sản phẩm 382 x 594 x 318 mm, hộc tủ lắp đặt cần 362–382 x 560–568 x 300 mm, phù hợp với đa số tủ bếp tiêu chuẩn.', N'Thương hiệu: Bosch (Đức); Model: BEL7321B1; Sản xuất tại: Anh; Kiểu lắp: Âm tủ; Điện áp: 220-240V / 50-60Hz; Tổng công suất (W): 2050; Kiểu điều khiển: Cảm ứng; KT (R×S×C mm): 594 x 318 x 382; Dung tích (lít): 21; Công nghệ: Inverter; Loại lò: Vi sóng + nướng; Kiểu khoang: Không đĩa quay (đáy phẳng); Công suất vi sóng (W): 900; Số mức công suất: 5', 0, 6, 1),
(37, N'BHN-KAF-004', N'Bếp đôi từ hồng ngoại Kaff KF-IC3801', 16800000, 11060000, 6, N'/img/sp/bhn-kaf-004/1.jpg', N'/img/sp/bhn-kaf-004/2.jpg|/img/sp/bhn-kaf-004/3.jpg', N'5900W', 36, N'Bếp điện từ Kaff KF-IC3801 là mẫu bếp ba vùng nấu lắp âm kết hợp hai vùng từ và một vùng hồng ngoại, nhập khẩu nguyên chiếc từ Malaysia. Đây là lựa chọn dành cho gia đình cần nấu nhanh, nấu nhiều món cùng lúc nhưng vẫn muốn giữ sự linh hoạt để dùng được cả những bộ nồi không nhiễm từ có sẵn trong nhà.
Mặt bếp sử dụng kính Crystal siêu bền, chịu lực và chịu sốc nhiệt tốt, chống trầy xước, tản nhiệt nhanh và cách điện an toàn với các linh kiện điện tử bên trong thân bếp. Bề mặt liền khối, sáng bóng, chỉ cần lau bằng khăn ẩm là sạch, giúp việc giữ gìn mặt bếp trở nên nhẹ nhàng.
Kích thước mặt kính 750x450 mm và kích thước khoét đá 730x400 mm, đường kính vùng nấu giữa khoảng 16 cm còn hai vùng nấu trái và phải khoảng 22 cm, bố trí rộng rãi để đặt nhiều nồi cùng lúc. Ba vùng nấu có công suất lần lượt 1800W, 1500W và 2200W.', N'Thương hiệu: Kaff (Đức); Model: KF-IC3801; Sản xuất tại: Malaysia; Kiểu lắp: Âm tủ; Chất liệu: Kính; Điện áp: 220-240V; Tổng công suất (W): 5900; Kiểu điều khiển: Cảm ứng; Số vùng nấu: 3; KT mặt kính (R×S mm): 750 x 450; KT khoét đá (R×S mm): 730 x 400; Công suất từng vùng (W): 2200 + 1500 + 2200; Booster: Có; CS Booster tối đa (W): 3500; Mặt kính: Schott Ceran (Đức); Công nghệ bo mạch: Inverter; Số vùng từ: 2; Số vùng hồng ngoại: 1; Loại mâm nhiệt: Hi-Light (Radiant); Thương hiệu mâm nhiệt: E.G.O (Đức)', 1, 2, 2),
(38, N'HM-KOC-002', N'Máy hút mùi Kocher K-2080E 70cm', 17100000, 11890000, 12, N'/img/sp/hm-koc-002/1.jpg', N'/img/sp/hm-koc-002/2.jpg', N'1800 m3/h', 36, N'Máy hút mùi Kocher K-2080E 70cm mang đến giải pháp làm sạch không khí bếp hiệu quả, kết hợp giữa công nghệ cao và thiết kế, giúp không gian bếp luôn trong lành, không mùi khói và đồng thời nâng tầm thẩm mỹ cho căn bếp hiện đại. Với kích thước 700 mm (rộng) × 430 mm (sâu) × 1281 mm (cao), máy được lắp đặt gắn tường, thiết kế vát kính tự động mở, mặt kính cường lực sang trọng và khung inox cao cấp, tạo nên một thiết kế, bền bỉ và chịu nhiệt tốt.
Hệ thống lọc mỡ bằng lưới inox và tùy chọn bộ lọc than hoạt tính giúp duy trì hiệu suất lâu dài mà không ảnh hưởng đến thẩm mỹ. Công nghệ Ultra Swing A++ của Kocher, động cơ 220 W, cung cấp công suất hút 1400 m³/h, đáp ứng tiêu chuẩn châu Âu A++.
Máy có ba cấp độ hút, màn hình LCD cảm ứng và chức năng hẹn giờ linh hoạt, cho phép người dùng điều chỉnh nhanh chóng theo nhu cầu nấu nướng.', N'Thương hiệu: Kocher (Đức); Model: K-2080E 70cm; Sản xuất tại: Malaysia; Kiểu lắp: Áp tường; Chất liệu: Inox / Kính cường lực; Điện áp: 220V; Tổng công suất (W): 220; Kiểu điều khiển: Cảm ứng; KT máy (R×S×C mm): 700 x 430; Lưu lượng hút (m³/h): 1800; Độ ồn (dB): 46; Số cấp tốc độ: 3; Số motor: 1; Công suất motor (W): 220; Loại lọc: Lọc mỡ / Lọc than hoạt tính (khử mùi); Đèn chiếu sáng: LED; Bề rộng (mm): 700; Chất liệu lưới lọc: Inox', 1, 3, 3),
(39, N'MRC-KAF-002', N'Máy rửa chén Kaff KF-BHMI775YS', 27800000, 21970000, 4, N'/img/sp/mrc-kaf-002/1.jpg', NULL, N'17 bộ', 36, N'Máy rửa chén Kaff KF-BHMI775YS là phiên bản vỏ inox màu bạc của dòng BHMI775Y, giữ nguyên toàn bộ cấu hình cao cấp và chỉ khác biệt ở tông màu bên ngoài. Đây là lựa chọn dành cho những căn bếp sử dụng nhiều thiết bị inox sáng màu, muốn tổng thể đồng bộ và cảm giác rộng thoáng hơn so với bản màu đen.
Máy có sức chứa 17 bộ đồ ăn châu Âu với 3 dàn rửa, kích thước 775 x 598R x 600S mm nên có thể đặt độc lập hoặc lắp âm dưới mặt bàn bếp. Vỏ ngoài inox cao cấp cho độ bền cao, chống bám bẩn, chống ăn mòn và rất dễ lau chùi trong môi trường bếp nhiều hơi nước.
Ba dàn rửa được thiết kế để tối ưu không gian: hai giàn dưới dành cho bát đĩa, nồi chảo và ly cốc, khay trên cùng dành riêng cho dao, dĩa, thìa. Bảng điều khiển cảm ứng cùng màn hình LED cho thao tác nhẹ nhàng và hiển thị trạng thái rõ ràng.', N'Thương hiệu: Kaff (Đức); Model: KF-BHMI775YS; Sản xuất tại: Thái Lan; Kiểu lắp: Độc lập; Điện áp: 220-240V; Độ ồn (dB): 48; Số bộ (place settings): 17; Số chương trình: 8; Công nghệ sấy: Sấy khí nóng (PTC); Điện/chu trình (kWh): 0,92; Nước/chu trình (lít): 6; KT phủ bì (R×S mm): 600 x 775; KT khoét (R×C mm): 598 x 600; KT (R×S×C mm): 600 x 598 x 775; An toàn: khóa trẻ em', 1, 4, 2),
(40, N'LN-KOC-002', N'Lò nướng Kocher KOEU-71S13F', 19700000, 11770000, 9, N'/img/sp/ln-koc-002/1.jpg', NULL, N'71 lít', 36, N'Lò nướng âm tủ Kocher KOEU-71S13F là mẫu lò dung tích lớn với 13 chương trình nướng tự động, hướng tới những gia đình yêu bếp và muốn có nhiều lựa chọn chế biến hơn. Sản phẩm mang thương hiệu Kocher xuất xứ Đức, sản xuất tại Đức, bảo hành chính hãng 36 tháng.
Về thiết kế, lò mang thiết kế theo phong cách châu Âu với vỏ ngoài bằng thép không gỉ siêu bền, sáng bóng và dễ vệ sinh. Kiểu lắp âm tủ giúp tối ưu diện tích bếp và tạo sự liền mạch cho tổng thể.
Cửa lò dày dặn với hệ thống nhiều lớp kính cách nhiệt, cụ thể là mặt kính 3 lớp chịu lực chịu nhiệt, vừa giữ nhiệt hiệu quả bên trong vừa tránh nguy cơ bỏng khi chạm vào cánh cửa. Thiết kế mở phía trước giúp thao tác đưa khay vào và lấy ra thuận tiện.
Kích thước sản phẩm 595 cao x 575 rộng x 595 sâu mm. Khoang lò dung tích 71 lít cho phép nướng nhiều thực phẩm trong cùng một lần mà vẫn đảm bảo chín đều mọi mặt.', N'Thương hiệu: Kocher (Đức); Model: KOEU-71S13F; Sản xuất tại: Đức; Kiểu lắp: Âm tủ; Chất liệu: Inox; Điện áp: 220-240V / 50Hz; Tổng công suất (W): 2300; Kiểu điều khiển: Cảm ứng; KT (R×S×C mm): 595 x 575 x 595; Số lớp kính cửa: 3; Dung tích (lít): 71; Số chức năng nướng: 13; Công nghệ: Khí nóng 3D (3D hot air); Chức năng: 13 chương trình nấu, nướng tự động; An toàn: Khóa an toàn trẻ em, kính 3 lớp chịu nhiệt', 1, 5, 3);
SET IDENTITY_INSERT san_pham OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM tai_khoan)
BEGIN
SET IDENTITY_INSERT tai_khoan ON;
INSERT INTO tai_khoan (id, ten_dang_nhap, mat_khau, ho_ten, email, vai_tro, hoat_dong, ngay_tao) VALUES
(101, N'kh101', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Nguyễn Văn An', N'an.nguyen@gmail.com', N'KHACHHANG', 1, N'2026-10-21 09:00:00'),
(102, N'kh102', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Trần Thị Bình', N'binh.tran@gmail.com', N'KHACHHANG', 1, N'2026-08-22 09:00:00'),
(103, N'kh103', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Lê Hoàng Cường', N'cuong.le@gmail.com', N'KHACHHANG', 1, N'2026-09-23 09:00:00'),
(104, N'kh104', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Phạm Thị Dung', N'dung.pham@gmail.com', N'KHACHHANG', 1, N'2026-10-24 09:00:00'),
(105, N'kh105', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Hoàng Anh Tuấn', N'tuan.hoang@gmail.com', N'KHACHHANG', 1, N'2026-08-25 09:00:00'),
(106, N'kh106', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Trần Quốc Long', N'khach106@example.com', N'KHACHHANG', 1, N'2026-09-26 09:00:00'),
(107, N'kh107', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Bùi Thị Yến', N'khach107@example.com', N'KHACHHANG', 1, N'2026-10-27 09:00:00'),
(108, N'kh108', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Dương Kim Hương', N'khach108@example.com', N'KHACHHANG', 1, N'2026-08-10 09:00:00'),
(109, N'kh109', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Phan Kim Long', N'khach109@example.com', N'KHACHHANG', 1, N'2026-09-11 09:00:00'),
(110, N'kh110', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Hồ Kim Vy', N'khach110@example.com', N'KHACHHANG', 1, N'2026-10-12 09:00:00'),
(111, N'kh111', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Nguyễn Kim Lan', N'khach111@example.com', N'KHACHHANG', 1, N'2026-08-13 09:00:00'),
(112, N'kh112', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Hoàng Thị Vy', N'khach112@example.com', N'KHACHHANG', 1, N'2026-09-14 09:00:00'),
(113, N'kh113', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Ngô Gia Tâm', N'khach113@example.com', N'KHACHHANG', 1, N'2026-10-15 09:00:00'),
(114, N'kh114', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Phạm Thu Phúc', N'khach114@example.com', N'KHACHHANG', 1, N'2026-08-16 09:00:00'),
(115, N'kh115', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Dương Quốc Thảo', N'khach115@example.com', N'KHACHHANG', 1, N'2026-09-17 09:00:00'),
(116, N'kh116', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Đỗ Hữu Tâm', N'khach116@example.com', N'KHACHHANG', 1, N'2026-10-18 09:00:00'),
(117, N'kh117', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Đỗ Quốc Lan', N'khach117@example.com', N'KHACHHANG', 1, N'2026-08-19 09:00:00'),
(118, N'kh118', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Võ Ngọc Phúc', N'khach118@example.com', N'KHACHHANG', 1, N'2026-09-20 09:00:00'),
(119, N'kh119', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Lê Kim Phúc', N'khach119@example.com', N'KHACHHANG', 1, N'2026-10-21 09:00:00'),
(120, N'kh120', N'$2a$10$nc0EQYWGqt8vqeo1V/2NuOWDztla2lo2jnr/Lpah3TSIFZk.yzxO6', N'Ngô Quốc Trang', N'khach120@example.com', N'KHACHHANG', 1, N'2026-08-22 09:00:00');
SET IDENTITY_INSERT tai_khoan OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM khach_hang)
BEGIN
SET IDENTITY_INSERT khach_hang ON;
INSERT INTO khach_hang (id, ho_ten, so_dien_thoai, email, dia_chi, tai_khoan_id) VALUES
(101, N'Nguyễn Văn An', N'0901234567', N'an.nguyen@gmail.com', N'123 Lê Lợi, Q1, TP.HCM', 101),
(102, N'Trần Thị Bình', N'0912345678', N'binh.tran@gmail.com', N'456 Nguyễn Huệ, Q1, TP.HCM', 102),
(103, N'Lê Hoàng Cường', N'0923456789', N'cuong.le@gmail.com', N'789 Trần Hưng Đạo, Q5, TP.HCM', 103),
(104, N'Phạm Thị Dung', N'0934567890', N'dung.pham@gmail.com', N'101 Võ Văn Ngân, Thủ Đức, TP.HCM', 104),
(105, N'Hoàng Anh Tuấn', N'0945678901', N'tuan.hoang@gmail.com', N'202 Phan Đăng Lưu, Phú Nhuận, TP.HCM', 105),
(106, N'Trần Quốc Long', N'0978695681', N'khach106@example.com', N'58 Cách Mạng Tháng 8, Q3, TP.HCM', 106),
(107, N'Bùi Thị Yến', N'0939968745', N'khach107@example.com', N'33 Nguyễn Trãi, Q5, TP.HCM', 107),
(108, N'Dương Kim Hương', N'0984700087', N'khach108@example.com', N'91 Âu Cơ, Tân Phú, TP.HCM', 108),
(109, N'Phan Kim Long', N'0975888073', N'khach109@example.com', N'140 Lê Trọng Tấn, Tân Phú, TP.HCM', 109),
(110, N'Hồ Kim Vy', N'0942203407', N'khach110@example.com', N'27 Phạm Văn Đồng, Thủ Đức, TP.HCM', 110),
(111, N'Nguyễn Kim Lan', N'0924862807', N'khach111@example.com', N'8 Hoàng Văn Thụ, Phú Nhuận, TP.HCM', 111),
(112, N'Hoàng Thị Vy', N'0911542004', N'khach112@example.com', N'66 Quang Trung, Gò Vấp, TP.HCM', 112),
(113, N'Ngô Gia Tâm', N'0952175656', N'khach113@example.com', N'15 Nguyễn Văn Linh, Q7, TP.HCM', 113),
(114, N'Phạm Thu Phúc', N'0956661846', N'khach114@example.com', N'203 Hậu Giang, Q6, TP.HCM', 114),
(115, N'Dương Quốc Thảo', N'0960512102', N'khach115@example.com', N'44 Xô Viết Nghệ Tĩnh, Bình Thạnh, TP.HCM', 115),
(116, N'Đỗ Hữu Tâm', N'0920243483', N'khach116@example.com', N'19 Kinh Dương Vương, Bình Tân, TP.HCM', 116),
(117, N'Đỗ Quốc Lan', N'0984819744', N'khach117@example.com', N'72 Trường Chinh, Tân Bình, TP.HCM', 117),
(118, N'Võ Ngọc Phúc', N'0971397604', N'khach118@example.com', N'5 Tôn Đức Thắng, Q1, TP.HCM', 118),
(119, N'Lê Kim Phúc', N'0913320452', N'khach119@example.com', N'120 Lạc Long Quân, Q11, TP.HCM', 119),
(120, N'Ngô Quốc Trang', N'0971884106', N'khach120@example.com', N'12 Lý Thường Kiệt, Q10, TP.HCM', 120);
SET IDENTITY_INSERT khach_hang OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM don_hang)
BEGIN
SET IDENTITY_INSERT don_hang ON;
INSERT INTO don_hang (id, khach_hang_id, ngay_dat, tong_tien, trang_thai, ten_nguoi_nhan, so_dien_thoai_nhan, dia_chi_giao, ghi_chu) VALUES
(901, 101, N'2026-10-20 08:30:00', 15240000, N'HOAN_THANH', N'Nguyễn Văn An', N'0901234567', N'123 Lê Lợi, Q1, TP.HCM', N'Giao giờ hành chính'),
(902, 101, N'2026-10-24 14:15:00', 20810000, N'CHO_XAC_NHAN', N'Nguyễn Văn An', N'0901234567', N'123 Lê Lợi, Q1, TP.HCM', N''),
(903, 102, N'2026-10-21 09:00:00', 11640000, N'HOAN_THANH', N'Trần Thị Bình', N'0912345678', N'456 Nguyễn Huệ, Q1, TP.HCM', N'Gọi trước khi giao'),
(904, 102, N'2026-10-25 10:30:00', 10370000, N'DANG_GIAO', N'Trần Thị Bình', N'0912345678', N'456 Nguyễn Huệ, Q1, TP.HCM', N''),
(905, 103, N'2026-10-22 11:45:00', 11340000, N'DA_HUY', N'Lê Hoàng Cường', N'0923456789', N'789 Trần Hưng Đạo, Q5, TP.HCM', N'Khách đổi ý'),
(906, 103, N'2026-10-26 15:20:00', 12380000, N'CHO_XAC_NHAN', N'Lê Hoàng Cường', N'0923456789', N'789 Trần Hưng Đạo, Q5, TP.HCM', N''),
(907, 104, N'2026-10-18 16:10:00', 26220000, N'HOAN_THANH', N'Phạm Thị Dung', N'0934567890', N'101 Võ Văn Ngân, Thủ Đức, TP.HCM', N''),
(908, 104, N'2026-10-26 08:05:00', 3780000, N'CHO_XAC_NHAN', N'Phạm Thị Dung', N'0934567890', N'101 Võ Văn Ngân, Thủ Đức, TP.HCM', N''),
(909, 105, N'2026-10-23 19:30:00', 15370000, N'DANG_GIAO', N'Hoàng Anh Tuấn', N'0945678901', N'202 Phan Đăng Lưu, Phú Nhuận, TP.HCM', N'Giao tối'),
(910, 105, N'2026-10-25 20:45:00', 4350000, N'DA_HUY', N'Hoàng Anh Tuấn', N'0945678901', N'202 Phan Đăng Lưu, Phú Nhuận, TP.HCM', N'Sai địa chỉ');
INSERT INTO don_hang (id, khach_hang_id, ngay_dat, tong_tien, trang_thai, ten_nguoi_nhan, so_dien_thoai_nhan, dia_chi_giao, ghi_chu, phuong_thuc_thanh_toan, da_thanh_toan, tam_tinh, so_tien_giam, ly_do_huy, huy_boi) VALUES
(911, 106, N'2026-08-02 08:00:00', 31438400, N'HOAN_THANH', N'Trần Quốc Long', N'0978695681', N'58 Cách Mạng Tháng 8, Q3, TP.HCM', NULL, N'CK', 1, 32080000, 641600, NULL, NULL),
(912, 107, N'2026-09-05 09:07:00', 39980000, N'HOAN_THANH', N'Bùi Thị Yến', N'0939968745', N'33 Nguyễn Trãi, Q5, TP.HCM', NULL, N'COD', 1, 39980000, 0, NULL, NULL),
(913, 108, N'2026-10-08 10:14:00', 21710000, N'DA_HUY', N'Dương Kim Hương', N'0984700087', N'91 Âu Cơ, Tân Phú, TP.HCM', NULL, N'COD', 0, 21710000, 0, N'Khách đổi ý', N'KHACH'),
(914, 109, N'2026-11-11 11:21:00', 52106600, N'HOAN_THANH', N'Phan Kim Long', N'0975888073', N'140 Lê Trọng Tấn, Tân Phú, TP.HCM', NULL, N'CK', 1, 53170000, 1063400, NULL, NULL),
(915, 110, N'2026-08-14 12:28:00', 86940000, N'DANG_GIAO', N'Hồ Kim Vy', N'0942203407', N'27 Phạm Văn Đồng, Thủ Đức, TP.HCM', NULL, N'COD', 0, 86940000, 0, NULL, NULL),
(916, 111, N'2026-09-17 13:35:00', 4350000, N'CHO_XAC_NHAN', N'Nguyễn Kim Lan', N'0924862807', N'8 Hoàng Văn Thụ, Phú Nhuận, TP.HCM', NULL, N'COD', 0, 4350000, 0, NULL, NULL),
(917, 112, N'2026-10-20 14:42:00', 14935200, N'DANG_GIAO', N'Hoàng Thị Vy', N'0911542004', N'66 Quang Trung, Gò Vấp, TP.HCM', NULL, N'CK', 1, 15240000, 304800, NULL, NULL),
(918, 113, N'2026-11-23 15:49:00', 43940000, N'HOAN_THANH', N'Ngô Gia Tâm', N'0952175656', N'15 Nguyễn Văn Linh, Q7, TP.HCM', NULL, N'COD', 1, 43940000, 0, NULL, NULL),
(919, 114, N'2026-08-26 16:56:00', 21670000, N'DA_HUY', N'Phạm Thu Phúc', N'0956661846', N'203 Hậu Giang, Q6, TP.HCM', NULL, N'COD', 0, 21670000, 0, N'Khách đổi ý', N'KHACH'),
(920, 115, N'2026-09-03 17:03:00', 49960400, N'DA_XAC_NHAN', N'Dương Quốc Thảo', N'0960512102', N'44 Xô Viết Nghệ Tĩnh, Bình Thạnh, TP.HCM', NULL, N'CK', 1, 50980000, 1019600, NULL, NULL),
(921, 116, N'2026-10-06 18:10:00', 42520000, N'HOAN_THANH', N'Đỗ Hữu Tâm', N'0920243483', N'19 Kinh Dương Vương, Bình Tân, TP.HCM', NULL, N'COD', 1, 42520000, 0, NULL, NULL),
(922, 117, N'2026-11-09 19:17:00', 5490000, N'DANG_GIAO', N'Đỗ Quốc Lan', N'0984819744', N'72 Trường Chinh, Tân Bình, TP.HCM', NULL, N'COD', 0, 5490000, 0, NULL, NULL),
(923, 118, N'2026-08-12 08:24:00', 7560000, N'CHO_XAC_NHAN', N'Võ Ngọc Phúc', N'0971397604', N'5 Tôn Đức Thắng, Q1, TP.HCM', NULL, N'COD', 0, 7560000, 0, NULL, NULL),
(924, 119, N'2026-09-15 09:31:00', 61030000, N'HOAN_THANH', N'Lê Kim Phúc', N'0913320452', N'120 Lạc Long Quân, Q11, TP.HCM', NULL, N'COD', 1, 61030000, 0, NULL, NULL),
(925, 120, N'2026-10-18 10:38:00', 11640000, N'HOAN_THANH', N'Ngô Quốc Trang', N'0971884106', N'12 Lý Thường Kiệt, Q10, TP.HCM', NULL, N'COD', 1, 11640000, 0, NULL, NULL),
(926, 101, N'2026-11-21 11:45:00', 27802600, N'HOAN_THANH', N'Nguyễn Văn An', N'0901234567', N'123 Lê Lợi, Q1, TP.HCM', NULL, N'CK', 1, 28370000, 567400, NULL, NULL),
(927, 102, N'2026-08-24 12:52:00', 30480000, N'CHO_XAC_NHAN', N'Trần Thị Bình', N'0912345678', N'456 Nguyễn Huệ, Q1, TP.HCM', NULL, N'COD', 0, 30480000, 0, NULL, NULL),
(928, 103, N'2026-09-27 13:59:00', 33810000, N'DA_XAC_NHAN', N'Lê Hoàng Cường', N'0923456789', N'789 Trần Hưng Đạo, Q5, TP.HCM', NULL, N'COD', 0, 33810000, 0, NULL, NULL),
(929, 104, N'2026-10-04 14:06:00', 11534600, N'HOAN_THANH', N'Phạm Thị Dung', N'0934567890', N'101 Võ Văn Ngân, Thủ Đức, TP.HCM', NULL, N'CK', 1, 11770000, 235400, NULL, NULL),
(930, 105, N'2026-11-07 15:13:00', 22470000, N'DA_XAC_NHAN', N'Hoàng Anh Tuấn', N'0945678901', N'202 Phan Đăng Lưu, Phú Nhuận, TP.HCM', NULL, N'COD', 0, 22470000, 0, NULL, NULL);
SET IDENTITY_INSERT don_hang OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM chi_tiet_don_hang)
BEGIN
SET IDENTITY_INSERT chi_tiet_don_hang ON;
INSERT INTO chi_tiet_don_hang (id, don_hang_id, san_pham_id, so_luong, don_gia) VALUES
(901, 901, 1, 1, 15240000), (902, 902, 2, 1, 20810000), (903, 903, 7, 1, 11640000), (904, 904, 9, 1, 10370000),
(905, 905, 4, 1, 11340000), (906, 906, 3, 1, 12380000), (907, 907, 14, 1, 26220000), (908, 908, 12, 1, 3780000),
(909, 909, 18, 1, 15370000), (910, 910, 13, 1, 4350000);
INSERT INTO chi_tiet_don_hang (id, don_hang_id, san_pham_id, so_luong, don_gia) VALUES
(911, 911, 15, 1, 21710000),
(912, 911, 9, 1, 10370000),
(913, 912, 40, 2, 11770000),
(914, 912, 28, 1, 16440000),
(915, 913, 15, 1, 21710000),
(916, 914, 30, 1, 11950000),
(917, 914, 31, 2, 20610000),
(918, 915, 20, 2, 13350000),
(919, 915, 36, 2, 30120000),
(920, 916, 13, 1, 4350000),
(921, 917, 1, 1, 15240000),
(922, 918, 39, 2, 21970000),
(923, 919, 32, 1, 13540000),
(924, 919, 24, 1, 8130000),
(925, 920, 32, 2, 13540000),
(926, 920, 30, 2, 11950000),
(927, 921, 3, 1, 12380000),
(928, 921, 19, 2, 15070000),
(929, 922, 29, 1, 5490000),
(930, 923, 12, 2, 3780000),
(931, 924, 31, 1, 20610000),
(932, 924, 5, 2, 20210000),
(933, 925, 7, 1, 11640000),
(934, 926, 27, 2, 11720000),
(935, 926, 10, 1, 4930000),
(936, 927, 1, 2, 15240000),
(937, 928, 35, 1, 15790000),
(938, 928, 6, 1, 18020000),
(939, 929, 40, 1, 11770000),
(940, 930, 18, 1, 15370000),
(941, 930, 23, 1, 7100000);
SET IDENTITY_INSERT chi_tiet_don_hang OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM phieu_nhap)
BEGIN
SET IDENTITY_INSERT phieu_nhap ON;
INSERT INTO phieu_nhap (id, ngay_nhap, tong_tien, ghi_chu, nha_cung_cap_id) VALUES
(1, N'2026-07-06 10:00:00', 125400000, N'Nhập hàng đợt 1', 1),
(2, N'2026-07-11 10:00:00', 222550000, N'Nhập hàng đợt 2', 2),
(3, N'2026-07-16 10:00:00', 99260000, N'Nhập hàng đợt 3', 3),
(4, N'2026-07-21 10:00:00', 57250000, N'Nhập hàng đợt 4', 4),
(5, N'2026-07-26 10:00:00', 83800000, N'Nhập hàng đợt 5', 5),
(6, N'2026-07-04 10:00:00', 65280000, N'Nhập hàng đợt 6', 6),
(7, N'2026-08-09 10:00:00', 231900000, N'Nhập hàng đợt 7', 7),
(8, N'2026-08-14 10:00:00', 150000000, N'Nhập hàng đợt 8', 8),
(9, N'2026-08-19 10:00:00', 101450000, N'Nhập hàng đợt 9', 9),
(10, N'2026-08-24 10:00:00', 203760000, N'Nhập hàng đợt 10', 10),
(11, N'2026-08-02 10:00:00', 15650000, N'Nhập hàng đợt 11', 11),
(12, N'2026-08-07 10:00:00', 34110000, N'Nhập hàng đợt 12', 12),
(13, N'2026-09-12 10:00:00', 83590000, N'Nhập hàng đợt 13', 13),
(14, N'2026-09-17 10:00:00', 188850000, N'Nhập hàng đợt 14', 14),
(15, N'2026-09-22 10:00:00', 152510000, N'Nhập hàng đợt 15', 15),
(16, N'2026-09-27 10:00:00', 90480000, N'Nhập hàng đợt 16', 16),
(17, N'2026-09-05 10:00:00', 164000000, N'Nhập hàng đợt 17', 17),
(18, N'2026-09-10 10:00:00', 104000000, N'Nhập hàng đợt 18', 18),
(19, N'2026-10-15 10:00:00', 31600000, N'Nhập hàng đợt 19', 19),
(20, N'2026-10-20 10:00:00', 94400000, N'Nhập hàng đợt 20', 20);
SET IDENTITY_INSERT phieu_nhap OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM chi_tiet_phieu_nhap)
BEGIN
SET IDENTITY_INSERT chi_tiet_phieu_nhap ON;
INSERT INTO chi_tiet_phieu_nhap (id, phieu_nhap_id, san_pham_id, so_luong, gia_nhap) VALUES
(1, 1, 38, 10, 8560000),
(2, 1, 37, 5, 7960000),
(3, 2, 15, 5, 15630000),
(4, 2, 31, 8, 14840000),
(5, 2, 38, 3, 8560000),
(6, 3, 11, 8, 3070000),
(7, 3, 9, 10, 7470000),
(8, 4, 10, 5, 3550000),
(9, 4, 34, 10, 3950000),
(10, 5, 7, 10, 8380000),
(11, 6, 4, 8, 8160000),
(12, 7, 23, 10, 5110000),
(13, 7, 2, 8, 14980000),
(14, 7, 33, 8, 7620000),
(15, 8, 21, 10, 7680000),
(16, 8, 8, 10, 7320000),
(17, 9, 40, 5, 8470000),
(18, 9, 11, 10, 3070000),
(19, 9, 10, 8, 3550000),
(20, 10, 39, 3, 15820000),
(21, 10, 15, 10, 15630000),
(22, 11, 13, 5, 3130000),
(23, 12, 35, 3, 11370000),
(24, 13, 13, 3, 3130000),
(25, 13, 31, 5, 14840000),
(26, 14, 34, 5, 3950000),
(27, 14, 14, 5, 18880000),
(28, 14, 9, 10, 7470000),
(29, 15, 28, 10, 11840000),
(30, 15, 35, 3, 11370000),
(31, 16, 20, 8, 9610000),
(32, 16, 12, 5, 2720000),
(33, 17, 30, 10, 8600000),
(34, 17, 32, 8, 9750000),
(35, 18, 12, 10, 2720000),
(36, 18, 21, 10, 7680000),
(37, 19, 34, 8, 3950000),
(38, 20, 14, 5, 18880000);
SET IDENTITY_INSERT chi_tiet_phieu_nhap OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM ma_giam_gia)
BEGIN
SET IDENTITY_INSERT ma_giam_gia ON;
INSERT INTO ma_giam_gia (id, ma, mo_ta, loai_giam, gia_tri, giam_toi_da, don_toi_thieu, bat_dau, ket_thuc, hoat_dong, gioi_han_luot_dung, so_luot_da_dung, moi_khach_mot_lan, hien_cho_khach, ngay_tao) VALUES
(1, N'CHAOBANMOI', N'Chào thành viên mới', N'PHAN_TRAM', 5, 500000, 0, N'2026-01-01 00:00:00', NULL, 1, NULL, 0, 1, 1, N'2026-01-01 00:00:00'),
(2, N'BEPNE300K', N'Giảm 300k đơn từ 10 triệu', N'SO_TIEN', 300000, NULL, 10000000, N'2026-01-01 00:00:00', N'2027-12-31 23:59:00', 1, 200, 0, 1, 1, N'2026-01-02 00:00:00'),
(3, N'TET2027', N'Khuyến mãi Tết - chưa mở', N'PHAN_TRAM', 8, 1000000, 5000000, N'2027-01-15 00:00:00', N'2027-02-15 00:00:00', 1, 100, 0, 1, 1, N'2026-01-03 00:00:00'),
(4, N'NOEL2025', N'Giáng sinh 2025 - đã hết hạn', N'SO_TIEN', 200000, NULL, 3000000, N'2025-12-01 00:00:00', N'2025-12-31 23:59:00', 1, NULL, 0, 1, 0, N'2025-11-20 00:00:00');
SET IDENTITY_INSERT ma_giam_gia OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM khuyen_mai)
BEGIN
SET IDENTITY_INSERT khuyen_mai ON;
INSERT INTO khuyen_mai (id, ten, mo_ta, loai_giam, gia_tri, giam_toi_da, don_toi_thieu, bat_dau, ket_thuc, hoat_dong, pham_vi, loai_san_pham_id, thuong_hieu_id, chi_thanh_vien) VALUES
(1, N'Tuần lễ bếp từ - thành viên giảm 3%', N'Áp dụng cho mọi bếp điện từ', N'PHAN_TRAM', 3, 1000000, 0, N'2026-11-01 00:00:00', N'2026-12-31 23:59:00', 1, N'LOAI_SAN_PHAM', 1, NULL, 1),
(2, N'Thành viên mua Bosch giảm 500k', N'Đơn hàng Bosch từ 15 triệu', N'SO_TIEN', 500000, NULL, 15000000, N'2026-11-01 00:00:00', N'2026-12-31 23:59:00', 1, N'THUONG_HIEU', NULL, 1, 1),
(3, N'Giảm 1% mọi đơn từ 20 triệu', N'Áp dụng cả khách không đăng nhập', N'PHAN_TRAM', 1, 300000, 20000000, N'2026-11-01 00:00:00', NULL, 1, N'TAT_CA', NULL, NULL, 0);
SET IDENTITY_INSERT khuyen_mai OFF;
END
GO
IF NOT EXISTS (SELECT 1 FROM danh_gia)
BEGIN
SET IDENTITY_INSERT danh_gia ON;
INSERT INTO danh_gia (id, san_pham_id, khach_hang_id, so_sao, noi_dung, ngay_tao, hien) VALUES
(1, 1, 101, 5, N'Bếp nấu nhanh, mặt kính dễ lau. Nhân viên giao và lắp đặt đúng hẹn.', N'2026-10-23 19:10:00', 1),
(2, 7, 102, 4, N'Bếp chạy êm, nấu ổn. Trừ 1 sao vì hộp hơi móp khi giao.', N'2026-10-24 08:40:00', 1),
(3, 14, 104, 5, N'Máy rửa sạch, sấy khô tốt, gia đình 5 người dùng rất hợp.', N'2026-10-21 21:05:00', 1);
INSERT INTO danh_gia (id, san_pham_id, khach_hang_id, so_sao, noi_dung, ngay_tao, hien) VALUES
(4, 1, 103, 5, N'Đáng tiền, sẽ ủng hộ cửa hàng lần sau.', N'2026-10-05 20:00:00', 1),
(5, 22, 114, 4, N'Chất lượng ổn, bảo hành rõ ràng.', N'2026-11-06 20:00:00', 1),
(6, 12, 101, 5, N'Thiết kế đẹp, hợp với tủ bếp nhà mình.', N'2026-09-07 20:00:00', 1),
(7, 11, 112, 4, N'Chất lượng ổn, bảo hành rõ ràng.', N'2026-10-08 20:00:00', 1),
(8, 37, 118, 5, N'Nhân viên lắp đặt nhiệt tình, hướng dẫn kỹ.', N'2026-11-09 20:00:00', 1),
(9, 8, 120, 4, N'Chạy êm, tiết kiệm điện. Giao hàng hơi trễ một ngày.', N'2026-09-10 20:00:00', 1),
(10, 36, 119, 5, N'Nhân viên lắp đặt nhiệt tình, hướng dẫn kỹ.', N'2026-10-11 20:00:00', 1),
(11, 37, 110, 4, N'Chạy êm, tiết kiệm điện. Giao hàng hơi trễ một ngày.', N'2026-11-12 20:00:00', 1),
(12, 36, 105, 5, N'Dùng một tháng thấy rất ổn, cả nhà đều thích.', N'2026-09-13 20:00:00', 1),
(13, 17, 104, 3, N'Dùng được, nhưng hướng dẫn sử dụng chưa có tiếng Việt.', N'2026-10-14 20:00:00', 1),
(14, 11, 104, 5, N'Hàng chính hãng, đóng gói kỹ, giao đúng hẹn.', N'2026-11-15 20:00:00', 1),
(15, 23, 106, 5, N'Dùng một tháng thấy rất ổn, cả nhà đều thích.', N'2026-09-16 20:00:00', 1),
(16, 33, 111, 4, N'Chạy êm, tiết kiệm điện. Giao hàng hơi trễ một ngày.', N'2026-10-17 20:00:00', 1),
(17, 38, 103, 4, N'Chạy êm, tiết kiệm điện. Giao hàng hơi trễ một ngày.', N'2026-11-18 20:00:00', 1),
(18, 21, 106, 4, N'Chạy êm, tiết kiệm điện. Giao hàng hơi trễ một ngày.', N'2026-09-19 20:00:00', 1),
(19, 17, 103, 5, N'Hàng chính hãng, đóng gói kỹ, giao đúng hẹn.', N'2026-10-20 20:00:00', 1),
(20, 27, 109, 3, N'Dùng được, nhưng hướng dẫn sử dụng chưa có tiếng Việt.', N'2026-11-21 20:00:00', 1),
(21, 1, 102, 5, N'Dùng một tháng thấy rất ổn, cả nhà đều thích.', N'2026-09-22 20:00:00', 1),
(22, 16, 101, 4, N'Chạy êm, tiết kiệm điện. Giao hàng hơi trễ một ngày.', N'2026-10-23 20:00:00', 1),
(23, 8, 104, 4, N'Chất lượng ổn, bảo hành rõ ràng.', N'2026-11-24 20:00:00', 1);
SET IDENTITY_INSERT danh_gia OFF;
END
GO
