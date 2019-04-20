package com.automation.selenium.core.io.reader;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import com.automation.selenium.core.io.Res;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

/**
 * TemplateReader a class for reads template files
 * @author Amanda Adams
 * 
 */
public class TemplateReader {

    private static final Version TEMPLATE_VERSION = Configuration.VERSION_2_3_28;

    /**
     * Method to read a file to load the templates
     * @param templateName templateName
     * @param fields fields
     * @return result
     */
    public static String readTemplate(String templateName, Map<String, Object> fields) {
        Configuration cfg = new Configuration(TEMPLATE_VERSION);
        StringWriter result = new StringWriter();
        try {
            File file = new File(Res.getResource("templates").getPath());
            cfg.setDirectoryForTemplateLoading(file);
            cfg.setDefaultEncoding("UTF-8");
            Template temp = cfg.getTemplate(templateName);

            BeansWrapper b = new BeansWrapper(TEMPLATE_VERSION);
            fields.put("statics", b.getStaticModels());

            temp.process(fields, result);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            return null;
        }
        return result.toString();
    }
}