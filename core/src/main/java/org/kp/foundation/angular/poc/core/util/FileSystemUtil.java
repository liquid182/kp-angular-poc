package org.kp.foundation.angular.poc.core.util;

import com.day.crx.JcrConstants;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;
import java.util.Iterator;

public class FileSystemUtil {

    private static Logger LOG = LoggerFactory.getLogger(FileSystemUtil.class);

    public static boolean copyJcrToFS(Resource srcDir, File fsDir, Boolean deep) {
        boolean success = true;
        if (!Resource.RESOURCE_TYPE_NON_EXISTING.equals(srcDir.getResourceType())) {
            Iterator<Resource> resourceIterator = srcDir.listChildren();
            while (resourceIterator.hasNext()) {
                Resource child = resourceIterator.next();
                String fsPath = fsDir.getPath().concat("/").concat(child.getName());
                if (child.getResourceType().equals(JcrConstants.NT_FOLDER)) {
                    File newDir = new File(fsPath);
                    success = newDir.mkdir();
                    if (success && deep) {
                        success = copyJcrToFS(child, newDir, deep);
                    }
                    LOG.debug("Creating directory: {}", fsDir.getPath());
                } else if (child.getResourceType().equals(JcrConstants.NT_FILE)) {
                    success = writeFile(fsPath, child);
                }
            }
        }
        return success;
    }

    public static File createTempFileDir() {
        return Files.createTempDir();
    }

    public static boolean writeFile(String fsPath, Resource jcrRes) {
        boolean success = true;
        LOG.debug("Creating file: {}", fsPath);
        byte[] fileBytes = JCRUtil.getNtFileByteArray(jcrRes);
        return writeFile(fsPath, fileBytes);
    }

    public static boolean writeFile(String fsPath, byte[] fileBytes) {
        boolean success;
        File file = new File(fsPath);
        LOG.debug("Creating file: {}", fsPath);
        try {
            success = file.createNewFile();
            if (success && fileBytes != null) {
                Files.write(fileBytes, file);
            }
        } catch (IOException ioe) {
            LOG.warn("Error creating file on FS:{}:", fsPath, ioe);
            success = false;
        }
        return success;
    }

    public static String readFile(String filePath) {
        return readFile(new File(filePath));
    }

    public static String readFile(File file) {
        String fileContents = null;
        try {
            fileContents = Files.toString(file, Charset.defaultCharset());
        } catch (IOException ioe) {
            LOG.warn("Error reading file:{}:", file, ioe);
        }
        return fileContents;
    }
}
