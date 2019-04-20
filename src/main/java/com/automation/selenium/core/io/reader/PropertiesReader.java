package com.automation.selenium.core.io.reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import com.automation.selenium.core.io.Res;

/**
 * PropertiesReader a class for reading properties files
 * @author Amanda Adams
 *
 */
public class PropertiesReader {
	
    /**
     * Method to read a file and retrieve its properties
     * @param fileName fileName
     * @return Properties properties
     */
    public static Properties read(String fileName) {
        URL file = Res.getResource(fileName);
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = file.openStream();
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return prop;
    }
}