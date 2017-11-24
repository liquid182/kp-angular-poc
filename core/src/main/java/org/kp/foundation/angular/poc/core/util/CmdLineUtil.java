package org.kp.foundation.angular.poc.core.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by ryanmccullough on 2017-08-23.
 */
public class CmdLineUtil {

    private static Logger LOG = LoggerFactory.getLogger(CmdLineUtil.class);

    private static TimeUnit WAIT_TIME_TYPE = TimeUnit.MINUTES;
    private static Integer WAIT_TIME = 5;
    private static String LOG_EXTENSION = ".log";

    public static String getStreamString(InputStream is) {

        StringBuilder streamString = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line = reader.readLine();
            while (line != null) {
                streamString.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException ioe) {
            LOG.warn("Error reading InputStream to string:", ioe);
        }

        return streamString.toString();
    }

    public static String runCommand(String[] command, String directory) {
        String processOutput = "";
        File dir = new File(directory);
        File logFile = new File(directory.concat("/").concat(String.join(".",command).concat(LOG_EXTENSION)));
        LOG.debug("Running CMD:{}", String.join(" ",command));
        ProcessBuilder pb =
                new ProcessBuilder(command)
                        .directory(dir)
                        .redirectErrorStream(true)
                        .redirectOutput(ProcessBuilder.Redirect.appendTo(logFile));
        assert pb.redirectInput() == ProcessBuilder.Redirect.PIPE;
        assert pb.redirectOutput().file() == logFile;
        Process process;
        try {
            process = pb.start();
            //processOutput = getStreamString(process.getInputStream());
            if(!process.waitFor(WAIT_TIME, WAIT_TIME_TYPE)){
                LOG.warn("Waited longer that {} {} for process {} to complete. Forcibly stopping.", new Object[]{WAIT_TIME,WAIT_TIME_TYPE.toString(), command});
                process.destroyForcibly();
            }
        } catch (IOException ioe) {
            LOG.warn("Error executing command [{}]:", command, ioe);
        }catch (InterruptedException ie){
            LOG.error("Interrupt Exception thrown executing command.  Force closing.",ie);
        }finally {
            processOutput = FileSystemUtil.readFile(logFile);
        }

        return processOutput;
    }
}
