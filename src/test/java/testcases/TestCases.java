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

public class TestCases extends BaseTest {

	private String sTestCaseName;

	private int iTestCaseRow;
	
	public String URL = "http://192.168.1.80:8280";
	
	public String OAuthToken=null;
	public String wsrToken=null;

	public String vendorname = randomString();
	public int vendorID ;
	public String vendoruniqueID ;
	
	public String ApprovalLevel = null;
	public String assessmentName = null;
	public int assessmentId ;

	public int ControlID ;
	public int SeriesId;
	

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
   



  @Test(dataProvider = "Execute_Module", priority = 1)
 	public void dashboard_AssessmentCount(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/assessmentcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}


  

  @Test(dataProvider = "Execute_Module",priority = 2)
 	public void dashboard_AssessmentCount_InvalidEndpoint(String Execute) // invalid endpoint...
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/assessmentcoun");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 3)
 	public void dashboard_AssessmentCount_InvalidMethod(String Execute) // invalid endpoint...
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dashboard/assessmentcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  @Test(dataProvider = "Execute_Module",priority = 4)
 	public void dashboard_ResidualRatingCount(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/residualriskratingcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 5)
 	public void dashboard_ResidualRatingCount_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/residualriskratingcoun");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 6)
 	public void dashboard_ResidualRatingCount_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dashboard/residualriskratingcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  


  @Test(dataProvider = "Execute_Module",priority = 7)
 	public void dashboard_InherentRiskRatingCount(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/inherentriskratingcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  


  @Test(dataProvider = "Execute_Module",priority = 8)
 	public void dashboard_InherentRiskRatingCount_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/inherentriskratingcoun");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  


  @Test(dataProvider = "Execute_Module",priority = 9)
 	public void dashboard_InherentRiskRatingCount_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dashboard/inherentriskratingcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  


  @Test(dataProvider = "Execute_Module",priority = 10)
 	public void dashboard_HeatMapCount(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/heatmapcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  


  @Test(dataProvider = "Execute_Module",priority = 11)
 	public void dashboard_HeatMapCount_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/heatmapcoun");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  


  @Test(dataProvider = "Execute_Module",priority = 12)
 	public void dashboard_HeatMapCount_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dashboard/heatmapcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  
  @Test(dataProvider = "Execute_Module",priority = 13)
 	public void dashboard_FindAllCount(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/find/all");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 14)
 	public void dashboard_FindAllCount_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/find/al");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 15)
 	public void dashboard_FindAllCount_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/find/all");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  


  @Test(dataProvider = "Execute_Module",priority = 16)
 	public void dashboard_AssessmentWithVendor(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/view/asessmentwithvendor");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  

  @Test(dataProvider = "Execute_Module",priority = 17)
 	public void dashboard_AssessmentWithVendor_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/view/asessmentwithvendo");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  

  @Test(dataProvider = "Execute_Module",priority = 18)
 	public void dashboard_AssessmentWithVendor_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/view/asessmentwithvendor");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 19)
 	public void dashboard_VendorCount(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/vendorcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 20)
 	public void dashboard_VendorCount_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dashboard/vendorcoun");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  

  

  @Test(dataProvider = "Execute_Module",priority = 21)
 	public void dashboard_VendorCount_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dashboard/vendorcount");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  


  

  @Test(dataProvider = "Execute_Module",priority = 22)
 	public void vendorRegister_Dropdownvalues(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dropdownvalues");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 23)
 	public void vendorRegister_Dropdownvalues_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/dropdownvalue");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  


  @Test(dataProvider = "Execute_Module",priority = 24)
 	public void vendorRegister_Dropdownvalues_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/dropdownvalues");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 25)
 	public void vendorRegister_EmployeeAll(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/riskregistry/1/riskWorkshop/employees/all");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 26)
 	public void vendorRegister_EmployeeAll_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/riskregistry/1/riskWorkshop/employees/al");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 27)
 	public void vendorRegister_EmployeeAll_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/riskregistry/1/riskWorkshop/employees/all");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  
  @Test(dataProvider = "Execute_Module",priority = 28)
 	public void vendorRegister_CreateSinglevendor(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 					
 					JSONObject obj1 = new JSONObject(response.getBody().asString());
 					
 					JSONObject obj2 = new JSONObject(obj1.get("result").toString());
 					
 					vendorID = obj2.getInt("vendorId");
 					vendoruniqueID = obj2.getString("vendorUniqueId");
 					
 					test.log(LogStatus.INFO, "vendor id : "+vendorID);
 					test.log(LogStatus.INFO, "vendoruniqueID : "+vendoruniqueID);
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  
  
  @Test(dataProvider = "Execute_Module",priority = 29)
 	public void vendorRegister_CreateSinglevendor_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/ne");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 					
 					
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  
  
  


  @Test(dataProvider = "Execute_Module",priority = 30)
 	public void vendorRegister_DetailswithVendorID_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/find/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  



  @Test(dataProvider = "Execute_Module",priority = 31)
 	public void vendorRegister_AttachmentDetailswithVendorID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/attachment/view/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 412)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  



  @Test(dataProvider = "Execute_Module",priority = 32)
 	public void vendorRegister_AttachmentDetailswithVendorID_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/attachment/vie/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  



  @Test(dataProvider = "Execute_Module",priority = 33)
 	public void vendorRegister_AttachmentDetailswithVendorID_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/attachment/view/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  @Test(dataProvider = "Execute_Module",priority = 34)
 	public void vendorRegister_EditSinglevendor(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/modify");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 35)
 	public void vendorRegister_EditSinglevendor_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/modif");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 36)
 	public void vendorRegister_EditSinglevendor_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/modify");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 37)
 	public void vendorRegister_SubmissionApproval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				
 				obj.put("vendorUniqueId",vendoruniqueID);				
 				obj.put("remarks","test");
 				obj.put("ratingColor","RED");
 				obj.put("residualImpactType","0");
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  


  @Test(dataProvider = "Execute_Module",priority = 38)
 	public void vendorRegister_SubmissionApproval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				
 				obj.put("vendorUniqueId",vendoruniqueID);				
 				obj.put("remarks","test");
 				obj.put("ratingColor","RED");
 				obj.put("residualImpactType","0");
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/approva");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  

  


  @Test(dataProvider = "Execute_Module",priority = 39)
 	public void vendorRegister_SubmissionApproval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				
 				obj.put("vendorUniqueId",vendoruniqueID);				
 				obj.put("remarks","test");
 				obj.put("ratingColor","RED");
 				obj.put("residualImpactType","0");
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/workflow/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  


  @Test(dataProvider = "Execute_Module",priority = 40)
 	public void vendorRegister_FindAllApproval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/find/all/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 41)
 	public void vendorRegister_FindAllApproval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/find/all/approva");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 42)
 	public void vendorRegister_FindAllApproval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/find/all/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 43)
 	public void vendorRegister_ApprovalHistory(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
				
				  JSONObject obj = new JSONObject(); 
				  obj.put("vendorUniqueId", vendoruniqueID);
				  obj.put("vendorId", vendorID);
				  
				  request.body(obj.toString());
				 
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/approval/history/find");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 44)
 	public void vendorRegister_ApprovalHistory_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorUniqueId", vendoruniqueID);
 				obj.put("vendorId", vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/workflow/approval/history/fin");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 45)
 	public void vendorRegister_ApprovalHistory_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorUniqueId", vendoruniqueID);
 				obj.put("vendorId", vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/workflow/approval/history/find");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 46)
 	public void vendorRegister_L3Approval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorUniqueId", vendoruniqueID);
 				obj.put("vendorId", vendorID);
 				obj.put("approvalStatus", "Approved");
 				obj.put("comments", "test");
 				
 				request.body(obj.toString());
 				
 				
 				for(int i=0;i<3;i++)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj3 = new JSONObject(response.getBody().asString());
	 					ApprovalLevel = obj3.getString("approvalLevel");
	 					
	 					if(ApprovalLevel.equals("L3")) 
	 					{
	 						test.log(LogStatus.INFO, "Vendor is approved to L3 level..");
	 						break;
	 					}
	 					else {
	 						test.log(LogStatus.INFO, "Vendor is not yet approve to L3..");
	 					}
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 47)
 	public void vendorRegister_L3Approval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorUniqueId", vendoruniqueID);
 				obj.put("vendorId", vendorID);
 				obj.put("approvalStatus", "Approved");
 				obj.put("comments", "test");
 				
 				request.body(obj.toString());
 				
 				
 				
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/approval/status/ne");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 48)
 	public void vendorRegister_L3Approval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorUniqueId", vendoruniqueID);
 				obj.put("vendorId", vendorID);
 				obj.put("approvalStatus", "Approved");
 				obj.put("comments", "test");
 				
 				request.body(obj.toString());
 				
 			
	 				Response response = request.get(URL+"/vendormgmt/1/workflow/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
   


  


  @Test(dataProvider = "Execute_Module",priority = 49)
 	public void vendorAssessment_AssessmentDetailswithVendorID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/vendor/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 412)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 50)
 	public void vendorAssessment_AssessmentDetailswithVendorID_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/vendo/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}


  
  

  @Test(dataProvider = "Execute_Module",priority = 51)
 	public void vendorAssessment_AssessmentDetailswithVendorID_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/assessment/vendor/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}


  
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 52)
 	public void vendorAssessment_DepartmentDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/riskregistry/1/riskWorkshop/departments/all");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 53)
 	public void vendorAssessment_DepartmentDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/riskregistry/1/riskWorkshop/departments/al");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 54)
 	public void vendorAssessment_DepartmentDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.put(URL+"/riskregistry/1/riskWorkshop/departments/all");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 55)
 	public void vendorAssessment_CreateMeetingScheduler(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("meetingName", "test100");
 				obj.put("meetingConvenerName", "test");
 				obj.put("meetingStartDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingEndDate", "2000-12-07T04:29:00.000Z");
 				obj.put("meetingAgenda", "test");
 				obj.put("meetingPurpose", "test");
 				obj.put("meetingReminder", "1");
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("participantID", "BS1000014");
 				obj.put("participantName", "Samir");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "Head Office");
 				obj.put("participantEmail", "test@test.com");
 						
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("participantList", arr1);
 				
 				request.body(obj.toString());
 				
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/meeting/create");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  

  @Test(dataProvider = "Execute_Module",priority = 56)
 	public void vendorAssessment_CreateMeetingScheduler_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("meetingName", "test100");
 				obj.put("meetingConvenerName", "test");
 				obj.put("meetingStartDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingEndDate", "2000-12-07T04:29:00.000Z");
 				obj.put("meetingAgenda", "test");
 				obj.put("meetingPurpose", "test");
 				obj.put("meetingReminder", "1");
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("participantID", "BS1000014");
 				obj.put("participantName", "Samir");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "Head Office");
 				obj.put("participantEmail", "test@test.com");
 						
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("participantList", arr1);
 				
 				request.body(obj.toString());
 				
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/meeting/creat");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 57)
 	public void vendorAssessment_CreateMeetingScheduler_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("meetingName", "test100");
 				obj.put("meetingConvenerName", "test");
 				obj.put("meetingStartDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingEndDate", "2000-12-07T04:29:00.000Z");
 				obj.put("meetingAgenda", "test");
 				obj.put("meetingPurpose", "test");
 				obj.put("meetingReminder", "1");
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("participantID", "BS1000014");
 				obj.put("participantName", "Samir");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "Head Office");
 				obj.put("participantEmail", "test@test.com");
 						
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("participantList", arr1);
 				
 				request.body(obj.toString());
 				
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/meeting/create");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 58)
 	public void vendorAssessment_templateList(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 			
 				
 				Response response = request.get(URL+"/grctmp/1/templates/template/names/test");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  

  

  @Test(dataProvider = "Execute_Module",priority = 59)
 	public void vendorAssessment_templateList_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 			
 				
 				Response response = request.get(URL+"/grctmp/1/templates/template/name/test");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 60)
 	public void vendorAssessment_templateList_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 			
 				
 				Response response = request.put(URL+"/grctmp/1/templates/template/names/test");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 61)
 	public void vendorAssessment_SaveAssessmentDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentName", "AT");
 				obj.put("description", "test");
 				obj.put("assessmentDueDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingSchedulerId", "MS142");
 				obj.put("questionerTemplateId", "Test - T_1692");
 				obj.put("remarks", "test"); 				
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("assessorID", "BS1000015");
 				obj.put("assessorName", "IT Head");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "IT Department");
 				obj.put("assessorEmail", "christy@gmail.com"); 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("assessors", arr1);
 				obj.put("vendorId", vendorID);
 				obj.put("vendorUniqueId", vendoruniqueID); 				
 				
 				request.body(obj.toString());				
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 					
 					
 					JSONObject obj4 = new JSONObject(response.getBody().asString());
 					
 					JSONObject obj5 = new JSONObject(obj4.get("result").toString());
 					
 					assessmentId = obj5.getInt("assessmentId");
 					assessmentName = obj5.getString("assessmentName");
 					
 					test.log(LogStatus.INFO, "vendor id : "+vendorID);
 					test.log(LogStatus.INFO, "vendoruniqueID : "+vendoruniqueID);
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 62)
 	public void vendorAssessment_SaveAssessmentDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentName", "AT");
 				obj.put("description", "test");
 				obj.put("assessmentDueDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingSchedulerId", "MS142");
 				obj.put("questionerTemplateId", "Test - T_1692");
 				obj.put("remarks", "test"); 				
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("assessorID", "BS1000015");
 				obj.put("assessorName", "IT Head");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "IT Department");
 				obj.put("assessorEmail", "christy@gmail.com"); 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("assessors", arr1);
 				obj.put("vendorId", vendorID);
 				obj.put("vendorUniqueId", vendoruniqueID); 				
 				
 				request.body(obj.toString());				
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/ne");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 63)
 	public void vendorAssessment_SaveAssessmentDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentName", "AT");
 				obj.put("description", "test");
 				obj.put("assessmentDueDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingSchedulerId", "MS142");
 				obj.put("questionerTemplateId", "Test - T_1692");
 				obj.put("remarks", "test"); 				
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("assessorID", "BS1000015");
 				obj.put("assessorName", "IT Head");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "IT Department");
 				obj.put("assessorEmail", "christy@gmail.com"); 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("assessors", arr1);
 				obj.put("vendorId", vendorID);
 				obj.put("vendorUniqueId", vendoruniqueID); 				
 				
 				request.body(obj.toString());				
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 64)
 	public void vendorAssessment_AssessmentDetailswithID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/vendor/"+vendorID);
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 65)
 	public void vendorAssessment_Modify_AssessmentDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentName", "AT");
 				obj.put("description", "test");
 				obj.put("assessmentDueDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingSchedulerId", "MS142");
 				obj.put("questionerTemplateId", "Test - T_1692");
 				obj.put("remarks", "test"); 				
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("assessorID", "BS1000015");
 				obj.put("assessorName", "IT Head");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "IT Department");
 				obj.put("assessorEmail", "christy@gmail.com"); 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("assessors", arr1);
 				obj.put("vendorId", vendorID);
 				obj.put("vendorUniqueId", vendoruniqueID); 	
 				obj.put("assessmentId", assessmentId); 	
 				
 				request.body(obj.toString());				
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/assessment/modify");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 66)
 	public void vendorAssessment_Modify_AssessmentDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentName", "AT");
 				obj.put("description", "test");
 				obj.put("assessmentDueDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingSchedulerId", "MS142");
 				obj.put("questionerTemplateId", "Test - T_1692");
 				obj.put("remarks", "test"); 				
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("assessorID", "BS1000015");
 				obj.put("assessorName", "IT Head");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "IT Department");
 				obj.put("assessorEmail", "christy@gmail.com"); 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("assessors", arr1);
 				obj.put("vendorId", vendorID);
 				obj.put("vendorUniqueId", vendoruniqueID); 	
 				obj.put("assessmentId", assessmentId); 	
 				
 				request.body(obj.toString());				
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/assessment/modi");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 67)
 	public void vendorAssessment_Modify_AssessmentDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentName", "AT");
 				obj.put("description", "test");
 				obj.put("assessmentDueDate", "1998-12-07T05:28:00.000Z");
 				obj.put("meetingSchedulerId", "MS142");
 				obj.put("questionerTemplateId", "Test - T_1692");
 				obj.put("remarks", "test"); 				
 				
 				JSONObject obj1 = new JSONObject();
 				obj.put("assessorID", "BS1000015");
 				obj.put("assessorName", "IT Head");
 				obj.put("departmentID", "CL00006");
 				obj.put("departmentName", "IT Department");
 				obj.put("assessorEmail", "christy@gmail.com"); 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj1);
 				
 				obj.put("assessors", arr1);
 				obj.put("vendorId", vendorID);
 				obj.put("vendorUniqueId", vendoruniqueID); 	
 				obj.put("assessmentId", assessmentId); 	
 				
 				request.body(obj.toString());				
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/modify");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 68)
 	public void vendorAssessment_AssessmentwithVendorDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/view/asessmentwithvendor");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 69)
 	public void vendorAssessment_AssessmentwithVendorDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/view/asessmentwithvendo");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 70)
 	public void vendorAssessment_AssessmentwithVendorDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/assessment/view/asessmentwithvendor");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 71)
 	public void vendorAssessment_SubmissionAssessment(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentId",assessmentId);
 				obj.put("remarks","test");
 				obj.put("vendorId",vendorID);
 						
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/assessment/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 72)
 	public void vendorAssessment_AssessmentApprovalList(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/find/all/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 73)
 	public void vendorAssessment_AssessmentApprovalList_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/assessment/find/all/approva");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 74)
 	public void vendorAssessment_AssessmentApprovalList_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/assessment/find/all/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 75)
 	public void vendorAssessment_AssessmentApprovalHistory(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("assessmentId",assessmentId);
 				obj2.put("vendorId",vendorID);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/assessment/approval/history/find");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 76)
 	public void vendorAssessment_AssessmentApprovalHistory_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("assessmentId",assessmentId);
 				obj2.put("vendorId",vendorID);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/assessment/approval/history/fin");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 77)
 	public void vendorAssessment_AssessmentApprovalHistory_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("assessmentId",assessmentId);
 				obj2.put("vendorId",vendorID);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/workflow/assessment/approval/history/find");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 78)
 	public void vendorAssessment_L3Approval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentId", assessmentId);
 				obj.put("vendorId", vendorID);
 				obj.put("approvalStatus", "Approved");
 				obj.put("comments", "test");
 				
 				request.body(obj.toString());
 				
 				
 				for(int i=0;i<3;i++)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/assessment/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj3 = new JSONObject(response.getBody().asString());
	 					ApprovalLevel =null;
	 					ApprovalLevel = obj3.getString("approvalLevel");
	 					
	 					if(ApprovalLevel.equals("L3")) 
	 					{
	 						test.log(LogStatus.INFO, "Vendor is approved to L3 level..");
	 						break;
	 					}
	 					else {
	 						test.log(LogStatus.INFO, "Vendor is not yet approve to L3..");
	 					}
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  

  @Test(dataProvider = "Execute_Module",priority = 79)
 	public void vendorAssessment_L3Approval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentId", assessmentId);
 				obj.put("vendorId", vendorID);
 				obj.put("approvalStatus", "Approved");
 				obj.put("comments", "test");
 				
 				request.body(obj.toString());
 				
 				
 				for(int i=0;i<3;i++)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/assessment/approval/status/ne");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 80)
 	public void vendorAssessment_L3Approval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj = new JSONObject();
 				obj.put("assessmentId", assessmentId);
 				obj.put("vendorId", vendorID);
 				obj.put("approvalStatus", "Approved");
 				obj.put("comments", "test");
 				
 				request.body(obj.toString());
 				
 				
 				for(int i=0;i<3;i++)
 				{
	 				Response response = request.get(URL+"/vendormgmt/1/workflow/assessment/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 81)
 	public void vendorFindings_FindVendorAssessmentIDDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/finding/find/vendor/"+vendorID+"/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 412)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  



  @Test(dataProvider = "Execute_Module",priority = 82)
 	public void vendorFindings_FindVendorAssessmentIDDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/findin/find/vendor/"+vendorID+"/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  



  @Test(dataProvider = "Execute_Module",priority = 83)
 	public void vendorFindings_FindVendorAssessmentIDDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/finding/find/vendor/"+vendorID+"/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 84)
 	public void vendorFindings_AddFindings(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("vendorAssessmentImpact","Financial");
 				obj2.put("inherentRiskRating","1");
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("findingType","Non-Compliance");
 				obj3.put("findingDesc","test");
 				obj3.put("businessLine","HR");
 				obj3.put("findingCreatedDate","1998-12-07T18:30:00.000Z");
 				obj3.put("findingCreatedBy","BS1000015");
 				obj3.put("organization","HO");
 				
 				JSONArray arr2 = new JSONArray(); 				
 				arr2.put(obj3);
 				
 				obj2.put("findings", arr2);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/finding/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  



  @Test(dataProvider = "Execute_Module",priority = 85)
 	public void vendorFindings_AddFindings_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("vendorAssessmentImpact","Financial");
 				obj2.put("inherentRiskRating","1");
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("findingType","Non-Compliance");
 				obj3.put("findingDesc","test");
 				obj3.put("businessLine","HR");
 				obj3.put("findingCreatedDate","1998-12-07T18:30:00.000Z");
 				obj3.put("findingCreatedBy","BS1000015");
 				obj3.put("organization","HO");
 				
 				JSONArray arr2 = new JSONArray(); 				
 				arr2.put(obj3);
 				
 				obj2.put("findings", arr2);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/finding/ne");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  



  @Test(dataProvider = "Execute_Module",priority = 86)
 	public void vendorFindings_AddFindings_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("vendorAssessmentImpact","Financial");
 				obj2.put("inherentRiskRating","1");
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("findingType","Non-Compliance");
 				obj3.put("findingDesc","test");
 				obj3.put("businessLine","HR");
 				obj3.put("findingCreatedDate","1998-12-07T18:30:00.000Z");
 				obj3.put("findingCreatedBy","BS1000015");
 				obj3.put("organization","HO");
 				
 				JSONArray arr2 = new JSONArray(); 				
 				arr2.put(obj3);
 				
 				obj2.put("findings", arr2);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/finding/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 87)
 	public void vendorFindings_Approval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("ratingColor","GREEN");
 				obj3.put("residualImpactType","MEDIUM");
 				obj3.put("remarks","test");
 				
 				
 				request.body(obj3.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/finding/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 88)
 	public void vendorFindings_Approval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("ratingColor","GREEN");
 				obj3.put("residualImpactType","MEDIUM");
 				obj3.put("remarks","test");
 				
 				
 				request.body(obj3.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/finding/approva");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  


  @Test(dataProvider = "Execute_Module",priority = 89)
 	public void vendorFindings_Approval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("ratingColor","GREEN");
 				obj3.put("residualImpactType","MEDIUM");
 				obj3.put("remarks","test");
 				
 				
 				request.body(obj3.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/workflow/finding/approval");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  @Test(dataProvider = "Execute_Module",priority = 90)
 	public void vendorFindings_FindAssessmentDetailswithID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/finding/find/vendor/"+vendorID+"/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 91)
 	public void vendorFindings_FindAssessmentDetailswithID_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/finding/find/vendo/"+vendorID+"assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 92)
 	public void vendorFindings_FindAssessmentDetailswithID_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/finding/find/vendor/"+vendorID+"/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 93)
 	public void vendorFindings_ModifyFindingsDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("vendorAssessmentImpact","Financial");
 				obj2.put("inherentRiskRating","1");
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("findingType","Non-Compliance");
 				obj3.put("findingDesc","test1");
 				obj3.put("businessLine","HR");
 				obj3.put("findingCreatedDate","1998-12-07T18:30:00.000Z");
 				obj3.put("findingCreatedBy","BS1000015");
 				obj3.put("organization","HO");
 				
 				JSONArray arr2 = new JSONArray(); 				
 				arr2.put(obj3);
 				
 				obj2.put("findings", arr2);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/finding/modify");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 94)
 	public void vendorFindings_ModifyFindingsDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("vendorAssessmentImpact","Financial");
 				obj2.put("inherentRiskRating","1");
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("findingType","Non-Compliance");
 				obj3.put("findingDesc","test1");
 				obj3.put("businessLine","HR");
 				obj3.put("findingCreatedDate","1998-12-07T18:30:00.000Z");
 				obj3.put("findingCreatedBy","BS1000015");
 				obj3.put("organization","HO");
 				
 				JSONArray arr2 = new JSONArray(); 				
 				arr2.put(obj3);
 				
 				obj2.put("findings", arr2);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/finding/modif");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 95)
 	public void vendorFindings_ModifyFindingsDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 							
 				JSONObject obj2 = new JSONObject();
 				obj2.put("vendorAssessmentImpact","Financial");
 				obj2.put("inherentRiskRating","1");
 								
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("findingType","Non-Compliance");
 				obj3.put("findingDesc","test1");
 				obj3.put("businessLine","HR");
 				obj3.put("findingCreatedDate","1998-12-07T18:30:00.000Z");
 				obj3.put("findingCreatedBy","BS1000015");
 				obj3.put("organization","HO");
 				
 				JSONArray arr2 = new JSONArray(); 				
 				arr2.put(obj3);
 				
 				obj2.put("findings", arr2);
 				
 				request.body(obj2.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/finding/modify");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 96)
 	public void ApprovalInherentRisk_L3Approval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("approvalStatus","Approved");
 				obj3.put("comments","test");
 				
 				request.body(obj3.toString());
 				

 				for(int i=0;i<3;i++)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/finding/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj4 = new JSONObject(response.getBody().asString());
	 					ApprovalLevel = obj4.getString("approvalLevel");
	 					
	 					if(ApprovalLevel.equals("L3")) 
	 					{
	 						test.log(LogStatus.INFO, "Vendor is approved to L3 level..");
	 						break;
	 					}
	 					else {
	 						test.log(LogStatus.INFO, "Vendor is not yet approve to L3..");
	 					}
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 97)
 	public void ApprovalInherentRisk_L3Approval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("approvalStatus","Approved");
 				obj3.put("comments","test");
 				
 				request.body(obj3.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/workflow/finding/approval/status/ne");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 98)
 	public void ApprovalInherentRisk_L3Approval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("approvalStatus","Approved");
 				obj3.put("comments","test");
 				
 				request.body(obj3.toString());
 				

 			
	 				Response response = request.get(URL+"/vendormgmt/1/workflow/finding/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 99)
 	public void addControlandMitigation_FindControlwithID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/control/find/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 412)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
    


  @Test(dataProvider = "Execute_Module",priority = 100)
 	public void addControlAndMitigation_CreateControl(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj1 = new JSONObject();
 				obj1.put("controlMitigation","test");
 				obj1.put("residualRiskRating","1");
 				obj1.put("nextAssessmentDueDate","1998-12-08T18:30:00.000Z");
 				obj1.put("remarks","test"); 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("assigneeID","BS1000015");
 				obj3.put("assigneeName","IT Head");
 				obj3.put("departmentID","CL00002");
 				obj3.put("departmentName","IT Department");
 				obj3.put("assigneeEmail","christy@gmail.com");
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj3);
 				
 				obj1.put("assignees", arr1);
 				
 				request.body(obj1.toString());
 				
	 				Response response = request.post(URL+"/vendormgmt/1/vendor/control/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj6 = new JSONObject(response.getBody().asString());
	 					
	 					JSONObject obj2 = new JSONObject(obj6.get("result").toString());
	 					
	 					ControlID = obj2.getInt("controlId");
	 						 					
	 					test.log(LogStatus.INFO, "controlId : "+ControlID);
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 101)
 	public void addControlAndMitigation_CreateControl_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj1 = new JSONObject();
 				obj1.put("controlMitigation","test");
 				obj1.put("residualRiskRating","1");
 				obj1.put("nextAssessmentDueDate","1998-12-08T18:30:00.000Z");
 				obj1.put("remarks","test"); 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("assigneeID","BS1000015");
 				obj3.put("assigneeName","IT Head");
 				obj3.put("departmentID","CL00002");
 				obj3.put("departmentName","IT Department");
 				obj3.put("assigneeEmail","christy@gmail.com");
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj3);
 				
 				obj1.put("assignees", arr1);
 				
 				request.body(obj1.toString());
 				
	 				Response response = request.post(URL+"/vendormgmt/1/vendor/control/ne");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj6 = new JSONObject(response.getBody().asString());
	 					
	 					JSONObject obj2 = new JSONObject(obj6.get("result").toString());
	 					
	 					ControlID = obj2.getInt("controlId");
	 						 					
	 					test.log(LogStatus.INFO, "controlId : "+ControlID);
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 102)
 	public void addControlAndMitigation_CreateControl_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj1 = new JSONObject();
 				obj1.put("controlMitigation","test");
 				obj1.put("residualRiskRating","1");
 				obj1.put("nextAssessmentDueDate","1998-12-08T18:30:00.000Z");
 				obj1.put("remarks","test"); 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("assigneeID","BS1000015");
 				obj3.put("assigneeName","IT Head");
 				obj3.put("departmentID","CL00002");
 				obj3.put("departmentName","IT Department");
 				obj3.put("assigneeEmail","christy@gmail.com");
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj3);
 				
 				obj1.put("assignees", arr1);
 				
 				request.body(obj1.toString());
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/control/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj6 = new JSONObject(response.getBody().asString());
	 					
	 					JSONObject obj2 = new JSONObject(obj6.get("result").toString());
	 					
	 					ControlID = obj2.getInt("controlId");
	 						 					
	 					test.log(LogStatus.INFO, "controlId : "+ControlID);
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 103)
 	public void addControlAndMitigation_CreateControlApproval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("ratingColor","GREEN");
 				obj3.put("remarks","remarks");
 				obj3.put("residualImpactType","MEDIUM");
 				
 				request.body(obj3.toString());
 				
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/control/approval");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 104)
 	public void addControlAndMitigation_CreateControlApproval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("ratingColor","GREEN");
 				obj3.put("remarks","remarks");
 				obj3.put("residualImpactType","MEDIUM");
 				
 				request.body(obj3.toString());
 				
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/control/approva");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 105)
 	public void addControlAndMitigation_CreateControlApproval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("ratingColor","GREEN");
 				obj3.put("remarks","remarks");
 				obj3.put("residualImpactType","MEDIUM");
 				
 				request.body(obj3.toString());
 				
	 				Response response = request.get(URL+"/vendormgmt/1/workflow/control/approval");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 106)
 	public void addControlandMitigation_FindControlDetailswithID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/control/find/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
    

  @Test(dataProvider = "Execute_Module",priority = 107)
 	public void addControlandMitigation_FindControlDetailswithID_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/control/find/assesnt/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 108)
 	public void addControlandMitigation_FindControlDetailswithID_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/control/find/assessment/"+assessmentId);
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 109)
 	public void addControlAndMitigation_ModifyControl(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj1 = new JSONObject();
 				obj1.put("controlMitigation","test");
 				obj1.put("residualRiskRating","1");
 				obj1.put("nextAssessmentDueDate","1998-12-08T18:30:00.000Z");
 				obj1.put("remarks","test"); 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("controlId",ControlID);
 				obj3.put("assigneeID","BS1000015");
 				obj3.put("assigneeName","IT Head");
 				obj3.put("departmentID","CL00002");
 				obj3.put("departmentName","IT Department");
 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj3);
 				
 				obj1.put("assignees", arr1);
 				
 				request.body(obj1.toString());
 				
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/control/modify");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 110)
 	public void addControlAndMitigation_ModifyControl_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj1 = new JSONObject();
 				obj1.put("controlMitigation","test");
 				obj1.put("residualRiskRating","1");
 				obj1.put("nextAssessmentDueDate","1998-12-08T18:30:00.000Z");
 				obj1.put("remarks","test"); 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("controlId",ControlID);
 				obj3.put("assigneeID","BS1000015");
 				obj3.put("assigneeName","IT Head");
 				obj3.put("departmentID","CL00002");
 				obj3.put("departmentName","IT Department");
 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj3);
 				
 				obj1.put("assignees", arr1);
 				
 				request.body(obj1.toString());
 				
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/control/modif");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 111)
 	public void addControlAndMitigation_ModifyControl_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj1 = new JSONObject();
 				obj1.put("controlMitigation","test");
 				obj1.put("residualRiskRating","1");
 				obj1.put("nextAssessmentDueDate","1998-12-08T18:30:00.000Z");
 				obj1.put("remarks","test"); 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("controlId",ControlID);
 				obj3.put("assigneeID","BS1000015");
 				obj3.put("assigneeName","IT Head");
 				obj3.put("departmentID","CL00002");
 				obj3.put("departmentName","IT Department");
 				
 				
 				JSONArray arr1 = new JSONArray();
 				arr1.put(obj3);
 				
 				obj1.put("assignees", arr1);
 				
 				request.body(obj1.toString());
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/control/modify");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  
  


  @Test(dataProvider = "Execute_Module",priority = 112)
 	public void approveResidualRisk_L3Approval(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("approvalStatus","Approved");
 				obj3.put("comments","test");
 				
 				request.body(obj3.toString());
 				

 				for(int i=0;i<3;i++)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/control/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 					JSONObject obj4 = new JSONObject(response.getBody().asString());
	 					ApprovalLevel = obj4.getString("approvalLevel");
	 					
	 					if(ApprovalLevel.equals("L3")) 
	 					{
	 						test.log(LogStatus.INFO, "Vendor is approved to L3 level..");
	 						break;
	 					}
	 					else {
	 						test.log(LogStatus.INFO, "Vendor is not yet approve to L3..");
	 					}
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  


  @Test(dataProvider = "Execute_Module",priority = 113)
 	public void approveResidualRisk_L3Approval_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("approvalStatus","Approved");
 				obj3.put("comments","test");
 				
 				request.body(obj3.toString());
 				
	 				Response response = request.post(URL+"/vendormgmt/1/workflow/control/approval/status/ne");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 114)
 	public void approveResidualRisk_L3Approval_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("vendorId",vendorID);
 				obj3.put("assessmentId",assessmentId);
 				obj3.put("approvalStatus","Approved");
 				obj3.put("comments","test");
 				
 				request.body(obj3.toString());
 				

 				
	 				Response response = request.get(URL+"/vendormgmt/1/workflow/control/approval/status/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  
  // Unique ID Module..........................................................................................
  

  @Test(dataProvider = "Execute_Module",priority = 115)
 	public void uniqueID_FindAllPreferences(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/id/preference/find/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
						/*
						 * JSONObject obj6 = new JSONObject(response.getBody().asString());
						 * 
						 * JSONObject obj2 = new JSONObject(obj6.get("result").toString());
						 * 
						 * SeriesId = obj2.getInt("seriesId");
						 * 
						 * test.log(LogStatus.INFO, "SeriesId : "+SeriesId);
						 */
	 					
	 					//Delete Unique ID..............
	 					//uniqueID_DeleteID("Yes");
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
    

  //@Test(dataProvider = "Execute_Module",priority = 29)
 	public void uniqueID_FindAllPreferencesDetails(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				if(SeriesId == 0)
 				{	
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/id/preference/find/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 412)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				}
 				else
 				{
 					Response response = request.get(URL+"/vendormgmt/1/vendor/id/preference/find/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 116)
 	public void uniqueID_FindAllPreferencesDetails_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				if(SeriesId == 0)
 				{	
	 				Response response = request.get(URL+"/vendormgmt/1/vendor/id/preferenc/find/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				}
 				else
 				{
 					Response response = request.get(URL+"/vendormgmt/1/vendor/id/preference/fin/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 117)
 	public void uniqueID_FindAllPreferencesDetails_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				if(SeriesId == 0)
 				{	
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/id/preference/find/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				}
 				else
 				{
 					Response response = request.put(URL+"/vendormgmt/1/vendor/id/preference/find/all");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  
  
 	public void uniqueID_DeleteID(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("seriesId",SeriesId);
 				obj3.put("preferenceOganization","BCT");
 				obj3.put("preferenceYear","2023");
 				obj3.put("preferenceModule","Vendor");
 				obj3.put("runningSeries","VM");
 				obj3.put("status","N");
 				obj3.put("activeFlag","Y");
 				obj3.put("deleteFlag","N");
 				obj3.put("createdBy","WFU22222");
 				obj3.put("modifiedBy","Admin");
 				obj3.put("createdDate","2023-01-04T18:05:02.457+00:00");
 				obj3.put("modifiedDate","2023-01-04T18:05:02.457+00:00");
 				 				
 				request.body(obj3.toString());
 				
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/id/preference/remove");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 					SeriesId = 0;
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}
  
  

  @Test(dataProvider = "Execute_Module",priority = 118)
 	public void uniqueID_Create(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("organization","Yes");
 				obj3.put("preferenceModule","Vendor");
 				obj3.put("preferenceOganization","BCT");
 				obj3.put("preferenceYear","2023");
 				obj3.put("runningSeries","VM");
 				obj3.put("year","Yes");
 				 				
 				request.body(obj3.toString());
 				
 				if(SeriesId == 0)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/vendor/id/preference/find/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 200)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 119)
 	public void uniqueID_Create_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("organization","Yes");
 				obj3.put("preferenceModule","Vendor");
 				obj3.put("preferenceOganization","BCT");
 				obj3.put("preferenceYear","2023");
 				obj3.put("runningSeries","VM");
 				obj3.put("year","Yes");
 				 				
 				request.body(obj3.toString());
 				
 				if(SeriesId == 0)
 				{
	 				Response response = request.post(URL+"/vendormgmt/1/vendor/id/preferenc/find/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 404)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 120)
 	public void uniqueID_Create_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj3 = new JSONObject();
 				obj3.put("organization","Yes");
 				obj3.put("preferenceModule","Vendor");
 				obj3.put("preferenceOganization","BCT");
 				obj3.put("preferenceYear","2023");
 				obj3.put("runningSeries","VM");
 				obj3.put("year","Yes");
 				 				
 				request.body(obj3.toString());
 				
 			
	 				Response response = request.put(URL+"/vendormgmt/1/vendor/id/preference/find/new");
	 				System.out.println(response.getBody().asString());	
	 				System.out.println("Group details ........"+response.getBody().prettyPrint());
	 				
	 				if(response.getStatusCode() == 405)
	 				{			
	 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
	 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
	 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
	 					
	 				}
	 				else
	 				{
	 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
	 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
	 				}
 				
 				
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  

  
  @Test(dataProvider = "Execute_Module",priority = 121)
 	public void vendorRegister_CreateSinglevendor_ExistingVendorName(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 					
 					test.log(LogStatus.FAIL,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Expected: "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  

  @Test(dataProvider = "Execute_Module",priority = 122)
 	public void vendorRegister_CreateSinglevendor_InvalidEmailFormat(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				obj.put("vendorName",vendorname);
 				obj.put("status","Active");
 				obj.put("vendorIndustry","IT");
 				obj.put("productOrService","test");
 				obj.put("vendorCriticality","High");
 				obj.put("businessOwner","BS1000015");
 				obj.put("location","H.O");
 				obj.put("vendorEmail","abc@abc.com");
 				obj.put("vendorMobile","9090909090");
 				obj.put("vendorAddress","test");
 				obj.put("remarks","test");
 				obj.put("effectFrom","1998-12-15T18:30:00.000Z");
 				obj.put("validTill","2000-12-15T18:30:00.000Z");
 				obj.put("vendorContact","test");
 				
 				request.body(obj.toString());
 				
 				Response response = request.post(URL+"/vendormgmt/1/vendor/new");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 					
 					test.log(LogStatus.FAIL,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Expected: "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  

  @Test(dataProvider = "Execute_Module",priority = 123)
 	public void vendorRegister_DeActivevendor(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/inactive");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 200)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  


  @Test(dataProvider = "Execute_Module",priority = 124)
 	public void vendorRegister_DeActivevendor_InvalidEndpoint(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.put(URL+"/vendormgmt/1/vendor/inactiv");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 404)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
 			}
 		
 		}
 		catch(Exception ex)
 		{
 			System.out.println(ex);
 			test.log(LogStatus.FAIL, "Exception occured"+ex);
 		}	
 	}

  
  


  @Test(dataProvider = "Execute_Module",priority = 125)
 	public void vendorRegister_DeActivevendor_InvalidMethod(String Execute)
 	{
 		
 		try
 		{
 			if(Execute.equalsIgnoreCase("Yes")) {				
 			
 				String Tokens = getOAuthToken();
 				String arr[] = Tokens.split(",");
 				String WSR = arr[0];
 				String OAuth = arr[1];
 				
 				test.log(LogStatus.INFO, "My test is starting.....");	
 				
 				//RestAssured.baseURI = "http://192.168.1.77:8280";
 				  
 				RequestSpecification request = RestAssured.given().log().all().when();
 									
 				request.header("Content-Type","application/json");
 				request.header("Accept-Encoding", "gzip, deflate, br");
 				request.header("Connection","keep-alive");
 				request.header("Authorization","Bearer "+WSR);
 				request.header("Authorization","Bearer "+OAuth );
 				
 				
 				JSONObject obj = new JSONObject();
 				
 				obj.put("vendorId",vendorID);
 				
 				request.body(obj.toString());
 				
 				Response response = request.get(URL+"/vendormgmt/1/vendor/inactive");
 				System.out.println(response.getBody().asString());	
 				System.out.println("Group details ........"+response.getBody().prettyPrint());
 				
 				if(response.getStatusCode() == 405)
 				{			
 					test.log(LogStatus.PASS,"Response Code is : "+response.getStatusCode());
 					test.log(LogStatus.PASS,"Group API Response Time : "+response.getTime());
 					test.log(LogStatus.INFO,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				else
 				{
 					test.log(LogStatus.PASS,"Response Code is Not Expected: "+response.getStatusCode());
 					test.log(LogStatus.FAIL,"Group API Full Response : "+response.getBody().prettyPrint());
 				}
 				
 				test.log(LogStatus.INFO, "My test has been ended.....");
 			}
 			else  {
 				System.out.println("Test Not Executed...");
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