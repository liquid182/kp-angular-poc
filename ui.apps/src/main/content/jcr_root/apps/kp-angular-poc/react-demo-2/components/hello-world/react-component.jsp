<%@page session="false" %><%
%><%@ include file="/libs/foundation/global.jsp" %><%
%><%
	String componentUniqueId = currentNode.getIdentifier().replaceAll("(/|:|-)", "_");
%>

var HelloAEMWorld_<%=componentUniqueId%> =  React.createClass({
  dynamicData: 'some data',
  functionData: function() {
    return 'this could be ajax';
  },
  render: helloWorld_<%=componentUniqueId%>});
