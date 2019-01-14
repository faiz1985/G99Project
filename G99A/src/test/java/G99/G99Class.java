package G99;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class G99Class {
	WebDriver driver;
	String fireFoxDriver, FFDriverPath, appURL, appUname, appPwd,verifyLoginTitle,invalidCredentialsError;
	
	@BeforeSuite
	@Parameters({"FireFoxDriver", "driverPath", "AUTLink", "uname", "pwd"})
	public void dataSource(String firefoxDriver, String DriverPath, String URL, String userName, String password) {
		//These are from TestNG.XML file
		fireFoxDriver=firefoxDriver;
		FFDriverPath=DriverPath;
		appURL=URL;
		appUname=userName;
		appPwd=password;
		
		//These are from Util.Java file
		verifyLoginTitle=Util.verifyLoginTitle;
		invalidCredentialsError=Util.invalidCredentialsError;
	}
	
//@Parameters({"FireFoxDriver", "driverPath"})
  @BeforeClass()
  public void initialiseBrowser() {
	  System.setProperty(fireFoxDriver, FFDriverPath);
	  driver = new FirefoxDriver();
	  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //wait for a certain amount of time before throwing an exception that it cannot find the element on the page
  }
	
	//@Parameters({"AUTLink", "uname", "pwd"})
	@Test
	public void verifyLogin() throws InterruptedException {
		driver.get(appURL);
		loginPageObjects LPO = new loginPageObjects(driver);
		LPO.enterUname(appUname);
		LPO.enterPassword(appPwd);
		LPO.clickLoginBtn();
		//Assert.assertEquals(driver.getTitle(), Util.verifyLoginTitle);		
		
		if(driver.getTitle().equals(verifyLoginTitle)) {
			System.out.println("Login Successful");
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
