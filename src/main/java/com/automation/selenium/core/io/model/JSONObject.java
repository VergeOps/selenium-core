package com.automation.selenium.core.io.model;
/**
 * 
 * Object to handle JSON files used as input data
 * @author Amanda Adams 
 *
 */
public abstract class JSONObject {
	
	private String fileName;
	private String testName;

	/**
	 * Method to retrieve the File Name
	 * @return String fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Method to set the File Name
	 * @param fileName fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Retrieve the Test Name
	 * @return testName String with the Test Name
	 */
	public String getTestName() {
		return testName;
	}

	/**
	 * Set the Test Name
	 * @param testName String with the Test Name
	 */
	public void setTestName(String testName) {
		this.testName = testName;
	}
}