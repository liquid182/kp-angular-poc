package org.kp.foundation.angular.poc.core.util;

import com.day.cq.wcm.api.components.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import javax.jcr.Node;
import javax.jcr.nodetype.NodeType;
import java.io.File;
import java.util.Iterator;

public class NGUtil {

    public String doAngularBuild(Component component, String relativeSrcDir, ResourceResolver resourceResolver){
        String componentPath = component.getPath();
        if( relativeSrcDir != null && !relativeSrcDir.startsWith("/") ){
            relativeSrcDir += "/";
        }

        String srcPath = componentPath + relativeSrcDir;
        Resource srcRes = resourceResolver.resolve(srcPath);
        File fsDir = FileSystemUtil.createTempFileDir();
        return copySrcToFS(srcRes, resourceResolver, fsDir);
    }



    public String copySrcToFS(Resource srcDir, ResourceResolver resourceResolver, File fsDir){
        if( ! Resource.RESOURCE_TYPE_NON_EXISTING.equals(srcDir.getResourceType()) ) {
            Iterator<Resource> resourceIterator = srcDir.listChildren();
            while(resourceIterator.hasNext()){
                Resource child = resourceIterator.next();
                if(child.getResourceType().equals(NodeType.NT_FOLDER)){
                    //CmdLineUtil.createPath();
                }
            }
        }
        return "";
    }

    private String createTempDir(){
        return null;
    }

    private void copyFile(Node fileNode){

    }

}
