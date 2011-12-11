@echo off
REM command file for compiling and executing following files
REM jdbc\demo\samples\jdbcoci\*.java on Windows platform.
REM
REM Usage: rundemo.bat oci | ocitns | <driver> <single demo> | help
REM
REM Before you run any demo programs, you should:
REM       1. set up your database and demo schema. Please refer to
REM          Samples-Readme.txt, section Setting Up Schemas for details.
REM       2. Modify this file to your settings.
REM          search for MODIFY_HERE ... to process your modification.
REM
REM Features demonstrated:
REM       OCI Connection Pooling, Ntier Authentication, PL/SQL index-by tables,
REM       OCI application failover with callbacks, and JNDI.
REM       native XA feature is demonstrated in a sub directory native-xa
REM


setlocal


REM
REM call to jdbc\demo\samples\setvars.bat to set up environment variables
REM
call ..\setvars.bat

set JDBC_URL=jdbc:oracle:oci:@

set DEMO_CLASSPATH=.;%ORACLE_HOME%\jlib\jndi.jar;%ORACLE_HOME%\jlib\jta.jar
set CLASSPATH=%DEMO_CLASSPATH%;%ORACLE_HOME%\jdbc\lib\ojdbc5.jar;%ORACLE_HOME%\jdbc\lib\orai18n.jar;%ORACLE_HOME%\jlib\providerutil.jar;%ORACLE_HOME%\jlib\fscontext.jar



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
del *.class

:OCIConnectionPool
%JAVAC% -classpath %CLASSPATH%  -g OCIConnectionPool.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% OCIConnectionPool
if "%1" == "OCIConnectionPool" goto end

:NtierAuth
%JAVAC% -classpath %CLASSPATH%  -g NtierAuth.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% NtierAuth
if "%1" == "NtierAuth" goto end

:OCIFailOver
%JAVAC% -classpath %CLASSPATH%  -g OCIFailOver.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% OCIFailOver
if "%1" == "OCIFailOver" goto end

:PLSQLIndexTab
%JAVAC% -classpath %CLASSPATH%  -g PLSQLIndexTab.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% PLSQLIndexTab
if "%1" == "PLSQLIndexTab" goto end

REM MODIFY_HERE
REM The sub-directorie JNDI must be present in this directory.
REM Or you could replace it with an appropriate path.
:OCIConnPoolJNDI
del .\JNDI\.bindings
%JAVAC% -classpath %CLASSPATH%  -g OCIConnPoolJNDI.java
%JAVA% -classpath %CLASSPATH% -DJDBC_URL=%JDBC_URL% OCIConnPoolJNDI .\JNDI
if "%1" == "OCIConnPoolJNDI" goto end
:end
endlocal
