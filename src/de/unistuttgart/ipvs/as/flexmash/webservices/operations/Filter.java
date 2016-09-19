package de.unistuttgart.ipvs.as.flexmash.webservices.operations;

import javax.jws.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebService(name = "Filter")
/**
 * web service that joins two data sources
 */
public class Filter {

	@WebMethod(operationName = "filter")
	@WebResult(name = "key")
	/**
	 * joins two data sources
	 * 
	 * @param key
	 *            data to be filtered
	 * @param criteria
	 *            the filter criteria
	 * @return the filtered data
	 */
	public String filter(@WebParam(name = "key") String key, @WebParam(name = "criteria") String criteria) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject tableContent = (JSONObject) parser.parse(key);
			JSONArray values = (JSONArray) tableContent.get("result");

			JSONObject result = new JSONObject();

			for (int i = 0; i < values.size(); i++) {
				JSONObject currColumn = (JSONObject) values.get(i);

				Object[] keys = currColumn.keySet().toArray();
				for (int j = 0; j < keys.length; j++) {
					if (currColumn.get(keys[j]).equals(criteria)) {
						result.put("result", currColumn);
					}
				}
			}

			return result.toJSONString();

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
