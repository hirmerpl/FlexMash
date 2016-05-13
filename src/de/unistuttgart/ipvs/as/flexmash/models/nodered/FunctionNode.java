package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class FunctionNode extends NodeREDNode {

	String outputs;
	String function;
	
	public FunctionNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String outputs, String function, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		this.outputs = outputs;
		this.function = function;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("outputs", outputs);
		nodeREDNode.put("func", function);
		
		return nodeREDNode;
	}
}
