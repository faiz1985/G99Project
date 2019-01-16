package G99;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class loginPageObjects {
	WebDriver driver;
	
	@FindBy(name="uid")
	WebElement uname;
	
	@FindBy(name="password")
	WebElement pwd;
	
	@FindBy(name="btnLogin")
	WebElement loginBtn;
	
	@FindBy(xpath="/html/body/table/tbody/tr/td/table/tbody/tr[3]/td")
	WebElement managerIDTxt;
	
	public loginPageObjects(WebDriver driver) {
		this.driver=driver;
		PageFactory.initElements(driver, this);
	}
	
	public void enterUname(String user) {
		uname.sendKeys(user);
	}
	
	public void enterPassword(String pass) {
		pwd.sendKeys(pass);
	}
	
	public void clickLoginBtn() {
		loginBtn.click();
	}
	
	public String verifyMgrID() {
		return managerIDTxt.getText();
	}
}
