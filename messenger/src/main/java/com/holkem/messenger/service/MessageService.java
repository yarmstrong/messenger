package com.holkem.messenger.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.holkem.messenger.database.Database;
import com.holkem.messenger.exceptions.DataNotFoundException;
import com.holkem.messenger.model.Message;

public class MessageService {
	
	private Map<Long, Message> messages;
	
	public MessageService() {
		this.messages = Database.getMessages(); 
	}
	
	/* holkem: CRUD methods. create, read, update, delete */

	public List<Message> getAllMessages() {
		return new ArrayList<>(messages.values());
	}
	
	public List<Message> getAllMessagesByYear(int year) {  
		Stream<Message> st = messages.values().stream();
		List<Message> filteredList = st.filter(msg -> {
			LocalDate locDate = msg.getCreated().toInstant()
								   .atZone(ZoneId.systemDefault())
								   .toLocalDate();
			int yr = locDate.getYear();
			return (yr == year);
		}).collect(Collectors.toList());
		/* holkem: after Stream<T> initialization, all other methods
		 * to be performed on the stream (ie filter and transform into
		 * a List<T>) must be executed in a chain method calls. bec 
		 * stream represents a single-use sequence of data. must be 
		 * operated on only once. or throw IllegalStateException if
		 * being re-used again. must perform 1 single operation that
		 * will consume the stream and then close it. NOTE: the use of
		 * Supplier<Sytream<T>> will not fix the issue bec separate
		 * supplier.get().filter and supplier.get().collect will 
		 * operate on the same stream. so the collected list is still
		 * the original and not the filtered one */
		return filteredList; 
	}
	
	public List<Message> getAllMessagesPaginated(int start, int size) {  
		List<Message> list = new ArrayList<>(messages.values());
		int end = ((start + size) > list.size()) ? list.size() : (start + size);
		return list.subList(start, end);
	}
	
	/* holkem: updated to throw DataNotFoundException custom exception which
	 * resource will not catch so jersey will catch and send our custom error
	 * message by checking @Provider for the DataNotFoundExceptionMapper 
	 * exception mapping which returns a Response with error code and 
	 * entity of custom ErrorMessage object */
	public Message getMessage(long id) {
		Message message = messages.get(id);
		if (message == null) {
			throw new DataNotFoundException("Requested message (id=" + id + ") not found.");
		}
		return message;
	}
	
	public Message addMessage(Message message) {
		Message newMsg = new Message(message.getMessage(), message.getAuthor());
		messages.put(newMsg.getId(), newMsg);
		return newMsg;
	}
	
	public Message updateMessage(Message message) {
		long id = message.getId();
		if (id <= 0L) return null;
		if (!messages.containsKey(id)) {
			message.setMessage("ERR: this is not an existing id. nothing updated."); // TODO: handle throw error msgs
		} else {
			messages.replace(message.getId(), message);
		} 
		return message;
	}
	
	public Message deleteMessage(long id) {	
		return messages.remove(id);
	}
}
