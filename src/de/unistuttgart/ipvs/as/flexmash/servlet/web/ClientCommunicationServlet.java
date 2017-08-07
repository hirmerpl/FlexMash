package de.unistuttgart.ipvs.as.flexmash.servlet.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.sun.javafx.collections.MappingChange.Map;

import org.json.JSONArray;

import de.unistuttgart.ipvs.as.flexmash.bpmn.BPMNmodel;
import de.unistuttgart.ipvs.as.flexmash.servlet.engine.BPELEngineCommunicator;
import de.unistuttgart.ipvs.as.flexmash.servlet.engine.Engine;
import de.unistuttgart.ipvs.as.flexmash.servlet.engine.EngineProcessStarter;
import de.unistuttgart.ipvs.as.flexmash.transformation.MashupPlanToBPELConverter;
import de.unistuttgart.ipvs.as.flexmash.transformation.MashupPlanToNodeREDFlowConverter;
import de.unistuttgart.ipvs.as.flexmash.transformation.MashupPlantoBPMNConverter;
import de.unistuttgart.ipvs.as.flexmash.utils.Util;
import de.unistuttgart.ipvs.as.flexmash.utils.http.IOUtils;


@WebServlet("/DataMashup")
/**
 * Entry point of the server side. Receives a Mashup Plan, transforms and executes it
 */
public class ClientCommunicationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// static string definitions
	private static final String PATTERN_STRING = "pattern";
	private static final String FLOW_STRING = "flow";
	private static final String ROBUST = "Robust";
	private static final String TIME_CRITICAL = "Time-Critical";
	private static final String ERROR = "error";

	private static final Logger LOGGER = Logger.getLogger(ClientCommunicationServlet.class.getName()); 
	
	/**
	 * Receives a POST request from the client side
	 * 
	 * @param req
	 * 			the request from the client containing the Mashup Plan and the pattern to be used
	 * 
	 * @param resp
	 * 			the response to the client whether the data mashup was successful
	 * 
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String selectedPattern = req.getParameter(PATTERN_STRING);
		String mashupPlan = req.getParameter(FLOW_STRING);
		
		// remove tab stops, line breaks
		mashupPlan = mashupPlan.replaceAll("[\\t\\n\\r]", "");
		PrintWriter out = resp.getWriter();
		JSONObject mashupPlanAsJSON = Util.createJsonObjects(mashupPlan);
		
		switch (selectedPattern) {
			case ROBUST:
				LOGGER.log(Level.INFO, "Robust pattern selected.");
				
				MashupPlantoBPMNConverter MashupPlanToBPMNConverter = new MashupPlantoBPMNConverter();
				BPMNmodel generatedModel = MashupPlanToBPMNConverter.convert(mashupPlanAsJSON);
				

				Engine engine = new Engine();
				engine.deployProcessModel(generatedModel.fileName, generatedModel);
				engine.runProcessModel("MainProcess", generatedModel);
				Files.delete( Paths.get(generatedModel.fileName));
				engine.close();
				
//				MashupPlanToBPELConverter mashupPlanToBPELConverter = new MashupPlanToBPELConverter();
//				String mashupPlanAsBPEL = mashupPlanToBPELConverter.convert(mashupPlanAsJSON);
//				HashMap<String,String> properties =  mashupPlanToBPELConverter.getEntries();
//				EngineProcessStarter.generateFiles(mashupPlanAsBPEL);
//				String result = BPELEngineCommunicator.callEngine(properties.get("category"), properties.get("dataSource_twitter"), 
//						properties.get("NYTFilter"), properties.get("TwitterFilter"), properties.get("criteria"));
//
//				out.println(result);
//				
				break;
			case TIME_CRITICAL:
				// implement Node-RED Mapping here
				LOGGER.log(Level.INFO, "Time-critical pattern selected.");

				JSONArray nodeRedFlow = MashupPlanToNodeREDFlowConverter.convertToNodeRED(mashupPlanAsJSON);
				IOUtils.deployToNodeRED(nodeRedFlow);
				
				out.println(TIME_CRITICAL);
				
				break;
			default:
				break;
		}
		
	}
}
