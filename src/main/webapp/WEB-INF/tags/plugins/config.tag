<%@ attribute name="plugin" type="net.sourceforge.pebble.plugins.Plugin" required="true" %>
<%@ attribute name="properties" type="java.util.Properties" required="true" %>
<%@ attribute name="divId" type="java.lang.String" required="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://pebble.sourceforge.net/pebble" prefix="pebble" %>

<div id="${divId}" class="pluginConfig" style="display: none;">
    <c:if test="${plugin.description != null}">
        <div class="pluginDescription">
            ${plugin.description}
        </div>
    </c:if>
    <c:if test="${plugin.description == null}">
        No plugin description
    </c:if>
    <c:if test="${plugin.configurable}">
        <ul>
        <c:forEach items="${plugin.pluginConfig}" var="pluginConfig">
            <li>
                <div class="pluginPropertyLabel">${pluginConfig.name} :</div>
                <div class="pluginPropertyControl">
                    <pebble:renderpluginconfig pluginConfig="${pluginConfig}" properties="${properties}"/>
                </div>
            </li>
        </c:forEach>
        </ul>
    </c:if>
</div>
