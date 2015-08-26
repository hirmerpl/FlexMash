package de.unistuttgart.ipvs.as.flexmash.transformation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.utils.transformation_utils.FlowNode;

/**
 * class to convert a Data Mashup flow into an executable BPEL workflow
 */
public class BPELConverter {

	private HashMap<String, FlowNode> WorkflowMap = new HashMap<String, FlowNode>();
	String[] entries = new String[8];

	/**
	 * Converts the JSONObjects of the Data Mashup flow into a BPEL flow
	 * 
	 * @param mashupFlowAsJSON
	 *            the Data Mashup flow as JSON
	 * 
	 * @return the output of the executed flow
	 */
	public String convert(JSONObject mashupFlowAsJSON) {

		String startNode = null;

		try {
			JSONArray nodes = mashupFlowAsJSON.getJSONArray("nodes");
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

				switch (type) {
				case "start":
					WFNode.type = "start";
					WFNode.name = node.getString("name");
					startNode = WFNode.name;
					break;
				case "end":
					WFNode.type = "end";
					WFNode.name = node.getString("name");
					break;
				case "merge":
					WFNode.criteria = node.getString("criteria");
					entries[7] = WFNode.criteria;
					WFNode.type = "merge";
					WFNode.name = node.getString("name");
					break;
				case "dataSource_NYT":

					WFNode.type = "dataSource_NYT";
					WFNode.name = node.getString("name");
					String category = "";
					if (!node.get("dataSource_NYTName").toString().equals("")) {
						category = node.get("dataSource_NYTName").toString();
					}
					WFNode.category = category;
					entries[0] = category;
					break;
				case "dataSource_twitter":
					WFNode.type = "dataSource_twitter";
					WFNode.name = node.getString("name");
					WFNode.hashtag = node.getString("dataSource_twitterHashtag");
					entries[4] = WFNode.hashtag;
					break;
				case "filter":
					if (node.get("filtertype").toString().equals("NYT")) {
						WFNode.filter_criteria = node.getString("filter_criteria");
						entries[5] = WFNode.filter_criteria;
						WFNode.type = "NYTFilter";
						WFNode.name = node.get("name").toString();
					}
					break;
				case "analytics":
					if (node.get("analyticstype").toString().equals("Sentiment")) {
						WFNode.filter_criteria = node.getString("filter_criteria");
						entries[6] = WFNode.filter_criteria;
						WFNode.type = "TwitterFilter";
						WFNode.name = node.get("name").toString();
					} else if (node.get("analyticstype").toString().equals("NE")) {
						System.out.println("NE Node reached");
						WFNode.type = "NE";
						WFNode.name = node.get("name").toString();
					}
					break;
				}

				WorkflowMap.put(WFNode.name, WFNode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Queue<String> queue = new LinkedList<String>();

		FlowNode node = null;
		queue.add(startNode);

		String BPELWorkflow = "<bpel:process name=\"DataMashupProcess\"  targetNamespace=\"http://bpel.data_mashup.as.ipvs.informatik.uni_stuttgart.de\"  suppressJoinFailure=\"yes\"  xmlns:tns=\"http://bpel.data_mashup.as.ipvs.informatik.uni_stuttgart.de\"  xmlns:bpel=\"http://docs.oasis-open.org/wsbpel/2.0/process/executable\"  xmlns:ns=\"http://sql.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:ns0=\"http://twitter.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:ns1=\"http://operations.web_services.data_mashup.as.ipvs.uni_stuttgart.de\">  <bpel:import namespace=\"http://operations.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" location=\"Join.wsdl\" importType=\"http://schemas.xmlsoap.org/wsdl/\"></bpel:import>  <bpel:import namespace=\"http://twitter.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" location=\"TwitterFilter.wsdl\" importType=\"http://schemas.xmlsoap.org/wsdl/\"></bpel:import>  <bpel:import namespace=\"http://twitter.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" location=\"TwitterEtractor.wsdl\" importType=\"http://schemas.xmlsoap.org/wsdl/\"></bpel:import>  <bpel:import namespace=\"http://sql.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" location=\"SQLFilter.wsdl\" importType=\"http://schemas.xmlsoap.org/wsdl/\"></bpel:import>  <bpel:import namespace=\"http://sql.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" location=\"SQLExtractor.wsdl\" importType=\"http://schemas.xmlsoap.org/wsdl/\"></bpel:import>  <bpel:import location=\"DataMashupProcessArtifacts.wsdl\" namespace=\"http://bpel.data_mashup.as.ipvs.informatik.uni_stuttgart.de\" 	 importType=\"http://schemas.xmlsoap.org/wsdl/\" />   <bpel:partnerLinks> <bpel:partnerLink name=\"client\"     partnerLinkType=\"tns:DataMashupProcess\"     myRole=\"DataMashupProcessProvider\"     /> <bpel:partnerLink name=\"SQLExtractorParnterLink\" partnerLinkType=\"tns:SQLExtractorPartnerLinkType\" partnerRole=\"SQLExtractorRole\"></bpel:partnerLink> <bpel:partnerLink name=\"SQLFilterPartnerLink\" partnerLinkType=\"tns:SQLFilterParnterLinkType\" partnerRole=\"SQLFIlterRole\"></bpel:partnerLink> <bpel:partnerLink name=\"TwitterExtractorPartnerLink\" partnerLinkType=\"tns:TwitterExtractorPartnerLinkType\" partnerRole=\"TwitterExtractorRole\"></bpel:partnerLink> <bpel:partnerLink name=\"TwitterFilterPartnerLink\" partnerLinkType=\"tns:TwitterFilterPartnerLinkType\" partnerRole=\"TwitterFilterRole\"></bpel:partnerLink> <bpel:partnerLink name=\"JoinPartnerLink\" partnerLinkType=\"tns:JoinPartnerLinkType\" partnerRole=\"JoinRole\"></bpel:partnerLink>  </bpel:partnerLinks>   <bpel:variables> <bpel:variable name=\"input\"   messageType=\"tns:DataMashupProcessRequestMessage\"/> <bpel:variable name=\"output\"   messageType=\"tns:DataMashupProcessResponseMessage\"/> <bpel:variable name=\"SQLExtractorParnterLinkResponse\" messageType=\"ns:extractResponse\"></bpel:variable> <bpel:variable name=\"SQLExtractorParnterLinkRequest\" messageType=\"ns:extractRequest\"></bpel:variable> <bpel:variable name=\"SQLFilterPartnerLinkResponse\" messageType=\"ns:filterDataResponse\"></bpel:variable> <bpel:variable name=\"SQLFilterPartnerLinkRequest\" messageType=\"ns:filterDataRequest\"></bpel:variable> <bpel:variable name=\"TwitterExtractorPartnerLinkResponse\" messageType=\"ns0:extractResponse\"></bpel:variable> <bpel:variable name=\"TwitterExtractorPartnerLinkRequest\" messageType=\"ns0:extractRequest\"></bpel:variable> <bpel:variable name=\"TwitterFilterPartnerLinkResponse\" messageType=\"ns0:filterDataResponse\"></bpel:variable> <bpel:variable name=\"TwitterFilterPartnerLinkRequest\" messageType=\"ns0:filterDataRequest\"></bpel:variable> <bpel:variable name=\"JoinPartnerLinkResponse\" messageType=\"ns1:joinDataResponse\"></bpel:variable> <bpel:variable name=\"JoinPartnerLinkRequest\" messageType=\"ns1:joinDataRequest\"></bpel:variable>  </bpel:variables>";

		BPELWorkflow += "<bpel:sequence name=\"main\"><bpel:receive name=\"receiveInput\" partnerLink=\"client\" portType=\"tns:DataMashupProcess\" operation=\"process\" variable=\"input\" createInstance=\"yes\" />";

		while (!queue.isEmpty()) {
			node = WorkflowMap.get(queue.poll());
			BPELWorkflow = BPELWorkflow + createBPEL(node);
			for (String t : node.target) {
				if (!queue.contains(t)) {
					queue.add(t);
					break;
				}
			}
		}

		try {
			BPELWorkflow = BPELWorkflow
					+ "<bpel:assign validate=\"no\" name=\"Assign1\"><bpel:copy>   <bpel:from><bpel:literal><tns:DataMashupProcessResponse xmlns:tns=\"http://bpel.data_mashup.as.ipvs.informatik.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <tns:result>tns:result</tns:result> </tns:DataMashupProcessResponse> </bpel:literal></bpel:from>   <bpel:to variable=\"output\" part=\"payload\"></bpel:to>   </bpel:copy>   <bpel:copy>   <bpel:from part=\"parameters\" variable=\"JoinPartnerLinkResponse\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\">   <![CDATA[ns1:joinDataReturn]]>   </bpel:query>   </bpel:from>   <bpel:to part=\"payload\" variable=\"output\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[tns:result]]></bpel:query>   </bpel:to>   </bpel:copy>   </bpel:assign>   <bpel:reply name=\"replyOutput\" partnerLink=\"client\" portType=\"tns:DataMashupProcess\" operation=\"process\" variable=\"output\" /></bpel:sequence></bpel:process>";

			File file = new File("C:\\Users\\hirmerpl\\Documents\\Promotion\\Implementierungen\\Mashup_Challenge\\Data_Mashup\\BPELOutput.bpel");
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));

			writer.write(BPELWorkflow);

			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return BPELWorkflow;
	}

	/**
	 * Creates JSONObjects from a JSON String TODO: move to utils
	 * 
	 * @param jsonFlow
	 *            the JSON flow as string
	 * 
	 * @return JSONArray with JSONObjects
	 */
	public org.json.JSONObject createJsonObjects(String jsonFlow) {
		org.json.JSONObject jsnObj = null;
		try {
			jsnObj = new org.json.JSONObject(jsonFlow);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsnObj;
	}

	/**
	 * Creates the BPEL Code from a WorkflowNode Object
	 * 
	 * @param node
	 *            the node in the flow
	 * 
	 * @return the BPEL code
	 */
	private static String createBPEL(FlowNode node) {
		String BPELWorkflow = "";
		String invoke = "";
		String assign = "";
		if (node != null) {
			if (node.type != null) {
				switch (node.type) {
				case "start":
					break;
				case "end":
					break;
				case "merge":
					assign = "<bpel:assign validate=\"no\" name=\"Assign5\">  <bpel:copy>    <bpel:from><bpel:literal><impl:joinData xmlns:impl=\"http://operations.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <impl:key1>impl:key1</impl:key1>  <impl:key2>impl:key2</impl:key2>  <impl:criteria>impl:criteria</impl:criteria> </impl:joinData> </bpel:literal></bpel:from>    <bpel:to variable=\"JoinPartnerLinkRequest\" part=\"parameters\"></bpel:to>  </bpel:copy>  <bpel:copy>    <bpel:from part=\"payload\" variable=\"input\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[tns:joinCriteria]]></bpel:query>    </bpel:from>    <bpel:to part=\"parameters\" variable=\"JoinPartnerLinkRequest\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns1:criteria]]></bpel:query>    </bpel:to>  </bpel:copy>  <bpel:copy>    <bpel:from part=\"parameters\" variable=\"SQLFilterPartnerLinkResponse\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns:filterDataReturn]]></bpel:query>    </bpel:from>    <bpel:to part=\"parameters\" variable=\"JoinPartnerLinkRequest\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns1:key1]]></bpel:query>    </bpel:to>  </bpel:copy>  <bpel:copy>    <bpel:from part=\"parameters\" variable=\"TwitterFilterPartnerLinkResponse\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns0:filterDataReturn]]></bpel:query>    </bpel:from>    <bpel:to part=\"parameters\" variable=\"JoinPartnerLinkRequest\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns1:key2]]></bpel:query>    </bpel:to>  </bpel:copy>  </bpel:assign>";
					invoke = " <bpel:invoke name=\"InvokeMerge\" partnerLink=\"JoinPartnerLink\" operation=\"joinData\" portType=\"ns1:Join\" inputVariable=\"JoinPartnerLinkRequest\" outputVariable=\"JoinPartnerLinkResponse\"></bpel:invoke>";
					break;
				case "NYTFilter":
					assign = "<bpel:assign validate=\"no\" name=\"Assign2\">             <bpel:copy>                 <bpel:from><bpel:literal><impl:filterData xmlns:impl=\"http://sql.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">   <impl:key>impl:key</impl:key>   <impl:criteria>impl:criteria</impl:criteria> </impl:filterData> </bpel:literal></bpel:from>                 <bpel:to variable=\"SQLFilterPartnerLinkRequest\" part=\"parameters\"></bpel:to>             </bpel:copy>                          <bpel:copy>                 <bpel:from part=\"payload\" variable=\"input\">                     <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\">                         <![CDATA[tns:sqlFilterCriteria]]>                     </bpel:query>                 </bpel:from>                 <bpel:to part=\"parameters\" variable=\"SQLFilterPartnerLinkRequest\">                     <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\">                         <![CDATA[ns:criteria]]>                     </bpel:query>                 </bpel:to>             </bpel:copy>             <bpel:copy>                 <bpel:from part=\"parameters\" variable=\"SQLExtractorParnterLinkResponse\">                     <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns:extractReturn]]></bpel:query></bpel:from> <bpel:to part=\"parameters\" variable=\"SQLFilterPartnerLinkRequest\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns:key]]></bpel:query></bpel:to>  </bpel:copy>   </bpel:assign>";
					invoke = "<bpel:invoke name=\"InvokeNYTFilterAndAnalytics\" partnerLink=\"SQLFilterPartnerLink\" operation=\"filterData\" portType=\"ns:SQLFilter\" inputVariable=\"SQLFilterPartnerLinkRequest\" outputVariable=\"SQLFilterPartnerLinkResponse\"></bpel:invoke>";
					break;
				case "TwitterFilter":
					assign = "<bpel:assign validate=\"no\" name=\"Assign3\">       <bpel:copy>         <bpel:from><bpel:literal><impl:filterData xmlns:impl=\"http://twitter.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">  <impl:key>impl:key</impl:key>  <impl:criteria>impl:criteria</impl:criteria> </impl:filterData> </bpel:literal></bpel:from>         <bpel:to variable=\"TwitterFilterPartnerLinkRequest\" part=\"parameters\"></bpel:to>       </bpel:copy>       <bpel:copy>         <bpel:from part=\"payload\" variable=\"input\">           <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[tns:twitterFilterCriteria]]></bpel:query>         </bpel:from>         <bpel:to part=\"parameters\" variable=\"TwitterFilterPartnerLinkRequest\">           <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns0:criteria]]></bpel:query>         </bpel:to>       </bpel:copy>       <bpel:copy>         <bpel:from part=\"parameters\" variable=\"TwitterExtractorPartnerLinkResponse\">           <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns0:extractReturn]]></bpel:query>         </bpel:from>         <bpel:to part=\"parameters\" variable=\"TwitterFilterPartnerLinkRequest\">           <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns0:key]]></bpel:query>     </bpel:to>   </bpel:copy>  </bpel:assign>";
					invoke = "<bpel:invoke name=\"InvokeTwitterFilterAndAnalytics\" partnerLink=\"TwitterFilterPartnerLink\" operation=\"filterData\" portType=\"ns0:TwitterFilter\" inputVariable=\"TwitterFilterPartnerLinkRequest\" outputVariable=\"TwitterFilterPartnerLinkResponse\"></bpel:invoke>";
					break;
				case "dataSource_NYT":
					assign = "<bpel:assign validate=\"no\" name=\"Assign\"><bpel:copy><bpel:from><bpel:literal><impl:extract xmlns:impl=\"http://sql.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">   <impl:address>impl:address</impl:address>  </impl:extract> </bpel:literal></bpel:from>                 <bpel:to variable=\"SQLExtractorParnterLinkRequest\" part=\"parameters\"></bpel:to></bpel:copy><bpel:copy><bpel:from part=\"payload\" variable=\"input\"><bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[tns:sqlExtractorAdress]]></bpel:query></bpel:from><bpel:to part=\"parameters\" variable=\"SQLExtractorParnterLinkRequest\"><bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns:address]]></bpel:query></bpel:to></bpel:copy></bpel:assign>";
					invoke = "<bpel:invoke name=\"InvokeNYTExtractor\" partnerLink=\"SQLExtractorParnterLink\" operation=\"extract\" portType=\"ns:SQLExtractor\" inputVariable=\"SQLExtractorParnterLinkRequest\" outputVariable=\"SQLExtractorParnterLinkResponse\"></bpel:invoke>";

					BPELWorkflow = BPELWorkflow + assign + invoke;

					assign = "<bpel:assign validate=\"no\" name=\"Assign2\">             <bpel:copy>                 <bpel:from><bpel:literal><impl:filterData xmlns:impl=\"http://sql.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">   <impl:key>impl:key</impl:key>   <impl:criteria>impl:criteria</impl:criteria> </impl:filterData> </bpel:literal></bpel:from>                 <bpel:to variable=\"SQLFilterPartnerLinkRequest\" part=\"parameters\"></bpel:to>             </bpel:copy>                          <bpel:copy>                 <bpel:from part=\"payload\" variable=\"input\">                     <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\">                         <![CDATA[tns:sqlFilterCriteria]]>                     </bpel:query>                 </bpel:from>                 <bpel:to part=\"parameters\" variable=\"SQLFilterPartnerLinkRequest\">                     <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\">                         <![CDATA[ns:criteria]]>                     </bpel:query>                 </bpel:to>             </bpel:copy>             <bpel:copy>                 <bpel:from part=\"parameters\" variable=\"SQLExtractorParnterLinkResponse\">                     <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns:extractReturn]]></bpel:query></bpel:from> <bpel:to part=\"parameters\" variable=\"SQLFilterPartnerLinkRequest\">   <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns:key]]></bpel:query></bpel:to>  </bpel:copy>   </bpel:assign>";
					invoke = "<bpel:invoke name=\"InvokeNYTFilterAndAnalytics\" partnerLink=\"SQLFilterPartnerLink\" operation=\"filterData\" portType=\"ns:SQLFilter\" inputVariable=\"SQLFilterPartnerLinkRequest\" outputVariable=\"SQLFilterPartnerLinkResponse\"></bpel:invoke>";

					break;
				case "dataSource_twitter":
					assign = "<bpel:assign validate=\"no\" name=\"Assign4\">   <bpel:copy>    <bpel:from><bpel:literal><impl:extract xmlns:impl=\"http://twitter.web_services.data_mashup.as.ipvs.uni_stuttgart.de\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"> <impl:hashtag>impl:hashtag</impl:hashtag> </impl:extract> </bpel:literal></bpel:from>    <bpel:to variable=\"TwitterExtractorPartnerLinkRequest\" part=\"parameters\"></bpel:to>   </bpel:copy>   <bpel:copy>    <bpel:from part=\"payload\" variable=\"input\">    <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[tns:twitterExtractorHashtag]]></bpel:query>    </bpel:from>    <bpel:to part=\"parameters\" variable=\"TwitterExtractorPartnerLinkRequest\">    <bpel:query queryLanguage=\"urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0\"><![CDATA[ns0:hashtag]]></bpel:query>    </bpel:to>   </bpel:copy>  </bpel:assign>";
					invoke = "<bpel:invoke name=\"InvokeTwitterExtractor\" partnerLink=\"TwitterExtractorPartnerLink\" operation=\"extract\" portType=\"ns0:TwitterEtractor\" inputVariable=\"TwitterExtractorPartnerLinkRequest\" outputVariable=\"TwitterExtractorPartnerLinkResponse\"></bpel:invoke>";
					break;
				case "NE":
					break;
				}

				BPELWorkflow = BPELWorkflow + assign + invoke;
			}
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
