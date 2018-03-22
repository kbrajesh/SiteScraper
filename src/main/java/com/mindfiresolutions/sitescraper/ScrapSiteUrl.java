package com.mindfiresolutions.sitescraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScrapSiteUrl {

	public static void main(String[] args) { 
		executeScrap();
	}
	
	private static void executeScrap() {
		Integer maxPoolSize = Integer.parseInt(ReadProperties.getProperty("maxPoolSize"));
		String scrapDirectory = ReadProperties.getProperty("scrapDirectory");
		Boolean hashTagFilterEnabled = Boolean.parseBoolean(ReadProperties.getProperty("hashTagFilterEnabled"));
		Boolean twitterAccountFilterEnabled = Boolean.parseBoolean(ReadProperties.getProperty("twitterAccountFilterEnabled"));
		
		// Clean Directory
		FileUtils.deleteFiles(scrapDirectory);
		
		ExecutorService executor = Executors.newFixedThreadPool(maxPoolSize);

		List<String> urlList = readUrlsFromFile();

		for(String url : urlList) {
			Runnable worker = new SiteScraper(url, scrapDirectory, hashTagFilterEnabled, twitterAccountFilterEnabled);
			executor.execute(worker); //calling execute method of ExecutorService  
		}

		executor.shutdown();  
		while (!executor.isTerminated()) {      }  

		System.out.println("Finished all threads");
	}
	
	/**
	 * Read urls from file and create list of String. Will skip empty lines.
	 * File path will read from config file.
	 * 
	 * @return List<String> list of url string
	 */
	private static List<String> readUrlsFromFile() {
		List<String> urls = new ArrayList<String>();

		String siteUrlFile  = ReadProperties.getProperty("urlFilePath");

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(siteUrlFile));
			String line = reader.readLine();
			while (line != null) {
				// read next line
				line = reader.readLine();
				if(line != null && !line.isEmpty()) {
					urls.add(line);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  finally {
			try {
				if(reader != null) {
					reader.close();
				}

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return urls;
	}
}
