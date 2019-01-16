package G99;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
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
	loginPageObjects LPO;
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
		//appUname=prop.getProperty("uname");
		//appPwd=prop.getProperty("pwd");
		
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
				
			}
			//
		}
		xlWB.close();
		
		for(int i=0;i<inputData.length; i++) {
			appUname=inputData[i][0];
			appPwd=inputData[i][1];
			
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
	@Test(priority=1)
	public void verifyLogin() {
		driver.get(appURL);
		LPO = new loginPageObjects(driver);
		
		LPO.enterUname(appUname);
		LPO.enterPassword(appPwd);
		LPO.clickLoginBtn();

        /* Determine Pass Fail Status of the Script
         * If login credentials are correct,  Alert(Pop up) is NOT present. An Exception is thrown and code in catch block is executed	
         * If login credentials are invalid, Alert is present. Code in try block is executed 	    
         */
		
		try{ 
		    
	       	Alert alt = driver.switchTo().alert();
			String actualBoxtitle = alt.getText(); // get content of the Alter Message
			alt.accept();
			/*if (actualBoxtitle.contains(Util.invalidCredentialsError)) { // Compare Error Text with Expected Error Value
				System.out.println(Util.invalidCredentialsError);
			}
			*/
			Assert.assertEquals(actualBoxtitle, Util.invalidCredentialsError);
		}    
	    catch (NoAlertPresentException Ex){ 
	    	String actualTitle = driver.getTitle();
			// On Successful login compare Actual Page Title with Expected Title
			/*if (actualTitle.contains(verifyLoginTitle)) {
				System.out.println(successfulLoginMsg);
			}
			*/
			Assert.assertEquals(actualTitle, verifyLoginTitle);
        } 
	}
	
	@Test(priority=2)
	public void verifymanagerID() {
		String managerIDTxt = LPO.verifyMgrID();
		if(managerIDTxt.contains(appUname)) {
			System.out.println("Manager ID " + appUname + " is present in the login message");
		}
		else {
			System.out.println("Manager ID " + appUname + " is not present in the login message");
		}
	}
	
	@AfterClass
	public void closeBrowser() {
		driver.close();
	}
}
