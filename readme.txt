Prequisites
-----------
 - Java 5.0
 - A JSP 2.0/Servlet 2.4 compatible server such as Apache Tomcat 5.5.x 

Getting Started
---------------
The documentation set (not included in this early distribution) contains full details of how to install and configure Pebble, but if you're in a hurry, here's a summary.

 (1) Take the pebble.war file from the distribution and deploy it to your web container.
 (2) Restart your web container and point your browser to the deployed web application.

Configuration
-------------
There are two key configuration files that you'll be interested in, both of which reside in the WEB-INF directory inside the pebble.war file.

 - applicationContext-pebble.xml : contains configuration for Pebble itself, including the location of your blog data, your blog URL and a flag to indicate whether would like to run Pebble in multi-blog mode.

 - applicationContext-acegi-security.xml : contains the configuration for the Acegi security framework. By default, a single user (username/password) is defined using an in-memory security realm.

Help and Support
----------------
Help and support is available via the pebble-users[at]lists.sourceforge.net mailing list. Details on subscribing to this list can be found at http://lists.sourceforge.net/mailman/listinfo/pebble-user.
