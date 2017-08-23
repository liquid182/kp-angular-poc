<%@page session="false" %><%
%><%@ include file="/libs/foundation/global.jsp" %><%
%><%@ include file="/apps/react-foundation/compiler.jsp" %><%
%><%@ page contentType="text/html; charset=utf-8"import="java.io.*,java.nio.file.*" %><%
%><%
    String reactContent = properties.get("jcr:reactContent", "");
	String reactTemplateFile = properties.get("jcr:reactTemplate", "");
	String reactTemplate = reactContent;		// default in the event there was no template selected
	String componentUniqueId = currentNode.getIdentifier().replaceAll("(/|:|-)", "_");
%>

<div id="app">PLACEHOLDER</div>

<%
    if(reactTemplateFile != "")
	{
    	// fetch the template
		Resource res = resourceResolver.getResource(reactTemplateFile);
		Node node = res.adaptTo(Node.class);
		reactTemplate = node.getNode("./jcr:content").getProperty("jcr:data").getString(); 

    	// do the replacements
    	reactTemplate = reactTemplate.replace("[reactContent]", reactContent);
	}
%>

<cq:includeClientLib categories="apps.react-demo-3"/>
<script type="text/javascript">
    <%=compileReactTemplate(reactTemplate)%>
    <cq:include script="react-component.jsp"/>
</script>

