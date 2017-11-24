package org.kp.foundation.angular.poc.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.api.components.Component;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.kp.foundation.angular.poc.core.constants.NGConstants;
import org.kp.foundation.angular.poc.core.util.ClientLibraryUtil;
import org.kp.foundation.angular.poc.core.util.CmdLineUtil;
import org.kp.foundation.angular.poc.core.util.FileSystemUtil;
import org.kp.foundation.angular.poc.core.util.NGUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;

/**
 * Created by ryanmccullough on 2017-08-23.
 */
@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class AngularPocModel {

    private static Logger LOG = LoggerFactory.getLogger(AngularPocModel.class);

    private static String uuid;

    @Inject @Required
    //Must match the options for compile type found in the NGConstants Enum ("jit" and "aot" at time of writing)
    String compileTypeStr;


    @Self
    Resource resource;

    private NGConstants.COMPILE_TYPE compileType;
    private String[] clientLibCategories;

    @PostConstruct
    protected void init(){
        compileType = NGConstants.getCompileType(compileTypeStr);
        clientLibCategories = NGUtil.getNGClientLibCategories(getUUID(), compileType);
        NGUtil.generateNGComponentProperty(resource);
    }

    public String getUUID(){
        if (StringUtils.isEmpty(uuid)){
            Resource pageResource = resource.getResourceResolver().adaptTo(PageManager.class).getContainingPage(resource).getContentResource();
            uuid = NGUtil.generateUUID(pageResource);
        }
        return uuid;
    }

    public String[] getClientLibCategories(){
        return NGUtil.getNGClientLibCategories(uuid,compileType);
    }
}
