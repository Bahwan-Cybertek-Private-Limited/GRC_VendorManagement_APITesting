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

public class VendorFindings extends BaseTest {

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

  
  
  
  @Test(dataProvider = "Execute_Module",priority = 86)
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

  
  



  @Test(dataProvider = "Execute_Module",priority = 86)
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

  
  
  



  @Test(dataProvider = "Execute_Module",priority = 86)
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

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  



  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  



  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  


  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 83)
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
  
  


  @Test(dataProvider = "Execute_Module",priority = 83)
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

  @Test(dataProvider = "Execute_Module",priority = 86)
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

  
  


  @Test(dataProvider = "Execute_Module",priority = 86)
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

  
  
  


  @Test(dataProvider = "Execute_Module",priority = 86)
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

  
  
  

  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 83)
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

  
  

  @Test(dataProvider = "Execute_Module",priority = 83)
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