package com.automation.selenium.core.analyzer;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * 
 * Class to implements the retry logic on fail
 * @author Amanda Adams
 *
 */
public class Retry implements IRetryAnalyzer {

	private int count = 0;
	private static int maxTry = 0;

	/**
	 * Initialization method
	 * 
	 */
	public void init() {
		String max = System.getProperty("retryOnFailure");
		if (max != null) {
			try {
				maxTry = Integer.parseInt(max);
			} catch (NumberFormatException e) {
			}
		}

	}

	/* (non-Javadoc)
	 * @see org.testng.IRetryAnalyzer#retry(org.testng.ITestResult)
	 */
	@Override
	public boolean retry(ITestResult iTestResult) {
		init();
		if (!iTestResult.isSuccess()) {
			if (count < maxTry) {
				count++;
				return true;
			} else {
				count = 0;
			}
		} else {
			count = 0;
		}
		return false;
	}
}