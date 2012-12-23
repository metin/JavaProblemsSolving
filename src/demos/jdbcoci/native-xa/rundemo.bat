@echo off
REM command file for compiling and executing files under
REM jdbc\demo\samples\jdbcoci\native-xa on Windows platform
REM
REM Usage: rundemo.bat oci | ocitns | <driver> <single demo> | help
REM
REM Before you run any demo programs, you should:
REM       1. set up your database and demo schema HR/HR. Please refer to
REM          Samples-Readme.txt, section Setting Up Schemas for details.
REM
REM Features demonstrated:
REM       Native XA.
REM


setlocal


REM
REM call to jdbc\demo\samples\setvars.bat to set up environment variables
REM
call ..\..\setvars.bat


set JDBC_URL=jdbc:oracle:oci:@

set DEMO_CLASSPATH=.;%ORACLE_HOME%\jlib\jndi.jar;%ORACLE_HOME%\jlib\jta.jar
set CLASSPATH=%ORACLE_HOME%\jdbc\lib\ojdbc5.jar;%DEMO_CLASSPATH%




if "%1" == "oci" set JDBC_URL=jdbc:oracle:oci:@ &goto next
if "%1" == "ocitns" set JDBC_URL="jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=%ORACLE_HOST%)(PORT=%ORACLE_PORT%))(CONNECT_DATA=(SERVICE_NAME=%ORACLE_SERVICE_NAME%)))" &goto next
if "%1" == "help" echo usage: rundemo.bat drivername or rundemo.bat drivername demoname &goto end
if "%1" == "" echo missing drivername or demoname &goto end
echo not supported driver: %1
goto end



:next
shift
if not "%1" == "" goto %1
goto all



:all
:XA6
REM Please use appropriate connect string that is defined in tnsnames.ora
REM Please see XA6.java for setup comments
%JAVAC% -classpath %CLASSPATH% -g XA6.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% XA6 %ORACLE_SERVICE_NAME%.oracle.com
goto end
:end
endlocal
