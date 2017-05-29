package de.unistuttgart.ipvs.as.flexmash.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.json.JSONObject;
import java.net.URL;
import java.util.ArrayList;

import java.util.Map;

/**
 * @author KMahrous
 *
 */
public class ExecutionHelper {

	/**
	 * @return filepath for based on the env. variable
	 */
	public String getFilePath() {

		return System.getenv("FLEXMASH") + "/files/";
	}

	/**
	 * Getter for the input variable for a process within the BPMN model
	 * 
	 * @param execution
	 * @return
	 */
	public Object getInput(DelegateExecution execution) {
		return execution.getVariable(execution.getCurrentActivityId() + "In");
	}

	/**
	 * Setter for the output variable for a process within the BPMN model
	 * 
	 * @param execution
	 * @param output
	 */
	public void setOutput(DelegateExecution execution, Object output) {
		execution.setVariable(execution.getCurrentActivityId() + "Out", output);
	}

	/**
	 * @return URL for webservices within the same project
	 */
	public String getURLPath() {
		return "http://localhost:8080/Data_Mashup/services/";
	}

	/**
	 * Adding entries to the transition maps per node
	 * 
	 * @param hashMap
	 * @param key
	 * @param value
	 */
	public void addValues(Map<String, ArrayList<String>> hashMap, String key,
			String value) {
		ArrayList<String> tempList = null;
		if (hashMap.containsKey(key)) {
			tempList = (ArrayList<String>) hashMap.get(key);
			if (tempList == null)
				tempList = new ArrayList<String>();
			tempList.add(value);
		} else {
			tempList = new ArrayList<String>();
			tempList.add(value);
		}
		hashMap.put(key, tempList);
	}

	/**
	 * Getter for all the direct predecessor nodes to determine all possible
	 * inputs for a node
	 * 
	 * @param execution
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getPredecessors(DelegateExecution execution) {

		return (ArrayList<String>) execution
				.getVariable(execution.getCurrentActivityId() + "Pre");
	}

	/**
	 * Communication method to send the input to the service platform and
	 * integrate the result back into the camunda engine.
	 * 
	 * @param input
	 * @param serviceURL
	 * @return result JSONObject
	 * @throws Exception
	 */
	public JSONObject sendInputToPlatform(Map<String, Object> input,
			URL serviceURL) throws Exception {

		HttpClient client = HttpClientBuilder.create().build();
		JSONObject toSend = new JSONObject(input);
		System.out.println("SENT OUTPUT:");
		System.out.println(toSend.toString());
		StringEntity requestEntity = new StringEntity(toSend.toString(),
				ContentType.APPLICATION_JSON);
		HttpPost post = new HttpPost(serviceURL.toExternalForm());
		post.setEntity(requestEntity);
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity);
		System.out.println("RECEIVED INPUT:");
		System.out.println(content.toString());
		return new JSONObject(content);

	}
}
