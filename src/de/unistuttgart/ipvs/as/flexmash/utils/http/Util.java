package de.unistuttgart.ipvs.as.flexmash.utils.http;

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
	
}
