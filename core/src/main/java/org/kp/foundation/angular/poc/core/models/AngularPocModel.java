package org.kp.foundation.angular.poc.core.models;

import com.day.cq.wcm.api.WCMMode;
import com.day.cq.wcm.api.components.Component;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
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

    @Self
    Resource resource;

    @Inject @Source("sling-object") ResourceResolver resourceResolver;
    @Inject @Source("sling-object") Component component;
    @Inject SlingHttpServletRequest request;
    @PostConstruct
    protected void init(){
        if(!WCMMode.fromRequest(request).equals(WCMMode.DISABLED)) {
            NGUtil.doAngularBuild(component, resource, NGConstants.COMPILE_TYPE.AOT);
        }
        //don't do a build for publish.
    }
}
