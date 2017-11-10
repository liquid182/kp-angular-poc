package org.kp.foundation.angular.poc.core.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreemarkerUtil {

    private static Logger LOG = LoggerFactory.getLogger(FreemarkerUtil.class);

    private static final String PROPERTIES = "properties";

    public static String doFreemarkerReplacement(String markupString, Map properties, String uuid) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
        final StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate(uuid, markupString);
        cfg.setTemplateLoader(stringTemplateLoader);
        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();
        root.put(PROPERTIES, properties);

        final Writer stringWriter = new StringWriter();

        try {
            final Template fmTemplate = cfg.getTemplate(uuid);
            fmTemplate.process(root, stringWriter);
        }catch(IOException ioe){
            LOG.warn("IO Exception thrown while transpiling freemarker template:",ioe);
        }catch(TemplateException te){
            LOG.warn("Template Exception thrown while transpiling freemarker template:", te);
        }

        return stringWriter.toString();
    }
}
