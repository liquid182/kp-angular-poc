<%@page session="false" import="java.io.*,java.nio.file.*" %><%
%><%@include file="/libs/foundation/global.jsp" %>
<%!

	public String compileReactTemplate(String reactTemplate)
	{
        Path reactBuildFolder = null;
    	Path templateFile = null;

    	try { 
        	reactBuildFolder = Paths.get("react-aem-master/build").toAbsolutePath();

        	// create a temporary template file in the React build folder...
        	templateFile = Files.createTempFile(reactBuildFolder, "template-", ".html");

        } catch (IOException e) {
			e.printStackTrace();
        }
    
        // write the reactContent to the temp file...
        try(Writer writer = new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(templateFile.toString())))) {

            writer.write(reactTemplate);

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

    	return processOutput;
	}

%>
