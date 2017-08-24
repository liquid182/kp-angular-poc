package org.kp.foundation.angular.poc.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.kp.foundation.angular.poc.core.util.ClientLibraryUtil;
import org.kp.foundation.angular.poc.core.util.CmdLineUtils;
import org.kp.foundation.angular.poc.core.util.JCRUtil;
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

    private static String clientLibPath = "/etc/designs/kp-angular-poc/clientlib-angular-poc";
    private static String baseProjectDir = "angular2-aot-within-AEM";
    private static String compiledFilename = "main.bundle.js";
    private static String outputFile = baseProjectDir + "/dist/" + compiledFilename;
    private static String runCmd = "npm run build-aot";

    private String output;

    @PostConstruct
    protected void init(){
        compileCMD();
    }

    private void compileCMD(){
        CmdLineUtils.runCommand(runCmd, baseProjectDir);
        String jsFileContents = CmdLineUtils.readFile(outputFile);
        output = jsFileContents;
        writeToClientLib();
    }

    public String getOutput(){
        return output;
    }

    private void writeToClientLib() {
        Node clientLibPathNode = JCRUtil.getNode(clientLibPath, resourceResolver);
        ClientLibraryUtil.addJavaScriptToClientLibrary(clientLibPathNode, compiledFilename, output, true, true);
    }
}
