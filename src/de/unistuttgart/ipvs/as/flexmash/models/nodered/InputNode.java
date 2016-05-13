package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class InputNode extends NodeREDNode {

	String topic;
	String payload;
	String payloadType;
	String repeat;
	String crontab;
	boolean once;
	String outputs;

	
	public InputNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String topic, String payload, String payloadType, String repeat, String crontab, boolean once, String outputs, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		this.topic = topic;
		this.payload = payload;
		this.payloadType = payloadType;
		this.repeat = repeat;
		this.crontab = crontab;
		this.once = once;
		this.outputs = outputs;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("topic", topic);
		nodeREDNode.put("payload", payload);
		nodeREDNode.put("payloadType", payloadType);
		nodeREDNode.put("repeat", repeat);
		nodeREDNode.put("crontab", crontab);
		nodeREDNode.put("once", once);		
		nodeREDNode.put("outputs", outputs);
		return nodeREDNode;
	}
}
