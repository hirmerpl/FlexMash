package de.unistuttgart.ipvs.as.flexmash.transformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.org.apache.xml.internal.security.utils.HelperNodeList;
import com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent;

import de.unistuttgart.ipvs.as.flexmash.BPMN.BPMNmodel;
import de.unistuttgart.ipvs.as.flexmash.BPMN.Engine;
import de.unistuttgart.ipvs.as.flexmash.BPMN.ExecutionHelper;

/**
 * class to convert a Data Mashup flow into an executable BPMN workflow
 */
public class MashupPlantoBPMNConverter {

	HashMap<String, String> entriesMap = new HashMap<String, String>();

	/**
	 * Converts the JSONObjects of the Data Mashup flow into a BPMN flow
	 * 
	 * @param mashupFlowAsJSON
	 *            the Data Mashup flow as JSON
	 * 
	 * @return the output of the executed flow
	 * @throws IOException
	 */
	public BPMNmodel convert(JSONObject mashupFlowAsJSON) throws IOException {

		BPMNmodel BPMNWorkFlow = new BPMNmodel("MainProcess");
		Map<String, ArrayList<String>> outTransitionsMap = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> inTransitionsMap = new HashMap<String, ArrayList<String>>();
		ExecutionHelper Helper = new ExecutionHelper();

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
						Helper.addValues(outTransitionsMap, transitions.getJSONObject(k).getString("source"),
								transitions.getJSONObject(k).getString("target"));
						Helper.addValues(inTransitionsMap, transitions.getJSONObject(k).getString("target"),
								transitions.getJSONObject(k).getString("source"));
					}
				}

				BPMNWorkFlow.variables.put(node.getString("name") + "Out", null);
				ArrayList<String> pre = inTransitionsMap.get(node.getString("name"));
				BPMNWorkFlow.variables.put(node.getString("name")+"Pre", pre);
				//System.out.println("var name : "+ node.getString("name")+"Pre");
				switch (type) {
				case "start":
					StartEvent startEvent = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess, node.getString("name"),
							StartEvent.class);
					startEvent.setName(node.getString("name"));
					break;
				case "end":
					EndEvent endEvent = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess, node.getString("name"),
							EndEvent.class);
					endEvent.setName(node.getString("name"));
					break;
				case "merge":
					BPMNWorkFlow.variables.put(node.getString("name") + "In", node.getString("criteria"));
					ServiceTask merge = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					merge.setName("merge");
					merge.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.MergeExe");
					
					break;
				case "dataSource_NYT":
					ServiceTask NYTds = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					NYTds.setName("NYTds");
					NYTds.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.NYTdsExe");
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}
					BPMNWorkFlow.variables.put(node.getString("name") + "In", category);
					break;
				case "dataSource_twitter":
					ServiceTask Twitterds = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					Twitterds.setName("Twitterds");
					Twitterds.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.TwitterdsExe");
					BPMNWorkFlow.variables.put(node.getString("name") + "In",
							node.getString("dataSource_twitterHashtag"));
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						BPMNWorkFlow.variables.put(node.getString("name") + "In", node.getString("filter_criteria"));
						ServiceTask NYTFilter = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						NYTFilter.setName("NYTFilter");
						NYTFilter.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.NYTFilterExe");
					}else if(node.get("filtertype").toString().equals("CSVFilter")){
						BPMNWorkFlow.variables.put(node.getString("name") + "In", node.getString("filter_criteria"));
						ServiceTask CSVFilter = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						CSVFilter.setName("CSVFilter");
						CSVFilter.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.CSVFilterExe");
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {
						BPMNWorkFlow.variables.put(node.getString("name") + "In", node.getString("filter_criteria"));
						ServiceTask TwitterFilter = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						TwitterFilter.setName("TwitterFilter");
						TwitterFilter.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.TwitterFilterExe");

					} else if (node.get("analyticstype").toString().equals("NE")) {
						System.out.println("NE Node reached");

					}
					break;
				case "dataSource_CSV":
					
						BPMNWorkFlow.variables.put(node.getString("name") + "In", node.getString("dataSource_CSV_path"));
						ServiceTask CSV = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						CSV.setName("CSV");
						CSV.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.CSVExe");
					
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		replaceValues(outTransitionsMap, inTransitionsMap, BPMNWorkFlow);
		// Iterator for source nodes
		Iterator<Entry<String, ArrayList<String>>> outIter = outTransitionsMap.entrySet().iterator();
		while (outIter.hasNext()) {
			// oEntry <Source node, list of target nodes>
			Map.Entry<String, ArrayList<String>> oEntry = (Map.Entry<String, ArrayList<String>>) outIter.next();
			ArrayList<String> targets = oEntry.getValue();
			String sourceNode = oEntry.getKey().toString();

			for (Iterator<String> iterTargets = targets.iterator(); iterTargets.hasNext();) {
				String targetNode = iterTargets.next();
				BPMNWorkFlow.createSequenceFlow(BPMNWorkFlow.MainProcess,
						BPMNWorkFlow.ModelInstance.getModelElementById(sourceNode),
						BPMNWorkFlow.ModelInstance.getModelElementById(targetNode));

			}
		}

		BPMNWorkFlow.Validate();
		String filename = System.getenv("FLEXMASH") + "/files/DataMashupProcess.bpmn";
		File file = new File(filename);
		Bpmn.writeModelToFile(file, BPMNWorkFlow.ModelInstance);
		BPMNWorkFlow.fileName = filename;
		return BPMNWorkFlow;
	}

	public void replaceValues(Map<String, ArrayList<String>> outTransitionMap,
			Map<String, ArrayList<String>> inTransitionMap, BPMNmodel BPMNWorkFlow) {
		/* 
		 * This part is to remove all the nodes with multiple outgoing transitions and replace them with the 
		 * parallel gate in the list of sequence flows
		 * */
		
		// Iterator for source nodes
		ArrayList<String> replaceKeys = new ArrayList<>() ;
		Map<String, ArrayList<String>> outTransitionMapReplaced = new HashMap<String, ArrayList<String>>();
		
		for (Iterator<Entry<String, ArrayList<String>>> outIter = outTransitionMap.entrySet().iterator();outIter.hasNext();) {
			
			// oEntry <Source node, list of target nodes>
			Map.Entry<String, ArrayList<String>> oEntry = (Map.Entry<String, ArrayList<String>>) outIter.next();
			
			if(oEntry.getValue().size()>1 && !oEntry.getKey().startsWith("PG")){
				ParallelGateway parallel = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
						"PG"+oEntry.getKey(), ParallelGateway.class);
				parallel.setName("PG"+oEntry.getKey());
				
			BPMNWorkFlow.createSequenceFlow(BPMNWorkFlow.MainProcess,
					BPMNWorkFlow.ModelInstance.getModelElementById(oEntry.getKey()),parallel);
			outTransitionMapReplaced.put(parallel.getName(), oEntry.getValue());
			replaceKeys.add(oEntry.getKey());
			
			}
		}
		replaceKeys.forEach((k)-> outTransitionMap.remove(k));
		outTransitionMapReplaced.forEach((k,v)->outTransitionMap.put(k, v));
		replaceKeys.clear();
		
		/* 
		 * This part is to remove all the nodes with multiple incoming transitions and replace them with the 
		 * parallel gate in the list of sequence flows
		 * */
		for(Iterator<Entry<String, ArrayList<String>>> inIter = inTransitionMap.entrySet().iterator();inIter.hasNext(); ){
			Map.Entry<String, ArrayList<String>> iEntry = (Map.Entry<String, ArrayList<String>>) inIter.next();
			if(iEntry.getValue().size()>1){
				
				ParallelGateway parallel = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
						"PG"+iEntry.getKey(), ParallelGateway.class);
				parallel.setName("PG"+iEntry.getKey());
				
			BPMNWorkFlow.createSequenceFlow(BPMNWorkFlow.MainProcess,parallel,
			BPMNWorkFlow.ModelInstance.getModelElementById(iEntry.getKey()));
			
				for(Iterator<String>  sourceNodes = iEntry.getValue().iterator();sourceNodes.hasNext(); ){
					
					String sourceNode = sourceNodes.next();
					
					outTransitionMap.get(sourceNode).add(parallel.getName());
					outTransitionMap.get(sourceNode).remove(iEntry.getKey());
				}
				
				
			}
		}
		
	}

	public boolean MultipleIncomingNode(String nodeName, Map<String, ArrayList<String>> inTransitionsMap) {
		ArrayList<String> incomings = inTransitionsMap.get(nodeName);
		if (incomings.size() > 1)
			return true;
		else
			return false;

	}

	/**
	 * getter for the parameters
	 * 
	 * @return the parameters as list
	 */
	public HashMap<String, String> getEntries() {
		return entriesMap;
	}
}
