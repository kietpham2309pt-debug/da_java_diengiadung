@echo off
for /L %%i in (1,1,3) do start "May kho %%i" cmd /k "%~dp0chay-client.bat" %*
