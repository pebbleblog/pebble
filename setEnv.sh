export JAVA_HOME=/Library/Java/Home
export ANT_HOME=~/apache-ant

# WEBTEST_HOME is only for functional tests
export WEBTEST_HOME=~/webtest

# CLOVER_HOME is only used for code coverage
export CLOVER_HOME=~/clover-1.3.9

export PATH=$JAVA_HOME/bin:$PATH:$ANT_HOME/bin:$WEBTEST_HOME/bin
export CLASSPATH=$ANT_HOME/lib/ant-junit.jar:./lib/junit.jar:$CLOVER_HOME/lib/clover.jar
