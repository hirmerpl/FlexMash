package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class HTTPNode extends NodeREDNode {

	String method;
	String url;
	String outputs;
	
	public HTTPNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String method, String url, String outputs, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		
		this.method = method;
		this.url = url;
		this.outputs = outputs;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("method", method);
		nodeREDNode.put("url", url);
		nodeREDNode.put("outputs", outputs);

		return nodeREDNode;
	}
}
