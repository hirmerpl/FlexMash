package de.unistuttgart.ipvs.as.flexmash.transformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.ParallelGateway;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.bpmn.BPMNmodel;
import de.unistuttgart.ipvs.as.flexmash.utils.ExecutionHelper;

/**
 * class to convert a Data Mashup flow into an executable BPMN workflow
 */
/**
 * @author KMahrous
 *
 */
public class MashupPlantoBPMNConverter {

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
		System.out.println(mashupFlowAsJSON.toString());
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
					if (!transitions.getJSONObject(k).getString("target")
							.equals(null)) {
						Helper.addValues(outTransitionsMap,
								transitions.getJSONObject(k)
								.getString("source"),
								transitions.getJSONObject(k)
								.getString("target"));
						Helper.addValues(inTransitionsMap,
								transitions.getJSONObject(k)
								.getString("target"),
								transitions.getJSONObject(k)
								.getString("source"));
					}
				}

				BPMNWorkFlow.variables.put(node.getString("name") + "Out",
						null);
				ArrayList<String> pre = inTransitionsMap
						.get(node.getString("name"));
				BPMNWorkFlow.variables.put(node.getString("name") + "Pre", pre);

				switch (type) {
				case "start":
					StartEvent startEvent = BPMNWorkFlow.createElement(
							BPMNWorkFlow.MainProcess, node.getString("name"),
							StartEvent.class);
					startEvent.setName(node.getString("name"));
					break;
				case "end":
					EndEvent endEvent = BPMNWorkFlow.createElement(
							BPMNWorkFlow.MainProcess, node.getString("name"),
							EndEvent.class);
					endEvent.setName(node.getString("name"));
					break;
				case "dataSource_NYT":
					ServiceTask NYTds = BPMNWorkFlow.createElement(
							BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					NYTds.setName("NYTds");
					NYTds.setCamundaClass(
							"de.unistuttgart.ipvs.as.flexmash.bpmn.executables.NYTdsExe");
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}
					BPMNWorkFlow.variables.put(node.getString("name") + "In",
							category);
					break;
				case "dataSource_twitter":
					ServiceTask Twitterds = BPMNWorkFlow.createElement(
							BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					Twitterds.setName("Twitterds");
					Twitterds.setCamundaClass(
							"de.unistuttgart.ipvs.as.flexmash.bpmn.executables.TwitterdsExe");
					BPMNWorkFlow.variables.put(node.getString("name") + "In",
							node.getString("dataSource_twitterHashtag"));
					break;

				case "analytics":
					if (node.get("analyticstype").toString()
							.equals("Sentiment")) {
						BPMNWorkFlow.variables.put(
								node.getString("name") + "In",
								node.getString("filter_criteria"));
						ServiceTask TwitterFilter = BPMNWorkFlow.createElement(
								BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						TwitterFilter.setName("TwitterFilter");
						TwitterFilter.setCamundaClass(
								"de.unistuttgart.ipvs.as.flexmash.bpmn.executables.TwitterFilterExe");

					} else if (node.get("analyticstype").toString()
							.equals("NE")) {
						System.out.println("NE Node reached");

					}
					break;
				case "dataSource_CSV":

					BPMNWorkFlow.variables.put(node.getString("name") + "In",
							node.getString("dataSource_CSV_path"));
					ServiceTask CSV = BPMNWorkFlow.createElement(
							BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					CSV.setName("CSV");
					CSV.setCamundaClass(
							"de.unistuttgart.ipvs.as.flexmash.bpmn.executables.CSVExe");

					break;
				case "filter":
					if (node.get("subtype").toString().equals("NYT")) {
						BPMNWorkFlow.variables.put(
								node.getString("name") + "In",
								node.getString("criteria"));
						ServiceTask NYTFilter = BPMNWorkFlow.createElement(
								BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						NYTFilter.setName("NYTFilter");
						NYTFilter.setCamundaClass(
								"de.unistuttgart.ipvs.as.flexmash.bpmn.executables.NYTFilterExe");
						break;
					}

				default:
					String criteria;
					try {
						criteria = node.getString("criteria");
					} catch (Exception e) {
						criteria = "";
					}
					BPMNWorkFlow.variables.put(node.getString("name") + "In",
							criteria);
					ServiceTask genericService = BPMNWorkFlow.createElement(
							BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					genericService.setName(node.has("subtype")
							? node.getString("subtype").toString()
									: node.get("type").toString());
					genericService.setCamundaClass(
							"de.unistuttgart.ipvs.as.flexmash.bpmn.executables.GenericExe");
					break;

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		replaceValues(outTransitionsMap, inTransitionsMap, BPMNWorkFlow);
		// Iterator for source nodes
		Iterator<Entry<String, ArrayList<String>>> outIter = outTransitionsMap
				.entrySet().iterator();
		while (outIter.hasNext()) {
			// oEntry <Source node, list of target nodes>
			Map.Entry<String, ArrayList<String>> oEntry = outIter
					.next();
			ArrayList<String> targets = oEntry.getValue();
			String sourceNode = oEntry.getKey().toString();
			for (String targetNode : targets) {
				BPMNWorkFlow.createSequenceFlow(BPMNWorkFlow.MainProcess,
						BPMNWorkFlow.ModelInstance
						.getModelElementById(sourceNode),
						BPMNWorkFlow.ModelInstance
						.getModelElementById(targetNode));
			}
		}

		String guid = java.util.UUID.randomUUID().toString();
		BPMNWorkFlow.Validate();
		String filename = System.getenv("FLEXMASH") + "/files/DataMashupProcess"
				+ guid + ".bpmn";
		File file = new File(filename);
		Bpmn.writeModelToFile(file, BPMNWorkFlow.ModelInstance);
		BPMNWorkFlow.fileName = filename;
		return BPMNWorkFlow;
	}

	/**
	 * A method to handle replacing the node values with the parallel gateways
	 * when necessary in case of the existence of multiple incoming or outgoing
	 * transitions for one process
	 * 
	 * @param outTransitionMap
	 * @param inTransitionMap
	 * @param BPMNWorkFlow
	 */
	@SuppressWarnings("deprecation")
	public void replaceValues(Map<String, ArrayList<String>> outTransitionMap,
			Map<String, ArrayList<String>> inTransitionMap,
			BPMNmodel BPMNWorkFlow) {
		/*
		 * This part is to remove all the nodes with multiple outgoing
		 * transitions and replace them with the parallel gate in the list of
		 * sequence flows
		 */

		// Iterator for source nodes
		ArrayList<String> replaceKeys = new ArrayList<>();
		Map<String, ArrayList<String>> outTransitionMapReplaced = new HashMap<String, ArrayList<String>>();

		for (Entry<String, ArrayList<String>> entry : outTransitionMap
				.entrySet()) {

			// oEntry <Source node, list of target nodes>
			Map.Entry<String, ArrayList<String>> oEntry = entry;

			if (oEntry.getValue().size() > 1
					&& !oEntry.getKey().startsWith("PG")) {
				ParallelGateway parallel = BPMNWorkFlow.createElement(
						BPMNWorkFlow.MainProcess, "PG" + oEntry.getKey(),
						ParallelGateway.class);
				parallel.setName("PG" + oEntry.getKey());
				parallel.setCamundaAsync(false);
				BPMNWorkFlow.createSequenceFlow(
						BPMNWorkFlow.MainProcess, BPMNWorkFlow.ModelInstance
						.getModelElementById(oEntry.getKey()),
						parallel);
				outTransitionMapReplaced.put(parallel.getName(),
						oEntry.getValue());
				replaceKeys.add(oEntry.getKey());

			}
		}
		replaceKeys.forEach((k) -> outTransitionMap.remove(k));
		outTransitionMapReplaced.forEach((k, v) -> outTransitionMap.put(k, v));
		replaceKeys.clear();

		/*
		 * This part is to remove all the nodes with multiple incoming
		 * transitions and replace them with the parallel gate in the list of
		 * sequence flows
		 */
		for (Entry<String, ArrayList<String>> entry : inTransitionMap
				.entrySet()) {
			Map.Entry<String, ArrayList<String>> iEntry = entry;
			if (iEntry.getValue().size() > 1) {

				ParallelGateway parallel = BPMNWorkFlow.createElement(
						BPMNWorkFlow.MainProcess, "PG" + iEntry.getKey(),
						ParallelGateway.class);
				parallel.setName("PG" + iEntry.getKey());
				parallel.setCamundaAsync(false);
				BPMNWorkFlow.createSequenceFlow(BPMNWorkFlow.MainProcess,
						parallel, BPMNWorkFlow.ModelInstance
						.getModelElementById(iEntry.getKey()));

				for (String sourceNode : iEntry.getValue()) {

					outTransitionMap.get(sourceNode).add(parallel.getName());
					outTransitionMap.get(sourceNode).remove(iEntry.getKey());
				}

			}
		}

	}

	/**
	 * Method to check whether a node or process has Multiple incoming
	 * transitions
	 * 
	 * @param nodeName
	 * @param inTransitionsMap
	 * @return
	 */
	public boolean MultipleIncomingNode(String nodeName,
			Map<String, ArrayList<String>> inTransitionsMap) {
		ArrayList<String> incomings = inTransitionsMap.get(nodeName);
		if (incomings.size() > 1)
			return true;
		else
			return false;

	}

}
