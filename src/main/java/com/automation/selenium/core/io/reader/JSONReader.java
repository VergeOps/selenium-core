package com.automation.selenium.core.io.reader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.automation.selenium.core.io.Res;
import com.automation.selenium.core.io.model.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A class for working with JSON files
 * @author Amanda Adams
 *
 */
public class JSONReader {
	
	private URL folder;
	
	/**
	 * Constructor to create a new JSONReader
	 * 
	 * @param folder the folder name contains the JSON files
	 */
	public JSONReader(String folder) {
		this.folder = Res.getResource(folder);
	}
	
	/**
	 * Method to read all JSON files from a folder recursively and return a list of deserialized 
	 * objects for use in TestNG data providers
	 * 
	 * @param jsonType the class mapping type to load the JSON files
	 * @param <T> the class type
	 * @return the object for use as a data provider 
	 */
	public <T extends JSONObject> Object[][] loadFiles(Class<T> jsonType) {
		
		File file = new File(folder.getFile());
		if (!file.isDirectory()) {
			return null;
		}
		
		List<File> jsonFiles = new ArrayList<File>();
		populateFiles(file, jsonFiles);
		Object[][] files = new Object[jsonFiles.size()][1];
		
		ObjectMapper mapper = new ObjectMapper();
		
		for (int i = 0; i < jsonFiles.size(); i++) {
			T object = null;
			try {
				object = jsonType.newInstance();
				
				object  = mapper.readValue(jsonFiles.get(i), jsonType);
				object.setFileName(jsonFiles.get(i).getParentFile().getName() + "/" + jsonFiles.get(i).getName());
				
				
			} catch (IOException | InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			} finally {
				files[i][0] = object;
			}
		}
		
		return files;
	}
	
	
	/**
	 * Method to populate a list, with the JSON files, recursively 
	 * @param directory the directory containing the JSON files
	 * @param jsonFiles the list that will contain the JSon file names
	 */
	private void populateFiles(File directory, List<File> jsonFiles) {
		for (File possibleJsonFile : directory.listFiles()) {
			if (possibleJsonFile.isDirectory()) {
				populateFiles(possibleJsonFile, jsonFiles);
			} else {
				if (possibleJsonFile.getName().toLowerCase().endsWith(".json")) {
					jsonFiles.add(possibleJsonFile);
				}
			}
		}
	}
	
}