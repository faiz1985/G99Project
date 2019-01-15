package G99;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;


public class G99Class {
	WebDriver driver;
	String BrowserDriver, FireFoxDriverPath, ChromeDriverPath, appURL, appUname, appPwd, verifyLoginTitle, invalidCredentialsError, successfulLoginMsg;
	
	@BeforeSuite
	@Parameters({"browserDriver", "firefoxDriverPath","chromeDriverPath"})
	public void dataSource(String browserDriver, String firefoxDriverPath, String chromeDriverPath) throws IOException, BiffException {
		
		//These are from TestNG.XML file
		BrowserDriver=browserDriver;
		FireFoxDriverPath=firefoxDriverPath;
		ChromeDriverPath=chromeDriverPath;
			
		//These are from Util.Java file
		verifyLoginTitle=Util.verifyLoginTitle;
		invalidCredentialsError=Util.invalidCredentialsError;
		successfulLoginMsg=Util.successfulLoginMsg;
		
		//These are from config.properties file
		FileInputStream fis = new FileInputStream(Util.configProp);
		Properties prop = new Properties();
		prop.load(fis);
		appURL=prop.getProperty("URL");
		appUname=prop.getProperty("uname");
		appPwd=prop.getProperty("pwd");
		
		//These are from an excel file - JXL will work only with .xls format; to use .xlsx format, work with POI library
		Workbook xlWB = Workbook.getWorkbook(new File(Util.FILE_PATH));
		Sheet xlSheet = xlWB.getSheet(Util.SHEET_NAME);
		
		int rowCount=xlSheet.getRows();
		int colCount=xlSheet.getColumns();
		
		String inputData[][] = new String [rowCount][colCount];
		
		for(int i=0;i<rowCount;i++) {
			for(int j=0;j<colCount;j++) {
				jxl.Cell c = xlSheet.getCell(j, i);
				inputData[i][j] = c.getContents();
				//System.out.print(inputData[i][j] + "\t");
			}
			//System.out.println("\n");
		}
		xlWB.close();
		
		for(int i=0;i<inputData.length; i++) {
			appUname=inputData[i][0];
			appPwd=inputData[i][1];
			//System.out.println("Username: " + unameExcel + ", Password: " + pwdExcel);
		}
	}
	
//@Parameters({"FireFoxDriver", "driverPath"})
  @BeforeClass()
  public void initialiseBrowser() {
	  
	  if(BrowserDriver.contains("chrome")) {
		  System.setProperty(BrowserDriver, ChromeDriverPath);
		  driver = new ChromeDriver();
	  }
	  
	  if(BrowserDriver.contains("gecko")) {
		  System.setProperty(BrowserDriver, FireFoxDriverPath);
	  	  driver = new FirefoxDriver();
	  }
	  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //wait for a certain amount of time before throwing an exception that it cannot find the element on the page
  }
	
	//@Parameters({"AUTLink", "uname", "pwd"})
	@Test
	public void verifyLogin() {
		driver.get(appURL);
		loginPageObjects LPO = new loginPageObjects(driver);
		LPO.enterUname(appUname);
		LPO.enterPassword(appPwd);
		LPO.clickLoginBtn();
		//Assert.assertEquals(driver.getTitle(), Util.verifyLoginTitle);		
		
		if(driver.getTitle().equals(verifyLoginTitle)) {
			System.out.println(successfulLoginMsg);
		}
	/*	else if(driver.switchTo().alert().getText().contains(invalidCredentialsError)) {
			System.out.println("Invalid User ID or Password Entered");
			driver.close();
		}*/
	}
	
	@AfterClass
	public void closeBrowser() {
		driver.close();
	}
}
