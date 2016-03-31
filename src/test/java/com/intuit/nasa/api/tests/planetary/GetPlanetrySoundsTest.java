package com.intuit.nasa.api.tests.planetary;

import org.apache.log4j.Logger;
import org.hamcrest.Matchers;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.intuit.core.rest.HttpResponseStatusType;
import com.intuit.nasa.api.planetary.sounds.GetSounds;
import com.intuit.nasa.api.planetary.sounds.GetSoundsErrorMessages;

@Test(singleThreaded=true)
public class GetPlanetrySoundsTest {

	protected static final Logger LOGGER = Logger.getLogger(GetPlanetrySoundsTest.class);
	
	private int LIMIT = 39;
	
	private long currentTimeStamp = 0;
	
	private static int currentLimit = 0; // Put a logic in after class in order to push the currentLimit to DB and read before suite the limit.
	
	@BeforeClass
	public void readLimit(){
		//TODO : Code to put the limit in DB with Time Stamp
		currentLimit=0;  //Get the previous left over limit.
		currentTimeStamp = 0; // Get the previous execution time stamp
	}
	
	
	@AfterClass
	public void writeLimit(){
		//TODO : write back values to DB after all test case compelted.
		currentLimit=0;  //Get the previous left over limit.
		currentTimeStamp = 0; // Get the previous execution time stamp
	}
	/**
	 *  1. Test without API_KEY
	 	* Verify the HTTP Response Status Code as 403 (Forbidden).
	 	* Verify that Response has Error Code as "API_KEY_MISSING".
	 	* No api_key was supplied. Get one at https://api.nasa.gov.
	 	* 
	    */


	public void testWhenAPIKeyIsMissing() {
		GetSounds getSounds = new GetSounds();
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode(GetSoundsErrorMessages.API_KEY_MISSING_CODE);
		getSounds.expectedErrorMessage(GetSoundsErrorMessages.API_KEY_MISSING_MESSAGE);
		getSounds.callAPI();
	}
	
	
	
	/** 
	 *  2. Test with Invalid API_KEY
	 	* Verify the HTTP Response Status Code as 403 (Forbidden).
	 	* Verify that Response has Error Code as "API_KEY_INVALID".
	 	* Verify that Response has Error Message as "An invalid api_key was supplied. Get one at https://api.nasa.gov".
	 */


	public void testWhenAPIKeyIsInvalid() {
		GetSounds getSounds = new GetSounds(GetSoundsErrorMessages.API_KEY_INVALID);
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode(GetSoundsErrorMessages.API_KEY_INVALID_CODE);
		getSounds.expectedErrorMessage(GetSoundsErrorMessages.API_KEY_INVALID_MESSAGE);
		getSounds.callAPI();
		
	}
	
	/**
	 *  3. Test with only **q** query Parameter and No API_KEY
		Example : <https://api.nasa.gov/planetary/sounds?q=apollo> 
	 	* Verify that HTTP Response Status Code as 403.
	 	* Verify that Response has Error Code as "API_KEY_MISSING".
	 	* No api_key was supplied. Get one at https://api.nasa.gov.
	 	* 
	 	**/
	

	public void testWithOnlyQueryParam_AND_NoAPIKey() {
		GetSounds getSounds = new GetSounds();
		getSounds.addQueryParameter("q", "apollo");
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode(GetSoundsErrorMessages.API_KEY_MISSING_CODE);
		getSounds.expectedErrorMessage(GetSoundsErrorMessages.API_KEY_MISSING_MESSAGE);
		getSounds.callAPI();
	}
	
	
	
	/**
	 * 4. Test with Valid API_KEY with Query Parameter q=apollo
	 	* Verify that HTTP Response Status Code as 200.
	 	* Verify that Response is against JSON Schema. 
	 	* Verify that Response doesn't have any Error message
	 	* Verify that Response Header has X-RateLimit-Limit:1000 and X-RateLimit-Remaining:997
	 	* Verify that Response Header has X-RateLimit-Remaining decrement by 1
	 */

	
	public void testWhenAPIKeyIsValid() {
		GetSounds getSounds = new GetSounds(GetSoundsErrorMessages.API_KEY_VALID);
		getSounds.addQueryParameter("q", "apollo");
		getSounds.expectResponseStatus(HttpResponseStatusType.OK_200);
		getSounds.callAPI();
		getSounds.validateResponseAgainstJSONSchema("sounds-schema.json");
	}
	
	
	/**
	 * 5. Test with OVER_RATE_LIMIT (using DEMO_KEY)
	 	* Verify that HTTP Response Status Code as 429 (too many Parameters)
	 	* Assuming that we have already sent 40 Requests from the Same IP Address (DEMO_KEY Limit is 40).
	 	* Verify that Response Header has X-RateLimit-Limit:40 and X-RateLimit-Remaining:0
	*/
	
	// This test will only run when your DEMO_KEY Limit is not reached to 40;	
	// should have some kind of mechansim to reset the key limit 
	@Test(enabled=false)
	public void testWhenAPIKeyIsDemoAndLimit() throws InterruptedException {
		for(int i=1;i<=LIMIT+2;i++){
			if(i<=LIMIT){
				GetSounds getSounds = new GetSounds(GetSoundsErrorMessages.API_KEY_DEMO);
				getSounds.expectResponseStatus(HttpResponseStatusType.OK_200);
				getSounds.callAPI();
				Thread.sleep(2000); //Waiting for response being served. //TODO use mechanism to wait till the the response is received
				getSounds.validateResponseAgainstJSONSchema("sounds-schema.json");
			}
			else{
				GetSounds getSounds = new GetSounds(GetSoundsErrorMessages.API_KEY_DEMO);
				getSounds.expectResponseStatus(HttpResponseStatusType.TOO_MANY_REQUESTS_429);
				getSounds.callAPI();
			}
		}
	}
	
	/**6. Test with Valid API_KEY and Invalid Query Parameter value (junk)
	 	Example : <https://api.nasa.gov/planetary/sounds?api_key=79U7LZO3OguHmTUfdN4UhOUVZOgMzzZwITEST&q=junk
	 	* Verify that HTTP Response Status Code as 200.
	 	* Verify that Response count is 0.
	    */
	//This fails when my query parameter is values is invalid then it should return me 0 instead now it is returing me 10 for any query parameter

	public void testWhenAPIKeyIsValid_WithInvalidQueryParamValue() {
		GetSounds getSounds = new GetSounds(GetSoundsErrorMessages.API_KEY_VALID);
		getSounds.addQueryParameter("q", "junk");
		getSounds.expectResponseStatus(HttpResponseStatusType.OK_200);
		getSounds.expectResponseContains("count", Matchers.hasValue("0")); 
		getSounds.callAPI();
	}
	
	
	/** 7. Test with Valid API_KEY and Invalid Query Parameter Key (instead of q say z
		 Example : <https://api.nasa.gov/planetary/sounds?api_key=79U7LZO3OguHmTUfdN4UhOUVZOgMzzZwITEST&z=apollo
		* Verify that HTTP Response Status Code as 400 or 404.
	    * 
	    */
	//This will also fail as the system should validate the Query Param key as well in case if wrong key specified it should through either 400 or 404


	public void testWhenAPIKeyIsValid_WithInvalidQueryParamKey() {
		GetSounds getSounds = new GetSounds(GetSoundsErrorMessages.API_KEY_VALID);
		getSounds.addQueryParameter("z", "apollo");
		getSounds.expectResponseStatus(HttpResponseStatusType.BAD_REQUEST_400);
		getSounds.callAPI();
	}
	

	

}
