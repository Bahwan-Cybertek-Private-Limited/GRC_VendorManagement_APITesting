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



public class Dashboard extends BaseTest {

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