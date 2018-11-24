package com.holkem.messenger.model;

import java.util.Date;

import com.holkem.messenger.database.Database;

public class Comment {
	private long id;
	private String comment;
	private Date created;
	private String author;
	
	public Comment(String comment, String author) {
		this.id = Database.generateCommentId();
		this.comment = comment;
		this.created = new Date();
		this.author = author;
	}
	
	public Comment() { }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
}
