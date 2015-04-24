
REM Add MS build to path to enable native windows compilation
Set PATH=C:\Windows\Microsoft.NET\Framework\v4.0.30319;%PATH%

REM Add generated fax4j.dll and fax4j.exe to path to enable unit testing
Set PATH=.\target\native\windows\lib;%PATH%

REM fax4j JUnit flag (only for build)
Set FAX4J_JUNIT_WINDOWS=true

call mvn clean install site
