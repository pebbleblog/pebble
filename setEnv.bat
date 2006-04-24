set JAVA_HOME=c:\j2sdk1.4.1_03
set ANT_HOME=c:\apache-ant-1.5.3
set CLASSPATH=%ANT_HOME%\lib\optional.jar;.\lib\junit.jar;%CLOVER_HOME%\lib\clover.jar
set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%PATH%

rem CLOVER_HOME is only used for code coverage
set CLOVER_HOME=c:\clover-1.2.4

rem MIDP_HOME is only used to build Pebble moblog and if this directory doesn't exist, moblog simply isn't built
set MIDP_HOME=c:\midp-1.0

