package com.automation.selenium.core.io.writer;

/**
 * CSVWriter a class to write csv files
 * @author Amanda Adams
 */
public class CSVWriter extends DelimitedFileWriter {

	/**
	 * Constructor to create a new CSV Writer
	 * 
	 * @param file the file name of the CSV file
	 */
	public CSVWriter(String file) {
		super(file, ",");
	}
}