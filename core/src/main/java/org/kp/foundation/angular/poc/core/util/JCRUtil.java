package org.kp.foundation.angular.poc.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.Workspace;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.value.BinaryImpl;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

/**
 * The Class JCRUtil.
 * <p>
 * Utility class containing methods relating to the reading, updating, and deleting Nodes and Properties from the JCR.
 * </p>
 *
 * @author Ryan McCullough, Shannon Sumner
 */
public class JCRUtil {

    /**
     * The Constant LOG.
     */
    private static final Logger LOG = LoggerFactory.getLogger(JCRUtil.class);

    /**
     * Adds the file to store path.
     *
     * @param jcrNode  the store path node
     * @param fileName the file name
     * @param is       the is
     * @param mimeType the mime type
     * @param autoSave the auto save
     * @return the node
     */
    public static Node addFileToJCR(
            final Node jcrNode,
            final String fileName,
            final InputStream is,
            final String mimeType,
            final Boolean autoSave) {
        Node fileNode = null;
        try {
            if (jcrNode.hasNode(fileName)) {
                jcrNode.getNode(fileName).removeSharedSet();
            }
            fileNode = jcrNode.addNode(fileName, JcrConstants.NT_FILE);
            final Node jcrContent = fileNode.addNode(JcrConstants.JCR_CONTENT, JcrConstants.NT_RESOURCE);
            jcrContent.setProperty(JcrConstants.JCR_MIMETYPE, mimeType);
            jcrContent.setProperty(JcrConstants.JCR_DATA, new BinaryImpl(IOUtils.toByteArray(is)));
            if (autoSave) {
                jcrNode.getSession().refresh(true);
                jcrNode.getSession().save();
            }
        } catch (RepositoryException | IOException e) {
            LOG.error("Error adding file to JCR [{}]:", jcrNode, e);
        }
        return fileNode;
    }

    /**
     * Copy node.
     *
     * @param sourceNodePath the source node path
     * @param newNodePath    the new node path
     * @param session        the session
     */
    public static void copyNode(
            final String sourceNodePath, final String newNodePath, final Session session) {
        try {
            final Workspace workspace = session.getWorkspace();
            workspace.copy(sourceNodePath, newNodePath);
        } catch (RepositoryException e) {
            LOG.error("Error copying node [{}]:", sourceNodePath, e);
        }
    }

    /**
     * Creates the folder.
     *
     * @param parentNode the parent node
     * @param folderName the folder name
     * @param autoSave   the auto save
     * @return the node
     */
    public static Node createFolder(
            final Node parentNode, final String folderName, final Boolean autoSave) {
        Node folderNode = null;
        try {
            folderNode = parentNode.addNode(folderName, JcrConstants.NT_FOLDER);
            if (autoSave) {
                parentNode.getSession().refresh(true);
                parentNode.getSession().save();
            }
        } catch (RepositoryException re) {
            LOG.error("Error making folder {}:", folderName, re);
        }
        return folderNode;
    }

    /**
     * Creates the text file.
     *
     * @param parentNode the parent node
     * @param fileName   the file name
     * @param fileText   the file text
     * @param mimeType   the mime type
     * @param autoSave   the auto save
     * @return the node
     */
    public static Node createTextFile(
            final Node parentNode,
            final String fileName,
            final String fileText,
            final String mimeType,
            final Boolean autoSave) {
        Node textNode = null;
        try {
            final InputStream inputStream = IOUtils.toInputStream(fileText, StandardCharsets.UTF_8);
            textNode = addFileToJCR(parentNode, fileName, inputStream, mimeType, autoSave);
            if (autoSave) {
                parentNode.getSession().refresh(true);
                parentNode.getSession().save();
            }
        } catch (RepositoryException e) {
            LOG.error("Error creating text file [{}]:", fileName, e);
        }
        return textNode;
    }

    /**
     * Gets the binary prop as string.
     *
     * @param propNode the prop node
     * @param propKey  the prop key
     * @return the binary prop as string
     */
    public static String getBinaryPropAsString(
            final Node propNode, final String propKey) {
        final StringWriter writer = new StringWriter();
        try {
            final InputStream is = propNode.getProperty(propKey).getBinary().getStream();
            IOUtils.copy(is, writer, StandardCharsets.UTF_8);
        } catch (IOException | RepositoryException e) {
            LOG.error("Error converting binary property to string:", e);
        }
        return writer.toString();
    }

    /**
     * Gets the node.
     *
     * @param nodePath         the node path
     * @param resourceResolver the resource resolver
     * @return the node
     */
    public static Node getNode(
            final String nodePath, final ResourceResolver resourceResolver) {
        final Resource resource = resourceResolver.getResource(nodePath);
        Node node = null;
        if (resource != null) {
            node = resource.adaptTo(Node.class);
        }
        return node;
    }

    /**
     * Gets page depth from path.
     *
     * @param path             the path
     * @param resourceResolver the resource resolver
     * @return the depth
     */
    public static int getPageDepthFromPath(
            final String path, final ResourceResolver resourceResolver) {
        String pagePath = path;
        int depth = -1;
        if (resourceResolver != null) {
            final PageManager pm = resourceResolver.adaptTo(PageManager.class);
            final Page page = pm.getContainingPage(pagePath);
            if (page != null) {
                depth = page.getDepth();
            }
        }
        if (depth == -1) {
            if (pagePath.contains(JcrConstants.JCR_CONTENT)) {
                pagePath = StringUtils.substringBefore(pagePath, JcrConstants.JCR_CONTENT);
            }
            depth = StringUtils.countMatches(pagePath, "/");
            if (pagePath.endsWith("/")) {
                depth -= 1;
            }
        }
        return depth;
    }

    /**
     * Move node.
     *
     * @param sourceNodePath the source node path
     * @param newNodePath    the new node path
     * @param session        the session
     */
    public static void moveNode(
            final String sourceNodePath, final String newNodePath, final Session session) {
        try {
            final Workspace workspace = session.getWorkspace();
            workspace.move(sourceNodePath, newNodePath);

        } catch (RepositoryException e) {
            LOG.error("Error moving node from {} to {}:", new String[] { sourceNodePath, newNodePath }, e);
        }
    }

    /**
     * Removes the node.
     *
     * @param node the node
     */
    public static void removeNode(final Node node) {
        try {
            Session session = node.getSession();
            session.refresh(false);
            node.remove();
            session.save();
        } catch (Exception e) {
            LOG.error("Exception in JCRUtil:removeNode ", e);
        }

    }

}
