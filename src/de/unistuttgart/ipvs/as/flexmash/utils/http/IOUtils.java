package de.unistuttgart.ipvs.as.flexmash.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Utility class for input and output
 */
public class IOUtils {

	/**
	 * Clears all flows currently deployed in Node-RED
	 */
	public static void clearNodeRED() {
		try {

			JSONArray flow = new JSONArray();

			String body = flow.toString();

			URL url;

			url = new URL("http://localhost:1880/flows");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "UTF-8");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(body);

			writer.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			writer.close();
			reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method deploys the model to NodeRED
	 * 
	 * @param nodeREDModel
	 *            the model to be deployed
	 * @param situationTemplate
	 *            the situation template as XML
	 * @param doOverwrite
	 *            boolean determining whether the flow shall be overwritten or
	 *            not
	 */
	@SuppressWarnings("unchecked")
	public static void deployToNodeRED(JSONArray nodeREDModel) {
		try {

			JSONArray flow;

			flow = new JSONArray();

			JSONObject sheet = new JSONObject();

			// TODO create constant for tab
			sheet.put("type", "tab");
			sheet.put("id", "0");
			sheet.put("label", "0" + ": " + "FlexMash");

			// add the sheet definition and all other components to the node red flow
			flow.put(sheet);

			for (int i = 0; i < nodeREDModel.length(); i++) {
				flow.put(nodeREDModel.get(i));
			}

			String body = flow.toString();

			URL url = new URL("http://localhost:1880/flows");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "UTF-8");

			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());

			writer.write(body);

			writer.flush();

			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			writer.close();
			reader.close();

		} catch (IOException | JSONException e) {
			System.err.println("Could not process HTTP request.");
			e.printStackTrace();
		}
	}

	/**
	 * This methods gets the currently deployed flows in Node-RED
	 * 
	 * @param urlToRead
	 *            the url of Node-RED
	 * @return the current flow
	 */
	public static String getHTML(String urlToRead) {
		URL url;
		HttpURLConnection conn;
		BufferedReader rd;
		String line;
		String result = "";
		try {
			url = new URL(urlToRead);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = rd.readLine()) != null) {
				result += line;
			}
			rd.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
