package de.unistuttgart.ipvs.as.flexmash.servlet.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;

import de.unistuttgart.ipvs.as.flexmash.servlet.engine.BPELEngineCommunicator;
import de.unistuttgart.ipvs.as.flexmash.servlet.engine.EngineProcessStarter;
import de.unistuttgart.ipvs.as.flexmash.transformation.BPELConverter;
import de.unistuttgart.ipvs.as.flexmash.transformation.NodeREDConverter;
import de.unistuttgart.ipvs.as.flexmash.utils.http.IOUtils;

@WebServlet("/DataMashup")
/**
 * Servlet to communicate with the client
 */
public class ClientCommunicationServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * receives a POST request from the client side
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String pattern = req.getParameter("pattern");

		String data = req.getParameter("flow");
		data = data.replaceAll("[\\t\\n\\r]", "");

		if (pattern.equals("robust")) {

			BPELConverter conv = new BPELConverter();

			org.json.JSONObject jsnObj = conv.createJsonObjects(data);

			String bpel = conv.convert(jsnObj);

			String[] entries = conv.getEntries();

			EngineProcessStarter engineProcessStarter = new EngineProcessStarter();
			engineProcessStarter.generateFiles(bpel);

			BPELEngineCommunicator engineCommunicator = new BPELEngineCommunicator();

			engineCommunicator.callEngine(entries[0], entries[4], entries[5], entries[6], entries[7]);

			String reply = "robust";
			PrintWriter out = resp.getWriter();
			
			out.println(reply);

		} else if (pattern.equals("timeCritical")) {
			// implement Node-RED Mapping here
			System.out.println("Time Critical Pattern selected..");

			NodeREDConverter conv = new NodeREDConverter();

			org.json.JSONObject jsnObj = conv.createJsonObjects(data);

			JSONArray nodeRedFlow = conv.convertToNodeRED(jsnObj);

			IOUtils.deployToNodeRED(nodeRedFlow);

			PrintWriter out = resp.getWriter();
					
			String reply = "timeCritical";
				
			out.println(reply);
		}
	}
}
