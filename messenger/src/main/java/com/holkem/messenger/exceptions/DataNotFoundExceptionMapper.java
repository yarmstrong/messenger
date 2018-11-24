package com.holkem.messenger.exceptions;

import java.net.HttpURLConnection;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException exception) {
		ErrorMessage errMsg = new ErrorMessage(exception.getMessage(), HttpURLConnection.HTTP_NOT_FOUND, "http://holkem.com");
		Response response = Response.status(Status.NOT_FOUND).entity(errMsg).build();
		return response;
	}

}
