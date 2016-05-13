package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class TwitterNode extends NodeREDNode {

	String twitter;
	String tags;
	String topic;
	String user;
	String outputs;
	
	public TwitterNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String twitter, String tags, String topic, String user, String outputs, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		this.twitter = twitter;
		this.tags = tags;
		this.topic = topic;
		this.user = user;
		this.outputs = outputs;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("twitter", twitter);
		nodeREDNode.put("tags", tags);
		nodeREDNode.put("topic", topic);
		nodeREDNode.put("user", user);
		nodeREDNode.put("outputs", outputs);
		
		return nodeREDNode;
	}
}
