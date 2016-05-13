package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class DelayNode extends NodeREDNode {

	String pauseType;
	String timeout;
	String timeoutUnits;
	String rate;
	String rateUnits;
	String randomFirst;
	String randomLast;
	String randomUnits;
	String drop;
	
	public DelayNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String pauseType, String timeout, String timeoutUnits, String rate, String rateUnits, String randomFirst, String randomLast, String randomUnits, String drop, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		this.pauseType = pauseType;
		this.timeout = timeout;
		this.timeoutUnits = timeoutUnits;
		this.rate = timeoutUnits;
		this.rateUnits = timeoutUnits;
		this.randomFirst = timeoutUnits;
		this.randomLast = timeoutUnits;
		this.randomUnits = timeoutUnits;
		this.drop = timeoutUnits;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("pauseType", pauseType);
		nodeREDNode.put("timeout", timeout);
		nodeREDNode.put("timeoutUnits", timeoutUnits);
		nodeREDNode.put("rate", rate);
		nodeREDNode.put("rateUnits", rateUnits);
		nodeREDNode.put("randomFirst", randomFirst);		
		nodeREDNode.put("randomLast", randomLast);
		nodeREDNode.put("randomUnits", randomUnits);
		nodeREDNode.put("drop", drop);
		return nodeREDNode;
	}
}
