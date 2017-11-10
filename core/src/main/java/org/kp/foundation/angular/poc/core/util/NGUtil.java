package org.kp.foundation.angular.poc.core.util;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.components.Component;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class NGUtil {

    private static Logger LOG = LoggerFactory.getLogger(NGUtil.class);

    public static boolean doAngularBuild(Component component, Resource componentRes, NGConstants.COMPILE_TYPE compileType) {
        boolean success;
        String componentPath = component.getPath();
        ResourceResolver resourceResolver = componentRes.getResourceResolver();
        String srcPath = NGConstants.BASE_PROJECT_PATH;
        Resource srcRes = resourceResolver.resolve(srcPath);
        File fsDir = FileSystemUtil.createTempFileDir();
        String fsDirPath = fsDir.getPath();
        //copy base project to FS
        success = FileSystemUtil.copyJcrToFS(srcRes, fsDir,true);
        //get component template/data and copy as well.
        Map<String,String> fileMap = getComponentFiles(componentPath,resourceResolver);
        fileMap = doFreemarkerCompile(fileMap, componentRes);
        success = copyCompiledFiles(fileMap,fsDirPath);
        doNPMInstall(fsDirPath);
        doNPMBuild(fsDirPath,compileType);
        String bundleContent = getCompiledBundleContent(fsDirPath, compileType);
        createNGClientLibraryFromFS(bundleContent, componentRes, compileType);
        return success;
    }

    public static String[] getNGClientLibCategories(Resource componentResource, NGConstants.COMPILE_TYPE compileType){
        String uuid = getNgIdFromResource(componentResource);
        return getNGClientLibCategories(uuid,compileType);
    }

    public static String[] getNGClientLibCategories(String componentId, NGConstants.COMPILE_TYPE compileType){
        String[] categories = new String[1];
        categories[0] = String.format(NGConstants.CLIENTLIB_CATEGORY_REGEX, componentId, NGConstants.getCompileTypeString(compileType));
        return categories;
    }

    public static void createNGClientLibraryFromFS(String bundleContents,
                                                            Resource componentRes,
                                                            NGConstants.COMPILE_TYPE compileType){
        String componentId = getNgIdFromResource(componentRes);
        String clientLibPath = getNGClientLibPath(componentId);
        ResourceResolver resourceResolver = componentRes.getResourceResolver();
        Resource clientLibRes = resourceResolver.resolve(clientLibPath);
        Node clientLibNode;
        if( clientLibRes == null || clientLibRes.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING) ) {
            clientLibNode = ClientLibraryUtil
                    .createClientLibrary(clientLibPath, getNGClientLibCategories(componentId, compileType), null,
                            componentRes.getResourceResolver());
        }else{
            clientLibNode = clientLibRes.adaptTo(Node.class);
        }
        ClientLibraryUtil.addJavaScriptToClientLibrary(clientLibNode,
                                                NGConstants.COMPILED_FILENAME,
                                                bundleContents,
                                                true, true);
    }

    public static String getCompiledBundleContent(String tempDir, NGConstants.COMPILE_TYPE compileType){
        String bundlePath = tempDir.concat(NGConstants.getDistForCompileType(compileType)).concat(NGConstants.COMPILED_FILENAME);
        return FileSystemUtil.readFile(bundlePath);
    }

    public static void doNPMInstall(String fsDir){
        CmdLineUtil.runCommand(NGConstants.NPM_INSTALL,fsDir);
    }

    public static void doNPMBuild(String fsDir, NGConstants.COMPILE_TYPE compileType){
        CmdLineUtil.runCommand(NGConstants.getNpmBuildCmdForCompileType(compileType), fsDir);
    }

    public static String getNgIdFromResource(Resource resource){
        String uuid = null;
        ResourceResolver resourceResolver = resource.getResourceResolver();
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Resource pageContent = pageManager.getContainingPage(resource).getContentResource();
        ModifiableValueMap properties = pageContent.adaptTo(ModifiableValueMap.class);
        if( properties.containsKey(NGConstants.NG_UUID) ){
            uuid = properties.get(NGConstants.NG_UUID, String.class);
        }
        if(StringUtils.isEmpty(uuid) ) {
            uuid = UUID.randomUUID().toString();
            properties.put(NGConstants.NG_UUID, uuid);
        }
        return uuid;
    }

    public static Map<String,String> getComponentFiles(String componentAppsPath, ResourceResolver resourceResolver){
        Map<String , String> fileMap = new HashMap<>();
        String ngSrcPath = getNGSrcPathForComponent(componentAppsPath);
        Resource ngRes = resourceResolver.getResource(ngSrcPath);
        if( ngRes != null && !ngRes.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING)){
            Iterator<Resource> ngSrcItr = ngRes.listChildren();
            while( ngSrcItr.hasNext() ){
                Resource srcItem = ngSrcItr.next();
                if( srcItem.isResourceType(JcrConstants.NT_FILE)) {
                    String sourceStr = JCRUtil.getNtFileAsString(srcItem);
                    fileMap.put(srcItem.getName(), sourceStr);
                }
            }
        }else{
            LOG.warn("Tried to get NG component files in component without {} directory [{}].  Nothing to do.", NGConstants.NG_SRC, ngSrcPath);
        }
        return fileMap;
    }

    public static Map<String, String> doFreemarkerCompile(Map<String,String> files, Resource componentRes){
        for(String name: files.keySet()){
            if( name.endsWith(NGConstants.HTML_EXTENSION) ){
                String markup = files.get(name);
                markup = FreemarkerUtil.doFreemarkerReplacement(markup, componentRes.getValueMap(), name);
                LOG.debug("Replaced values:{}",markup);
                files.put(name,markup);
            }
        }
        return files;
    }

    public static boolean copyCompiledFiles(Map<String,String> fileMap, String prefixPath){
        boolean success = true;
        for( String name: fileMap.keySet()){
            String filePath = prefixPath.concat(NGConstants.NG_BASE_APP_DIR).concat(name);
            success = success && FileSystemUtil.writeFile(filePath, fileMap.get(name).getBytes());
            LOG.debug("Writing file {} to location {}", name, filePath);
        }
        return success;
    }

    public static String getNGClientLibPath(String componentId){
        return NGConstants.CLIENTLIB_BASE_DIR + JcrUtil.createValidName(componentId);
    }

    public static String getNGSrcPathForComponent(String componentAppsPath){
        String ngSrcPath = componentAppsPath;
        if( !ngSrcPath.endsWith("/")){
            ngSrcPath = ngSrcPath.concat("/");
        }
        return ngSrcPath.concat(NGConstants.NG_SRC);
    }
}
