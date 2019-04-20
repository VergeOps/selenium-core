package com.automation.selenium.core.io.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import com.automation.selenium.core.io.Res;
/**
 * 
 * Writes a File delimited with a specified character
 * @author Amanda Adams
 *
 */
public abstract class DelimitedFileWriter {
	
	private URL file;
	private String delimiter;
	
	/**
	 * Constructor to create a new Delimited File Writer
	 * 
	 * @param file the file name of the file
	 * @param delimiter the delimiter used for the file
	 */
	protected DelimitedFileWriter(String file, String delimiter) {
		this.file = Res.getResource(file);
		this.delimiter = delimiter;
	}
	
	/**
	 * Method to write a file with a flag for whether or not to include headers in the end file.
	 * 
	 * @param headers a list of headers to be written
	 * @param data a list of maps containing the data to be written
	 * @param writeHeaders a flag to, or not to, include the headers 
	 */
	public void writeFile(List<String> headers, List<HashMap<String, String>> data, boolean writeHeaders) {
		
		if (headers == null || headers.size() == 0) {
			return;
		}
		
		if (!writeHeaders && (data == null || data.size() == 0)) {
			return;
		}
			
		FileWriter writer = null;
		BufferedWriter bw = null;
		
		try {
			writer = new FileWriter(file.getFile());
			bw = new BufferedWriter(writer); 
			
			if (writeHeaders) {
				String headerline = "";
				for (String header : headers) {
					if (header.contains(delimiter)) {
						header = "\"" + header + "\"";
					}
					headerline += header + delimiter;
				}
				
				headerline = headerline.substring(0, headerline.lastIndexOf(delimiter));
				bw.write(headerline);
				bw.newLine();
			}
			
			if (data != null && data.size() != 0) {
				for (HashMap<String, String> values : data) {
					String dataline = "";
					for (String header : headers) {
						String item = "";
						if (values.containsKey(header)) {
							item = values.get(header);
							if (item.contains(delimiter)) {
								item = "\"" + item + "\"";
							}
						} else {
							item = "";
						}
						dataline += item + delimiter;
					}
					dataline = dataline.substring(0, dataline.lastIndexOf(delimiter));
					bw.write(dataline);
					bw.newLine();
				}
			}
			
			bw.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
	}
	
	/**
	 * Method to write a file including headers
	 * 
	 * @param headers a list of headers to be written
	 * @param data a list of maps containing the data to be written
	 */
	public void writeFile(List<String> headers, List<HashMap<String, String>> data) {
		writeFile(headers, data, true);
	}
}