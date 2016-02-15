package de.unistuttgart.ipvs.as.flexmash.managers;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;

import org.json.JSONArray;
import org.json.JSONObject;


public class GoogleManager {

	private String uRL = "https://www.googleapis.com/plus/v1/people?query={0}&key=AIzaSyC5EJ6otc4O5T6XcF0o04HWNmYGQqNyQng";

	public JSONObject performQuery(String inQuery) {
		JSONObject result = new JSONObject();
		JSONArray users1 = new JSONArray();
        InputStream is;
		try {
			is = new URL(MessageFormat.format(uRL, inQuery)).openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
	        String jsonText = readAll(rd);
	        JSONObject json = new JSONObject(jsonText);
	        if (json.has("items")) {
	        	JSONArray users = json.getJSONArray("items");
	        	for(int i = 0; i < users.length(); i++) {
	        		JSONObject user = users.getJSONObject(i);
	        		JSONObject user1 = new JSONObject();
	        		user1.put("id", user.getString("id"));
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
	        		user1.put("firstName", firstName);
	        		user1.put("lastName", lastName);
	        		user1.put("midleName", midleName);
	        		user1.put("gender", user.getString("objectType"));
	        		user1.put("bio", "TODO");
	        		user1.put("link", user.getString("url"));
	        		users1.put(user1);
	        	}
	        }
			result.put("users", users1);
	        is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }
}