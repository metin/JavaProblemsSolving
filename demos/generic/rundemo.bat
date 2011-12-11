@echo off

REM This is a command file for compiling and executing
REM demos under generic directory on Windows platform.

REM Usage:  rundemo.bat thin | oci | ocitns |
REM         <driver> <single_demo> | help

REM Before you run any demo programs, you should:
REM 1. set up your database and schema. Please refer to Samples-Readme.txt,
REM    section "Setting Up Schemas" for details.
REM 2. Modify this file to adjust settings to your machine. 
REM    search for MODIFY_HERE ... to process your modification

REM Features demonstrated:
REM 1. basic JDBC 1.22 features, such as Select, Insert, Define Column Type,
REM    embeded PL/SQL procedure, Prefetch, Batch execution, and Stream I/O.
REM 2. JDBC 2.0 features, such as Connection Cache, Statement Cache, DataSource,
REM    JNDI, PooledConnection, ResultSet, RowSet(OracleCachedRowSet), and XA.
REM 3. JDBC 3.0 features, such as transaction savepoint.
REM 4. connection wrapping feature.
REM 5. Oracle 9i features are demonstrated at sub directories -- JavaObject1,
REM    Inheritance, and NestedConnection
REM 6. This test needs interactive inputs

REM Notes on Oracle Connection Wrapper:
REM   oracle.jdbc.OracleConnectionWrapper is an implementation of a connection
REM   wrapper originally developed for testing. The compiled code is  provided in
REM   the distributed classesNN.zip/jar files and the source is in WrapperSource/.
REM   While some implementations might be able to use the class directly by
REM   extending it for their own use. It is anticipated that many will require an
REM   implementation in a class in their own hierarchy. Given that Java provides
REM   single inheritance of implementation it would be necessary to provide all
REM   the forwarding methods. This source is provided to assist in that effort.
REM   Please be aware that using the source will produce problems when the Oracle
REM   JDBC API is changed in the future whereas extending the Oracle supplied class
REM   should not. This is the JDK1.2 version of this class. See the comment block
REM   which has JDBC 3.0 interfaces for JDK1.4 and up.


setlocal

REM 
REM call to jdbc\demo\samples\setvars.bat to set up environment variables
REM
call ..\setvars.bat

REM You could set DEBUG to true for debugging
set DEBUG="false"

REM use oci as the default
set JDBC_URL=jdbc:oracle:oci:@
set JDBC_URL_2=jdbc:oracle:oci:@

set ORACLELOGFILE=%ORACLE_HOME%\jdbc\demo\OracleLog.properties
set DEMO_CLASSPATH=.;%ORACLE_HOME%\jlib\jndi.jar;%ORACLE_HOME%\jlib\jta.jar
set CLASSPATH15=.;%ORACLE_HOME%\jdbc\lib\ojdbc5.jar;%DEMO_CLASSPATH%;%ORACLE_HOME%\jlib\providerutil.jar;%ORACLE_HOME%\jlib\fscontext.jar;%ORACLE_HOME%\jlib\orai18n.jar
set CLASSPATH15g=.;%ORACLE_HOME%\jdbc\lib\ojdbc5_g.jar;%DEMO_CLASSPATH%;%ORACLE_HOME%\jlib\providerutil.jar;%ORACLE_HOME%\jlib\fscontext.jar;%ORACLE_HOME%\jlib\orai18n.jar


if "%1" == "thin" set JDBC_URL=jdbc:oracle:thin:@//%ORACLE_HOST%:%ORACLE_PORT%/%ORACLE_SERVICE_NAME% &goto next
if "%1" == "oci" set JDBC_URL=jdbc:oracle:oci:@ &goto next
if "%1" == "ocitns" set JDBC_URL="jdbc:oracle:oci:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcp)(HOST=%ORACLE_HOST%)(PORT=%ORACLE_PORT%))(CONNECT_DATA=(SERVICE_NAME=%ORACLE_SERVICE_NAME%)))" &goto next
if "%1" == "help" echo usage: rundemo.bat drivername or rundemo.bat drivername demoname&goto end
if "%1" == "" echo missing driver_name or demo name&goto end
echo not supported driver: %1
goto end



:next
shift
if not "%1" == "" goto %1

:all
del *.class
del *.out

:SelectExample
%JAVAC% -classpath %CLASSPATH15% -g  SelectExample.java 
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% SelectExample
if "%1" == "SelectExample"  goto end

:DefineColumnType
%JAVAC% -classpath %CLASSPATH15% -g  DefineColumnType.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% DefineColumnType
if "%1" == "DefineColumnType" goto end

:InsertExample
%JAVAC% -classpath %CLASSPATH15% -g  InsertExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% InsertExample
if "%1" == "InsertExample" goto end

:JdbcCheckup
%JAVAC% -classpath %CLASSPATH15% -g  JdbcCheckup.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% JdbcCheckup
if "%1" == "JdbcCheckup" goto end

:JdbcMTSample
%JAVAC% -classpath %CLASSPATH15% -g  JdbcMTSample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% JdbcMTSample
if "%1" == "JdbcMTSample" goto end

:PLSQL
%JAVAC% -classpath %CLASSPATH15% -g  PLSQL.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PLSQL
if "%1" == "PLSQL" goto end

:PLSQLExample
%JAVAC% -classpath %CLASSPATH15% -g  PLSQLExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PLSQLExample
if "%1" == "PLSQLExample" goto end

:RefCursorExample
%JAVAC% -classpath %CLASSPATH15% -g  RefCursorExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RefCursorExample
if "%1" == "RefCursorExample" goto end

:RowPrefetch_connection
%JAVAC% -classpath %CLASSPATH15% -g  RowPrefetch_connection.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowPrefetch_connection
if "%1" == "RowPrefetch_connection" goto end

:RowPrefetch_statement
%JAVAC% -classpath %CLASSPATH15% -g  RowPrefetch_statement.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowPrefetch_statement
if "%1" == "RowPrefetch_statement" goto end

:SendBatch
%JAVAC% -classpath %CLASSPATH15% -g  SendBatch.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% SendBatch
if "%1" == "SendBatch" goto end

:SetExecuteBatch
%JAVAC% -classpath %CLASSPATH15% -g  SetExecuteBatch.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% SetExecuteBatch
if "%1" == "SetExecuteBatch" goto end

:SetExecuteBatch2
%JAVAC% -classpath %CLASSPATH15% -g  SetExecuteBatch2.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% SetExecuteBatch2
if "%1" == "SetExecuteBatch2" goto end

:StreamExample
%JAVAC% -classpath %CLASSPATH15% -g  StreamExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% StreamExample
if "%1" == "StreamExample" goto end

:DataSource
%JAVAC% -classpath %CLASSPATH15% -g  DataSource.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% DataSource
if "%1" == "DataSource" goto end

:DataSourceJNDI
%JAVAC% -classpath %CLASSPATH15% -g  DataSourceJNDI.java
REM MODIFY_HERE
REM The sub-directories JNDI must be present in this directory,
REM or you could replace it with an appropriate path
del .\JNDI\.bindings
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% DataSourceJNDI .\JNDI
if "%1" == "DataSourceJNDI" goto end

:PooledConnection1
%JAVAC% -classpath %CLASSPATH15% -g  PooledConnection1.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PooledConnection1
if "%1" == "PooledConnection1" goto end

:PooledConnection2
%JAVAC% -classpath %CLASSPATH15% -g  PooledConnection2.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PooledConnection2
if "%1" == "PooledConnection2" goto end

:BatchUpdates
%JAVAC% -classpath %CLASSPATH15% -g  BatchUpdates.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% BatchUpdates
if "%1" == "BatchUpdates" goto end

:XA1
%JAVAC% -classpath %CLASSPATH15% -g  XA1.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% XA1
if "%1" == "XA1" goto end

:XA2
%JAVAC% -classpath %CLASSPATH15% -g  XA2.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% XA2
if "%1" == "XA2" goto end

:XA3
%JAVAC% -classpath %CLASSPATH15% -g  XA3.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% XA3
if "%1" == "XA3" goto end

:XA4
%JAVAC% -classpath %CLASSPATH15% -g  XA4.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% -DJDBC_URL_2=%JDBC_URL_2% XA4 %ORACLE_HOST% %ORACLE_PORT% %ORACLE_SERVICE_NAME%.oracle.com
if "%1" == "XA4" goto end

:XA5
%JAVAC% -classpath %CLASSPATH15% -g  XA5.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% XA5
if "%1" == "XA5" goto end

:ResultSet1
%JAVAC% -classpath %CLASSPATH15% -g  ResultSet1.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ResultSet1
if "%1" == "ResultSet1" goto end

:ResultSet2
%JAVAC% -classpath %CLASSPATH15% -g  ResultSet2.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ResultSet2
if "%1" == "ResultSet2" goto end

:ResultSet3
%JAVAC% -classpath %CLASSPATH15% -g  ResultSet3.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ResultSet3
if "%1" == "ResultSet3" goto end

:ResultSet4
%JAVAC% -classpath %CLASSPATH15% -g  ResultSet4.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ResultSet4
if "%1" == "ResultSet4" goto end

:ResultSet5
%JAVAC% -classpath %CLASSPATH15% -g  ResultSet5.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ResultSet5
if "%1" == "ResultSet5" goto end

:ResultSet6
%JAVAC% -classpath %CLASSPATH15% -g  ResultSet6.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ResultSet6
if "%1" == "ResultSet6" goto end

:StmtCache1
%JAVAC% -classpath %CLASSPATH15% -g  StmtCache1.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% StmtCache1
if "%1" == "StmtCache1" goto end

:StmtCache2
%JAVAC% -classpath %CLASSPATH15% -g  StmtCache2.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% StmtCache2
if "%1" == "StmtCache1" goto end

:RowId
%JAVAC% -classpath %CLASSPATH15% -g  RowId.java
sqlplus hr/hr < books.sql
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowId
if "%1" == "RowId" goto end

REM The RowSet demos are not compiled by default. To compile these,
REM you could either use JDK 5.0, or install the JSR-114 RowSet
REM JWSDP Co-Bundle and use JDK 1.4.x:
REM http://java.sun.com/products/jdbc/download.html#rowsetcobundle1_0

REM :RowSet01
REM %JAVAC% -classpath %CLASSPATH15% -g  RowSet01.java
REM sqlplus hr/hr <  rowset.sql
REM %JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowSet01
REM if "%1" == "RowSet01" goto end

REM :RowSet02
REM %JAVAC% -classpath %CLASSPATH15% -g  RowSet02.java
REM %JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowSet02
REM if "%1" == "RowSet02" goto end

REM :RowSet03
REM %JAVAC% -classpath %CLASSPATH15% -g  RowSet03.java
REM %JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowSet03
REM if "%1" == "RowSet03" goto end

REM :RowSet04
REM %JAVAC% -classpath %CLASSPATH15% -g  RowSet04.java
REM %JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% RowSet04
REM if "%1" == "RowSet04" goto end

REM :WebRowset01
REM %JAVAC% -classpath %CLASSPATH15% -g WebRowset01
REM %JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% WebRowset01
REM if "%1" == "WebRowset01" goto en

:ArrayExample
%JAVAC% -classpath %CLASSPATH15% -g  ArrayExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ArrayExample
if "%1" == "ArrayExample" goto end

:ORADataExample
%JAVAC% -classpath %CLASSPATH15% -g  ORADataExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ORADataExample
if "%1" == "ORADataExample" goto end

:FileExample
%JAVAC% -classpath %CLASSPATH15% -g  FileExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% FileExample %JDBC_TEMPDIR%
if "%1" == "FileExample" goto end

:LobExample
%JAVAC% -classpath %CLASSPATH15% -g  LobExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% LobExample
if "%1" == "LobExample" goto end

:OpenCloseLob
%JAVAC% -classpath %CLASSPATH15% -g  OpenCloseLob.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% OpenCloseLob
if "%1" == "LobExample" goto end

:PLSQL_FileExample
%JAVAC% -classpath %CLASSPATH15% -g  PLSQL_FileExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PLSQL_FileExample %JDBC_TEMPDIR%
if "%1" == "PLSQL_FileExample" goto end

:PLSQL_LobExample
%JAVAC% -classpath %CLASSPATH15% -g  PLSQL_LobExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PLSQL_LobExample
if "%1" == "PLSQL_LobExample" goto end

:PersonObject
%JAVAC% -classpath %CLASSPATH15% -g  PersonObject.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PersonObject
if "%1" == "PersonObject" goto end

:PersonRef
%JAVAC% -classpath %CLASSPATH15% -g  PersonRef.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% PersonRef
if "%1" == "PersonRef" goto end

:RefClient
%JAVAC% -classpath %CLASSPATH15% -g  RefClient.java
sqlplus hr/hr < RefClient.sql 
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% -DJDBC_URL_2=%JDBC_URL_2% RefClient
if "%1" == "RefClient" goto end

:SQLDataExample
%JAVAC% -classpath %CLASSPATH15% -g  SQLDataExample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% SQLDataExample
if "%1" == "SQLDataExample" goto end

:TemporaryLob
%JAVAC% -classpath %CLASSPATH15% -g  TemporaryLob.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% TemporaryLob
if "%1" == "TemporaryLob" goto end

:TrimLob
%JAVAC% -classpath %CLASSPATH15% -g  TrimLob.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% TrimLob
if "%1" == "TrimLob" goto end

:ConnectionWrapperSample
%JAVAC% -classpath %CLASSPATH15% -g  ConnectionWrapperSample.java
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ConnectionWrapperSample
if "%1" == "ConnectionWrapperSample" goto end

:Svpt1
%JAVAC% -classpath %CLASSPATH15g% -g Svpt1.java
if %DEBUG% == "true" goto Svpt1_dbg
if %DEBUG% == "false" goto Svpt1_opt

:Svpt1_dbg
%JAVA% -classpath %CLASSPATH15g% \
 -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
 -Djava.util.logging.config.file=%ORACLELOGFILE% Svpt1 
if "%1" == "Svpt1" goto end

:Svpt1_opt
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% Svpt1
if "%1" == "Svpt1" goto end

:ImplicitCache01
%JAVAC% -classpath %CLASSPATH15g% -g ImplicitCache01.java
if %DEBUG% == "true" goto ImplicitCache01_dbg
if %DEBUG% == "false" goto ImplicitCache01_opt

:ImplicitCache01_dbg
%JAVA% -classpath %CLASSPATH15g% \
 -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
 -Djava.util.logging.config.file=%ORACLELOGFILE% ImplicitCache01
if "%1" == "ImplicitCache01" goto end

:ImplicitCache01_opt
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL%  ImplicitCache01
if "%1" == "ImplicitCache01" goto end

:ImplicitCache02
%JAVAC% -classpath %CLASSPATH15% -g ImplicitCache02.java
if %DEBUG% == "true" goto ImplicitCache02_dbg
if %DEBUG% == "false" goto ImplicitCache02_opt
if "%1" == "ImplicitCache02" goto end

:ImplicitCache02_dbg
%JAVA% -classpath %CLASSPATH15g% \
 -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
 -Djava.util.logging.config.file=%ORACLELOGFILE% ImplicitCache02
if "%1" == "ImplicitCache02" goto end

"ImplicitCache02_opt
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ImplicitCache02
if "%1" == "ImplicitCache02" goto end

:ImplicitCache03
%JAVAC% -classpath %CLASSPATH15% -g ImplicitCache03.java
if %DEBUG% == "true" goto ImplicitCache03_dbg
if %DEBUG% == "false" goto ImplicitCache03_opt

:ImplicitCache03_dbg
%JAVA% -classpath %CLASSPATH15g% \
 -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
 -Djava.util.logging.config.file=%ORACLELOGFILE% ImplicitCache03
if "%1" == "ImplicitCache03" goto end

:ImplicitCache03_opt
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ImplicitCache03
if "%1" == "ImplicitCache03" goto end

:ImplicitCache04
%JAVAC% -classpath %CLASSPATH15% -g ImplicitCache04.java
if %DEBUG% == "true" goto ImplicitCache04_dbg
if %DEBUG% == "false" goto ImplicitCache04_opt

:ImplicitCache04_dbg
%JAVA% -classpath %CLASSPATH15g% \
 -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
 -Djava.util.logging.config.file=%ORACLELOGFILE% ImplicitCache04
if "%1" == "ImplicitCache04" goto end

:ImplicitCache04_opt
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ImplicitCache04
if "%1" == "ImplicitCache04" goto end

:ImplicitCache05
%JAVAC% -classpath %CLASSPATH15% -g ImplicitCache05.java
if %DEBUG% == "true" goto ImplicitCache05_dbg
if %DEBUG% == "false" goto ImplicitCache05_opt

:ImplicitCache05_dbg
%JAVA% -classpath %CLASSPATH15g% \
 -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
 -Djava.util.logging.config.file=%ORACLELOGFILE% ImplicitCache05
if "%1" == "ImplicitCache05" goto end

:ImplicitCache05_opt
%JAVA% -classpath %CLASSPATH15% -DJDBC_URL=%JDBC_URL% ImplicitCache05
if "%1" == "ImplicitCache05" goto end

:ProxySession01
%JAVAC% -classpath %CLASSPATH15% -g ProxySession01.java
sqlplus hr/hr < ProxySession01.sql
%JAVA% -classpath %CLASSPATH15% ProxySession01
if "%1" == "ProxySession01" goto end

REM To enable FastConnectionFailover demos,
REM you need to set a RAC database and setup ONS
REM:FastConnectionFailover01
REM%JAVAC% -classpath %CLASSPATH15% -g FastConnectionFailover01.java
REMif %DEBUG% == "true" goto FastConnectionFailover01_dbg
REMif %DEBUG% == "false" goto FastConnectionFailover01_opt

REM:FastConnectionFailover01_dbg
REM%JAVA% -classpath %CLASSPATH15g% \
REM -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
REM -Djava.util.logging.config.file=%ORACLELOGFILE% \
REM -Doracle.ons.oraclehome=$(ONS_HOME) FastConnectionFailover01
REMif "%1" == "FastConnectionFailover01" goto end

REM:FastConnectionFailover01_opt
REM%JAVA% -classpath %CLASSPATH15% \
REM -DJDBC_URL=%JDBC_URL% \
REM -Doracle.ons.oraclehome=$(ONS_HOME) FastConnectionFailover01
REMif "%1" == "FastConnectionFailover01" goto end
 
REM:FastConnectionFailover02
REM%JAVAC% -classpath %CLASSPATH15% -g FastConnectionFailover02.java
REMif %DEBUG% == "true" goto FastConnectionFailover02_dbg
REMif %DEBUG% == "false" goto FastConnectionFailover02_opt

REM:FastConnectionFailover02_dbg
REM%JAVA% -classpath %CLASSPATH15g% \
REM -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
REM -Djava.util.logging.config.file=%ORACLELOGFILE% \
REM -Doracle.ons.oraclehome=$(ONS_HOME) FastConnectionFailover02
REMif "%1" == "FastConnectionFailover02" goto end

REM:FastConnectionFailover02_opt
REM%JAVA% -classpath %CLASSPATH15% \
REM -DJDBC_URL=%JDBC_URL% \
REM -Doracle.ons.oraclehome=$(ONS_HOME) FastConnectionFailover02
REMif "%1" == "FastConnectionFailover02" goto end

REM:FastConnectionFailover03
REM%JAVAC% -classpath %CLASSPATH15% -g FastConnectionFailover03.java
REMif %DEBUG% == "true" goto FastConnectionFailover03_dbg
REMif %DEBUG% == "false" goto FastConnectionFailover03_opt

REM:FastConnectionFailover03_dbg
REM%JAVA% -classpath %CLASSPATH15g% \
REM -DJDBC_URL=%JDBC_URL% -Doracle.jdbc.Trace=true \
REM -Djava.util.logging.config.file=%ORACLELOGFILE% \
REM -Doracle.ons.oraclehome=$(ONS_HOME) FastConnectionFailover03
REMif "%1" == "FastConnectionFailover03" goto end

REM:FastConnectionFailover03_opt
REM%JAVA% -classpath %CLASSPATH15% \
REM -DJDBC_URL=%JDBC_URL% \
REM -Doracle.ons.oraclehome=$(ONS_HOME) FastConnectionFailover03
REMif "%1" == "FastConnectionFailover03" goto end

:end
endlocal
