package de.unistuttgart.ipvs.as.flexmash.transformation;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.instance.EndEvent;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.camunda.bpm.model.bpmn.instance.StartEvent;
import org.json.JSONArray;
import org.json.JSONObject;

import com.sun.xml.internal.fastinfoset.stax.events.EndElementEvent;

import de.unistuttgart.ipvs.as.flexmash.BPMN.BPMNmodel;
import de.unistuttgart.ipvs.as.flexmash.BPMN.Engine;

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
		Map<String, String> transitionsMap = new HashMap<String, String>();
		
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
						transitionsMap.put(transitions.getJSONObject(k).getString("source"),
								transitions.getJSONObject(k).getString("target"));
					}
				}

				
				BPMNWorkFlow.variables.put(node.getString("name")+"Out", null);
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
					BPMNWorkFlow.variables.put(node.getString("name")+"In", node.getString("criteria"));
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
					BPMNWorkFlow.variables.put(node.getString("name")+"In", category);
					break;
				case "dataSource_twitter":
					ServiceTask Twitterds = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess, node.getString("name"),
							ServiceTask.class);
					Twitterds.setName("Twitterds");
					Twitterds.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.TwitterdsExe");
					BPMNWorkFlow.variables.put(node.getString("name")+"In", node.getString("dataSource_twitterHashtag"));
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						BPMNWorkFlow.variables.put(node.getString("name")+"In", node.getString("filter_criteria"));
						ServiceTask NYTFilter = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						NYTFilter.setName("NYTFilter");
						NYTFilter.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.NYTFilterExe");
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {
						BPMNWorkFlow.variables.put(node.getString("name")+"In", node.getString("filter_criteria"));
						ServiceTask TwitterFilter = BPMNWorkFlow.createElement(BPMNWorkFlow.MainProcess,
								node.getString("name"), ServiceTask.class);
						TwitterFilter.setName("TwitterFilter");
						TwitterFilter.setCamundaClass("de.unistuttgart.ipvs.as.flexmash.BPMN.TwitterFilterExe");

					} else if (node.get("analyticstype").toString().equals("NE")) {
						System.out.println("NE Node reached");

					}
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		Iterator<Entry<String, String>> iter = transitionsMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<String, String> mEntry = (Map.Entry<String, String>) iter.next();
			
			BPMNWorkFlow.createSequenceFlow(BPMNWorkFlow.MainProcess,
					BPMNWorkFlow.ModelInstance.getModelElementById(mEntry.getKey().toString()),
					BPMNWorkFlow.ModelInstance.getModelElementById(mEntry.getValue().toString()));
		}

		BPMNWorkFlow.Validate();
		String filename = System.getenv("FLEXMASH") + "/files/DataMashupProcess.bpmn";
		File file = new File(filename);
		Bpmn.writeModelToFile(file, BPMNWorkFlow.ModelInstance);
		
		Engine engine = new Engine();
		engine.deployProcessModel(filename, BPMNWorkFlow);
		engine.runProcessModel("MainProcess",BPMNWorkFlow);
		return BPMNWorkFlow;
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
