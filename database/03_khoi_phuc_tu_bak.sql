DECLARE @tep_bak NVARCHAR(400) = N'C:\DoAnJava\da_java_diengiadung\database\dien_gia_dung.bak';
DECLARE @ten_csdl SYSNAME = N'dien_gia_dung';

DECLARE @thu_muc NVARCHAR(400) = CAST(SERVERPROPERTY('InstanceDefaultDataPath') AS NVARCHAR(400));
DECLARE @thu_muc_log NVARCHAR(400) = CAST(SERVERPROPERTY('InstanceDefaultLogPath') AS NVARCHAR(400));
DECLARE @lenh NVARCHAR(MAX);

IF NOT EXISTS (SELECT 1 FROM sys.server_principals WHERE name = N'bepne')
    CREATE LOGIN bepne WITH PASSWORD = N'BepNe@2026', CHECK_POLICY = OFF;
ALTER SERVER ROLE dbcreator ADD MEMBER bepne;

IF DB_ID(@ten_csdl) IS NOT NULL
BEGIN
    SET @lenh = N'ALTER DATABASE ' + QUOTENAME(@ten_csdl) + N' SET SINGLE_USER WITH ROLLBACK IMMEDIATE';
    EXEC (@lenh);
END

SET @lenh = N'RESTORE DATABASE ' + QUOTENAME(@ten_csdl) + N' FROM DISK = @bak WITH REPLACE, '
    + N'MOVE N''dien_gia_dung'' TO @mdf, MOVE N''dien_gia_dung_log'' TO @ldf';
DECLARE @mdf NVARCHAR(400) = @thu_muc + @ten_csdl + N'.mdf';
DECLARE @ldf NVARCHAR(400) = @thu_muc_log + @ten_csdl + N'_log.ldf';
EXEC sp_executesql @lenh, N'@bak NVARCHAR(400), @mdf NVARCHAR(400), @ldf NVARCHAR(400)', @tep_bak, @mdf, @ldf;

SET @lenh = N'ALTER DATABASE ' + QUOTENAME(@ten_csdl) + N' SET MULTI_USER;'
    + N' USE ' + QUOTENAME(@ten_csdl) + N';'
    + N' IF EXISTS (SELECT 1 FROM sys.database_principals WHERE name = N''bepne'')'
    + N' ALTER USER bepne WITH LOGIN = bepne'
    + N' ELSE BEGIN CREATE USER bepne FOR LOGIN bepne; ALTER ROLE db_owner ADD MEMBER bepne; END';
EXEC (@lenh);

SET @lenh = N'SELECT (SELECT COUNT(*) FROM ' + QUOTENAME(@ten_csdl) + N'.dbo.san_pham) AS so_san_pham, '
    + N'(SELECT COUNT(*) FROM ' + QUOTENAME(@ten_csdl) + N'.dbo.don_hang) AS so_don_hang';
EXEC (@lenh);
