package com.intuit.core.rest;

public enum HttpResponseStatusType
{
	OK_200(200, "OK"),
	ACCEPTED_202(202, "Accepted"),
	NO_CONTENT_204(204, "No Content"),
	BAD_REQUEST_400(400, "Bad Request"),
	UNAUTHORIZED_401(401, "Unauthorized"),
	FORBIDDEN_403(403, "Forbidden"),
	NOT_FOUND_404(404, "Not Found"),
	CONFLICT_409(409, "Conflict"),
	UNSUPPORTED_MEDIA_TYPE_415(415, "Unsupported Media Type"),
	EXPECTATION_FAILED_417(417, "Expectation Failed"),
	UNPROCESSABLE_ENTITY_422(422, "Unprocessable Entity"),
	TOO_MANY_REQUESTS_429(429,"Too Many Requests"); 


	private int code;
	
	private String message;

	HttpResponseStatusType(int code, String message)
	{
		this.code = code;
		this.message = message;
	}

	public int getCode()
	{
		return code;
	}

	public String getMessage()
	{
		return message;
	}
}