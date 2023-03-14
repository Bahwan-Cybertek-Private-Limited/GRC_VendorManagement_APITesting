package baseTest;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExtentReportListner;
import utils.FileandEnv;

import java.nio.charset.Charset;
import java.util.Random;

import org.json.*;

@Listeners(ExtentReportListner.class)
public class BaseTest extends ExtentReportListner{
	
	
	public String OAuthToken=null;
	public String wsrToken=null;

	
	
	  @BeforeClass 
	  public void baseTest() {
	  
	  // TODO...... 
	  
	  }
	 	  
	
	  	
	
	
	public String randomString() {
		
		String generatedString = "";
		
		try {
			 int leftLimit = 97; // letter 'a'
			    int rightLimit = 122; // letter 'z'
			    int targetStringLength = 10;
			    Random random = new Random();
			    StringBuilder buffer = new StringBuilder(targetStringLength);
			    for (int i = 0; i < targetStringLength; i++) {
			        int randomLimitedInt = leftLimit + (int) 
			          (random.nextFloat() * (rightLimit - leftLimit + 1));
			        buffer.append((char) randomLimitedInt);
			    }
			    
			    generatedString = buffer.toString();
		}
		catch(Exception ex) {
			
		}
		
		return generatedString;
	}
	
	
	

}
