package com.holkem.messenger.rest.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.holkem.messenger.model.Message;

public class RestApiClient {
	private Client client = ClientBuilder.newClient();
	private WebTarget baseTarget = client.target("http://localhost:8080/messenger/webapi");
	private WebTarget messagesTarget = baseTarget.path("messages");
	private WebTarget singleMessageTarget = messagesTarget.path("{messageId}");
	
	/* holkem: sample of get and post request in 1 statement
	 * Response getResponse = singleMessageTarget
	 *			.resolveTemplate("messageId", "2")
	 *			.request(MediaType.APPLICATION_JSON)
	 *			.get();
	 * Message message = getResponse.readEntity(Message.class); // or use String.class
	 * to read the whole payload
	 *  
	 * Message newMessage = new Message("msg created in client", "whoknows2");
	 * Response postResponse = messagesTarget
	 *			.request()
	 *			.post(Entity.json(newMessage));
	 */
	
	public static void main(String[] args) {
		RestApiClient apiClient = new RestApiClient();
		
		// do get request
		Invocation getMessage = apiClient.prepareRequestForGetMessage("2");
		Response response = getMessage.invoke();
		Message message = response.readEntity(Message.class);
		System.out.println(message.getMessage());
		
		// do get request by year receiving a generic type of list
		getMessage = apiClient.prepareRequestForGetMessageByYear(2018);
		response = getMessage.invoke();
		List<Message> list = response.readEntity(new GenericType<List<Message>>() { });
		list.forEach((msg) -> System.out.println(msg.getMessage()));
		
		// do post request
		Invocation postMessage = apiClient.prepareRequestForPostMessage(
				new Message("msg created in client", "whoknows2"));
		Response postResponse = postMessage.invoke();		
		if (postResponse.getStatus() != Status.CREATED.getStatusCode()) {
			System.out.println("Error!");
		}
		Message createdMessage = postResponse.readEntity(Message.class);
		System.out.println(createdMessage.getMessage());
	}
	
	private Invocation prepareRequestForGetMessage(String id) {
		return singleMessageTarget
				.resolveTemplate("messageId", id)
				.request(MediaType.APPLICATION_JSON)
				.buildGet();
	}
	
	private Invocation prepareRequestForGetMessageByYear(int year) {
		return messagesTarget
				.queryParam("year", year)
				.request(MediaType.APPLICATION_JSON)
				.buildGet();
	}

	private Invocation prepareRequestForPostMessage(Message message) {
		return messagesTarget
				.request()
				.buildPost(Entity.json(message));
	}

}
