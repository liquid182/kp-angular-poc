package org.kp.foundation.angular.poc.core.util;

import com.day.cq.commons.jcr.JcrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.kp.foundation.angular.poc.core.models.NGApp;
import org.kp.foundation.angular.poc.core.models.NGComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class NGUtil {

    private static Logger LOG = LoggerFactory.getLogger(NGUtil.class);

    public static boolean doAngularBuild(Resource pageResource, NGConstants.COMPILE_TYPE compileType) {
        NGApp ngApp = NGApp.fromPageRes(pageResource, compileType);

        //todo: Get component resources by looking for NGCONSTANTS.NG_COMPONENT_PROPERTY under page.
        return doAngularBuild(ngApp);
    }

    public static boolean doAngularBuild(NGApp ngApp) {
        boolean success;
        List<NGComponent> ngComponents = ngApp.getComponents();
        String srcPath = NGConstants.BASE_PROJECT_PATH;
        //copy the base project
        Resource srcRes = ngApp.getResourceResolver().resolve(srcPath);
        File fsDir = FileSystemUtil.createTempFileDir();
        String fsDirPath = fsDir.getPath();
        //copy base project to FS
        //TODO: do freemarker transform on substitution files.
        success = FileSystemUtil.copyJcrToFS(srcRes, fsDir, true);


        //copy components
        for( NGComponent component: ngComponents) {
            //get component template/data and copy as well.
            Map<String,String> fileMap = doFreemarkerCompile(component);
            success = copyCompiledFiles(fileMap, fsDirPath);
        }
        doNPMInstall(fsDirPath);
        doNPMBuild(fsDirPath,ngApp.getCompileType());
        String bundleContent = getCompiledBundleContent(fsDirPath, ngApp.getCompileType());
        createNGClientLibraryFromFS(bundleContent, ngApp);
        return success;
    }


    public static String[] getNGClientLibCategories(String componentId, NGConstants.COMPILE_TYPE compileType){
        String[] categories = new String[1];
        categories[0] = String.format(NGConstants.CLIENTLIB_CATEGORY_REGEX, componentId, NGConstants.getCompileTypeString(compileType));
        return categories;
    }

    public static void createNGClientLibraryFromFS(String bundleContents,
                                                           NGApp ngApp){
        Node clientLibNode;
        Resource clientLibRes = ngApp.getClientLibraryResource();
        if( clientLibRes == null || clientLibRes.isResourceType(Resource.RESOURCE_TYPE_NON_EXISTING) ) {
            clientLibNode = ClientLibraryUtil
                    .createClientLibrary(ngApp.getClientLibraryPath(), ngApp.getClientLibraryCategories(), null,
                            ngApp.getResourceResolver());
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


    public static Map<String, String> doFreemarkerCompile(NGComponent component){
        Map<String,String> files = component.getSrcFileMap();
        for(String name: files.keySet()){
            //get the new name of the file, based on the component settings
            String modifiedName = null;
            if( name.toLowerCase().endsWith(NGConstants.TEMPLATE_FILE_SUFFIX)){
                modifiedName = component.getTemplateFileName();
            }else if( name.toLowerCase().endsWith(NGConstants.COMPONENT_FILE_SUFFIX) ){
                modifiedName = component.getComponentFileName();
            }
            if( StringUtils.isNotEmpty(modifiedName) ){
                String markup = files.get(name);
                Map properties = component.getProperties();
                properties.put("ngComponent",component);
                markup = FreemarkerUtil.doFreemarkerReplacement(markup, component.getProperties(), modifiedName);
                LOG.debug("Replaced values:{}",markup);
                files.put(modifiedName,markup);
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

    public static String generateUUID(Resource resource){
        String uuid = "";
        ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);
        if (properties.containsKey(NGConstants.NG_UUID)) {
            uuid = properties.get(NGConstants.NG_UUID, String.class);
        }
        if (StringUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            properties.put(NGConstants.NG_UUID, uuid);
        }
        return uuid;
    }

    public static String getNGClientLibPath(String componentId){
        return NGConstants.CLIENTLIB_BASE_DIR + JcrUtil.createValidName(componentId);
    }

    public static String getPascalCaseForHyphen(String hyphenString){
        return Arrays.stream(hyphenString.split("\\-"))
                .map(String::toLowerCase)
                .map(s -> s.substring(0, 1).toUpperCase() + s.substring(1))
                .collect(Collectors.joining());
    }

    public static String updateStringInteger(String string){
        String updatedString = string;
        Pattern idPattern = Pattern.compile("^([^\\d]+)(\\d*)$");
        Matcher m = idPattern.matcher(string);
        if( m.matches() ){
            String prefix = m.group(1);
            String intStr = m.group(2);
            if(intStr != null && StringUtils.isNotEmpty(intStr)){
                int number = Integer.parseInt(intStr);
                intStr = Integer.toString(number + 1);
            }else{
                intStr = "";
            }
            updatedString = prefix.concat(intStr);
        }
        return updatedString;
    }

    public static void generateNGComponentProperty(Resource ngContentResource){
        ModifiableValueMap valueMap = ngContentResource.adaptTo(ModifiableValueMap.class);
        if( !valueMap.containsKey(NGConstants.NG_COMPONENT_PROPERTY) ){
            valueMap.put(NGConstants.NG_COMPONENT_PROPERTY, true);
        }
    }
}
