package org.kp.foundation.angular.poc.core.models;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.components.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.kp.foundation.angular.poc.core.util.JCRUtil;
import org.kp.foundation.angular.poc.core.util.NGUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class NGComponent {

    private static Logger LOG = LoggerFactory.getLogger(NGComponent.class);

    private Component component;
    private Resource resource;
    private ValueMap properties;
    private ResourceResolver resourceResolver;
    private Map<String,String> srcFileMap;
    private String ngSrcJcrPath;
    private String selector;
    private String componentFileName;
    private String className;
    private String importComponentPath;
    private String templateTag;
    private String templateFileName;

    public NGComponent(Resource resource){
        this.resource = resource;
        this.component = JCRUtil.getComponentFromContentResource(resource);
        this.properties = resource.getValueMap();
        LOG.debug("Properties:");
        for(String key:properties.keySet()){
            LOG.debug("{}:{}",key,properties.get(key));
        }
        this.resourceResolver = resource.getResourceResolver();
        setNgSrcJcrPath();
        setSrcFileMap();
        setSelector(component.getName());
    }

    public Component getComponent() {
        return component;
    }

    public Resource getResource() {
        return resource;
    }

    public ValueMap getProperties() {
        return properties;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public String getUUID(){
        return NGUtil.generateUUID(resource);
    }

    public String getNgSrcJcrPath(){
        return ngSrcJcrPath;
    }

    private void setSrcFileMap(){
        Map<String , String> fileMap = new HashMap<>();
        String ngSrcPath = ngSrcJcrPath;
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
        srcFileMap = fileMap;
    }

    public Map<String,String> getSrcFileMap(){
        return srcFileMap;
    }

    public void setNgSrcJcrPath(){
        ngSrcJcrPath = component.getPath();
        if( !ngSrcJcrPath.endsWith("/")){
            ngSrcJcrPath = ngSrcJcrPath.concat("/");
        }
        ngSrcJcrPath = ngSrcJcrPath.concat(NGConstants.NG_SRC);
    }

    public String getSelector() {
        return selector;
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public String getComponentFileName() {
        return componentFileName;
    }

    public String getClassName() {
        return className;
    }

    public String getImportComponentPath(){
        return importComponentPath;
    }

    public String getTemplateTag(){
        return templateTag;
    }

    public void setSelector(String selector){
        this.selector = selector;
        this.templateFileName = String.format(NGConstants.TEMPLATE_FILE_FORMAT,selector);
        this.componentFileName = String.format(NGConstants.COMPONENT_FILE_FORMAT, selector);
        this.className = NGUtil.getPascalCaseForHyphen(selector);
        this.importComponentPath = String.format(NGConstants.IMPORT_COMPONENT_FORMAT,selector);
        this.templateTag = String.format(NGConstants.APP_TEMPLATE_TAG_FORMAT,selector);
    }

}
