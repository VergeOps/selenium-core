package com.automation.selenium.core.io.reader;

/**
 * CSVReader a class for reading csv files
 * @author Amanda Adams
 *
 */
public class CSVReader extends DelimitedFileReader {
	
	/**
	 * Constructor to create a CSVReader.
	 * 
	 * @param file the file name of the csv
	 */
	public CSVReader(String file) {
		super(file, ",");
	}
}