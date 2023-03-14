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

public class VendorAssessment extends BaseTest {

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
   


  
  
  
  @Test(dataProvider = "Execute_Module",priority = 52)
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

  
    

  @Test(dataProvider = "Execute_Module",priority = 53)
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

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 54)
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

  
  


  @Test(dataProvider = "Execute_Module",priority = 55)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 56)
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


  
  

  @Test(dataProvider = "Execute_Module",priority = 57)
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


  
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 58)
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

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 59)
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

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 60)
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

  
  

  
  

  @Test(dataProvider = "Execute_Module",priority = 61)
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

  
  

  

  @Test(dataProvider = "Execute_Module",priority = 62)
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

  
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 63)
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

  
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 64)
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

  
  

  

  

  @Test(dataProvider = "Execute_Module",priority = 65)
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

  
  
  
  

  

  @Test(dataProvider = "Execute_Module",priority = 66)
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

  
  
  
  

  @Test(dataProvider = "Execute_Module",priority = 67)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 68)
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

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 69)
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

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 70)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 71)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 72)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 73)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 74)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 75)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 76)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 78)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 79)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 80)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 81)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 82)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 84)
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

  

  @Test(dataProvider = "Execute_Module",priority = 85)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 86)
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