package com.holkem.messenger.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import com.holkem.messenger.database.Database;

@XmlRootElement
public class Profile {
	private long id;
	private String firstName;
	private String lastName;
	private Date created;
	private String profileName;
	
	public Profile(String firstName, String lastName, String profileName) {
		this.id = Database.generateProfileId();
		this.firstName = firstName;
		this.lastName = lastName;
		this.created = new Date();
		this.profileName = profileName;
	}

	public Profile() {
	}

	public String getProfileName() {
		return profileName;
	}

	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
