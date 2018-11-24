package com.holkem.messenger.service;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.holkem.messenger.database.Database;
import com.holkem.messenger.exceptions.ErrorMessage;
import com.holkem.messenger.model.Comment;
import com.holkem.messenger.model.Message;

public class CommentService {
	
	private Map<Long, Message> messages;
	
	public CommentService() {
		this.messages = Database.getMessages(); 
	}
	
	public List<Comment> getAllComments(long messageId) {
		return new ArrayList<>(messages.get(messageId).getComments().values());
	}
	
	/* holkem: instead of throwing custom exception, will use jersey's 
	 * WebApplicationException. difference with using custom is that
	 * when WebApplicationException is thrown, jersey knew to catch 
	 * this without doing exception mapping like what needs to be 
	 * done for custom exception. use of NotFoundException instead
	 * of WebApplicationException adds checking that the error status
	 * ur returning is indeed a 404 error */
	public Comment getComment(long messageId, long commentId) {
		MessageService msgService = new MessageService();
		msgService.getMessage(messageId); // throws runtime exception when not found
		
		Comment comment = messages.get(messageId).getComments().get(commentId);
		if (comment == null) {
			ErrorMessage errMsg = new ErrorMessage(
					"Requested comment (id=" + commentId + ") not found.", 
					HttpURLConnection.HTTP_NOT_FOUND, 
					"http://holkem.com");
			Response response = Response.status(Status.NOT_FOUND).entity(errMsg).build();
			throw new WebApplicationException(response);
		}
		return comment;
	}
	
	public Comment addComment(long messageId, Comment comment) {
		Comment newComment = new Comment(comment.getComment(), comment.getAuthor());
		messages.get(messageId).getComments().put(newComment.getId(), newComment);
		return newComment;
	}
	
	public Comment updateComment(long messageId, Comment comment) {
		long id = comment.getId();
		if (id <= 0L) return null;
		
		Map<Long, Comment> comments = messages.get(messageId).getComments();
		if (!comments.containsKey(id)) {
			comment.setComment("ERR: this is not an existing id. nothing updated."); // TODO: handle throw error msgs
		} else {
			comments.replace(comment.getId(), comment);
		}
		return comment;
	}
	
	public Comment deleteComment(long messageId, long commentId) {
		return messages.get(messageId).getComments().remove(commentId);
	}
}
