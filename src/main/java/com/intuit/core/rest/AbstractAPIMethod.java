package com.intuit.core.rest;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.xml.HasXPath;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.filter.log.LogDetail;
import com.jayway.restassured.filter.log.RequestLoggingFilter;
import com.jayway.restassured.response.Headers;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;


public abstract class AbstractAPIMethod {
	protected static final Logger LOGGER = Logger.getLogger(AbstractAPIMethod.class);
	protected String methodPath = null;
	protected HttpMethodType methodType = null;
	private StringBuilder bodyContent = null;
	protected Object response;
	public RequestSpecification request;
	public String actualRsBody;

	public void validateResponseAgainstJSONSchema(String schemaPath) {
		try {
		if (actualRsBody == null) {
			throw new RuntimeException(
					"Actual response body is null. Pleae make API call before validation response");
		}
		URL input = getClass().getClassLoader().getResource(schemaPath);
		JsonValidationUtils.validateJson(input, actualRsBody);
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
	Response rs;
	private String callGET() {
		LOGGER.info("-------------------------- Sending REQUEST --------------------------\n");
		request.filter(new RequestLoggingFilter(LogDetail.ALL));
		rs = request.get(methodPath);
		LOGGER.info("-------------------------- Received RESPONSE --------------------------\n");
		rs.prettyPeek();
		head=rs.headers();
		setTime(rs.getTime());
		actualRsBody = rs.asString();
		return actualRsBody;
	}
	
	public Headers getResponseHeader(){
		return head;
	}

	public int getRateLimitRemaining(){
		return Integer.parseInt(head.getValue("X-RateLimit-Remaining"));
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

	public void addQueryParameter(String key, String value) {
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
