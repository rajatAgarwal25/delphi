/**
 * 
 */
package com.proptiger.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {

    private static String     filename = "config.properties";
    private static Properties prop     = null;

    static {
        prop = new Properties();
        InputStream input = null;
        try {
            input = PropertyUtil.class.getClassLoader().getResourceAsStream(filename);
            prop.load(input);
        }
        catch (IOException e) {
            System.err.println("Could not load properties file." + e.getMessage());
            e.printStackTrace(System.err);
        }
        finally {
            if (input != null) {
                try {
                    input.close();
                }
                catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    public static String getPropertyValue(String propertyKey) {
        return prop.getProperty(propertyKey);
    }

}
