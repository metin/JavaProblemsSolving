@echo off

REM setvars.bat
REM
REM This file is invoked by rundemo.bat at each jdbc\demo directory. 
REM It is to set up necessary environment variables
REM for jdbc demos. Please change them to match your
REM settings.
REM

set ORACLE_HOME=c:\oracle\ora10
set ORACLE_HOST=localhost
set ORACLE_SID=orcl
set ORACLE_SERVICE_NAME=orcl
set ORACLE_PORT=1521


REM
REM You could choose an appropriate directory to store
REM file1 and file2. The files and directory should be
REM presented prior to the execution of the demo
REM
set JDBC_TEMPDIR=%ORACLE_HOME%\tmpdir


REM
REM use the jdk that bundled with the database
REM as the default. jdk1.5 is bundled with Oracle11R1.
REM
set JDK_HOME=%ORACLE_HOME%\jdk
set JAVAC=%JDK_HOME%\bin\javac
set JAVA=%JDK_HOME%\bin\java
