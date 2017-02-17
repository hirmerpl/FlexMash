package de.unistuttgart.ipvs.as.flexmash.transformation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.bpelmappings.BPELMapper;
import de.unistuttgart.ipvs.as.flexmash.bpmn.BPMNmodel;


/**
 * class to convert a Data Mashup flow into an executable BPEL workflow
 */
public class MashupPlanToBPELConverter {

	HashMap<String, String> entriesMap = new HashMap<String,String>();

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
	
		
		String BPELWorkflow = BPELMapper.getBPELConfig("BPELHeader");
		
		try {
			JSONArray nodes = mashupFlowAsJSON.getJSONArray("nodes");
			JSONObject node;
			String type;
			JSONArray transitions;
			
			for (int i = 0; i < nodes.length(); i++) {
				node = nodes.getJSONObject(i);
				type = node.get("type").toString();
					transitions = node.getJSONArray("transitions");

				for (int k = 0; k < transitions.length(); k++) {
					if (!transitions.getJSONObject(k).getString("target").equals(null)) {
						String target = transitions.getJSONObject(k).getString("target");
						JSONObject newNode= new JSONObject("{type:"+target+"}");
						nodes.put(newNode);
					}
				}

				switch (type) {
				case "start":
					
					BPELWorkflow += createBPEL("start");
					break;
				case "end":
					BPELWorkflow += createBPEL("end");
					break;
				case "merge":
					entriesMap.put("criteria", node.getString("criteria"));
					BPELWorkflow += createBPEL("merge");
					break;
				case "dataSource_NYT":
					BPELWorkflow += createBPEL("dataSource_NYT");
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}
					entriesMap.put("category", category);
					break;
				case "dataSource_twitter":
					BPELWorkflow += createBPEL("dataSource_twitter");
					entriesMap.put("dataSource_twitter", node.getString("dataSource_twitterHashtag"));
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						entriesMap.put("NYTFilter", node.getString("filter_criteria"));
						BPELWorkflow += createBPEL("NYTFilter");
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {
						entriesMap.put("TwitterFilter", node.getString("filter_criteria"));
						BPELWorkflow += createBPEL("TwitterFilter");
						
					} else if (node.get("analyticstype").toString().equals("NE")) {
						System.out.println("NE Node reached");
						BPELWorkflow += createBPEL("NE");
					}
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BPELWorkflow +=  BPELMapper.getBPELConfig("response.assign");

			File file = new File(System.getenv("FLEXMASH")+"/files/DataMashupProcess.bpel");
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


				BPELWorkflow = assign +""+ invoke;
			}
		
		return BPELWorkflow;
	}

	/**
	 * getter for the parameters
	 * 
	 * @return the parameters as list
	 */
	public HashMap<String,String> getEntries() {
		return entriesMap;
	}
}
