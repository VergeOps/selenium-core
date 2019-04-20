package com.automation.selenium.core.utility;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.Augmenter;
import org.testng.ITestResult;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;
import com.automation.selenium.core.BaseTest;

/**
 * A class to handle Screenshotting
 * @author Amanda Adams
 */
public class Screenshot {

	/**
	 * Screenshot object constructor
	 */
	public Screenshot() {
	}
	
	/**
     * Calls the doScreenShot method to take a screenshot of the current page
     * 
     * @param driver WebDriver
     */
    public static void doScreenShot(WebDriver driver) {
        doScreenShot(null, driver, null);
    }

	/**
	 * Calls the doScreenShot method to take a screenshot of the current page and record a custom message
	 * 
	 * @param driver WebDriver
	 * @param message A message to print ahead of the screenshot
	 */
	public static void doScreenShot(WebDriver driver, String message) {
		doScreenShot(null, driver, message);
	}

	/**
	 * Calls the doScreenShot method passing the ITestResult and WebDriver to take a screenshot with info based on test info
	 * 
	 * @param test ITestResult
	 * @param driver WebDriver
	 */
	public static void doScreenShot(ITestResult test, WebDriver driver) {
		doScreenShot(test, driver, null);
	}

	/**
	 * Method to take a screenshot
	 * 
	 * @param test ITestResult
	 * @param driver WebDriver
	 * @param message A message to print ahead the report
	 */
	public static void doScreenShot(ITestResult test, WebDriver driver, String message) {
		try {
			if (driver == null) {
				driver = BaseTest.getDriver();
			}
			String screenshotDirectory = "html_report/screenshots";

			String fileName = File.separator + Thread.currentThread().getId() + "_" + System.currentTimeMillis();

			String screenshotAbsolutePath = screenshotDirectory + fileName + ".png";
			File screenshot = new File(screenshotAbsolutePath);
			if (createFile(screenshot)) {
				try {
					writeScreenshotToFile(driver, fileName, screenshotDirectory);
				} catch (ClassCastException weNeedToAugmentOurDriverObject) {
					writeScreenshotToFile(new Augmenter().augment(driver), fileName, screenshotDirectory);
				}
				String fn = "screenshots" + fileName + ".png";
				String img = "<img src='" + fn + "' data-featherlight='" + fn + "' width='10%' data-src='" + fn + "' />";
				if(test != null) {
					switch (test.getStatus()) {
						case ITestResult.FAILURE:
							if(message != null) {
								BaseTest.getExtentTest().fail(message);
							} else {
								BaseTest.getExtentTest().fail("Error");
							}
							BaseTest.getExtentTest().fail(img);
						break;
						case ITestResult.SKIP:
							if(message != null) {
								BaseTest.getExtentTest().skip(message);
							} else {
								BaseTest.getExtentTest().skip("Skip");
							}
							BaseTest.getExtentTest().skip(img);
						break;
						default:
							if(message != null) {
								BaseTest.getExtentTest().info(message);
							} else {
								BaseTest.getExtentTest().info("Screenshot");
							}
							BaseTest.getExtentTest().info(img);
						break;
					}
				} else {
					if(message != null) {
						BaseTest.getExtentTest().info(message);
					} else {
						BaseTest.getExtentTest().info("Screenshot");
					}
					BaseTest.getExtentTest().info(img);
				}
			} else {
				System.err.println("Unable to create " + screenshotAbsolutePath);
			}
		} catch (Exception ex) {
			System.err.println("Unable to capture screenshot...");
			ex.printStackTrace();
		}
	}

	private static void writeScreenshotToFile(WebDriver driver, String fileName, String directory) {
		if(driver instanceof ChromeDriver) {
			Shutterbug.shootPage(driver, ScrollStrategy.WHOLE_PAGE_CHROME, true).withName(fileName).save(directory);
		} else {
			Shutterbug.shootPage(driver, ScrollStrategy.BOTH_DIRECTIONS).withName(fileName).save(directory);
		}
	}

	private static boolean createFile(File screenshot) {
		boolean fileCreated = false;

		if (screenshot.exists()) {
			fileCreated = true;
		} else {
			File parentDirectory = new File(screenshot.getParent());
			if (parentDirectory.exists() || parentDirectory.mkdirs()) {
				try {
					fileCreated = screenshot.createNewFile();
				} catch (IOException errorCreatingScreenshot) {
					errorCreatingScreenshot.printStackTrace();
				}
			}
		}

		return fileCreated;
	}
}
