package com.automation.selenium.core.utility;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.SessionId;

import com.automation.selenium.core.assertions.SoftAssert;
import com.aventstack.extentreports.ExtentTest;

/**
 * 
 * A ThreadedItems has resources to pass between classes as needed
 * @author Amanda Adams
 *
 */
public class ThreadedItems {

    private String name;
    private SoftAssert softAssert;
    private ExtentTest test;
    private WebDriver driver;

    /**
     * Method to get the test name
     * 
     * @return A string with the name
     */
    public String getTestName() {
          return name;
    }

    /**
     * Method to set the test name
     * 
     * @param name The test's name
     */
    public void setTestName(String name) {
          this.name = name;
    }

    /**
     * Method to retrieve the SoftAssert
     * @return softAssert SoftAssert
     */
	public SoftAssert getSoftAssert() {
		return softAssert;
	}

	/**
	 * Method to set the SoftAssert
	 * @param softAssert SoftAssert
	 */
	public void setSoftAssert(SoftAssert softAssert) {
		this.softAssert = softAssert;
	}

	/**
	 * Method to retrieve the ExtentTest
	 * @return test ExtentTest
	 */
	public ExtentTest getTest() {
		return test;
	}

	/**
	 * Method to set the ExtentTest
	 * @param test ExtentTest
	 */
	public void setTest(ExtentTest test) {
		this.test = test;
	}
	
	/**
     * Method to get the web driver instance
     * 
     * @return The WebDriver instance
     */
    public WebDriver getDriver() {
          return driver;
    }

    /**
     * Method to set the web driver instance
     * 
     * @param driver The WebDriver to instance
     */
    public void setDriver(WebDriver driver) {
          this.driver = driver;
    }

}