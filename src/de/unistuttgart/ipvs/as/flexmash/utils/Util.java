package de.unistuttgart.ipvs.as.flexmash.utils;

import java.io.IOException;
import java.io.Reader;

public class Util {

	/**
	 * Creates JSONObjects from a JSON String TODO: move to utils
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
	 * TODO: move to utils
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
	
}
