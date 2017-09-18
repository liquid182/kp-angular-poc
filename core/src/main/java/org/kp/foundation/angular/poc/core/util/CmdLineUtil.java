package org.kp.foundation.angular.poc.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by ryanmccullough on 2017-08-23.
 */
public class CmdLineUtil {
    private static Logger LOG = LoggerFactory.getLogger(CmdLineUtil.class);

    public static String getStreamString(InputStream is){

        StringBuilder streamString = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        try{
            String line = reader.readLine();
            while(line != null){
                streamString.append(line).append("\n");
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
