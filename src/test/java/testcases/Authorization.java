package testcases;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.LogStatus;

import baseTest.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.ExcelUtils;
import utils.FileandEnv;

public class Authorization extends BaseTest {

	private String sTestCaseName;

	private int iTestCaseRow;
	
	public String URL = "http://192.168.1.80:8280";
	
	public String OAuthToken=null;
	public String wsrToken=null;

	public String vendorname = randomString();
	public int vendorID ;
	

public String wsrToken(String execute) {	  	
	  		
  		try
		{  			
  				
				test.log(LogStatus.INFO, "My test is starting.....");	
				
				
				RequestSpecification request = RestAssured.given().log().all().when();
				  
				request.header("Content-Type","application/x-www-form-urlencoded");
				request.header("Accept-Encoding", "gzip, deflate, br");
				request.header("Accept-Language","en-US,en;q=0.9");
				request.header("Connection","keep-alive");
			//	request.header("Accept","application/json, text/plain, */*");
				
				request.header("Authorization","Basic eDI2endKeWtfbFJTRmExdDg5NUh6QjlKaUhJYTpiZlZhWlAzWW9KdmJQeGlVNE1qRmM4UmQ1bWdh");
					
				
				Response response = request.post(URL+"/token?grant_type=client_credentials");
										  
				System.out.println(response.asPrettyString());
				
				JSONObject json = new JSONObject(response.getBody().asString());
				System.out.println(json.get("access_token"));
				
				if(response.getStatusCode() == 200)
				{			
					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
					test.log(LogStatus.PASS,"WSR TOken Response Time : "+response.getTime());
					test.log(LogStatus.INFO,"Full Response : "+response.getBody().asString());
				}
				
				test.log(LogStatus.INFO, "Successfully received WSR Token");	
				wsrToken = json.get("access_token").toString();
						
				test.log(LogStatus.INFO, "WSR test has been ended.....");
			
		
		}		
		catch(Exception ex)
		{
			System.out.println(ex);
			test.log(LogStatus.FAIL, "Exception occured"+ex);
		}
		
  		return wsrToken;
		
  	}  


  public String getOAuthToken()
	{
		String Tokens = null;
		String wsr = null;
		try
		{
			
			wsr = wsrToken("Yes");		
			
			test.log(LogStatus.INFO, "My test is starting.....");	
			
			//RestAssured.baseURI = "http://192.168.1.77:8280";
			  
			RequestSpecification request = RestAssured.given();
			
			request.header("Content-Type","application/x-www-form-urlencoded");
			request.header("Accept-Encoding", "gzip, deflate, br");
			request.header("Connection","keep-alive");
			request.header("Authorization","Bearer "+ wsr);			
			request.header("Authorization","Basic YmN0YmZzaTpiY3RiZnNpQDEyMw==");
			
			request.queryParams("grant_type","password","username","Admin","password","$2a$10$g6S5Ljco1ULYlrjW3oW9R.o9AzthM2ctiU0rGLpjML924lQQ/VXA2");
			//request.queryParams("grant_type","password","username","Admin","password","admin!01");
		
			Response response = request.post(URL+"/grcoauth/1/oauth/token");
			System.out.println(response.getBody().asString());	
				  
			if(response.getStatusCode() == 200)
			{			
				test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
				test.log(LogStatus.PASS,"OAUTH Token API Response Time : "+response.getTime());
				test.log(LogStatus.INFO,"Full Response : "+response.getBody().asString());
			}
			
			test.log(LogStatus.INFO, "Successfully received OAUTH Token");	
			
			JSONObject json = new JSONObject(response.getBody().asString());
			System.out.println(json.get("access_token"));
			OAuthToken = json.get("access_token").toString();
			
			test.log(LogStatus.INFO, "OAUTH test has been ended.....");
		
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			test.log(LogStatus.FAIL, "Exception occured"+ex);
		}	
		
		//Returning WSR and OAUth TOkents.....
		return wsr+","+OAuthToken;
		
	}
   

  
  @Test
  public void getOAuthToken_Autorization_InvalidEndpoint()
	{
		String Tokens = null;
		String wsr = null;
		try
		{
			
			wsr = wsrToken("Yes");		
			
			test.log(LogStatus.INFO, "My test is starting.....");	
			
			//RestAssured.baseURI = "http://192.168.1.77:8280";
			  
			RequestSpecification request = RestAssured.given();
			
			request.header("Content-Type","application/x-www-form-urlencoded");
			request.header("Accept-Encoding", "gzip, deflate, br");
			request.header("Connection","keep-alive");
			request.header("Authorization","Bearer "+ wsr);			
			request.header("Authorization","Basic YmN0YmZzaTpiY3RiZnNpQDEyMw==");
			
			request.queryParams("grant_type","password","username","Admin","password","$2a$10$g6S5Ljco1ULYlrjW3oW9R.o9AzthM2ctiU0rGLpjML924lQQ/VXA2");
			//request.queryParams("grant_type","password","username","Admin","password","admin!01");
		
			Response response = request.post(URL+"/grcoauth/1/oauth/toke");
			System.out.println(response.getBody().asString());	
				  
			if(response.getStatusCode() == 404)
			{			
				test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
				test.log(LogStatus.PASS,"OAUTH Token API Response Time : "+response.getTime());
				test.log(LogStatus.INFO,"Full Response : "+response.getBody().asString());
			}
			
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			test.log(LogStatus.FAIL, "Exception occured"+ex);
		}	
		
		
		
	}
   
  
  
  
  @Test
  public void getOAuthToken_Autorization_InvalidMethod()
	{
		String Tokens = null;
		String wsr = null;
		try
		{
			
			wsr = wsrToken("Yes");		
			
			test.log(LogStatus.INFO, "My test is starting.....");	
			
			//RestAssured.baseURI = "http://192.168.1.77:8280";
			  
			RequestSpecification request = RestAssured.given();
			
			request.header("Content-Type","application/x-www-form-urlencoded");
			request.header("Accept-Encoding", "gzip, deflate, br");
			request.header("Connection","keep-alive");
			request.header("Authorization","Bearer "+ wsr);			
			request.header("Authorization","Basic YmN0YmZzaTpiY3RiZnNpQDEyMw==");
			
			request.queryParams("grant_type","password","username","Admin","password","$2a$10$g6S5Ljco1ULYlrjW3oW9R.o9AzthM2ctiU0rGLpjML924lQQ/VXA2");
			//request.queryParams("grant_type","password","username","Admin","password","admin!01");
		
			Response response = request.get(URL+"/grcoauth/1/oauth/token");
			System.out.println(response.getBody().asString());	
				  
			if(response.getStatusCode() == 405)
			{			
				test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
				test.log(LogStatus.PASS,"OAUTH Token API Response Time : "+response.getTime());
				test.log(LogStatus.INFO,"Full Response : "+response.getBody().asString());
			}
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			test.log(LogStatus.FAIL, "Exception occured"+ex);
		}	
		
		
		
	}
   
  
  

@Test
  public void getOAuthToken_UnAutorization()
	{
		String Tokens = null;
		String wsr = null;
		try
		{
			
			wsr = wsrToken("Yes");		
			
			test.log(LogStatus.INFO, "My test is starting.....");	
			
			//RestAssured.baseURI = "http://192.168.1.77:8280";
			  
			RequestSpecification request = RestAssured.given();
			
			request.header("Content-Type","application/x-www-form-urlencoded");
			request.header("Accept-Encoding", "gzip, deflate, br");
			request.header("Connection","keep-alive");
			//request.header("Authorization","Bearer "+ wsr);			
			request.header("Authorization","Basic YmN0YmZzaTpiY3RiZnNpQDEyMw==");
			
			request.queryParams("grant_type","password","username","Admin","password","$2a$10$g6S5Ljco1ULYlrjW3oW9R.o9AzthM2ctiU0rGLpjML924lQQ/VXA2");
			//request.queryParams("grant_type","password","username","Admin","password","admin!01");
		
			Response response = request.post(URL+"/grcoauth/1/oauth/toke");
			System.out.println(response.getBody().asString());	
				  
			if(response.getStatusCode() == 401)
			{			
				test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
				test.log(LogStatus.PASS,"OAUTH Token API Response Time : "+response.getTime());
				test.log(LogStatus.INFO,"Full Response : "+response.getBody().asString());
			}
			
			
		}
		catch(Exception ex)
		{
			System.out.println(ex);
			test.log(LogStatus.FAIL, "Exception occured"+ex);
		}	
		
		
		
	}
   
  
  
  

    

  
  @DataProvider
  public Object[] Execute_Module() throws Exception{
  //public  String Execute_Module() throws Exception{	  

	    // Setting up the Test Data Excel file

	 	ExcelUtils.setExcelFile(System.getProperty("user.dir")+"\\inputs\\testdata.xlsx","Runner");

	 	//TestMethods = this.getClass().getMethods();
	 	
	 	sTestCaseName = this.toString();

	  	// From above method we get long test case name including package and class name etc.

	  	// The below method will refine your test case name, exactly the name use have used

	  	sTestCaseName = ExcelUtils.getTestCaseName(this.toString());

	    // Fetching the Test Case row number from the Test Data Sheet

	    // Getting the Test Case name to get the TestCase row from the Test Data Excel sheet

	 	//iTestCaseRow = ExcelUtils.getRowContains(sTestCaseName,0);
	  	 Object[] Execute = ExcelUtils.getRowContains(sTestCaseName,0);
	  		 	
	 	System.out.println(Execute);
	 //   Object[][] testObjArray = ExcelUtils.getTableArray(System.getProperty("user.dir")+"\\inputs\\testdata.xlsx","TestData",iTestCaseRow);

	 	
	    return (Execute);

		}

}