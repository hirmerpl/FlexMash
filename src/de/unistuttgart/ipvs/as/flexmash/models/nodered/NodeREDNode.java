package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NodeREDNode {
	
	String id;
	String name;
	String type;
	String xCoordinate;
	String yCoordinate;
	String zCoordinate;
	ArrayList<String> targets;
	
	public NodeREDNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, ArrayList<String> targets) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.zCoordinate = zCoordinate;		
		this.targets = targets;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("name", name);
		nodeREDNode.put("x", xCoordinate);
		nodeREDNode.put("y", yCoordinate);
		nodeREDNode.put("z", zCoordinate);
		
		JSONArray connections = new JSONArray();
		JSONArray wires = new JSONArray();

		for (String connection : targets) {
			connections.put(connection);
		}

		wires.put(connections);
		nodeREDNode.put("wires", wires);
		
		return nodeREDNode;
	}
	
	public String getId() {
		return id;
	}
}