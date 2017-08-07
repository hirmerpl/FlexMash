package de.unistuttgart.ipvs.as.flexmash.servlet.web;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import com.mysql.fabric.xmlrpc.base.Param;

import org.apache.http.entity.StringEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

@WebServlet("/RegisterServiceServlet")

public class RegisterServiceServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Receives a POST request from the client side
	 * 
	 * @param req
	 * @param resp
	 *           
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter pw = resp.getWriter();
		HashMap<String, String> params = handleRequest(req);
		HttpClient client = HttpClientBuilder.create().build();
		JSONObject cred = new JSONObject();
		System.out.println(params.size());
		try {
			cred.put("op", "registerService");
			cred.put("servicename", params.get("servicename").trim());
			cred.put("usertoken", "flexmash");
			cred.put("description", params.get("description").trim());
			cred.put("parameters", params.get("parameters").trim());
			cred.put("tags", params.get("tags").trim());
			cred.put("healthcheck", params.get("healthcheck").trim());
			cred.put("serviceaddress", params.get("serviceaddress").trim());
			cred.put("port", params.get("port").trim());
			cred.put("ttl", params.get("ttl").trim());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			pw.println(e.toString());
		}
		StringEntity requestEntity = new StringEntity(cred.toString(),
				ContentType.APPLICATION_JSON);
		HttpPost post = new HttpPost(
				"http://localhost:8090/ServicesPlatform/operation");
		post.setEntity(requestEntity);
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String content = EntityUtils.toString(entity);
		resp.setStatus(response.getStatusLine().getStatusCode() );
		pw.println(content);
		

	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		PrintWriter pw = resp.getWriter();
		pw.println("Please use POST operations for this service");

	}

	public HashMap<String, String> handleRequest(HttpServletRequest req) throws IOException {
		HashMap<String, String> params = new HashMap<String, String>();
		Enumeration<String> parameterNames = req.getParameterNames();
		
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValue = Arrays
					.toString(req.getParameterValues(paramName));
			params.put(paramName, paramValue);
		}

		return params;

	}

}
