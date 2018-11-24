package com.holkem.messenger.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.MatrixParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("/injectdemo")
@Consumes(MediaType.TEXT_PLAIN)
@Produces(MediaType.TEXT_PLAIN)
public class InjectDemoResource {
	
	/* holkem: JAX-RS PARAMS annotations available to get/inject data from HTTP request to the resource method
	   1. @QueryParam
	   		/messages?start=0&size=2
	  		fxnName(@QueryParam("start") int start, @QueryParam("size") int size) {...}
	   2. @PathParam
	   		/messages/1 => /messages/{messageId}
	   		fxnName(@PathParam("messageId") long id)
	   3. @MatrixParam
	   		/injectdemo/annotations;param=value
	   		fxnName(@MatrixParam("param") String param)
	   4. @HeaderParam
	   		HTTP request header => customHeaderParam: customData
	   		fxnName(@HeaderParam("customHeaderParam") String headerParam)
	   		esp useful for sessionId
	   5. @CookieParam
	 */
	@GET
	@Path("/annotations")
	public String getParamsUsingAnnotations(
			@MatrixParam("param") String matrixParam,
			@HeaderParam("customHeaderParam") String headerParam,
			@CookieParam("name") String cookieParam) {
		return "Matrix param: " + matrixParam + 
			   "\ncustom header param: " + headerParam +
			   "\ncookie param: " + cookieParam;
	}
	
	/* holkem: JAX-RS CONTEXT annotations
	   1. @Context UriInfo uriInfo,
	   2. @Context HttpHeaders headers
	 */
	@GET
	@Path("context")
	public String getParamsUsingContext(
			@Context UriInfo uriInfo,
			@Context HttpHeaders headers) {
		String path = uriInfo.getAbsolutePath().toString();
		String cookies = headers.getCookies().toString();
		return "Path: " + path +
			   "\nCookie List: " + cookies;
	}
	
}
