package de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils;

import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject generateDebugNode(String id, String x, String y, String zCoordinate) {

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
		opWires.add(opConnections);
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
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject generateInputNode(String zCoordinate) {

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
		// TODO: this is hard coded
		input.put("x", "100");
		input.put("y", "75");
		input.put("z", zCoordinate);

		JSONArray wires = new JSONArray();
		JSONArray connections = new JSONArray();

		// TODO connect the nodes
		// for (TContextNode sensorNode : situationTemplate.getSituation()
		// .getContextNode()) {
		// String sensorNodeId = sensorNode.getId();
		// connections.add(situationTemplate.getId() + "." + sensorNodeId);
		// }

		wires.add(connections);
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
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject createNodeREDNode(String id, String name, String type, String x, String y, String z) {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", x);
		nodeREDNode.put("y", y);
		nodeREDNode.put("z", z);

		return nodeREDNode;
	}
}
