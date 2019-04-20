package com.automation.selenium.core.io.writer;

/**
 * PDLWriter a class for reads piped delimited files
 * @author Amanda Adams
 *
 */
public class PDLWriter extends DelimitedFileWriter {

	/**
	 * Constructor to create a new Pipe Delimited Writer
	 * 
	 * @param file the file name of the pipe delimited file
	 */
	public PDLWriter(String file) {
		super(file, "|");
	}
}