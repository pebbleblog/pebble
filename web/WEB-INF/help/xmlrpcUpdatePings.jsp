<div class="contentItem">
  <h1>XML-RPC Update Pings</h1>
  <h2>&nbsp;</h2>

  <div class="contentItemBody">
    <p>
      Many blog aggregators support the notion of <a href="http://www.xmlrpc.com/weblogsCom">XML-RPC update notification pings</a>
      that a blog can send when it has been updated. To enable this,
      add the following class name to the list of blog entry listeners on the <a href="viewPlugins.secureaction#blogEntryListeners">Plugins</a> page.
    </p>

    <pre class="codeSample">net.sourceforge.pebble.event.blogentry.XmlRpcNotificationListener</pre>

    <p>
      Next, add a property called <code>XmlRpcNotificationListener.urlList</code> to the properties specified on the <a href="viewPlugins.secureaction#properties">Plugins</a> page.
      The value of this property is a comma separated list of URLs that should be pinged when you add/edit a blog entry. Here are some example URLs that listen for XML-RPC update pings.
    </p>

    <ul>
      <li>http://rpc.weblogs.com/RPC2</li>
      <li>http://javablogs.com/xmlrpc</li>
      <li>http://api.my.yahoo.com/RPC2</li>
      <li>http://rpc.technorati.com/rpc/ping</li>
      <li>http://ping.blo.gs</li>
      <li>http://rcs.datashed.net/RPC2</li>
    </ul>
  </div>
</div>