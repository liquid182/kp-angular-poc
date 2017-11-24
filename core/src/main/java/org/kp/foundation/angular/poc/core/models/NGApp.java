package org.kp.foundation.angular.poc.core.models;

import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.kp.foundation.angular.poc.core.util.NGUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NGApp {
    private List<NGComponent> components;
    private NGConstants.COMPILE_TYPE compileType;
    private ResourceResolver resourceResolver;
    private Resource pageResource;
    private String uuid;
    private String clientLibraryPath;
    private Resource clientLibraryResource;
    private String[] clientLibraryCategories;
    private List<String> componentSelectors;

    public NGApp(Resource pageResource, NGConstants.COMPILE_TYPE compileType){
        this.compileType = compileType;
        this.pageResource = pageResource;
        this.resourceResolver = pageResource.getResourceResolver();
        this.uuid = initUUID();
        this.clientLibraryPath = NGUtil.getNGClientLibPath(uuid);
        this.clientLibraryResource = resourceResolver.getResource(clientLibraryPath);
        this.clientLibraryCategories = NGUtil.getNGClientLibCategories(uuid,compileType);
        components = new ArrayList<>();
        componentSelectors = new ArrayList<>();
    }

    public String getImports(){
        String imports = "";
        for( NGComponent component : components){
            imports = imports.concat(String.format(NGConstants.IMPORT_STRING_FORMAT, component.getClassName(), component.getImportComponentPath()));
        }
        return imports;
    }

    public String getDeclarations(){
        String declarationsStr = NGConstants.DECLARATION_STRING_EMPTY;
        String[] declarations = new String[components.size()];
        for( int i = 0; i < components.size(); i++){
           declarations[i] = components.get(i).getClassName();
        }
        if( declarations.length > 0 ){
            declarationsStr = String.format(NGConstants.DECLARATION_STRING_FORMAT,
                    String.join(",",declarations));
        }
        return declarationsStr;
    }

    public Resource getPageResource(){
        return pageResource;
    }

    public String getUUID(){
        return uuid;
    }

    public void addComponent(NGComponent component){
        component = determineComponentSelector(component);
        components.add(component);
        componentSelectors.add(component.getSelector());

    }

    private NGComponent determineComponentSelector(NGComponent newComponent){
        while(componentSelectors.contains(newComponent.getSelector())){
            String curSelector = newComponent.getSelector();
            newComponent.setSelector(NGUtil.updateStringInteger(curSelector));
        }
        return newComponent;
    }

    public void setCompileType(NGConstants.COMPILE_TYPE compileType){
        this.compileType = compileType;
    }

    public List<NGComponent> getComponents(){
        return components;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public NGConstants.COMPILE_TYPE getCompileType() {
        return compileType;
    }

    public void setResourceResolver(ResourceResolver resourceResolver){
        this.resourceResolver = resourceResolver;
    }

    public Resource getClientLibraryResource() {
        return clientLibraryResource;
    }

    public void setClientLibraryResource(Resource clientLibraryResource) {
        this.clientLibraryResource = clientLibraryResource;
    }

    private String initUUID() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Resource pageContent = pageManager.getContainingPage(pageResource).getContentResource();
        String uuid = NGUtil.generateUUID(pageContent);
        return uuid;
    }

    public String getClientLibraryPath(){
        return clientLibraryPath;
    }

    public String[] getClientLibraryCategories() {
        return clientLibraryCategories;
    }

    public static NGApp fromPageRes(Resource pageRes, NGConstants.COMPILE_TYPE compileType){
        NGApp ngApp = new NGApp(pageRes,compileType);
        return ngApp;
    }
}
