package com.mindfiresolutions.sitescraper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class FileUtils {
	
	/**
	 * Delete all files within given directory.
	 * 
	 * @param directoryPath directory path to delete all files.
	 * @return boolean value
	 */
	public static boolean deleteFiles(String directoryPath) {
		
		boolean returnValue = true;
		
		if(null == directoryPath || directoryPath.isEmpty()) {
			returnValue = false;
		}
		
		try {
		
			File directory = new File(directoryPath);
			if(!directory.exists()) {
				returnValue = false;
			}
			
			if(!directory.isDirectory()) {
				returnValue = false;
			}
			
			for(File file: directory.listFiles()) {
			    if (!file.isDirectory()) {
			    	returnValue = file.delete();
			    }
			}
			 
			
		} catch (Exception e) {
			returnValue = false;
		}
		
		return returnValue;
	}
	
	/**
	 * Write list of values in file
	 * 
	 * @param fileName fileName in which data will be written.
	 * @param values list of values to write in file.
	 * @return void
	 */
	public static void wtiteToFile(String fileName, List<String> values) {

		FileWriter writer = null;

		try {
			writer = new FileWriter(fileName, false);

			for(String str : values) {
				writer.write(str);
				writer.write(System.getProperty( "line.separator" ));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null) {
					writer.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

}
