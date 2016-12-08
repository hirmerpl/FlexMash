package de.unistuttgart.ipvs.as.flexmash.transformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.BPELMappings.BPELMapper;
import de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils.FlowNode;

/**
 * class to convert a Data Mashup flow into an executable BPEL workflow
 */
public class MashupPlanToBPELConverter {

	String[] entries = new String[8];

	/**
	 * Converts the JSONObjects of the Data Mashup flow into a BPEL flow
	 * 
	 * @param mashupFlowAsJSON
	 *            the Data Mashup flow as JSON
	 * 
	 * @return the output of the executed flow
	 * @throws IOException 
	 */
	public String convert(JSONObject mashupFlowAsJSON) throws IOException {

		String startNode = null;
		String BPELWorkflow = BPELMapper.getBPELConfig("BPELHeader");
		
		try {
			JSONArray nodes = mashupFlowAsJSON.getJSONArray("nodes");
			JSONObject node;
			String type;
			JSONArray transitions;
			List<String> targets = null;

			for (int i = 0; i < nodes.length(); i++) {
				node = nodes.getJSONObject(i);
				type = node.get("type").toString();
				transitions = node.getJSONArray("transitions");

				for (int k = 0; k < transitions.length(); k++) {
					if (!transitions.getJSONObject(k).getString("target").equals(null)) {
						targets.add(transitions.getJSONObject(k).getString("target"));
					}
				}

				switch (type) {
				case "start":
					startNode = node.getString("name");
					BPELWorkflow += BPELMapper.getBPELConfig("start");
					break;
				case "end":
					BPELWorkflow += BPELMapper.getBPELConfig("end");
					break;
				case "merge":
					entries[7] = node.getString("criteria");
					BPELWorkflow += BPELMapper.getBPELConfig("merge");
					break;
				case "dataSource_NYT":
					BPELWorkflow += BPELMapper.getBPELConfig("dataSourceNYT");
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}
					entries[0] = category;
					break;
				case "dataSource_twitter":
					BPELWorkflow += BPELMapper.getBPELConfig("dataSourcetwitter");
					entries[4] = node.getString("dataSource_twitterHashtag");
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						entries[5] = node.getString("filter_criteria");
						BPELWorkflow += BPELMapper.getBPELConfig("NYTFilter");
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {
						entries[6] = node.getString("filter_criteria");
						BPELWorkflow += BPELMapper.getBPELConfig("TwitterFilter");
						
					} else if (node.get("analyticstype").toString().equals("NE")) {
						System.out.println("NE Node reached");
						BPELWorkflow += BPELMapper.getBPELConfig("NE");
					}
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BPELWorkflow +=  BPELMapper.getBPELConfig("response.assign");

			File file = new File("C:/Users/mahrous/Documents/GitHub/FlexMash/files/DataMashupProcess.bpel");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			writer.write(BPELWorkflow);

			writer.close();

		} catch (IOException e) {			
			e.printStackTrace();
		}

		return BPELWorkflow;
	}

	/**
	 * Creates the BPEL Code from a WorkflowNode Object
	 * 
	 * @param node
	 *            the node in the flow
	 * 
	 * @return the BPEL code
	 * @throws IOException 
	 */
	private static String createBPEL(String nodeType) throws IOException {
		String BPELWorkflow = "";
		String invoke = "";
		String assign = "";
		
			if (nodeType != null) {
				switch (nodeType) {
				case "start":
					break;
				case "end":
					break;
				case "merge":
					System.out.println("Join");
					assign = BPELMapper.getBPELConfig("join.assign"); 
					invoke = BPELMapper.getBPELConfig("join.invoke"); 
					break;
				case "NYTFilter":
					System.out.println("NYTFilter");
					assign = BPELMapper.getBPELConfig("NYTFilter.assign");
					invoke = BPELMapper.getBPELConfig("NYTFilter.invoke");
					break;
				case "TwitterFilter":
					System.out.println("TwitterFilter");
					assign = BPELMapper.getBPELConfig("TwitterFilter.assign");
					invoke = BPELMapper.getBPELConfig("TwitterFilter.invoke");
					break;
				case "dataSource_NYT":
					System.out.println("dataSource_NYT");
					assign = BPELMapper.getBPELConfig("dataSourceNYT.assign");
					invoke = BPELMapper.getBPELConfig("dataSourceNYT.invoke");
					break;
				case "dataSource_twitter":
					System.out.println("dataSource_twitter");
					assign = BPELMapper.getBPELConfig("dataSourcetwitter.assign");
					invoke = BPELMapper.getBPELConfig("dataSourcetwitter.invoke");
					break;
				case "NE":
					break;
				}

				BPELWorkflow += assign + invoke;
			}
		
		return BPELWorkflow;
	}

	/**
	 * getter for the parameters
	 * 
	 * @return the parameters as list
	 */
	public String[] getEntries() {
		return entries;
	}
}
