package com.holkem.messenger.model;

import java.net.URI;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.holkem.messenger.resources.CommentResource;
import com.holkem.messenger.resources.ProfileResource;
import com.holkem.messenger.service.MessageService;

/* holkem: @Path("/") may or may not included / doesnt matter */
/* holkem: @Consumes and @Produces 
 * @Consumes(MediaType.APPLICATION_JSON)
 * @Produces({MediaType.APPLICATION_JSON})
 * if all of ur methods deal with JSON, and dont explicitly add the 
 * @Consumes and @Produces anymore. just put them in the class annotation
 */
@Path("/messagesbackup")
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Consumes(MediaType.APPLICATION_JSON)
public class MessageResourceBackup { 
	MessageService msgService = new MessageService(); 
	
	/* holkem: the main API for getting list of messages will be implementing 
	 * retrieval of query param to determine the appropriate get message API
	 * to use 
	 *  /messages and /messages?abcd will always be using the /message URI
	 *  it is now ur task to differentiate the request using the queryParams
	 *  it is important that u know the param name that u are expecting bec
	 *  any dummy will only result to not getting ur expected param
	 */
	/* holkem: Pro-active Content Negotiation
	 * "The target resource does not have a current representation that would
	 * be acceptable to the user agent, according to the pro-active negotiation
	 * header fields received in the request, and the server is unwilling to
	 * supply a default representation."
	 */
	@GET
	public List<Message> getMessages(
			@QueryParam("year") int year,
			@QueryParam("start") int start,
			@QueryParam("size") int size) {
		if (year > 0) {
			return msgService.getAllMessagesByYear(year);
		}
		if (start>= 0 && size > 0) {
			return msgService.getAllMessagesPaginated(start, size);
		}
		return msgService.getAllMessages();
	}
	
	
	/* holkem: JAX-RS BEAN param class usage : another implementation of getMessages()
	 * using @BeanParam() either less arg or do multiple getters on the bean obj  
	 public List<Message> getMessages(@BeanParam MessageFilterBean filterBean) {
		if (filterBean.getYear() > 0) {
			return msgService.getAllMessagesByYear(filterBean.getYear());
		}
		if (filterBean.getStart() >= 0 && filterBean.getSize() > 0) {
			return msgService.getAllMessagesPaginated(filterBean.getStart(), filterBean.getSize());
		}
		return msgService.getAllMessages();
	} */
	
	@GET
	/* holkem: JAXB is native in java jdk so no need to download. but json is not, so need
	 * update maven, pom.xml to add json dependency, update maven project, run as maven
	 * install and rebuild */
	/* holkem: updated to implement HATEOAS */
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo) { 
		/* holkem: jersey passes a String param into your preferred parameter type 
		 * by doing the conversion for u. u dont need to call Long.parseLong(str) */
		Message hateoasMsg = msgService.getMessage(id);
		hateoasMsg.addLink(getUrlForSelf(uriInfo, hateoasMsg), "self");
		hateoasMsg.addLink(getUrlForProfile(uriInfo, hateoasMsg), "profile");
		hateoasMsg.addLink(getUrlForComments(uriInfo, hateoasMsg), "comments");
		return hateoasMsg;
	}

	private String getUrlForSelf(UriInfo uriInfo, Message msg) {
		URI uri = uriInfo.getBaseUriBuilder()
						 .path(MessageResourceBackup.class)
						 .path(String.valueOf(msg.getId()))
						 .build();
		return uri.toString();
	}
	
	private String getUrlForProfile(UriInfo uriInfo, Message msg) {
		URI uri = uriInfo.getBaseUriBuilder()
						 .path(ProfileResource.class)
						 .path(msg.getAuthor())
						 .build();
		return uri.toString();
	}
	
	private String getUrlForComments(UriInfo uriInfo, Message msg) {
		URI uri = uriInfo.getBaseUriBuilder()
				 .path(MessageResourceBackup.class)
				 .path(MessageResourceBackup.class, "getCommentResource")
				 .path(CommentResource.class)
				 .resolveTemplate("messageId", msg.getId())
				 .build();
		return uri.toString();
	}

	@POST
	public Response addMessage(Message message, @Context UriInfo uriInfo) {
		Message newMsg = msgService.addMessage(message);
		URI uri = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newMsg.getId())).build();
		Response response = Response.created(uri) // 201 Created
									.entity(newMsg) // the body
									.build();
		return response;  
	}
	/* holkem: @Context UriInfo uriInfo path methods
	 * uriInfo.getAbsolutePath(): http://localhost:8080/messenger/webapi/messages/
	 * uriInfo.getPath(): messages/
	 * uriInfo.getBaseUri(): http://localhost:8080/messenger/webapi/
	 * uriInfo.getRequestUri(): http://localhost:8080/messenger/webapi/messages/
	 * target 'Location' header is .../{messageId}
	 */
	
	@PUT
	@Path("/{messageId}")
	public Message updateMesage(@PathParam("messageId") long id, Message message) {
		message.setId(id);
		return msgService.updateMessage(message);
	}
	
	@DELETE
	@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
	@Path("/{messageId}")
	public Message deleteMessage(@PathParam("messageId") long id) {
		System.out.println("deleteMessage() called");
		return msgService.deleteMessage(id);
	}
	
	@Path("/{messageId}/comments")
	public CommentResource getCommentResource() {
		return new CommentResource();
	}
}
