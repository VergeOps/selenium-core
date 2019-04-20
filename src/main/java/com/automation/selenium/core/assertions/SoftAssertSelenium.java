package com.automation.selenium.core.assertions;

import org.openqa.selenium.WebDriver;
import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;

import com.automation.selenium.core.BaseTest;
import com.automation.selenium.core.utility.Screenshot;

/**
 * 
 * Class to override testNG soft assertion with one that takes a screenshot on soft assertion failure
 * @author Amanda Adams
 *
 */
public class SoftAssertSelenium extends SoftAssert {

	WebDriver driver;

	public SoftAssertSelenium() {
		super();
		driver = BaseTest.getDriver();
	}

	@Override
	public void onAssertFailure(IAssert<?> arg0, AssertionError arg1) {
		Screenshot.doScreenShot(driver);
		super.onAssertFailure(arg0, arg1);
	}

}