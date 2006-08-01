<div class="contentItem">
  <h1>Developer Notes</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <h3>Getting the source</h3>
    <p>
      Pebble is an open source project and the source code is freely available from the SourceForge Subversion servers. Assuming
      that you have the Subversion command line tools installed, you
      can get a copy of the current SVN trunk by using the following command.
    </p>

    <pre class="codeSample">svn co https://svn.sourceforge.net/svnroot/pebble/trunk pebble</pre>

    <p>
      To checkout a particular version of the codebase, substitute <code>trunk</code> for a specific tag such as <code>tags/v2.0.0-M2</code>.
      All Pebble releases have been tagged using a tag such as <code>v2.0.0</code>.
    </p>

    <h3>Building</h3>
    <p>
      To build Pebble, open up the <code>setEnv.bat</code> (Windows) or <code>setEnv.sh</code> (UNIX, Linux or Mac OS X) script
      and alter the <code>JAVA_HOME</code> and <code>ANT_HOME</code> environment variables as appropriate for your environment. The
      following versions of software are recommended for building Pebble.
    </p>

    <ul>
    <li>Java 5.0</li>
    <li>Apache Ant 1.6 or above</li>
    </ul>

    <p>
      After running the <code>setEnv</code> script, to build Pebble, type <code>ant</code> at the command line. 
      Other useful build targets are <code>test</code> (runs all JUnit tests) and <code>dist</code> (produces
      a binary distribution, including the <code>pebble.war</code> file).
    </p>
  </div>
</div>