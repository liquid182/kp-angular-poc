<%
%><%@page session="false" contentType="text/html; charset=utf-8" %><%
%><%@include file="/libs/foundation/global.jsp" %><%
%><%
    String[] selectors = slingRequest.getRequestPathInfo().getSelectors();
	Boolean isPartial = (java.util.Arrays.asList(selectors).contains("partial") ? true : false);
%>
<%if(!isPartial) {%><%
%><!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <cq:include script="react-libs.jsp" />
</head>
<body>

    <p>React Partial Template</p>
    <div>
        <cq:include path="content-par" resourceType="foundation/components/parsys" />
    </div>

</body>
</html>
<%} else {%>
    <p>React Partial Template</p>
    <div>
        <cq:include path="content-par" resourceType="foundation/components/parsys" />
    </div>
<%}%>