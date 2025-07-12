package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import TestBase.BaseClass;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import pageObjects.MyAccountPage;
import utilities.DataProviders;

public class TC003_LoginDDT extends BaseClass {


	    @Test(dataProvider="LoginData",dataProviderClass=DataProviders.class,groups="DataDriven")//Getting dataProvider from different class
	    public void verify_loginDDT(String email,String pwd,String exp)
	    {
	    	logger.info("****** Starting Execution of TC_003_LoginDDT *********");
	    	
	    	
	    	try {
	        //HomePage
	        HomePage hp= new HomePage(driver);
	        hp.clickMyAccount();
	        hp.clickLogin();

	        //Login
	        LoginPage lp=new LoginPage(driver);
	        lp.setEmail(email);
	        lp.setPassword(pwd);
	        lp.clickLogin();

	        //MyAccount
	        MyAccountPage macc= new MyAccountPage(driver);
	        boolean targetPage=macc.isMyAccountPageExists();
	        if(exp.equalsIgnoreCase("Valid")) {
	        	if(targetPage=true) {
	        		Assert.assertTrue(true);
	        		macc.clickLogout();
	        	}
	        	else {
	        		Assert.assertTrue(false);
	        	}
	    }
	        if(exp.equalsIgnoreCase("Invalid")) {
	        	if(targetPage==true) {
	        		macc.clickLogout();
	        		Assert.assertTrue(false);
	        	}
	        	else {
	        		Assert.assertTrue(false);
	        	}
	        }
	    	}
	    	catch(Exception e) {
	    		Assert.fail();
	    	}
	        logger.info("****** Ending Execution of TC_003_LoginDDT *********");
	    }
	    
}

