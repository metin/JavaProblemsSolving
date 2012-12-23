#
# Makefile for compiling and executing following files 
# demo/samples/jdbcoci/*.java
# Please use rundemo.bat on NT, win64
#
# Usage: make <all> | <oci> | <ocitns> | <single demo>
#
# Before you run any demo programs, you should:
#       1. set up your database and demo schema. Please refer to
#          Samples-Readme.txt, section Setting Up Schemas for details.
#       2. Modify this makefile to use appropriate host/port/SID/service_name.
#          search for MODIFY_HERE ... to process your modification.
#
# Features demonstrated:
#       OCI Connection Pooling, Ntier Authentication, PL/SQL index-by tables,
#       OCI application failover with callbacks, and JNDI. 
#       native XA feature is demonstrated in a sub directory native-xa
#

 

JDBC_URL=jdbc:oracle:oci:@
JDBC_URL_2=jdbc:oracle:oci:@

DEMO_CLASSPATH=.:${ORACLE_HOME}/jlib/jndi.jar:${ORACLE_HOME}/jlib/jta.jar
CLASSPATH=.:${ORACLE_HOME}/jdbc/lib/ojdbc5.jar:${DEMO_CLASSPATH}:${ORACLE_HOME}/jlib/providerutil.jar:${ORACLE_HOME}/jlib/fscontext.jar

# MODIFY_HERE: Please use a correct path on your machine
JDK15_HOME=${ORACLE_HOME}/jdk15

# MODIFY_HERE: On 64 bit platforms, if java takes -d64 flag to run 
# in 64 bit mode, set the below value to TRUE.
USE_64FLAG=FALSE

# MODIFY_HERE: If LIBPATH is used instead of LD_LIBRARY_PATH then replace
# LD_LIBRARY_PATH by LIBPATH below
# For 64 bit platforms with 32 bit libraries modify lib to lib32 to use
# 32 bit library.
JAVA_LIBRARY_PATH=${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}

# choose JDK1.5 and ojdbc5.jar as the default settings and
# if USE_64FLAG is set to TRUE then use -d64
ifeq ("${USE_64FLAG}","TRUE")
  JAVAC=${JDK15_HOME}/bin/javac
  JAVA=${JDK15_HOME}/bin/java -d64 -Djava.library.path=${JAVA_LIBRARY_PATH}
else
  JAVAC=${JDK15_HOME}/bin/javac
  JAVA=${JDK15_HOME}/bin/java -Djava.library.path=${JAVA_LIBRARY_PATH}
endif

all: clobber compile run

oci: 
	make all JDBC_URL=jdbc:oracle:oci:@

# MODIFY_HERE
# Please use appropriate <host> <port> and <service_name>
ocitns:
	make all JDBC_URL="jdbc:oracle:oci:@\(DESCRIPTION=\(ADDRESS=\(PROTOCOL=tcp\)\(HOST=localhost\)\(PORT=1521\)\)\(CONNECT_DATA=\(SERVICE_NAME=orcl.oracle.com\)\)\)"


compile:
	$(JAVAC) -classpath $(CLASSPATH)  -g `find . -name \*.java -print`

%.class : %.java
	$(JAVAC) -classpath $(CLASSPATH)  -g  $<

run: OCIConnectionPool NtierAuth OCIFailOver PLSQLIndexTab OCIConnPoolJNDI



OCIConnectionPool: OCIConnectionPool.class
	$(JAVA) -classpath $(CLASSPATH) -DJDBC_URL=$(JDBC_URL) OCIConnectionPool

NtierAuth: NtierAuth.class
	$(JAVA) -classpath $(CLASSPATH) -DJDBC_URL=$(JDBC_URL) NtierAuth

OCIFailOver: OCIFailOver.class
	$(JAVA) -classpath $(CLASSPATH) -DJDBC_URL=$(JDBC_URL) OCIFailOver

PLSQLIndexTab: PLSQLIndexTab.class
	$(JAVA) -classpath $(CLASSPATH) -DJDBC_URL=$(JDBC_URL) PLSQLIndexTab

# MODIFY_HERE
# The sub-directories JNDI/ and JNDI/jdbc must be present in this directory.
# Or you could replace it with an appropriate path.
OCIConnPoolJNDI: OCIConnPoolJNDI.class
	-rm -f ./JNDI/jdbc/.bindings
	$(JAVA) -classpath $(CLASSPATH) -DJDBC_URL=$(JDBC_URL) OCIConnPoolJNDI ./JNDI



clobber:
	rm -f *.class
	rm -f *.out
