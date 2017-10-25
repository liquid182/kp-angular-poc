package org.kp.foundation.angular.poc.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;

public class ClientLibrary {

    private String[] categories = null;
    private String[] dependencies = null;
    private String[] embeds = null;
    private Resource resource = null;

    public static String DEPENDENCIES_PROP = "dependencies";
    public static String EMBED_PROP = "embed";
    public static String CATEGORIES_PROP = "categories";

    public ClientLibrary(Resource resource, String[] categories, String[] dependencies, String[] embeds) {
        this.categories = categories;
        this.embeds = embeds;
        this.resource = resource;
        this.dependencies = dependencies;
    }

    public ClientLibrary(Resource resource, String[] categories, String[] dependencies) {
        this.categories = categories;
        this.resource = resource;
        this.dependencies = dependencies;
    }

    public ClientLibrary(Resource resource, String[] categories) {
        this.categories = categories;
        this.resource = resource;
    }

    public ClientLibrary(Resource resource) {
        this.resource = resource;
    }

    public ClientLibrary(){}

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public void setCategory(String category){
        this.categories = new String[]{category};
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }

    public String[] getEmbeds() {
        return embeds;
    }

    public void setEmbeds(String[] embeds) {
        this.embeds = embeds;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getPath(){
        return resource.getPath();
    }

    public static ClientLibrary fromExistingResource(Resource resource){
        ClientLibrary clientLibrary = new ClientLibrary(resource);
        ValueMap props = resource.getValueMap();
        clientLibrary.setDependencies(props.get(DEPENDENCIES_PROP, String[].class));
        clientLibrary.setEmbeds(props.get(EMBED_PROP,String[].class));
        clientLibrary.setCategories(props.get(CATEGORIES_PROP,String[].class));
        clientLibrary.setResource(resource);
        return clientLibrary;
    }
}
