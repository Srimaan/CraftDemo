package com.intuit.nasa.api.planetary.sounds;

import static org.hamcrest.Matchers.equalTo;

import com.intuit.core.rest.AbstractAPIMethod;


public class GetSounds extends AbstractAPIMethod {
	public GetSounds(){
		super();
		replaceUrlPlaceholder("base_url", "https://api.nasa.gov/");
	}
	
	
	
	
	
	public GetSounds(GetSoundsErrorMessages api_key){
		super();
		replaceUrlPlaceholder("base_url", "https://api.nasa.gov/");
		addQueryParameter("api_key", api_key.info());
	}
	
	public void expectedErrorCode(GetSoundsErrorMessages errorCode){		
		expectResponseContains("error.code", equalTo(errorCode.info()));		
	}
	
	public void expectedErrorMessage(GetSoundsErrorMessages message){
		expectResponseContains("error.message", equalTo(message.info()));
	}
	
	
}
