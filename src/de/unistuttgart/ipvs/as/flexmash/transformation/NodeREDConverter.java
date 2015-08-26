package de.unistuttgart.ipvs.as.flexmash.transformation;

import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils.FlowNode;
import de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils.NodeREDUtils;

/**
 * class to convert the data mashup flow into a Node-RED flow
 */
public class NodeREDConverter {

	/**
	 * Converts the data mashup flow into a NODE-RED flow
	 * 
	 * @param jsonFlow
	 * 			the flow as JSON object
	 * @return the results of the conversion as JSONArray
	 */
	@SuppressWarnings("unchecked")
	public org.json.simple.JSONArray convertToNodeRED(JSONObject jsonFlow) {
		try {

			org.json.simple.JSONArray nodeRedFlow = new org.json.simple.JSONArray();

			JSONArray nodes = jsonFlow.getJSONArray("nodes");
			JSONObject node;
			String type;
			JSONArray transitions;

			for (int i = 0; i < nodes.length(); i++) {
				node = nodes.getJSONObject(i);
				type = node.get("type").toString();
				transitions = node.getJSONArray("transitions");

				FlowNode WFNode = new FlowNode();

				for (int k = 0; k < transitions.length(); k++) {
					WFNode.target.add(transitions.getJSONObject(k).getString("target"));
				}

				// now connect the node to the flow
				JSONArray wiresNode = new JSONArray();
				JSONArray connections = new JSONArray();

				JSONArray secondaryConnections;
				switch (type) {
				case "start":

					org.json.simple.JSONObject utilNode = NodeREDUtils.createNodeREDNode("util", "util", "function", "100", "100", "0");
					utilNode.put("func",
							"context.global.allarticles = new Array();context.global.allurls = new Array();context.global.categories = new Array();context.global.counter = 0;return msg;");
					utilNode.put("outputs", "1");

					JSONArray utilConnections = new JSONArray();
					JSONArray utilWires = new JSONArray();

					for (String connection : WFNode.target) {
						utilConnections.put(connection);
					}

					utilWires.put(utilConnections);
					utilNode.put("wires", utilWires);

					org.json.simple.JSONObject jsonNode = NodeREDUtils.generateInputNode(node.getString("name"));
					connections.put("util");

					wiresNode.put(connections);
					jsonNode.put("wires", wiresNode);
					jsonNode.put("outputs", "1");
					nodeRedFlow.add(utilNode);
					nodeRedFlow.add(jsonNode);
					break;
				case "end":
					org.json.simple.JSONObject outputNode = NodeREDUtils.createNodeREDNode(node.getString("name"), "output", "function", Integer.toString(500),
							Integer.toString(500), Integer.toString(500));
					outputNode
							.put("func",
									"var htmlString;\nvar currentArticle = context.global.categories[context.global.counter-1];\nvar searchKeywords = \"\";\nfor (var i= 0; i < currentArticle.length; i++) {\n		var keyword = currentArticle[i]._;\n	keyword = keyword.replace(',', '');\n	if (i != currentArticle.length-1) {\n		searchKeywords = searchKeywords + keyword + ',';\n	} else {\n		searchKeywords = searchKeywords + keyword;\n	}\n}\nif (context.global.counter == 1) {\n	htmlString = '<html><head><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\"><title>Data Mashup Result (Time-Critical) </title></head><body><table class=\"table table-striped\" style=\"width: 100%;\"><tr><th>Article</th><th>Keywords</th><th>Overall Sentiment</th><th>Example Tweets</th></tr>' + \n	'<tr><td>' + msg.article + ' (<a href=\"' + context.global.url + '\" target=\"_blank\">link)</td><td>' + searchKeywords + '</td><td>' + msg.payload + '</td><td>';\n	for (var i = 0; i < context.global.rendering.length; i++) {\n		htmlString = htmlString + context.global.rendering[i] + '<b> (Score: ' + context.global.scores[i] + ')</b><hr/>';\n}\n	htmlString = htmlString + '</td></tr>';\n	msg.payload = htmlString;\n	context.global.exampleTweets = new Array();\n	context.global.scores = new Array();\n	context.global.rendering = new Array();\n	//context.global.allarticles = new Array();\n	//context.global.allurls = new Array();\n	return [msg,msg];\n} else if (context.global.counter < 20) {\n	console.log('Sentiments for: ' + msg.article); \n	console.log(msg.payload);\n	htmlString = '<tr><td>' + msg.article + ' (<a href=\"' + context.global.url + '\" target=\"_blank\">link)</td><td>' + searchKeywords + '</td><td>' + msg.payload + '</td><td>';\n	for (var i = 0; i < context.global.rendering.length; i++) {\n		htmlString = htmlString + context.global.rendering[i] + '<b> (Score: ' + context.global.scores[i] + ')</b><hr/>';\n	}\n	htmlString = htmlString + '</td></tr>';\n	msg.payload = htmlString;\n	context.global.exampleTweets = new Array();\n	context.global.scores = new Array();\n	context.global.rendering = new Array();\n	//context.global.allarticles = new Array();\n	//context.global.allurls = new Array();\n	return [msg,msg];\n} else if (context.global.counter == 20) {\n	htmlString = '<tr><td>' + msg.article + ' (<a href=\"' + context.global.url + '\" target=\"_blank\">link)</td><td>' + searchKeywords + '</td><td>' + msg.payload + '</td><td>';\n	for (var i = 0; i < context.global.rendering.length; i++) {\n		htmlString = htmlString + context.global.rendering[i] + '<b> (Score: ' + context.global.scores[i] + ')</b><hr/>';\n	}\n	htmlString = htmlString + '</td></tr></table></body></html>';\n	msg.payload = htmlString;\n	context.global.exampleTweets = new Array();\n	context.global.scores = new Array();\n	context.global.rendering = new Array();\n	//context.global.allarticles = new Array();\n	//context.global.allurls = new Array();\n	return [msg,msg];\n} else {\n	return null;\n}\n");
					outputNode.put("outputs", 2);

					org.json.simple.JSONObject fileOutput = new org.json.simple.JSONObject();
					fileOutput.put("id", "fileOutput");
					fileOutput.put("type", "file");
					fileOutput.put("name", "result");
					fileOutput.put("filename", "result.html");
					fileOutput.put("appendNewline", "true");
					fileOutput.put("overwriteFile", false);
					fileOutput.put("x", "1400");
					fileOutput.put("y", "516");
					fileOutput.put("z", "0");

					JSONArray fileOutputWiresNode = new JSONArray();
					JSONArray fileOutputConnections = new JSONArray();

					fileOutputWiresNode.put(fileOutputConnections);
					fileOutput.put("wires", fileOutputWiresNode);

					for (String connection : WFNode.target) {
						connections.put(connection);
					}
					secondaryConnections = new JSONArray();
					connections.put("delay");
					secondaryConnections.put("fileOutput");
					wiresNode.put(connections);
					wiresNode.put(secondaryConnections);
					outputNode.put("wires", wiresNode);
					nodeRedFlow.add(outputNode);
					nodeRedFlow.add(fileOutput);
					break;
				case "merge":
					org.json.simple.JSONObject mergeNode = NodeREDUtils.createNodeREDNode(node.getString("name"), "merge", "function", "100",
							Integer.toString(200), "0");
					mergeNode
							.put("func",
									"context.values = context.values || new Array();\ncontext.values.push(msg);\nvar totalScore = 0;\nif (context.values.length == 5) {\nfor (var i=0; i < context.values.length; i++) {\nvar currentMessage = context.values[i];\n	console.log('Current Score' + currentMessage.sentiment.score);\ntotalScore = totalScore + currentMessage.sentiment.score;console.log('Total Score at the moment: ' + totalScore);\n}\ntotalScore = totalScore / 5;\nif (totalScore > 0) {\nmsg.payload = 'Sentiment is Positive!';\n} else if (totalScore < 0) {\nmsg.payload = 'Sentiment is Negative!';\n} else {\nmsg.payload = 'Sentiment is Neutral!';\n}\nmsg.article = context.global.article;\ncontext.values = new Array();\nreturn msg;\n}");
					for (String connection : WFNode.target) {
						connections.put(connection);
					}

					wiresNode.put(connections);
					mergeNode.put("wires", wiresNode);
					mergeNode.put("outputs", "1");

					nodeRedFlow.add(mergeNode);
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						org.json.simple.JSONObject filterNode = NodeREDUtils.createNodeREDNode(node.getString("name"), "Filter", "function", "100", "200", "0");
						filterNode.put("outputs", "1");
						if (!node.get("filter_criteria").toString().equals("")) {
							filterNode
									.put("func",
											"var articles = msg.payload.rss.channel[0].item;\nvar keyword = '" + node.get("filter_criteria") + "';\nfor (var j = 0; j < articles.length; j++){\n	var currentArticle = articles[j];\n		for (var i= 0; i < currentArticle.category.length; i++) {\n		var tag = currentArticle.category[i]._;\n		if (tag.indexOf(keyword) != -1) {\n			articles.splice(j, 1);\n			break;\n		}\n	}\n}\nreturn msg;");
						} else {
							filterNode.put("func", "return msg;");
						}

						for (String connection : WFNode.target) {
							if (!connection.contains("merge")) {
								connections.put(connection);
							}
						}
						wiresNode.put(connections);
						filterNode.put("wires", wiresNode);
						nodeRedFlow.add(filterNode);
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {

						org.json.simple.JSONObject sentimentNode = NodeREDUtils.createNodeREDNode("9d886b74.380ec", "sentiment", "sentiment", "100",
								Integer.toString(200), "0");
						JSONArray sentimentNodeConnections = new JSONArray();
						JSONArray sentimentNodeWires = new JSONArray();

						org.json.simple.JSONObject filterTwitterNode = NodeREDUtils.createNodeREDNode(node.getString("name"), "debuglog", "function", "100",
								Integer.toString(200), "0");
						filterTwitterNode
								.put("func",
										"context.global.exampleTweets = context.global.exampleTweets || new Array();context.global.exampleTweets.push(msg.payload);context.global.scores = context.global.scores || new Array();context.global.scores.push(msg.sentiment.score);console.log(msg.sentiment);\ncontext.global.rendering = context.global.rendering || new Array();context.global.rendering.push(msg.render);\nreturn msg;");
						for (String connection : WFNode.target) {
							connections.put(connection);
						}
						wiresNode.put(connections);
						filterTwitterNode.put("wires", wiresNode);
						filterTwitterNode.put("outputs", "1");

						sentimentNodeConnections.put(filterTwitterNode.get("id"));
						sentimentNodeWires.put(sentimentNodeConnections);
						sentimentNode.put("wires", sentimentNodeWires);

						nodeRedFlow.add(sentimentNode);
						nodeRedFlow.add(filterTwitterNode);
						break;
					} else if (node.get("analyticstype").toString().equals("NE")) {
						org.json.simple.JSONObject namedEntityNode = NodeREDUtils.createNodeREDNode(node.getString("name"), "Named Entity Analysis",
								"function", "100", Integer.toString(200), "0");

						org.json.simple.JSONObject delayNode = new org.json.simple.JSONObject();
						delayNode.put("id", "delay");
						delayNode.put("type", "delay");
						delayNode.put("name", "");
						delayNode.put("pauseType", "delay");
						delayNode.put("timeout", "5");
						delayNode.put("timeoutUnits", "seconds");
						delayNode.put("rate", "1");
						delayNode.put("rateUnits", "seconds");
						delayNode.put("randomFirst", "1");
						delayNode.put("randomLast", "5");
						delayNode.put("randomUnits", "seconds");
						delayNode.put("drop", "false");
						delayNode.put("x", "660");
						delayNode.put("y", "245");
						delayNode.put("z", "0");
						JSONArray delayNodeConnections = new JSONArray();
						JSONArray delayNodeWires = new JSONArray();

						delayNodeConnections.put(node.getString("name"));
						delayNodeWires.put(delayNodeConnections);
						delayNode.put("wires", delayNodeWires);

						nodeRedFlow.add(delayNode);

						namedEntityNode
								.put("func",
										"var articles; \nif (context.global.counter == 0) {\n	articles = msg.payload.rss.channel[0].item;\n}\nvar titles = new Array();\nvar searchKeywords = new Array();\nvar title;\nif (context.global.allarticles.length > 0) {\n	console.log('Number of Articles: ' + context.global.allarticles.length);\n	console.log('Current Article: ' + context.global.allarticles[context.global.counter][0]);\n	context.global.article=context.global.allarticles[context.global.counter][0];\n	context.global.url=context.global.allurls[context.global.counter][0];\n	//msg.payload = context.global.allarticles[context.global.counter][0].replace(/ /g,',');\n	var currentArticle = context.global.categories[context.global.counter];\n	var searchKeywords = '';\n	for (var i= 0; i < currentArticle.length; i++) {\n		var keyword = currentArticle[i]._;\n		keyword = keyword.replace(',', '');\n		if (i != currentArticle.length-1) {\n			searchKeywords = searchKeywords + keyword + ',';\n		} else {\n			searchKeywords = searchKeywords + keyword;\n		}\n	}\n	msg.payload = searchKeywords;\n	console.log('Keywords: ' + searchKeywords);\n	context.global.counter++;\n	return msg;\n} else {\n	for (var i=0; i < articles.length; i++ ) {\n		title = articles[i].title;\n		context.global.allurls.push(articles[i].link);\n		context.global.allarticles.push(title);\n		context.global.categories.push(articles[i].category);\n		titles.push(title);\n	}\n	context.global.article=context.global.allarticles[context.global.counter][0];\n	context.global.url=context.global.allurls[context.global.counter][0];\n	var currentArticle = context.global.categories[context.global.counter];\n	var searchKeywords = '';\n	for (var i= 0; i < currentArticle.length; i++) {\n		var keyword = currentArticle[i]._;\n		keyword = keyword.replace(',', '');\n		if (i != currentArticle.length-1) {\n			searchKeywords = searchKeywords + keyword + ',';\n		} else {\n			searchKeywords = searchKeywords + keyword;\n		}\n	}\n	msg.payload = searchKeywords;\n	console.log('Keywords: ' + searchKeywords);\n	context.global.counter++;\n	return msg;\n}\n");

						for (String connection : WFNode.target) {
							if (!connection.contains("merge")) {
								connections.put(connection);
							}
						}
						wiresNode.put(connections);
						namedEntityNode.put("wires", wiresNode);
						namedEntityNode.put("outputs", "1");

						nodeRedFlow.add(namedEntityNode);
						break;
					}
					break;
				case "dataSource_NYT":
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}
					// org.json.simple.JSONObject sqlDataAdapter =
					// NodeREDUtils.createNodeREDNode(node.getString("name"),
					// "data_adapter", "function", "100", Integer.toString(200),
					// "0");
					// sqlDataAdapter.put("func", "");
					// create the corresponding NodeRED JSON node
					org.json.simple.JSONObject nytAdapterNode = NodeREDUtils.createNodeREDNode(node.getString("name"), "data_adapter", "http request", "100",
							Integer.toString(200), "0");
					nytAdapterNode.put("method", "GET");
					nytAdapterNode.put("url", "http://rss.nytimes.com/services/xml/rss/nyt/" + category + ".xml");

					org.json.simple.JSONObject xmlNode = new org.json.simple.JSONObject();
					xmlNode.put("id", "xmlNode");
					xmlNode.put("type", "xml");
					xmlNode.put("name", "xmltojson");
					xmlNode.put("x", "341.1166687011719");
					xmlNode.put("y", "164.11669921875");
					xmlNode.put("z", "0");

					JSONArray xmlNodeConnections = new JSONArray();
					JSONArray xmlNodeWires = new JSONArray();

					for (String connection : WFNode.target) {
						if (!connection.contains("merge")) {
							xmlNodeConnections.put(connection);
						}
					}

					xmlNodeWires.put(xmlNodeConnections);
					xmlNode.put("wires", xmlNodeWires);

					nodeRedFlow.add(xmlNode);

					connections.put("xmlNode");

					wiresNode.put(xmlNode.get("id"));
					nytAdapterNode.put("wires", wiresNode);
					nytAdapterNode.put("outputs", "1");
					nodeRedFlow.add(nytAdapterNode);
					break;
				case "dataSource_twitter":
					org.json.simple.JSONObject twitterDataAdapter = NodeREDUtils.createNodeREDNode(node.getString("name"), "data_adapter", "twitter in", "100",
							Integer.toString(200), "0");
					twitterDataAdapter.put("twitter", "6755a16c.01157");
					twitterDataAdapter.put("tags", "foo");
					twitterDataAdapter.put("topic", "tweets");
					twitterDataAdapter.put("user", "false");

					org.json.simple.JSONObject twitterCredentialNode = new org.json.simple.JSONObject();
					twitterCredentialNode.put("id", "6755a16c.01157");
					twitterCredentialNode.put("type", "twitter-credentials");
					twitterCredentialNode.put("screen_name", "@hirmerpl");
					nodeRedFlow.add(twitterCredentialNode);

					connections.put("9d886b74.380ec");

					wiresNode.put(connections);
					twitterDataAdapter.put("wires", wiresNode);
					twitterDataAdapter.put("outputs", "1");
					nodeRedFlow.add(twitterDataAdapter);
					break;
				}
			}
			return nodeRedFlow;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
