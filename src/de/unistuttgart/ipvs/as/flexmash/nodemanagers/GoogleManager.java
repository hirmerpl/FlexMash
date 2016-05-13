package de.unistuttgart.ipvs.as.flexmash.nodemanagers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.utils.Util;

/**
 * Handles requests to the Google API
 */
public class GoogleManager {

	private static final String URL = "https://www.googleapis.com/plus/v1/people?query={0}&key=AIzaSyC5EJ6otc4O5T6XcF0o04HWNmYGQqNyQng";
	private final static Logger LOGGER = Logger.getLogger(GoogleManager.class.getName()); 

	/**
	 * Performs a query to the Google API in order to get user information 
	 * 
	 * @param inQuery
	 * 			the name of the user
	 * @return
	 * 			a list of matching users
	 */
	public JSONObject performQuery(String inQuery) {
		
		JSONObject result = new JSONObject();
		JSONArray users = new JSONArray();
        
		InputStream is;
		
		try {
			is = new URL(MessageFormat.format(URL, inQuery)).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	        String jsonText = Util.readAll(rd);
	        JSONObject json = new JSONObject(jsonText);
	        if (json.has("items")) {
	        	JSONArray allUsers = json.getJSONArray("items");
	        	for(int i = 0; i < allUsers.length(); i++) {
	        		JSONObject user = allUsers.getJSONObject(i);
	        		JSONObject userInformation = new JSONObject();
	        		userInformation.put("id", user.getString("id"));
	        		String firstName = "";
	        		String lastName = "";
	        		String midleName = "";
	        		String displayName = user.getString("displayName");
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
	        		userInformation.put("firstName", firstName);
	        		userInformation.put("lastName", lastName);
	        		userInformation.put("midleName", midleName);
	        		userInformation.put("gender", user.getString("objectType"));
	        		userInformation.put("link", user.getString("url"));
	        		users.put(userInformation);
	        	}
	        }
	        
			result.put("users", users);
	        is.close();
	        
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "The following exception occurred while accessing the Google API: " + e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
}