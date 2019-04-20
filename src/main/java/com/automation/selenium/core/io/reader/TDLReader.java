package com.automation.selenium.core.io.reader;

/**
 * A class for working with tab delimited files
 * @author Amanda Adams
 *
 */
public class TDLReader extends DelimitedFileReader {
	
	/**
	 * Constructor to create a Tab Delimited Reader.
	 * 
	 * @param file the file name of the tab delimited file
	 */
	public TDLReader(String file) {
		super(file, "\t");
	}
}