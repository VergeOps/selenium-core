package com.automation.selenium.core.listener;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * 
 * An ExtentManager is a class to handle the extent reports
 * @author Amanda Adams
 *
 */
public class ExtentManager {

  private static ExtentReports extent;
  private static String reportFileName = "execution_report.html";
  private static String path = System.getProperty("user.dir") + "/html_report";
  private static String reportFileLoc = path + "/" + reportFileName;
  private static ExtentHtmlReporter htmlReporter;

  /**
   * Static method to get the instance of the extent manager
   * 
   * @return the instance of the manager
   */
  public synchronized static ExtentReports getInstance() {
    if (extent == null)
      createInstance();
    return extent;
  }

  private synchronized static ExtentReports createInstance() {

    createReportPath(path);

    htmlReporter = new ExtentHtmlReporter(reportFileLoc);
    htmlReporter.config().setTheme(Theme.DARK);
    htmlReporter.config().setDocumentTitle(reportFileLoc);
    htmlReporter.config().setEncoding("utf-8");
    htmlReporter.config().setReportName(reportFileLoc);

    extent = new ExtentReports();
    extent.attachReporter(htmlReporter);

    return extent;
  }

  /**
   * Retrieve the HTML for the Extent Reporter
   * @return ExtentHtmlReporter The HTML of the Report
   */
  public static ExtentHtmlReporter getHtmlReporter() {
    return htmlReporter;
  }

  private static void createReportPath(String path) {
    File testDirectory = new File(path);
    if (!testDirectory.exists()) {
      if (testDirectory.mkdir()) {
        System.out.println("Directory: " + path + " is created!");
      } else {
        System.out.println("Failed to create directory: " + path);
      }
    } else {
      System.out.println("Directory already exists: " + path);
    }
  }
}
