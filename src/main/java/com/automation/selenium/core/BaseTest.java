package com.automation.selenium.core;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.automation.selenium.core.assertions.SoftAssert;
import com.automation.selenium.core.assertions.SoftAssertSelenium;
import com.automation.selenium.core.io.model.JSONObject;
import com.automation.selenium.core.listener.ExtentManager;
import com.automation.selenium.core.utility.Constants;
import com.automation.selenium.core.utility.PropertyConstants;
import com.automation.selenium.core.utility.ThreadedItems;
import com.aventstack.extentreports.ExtentTest;

/**
 * 
 * Abstract base test class that Selenium TestNG tests must extend
 * @author Amanda Adams
 *
 */
public abstract class BaseTest implements ITest {

	final static Logger logger = LogManager.getLogger();
	protected static ThreadLocal<ThreadedItems> threadDriver;
	
	public static Random rand;

	/**
	 * Get random instance
	 * 
	 * @return Random Instance of Random to use in getting random data
	 */
	public Random getRandom() {
		if (rand == null)
			rand = new Random();

		return rand;
	}
	
	/**
	 * Method to initialize the test
	 * 
	 * @param method Method information for the current test method
	 * @param testData An Object Array with the data used to drive the current test
	 */
	protected synchronized void initializeTest(Method method, Object[] testData) {
		
		initializeThreadLocal();
		
		String params = "";
		if (testData != null) {

			for (int i = 0; i < testData.length && params.length() < 225; i++) {
				if (testData[i] != null)
					if (testData[i] instanceof JSONObject) {
						String testName = ((JSONObject)testData[i]).getTestName();
						if (testName == null || testName.isEmpty()) {
							testName = ((JSONObject)testData[i]).getFileName();
							if (testName.contains("/")) {
								testName = testName.substring(testName.lastIndexOf("/") + 1);
							}
						}
						params +=  testName + ", ";
					} else {
						params += testData[i].toString() + ", ";
					}
				else
					params += "NULL, ";
			}

		}
		if (params.length() >= 2)
			params = params.substring(0, params.length() - 2);
		
		String methodTestName = this.extractMethodTestName(method);

		threadDriver.get().setTestName(
				String.format("%s: %s", methodTestName, params));
		
		ExtentTest extentTest = ExtentManager.getInstance().createTest(String.format("%s: %s", methodTestName, params));
		
		threadDriver.get().setTest(extentTest);
		threadDriver.get().setSoftAssert(new SoftAssert(new SoftAssertSelenium()));

	}
	
	private String extractMethodTestName(Method method) {
		
		String methodTestName = method.getAnnotationsByType(Test.class)[0].testName();
		
		if (methodTestName == null || methodTestName.isEmpty()) {
			methodTestName = method.getName()
					.replaceAll("([A-Z][a-z]+)", " $1")
		            .replaceAll("([A-Z][A-Z]+)", " $1")
		            .replaceAll("([^A-Za-z ]+)", " $1")
		            .trim();
			methodTestName = methodTestName.substring(0, 1).toUpperCase() + methodTestName.substring(1);
		}
		return methodTestName;
	}

	/**
	 * Static method to get the ExtentTest object from the threadDriver
	 * 
	 * @return The ExtentText object from the Extent reporting framework
	 */
	public static ExtentTest getExtentTest() {
		return threadDriver.get().getTest();
	}

	/**
	 * Method to get the current test's name
	 * 
	 * @return String The name of the current test
	 */
	public String getTestName() {
		String name = "";
		if (threadDriver != null) {
			ThreadedItems threadedItems = threadDriver.get();
			if (threadedItems != null) {
				name = threadedItems.getTestName();
			}
		}
		
		if (name == null) {
			name = "";
		}

		return name;
	}
	
	/**
	 * Method to pause for a specified number of seconds
	 * Should only be used when absolutely needed
	 * 
	 * @param seconds The time in seconds
	 */
	public void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Static method to get the SoftAssert instance
	 * @return SoftAssert the instance of the Soft Assert class
	 */
	public static SoftAssert getSoftAssert() {
		return threadDriver.get().getSoftAssert();
	}
	
	/**
	 * Method to call the assertAll method on our SoftAssert object
	 * 
	 */
	public void assertAll() {
		threadDriver.get().getSoftAssert().assertAll();
	}

	/**
	 * Get the web driver -- create if it doesn't exist
	 * 
	 * @return WebDriver
	 */
	public static WebDriver getDriver() {

		initializeThreadLocal();

		if (threadDriver.get().getDriver() == null) {
			threadDriver.get().setDriver(createDriver());
		}

		return threadDriver.get().getDriver();
	}
	
	private static WebDriver createDriver() {
		
	    String browser = getBrowserType();
		String local = System.getProperty("run.local");

		WebDriver driver = null;

		if ("chrome".equals(browser)) {

			ChromeOptions options = new ChromeOptions();
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "ignore");

			if (!"false".equals(local)) {
				driver = new ChromeDriver(options);
			} else {
		         driver = createRemoteDriver(options, DesiredCapabilities.chrome());
			}
			
		} else if (browser != null && browser.startsWith("ie")) {
			
		    InternetExplorerOptions options = new InternetExplorerOptions();
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "ignore");

			if (!"false".equals(local)) {
				driver = new InternetExplorerDriver(options);
			} else {
		         driver = createRemoteDriver(options, DesiredCapabilities.internetExplorer());
			}
			
		} else if (browser != null && browser.startsWith("firefox")) {
			
		    FirefoxOptions options = new FirefoxOptions();
			options.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, "ignore");

			if (!"false".equals(local)) {
				driver = new FirefoxDriver(options);
			} else {
		         driver = createRemoteDriver(options, DesiredCapabilities.firefox());
			}
			
		}
		
		getExtentTest().info("Browser: " + browser);
		
		driver.manage().timeouts().implicitlyWait(Constants.WEBDRIVER_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		return driver;
	}

	/**
	 * Method to stop Web driver and clean up after test execution
	 * 
	 * @param result The result of a test
	 */
	protected void killWebDriver(ITestResult result) {
		
		boolean isSauce = false;
	  
		if (System.getProperty(PropertyConstants.SAUCE_USER) != null && !System.getProperty(PropertyConstants.SAUCE_USER).equals("")) {
			isSauce = true;
			((JavascriptExecutor)getDriver()).executeScript("sauce:job-result=" + (result.isSuccess() ? "passed" : "failed"));
	        
		}
		
		if (isSauce || (System.getProperty(PropertyConstants.CLOSE_BROWSER) == null || !System.getProperty(PropertyConstants.CLOSE_BROWSER).equalsIgnoreCase("false"))) {
	
			threadDriver.get().getDriver().close();
			threadDriver.get().getDriver().quit();
	
			threadDriver.get().setDriver(null);
			
	  }
	}
	
	/**
	 * Method to flush report after every test
	 */
	@AfterTest(alwaysRun = true)
	public synchronized void afterTest() {
		ExtentManager.getInstance().flush();
	}
	
	/**
	 * Method to set Sauce build ID before suite
	 */
	@BeforeSuite(alwaysRun = true)
	public synchronized void beforeSuite() {
		if (System.getProperty(PropertyConstants.SAUCE_USER) != null && !System.getProperty(PropertyConstants.SAUCE_USER).equals("")) {
			System.setProperty(PropertyConstants.SAUCE_BUILD, "Selenium Sample Build " + System.currentTimeMillis());
	        
		}
	}

	private static void initializeThreadLocal() {

		if (threadDriver == null) {
			threadDriver = new ThreadLocal<ThreadedItems>();
		}

		if (threadDriver.get() == null) {
			ThreadedItems items = new ThreadedItems();
			threadDriver.set(items);
		}

	}

	private static String getBrowserType() {
		String browser = "chrome";

		if (System.getProperty("browser.type") != null && !System.getProperty("browser.type").equals(""))
			browser = System.getProperty("browser.type");

		return browser;
	}
	
	private static RemoteWebDriver createRemoteDriver (MutableCapabilities options, DesiredCapabilities caps) {
	    
	    RemoteWebDriver driver = null;
	    
	    String sauceUser = System.getProperty(PropertyConstants.SAUCE_USER);
	    String sauceAccessKey = System.getProperty(PropertyConstants.SAUCE_ACCESS_KEY);
	    
	    String hubURL = "http://localhost:4444/wd/hub";
	    
	    boolean isSauce = false;
	    
	    if ((sauceUser != null && !sauceUser.isEmpty()) ||
    			(sauceAccessKey != null && !sauceAccessKey.isEmpty())) {
	    	isSauce = true;
	    	getExtentTest().info("Running in Sauce");
	    	
	    	hubURL = "http://ondemand.saucelabs.com:80/wd/hub";
	    	
	    	caps.setCapability("username", sauceUser);
	        caps.setCapability("accessKey", sauceAccessKey);

	        caps.setCapability("maxDuration", 3600);
	        caps.setCapability("commandTimeout", 600);
	        caps.setCapability("idleTimeout", 1000);

	    	caps.setCapability("platform", "Windows 10");
	    	
	    	caps.setCapability("build", System.getProperty(PropertyConstants.SAUCE_BUILD));
	    	
	    	caps.setCapability("project", "Selenium Automation");
	    	caps.setCapability("name", threadDriver.get().getTestName());
	    	
	    	String browserType = getBrowserType();
	    	
	    	if (browserType.equals("chrome")) { 
	    		caps.setCapability("browserName", "Chrome");
		    	caps.setCapability("version", "73.0");
		    	
	    	} else if (browserType.startsWith("ie")) {
	    		
	    		String versionNumber = "11.0";
	    		if (!browserType.replaceAll("[^0-9.]+", "").isEmpty()) {
	    			versionNumber = browserType.replaceAll("[^0-9.]+", "");
	    		}
	    		
	    		caps.setCapability("version", versionNumber);
	    	} else if (browserType.startsWith("firefox")) { 
	    		caps.setCapability("version", "66.0");
		    	
	    	}
	    }
	    
	    try {
	    	System.out.println("Attempting connection to " + hubURL);
	    	driver = new RemoteWebDriver(new URL(hubURL), caps);
	    	
			driver.setFileDetector(new LocalFileDetector());
	    	
	    	if (isSauce) {
	    		SessionId sessionId = driver.getSessionId();
	    		String authKey = sauceUser + ":" + sauceAccessKey;
	    		getExtentTest().info("SessionID: " + sessionId.toString());
	    		
				try {
					SecretKeySpec sk = new SecretKeySpec(authKey.getBytes(), "HMACMD5"); 
		            Mac mac = Mac.getInstance("HmacMD5");
					mac.init(sk);
					byte[] result = mac.doFinal(sessionId.toString().getBytes());
					byte[] hexBytes = new Hex().encode(result); 
			        authKey = new String(hexBytes, "ISO-8859-1"); 
				} catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
					e.printStackTrace();
				}
	            getExtentTest().info("<script src=\"https://saucelabs.com/job-embed/" + sessionId.toString() + ".js?auth=" + authKey + "\"></script>");
	            getExtentTest().info("<script src=\"https://saucelabs.com/video-embed/" + sessionId.toString() + ".js?auth=" + authKey + "\"></script>");
	    	}
	    	
        } catch (MalformedURLException e) {

            e.printStackTrace();
        }
	    
	    return driver;
	}
	
}