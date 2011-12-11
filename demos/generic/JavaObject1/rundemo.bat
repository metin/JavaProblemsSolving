@echo off
REM command file for compiling and executing files under
REM jdbc\demo\samples\generic\JavaObject1 on Windows platform
REM
REM Usage: rundemo.bat thin | oci | ocitns | <driver> <single demo> | help
REM
REM Before you run any demo programs, you should:
REM       1. set up your database and make sure your database started with
REM          COMPATIBLE parameter with the value of 9.0.0.0.0 or greater
REM          in your database init files (e.g., tkinit.ora)
REM       2. set up demo schema. Please refer to Samples-Readme.txt, section
REM          Setting Up Schemas for the details.
REM
REM Features demonstrated:
REM       Oracle 9i SQLJ Object Types feature -- using the SQLData representation.
REM


setlocal

REM
REM call to jdbc\demo\samples\setvars.bat to set environment variables
REM
call ..\..\setvars.bat


set JDBC_URL=jdbc:oracle:oci:@

set DEMO_CLASSPATH=.;%ORACLE_HOME%\jlib\jndi.jar;%ORACLE_HOME%\jlib\jta.jar
set CLASSPATH=%DEMO_CLASSPATH%;%ORACLE_HOME%\jdbc\lib\ojdbc5.jar;%ORACLE_HOME%\jdbc\lib\orai18n.jar



if "%1" == "thin" set JDBC_URL=jdbc:oracle:thin:@//%ORACLE_HOST%:%ORACLE_PORT%/%ORACLE_SERVICE_NAME% &goto next
if "%1" == "oci" set JDBC_URL=jdbc:oracle:oci:@ &goto next
if "%1" == "ocitns" set JDBC_URL="jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=%ORACLE_HOST%)(PORT=%ORACLE_PORT%))(CONNECT_DATA=(SERVICE_NAME=%ORACLE_SERVICE_NAME%)))" &goto next
if "%1" == "help" echo usage: rundemo.bat drivername or rundemo.bat drivername demoname &goto end
if "%1" == "" echo missing driver_name or demo_name &goto end
echo not supported driver: %1
goto end



:next
shift
if not "%1" == "" goto %1
goto all



:all
:JavaObject1
del *.class
%JAVAC% -classpath %CLASSPATH% -g JavaObject1.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% JavaObject1
goto end
:end
endlocal
