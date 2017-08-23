<%@page session="false" %><%
%><%@include file="/libs/foundation/global.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <cq:include script="react-libs.jsp" />
</head>
<body>
	<p>React Root Template</p>
    <div>
        <cq:include path="content-par" resourceType="foundation/components/parsys" />
    </div>

</body>
</html>
