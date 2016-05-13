package de.unistuttgart.ipvs.as.flexmash.transformation;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.enums.DataProcessingDescriptionEnum;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.DelayNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.FileOutputNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.FunctionNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.HTTPNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.InputNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.NodeREDNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.TwitterCredentialNode;
import de.unistuttgart.ipvs.as.flexmash.models.nodered.TwitterNode;
import de.unistuttgart.ipvs.as.flexmash.nodefragments.DataProcessingDescriptions;
import de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils.FlowNode;
import de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils.NodeREDUtils;

/**
 * Class to convert the mashup plan into a Node-RED flow
 */
public class MashupPlanToNodeREDFlowConverter {

	private final static Logger LOGGER = Logger.getLogger(MashupPlanToNodeREDFlowConverter.class.getName()); 
	
	/**
	 * Converts the mashup plan into an executable NODE-RED flow
	 * 
	 * @param mashupPlanAsJSON
	 * 			the flow as JSON object
	 * 
	 * @return the results of the conversion as JSONArray
	 */
	public static JSONArray convertToNodeRED(JSONObject mashupPlanAsJSON) {
		try {
			
			ArrayList<NodeREDNode> nodeREDNodes = new ArrayList<NodeREDNode>();
			
			JSONArray nodes = mashupPlanAsJSON.getJSONArray("nodes");
			JSONObject node;
			String type;
			JSONArray transitions;
			String function;
			
			for (int i = 0; i < nodes.length(); i++) {
				node = nodes.getJSONObject(i);
				type = node.get("type").toString();
				transitions = node.getJSONArray("transitions");

				FlowNode WFNode = new FlowNode();

				for (int k = 0; k < transitions.length(); k++) {
					WFNode.target.add(transitions.getJSONObject(k).getString("target"));
				}

				switch (type) {
				case "start":
					function = DataProcessingDescriptions.getSourceCode(DataProcessingDescriptionEnum.START, node);
					FunctionNode utilNode = new FunctionNode("util", "util", "function", "100", "100", "0", "1", function, WFNode.target);
					
					ArrayList<String> newConnection = new ArrayList<String>();
					newConnection.add(utilNode.getId());
					InputNode inputNode = new InputNode(NodeREDUtils.generateNodeREDId(), node.getString("name"), "inject", "100", "75", "0", "", "", "date", "", "", false, "1", newConnection);
					
					nodeREDNodes.add(utilNode);
					nodeREDNodes.add(inputNode);
					break;
				case "end":
					function = DataProcessingDescriptions.getSourceCode(DataProcessingDescriptionEnum.END, node);;
					FunctionNode outputNode = new FunctionNode(node.getString("name"), "output", "function", Integer.toString(500),	Integer.toString(500), Integer.toString(500), "2", function, WFNode.target);
					FileOutputNode fileOutput = new FileOutputNode("fileOutput", "result", "file", "1400", "516", "0", "result.html", "true", false, WFNode.target);

					nodeREDNodes.add(outputNode);
					nodeREDNodes.add(fileOutput);
					break;
				case "merge":
					function = DataProcessingDescriptions.getSourceCode(DataProcessingDescriptionEnum.MERGE, node);
					FunctionNode mergeNode = new FunctionNode(node.getString("name"), "merge", "function", "100", Integer.toString(200), "0", "1", function, WFNode.target);
					nodeREDNodes.add(mergeNode);
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						
						if (!node.get("filter_criteria").toString().equals("")) {
							function = DataProcessingDescriptions.getSourceCode(DataProcessingDescriptionEnum.FILTER, node);
						} else {
							function = "return msg;";
						}
						FunctionNode filterNode = new FunctionNode(node.getString("name"), "Filter", "function", "100", "200", "0", "1", function, WFNode.target);
						nodeREDNodes.add(filterNode);
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {

						NodeREDNode sentimentNode = new NodeREDNode("9d886b74.380ec", "sentiment", "sentiment", "100", Integer.toString(200), "0", WFNode.target);

						function = DataProcessingDescriptions.getSourceCode(DataProcessingDescriptionEnum.SENTIMENT_ANALYTICS, node);
						FunctionNode filterTwitterNode = new FunctionNode(node.getString("name"), "debuglog", "function", "100", Integer.toString(200), "0", "1", function, WFNode.target);

						nodeREDNodes.add(sentimentNode);
						nodeREDNodes.add(filterTwitterNode);
						break;
					} else if (node.get("analyticstype").toString().equals("NE")) {
						function = DataProcessingDescriptions.getSourceCode(DataProcessingDescriptionEnum.NAMED_ENTITY_ANALYTICS, node);
						FunctionNode namedEntityNode = new FunctionNode(node.getString("name"), "Named Entity Analysis", "function", "100", Integer.toString(200), "0", "1", function, WFNode.target);
						DelayNode delayNode = new DelayNode("delay", "delay", "delay", "660", "245", "0", "delay", "5", "seconds", "1", "seconds", "1", "5", "seconds", "false", WFNode.target);

						nodeREDNodes.add(delayNode);
						nodeREDNodes.add(namedEntityNode);
						break;
					}
					break;
				case "dataSource_NYT":
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}

					// create the corresponding NodeRED JSON node
					HTTPNode nytAdapterNode = new HTTPNode(node.getString("name"), "data_adapter", "http request", "100", Integer.toString(200), "0", "GET", "http://rss.nytimes.com/services/xml/rss/nyt/" + category + ".xml", "1", WFNode.target);
					NodeREDNode xmlToJSONNode = new NodeREDNode("xmlNode", "xml", "xmltojson", "341.1166687011719", "164.11669921875", "0", WFNode.target);

					nodeREDNodes.add(xmlToJSONNode);
					nodeREDNodes.add(nytAdapterNode);
					break;
				case "dataSource_twitter":
					TwitterNode twitterDataAdapter = new TwitterNode(node.getString("name"), "data_adapter", "twitter in", "100", Integer.toString(200), "0", "6755a16c.01157", "foo", "tweets", "false", "1", WFNode.target);

					TwitterCredentialNode twitterCredentialNode = new TwitterCredentialNode("6755a16c.01157", "twitter-credentials", "@hirmerpl");
					nodeREDNodes.add(twitterCredentialNode);

					nodeREDNodes.add(twitterDataAdapter);
					break;
				}
			}
			
			// build JSONArray
			JSONArray nodeRedFlow = new JSONArray();
			for (NodeREDNode nrn: nodeREDNodes) {
				nodeRedFlow.put(nrn.toJSON());
			}

			return nodeRedFlow;
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "An error occurred while building the Node-RED flow");
		}

		return null;
	}
}
