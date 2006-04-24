# Utility to encode a password using a specific algorithm, using the username as the salt.
# Usage : [md5|sha] username password
#
# Set PEBBLE_HOME to the location of your exploded web application root
export PEBBLE_HOME=web

export CLASSPATH=$PEBBLE_HOME/WEB-INF/lib/pebble-2.0.0-M2.jar:$PEBBLE_HOME/WEB-INF/lib/commons-logging.jar:$PEBBLE_HOME/WEB-INF/lib/acegi-security-1.0.0-RC1.jar:$PEBBLE_HOME/WEB-INF/lib/commons-codec-1.3.jar

java net.sourceforge.pebble.util.SecurityUtils $1 $2 $3
