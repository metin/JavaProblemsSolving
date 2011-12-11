#
# Makefile for compiling and executing files under
# demo/samples/jdbcoci/native-xa/
# Please use rundemo.bat on NT, win64
#
# Usage: make <all> | <oci> | <ocitns> | <single demo>
#
# Before you run any demo programs, you should:
#       1. set up your database and demo schema HR/HR. Please refer to
#          Samples-Readme.txt, section Setting Up Schemas for details.
#       2. Modify this makefile to use appropriate host/port/SID/service_name.
#          search for MODIFY_HERE ... to process your modification.
#
# Features demonstrated:
#       Native XA.
#

 

JDBC_URL=jdbc:oracle:oci:@

DEMO_CLASSPATH=.:${ORACLE_HOME}/jlib/jndi.jar:${ORACLE_HOME}/jlib/jta.jar
CLASSPATH=${ORACLE_HOME}/jdbc/lib/ojdbc5.jar:${DEMO_CLASSPATH}

# MODIFY_HERE: Please use a correct path on your machine
JDK15_HOME=/usr/local/packages/jdk15

# MODIFY_HERE: If the platform is 64 bit & java takes -d64 flag to run in 64 
# bit mode, set the below value to TRUE
USE_64FLAG=FALSE

# MODIFY_HERE: if LIBPATH is used instead of LD_LIBRARY_PATH then replace
# LD_LIBRARY_PATH by LIBPATH below
# For 64 bit platforms with 32 bit libraries modify lib to lib32 to use
# 32 bit library.
JAVA_LIBRARY_PATH=${ORACLE_HOME}/lib:${LD_LIBRARY_PATH}

# choose JDK1.4 and ojdbc5.jar as the default settings and
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
	$(JAVAC) -classpath $(CLASSPATH) -g XA6.java

%.class : %.java
	$(JAVAC) -classpath $(CLASSPATH) -g $<

run: XA6


# MODIFY_HERE
# Please use appropriate connect string that is defined in tnsnames.ora
# Please see XA6.java for setup comments
XA6: XA6.class
	$(JAVA) -classpath $(CLASSPATH) -DJDBC_URL=$(JDBC_URL) XA6 orcl.oracle.com

clobber:
	rm -f *.class
