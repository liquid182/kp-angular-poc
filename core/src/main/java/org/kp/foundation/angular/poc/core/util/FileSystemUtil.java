package org.kp.foundation.angular.poc.core.util;

import com.day.crx.JcrConstants;
import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.Charset;
import java.util.Iterator;

public class FileSystemUtil {

    private static Logger LOG = LoggerFactory.getLogger(CmdLineUtil.class);

    public void copyJcrToFS(Object resource, File fsDir, Boolean deep) {
        copyJcrToFS((Resource) resource, fsDir, deep);
    }

    public void copyJcrToFS(Resource srcDir, File fsDir, Boolean deep) {
        if (!Resource.RESOURCE_TYPE_NON_EXISTING.equals(srcDir.getResourceType())) {
            Iterator<Resource> resourceIterator = srcDir.listChildren();
            while (resourceIterator.hasNext()) {
                Resource child = resourceIterator.next();
                if (child.getResourceType().equals(JcrConstants.NT_FOLDER)) {
                    File newDir = new File(fsDir.getPath() + "/" + child.getName());
                    newDir.mkdir();
                    if (deep) {
                        copyJcrToFS(child, newDir, deep);
                    }
                    LOG.debug("Creating directory: {}", fsDir.getPath());
                } else if (child.getResourceType().equals(JcrConstants.NT_FILE)) {
                    File file = new File(fsDir.getPath() + "/" + child.getName());
                    LOG.debug("Creating file: {}", fsDir.getPath());
                    try {
                        file.createNewFile();
                        Resource jcrContent = child.getChild(JcrConstants.JCR_CONTENT);
                        if (jcrContent != null) {
                            InputStream fileStream = jcrContent.adaptTo(InputStream.class);
                            if (fileStream != null) {
                                byte[] fileBytes = IOUtils.toByteArray(fileStream);
                                Files.write(fileBytes, file);
                            }
                        }
                    } catch (IOException ioe) {
                        LOG.warn("Error creating file on FS:{}:", fsDir.getPath(), ioe);
                    }
                }
            }
        }
    }

    public static File createTempFileDir() {
        return Files.createTempDir();
    }

    public static void writeFile(String filePath, String content) {
        writeFile(new File(filePath), content.getBytes());
    }

    public static void writeFile(File file, byte[] content) {
        try {
            Files.write(content, file);
        } catch (IOException ioe) {
            LOG.warn("Error creating file:", ioe);
        }
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
