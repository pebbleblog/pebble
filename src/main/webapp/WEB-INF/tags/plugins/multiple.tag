<%@ attribute name="plugins" type="java.util.Collection" required="true" %>
<%@ attribute name="enabled" type="java.util.Collection" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="orderable" type="java.lang.Boolean" %>
<%@ attribute name="properties" type="java.util.Properties" required="true" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="plugins" tagdir="/WEB-INF/tags/plugins" %>
<%@ taglib prefix="pebble" uri="http://pebble.sourceforge.net/pebble" %>

<input type="hidden" name="pluginType_${name}" value="true"/>
<ul id="pluginsBox_${name}" class="pluginsBox">
    <c:forEach items="${plugins}" var="plugin">
        <c:set var="id" value="pluginsBox_${name}_${plugin.pluginClass}"/>
        <li id="${id}">
            <div class="pluginHeader">
                <input id="${id}_checkbox" type="checkbox" value="${plugin.pluginClass}" name="${name}"
                  <c:if test="${pebble:contains(enabled, plugin.pluginClass)}">
                    checked="true"
                  </c:if> >
                <label for="${id}_checkbox">
                    ${plugin.name}
                </label>
                <c:if test="${orderable}">
                    <div class="moveHandle">&nbsp;</div>
                </c:if>

                <div onclick="Effect.toggle('${id}_pluginConfig', 'blind', {duration : 0.5});" class="pluginConfigLink">Configure</div>
            </div>
            <plugins:config divId="${id}_pluginConfig" plugin="${plugin}" properties="${properties}"/>
        </li>
    </c:forEach>
</ul>
<c:if test="${orderable}">
<script type="text/javascript">
    Sortable.create('pluginsBox_${name}', {handles: $$('#pluginsBox_${name} .moveHandle')})
</script>
</c:if>