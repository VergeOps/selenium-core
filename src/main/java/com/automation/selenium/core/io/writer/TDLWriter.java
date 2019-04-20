package com.automation.selenium.core.io.writer;

/**
 * TDLWriter a class for reads tab delimited files
 * @author Amanda Adams
 *
 */
public class TDLWriter extends DelimitedFileWriter {

	/**
	 * Constructor to create a new Tab Delimited Writer
	 * 
	 * @param file the file name of the tab delimited file
	 */
	public TDLWriter(String file) {
		super(file, "\t");
	}
}