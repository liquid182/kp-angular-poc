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

    public static void writeTempFile(Path tempFile, String content){
        try{
            Writer writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(tempFile.toString())));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            LOG.warn("Error writing content to file [{}]:",tempFile.toString(),e);
        }

    }


    public static String runCommand(String command){
        String processOutput = "";
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            while(line != null){
                processOutput += line + "\n";
                line = reader.readLine();
            }
            reader.close();

        } catch (IOException e) {
            LOG.warn("Error reading command output for command [{}]:",command,e);
        }
        return processOutput;
    }
}
