package com.holkem.messenger.database;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.holkem.messenger.model.Comment;
import com.holkem.messenger.model.Message;
import com.holkem.messenger.model.Profile;

public class Database {

	private static Map<Long, Message> messages = new HashMap<>();
	private static Map<String, Profile> profiles = new HashMap<>();
	private static Map<Long, Comment> comments = new HashMap<>();
	private static AtomicLong msgId;
	private static AtomicLong profileId;
	private static AtomicLong commentId;
	
	static {
		msgId = new AtomicLong(0L);
		profileId = new AtomicLong(0L);
		commentId = new AtomicLong(0L);
		
		Profile p1 = new Profile("firstname1", "lastname1", "profile1");
		Profile p2 = new Profile("firstname2", "lastname2", "profile2");
		Profile p3 = new Profile("firstname3", "lastname3", "profile3");
		profiles.put(p1.getProfileName(), p1);
		profiles.put(p2.getProfileName(), p2);
		profiles.put(p3.getProfileName(), p3);
		
		Message m1 = new Message("Message 1", "profile1");
		Message m2 = new Message("Message 2", "profile2");
		Message m3 = new Message("Message 3", "profile3");
		messages.put(m1.getId(), m1);
		messages.put(m2.getId(), m2);
		messages.put(m3.getId(), m3);
		
		Comment c1 = new Comment("msg1: comment1", "whoknows1");
		Comment c2 = new Comment("msg2: comment2", "whoknows1");
		Comment c3 = new Comment("msg1: comment3", "whoknows1");
		comments.put(c1.getId(), c1);
		comments.put(c3.getId(), c3);
		messages.get(m1.getId()).setComments(new HashMap<>(comments));
		
		comments.clear();
		comments.put(c2.getId(), c2);
		messages.get(m2.getId()).setComments(new HashMap<>(comments));
	}
	
	public static long generateMsgId() {
		/* holkem: AtomicLong completely changed int static and 
		 * the numbering now works fine */
		return msgId.incrementAndGet();
	}
	
	public static long generateProfileId() {
		return profileId.incrementAndGet();
	}
	
	public static long generateCommentId() {
		return commentId.incrementAndGet();
	}
	
	public static Map<Long, Message> getMessages() {
		return messages;
	}
	
	public static Map<String, Profile> getProfiles() {
		return profiles;
	}
}
