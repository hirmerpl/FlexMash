package de.unistuttgart.ipvs.as.flexmash.utils.http;

import org.json.JSONArray;
import org.json.JSONObject;

@Deprecated
public class RequestManager {

	/**
	 * Identifies the Request intentions
	 * 
	 * @param jsonWorkflow
	 * @return Boolean
	 */
	public boolean identifyInput(JSONObject jsonWorkflow) {
		boolean input = false;

		try {
			String identifier = jsonWorkflow.get("wrapper").toString();
			if (identifier.equals("true")) {
				input = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return input;
	}

	/**
	 * Retrieves Login Credentials for the Table name extraction
	 * 
	 * @param jsonWorkflow
	 * @return String-Array
	 */
	public String[] getTableLoginCredentials(JSONObject jsonWorkflow) {
		String[] entries = new String[4];
		String type;

		try {
			JSONObject node;
			JSONArray nodes = jsonWorkflow.getJSONArray("nodes");

			for (int i = 0; i < nodes.length(); i++) {
				node = nodes.getJSONObject(i);
				type = node.get("type").toString();

				if (type.equals("dataSource_mySQL")) {
					entries[0] = node.getString("dataSource_mySQLName");
					entries[1] = node.getString("dataSource_mySQLURL");
					entries[2] = node.getString("dataSource_mySQLUsername");
					entries[3] = node.getString("dataSource_mySQLPassword");
				}
			}

			System.out.println(entries[0]);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return entries;
	}

}
