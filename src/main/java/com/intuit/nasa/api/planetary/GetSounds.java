package com.intuit.nasa.api.planetary;

import static org.hamcrest.Matchers.equalTo;

import com.intuit.core.rest.AbstractAPIMethod;


public class GetSounds extends AbstractAPIMethod {
	public GetSounds(){
		super();
		replaceUrlPlaceholder("base_url", "https://api.nasa.gov/");
	}
	
	public GetSounds(String api_key){
		super();
		replaceUrlPlaceholder("base_url", "https://api.nasa.gov/");
		addUrlParameter("api_key", api_key);
	}
	
	public void expectedErrorCode(String errorCode){
		
		expectResponseContains("error.code", equalTo(errorCode));		
	}
	
	public void expectedErrorMessage(String message){
		expectResponseContains("error.message", equalTo(message));
	}
	
	
}
