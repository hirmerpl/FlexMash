package de.unistuttgart.ipvs.as.flexmash.models.nodered;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class FileOutputNode extends NodeREDNode {

	String filename;
	String appendNewline;
	boolean overwriteFile;
	
	public FileOutputNode(String id, String name, String type, String xCoordinate, String yCoordinate, String zCoordinate, String filename, String appendNewline, boolean overwriteFile, ArrayList<String> targets) {
		super(id, name, type, xCoordinate, yCoordinate, zCoordinate, targets);
		this.filename = filename;
		this.appendNewline = appendNewline;
		this.overwriteFile = overwriteFile;
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject nodeREDNode = super.toJSON();
		nodeREDNode.put("filename", filename);
		nodeREDNode.put("appendNewline", appendNewline);
		nodeREDNode.put("overwriteFile", overwriteFile);
		
		return nodeREDNode;
	}
}
