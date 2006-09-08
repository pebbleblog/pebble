# Utility to encode a password using a specific algorithm, using the username as the salt.
# Usage : [md5|sha|plaintext] username password
#
# Set PEBBLE_HOME to the location of your exploded web application root
export PEBBLE_HOME=web

export LOCAL_CLASSPATH=$PEBBLE_HOME/WEB-INF/lib/pebble-2.0.0.jar:$PEBBLE_HOME/WEB-INF/lib/commons-logging-1.0.4.jar:$PEBBLE_HOME/WEB-INF/lib/acegi-security-1.0.1.jar:$PEBBLE_HOME/WEB-INF/lib/commons-codec-1.3.jar:$PEBBLE_HOME/WEB-INF/lib/spring-dao-1.2.8.jar

java -classpath $LOCAL_CLASSPATH net.sourceforge.pebble.util.SecurityUtils $1 $2 $3
