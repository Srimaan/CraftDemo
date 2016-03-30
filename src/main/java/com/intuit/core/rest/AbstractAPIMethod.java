package com.intuit.core.rest;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.xml.HasXPath;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Header;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

public abstract class AbstractAPIMethod {

	protected String methodPath = null;
	protected HttpMethodType methodType = null;
	private StringBuilder bodyContent = null;
	protected Object response;
	public RequestSpecification request;
	public String actualRsBody;

	public void validateResponseAgainstJSONSchema(String schemaPath) {

		if (actualRsBody == null) {
			throw new RuntimeException(
					"Actual response body is null. Pleae make API call before validation response");
		}
		File file;
		try {
		 file = new File(schemaPath);
			JsonValidationUtils.validateJson(file, actualRsBody);
		} catch (IOException | ProcessingException ex) {
			ex.printStackTrace();
		} 

	}

	public void replaceUrlPlaceholder(String placeholder, String value) {
		if (value != null) {
			methodPath = methodPath.replace("${" + placeholder + "}", value);
		} else {
			methodPath = methodPath.replace("${" + placeholder + "}", "");
		}
	}

	public String callAPI() {
	
		String response = null;
		switch (methodType) {
		case GET:
			response = callGET();
			break;




		default:
			break;
		}

		return response;
	}
	
	Headers head = new Headers();

	private String callGET() {
		Response rs = request.get(methodPath);
		head=rs.headers();
		setTime(rs.getTime());
		actualRsBody = rs.asString();
		rs.prettyPrint();
		return actualRsBody;
	}
	
	public Headers getResponseHeader(){
		return head;
	}

	public String getRateLimitRemaining(){
		return head.getValue("X-RateLimit-Remaining");
	}

	private void setTime(long time) {
		this.time = time;
	}

	private long time;

	public long getTime() {
		return time;
	}

	public void restClient()  {
		Properties prop = new Properties();
		String propFileName = "api.properties";
		InputStream input = getClass().getClassLoader().getResourceAsStream(propFileName);
		String typePath = getClass().getSimpleName();
		if (input != null) {
			try {
				prop.load(input);
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		if (prop.getProperty(typePath).contains(":")) {
			methodType = HttpMethodType.valueOf(prop.getProperty(typePath)
					.split(":")[0]);
			methodPath = prop.getProperty(typePath).split(":")[1];
		} else {
			methodType = HttpMethodType.valueOf(prop.getProperty(typePath));
		}

	}

	public AbstractAPIMethod() {
		restClient();
		request = given();

	}

	public void addContentType(String contentType) {
		request.contentType(contentType);
	}

	public void addUrlParameter(String key, String value) {
		if (value != null) {
			request.queryParam(key, value);
		}
	}

	public void addCookie(String key, String value) {
		request.given().cookie(key, value);
	}

	public void addCookies(Map<String, String> cookies) {
		request.given().cookies(cookies);
	}

	// Example setHeaders("Accept=application/json")
	public void setHeaders(String... headerKeyValues) {
		for (String headerKeyValue : headerKeyValues) {
			String key = headerKeyValue.split("=")[0];
			String value = headerKeyValue.split("=")[1];
			request.header(key, value);
		}
	}

	public void expectResponseStatus(HttpResponseStatusType status) {
		request.expect().statusCode(status.getCode());
		request.expect().statusLine(
				Matchers.containsString(status.getMessage()));
	}

	public <T> void expectResponseContains(Matcher<T> key, Matcher<T> value) {
		request.expect().body(key, value);
	}

	public void expectValueByXpath(String xPath, String value) {
		request.expect().body(Matchers.hasXPath(xPath),
				Matchers.containsString(value));
	}

	public void expectValueByXpath(String xPath, String value1, String value2) {
		request.expect().body(
				Matchers.hasXPath(xPath),
				Matchers.anyOf(Matchers.containsString(value1),
						Matchers.containsString(value2)));
	}

	public <T> void expectResponseContains(Matcher<T> value) {
		request.expect().body(value);
	}

	public <T> void expectResponseContains(String key, Matcher<T> value) {
		request.expect().body(key, value);
	}

	public <T> void expectResponseContainsXpath(String xPath) {
		request.expect().body(HasXPath.hasXPath(xPath));
	}

	public void expectInResponse(Matcher<?> matcher) {
		request.expect().body(matcher);
	}

	public void expectInResponse(String locator, Matcher<?> value) {
		request.expect().body(locator, value);
	}

	public String getMethodPath() {
		return methodPath;
	}

	public void setMethodPath(String methodPath) {
		RestAssured.reset();
		this.methodPath = methodPath;
	}

	public void setBodyContent(String content) {
		this.bodyContent = new StringBuilder(content);
	}

	public RequestSpecification getRequest() {
		return request;
	}

}
