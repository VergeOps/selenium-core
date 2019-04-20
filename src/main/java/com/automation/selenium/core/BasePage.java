package com.automation.selenium.core;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.selenium.core.assertions.SoftAssert;
import com.automation.selenium.core.utility.Constants;
import com.automation.selenium.core.utility.Screenshot;
import com.aventstack.extentreports.ExtentTest;

/**
 * Base page class for Selenium page objects
 * @author Amanda Adams
 *
 */
public abstract class BasePage {

	private WebDriver driver;
	private WebDriverWait wait;


	/**
	 * Default constructor for pages.
	 * 
	 * @param driver Active Web driver
	 */
	protected BasePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	/**
	 * Get WebDriver
	 * 
	 * @return Active Web driver
	 */
	protected WebDriver getDriver() {
		return driver;
	}

	/**
	 * Method to get a WebElement by its label using the label for field to get the appropriate input field by its id
	 * 
	 * @param label The label for the field
	 * @return WebElement found with the label description
	 */
	protected WebElement getElementByLabelUsingId(WebElement label) {
		return driver.findElement(By.id(label.getAttribute("for")));
	}
	
	/**
	 * Method to get a WebElement by its label using the label for field to get the appropriate input field by its name
	 * 
	 * @param label The label for the field
	 * @return WebElement found with the label description
	 */
    protected WebElement getElementByLabelUsingName(WebElement label) {
        return driver.findElement(By.name(label.getAttribute("for")));
    }

	/**
	 * Method to scroll to an element using script
	 * 
	 * @param element WebElement to scroll to
	 */
	protected void scrollTo(WebElement element) {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
	}

	/**
	 * Method to wait for an element to be clickable
	 * 
	 * @param element WebElement to be clicked
	 */
	protected void waitForClickable(WebElement element) {
		getWait().until(ExpectedConditions.elementToBeClickable(element));
	}

	/**
	 * Method to wait for an element to be visible
	 * 
	 * @param element WebElement to wait to be visible
	 */
	protected void waitForVisible(WebElement element) {
		getWait().until(ExpectedConditions.visibilityOf(element));
	}

	/**
	 * Method to wait for an element to be invisible
	 * 
	 * @param element WebElement to wait to be invisible
	 */
	protected void waitForInvisible(WebElement element) {
		getWait().until(ExpectedConditions.invisibilityOf(element));
	}

	/**
	 * Method to hover over an element
	 * 
	 * @param element WebElement to hover over
	 */
	protected void hover(WebElement element) {
		waitForClickable(element);
		Actions builder = new Actions(driver);
		builder.moveToElement(element).build().perform();
	}

	/**
	 * Method to click an element
	 * 
	 * @param element WebElement to click
	 */
	protected void click(WebElement element) {
		waitForClickable(element);
		element.click();
	}

	/**
	 * Method to double click an element
	 * 
	 * @param element WebElement to double click
	 */
	protected void doubleClick(WebElement element) {
		waitForClickable(element);
		Actions builder = new Actions(driver);
		builder.doubleClick(element).build().perform();
	}

	
	/**
	 * Method to enter text into a form field
	 * 
	 * @param inputText Text to be entered in the input
	 * @param element WebElement to input the text
	 */
	protected void enterText(String inputText, WebElement element) {
		waitForClickable(element);
		element.clear();
		element.sendKeys(inputText);
	}

	/**
	 * Method to get a page's title
	 * 
	 * @return The page title
	 */
	protected String getTitle() {
		return driver.getTitle();
	}

	/**
	 * Method to get the text in an element
	 * 
	 * @param element WebElement to get the text from
	 * @return The text in the WebElement
	 */
	protected String getText(WebElement element) {
		waitForClickable(element);
		return element.getText();
	}

	/**
	 * Method to select a box
	 * 
	 * @param element WebElement for box to be selected
	 */
	protected void selectBox(WebElement element) {
		waitForClickable(element);
		if (!element.isSelected()) {
			element.click();
		}
	}

	/**
	 * Method to unselect a box
	 * 
	 * @param element WebElement for box to be unselected
	 */
	protected void unSelectBox(WebElement element) {
		waitForClickable(element);
		if (element.isSelected()) {
			element.click();
		}
	}

	/**
	 * Method to select an option from a select field by text
	 * 
	 * @param element WebElement representing the select element
	 * @param text The text in the option to select
	 */
	protected void selectByText(WebElement element, String text) {
		waitForClickable(element);
		Select item = new Select(element);
		item.selectByVisibleText(text);
	}

	/**
	 * Method to select an option from a select field by its value
	 * 
	 * @param element WebElement representing the select element
	 * @param value The value of the option to select
	 */
	protected void selectByValue(WebElement element, String value) {

		Select item = new Select(element);
		item.selectByValue(value);
	}
	
	/**
	 * Method to select an option from a select field by its index
	 * 
	 * @param element WebElement representing the select element
	 * @param index The index to select
	 */
	protected void selectByIndex(WebElement element, int index) {
	    Select item = new Select(element);
	    item.selectByIndex(index);
	}

	/**
	 * Method to execute JavaScript
	 * 
	 * @param scriptToExecute The Script to be executed
	 */
	protected void executeJavaScript(String scriptToExecute) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(scriptToExecute);
	}

	/**
	 * Method to click on an element when standard .click doesn't work
	 * 
	 * @param element The WebElement to click
	 */
	protected void specialClick(WebElement element) {
		Actions actions = new Actions(driver);
		actions.moveToElement(element);
		actions.click().build().perform();
	}

	/**
	 * Method to change the system's implicit element wait time
	 * 
	 * @param waitTime The time in seconds
	 */
	protected void changeWait(int waitTime) {
		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Method to reset the system's implicit element wait time to standard
	 */
	protected void resetWait() {
		driver.manage().timeouts().implicitlyWait(Constants.WEBDRIVER_TIMEOUT, TimeUnit.SECONDS);
	}

	/**
	 * Method to accept an alert
	 */
	protected void acceptAlert() {

		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();

		} catch (Exception e) {
			try {
				Alert alert = driver.switchTo().alert();
				alert.accept();
			} catch (Exception ex) {

			}
		}
	}

	/**
	 * Method to refresh the current page
	 */
	protected void refreshPage() {
		driver.navigate().refresh();
	}
	
	/**
	 * Method to pause for a specified number of seconds
	 * Should only be used when absolutely needed
	 * 
	 * @param seconds The time in seconds
	 */
	protected void sleep(int seconds) {

		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {

		}
	}

	
	private WebDriverWait getWait() {
		if (wait == null) {
			wait = new WebDriverWait(driver, Constants.WEBDRIVER_TIMEOUT);
		}

		return wait;
	}

	/**
	 * Method to retrieve the SoftAssert from the BaseTest
	 * @return SoftAssert SoftAssert
	 */
	public static SoftAssert getSoftAssert() {
		return BaseTest.getSoftAssert();
	}

	/**
	 * Method to retrieve the ExtentTest from the BaseTest
	 * @return ExtentTest ExtentTest
	 */
	public static ExtentTest getReporter() {
		return BaseTest.getExtentTest();
	}
	
	/**
	 * Method to take a screenshot with a message input
	 * @param message Message to record with the screenshot
	 */
	public void takeScreenshot(String message) {
        Screenshot.doScreenShot(getDriver(), message);
    }
	
	/**
	 * Method to take a screenshot
	 */
	public void takeScreenshot() {
	    Screenshot.doScreenShot(getDriver(), null);
	}

}
