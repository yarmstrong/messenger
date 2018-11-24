package com.holkem.messenger.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.bind.annotation.JsonbTransient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.holkem.messenger.database.Database;

@XmlRootElement
public class Message {
	private long id;
	private String message;
	private Date created;
	private String author;
	private Map<Long, Comment> comments;
	private List<Link> links;
	
	public Message(String message, String author) {
		this.id = Database.generateMsgId();
		this.message = message;
		this.author = author;
		this.created = new Date();
		this.comments = new HashMap<>();
		this.links = new ArrayList<>();
	}
	
	/* holkem: always always provide a no-arg constructor when doing
	 * XML/JSON conversion */
	public Message() { }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	
	/* holkem: @XmlTransient and @JsonbTransient
	 * annotate to denote that u dont want XML to show comments data 
	 * when message obj is pulled up (getter) from the API. For the
	 * corresponding setter, u dont have to since API will never get
	 * comments data in the first place. Also if u add it,  internal
	 * server error is encountered.
	 * NOTE: if jersey-media-json-jackson, @XmlTransient works for 
	 * both xml and json
	 */
	@XmlTransient
	@JsonbTransient
	public Map<Long, Comment> getComments() {
		return comments;
	}
	
	public void setComments(Map<Long, Comment> comments) {
		this.comments = comments;
	} 
	
	public List<Link> getLinks() {
		return links;
	}
	
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	
	public void addLink(String url, String rel) {
		Link link = new Link(url, rel);
		links.add(link);
	}

	@Override
	public String toString() {
		String str = "{" +
			"\nmsg:" + message +  
			"\ncreated:" + created +
			"\nauthor:" + author +
			"\nid: " + id + 
		"\n}";
		return str;
	}
}
