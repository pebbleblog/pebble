<%@ attribute name="plugins" type="java.util.Collection" required="true" %>
<%@ attribute name="enabled" type="java.lang.String" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="properties" type="java.util.Properties" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="plugins" tagdir="/WEB-INF/tags/plugins" %>
<%@ taglib prefix="pebble" uri="http://pebble.sourceforge.net/pebble" %>

<div class="singlePluginBox">
<pebble:select name="${name}" items="${plugins}"
               selected="${enabled}" label="name" value="pluginClass"/>
<c:forEach items="${plugins}" var="plugin">
  <c:set var="id" value="pluginsBox_${name}_${plugin.pluginClass}"/>
  <plugins:config plugin="${plugin}" properties="${properties}" divId="${id}_pluginConfig"/>
</c:forEach>
</div>
<script type="text/javascript">
  initSingleSelect("${name}");
</script>