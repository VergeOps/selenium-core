package com.automation.selenium.core.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.automation.selenium.core.BaseTest;
import com.automation.selenium.core.assertions.HardAssert;
import com.automation.selenium.core.io.model.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * ExtentListener to create Extent report
 * @author Amanda Adams
 * 
 */
public class ExtentListener implements ITestListener {

	final Logger logger = LogManager.getLogger();

	/**
	 * Creates an empty JSONListener
	 */
	public ExtentListener() {
	}

	/**
	 * Method to add the skipped test to the Report
	 * 
	 * @param skippedTest The result of the skipped test
	 */
	@Override
	public synchronized void onTestSkipped(ITestResult skippedTest) {
		logger.info("Skipped test");
		BaseTest.getExtentTest().info(includeJSONObject(skippedTest));
		BaseTest.getExtentTest().skip(skippedTest.getThrowable());
		HardAssert.assertTrue(false, skippedTest.getThrowable().getMessage());
	}

	/**
	 * Method to add the successful test to the Report
	 * 
	 * @param passedTest The result of the successful test
	 */
	@Override
	public synchronized void onTestSuccess(ITestResult passedTest) {
		logger.info("Passed test");
		BaseTest.getExtentTest().info(includeJSONObject(passedTest));
	}

	/**
	 * Method to add the failed test to the Report
	 * 
	 * @param failingTest The result of the failed test
	 */
	@Override
	public synchronized void onTestFailure(ITestResult failingTest) {
		logger.info("Failed test");
		BaseTest.getExtentTest().info(includeJSONObject(failingTest));
		BaseTest.getExtentTest().fail(failingTest.getThrowable());
	}

	private String includeJSONObject(ITestResult test) {
		Object[] parameters = test.getParameters();
		String jsonReportString = "";

		for (Object parameter : parameters) {
			if (parameter instanceof JSONObject) {

				JSONObject object = (JSONObject) parameter;
				String jsonName = object.getFileName();
				String jsonString = null;

				logger.info("JSON FILE NAME: " + jsonName);
				logger.info("Resulting object " + object);

				ObjectMapper mapper = new ObjectMapper();
				BaseTest.getExtentTest().info("Filename: " + jsonName);

				try {
					jsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				
				logger.info("JSON:" + jsonString);
				
				jsonReportString += "<div data-featherlight='<div style=\"color:#000;padding:10px;font-weight:bold\"><pre>"
						+ jsonString
						+ "</pre></div>' src='' data-src='' style='cursor:pointer;text-decoration:underline'>"
						+ jsonName + "</div>";

			}

		}
		
		return jsonReportString;
	}

	/**
	 * @see org.testng.ITestListener#onTestStart(ITestResult)
	 */
	@Override
	public synchronized void onTestStart(ITestResult result) {

	}

	/**
	 * @see org.testng.ITestListener#onTestFailedButWithinSuccessPercentage(ITestResult)
	 */
	@Override
	public synchronized void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		onTestFailure(result);

	}

	/**
	 * @see org.testng.ITestListener#onStart(ITestContext)
	 */
	@Override
	public synchronized void onStart(ITestContext context) {
		logger.debug("Extent Reports Version 3 Test Suite started!");
	}

	/**
	 * @see org.testng.ITestListener#onFinish(ITestContext)
	 */
	@Override
	public synchronized void onFinish(ITestContext context) {
	}

}