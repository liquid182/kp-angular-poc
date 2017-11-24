package org.kp.foundation.angular.poc.core.models;

import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.kp.foundation.angular.poc.core.util.NGUtil;

import java.util.ArrayList;
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
    private String appTemplateHtml;
    private String declarations;
    private String imports;
    /* Initializers */
    public NGApp(Resource pageResource, NGConstants.COMPILE_TYPE compileType) {
        this.compileType = compileType;
        this.pageResource = pageResource;
        this.resourceResolver = pageResource.getResourceResolver();
        this.uuid = initUUID();
        this.clientLibraryPath = NGUtil.getNGClientLibPath(uuid);
        this.clientLibraryResource = resourceResolver.getResource(clientLibraryPath);
        this.clientLibraryCategories = NGUtil.getNGClientLibCategories(uuid, compileType);
        components = new ArrayList<>();
        componentSelectors = new ArrayList<>();
    }

    public static NGApp fromPageRes(Resource pageRes, NGConstants.COMPILE_TYPE compileType) {
        NGApp ngApp = new NGApp(pageRes, compileType);
        return ngApp;
    }
    /* End Initializers */

    /* Getters and Setters */
    public String getAppTemplateHtml() {
        return appTemplateHtml;
    }

    public String getClientLibraryPath() {
        return clientLibraryPath;
    }

    public String[] getClientLibraryCategories() {
        return clientLibraryCategories;
    }

    public Resource getClientLibraryResource() {
        return clientLibraryResource;
    }

    public void setClientLibraryResource(Resource clientLibraryResource) {
        this.clientLibraryResource = clientLibraryResource;
    }

    public NGConstants.COMPILE_TYPE getCompileType() {
        return compileType;
    }

    public void setCompileType(NGConstants.COMPILE_TYPE compileType) {
        this.compileType = compileType;
    }

    public List<NGComponent> getComponents() {
        return components;
    }

    public String getDeclarations() {
        return declarations;
    }

    public String getImports() {
        return imports;
    }

    public Resource getPageResource() {
        return pageResource;
    }

    public ResourceResolver getResourceResolver() {
        return resourceResolver;
    }

    public String getUUID() {
        return uuid;
    }
    /* End Getters and Setters */

    /* Utility Methods */
    public void addComponent(Resource componentResource) {
        addComponent(new NGComponent(componentResource));
    }

    public void addComponent(NGComponent component) {
        component = determineComponentSelector(component);
        components.add(component);
        componentSelectors.add(component.getSelector());
        configAppStrings();
    }

    private void configAppStrings() {
        declarations = NGConstants.DECLARATION_STRING_EMPTY;
        imports = StringUtils.EMPTY;
        appTemplateHtml = StringUtils.EMPTY;

        String[] declarationList = new String[components.size()];
        for (int i = 0; i < components.size(); i++) {
            NGComponent component = components.get(i);
            declarationList[i] = component.getClassName();
            imports = imports.concat(String.format(NGConstants.IMPORT_STRING_FORMAT, component.getClassName(),
                    component.getImportComponentPath()));
            appTemplateHtml = appTemplateHtml.concat(component.getTemplateTag()).concat("\n");
        }
        if (declarationList.length > 0) {
            declarations = String.format(NGConstants.DECLARATION_STRING_FORMAT, String.join(",", declarationList));
        }
    }

    private NGComponent determineComponentSelector(NGComponent newComponent) {
        while (componentSelectors.contains(newComponent.getSelector())) {
            String curSelector = newComponent.getSelector();
            newComponent.setSelector(NGUtil.updateStringInteger(curSelector));
        }
        return newComponent;
    }

    private String initUUID() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Resource pageContent = pageManager.getContainingPage(pageResource).getContentResource();
        String uuid = NGUtil.generateUUID(pageContent);
        return uuid;
    }

}
