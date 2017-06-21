package de.unistuttgart.ipvs.as.flexmash.utils;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

public class Util {

	/**
	 * Creates JSONObjects from a JSON String 
	 *  move to utils
	 * 
	 * @param jsonFlow
	 *            the JSON flow as string
	 * 
	 * @return JSONArray with JSONObjects
	 */
	public static org.json.JSONObject createJsonObjects(String jsonFlow) {
		org.json.JSONObject jsnObj = null;
		try {
			jsnObj = new org.json.JSONObject(jsonFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsnObj;
	}
	
	/**
	 * helper method to generate a database key
	 * 
	 *  move to utils
	 * 
	 * @return the generated key
	 */
	public static String generateKey() {
		final int STRING_LENGTH = 16;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < STRING_LENGTH; i++) {
			sb.append((char) ((int) (Math.random() * 26) + 97));
		}
		return sb.toString();
	}
	
	public static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }
	
	public static List<String> parseJSONArray(JSONArray array) throws JSONException{
		List<String> list = new ArrayList<String>();
		for (String item: array.toString().split(",")){
			list.add(item.replace("[", "").replace("]", "").replace("\"", ""));
		}
		return list;
		
	}
}
