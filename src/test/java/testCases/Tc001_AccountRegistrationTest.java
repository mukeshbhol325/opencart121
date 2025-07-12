package testCases;

import java.time.Duration;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import TestBase.BaseClass;
import pageObjects.AccountRegistrationPage;
import pageObjects.HomePage;

public class Tc001_AccountRegistrationTest extends BaseClass {

	
	

	@Test(groups={"Regression","Master"})
	public void verify_account_registration() {
		logger.info("******** Starting Tc001_AccountRegistrationTest *****");
		try {
		HomePage hp=new HomePage(driver);
		hp.clickMyAccount();
		logger.info("Clicked on My Account Link...");
		hp.clickRegister();
		logger.info("Clicked on Register Link...");
		
		AccountRegistrationPage regpage = new AccountRegistrationPage(driver);
		
		logger.info("Providing customer details...");
		regpage.setFirstName(randomeString().toUpperCase());
		regpage.setLastName(randomeString().toUpperCase());
		regpage.setEmail(randomeString()+"@gmail.com"); // randomly generated the email
		regpage.setTelephone(randomeNumber());

		String password = randomeAlphaNumeric();

		regpage.setPassword(password);
		regpage.setConfirmPassword(password);

		regpage.setPrivacyPolicy();
		regpage.clickContinue();
		logger.info("Validating Expected Message...");

		String confmsg = regpage.getConfirmationMsg();

		Assert.assertEquals(confmsg, "Your Account Has Been Created!");

		}
		
		catch(Exception e) {
			logger.error("Test Failled..");
			logger.debug("Debug logs..");
			Assert.fail();
		}
		
		logger.info("****** Finished Tc001_AccountRegistrationTest **********");
		
	}
	

}
