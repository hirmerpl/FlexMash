package de.unistuttgart.ipvs.as.flexmash.utils.serviceplatform;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import de.unistuttgart.ipvs.as.flexmash.utils.Util;

public class ServiceModel implements Serializable {

	/**
	 * A class to encapsulate the consul logic of services as external
	 * representation for the users to get services as objects when calling the
	 * methods of the service platform
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String ID;
	private String Service;
	private List<String> Tags;
	private String Address;
	private String Port;
	private String Description;
	private List<String> Parameters;

	public ServiceModel(String ServiceName)
			throws ParseException, IOException, JSONException {
		this.getServiceByName(ServiceName);
	}

	public String getID() {
		return this.ID;
	}

	public String getName() {
		return this.Service;
	}

	public List<String> getTags() {
		return this.Tags;
	}

	public String getAddress() {
		return this.Address;
	}

	public String getPort() {
		return this.Port;
	}

	public String getDescription() {
		return this.Description;
	}

	public List<String> getParameters() {
		return this.Parameters;
	}

	private void getServiceByName(String ServiceName)
			throws ParseException, IOException, JSONException {
		HttpClient client = HttpClientBuilder.create().build();
		ServiceName = ServiceName.replaceAll("\\d", "");
		HttpGet request = new HttpGet(
				"http://localhost:8090/ServicesPlatform/operation?op=getServiceName&servicename="
						+ ServiceName);
		System.out.println(ServiceName);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity);
		System.out.println(content);
		if (content.equals("null"))
			return;
		JSONObject obj = new JSONObject(content);
		this.ID = obj.has("ID") ? obj.getString("ID") : null;
		this.Service = obj.has("Service") ? obj.getString("Service") : null;
		this.Port = obj.has("Port") ? obj.getString("Port") : null;
		this.Address = obj.has("Address") ? obj.getString("Address") : null;
		this.Description = obj.has("Description") ? obj.getString("Description")
				: null;
		this.Tags = obj.has("Tags")
				? Util.parseJSONArray(obj.getJSONArray("Tags")) : null;
		this.Parameters = obj.has("Parameters")
				? Util.parseJSONArray(obj.getJSONArray("Parameters")) : null;
	}
}
