@echo on
setlocal enabledelayedexpansion

set clientPath=D:\workplace\git\JavaModule\netty-client\src\protocol
set serverPath=D:\workplace\git\JavaModule\netty-server\src\protocol

rd /s /q protocol
for /f %%i in ('dir /b *.sc') do ( flatc %%i --java)
rd /s /q %clientPath%
rd /s /q %serverPath%

xcopy protocol %clientPath%\  /s /e /y
xcopy protocol %serverPath%\  /s /e /y

echo ------------------------------------------------------------
echo                           FINISH!
echo ------------------------------------------------------------
pause
exit
