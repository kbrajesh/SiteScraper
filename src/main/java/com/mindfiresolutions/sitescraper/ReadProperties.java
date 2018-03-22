package com.mindfiresolutions.sitescraper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	public static String getProperty(String key) {
		Properties prop = new Properties();
    	InputStream input = null;
    	String value = "";
    	
    	try {
        
    		String filename = "config.properties";
    		input = ReadProperties.class.getClassLoader().getResourceAsStream(filename);
    		if(input == null){
    	            System.out.println("Sorry, unable to find " + filename);
    		    return "";
    		}

    		//load a properties file from class path, inside static method
    		prop.load(input);
 
            //get the property value and print it out
    		value = prop.getProperty(key);
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
        	if(input!=null){
        		try {
        			input.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }
    	return value;
	}
}
