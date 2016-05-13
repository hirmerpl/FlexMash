package de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils;

import java.util.ArrayList;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class contains utility methods to be used to generate the NodeRED JSON
 * model.
 */
public class NodeREDUtils {

	/**
	 * Generates a NodeRED-conform ID.
	 * 
	 * @return the id
	 */
	public static String generateNodeREDId() {
		String id = "";
		for (int i = 0; i < 8; i++) {

			Random rdm = new Random();
			int rdmInt = rdm.nextInt(10);

			if (rdmInt < 5) {
				char c = (char) (rdm.nextInt(26) + 'a');
				id += c;
			} else {
				id += rdm.nextInt(9);
			}
		}
		id += ".";
		for (int i = 0; i < 5; i++) {
			Random rdm = new Random();
			int rdmInt = rdm.nextInt(10);

			if (rdmInt < 5) {
				char c = (char) (rdm.nextInt(26) + 'a');
				id += c;
			} else {
				id += rdm.nextInt(9);
			}
		}

		return id;
	}

	/**
	 * Generates a NodeRED debug node that is used to display the output.
	 * 
	 * @param zCoordinate
	 *            the sheet the node shall be used in
	 * @return the debug node
	 * @throws JSONException 
	 */
	public static JSONObject generateDebugNode(String id, String x, String y, String zCoordinate) throws JSONException {

		JSONObject output = new JSONObject();
		output.put("id", id);
		output.put("type", "debug");
		output.put("name", "debug");
		output.put("active", true);
		output.put("console", "false");
		output.put("complete", "false");
		output.put("x", x);
		output.put("y", y);
		output.put("z", zCoordinate);

		JSONArray opWires = new JSONArray();
		JSONArray opConnections = new JSONArray();
		opWires.put(opConnections);
		output.put("wires", opWires);

		return output;
	}

	/**
	 * Generates a NodeRED inject node that is used to display the output.
	 * 
	 * @param zCoordinate
	 *            the sheet the node shall be used in
	 * @param situationTemplate
	 * @param debugNode
	 * @return the inject node
	 * @throws JSONException 
	 */
	public static JSONObject generateInputNode(String zCoordinate) throws JSONException {

		JSONObject input = new JSONObject();
		input.put("id", generateNodeREDId());
		input.put("type", "inject");
		input.put("name", "inject");
		input.put("topic", "");
		input.put("payload", "");
		input.put("payloadType", "date");
		input.put("repeat", "");
		input.put("crontab", "");
		input.put("once", false);
		input.put("x", "100");
		input.put("y", "75");
		input.put("z", zCoordinate);
		input.put("outputs", 1);

		JSONArray wires = new JSONArray();
		JSONArray connections = new JSONArray();

		wires.put(connections);
		input.put("wires", wires);

		return input;
	}

	/**
	 * Creates a NodeRED JSON node
	 * 
	 * @param id
	 *            the id of the node
	 * @param name
	 *            the name of the node
	 * @param type
	 *            the type of the node
	 * @param x
	 *            the x coordinate of the node
	 * @param y
	 *            the y coordinate of the node
	 * @param z
	 *            the sheet id
	 * 
	 * @return the node as JSONObject
	 * @throws JSONException 
	 */
	public static JSONObject createNodeREDFunctionNode(String id, String name, String type, String x, String y, String z, String outputs, String function) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);
		nodeREDNode.put("outputs", outputs);
		nodeREDNode.put("func", function);

		return nodeREDNode;
	}

	public static JSONObject createNodeREDFileOutputNode(String id, String type, String name, String filename, String appendNewline, boolean overwriteFile, String x, String y, String z) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();

		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("filename", filename);
		nodeREDNode.put("appendNewline", appendNewline);
		nodeREDNode.put("overwriteFile", overwriteFile);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);
		
		return nodeREDNode;
	}

	public static JSONObject createSentimentFunctionNode(String id, String name, String type, String x, String y, String z) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);

		return nodeREDNode;
	}
	
	public static JSONObject createDelayNode(String id, String name, String type, String pauseType, String timeout, String timeoutUnits,
			String rate, String rateUnits, String randomFirst, String randomLast, String randomUnits, String drop, String x, String y, String z) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("name", name);
		nodeREDNode.put("type", type);
		nodeREDNode.put("pauseType", pauseType);
		nodeREDNode.put("timeout", timeout);
		nodeREDNode.put("timeoutUnits", timeoutUnits);
		nodeREDNode.put("rate", rate);
		nodeREDNode.put("rateUnits", rateUnits);
		nodeREDNode.put("randomFirst", randomFirst);
		nodeREDNode.put("randomLast", randomLast);
		nodeREDNode.put("randomUnits",randomUnits);
		nodeREDNode.put("drop", drop);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);

		return nodeREDNode;
	}

	public static JSONObject createNodeREDHTTPNode(String id, String name, String type, String x, String y, String z, String method, String URL, String outputs) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);
		nodeREDNode.put("method", method);
		nodeREDNode.put("url", URL);
		nodeREDNode.put("outputs", outputs);

		return nodeREDNode;
	}

	public static JSONObject createXMLToJSONNode(String id, String type, String name, String x, String y, String z) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);
		
		return nodeREDNode;
	}

	public static JSONObject createNodeREDTwitterNode(String id, String name, String type, String x, String y, String z, String twitter, String tags, String topic, String user, String outputs) throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);
		nodeREDNode.put("twitter", twitter);
		nodeREDNode.put("tags", tags);
		nodeREDNode.put("topic", topic);
		nodeREDNode.put("user", user);
		nodeREDNode.put("outputs", outputs);

		return nodeREDNode;
	}

	public static JSONObject connectNode(JSONObject node, ArrayList<String> targets) throws JSONException {
		JSONArray utilConnections = new JSONArray();
		JSONArray utilWires = new JSONArray();

		for (String connection : targets) {
			utilConnections.put(connection);
		}

		utilWires.put(utilConnections);
		node.put("wires", utilWires);
		
		return node;
		
	}
}
