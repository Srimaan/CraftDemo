package com.intuit.nasa.api.tests.planetary;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.intuit.core.rest.HttpResponseStatusType;
import com.intuit.nasa.api.planetary.GetSounds;

public class GetPlanetrySoundsTest {

	protected static final Logger LOGGER = Logger
			.getLogger(GetPlanetrySoundsTest.class);

	@Test
	public void testWhenAPIKeyIsMissing() {
		//LOGGER.info("================= Started  -  Test When API Key Is Missing ========================= \n");
		GetSounds getSounds = new GetSounds();
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode("API_KEY_MISSING");
		getSounds.expectedErrorMessage("No api_key was supplied. Get one at https://api.nasa.gov");
		getSounds.callAPI();	
		
	//	LOGGER.info("================= End  -  Test When API Key Is Missing ========================= \n");
		
	}

	@Test
	public void testWhenAPIKeyIsInvalid() {
		//LOGGER.info("================= Test When API Key Is Invalid =========================");
		GetSounds getSounds = new GetSounds("hack_the_key");
		getSounds.expectResponseStatus(HttpResponseStatusType.FORBIDDEN_403);
		getSounds.expectedErrorCode("API_KEY_INVALID");
		getSounds
				.expectedErrorMessage("An invalid api_key was supplied. Get one at https://api.nasa.gov");
		getSounds.callAPI();
	}

	@Test
	public void testWhenAPIKeyIsValid() {
		
		
	//	LOGGER.info("================= Test When API Key Is Valid =========================");
		GetSounds getSounds = new GetSounds(
				"79U7LZO3OguHmTUfdN4UhOUVZOgMzzZwIfGeN3uM");
		getSounds.expectResponseStatus(HttpResponseStatusType.OK_200);
		String rs = getSounds.callAPI();
		String leftOverLimit = getSounds.getRateLimitRemaining();
		System.out.println(leftOverLimit);
		getSounds.validateResponseAgainstJSONSchema("./schema.json");
		
	}

}
