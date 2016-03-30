package com.intuit.nasa.api.tests.planetary;


import org.testng.annotations.Test;

import com.intuit.core.rest.HttpResponseStatusType;
import com.intuit.nasa.api.planetary.GetSounds;
import com.jayway.restassured.response.Headers;




public class GetPlanetrySoundsTest {
	

	@Test
	public void testWhenAPIKeyIsMissing(){
		GetSounds getSounds = new GetSounds();
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode("API_KEY_MISSING");
		getSounds.expectedErrorMessage("No api_key was supplied. Get one at https://api.nasa.gov");
		getSounds.callAPI();
	}

	
	@Test
	public void testWhenAPIKeyIsInvalid(){
		GetSounds getSounds = new GetSounds("hack_the_key");
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode("API_KEY_INVALID");
		getSounds.expectedErrorMessage("An invalid api_key was supplied. Get one at https://api.nasa.gov");
		getSounds.callAPI();
	}
	
	@Test
	public void testWhenAPIKeyIsValid(){
		GetSounds getSounds = new GetSounds("79U7LZO3OguHmTUfdN4UhOUVZOgMzzZwIfGeN3uM");
		getSounds.expectResponseStatus(HttpResponseStatusType.OK_200);
		String rs = getSounds.callAPI();
		String leftOverLimit = getSounds.getRateLimitRemaining();
		System.out.println(leftOverLimit);
		getSounds.validateResponseAgainstJSONSchema("/test/java/com/intuit/nasa/api/tests/planetary/schema.json");
	}

}
