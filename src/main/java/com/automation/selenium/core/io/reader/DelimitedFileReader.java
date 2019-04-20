package com.automation.selenium.core.io.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;

import com.automation.selenium.core.io.Res;


/**
 * A class for working with delimited files
 * @author Amanda Adams
 *
 */
public abstract class DelimitedFileReader {
	
	private URL file;
	private String delimiter;
	
	/**
	 * Protected constructor
	 * 
	 * @param file the file name of the delimited file
	 * @param delimiter the delimiter of the file
	 */
	protected DelimitedFileReader(String file, String delimiter) {
		this.file = Res.getResource(file);
		this.delimiter = delimiter;
	}
	
	/**
	 * Method to read a file and return an object array for use in TestNG data providers.
	 * 
	 * @return the object for use as a data provider
	 */
	public Object[][] readFile() {
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		FileReader reader = null;
		BufferedReader br = null;
		
		try {
			reader = new FileReader(file.getFile());
			br = new BufferedReader(reader); 
			
			int count = 0;
			
			String s;
			while((s = br.readLine()) != null) {
				if (count != 0) {
					String[] strings = s.split(delimiter);
					ArrayList<String> str = new ArrayList<String>();
					String helper = "";
					for (int i = 0; i < strings.length; i++) {
						if (strings[i].contains("\"") && helper.equals("")) {
							helper = strings[i].replace("\"", "");
						} else if (helper.equals("")) {
							str.add(strings[i]);
						} else if (!helper.equals("") && !strings[i].contains("\"")){
							helper = helper + delimiter + strings[i];
						} else if (!helper.equals("") && strings[i].contains("\"")) {
							helper = helper + delimiter + strings[i].replace("\"", "");
							str.add(helper);
							helper = "";
						}
					}
					list.add(str);
				}
				count++;
			} 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		Object[][] data = new Object[list.size()][list.get(0).size()];
		for (int i = 0; i < list.size(); i++){
			ArrayList<String> subList = list.get(i);
			Object[] inner = new Object[subList.size()];
			for (int j = 0; j < subList.size(); j++) {
				inner[j] = subList.get(j);
			}
			data[i] = inner;
		}
		return data;
	}
}