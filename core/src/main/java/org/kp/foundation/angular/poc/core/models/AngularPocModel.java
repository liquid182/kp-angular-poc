package org.kp.foundation.angular.poc.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.kp.foundation.angular.poc.core.util.CmdLineUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

    private static String baseProjectDir = "angular2-aot-within-AEM";
    private static String outputFile = "dist/main.bundle.js";
    private static String runCmd = "npm run build-aot";

    private String output;

    @PostConstruct
    protected void init(){
        output = CmdLineUtils.runCommand(runCmd,baseProjectDir);
    }

    public String getOutput(){
        return output;
    }
}
