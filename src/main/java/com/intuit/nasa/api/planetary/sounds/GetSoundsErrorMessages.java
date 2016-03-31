package com.intuit.nasa.api.planetary.sounds;

public enum GetSoundsErrorMessages {
	
	API_KEY_MISSING_CODE("API_KEY_MISSING"),
	API_KEY_MISSING_MESSAGE("No api_key was supplied. Get one at https://api.nasa.gov"),
	
	API_KEY_INVALID_CODE("API_KEY_INVALID"),
	API_KEY_INVALID_MESSAGE("An invalid api_key was supplied. Get one at https://api.nasa.gov"),

	API_KEY_VALID("79U7LZO3OguHmTUfdN4UhOUVZOgMzzZwIfGeN3uM"),
	API_KEY_INVALID("HACK_ME"),
	API_KEY_DEMO("DEMO_KEY");
	
	
	private String info;

	GetSoundsErrorMessages(String info) {
        this.info = info;
    }

    public String info() {
        return info;
    }

}
