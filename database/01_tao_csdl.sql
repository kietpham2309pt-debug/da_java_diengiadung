IF DB_ID(N'dien_gia_dung') IS NULL
    CREATE DATABASE dien_gia_dung COLLATE Vietnamese_CI_AI;
GO

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = N'bepne')
    CREATE LOGIN bepne WITH PASSWORD = N'BepNe@2026', CHECK_POLICY = OFF;
ALTER SERVER ROLE dbcreator ADD MEMBER bepne;
GO

USE dien_gia_dung;
GO

IF NOT EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N'bepne')
BEGIN
    CREATE USER bepne FOR LOGIN bepne;
    ALTER ROLE db_owner ADD MEMBER bepne;
END
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

CREATE TABLE [dbo].[chi_tiet_don_hang](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[don_gia] [bigint] NULL,
	[so_luong] [int] NOT NULL,
	[don_hang_id] [bigint] NULL,
	[san_pham_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[chi_tiet_phieu_nhap](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[gia_nhap] [bigint] NULL,
	[so_luong] [int] NOT NULL,
	[phieu_nhap_id] [bigint] NULL,
	[san_pham_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[danh_gia](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[hien] [bit] NOT NULL,
	[ngay_tao] [datetime2](6) NULL,
	[noi_dung] [nvarchar](1000) NULL,
	[so_sao] [int] NOT NULL,
	[khach_hang_id] [bigint] NOT NULL,
	[san_pham_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [uk_danh_gia_sp_kh] UNIQUE NONCLUSTERED 
(
	[san_pham_id] ASC,
	[khach_hang_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[don_hang](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[da_thanh_toan] [bit] NULL,
	[dia_chi_cu_the] [nvarchar](200) NULL,
	[dia_chi_giao] [nvarchar](255) NULL,
	[email_nhan] [nvarchar](120) NULL,
	[ghi_chu] [nvarchar](500) NULL,
	[huy_boi] [nvarchar](10) NULL,
	[ly_do_huy] [nvarchar](300) NULL,
	[ma_giam_gia] [nvarchar](30) NULL,
	[ngay_dat] [datetime2](6) NULL,
	[ngay_huy] [datetime2](6) NULL,
	[phuong_thuc_thanh_toan] [nvarchar](10) NULL,
	[phuong_xa] [nvarchar](80) NULL,
	[so_dien_thoai_nhan] [nvarchar](255) NULL,
	[so_tien_giam] [bigint] NULL,
	[tam_tinh] [bigint] NULL,
	[ten_khuyen_mai] [nvarchar](120) NULL,
	[ten_nguoi_nhan] [nvarchar](255) NULL,
	[tien_giam_ma] [bigint] NULL,
	[tien_khuyen_mai] [bigint] NULL,
	[tinh_thanh] [nvarchar](80) NULL,
	[tong_tien] [bigint] NULL,
	[trang_thai] [nvarchar](255) NULL,
	[khach_hang_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[khach_hang](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[dia_chi] [nvarchar](255) NULL,
	[email] [nvarchar](255) NULL,
	[ho_ten] [nvarchar](255) NOT NULL,
	[so_dien_thoai] [nvarchar](255) NULL,
	[tai_khoan_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE UNIQUE NONCLUSTERED INDEX [UKjqpifldnnbhqlc1o42pbsuxqf] ON [dbo].[khach_hang]
(
	[tai_khoan_id] ASC
)
WHERE ([tai_khoan_id] IS NOT NULL)
WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]

GO

CREATE TABLE [dbo].[khuyen_mai](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[bat_dau] [datetime2](6) NOT NULL,
	[don_toi_thieu] [bigint] NOT NULL,
	[gia_tri] [bigint] NOT NULL,
	[giam_toi_da] [bigint] NULL,
	[hoat_dong] [bit] NOT NULL,
	[ket_thuc] [datetime2](6) NULL,
	[loai_giam] [nvarchar](10) NOT NULL,
	[chi_thanh_vien] [bit] NOT NULL,
	[mo_ta] [nvarchar](255) NULL,
	[pham_vi] [nvarchar](20) NOT NULL,
	[ten] [nvarchar](120) NOT NULL,
	[loai_san_pham_id] [bigint] NULL,
	[thuong_hieu_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[khuyen_mai]  WITH CHECK ADD CHECK  (([don_toi_thieu]>=(0)))

GO

ALTER TABLE [dbo].[khuyen_mai]  WITH CHECK ADD CHECK  (([gia_tri]>=(1)))

GO

ALTER TABLE [dbo].[khuyen_mai]  WITH CHECK ADD CHECK  (([giam_toi_da]>=(1)))

GO

CREATE TABLE [dbo].[loai_san_pham](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[ma_loai] [nvarchar](50) NOT NULL,
	[mo_ta] [nvarchar](255) NULL,
	[ten_loai] [nvarchar](100) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UK1082k5ntghryrmpmgtui8dukw] UNIQUE NONCLUSTERED 
(
	[ten_loai] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UKp1ullvkamhvegy0x9o3q6thk3] UNIQUE NONCLUSTERED 
(
	[ma_loai] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[ma_giam_gia](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[bat_dau] [datetime2](6) NOT NULL,
	[don_toi_thieu] [bigint] NOT NULL,
	[gia_tri] [bigint] NOT NULL,
	[giam_toi_da] [bigint] NULL,
	[hoat_dong] [bit] NOT NULL,
	[ket_thuc] [datetime2](6) NULL,
	[loai_giam] [nvarchar](10) NOT NULL,
	[gioi_han_luot_dung] [int] NULL,
	[hien_cho_khach] [bit] NOT NULL,
	[ma] [nvarchar](30) NOT NULL,
	[mo_ta] [nvarchar](255) NULL,
	[moi_khach_mot_lan] [bit] NOT NULL,
	[ngay_tao] [datetime2](6) NULL,
	[so_luot_da_dung] [int] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UKacuss6gxp9w56b8rg3mmg3yux] UNIQUE NONCLUSTERED 
(
	[ma] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[ma_giam_gia]  WITH CHECK ADD CHECK  (([don_toi_thieu]>=(0)))

GO

ALTER TABLE [dbo].[ma_giam_gia]  WITH CHECK ADD CHECK  (([gia_tri]>=(1)))

GO

ALTER TABLE [dbo].[ma_giam_gia]  WITH CHECK ADD CHECK  (([giam_toi_da]>=(1)))

GO

ALTER TABLE [dbo].[ma_giam_gia]  WITH CHECK ADD CHECK  (([gioi_han_luot_dung]>=(1)))

GO

CREATE TABLE [dbo].[nha_cung_cap](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[dia_chi] [nvarchar](255) NULL,
	[email] [nvarchar](255) NULL,
	[so_dien_thoai] [nvarchar](255) NULL,
	[ten_nha_cung_cap] [nvarchar](150) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[phieu_nhap](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[ghi_chu] [nvarchar](255) NULL,
	[ngay_nhap] [datetime2](6) NULL,
	[tong_tien] [bigint] NULL,
	[nha_cung_cap_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[san_pham](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[anh_phu] [nvarchar](2000) NULL,
	[bao_hanh_thang] [int] NOT NULL,
	[cong_suat] [nvarchar](100) NULL,
	[dang_ban] [bit] NOT NULL,
	[gia_ban] [bigint] NOT NULL,
	[gia_khuyen_mai] [bigint] NULL,
	[hinh_anh] [nvarchar](255) NULL,
	[ma_san_pham] [nvarchar](50) NOT NULL,
	[mo_ta] [nvarchar](1000) NULL,
	[so_luong_ton] [int] NOT NULL,
	[ten_san_pham] [nvarchar](200) NOT NULL,
	[thong_so_ky_thuat] [nvarchar](2000) NULL,
	[loai_san_pham_id] [bigint] NOT NULL,
	[thuong_hieu_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UKicwy2i055in7b498c5v1dq3ie] UNIQUE NONCLUSTERED 
(
	[ma_san_pham] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD CHECK  (([bao_hanh_thang]>=(0) AND [bao_hanh_thang]<=(120)))

GO

ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD CHECK  (([gia_ban]>=(1)))

GO

ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD CHECK  (([gia_khuyen_mai]>=(0)))

GO

ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD CHECK  (([so_luong_ton]>=(0)))

GO

CREATE TABLE [dbo].[san_pham_yeu_thich](
	[khach_hang_id] [bigint] NOT NULL,
	[san_pham_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[khach_hang_id] ASC,
	[san_pham_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[tai_khoan](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[email] [nvarchar](255) NULL,
	[ho_ten] [nvarchar](255) NOT NULL,
	[hoat_dong] [bit] NOT NULL,
	[mat_khau] [nvarchar](255) NOT NULL,
	[ngay_tao] [datetime2](6) NULL,
	[ten_dang_nhap] [nvarchar](50) NOT NULL,
	[vai_tro] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UKgkh4qh51gkiu8ccu1ybn1q7h7] UNIQUE NONCLUSTERED 
(
	[ten_dang_nhap] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

CREATE TABLE [dbo].[thuong_hieu](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[ten_thuong_hieu] [nvarchar](100) NOT NULL,
	[xuat_xu] [nvarchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [UKgygriqct8ovdqnujffj9v5lrg] UNIQUE NONCLUSTERED 
(
	[ten_thuong_hieu] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[chi_tiet_don_hang]  WITH CHECK ADD  CONSTRAINT [FKnlr5jpmfbvgad2w40cqo54mwg] FOREIGN KEY([san_pham_id])
REFERENCES [dbo].[san_pham] ([id])
GO

ALTER TABLE [dbo].[chi_tiet_don_hang] CHECK CONSTRAINT [FKnlr5jpmfbvgad2w40cqo54mwg]
GO

ALTER TABLE [dbo].[chi_tiet_don_hang]  WITH CHECK ADD  CONSTRAINT [FKt57maavf6s28hxyar724mdr1b] FOREIGN KEY([don_hang_id])
REFERENCES [dbo].[don_hang] ([id])
GO

ALTER TABLE [dbo].[chi_tiet_don_hang] CHECK CONSTRAINT [FKt57maavf6s28hxyar724mdr1b]
GO

ALTER TABLE [dbo].[chi_tiet_phieu_nhap]  WITH CHECK ADD  CONSTRAINT [FK1fx6o5y1lofdxf6pegut4j4bm] FOREIGN KEY([san_pham_id])
REFERENCES [dbo].[san_pham] ([id])
GO

ALTER TABLE [dbo].[chi_tiet_phieu_nhap] CHECK CONSTRAINT [FK1fx6o5y1lofdxf6pegut4j4bm]
GO

ALTER TABLE [dbo].[chi_tiet_phieu_nhap]  WITH CHECK ADD  CONSTRAINT [FKrwv42blu18960otrpapu1xyet] FOREIGN KEY([phieu_nhap_id])
REFERENCES [dbo].[phieu_nhap] ([id])
GO

ALTER TABLE [dbo].[chi_tiet_phieu_nhap] CHECK CONSTRAINT [FKrwv42blu18960otrpapu1xyet]
GO

ALTER TABLE [dbo].[danh_gia]  WITH CHECK ADD  CONSTRAINT [FKpb5x40mo1o7n473vju2rc2luc] FOREIGN KEY([san_pham_id])
REFERENCES [dbo].[san_pham] ([id])
GO

ALTER TABLE [dbo].[danh_gia] CHECK CONSTRAINT [FKpb5x40mo1o7n473vju2rc2luc]
GO

ALTER TABLE [dbo].[danh_gia]  WITH CHECK ADD  CONSTRAINT [FKs24q7va704gwc33kjomar4s4p] FOREIGN KEY([khach_hang_id])
REFERENCES [dbo].[khach_hang] ([id])
GO

ALTER TABLE [dbo].[danh_gia] CHECK CONSTRAINT [FKs24q7va704gwc33kjomar4s4p]
GO

ALTER TABLE [dbo].[don_hang]  WITH CHECK ADD  CONSTRAINT [FKeyporccvr9mq4k9j4fc5wpum5] FOREIGN KEY([khach_hang_id])
REFERENCES [dbo].[khach_hang] ([id])
GO

ALTER TABLE [dbo].[don_hang] CHECK CONSTRAINT [FKeyporccvr9mq4k9j4fc5wpum5]
GO

ALTER TABLE [dbo].[khach_hang]  WITH CHECK ADD  CONSTRAINT [FKjdwuhrypcrkgwync42t2fn2jw] FOREIGN KEY([tai_khoan_id])
REFERENCES [dbo].[tai_khoan] ([id])
GO

ALTER TABLE [dbo].[khach_hang] CHECK CONSTRAINT [FKjdwuhrypcrkgwync42t2fn2jw]
GO

ALTER TABLE [dbo].[khuyen_mai]  WITH CHECK ADD  CONSTRAINT [FK6aj25t60ladtj6rqmkwr8e05v] FOREIGN KEY([loai_san_pham_id])
REFERENCES [dbo].[loai_san_pham] ([id])
GO

ALTER TABLE [dbo].[khuyen_mai] CHECK CONSTRAINT [FK6aj25t60ladtj6rqmkwr8e05v]
GO

ALTER TABLE [dbo].[khuyen_mai]  WITH CHECK ADD  CONSTRAINT [FKnv6xxtsv60m21naooa4yogf3t] FOREIGN KEY([thuong_hieu_id])
REFERENCES [dbo].[thuong_hieu] ([id])
GO

ALTER TABLE [dbo].[khuyen_mai] CHECK CONSTRAINT [FKnv6xxtsv60m21naooa4yogf3t]
GO

ALTER TABLE [dbo].[phieu_nhap]  WITH CHECK ADD  CONSTRAINT [FK5lmupwyqjjdup4ki1g1dq2cct] FOREIGN KEY([nha_cung_cap_id])
REFERENCES [dbo].[nha_cung_cap] ([id])
GO

ALTER TABLE [dbo].[phieu_nhap] CHECK CONSTRAINT [FK5lmupwyqjjdup4ki1g1dq2cct]
GO

ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD  CONSTRAINT [FK8ixkt2s9hxy5bvdgk6g60ewax] FOREIGN KEY([loai_san_pham_id])
REFERENCES [dbo].[loai_san_pham] ([id])
GO

ALTER TABLE [dbo].[san_pham] CHECK CONSTRAINT [FK8ixkt2s9hxy5bvdgk6g60ewax]
GO

ALTER TABLE [dbo].[san_pham]  WITH CHECK ADD  CONSTRAINT [FKrum92qs4m7i0u7p7ub6bhbrr5] FOREIGN KEY([thuong_hieu_id])
REFERENCES [dbo].[thuong_hieu] ([id])
GO

ALTER TABLE [dbo].[san_pham] CHECK CONSTRAINT [FKrum92qs4m7i0u7p7ub6bhbrr5]
GO

ALTER TABLE [dbo].[san_pham_yeu_thich]  WITH CHECK ADD  CONSTRAINT [FKbol2nmdndw36avevs18gmfydf] FOREIGN KEY([khach_hang_id])
REFERENCES [dbo].[khach_hang] ([id])
GO

ALTER TABLE [dbo].[san_pham_yeu_thich] CHECK CONSTRAINT [FKbol2nmdndw36avevs18gmfydf]
GO

ALTER TABLE [dbo].[san_pham_yeu_thich]  WITH CHECK ADD  CONSTRAINT [FKqsxfxu1rxpucxpnvm1a293a5y] FOREIGN KEY([san_pham_id])
REFERENCES [dbo].[san_pham] ([id])
GO

ALTER TABLE [dbo].[san_pham_yeu_thich] CHECK CONSTRAINT [FKqsxfxu1rxpucxpnvm1a293a5y]
GO

CREATE TABLE [dbo].[phien_chat](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[cap_nhat_luc] [datetime2](6) NULL,
	[ma_khach] [nvarchar](64) NULL,
	[ngay_tao] [datetime2](6) NULL,
	[so_chua_doc_khach] [int] NOT NULL,
	[so_chua_doc_quan_tri] [int] NOT NULL,
	[so_dien_thoai] [nvarchar](20) NULL,
	[ten_khach] [nvarchar](80) NULL,
	[ten_nhan_vien] [nvarchar](80) NULL,
	[trang_thai] [nvarchar](20) NOT NULL,
	[khach_hang_id] [bigint] NULL,
PRIMARY KEY CLUSTERED ([id] ASC)
) ON [PRIMARY]
GO

CREATE UNIQUE NONCLUSTERED INDEX [UKag9mow9ubpm1qmvm91mg9vcn0] ON [dbo].[phien_chat] ([ma_khach] ASC)
WHERE ([ma_khach] IS NOT NULL)
GO

CREATE TABLE [dbo].[tin_nhan_chat](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[can_thong_tin] [bit] NOT NULL,
	[goiy] [nvarchar](500) NULL,
	[nguoi_gui] [nvarchar](20) NOT NULL,
	[noi_dung] [nvarchar](2000) NOT NULL,
	[ten_nguoi_gui] [nvarchar](80) NULL,
	[thoi_gian] [datetime2](6) NULL,
	[phien_chat_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED ([id] ASC)
) ON [PRIMARY]
GO

ALTER TABLE [dbo].[phien_chat]  WITH CHECK ADD  CONSTRAINT [FKmj7nkflrgf7mnett2tpqyr2wa] FOREIGN KEY([khach_hang_id])
REFERENCES [dbo].[khach_hang] ([id])
GO

ALTER TABLE [dbo].[tin_nhan_chat]  WITH CHECK ADD  CONSTRAINT [FKq24py5y3oju1lq4y92caowv9x] FOREIGN KEY([phien_chat_id])
REFERENCES [dbo].[phien_chat] ([id])
GO
