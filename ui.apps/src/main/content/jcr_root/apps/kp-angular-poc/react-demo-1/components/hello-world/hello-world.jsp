<%@page session="false"%><%
%><%@ include file="/libs/foundation/global.jsp" %><%
%><%@ page contentType="text/html; charset=utf-8"import="java.io.*,java.nio.file.*" %><%
%><%
    String reactContent = properties.get("jcr:reactContent", "");
	Path reactBuildFolder = Paths.get("react-aem-master/build").toAbsolutePath();
%>

<div id="placeholder">PLACEHOLDER</div>

<%
	// create a temporary template file in the React build folder...
	Path templateFile = Files.createTempFile(reactBuildFolder, "template-", ".html");

	// write the reactContent to the temp file...
	try(Writer writer = new BufferedWriter(
        	new OutputStreamWriter(
                new FileOutputStream(templateFile.toString())))) {

   		writer.write("var helloWorld = function() { return (" + reactContent + " )};");

    } catch (IOException e) {
        e.printStackTrace();
    }

	// compile the template and capture the output
	String gruntCommand = "react-aem-master/do-grunt.sh " + templateFile.toString();
    String processOutput = "";

    try {

        Process process = Runtime.getRuntime().exec(gruntCommand);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            processOutput += line + "\n";
        }
     
        reader.close();
     
    } catch (IOException e) {
        e.printStackTrace();
    }
%>

<script type="text/javascript">
	<%=processOutput%>
</script>
<cq:includeClientLib categories="apps.react-demo-1"/>
<script type="text/javascript">
    ReactDOM.render(React.createElement(HelloAEMWorld), document.getElementById('placeholder'));
</script>

<!-- START DEBUGGING INFO -->
<div style="padding-top:20px;">
    <table border="1">
		<tr>
            <td>reactContent</td><td><%=reactContent%></td>
        </tr>
		<tr>
            <td>reactBuildFolder</td><td><%=reactBuildFolder%></td>
        </tr>
		<tr>
            <td>templateFile</td><td><%=templateFile%></td>
        </tr>
		<tr>
            <td>gruntCommand</td><td><%=gruntCommand%></td>
        </tr>
		<tr>
            <td>processOutput</td><td><%=processOutput%></td>
        </tr>
    </table>
</div>
<!-- END DEBUGGING INFO -->
