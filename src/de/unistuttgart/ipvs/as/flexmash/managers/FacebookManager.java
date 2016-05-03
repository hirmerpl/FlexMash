package de.unistuttgart.ipvs.as.flexmash.managers;

import org.json.JSONArray;
import org.json.JSONObject;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;

public class FacebookManager {

	private String appId = "924275140983422";
	private String appSecret = "d283e40aaf0671a99878e2c504a226ae";
	private String accessToken = "CAANIn5NzZBn4BADZCFvSubTN7WryLSttmLRXWZAlTmf2s4zbZCYY2OjnpWkZCRjRV4YIhhrAgOCZAdNgwW51qVIEohKpcU9R6a54JQQwfxiQvNVtTAsrIdwUpt5iFsBqhSlwYG6FrdrAxqJr8Ja8AtSZBukJvj2HokIIKZAfpqYY7dLg9lozMSI2";

	@SuppressWarnings("unchecked")
	public JSONObject performQuery(String inQuery) {
		//make a facebook instance
		Facebook facebook = new FacebookFactory().getInstance();
		//configure the facebook instance
		facebook.setOAuthAppId(appId, appSecret);
		facebook.setOAuthAccessToken(new AccessToken(accessToken));
		//prepare the result
		JSONObject result = new JSONObject();
		JSONArray users1 = new JSONArray();
		try {
			//Query the facebook API for user information
			ResponseList<User> users = facebook.searchUsers(inQuery);
			if (users != null && users.size() > 0) {
				for (User user: users) {
					//For each user collect the detail inforation
					JSONObject user1 = new JSONObject();
					User extUser = facebook.getUser(user.getId());
					user1.put("id", extUser.getId());
					String firstName = "";
	        		String lastName = "";
	        		String midleName = "";
	        		String displayName = extUser.getName();
	        		//Consider the name is composed of First Middle Second and split it by space
	        		String[] name = displayName.split(" ");
	        		if(name.length > 0) {
	        			firstName = name[0];
	        		}
	        		if(name.length > 1) {
	        			lastName = name[1];
	        		}
	        		if(name.length > 2) {
	        			midleName = name[2];
	        		}
					user1.put("firstName", firstName);
					user1.put("lastName", lastName);
					user1.put("midleName", midleName);
					user1.put("gender", extUser.getGender());
					user1.put("bio", extUser.getBio());
					user1.put("link", "https://www.facebook.com/" + extUser.getId());
					users1.put(user1);
				}
				//put the user into result
				result.put("users", users1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}