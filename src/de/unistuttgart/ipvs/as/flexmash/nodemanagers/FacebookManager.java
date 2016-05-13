package de.unistuttgart.ipvs.as.flexmash.nodemanagers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import facebook4j.Facebook;
import facebook4j.FacebookFactory;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;

/**
 * Handles requests to the Facebook API
 */
public class FacebookManager {

	// TODO: config file
	private String appId = "924275140983422";
	private String appSecret = "d283e40aaf0671a99878e2c504a226ae";
	private String accessToken = "CAANIn5NzZBn4BADZCFvSubTN7WryLSttmLRXWZAlTmf2s4zbZCYY2OjnpWkZCRjRV4YIhhrAgOCZAdNgwW51qVIEohKpcU9R6a54JQQwfxiQvNVtTAsrIdwUpt5iFsBqhSlwYG6FrdrAxqJr8Ja8AtSZBukJvj2HokIIKZAfpqYY7dLg9lozMSI2";

	private final static Logger LOGGER = Logger.getLogger(FacebookManager.class.getName()); 
	
	/**
	 * Performs a query to the Facebook API in order to get user information 
	 * 
	 * @param inQuery
	 * 			the name of the user
	 * @return
	 * 			a list of matching users
	 */
	public JSONObject performQuery(String inQuery) {
		
		//make a facebook instance
		Facebook facebook = new FacebookFactory().getInstance();
		
		//configure the facebook instance
		facebook.setOAuthAppId(appId, appSecret);
		facebook.setOAuthAccessToken(new AccessToken(accessToken));
		
		//prepare the result
		JSONObject result = new JSONObject();
		JSONArray allUsers = new JSONArray();
		
		try {
			
			//Query the facebook API for user information
			ResponseList<User> users = facebook.searchUsers(inQuery);
			
			if (users != null && users.size() > 0) {
				for (User user: users) {
					// collect detailed user information
					JSONObject userInformation = new JSONObject();
					User extUser = facebook.getUser(user.getId());
					userInformation.put("id", extUser.getId());
					String firstName = "";
	        		String lastName = "";
	        		String midleName = "";
	        		String displayName = extUser.getName();
	        		
	        		//Consider the name is composed of First Middle Second and split it by space
	        		String[] name = displayName.split(" ");
	        		
	        		if(name.length == 1) {
	        			firstName = name[0];
	        		} else if (name.length == 2) {
	        			lastName = name[1];
	        		} else if (name.length == 3) {
	        			midleName = name[2];
	        		}
	        		
					userInformation.put("firstName", firstName);
					userInformation.put("lastName", lastName);
					userInformation.put("midleName", midleName);
					userInformation.put("gender", extUser.getGender());
					userInformation.put("bio", extUser.getBio());
					userInformation.put("link", "https://www.facebook.com/" + extUser.getId());
					allUsers.put(userInformation);
				}
				
				//put the user into result
				result.put("users", allUsers);
			}
			
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "The following exception occurred while accessing the Facebook API: " + e.getMessage());
			
			e.printStackTrace();
		}
		
		return result;
	}
}