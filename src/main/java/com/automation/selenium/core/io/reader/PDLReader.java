package com.automation.selenium.core.io.reader;

/**
 * PDLReader a class for reading pipe-delimited files
 * @author Amanda Adams
 *
 */
public class PDLReader extends DelimitedFileReader {
	
    /**
     * Constructor to create a PDLReader
     * @param file file
     */
	public PDLReader(String file) {
		super(file, "|");
	}
}