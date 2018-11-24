package com.holkem.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.holkem.messenger.database.Database;
import com.holkem.messenger.model.Profile;

public class ProfileService {
	private Map<String, Profile> profiles; 
	
	public ProfileService() {
		this.profiles  = Database.getProfiles();
	}

	// CRUD METHOD IMPL
	public List<Profile> getAllProfiles() {
		return new ArrayList<>(profiles.values());
	}
	
	public Profile getProfile(String profileName) {
		if (profileName.equals("")) return null;
		/* FIXME: need some msg to output that not existing */
		return profiles.get(profileName);
	}
	
	public Profile addProfile(Profile profile) {
		/* FIXME: putIfAbsent means if null returned, then it is existing
		 * or we can do duplicity checking ourselves and throw error */
		Profile newProfile = new Profile(profile.getFirstName(), profile.getLastName(), profile.getProfileName());
		profiles.putIfAbsent(newProfile.getProfileName(), newProfile);
		return newProfile;
	}
	
	public Profile updateProfile(Profile profile) {
		Profile oldProfile = profiles.get(profile.getProfileName());
		if (oldProfile == null) {
			/* FIXME: should throw error here */
			profile.setProfileName("ERR: this is not an existing profile. nothing updated.");
		} else {
			/* NOTE: assumption is "db-generated" data are not allowed to be changed in the client
			 * side, when they make a PUT request, these data are included so we will not be doing
			 * a copy first before replace. BUT to make sure, we're going to do the copy */
			profile.setCreated(oldProfile.getCreated());
			profile.setId(oldProfile.getId());
			profiles.replace(profile.getProfileName(), profile);
		}
		return profile;
	}
	
	public Profile deleteProfile(String profileName) {
		return profiles.remove(profileName);
	}	
}
