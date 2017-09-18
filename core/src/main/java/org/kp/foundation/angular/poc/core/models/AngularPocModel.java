package org.kp.foundation.angular.poc.core.models;

import com.day.cq.wcm.api.components.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.kp.foundation.angular.poc.core.constants.AngularConstants;
import org.kp.foundation.angular.poc.core.util.ClientLibraryUtil;
import org.kp.foundation.angular.poc.core.util.CmdLineUtil;
import org.kp.foundation.angular.poc.core.util.FileSystemUtil;
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

    //TODO: Get rid of all or most of these variables - Determine automatically (ideally), or read from component props.
    private static String AOT_CLIENTLIB_PATH = "/etc/designs/kp-angular-poc/clientlib-angular-aot";
    private static String JIT_CLIENTLIB_PATH = "/etc/designs/kp-angular-poc/clientlib-angular-jit";
    private static String baseProjectDir = "angular2-aot-within-AEM";
    //

    private static String compiledAOTFilePath = baseProjectDir + AngularConstants.AOT_DIST + AngularConstants.COMPILED_FILENAME;
    private static String compiledJITFilePath = baseProjectDir + AngularConstants.JIT_DIST + AngularConstants.COMPILED_FILENAME;



    private String compiledAOTFile;
    private String clientlibAOT;
    private String clientlibJIT;

    @PostConstruct
    protected void init(){
        clientlibAOT = String.format(AngularConstants.CLIENTLIB_CATEGORY_REGEX, component.getName(), AngularConstants.AOT);
        clientlibJIT = String.format(AngularConstants.CLIENTLIB_CATEGORY_REGEX, component.getName(), AngularConstants.JIT);

        compileAOT();
    }

    private void compileAOT(){
        setCompiledAOTFile(compileCMD(AngularConstants.BUILD_AOT_CMD, compiledAOTFilePath));
        writeClientLib(AOT_CLIENTLIB_PATH);
    }

    private void compileJIT(){
        setCompiledAOTFile(compileCMD(AngularConstants.BUILD_JIT_CMD, compiledJITFilePath));
        writeClientLib(JIT_CLIENTLIB_PATH);
    }

    private String compileCMD(String cmd, String compiledFile){
        CmdLineUtil.runCommand(cmd, baseProjectDir);
        return FileSystemUtil.readFile(compiledFile);
    }

    private void setCompiledAOTFile(String content){
        compiledAOTFile = content;
    }

    public String getCompiledAOTFile(){
        return compiledAOTFile;
    }

    private void writeClientLib(String clientLibPath) {
        Node clientLibNode = ClientLibraryUtil.createClientLibrary(clientLibPath,clientlibAOT,null,resourceResolver);
        ClientLibraryUtil.addJavaScriptToClientLibrary(clientLibNode, AngularConstants.COMPILED_FILENAME, getCompiledAOTFile(), true, true);
    }
}
