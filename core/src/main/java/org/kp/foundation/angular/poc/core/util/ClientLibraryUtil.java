package org.kp.foundation.angular.poc.core.util;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.BinaryImpl;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.JcrResourceConstants;
import org.kp.foundation.angular.poc.core.models.ClientLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;

/**
 * The Class ClientLibraryUtil.
 * <p>
 * Utility class containing methods relating to the creation of Client Libraries, addition of CSS/JavaScript files to
 * these libraries and the removal of files from these libraries.
 * </p>
 *
 * @author Ryan McCullough
 */
public class ClientLibraryUtil {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ClientLibraryUtil.class);

    /**
     * The Constant BASE_FOLDER_FORMAT.
     */
    public static final String BASE_FOLDER_FORMAT = "#base=%s";

    /**
     * The Constant CATEGORIES_PROP.
     */
    public static final String CATEGORIES_PROP = "categories";

    /**
     * The Constant CQ_CLIENTLIBRARYFOLDER.
     */
    public static final String CQ_CLIENTLIBRARYFOLDER = "cq:ClientLibraryFolder";

    /**
     * The Constant CSS_FOLDER.
     */
    public static final String CSS_FOLDER = "css";

    /**
     * The Constant CSS_MIMETYPE.
     */
    public static final String CSS_MIMETYPE = "text/css";

    /**
     * The Constant CSS_TXT.
     */
    public static final String CSS_TXT = "css.txt";

    /**
     * The Constant CUSTOM_CSS.
     */
    public static final String CUSTOM_CSS = "custom.css";

    /**
     * The Constant DEFAULT_DEPENDENCIES.
     */
    public static final String DEFAULT_DEPENDENCIES = "";

    /**
     * The Constant DEPENDENCIES_PROP.
     */
    public static final String DEPENDENCIES_PROP = "dependencies";

    /**
     * The Constant FN_REGEX.
     */
    public static final String FN_REGEX = "(?m)^%s$";

    /**
     * The Constant JAVASCRIPT_SUFFIX.
     */
    public static final String JAVASCRIPT_SUFFIX = ".js";

    /**
     * The Constant JS_FOLDER.
     */
    public static final String JS_FOLDER = "js";

    /**
     * The Constant JS_MIMETYPE.
     */
    public static final String JS_MIMETYPE = "application/javascript";

    /**
     * The Constant JS_TXT.
     */
    public static final String JS_TXT = "js.txt";

    /**
     * The Constant TEXT_MIMETYPE.
     */
    public static final String TEXT_MIMETYPE = "plain/text";

    /**
     * Adds the java script to client library.
     *
     * @param clientLib        the client lib
     * @param fileName         the file name
     * @param jsText           the js text
     * @param overrideExisting the override existing
     * @param autoSave         the auto save
     * @return the node
     */
    public static Node addJavaScriptToClientLibrary(
            final Node clientLib,
            final String fileName,
            final String jsText,
            final Boolean overrideExisting,
            final Boolean autoSave) {
        return addToClientLibrary(clientLib, JS_FOLDER, JS_TXT, fileName, jsText, JS_MIMETYPE, overrideExisting,
                autoSave);

    }

    /**
     * Adds the stylesheet to client library.
     *
     * @param clientLib        the client lib
     * @param fileName         the file name
     * @param cssText          the css text
     * @param overrideExisting the override existing
     * @param autoSave         the auto save
     * @return the node
     */
    public static Node addStylesheetToClientLibrary(
            final Node clientLib,
            final String fileName,
            final String cssText,
            final Boolean overrideExisting,
            final Boolean autoSave) {
        return addToClientLibrary(clientLib, CSS_FOLDER, CSS_TXT, fileName, cssText, CSS_MIMETYPE, overrideExisting,
                autoSave);
    }

    /**
     * Adds the to client library.
     *
     * @param clientLib        the client lib
     * @param hostFolder       the host folder
     * @param referenceFile    the reference file
     * @param fileName         the file name
     * @param fileText         the file text
     * @param mimeType         the mime type
     * @param overrideExisting the override existing
     * @param autoSave         the auto save
     * @return the node
     */
    private static Node addToClientLibrary(
            final Node clientLib,
            final String hostFolder,
            final String referenceFile,
            final String fileName,
            final String fileText,
            final String mimeType,
            final Boolean overrideExisting,
            final Boolean autoSave) {
        Node fileNode = null;
        // first thing to do is check whether the file's host folder (css/js)
        // exists
        try {
            final Node hostFolderNode;
            if (clientLib.hasNode(hostFolder)) {
                hostFolderNode = clientLib.getNode(hostFolder);
            } else {
                // create a new host folder node if it doesn't exist
                hostFolderNode = JCRUtil.createFolder(clientLib, hostFolder, false);
            }
            // add or update the file (css/js file)
            if (hostFolderNode.hasNode(fileName) && overrideExisting) {
                fileNode = hostFolderNode.getNode(fileName);
                fileNode.getNode(JcrConstants.JCR_CONTENT)
                        .setProperty(JcrConstants.JCR_DATA, new BinaryImpl(fileText.getBytes()));
            } else {
                fileNode = JCRUtil.createTextFile(hostFolderNode, fileName, fileText, mimeType, false);
            }

            // update reference file as well (css.txt or js.txt)
            final Node refFileNode;
            if (clientLib.hasNode(referenceFile)) {
                refFileNode = clientLib.getNode(referenceFile);
            } else {
                refFileNode = JCRUtil
                        .createTextFile(clientLib, referenceFile, String.format(BASE_FOLDER_FORMAT, hostFolder),
                                TEXT_MIMETYPE, false);
            }
            final Node refFileContentNode = refFileNode.getNode(JcrConstants.JCR_CONTENT);
            String refFileContent = JCRUtil.getBinaryPropAsString(refFileContentNode, JcrConstants.JCR_DATA);
            if (!refFileContent.contains(fileName)) {
                refFileContent += "\n" + fileName;
            }
            refFileContentNode.setProperty(JcrConstants.JCR_DATA, new BinaryImpl(refFileContent.getBytes()));
            refFileContentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());
            if (autoSave && clientLib.getSession().hasPendingChanges()) {
                clientLib.getSession().refresh(true);
                clientLib.getSession().save();
            }
        } catch (RepositoryException re) {
            LOG.error("Error adding file {} to client library:", fileName, re);
        }
        return fileNode;
    }

    /**
     * Creates the base css structure.
     *
     * @param clientLibraryPath the client library path
     * @param category          the category
     * @param resourceResolver  the resource resolver
     */
    public static void createBaseCssStructure(
            final String clientLibraryPath, final String category, final ResourceResolver resourceResolver) {

        final String customStylesheetPath = clientLibraryPath + "/" + CSS_FOLDER + "/" + CUSTOM_CSS;
        final Resource clientLibraryResource = resourceResolver.getResource(clientLibraryPath);
        final Node clientLibraryNode;
        if (clientLibraryResource == null) {
            // clientlib doesn't exist, let's create it.
            clientLibraryNode = ClientLibraryUtil
                    .createClientLibrary(clientLibraryPath, new String[]{category}, new String[]{ClientLibraryUtil.DEFAULT_DEPENDENCIES},
                            resourceResolver, false, true, false);
        } else {
            clientLibraryNode = clientLibraryResource.adaptTo(Node.class);
        }

        final Resource customCssResource = resourceResolver.getResource(customStylesheetPath);
        if (customCssResource == null) {
            ClientLibraryUtil
                    .addStylesheetToClientLibrary(clientLibraryNode, CUSTOM_CSS, StringUtils.EMPTY, true, true);

        }

    }

    public static Node createClientLibrary(ClientLibrary clientLibrary){
        return createClientLibrary(clientLibrary.getPath(),
                clientLibrary.getCategories(),
                clientLibrary.getDependencies(),
                clientLibrary.getResource().getResourceResolver(),
                true, true, true);
    }


    /**
     * Creates the client library.
     *
     * @param path             the path
     * @param category         the category
     * @param dependency       the dependency
     * @param resourceResolver the resource resolver
     * @return the node
     */
    public static Node createClientLibrary(
            final String path,
            final String category,
            final String dependency,
            final ResourceResolver resourceResolver) {
        return createClientLibrary(path, new String[]{category}, new String[]{dependency}, resourceResolver, true, true, true);
    }

    /**
     * Creates the client library.
     *
     * @param path             the path
     * @param category         the category
     * @param dependency       the dependency
     * @param resourceResolver the resource resolver
     * @return the node
     */
    public static Node createClientLibrary(
            final String path,
            final String[] category,
            final String[] dependency,
            final ResourceResolver resourceResolver) {
        return createClientLibrary(path, category, dependency, resourceResolver, true, true, true);
    }

    /**
     * Creates the client library.
     *
     * @param path             the path
     * @param category         the category
     * @param dependency       the dependency
     * @param resourceResolver the resource resolver
     * @param includeJS        the include JS
     * @param includeCSS       the include CSS
     * @param autoSave         the auto save
     * @return the node
     */
    public static Node createClientLibrary(
            final String path,
            final String[] category,
            final String[] dependency,
            final ResourceResolver resourceResolver,
            final Boolean includeJS,
            final Boolean includeCSS,
            final Boolean autoSave) {
        Node clientLib = null;
        try {
            clientLib = JcrUtil.createPath(path, JcrResourceConstants.NT_SLING_ORDERED_FOLDER, CQ_CLIENTLIBRARYFOLDER,
                    resourceResolver.adaptTo(Session.class), false);
            if (clientLib != null) {
                clientLib.setProperty(DEPENDENCIES_PROP, dependency);
                clientLib.setProperty(CATEGORIES_PROP, category);
                if (includeJS) {
                    JCRUtil.createTextFile(clientLib, JS_TXT, String.format(BASE_FOLDER_FORMAT, JS_FOLDER),
                            TEXT_MIMETYPE, false);
                    JCRUtil.createFolder(clientLib, JS_FOLDER, false);
                }
                if (includeCSS) {
                    JCRUtil.createTextFile(clientLib, CSS_TXT, String.format(BASE_FOLDER_FORMAT, CSS_FOLDER),
                            TEXT_MIMETYPE, false);
                    JCRUtil.createFolder(clientLib, CSS_FOLDER, false);
                }
                if (autoSave) {
                    clientLib.getSession().refresh(true);
                    clientLib.getSession().save();
                }
            }
        } catch (RepositoryException re) {
            LOG.error("Error creating client library:", re);
        }
        return clientLib;
    }

    /**
     * Removes the from client library.
     *
     * @param clientLib  the client lib
     * @param hostFolder the host folder
     * @param fileName   the file name
     * @param autoSave   the auto save
     */

    public static void removeFromClientLibrary(
            final Node clientLib,
            final String hostFolder,
            final String referenceFile,
            final String fileName,
            final Boolean autoSave) {
        // first thing to do is check whether the file's host folder (css/js)
        // exists
        try {
            if (clientLib.hasNode(hostFolder)) {
                // if the host folder node exists, lets see if we can find the
                // associated file.
                final Node hostFolderNode = clientLib.getNode(hostFolder);
                final NodeIterator fileNodeIterator = hostFolderNode.getNodes();
                while (fileNodeIterator.hasNext()) {
                    final Node fileNode = fileNodeIterator.nextNode();
                    final String nodeName = fileNode.getName();
                    if (nodeName.contains(fileName)) {
                        // if the JS/CSS file exists under the hostfoldernode,
                        // remove it.
                        hostFolderNode.getNode(nodeName).remove();
                    }
                }
            }
            // at this point, the file is removed, just need to remove
            // references in the text file.
            removeReferenceFromTxt(clientLib, referenceFile, fileName);
        } catch (RepositoryException re) {
            LOG.error("Error removing file from client library:", re);
        }
    }

    /**
     * Removes the java script from client library.
     *
     * @param clientLib the client lib
     * @param fileName  the file name
     * @param autoSave  the auto save
     */
    public static void removeJavaScriptFromClientLibrary(
            final Node clientLib, final String fileName, final Boolean autoSave) {
        removeFromClientLibrary(clientLib, JS_FOLDER, JS_TXT, fileName, autoSave);

    }

    /**
     * Removes the stylesheet from client library.
     *
     * @param clientLib the client lib
     * @param fileName  the file name
     * @param autoSave  the auto save
     */

    public static void removeStylesheetFromClientLibrary(
            final Node clientLib, final String fileName, final Boolean autoSave) {
        removeFromClientLibrary(clientLib, CSS_FOLDER, CSS_TXT, fileName, autoSave);
    }

    /**
     * Removes the reference from txt.
     *
     * @param clientLib     the client lib
     * @param referenceFile the reference file
     * @param fileName      the file name
     * @throws RepositoryException the repository exception
     */
    private static void removeReferenceFromTxt(
            final Node clientLib, final String referenceFile, final String fileName) throws RepositoryException {
        if (clientLib.hasNode(referenceFile)) {
            final Session session = clientLib.getSession();
            final Node referenceJCRContentNode = clientLib.getNode(referenceFile + "/" + JcrConstants.JCR_CONTENT);

            String referenceFileText = JCRUtil.getBinaryPropAsString(referenceJCRContentNode, JcrConstants.JCR_DATA);
            if (referenceFileText.contains(fileName)) {
                referenceFileText = referenceFileText.replaceAll(String.format(FN_REGEX, fileName), "").trim();
            }
            referenceJCRContentNode.setProperty(JcrConstants.JCR_DATA, new BinaryImpl(referenceFileText.getBytes()));
            referenceJCRContentNode.setProperty(JcrConstants.JCR_LASTMODIFIED, Calendar.getInstance());

            if (session.hasPendingChanges()) {
                session.refresh(true);
                session.save();
            }
        }
    }
}
