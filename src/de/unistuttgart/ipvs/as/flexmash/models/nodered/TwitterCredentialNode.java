package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import org.json.JSONException;
import org.json.JSONObject;

public class TwitterCredentialNode extends NodeREDNode {

	String id;
	String type;
	String screenName;
	
	public TwitterCredentialNode(String id, String type, String screenName) {
		// hack
		super(null, null, null, null, null, null, null);
		this.id = id;
		this.type = type;
		this.screenName = screenName;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = new JSONObject();
		nodeREDNode.put("id", id);
		nodeREDNode.put("type", type);
		nodeREDNode.put("screen_name", screenName);
		
		return nodeREDNode;
	}
}
