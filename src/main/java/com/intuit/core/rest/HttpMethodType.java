package com.intuit.core.rest;


public enum HttpMethodType
{
	HEAD(1, "HEAD"),
	GET(2, "GET"),
	PUT(3, "PUT"),
	POST(4, "POST"),
	DELETE(5, "DELETE"),
	PATCH(6, "PATCH"),
	HTTP(7, "http");
	private int code;
	private String name;

	HttpMethodType(int code, String name)
	{
		this.code = code;
		this.name = name;
	}

	public int getCode()
	{
		return code;
	}

	public String getName()
	{
		return name;
	}

	public HttpMethodType get(String name)
	{
		return valueOf(name);
	}
}
