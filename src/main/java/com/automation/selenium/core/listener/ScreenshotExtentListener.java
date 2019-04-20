package com.automation.selenium.core.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;

import com.automation.selenium.core.BaseTest;
import com.automation.selenium.core.utility.Screenshot;

/**
 * Takes a screenshot of the last step reached by execution
 * @author Amanda Adams
 *
 */
public class ScreenshotExtentListener extends ExtentListener {

	final static Logger logger = LogManager.getLogger();

	/**
	 * (non-Javadoc)
	 * @see org.testng.TestListenerAdapter#onTestFailure(org.testng.ITestResult)
	 */
	@Override
	public void onTestFailure(ITestResult failingTest) {
		logger.info("onTestFailure*****************************screenshot");
		if ("headless".equals(System.getProperty("browser.type"))) {
		    super.onTestFailure(failingTest);
			return;
		}

		WebDriver driver = BaseTest.getDriver();
		Screenshot.doScreenShot(failingTest, driver,failingTest.getThrowable().getMessage());
		
		super.onTestFailure(failingTest);
	}
	
	/**
	 * (non-Javadoc)
	 * @see org.testng.TestListenerAdapter#onTestSkipped(org.testng.ITestResult)
	 */
	@Override
	public void onTestSkipped(ITestResult skippedResult) {
    	if ("headless".equals(System.getProperty("browser.type"))) {
    	    super.onTestSkipped(skippedResult);
            return;
        }
    
        WebDriver driver = BaseTest.getDriver();
        Screenshot.doScreenShot(skippedResult, driver);
        
        super.onTestSkipped(skippedResult);
	}

	/**
	 *(non-Javadoc)
     * @see org.testng.TestListenerAdapter#onTestFailure(org.testng.ITestResult)
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		onTestFailure(result);

	}
}
