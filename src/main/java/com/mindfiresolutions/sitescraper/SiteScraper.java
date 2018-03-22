package com.mindfiresolutions.sitescraper;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;



public class SiteScraper implements Runnable {

	private String url;
	private String scrapDirectory;
	private Boolean hashTagFilterEnabled;
	private Boolean twitterAccountFilterEnabled;

	public SiteScraper(String url
			, String scrapDirectory
			, Boolean hashTagFilterEnabled
			, Boolean twitterAccountFilterEnabled) {
		this.url = url;
		this.scrapDirectory = scrapDirectory;
		this.hashTagFilterEnabled = hashTagFilterEnabled;
		this.twitterAccountFilterEnabled = twitterAccountFilterEnabled;
	}

	public void run() {

		System.out.println(Thread.currentThread().getName()+" (Start) message = "+ url); 

		scrap(url);

		System.out.println(Thread.currentThread().getName()+" (End)");//prints thread name

	}

	/**
	 * The entry point for the repository that finds the reviewer data tags.
	 * 
	 * @param urlName url for which data scrap is needed.
	 * @return void.
	 */
	private void scrap(String urlName) {

		Document doc = null;

		try {
			doc = Jsoup.connect(urlName).get();
		} catch (IOException ioe) {
			System.out.println("Not able to read site url: " + urlName);
		} catch (Exception e) {
			System.out.println("Error reading url: " + urlName);
			System.out.println(e.getMessage());
		}

		String fileName = getFileName(scrapDirectory, urlName);		

		if(doc != null) {

			Element table = doc.body();

			if(hashTagFilterEnabled) {
				parseHashTagList(table.text(), fileName);
			}

			if(twitterAccountFilterEnabled) {
				parseTwitterAccountList(table.text(), fileName);			
			}

		}
	}
	
	
	
	/**
	 * Generate file name from filePath and url provided.
	 * 
	 * @param filePath filePath where files need to be created.
	 * @param urlName urlName used for fileName creation.
	 * @return fileName
	 */
	private String getFileName(String filePath, String urlName) {

		String fileName = "";
		URL url = null;
		try {
			url = new URL(urlName);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}

		if(url != null) {
			fileName = filePath + System.getProperty( "file.separator" ) + url.getAuthority().replace(".", "") + ".txt";
		}

		return fileName;
	}
	
	/**
	 * Parse twitter account from search text and write it to file.
	 * 
	 * @param searchText search text in which parsing needed.
	 * @param fileName file name to create file.
	 * @return void
	 */
	private void parseTwitterAccountList(String searchText, String fileName) {

		List<String> twitterAccountList = new ArrayList<String>();

		// Find and create list of Twitter Account search
		String twitterAccountPattern = "\\@[a-zA-Z0-9-_.]+";
		Pattern regPat = Pattern.compile(twitterAccountPattern); 
		Matcher matcher = regPat.matcher(searchText); 

		regPat = Pattern.compile(twitterAccountPattern); 
		matcher = regPat.matcher(searchText);

		while (matcher.find()) {    
			twitterAccountList.add(matcher.group());
		}

		// Write in file if Twitter Account found
		if(!twitterAccountList.isEmpty()) {
			FileUtils.wtiteToFile(fileName, twitterAccountList);
		}
	}
	
	/**
	 * Parse hash tag from search text and write it to file.
	 * 
	 * @param searchText search text in which parsing needed.
	 * @param fileName file name to create file.
	 * @return void
	 */
	private void parseHashTagList(String searchText, String fileName) { 

		List<String> hashTagList = new ArrayList<String>();

		// Find and create list of Hash Tag search
		String hashTagPattern = "\\#[a-zA-Z0-9-_.]+";		
		Pattern regPat = Pattern.compile(hashTagPattern); 
		Matcher matcher = regPat.matcher(searchText);

		while (matcher.find()) {    
			hashTagList.add(matcher.group());
		}

		// Write in file if Hash Tag found
		if(!hashTagList.isEmpty()) {
			FileUtils.wtiteToFile(fileName, hashTagList);
		}
	}
}
