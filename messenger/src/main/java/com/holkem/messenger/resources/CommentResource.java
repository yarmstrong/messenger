package com.holkem.messenger.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.holkem.messenger.model.Comment;
import com.holkem.messenger.service.CommentService;

/* holkem: @Path("/{messageId}/comments") is the starting point for all methods
 * of this resource. the use of @Path("/") is optional */

@Path("/")
public class CommentResource {
	CommentService commService = new CommentService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Comment> getAllComments(@PathParam("messageId") long messageId) {
		return commService.getAllComments(messageId);
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{commentId}")
	public Comment getComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId) {
		return commService.getComment(messageId, commentId);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Comment addComment(@PathParam("messageId") long messageId, Comment comment) {
		return commService.addComment(messageId, comment);
	}
	
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{commentId}")
	public Comment updateComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId, Comment comment) {
		comment.setId(commentId);
		return commService.updateComment(messageId, comment);
	}
	
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/{commentId}")
	public Comment deleteComment(@PathParam("messageId") long messageId,  @PathParam("commentId") long commentId) {
		return commService.deleteComment(messageId, commentId);
	}
}
