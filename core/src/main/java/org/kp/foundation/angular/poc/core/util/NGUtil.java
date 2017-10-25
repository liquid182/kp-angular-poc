package org.kp.foundation.angular.poc.core.util;

import com.day.cq.commons.jcr.JcrUtil;
import com.day.cq.wcm.api.components.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.kp.foundation.angular.poc.core.models.ClientLibrary;

import javax.jcr.Node;
import java.io.File;

public class NGUtil {

    public static boolean doAngularBuild(Component component, Resource componentRes, NGConstants.COMPILE_TYPE compileType) {
        boolean success;
        String componentPath = component.getPath();
        ResourceResolver resourceResolver = componentRes.getResourceResolver();
        String srcPath = NGConstants.BASE_PROJECT_PATH;
        Resource srcRes = resourceResolver.resolve(srcPath);
        File fsDir = FileSystemUtil.createTempFileDir();
        String fsDirPath = fsDir.getPath();
        success = FileSystemUtil.copyJcrToFS(srcRes, fsDir,true);
        doNPMInstall(fsDirPath);
        //temporarily
        doNPMBuild(fsDirPath,compileType);
        String bundleContent = getCompiledBundleContent(fsDirPath, compileType);
        createNGClientLibraryFromFS(bundleContent, componentRes, compileType);
        return success;
    }


    public static String[] getNGClientLibCategories(String componentId, NGConstants.COMPILE_TYPE compileType){
        String[] categories = new String[1];
        categories[0] = String.format(NGConstants.CLIENTLIB_CATEGORY_REGEX, componentId, NGConstants.getCompileTypeString(compileType));
        return categories;
    }

    public static void createNGClientLibraryFromFS(String bundleContents,
                                                            Resource componentRes,
                                                            NGConstants.COMPILE_TYPE compileType){
        String componentId = getComponentIdFromResource(componentRes);
        String clientLibPath = getNGClientLibPath(componentId);

        Node clientLibNode = ClientLibraryUtil.createClientLibrary(clientLibPath,
                                    getNGClientLibCategories(componentId,compileType),
                                    null,
                                    componentRes.getResourceResolver());
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

    public static String getComponentIdFromResource(Resource resource){
        //ToDo: Create or Use UUID instead.
        return resource.getName();
    }

    public static String getNGClientLibPath(String componentId){
        return NGConstants.CLIENTLIB_BASE_DIR + JcrUtil.createValidName(componentId);
    }
}
