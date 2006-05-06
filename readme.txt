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
For most installations, there is a single configuration file you'll need to edit, which resides in the WEB-INF directory inside the pebble.war file.

 - applicationContext-pebble.xml : contains configuration for Pebble itself, including the location of your blog data and your blog URL.

Security
--------
All security details are stored in property files inside the ${pebbleContext.dataDirectory}/authentication directory, with one file per user. The name of the file is ${username}.properties and inside there are name=value pairs for the following characteristics.

 - password : the password, either in plaintext or hashed (e.g. with MD5, SHA, etc).
 - emailAddress : the e-mail address of the user (optional).
 - roles : a comma separated list of roles (i.e. ROLE_BLOG_OWNER, ROLE_BLOG_CONTRIBUTOR, ROLE_PEBBLE_ADMIN).

A single user (username/password with all roles) is created when Pebble is started up for the first time. To add additional users, simply add new files to the above directory. 

Help and Support
----------------
Help and support is available via the pebble-users[at]lists.sourceforge.net mailing list. Details on subscribing to this list can be found at http://lists.sourceforge.net/mailman/listinfo/pebble-user.
