package de.unistuttgart.ipvs.as.flexmash.servlet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import de.unistuttgart.ipvs.as.flexmash.managers.FacebookManager;
import de.unistuttgart.ipvs.as.flexmash.managers.GoogleManager;

@WebServlet("/DataMock")
/**
 * Servlet to communicate with the client
 */
public class ServiceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * receives a POST request from the client side
	 */
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String command = req.getParameter("command");
		String result = null;
		switch(command) {
			case "google": 
				//Get the query parameter for google plus
				String inQuery1 = req.getParameter("inQuery1");
				GoogleManager gm = new GoogleManager();
				//Retrieve the data from Google plus manager
				JSONObject gmObject = gm.performQuery(inQuery1);
				JSONArray gmData = new JSONArray();
				try {
					if (gmObject.has("users") && gmObject.getJSONArray("users") != null) {
						gmData = gmObject.getJSONArray("users");
					}
				} catch (Exception e) {
					//log me out
				}
				result = gmData.toString();
			break;
			case "facebook":
				//Get the query parameter for facebook
				String inQuery = req.getParameter("inQuery");
				FacebookManager fm = new FacebookManager();
				//Retrieve the data from Facebook manager
				JSONObject fmObject = fm.performQuery(inQuery);
				JSONArray fmData = new JSONArray();
				try {
					if (fmObject.has("users") && fmObject.getJSONArray("users") != null) {
						fmData  = fmObject.getJSONArray("users");
					}
				} catch (Exception e) {
					//log me out					
				}
				result = fmData.toString();
			break;
			case "merge":
				String data1 = req.getParameter("data1");
				String data2 = req.getParameter("data2");
				JSONArray first = new JSONArray();
				JSONArray second = new JSONArray();
				try {
					if (isJSONValid(data1)) {
						first = new JSONArray(data1);
					}
					if (isJSONValid(data2)) {
						second = new JSONArray(data1);
					}
					//Merge the data with union operator
					result = merge(first, second).toString();
				} catch (Exception e) {
					//log me out					
				}
			break;
			case "filter":
				String data = req.getParameter("data");
				JSONArray list = new JSONArray();
				try {
					if (isJSONValid(data)) {
						list = new JSONArray(data);
					}
					//Filter the results which are not person
					result = filter(list).toString();
				} catch (Exception e) {
					//log me out					
				}
			break;
			default: 
				result = "command me :)";
			break;
		}
		//Write the data back
		PrintWriter out = resp.getWriter();
		out.println(result);
	}

	private JSONArray merge(JSONArray arr1, JSONArray arr2)
	        throws JSONException {
	    JSONArray result = new JSONArray();
	    JSONObject temp = new JSONObject();
	    for (int i = 0; i < arr1.length(); i++) {
			JSONObject item = arr1.getJSONObject(i);
			String firstName = item.getString("firstName");
			String lastName = item.getString("lastName");
			String midleName = item.getString("midleName");
			String name = firstName + lastName + midleName;
			temp.put(name, item);
	    }
	    for (int i = 0; i < arr2.length(); i++) {
			JSONObject item = arr2.getJSONObject(i);
			String firstName = item.getString("firstName");
			String lastName = item.getString("lastName");
			String midleName = item.getString("midleName");
			String name = firstName + lastName + midleName;
			temp.put(name, item);
	    }
	    Iterator <String>it = temp.keys();
	    while(it.hasNext()) {
	    	result.put(temp.get(it.next()));
	    }
	    return result;
	}

	private JSONArray filter(JSONArray arr) throws JSONException {
		JSONArray result = new JSONArray();
		for (int i = 0; i < arr.length(); i++) {
			JSONObject item = arr.getJSONObject(i);
			String gender = item.has("gender") ? item.getString("gender") : null;
			if (gender == null || gender.equals("person")) {
				result.put(item);
			}
		}
		return result;
	}

	public boolean isJSONValid(String test) {
	    try {
	        new JSONObject(test);
	    } catch (JSONException ex) {
	        // edited, to include @Arthur's comment
	        // e.g. in case JSONArray is valid as well...
	        try {
	            new JSONArray(test);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
}