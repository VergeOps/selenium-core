package com.automation.selenium.core.io;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Res class for handling file-based resources
 * @author Amanda Adams
 *
 */
public class Res {

    private static final String[] DEFAULT_RESOURCE_PATHS = {"/files", "src/main/resources", "src/test/resources"};

    /**
     * Method to get a system resource based on resources path, if it doesn't exists throws an Exception
     * 
     * @param propFile the file name of the resource that we want
     * @return the resource file itself
     */
    public static URL getResource(String propFile) {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(propFile);
        
        if (resource == null) {
          
            for (String resourcePath : DEFAULT_RESOURCE_PATHS) {
                File resFile = new File(resourcePath, propFile);
                if (resFile.isFile() || resFile.isDirectory()) {
                    try {
                        resource = resFile.toURI().toURL();
                        break;
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException("Unable to locate the file");
                    }
                    }
                }
            }
        
        if (resource == null) {
        	String path = System.getProperty("input.file.path");
        	
        	File resFile = new File(path + "/" + propFile);
        	System.out.println("FULL PATH: " + resFile.getAbsolutePath());
            if (resFile.isFile() || resFile.isDirectory()) {
                try {
                    resource = resFile.toURI().toURL();
                } catch (MalformedURLException ex) {
                    throw new RuntimeException("Unable to locate the file");
                }
            }
        	
        }

        if(null == resource) 
            throw new NullPointerException("Resource not found for propFile: " + propFile);
        return resource;
    }
}