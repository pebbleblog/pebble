rem Utility to encode a password using a specific algorithm, using the username as the salt.
rem Usage : [md5|sha|plaintext] username password

rem Set PEBBLE_HOME to the location of your exploded web application root
set PEBBLE_HOME=web

set CLASSPATH=%PEBBLE_HOME%\WEB-INF\lib\pebble-2.0.0.jar;%PEBBLE_HOME%\WEB-INF\lib\commons-logging.jar;%PEBBLE_HOME%\WEB-INF\lib\acegi-security-1.0.0-RC2.jar;%PEBBLE_HOME%\WEB-INF\lib\commons-codec-1.3.jar

java net.sourceforge.pebble.util.SecurityUtils %1 %2 %3
