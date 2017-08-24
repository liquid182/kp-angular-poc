package org.kp.foundation.angular.poc.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by ryanmccullough on 2017-08-23.
 */
public class CmdLineUtils {
    private static Logger LOG = LoggerFactory.getLogger(CmdLineUtils.class);

    public static Path getTempFilePath(Path folderPath, String prefix, String suffix){
        Path tempPath = null;
        try{
            tempPath = Files.createTempFile(folderPath, prefix, suffix);
        }catch(IOException ioe){
            LOG.warn ("IOException thrown while creating temporary file [{}]:",folderPath.toString(),ioe);
        }
        return tempPath;
    }

    public static void writeFile(Path filePath, String content){
        try{
            Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(filePath.toString())));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            LOG.warn("Error writing content to file [{}]:",filePath.toString(),e);
        }

    }

    public static String readFile(Path file){
        String fileContents = "";

        try {
            InputStream is = new FileInputStream(file.toString());
            fileContents = getStreamString(is);
        }catch(IOException ioe){
            LOG.warn("Error reading file [{}]:",file.toString(), ioe);
        }
        return fileContents;

    }

    public static String getStreamString(InputStream is){

        StringBuilder streamString = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        try{
            String line = reader.readLine();
            while(line != null){
                streamString .append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
        }catch(IOException ioe){
            LOG.warn("Error reading InputStream to string:",ioe);
        }

        return streamString.toString();
    }

    public static String runCommand(String command, String directory){
        String processOutput = "";
        File dir = new File(directory);

        LOG.debug("Running CMD:{}",command);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command, null, dir);
        }catch (IOException ioe){
            LOG.warn("Error executing command [{}]:",command,ioe);
        }

        if( process != null) {
            String errorOutput = getStreamString(process.getErrorStream());
            if(!errorOutput.isEmpty()){
                LOG.warn("Error stream returned from command: {}", errorOutput);
            }
            processOutput = getStreamString(process.getInputStream());
        }
        return processOutput;
    }
}
