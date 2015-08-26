package de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils;

import java.util.ArrayList;

/**
 * Data model of a flow node
 */
public class FlowNode {
	public String name;
	public String type;
	public String source;
	public ArrayList<String> target;
	public String criteria;
	public String adress;
	public String user;
	public String password;
	public String table;
	public String hashtag;
	public String database_name;
	public String filter_criteria;
	public String category;

	public FlowNode() {
		target = new ArrayList<>();
	}
}
