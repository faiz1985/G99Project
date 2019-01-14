package G99;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class G99Class {
	WebDriver driver;
	
@Parameters({"FireFoxDriver", "driverPath"})
  @BeforeClass
  public void initialiseBrowser(String firefoxDriver, String DriverPath) {
	  System.setProperty(firefoxDriver, DriverPath);
	  driver = new FirefoxDriver();
	  driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS); //wait for a certain amount of time before throwing an exception that it cannot find the element on the page
	  //System.out.println("Data from Util File for trial: " + Util.MY_NAME);
  }
	
	@Parameters({"AUTLink", "uname", "pwd"})
	@Test
	public void verifyLogin(String URL, String userName, String password) throws InterruptedException {
		//verifyLoginTitle is derived from Util.java for practice purposes
		driver.get(URL);
		loginPageObjects LPO = new loginPageObjects(driver);
		LPO.enterUname(userName);
		LPO.enterPassword(password);
		LPO.clickLoginBtn();
		//Assert.assertEquals(driver.getTitle(), Util.verifyLoginTitle);
		//System.out.println(driver.switchTo().alert().getText());
		
		
		if(driver.getTitle().equals(Util.verifyLoginTitle)) {
			System.out.println("Login Successful");
		}
		else if(driver.switchTo().alert().getText().contains(Util.invalidCredentialsError)) {
			System.out.println("Invalid User ID or Password Entered");
			driver.close();
		}
	}
	
	@AfterClass
	public void closeBrowser() {
		driver.close();
	}
}
