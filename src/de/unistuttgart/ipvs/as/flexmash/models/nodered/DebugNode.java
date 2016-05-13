package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class DebugNode extends NodeREDNode {

	String active;
	String console;
	String complete;
	
	public DebugNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String active, String console, String complete, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		
		this.active = active;
		this.console = console;
		this.complete = complete;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("active", active);
		nodeREDNode.put("console", console);
		nodeREDNode.put("complete", complete);

		return nodeREDNode;
	}
}
