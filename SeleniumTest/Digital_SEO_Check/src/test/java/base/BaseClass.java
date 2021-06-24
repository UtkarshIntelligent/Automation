package base;

import org.testng.annotations.Test;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import redirectchecks.ReadData;
import redirectchecks.Reports;
import redirectchecks.Utility;

import java.io.IOException;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

public class BaseClass {

	private WebDriver driver;
	static ExtentReports report;
	static ExtentTest test;
	ChromeOptions options;

	/*
	 * Base Class to run testng suite
	 */
	public BaseClass() throws Exception {
		Reports.startReport();
		report = Reports.getReport();
		test = Reports.getTest();
		options = new ChromeOptions();
	}

	@BeforeSuite
	public void BeforeSuitRun() {
		String os = System.getProperty("os.name");

		if (os.contains("Mac")) {
			options.addArguments("--no-sandbox");
			options.addArguments("--headless");
			options.addArguments("--nogui"); options.addArguments("--disable-gpu");
			System.setProperty("webdriver.chrome.driver", "chromedriver");
		} else {
			System.setProperty("webdriver.chrome.driver", "chromedriverlinux");
			options.addArguments("--no-sandbox");
			options.addArguments("--headless");
			options.addArguments("--nogui"); options.addArguments("--disable-gpu");
		}

	}

	@BeforeMethod
	public void beforeMethod(Method method) {
		driver = new ChromeDriver(options);

		System.out.println("Executing test " + method.getName());

		test = report.createTest(" Executing Test : " + method.getName());
	}

	@AfterMethod
	public void afterMethod(ITestResult result) throws IOException {
		if (result.getStatus() == ITestResult.SUCCESS) {
			System.out.println(result.getName() + " Test Passed");
			test.log(Status.PASS, result.getName() + " Test Passed");
		} else if (result.getStatus() == ITestResult.FAILURE) {
			
			//System.out.println(result.getName() + " Test Failed");
			//String screenShotName = Utility.takeScreenShot(this.driver, result.getName());

			//System.out.println(screenShotName);

			test.log(Status.FAIL, result.getName() + " Test Failed");

			//test.fail("failed", MediaEntityBuilder
			//		.createScreenCaptureFromPath(screenShotName).build());
			//System.out.println("Test Failed: " + result.getName());
			test.log(Status.INFO, result.getThrowable());
		}

		report.flush();
		this.driver.quit();
		
		//System.out.println("check the results " + System.getProperty("user.dir")+"/Report.csv");
	}

	
	@Test(priority = 1)
	public void Digital_SEO_Check() throws Exception {
		
		
		ReadData checkRedirects = new ReadData(this.driver, test);
		
		this.test.log(Status.INFO, "Started Digital_SEO_Check");
		checkRedirects.getSheetDataFromSpreadsheet("digital_seo!A2:G25");
		checkRedirects.verifyStatus("Report");
		this.test.log(Status.INFO, "Finished Digital_SEO_Check");

	}

	
	
}
