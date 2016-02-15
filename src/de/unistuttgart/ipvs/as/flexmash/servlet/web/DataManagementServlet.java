package de.unistuttgart.ipvs.as.flexmash.servlet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.json.simple.JSONArray;

import de.unistuttgart.ipvs.as.flexmash.servlet.engine.BPELEngineCommunicator;
import de.unistuttgart.ipvs.as.flexmash.servlet.engine.EngineProcessStarter;
import de.unistuttgart.ipvs.as.flexmash.transformation.BPELConverter;
import de.unistuttgart.ipvs.as.flexmash.transformation.NodeREDConverter;
import de.unistuttgart.ipvs.as.flexmash.utils.http.IOUtils;

@WebServlet("/DataManagement")
/**
 * Servlet to communicate with the client
 */
public class DataManagementServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CouchDbInstance _db;
	/**
	 * receives a POST request from the client side
	 */
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		String dbname = "my_database";
	    try {
	        //creates a database with the specified name
	        CouchDbConnector dbc = _db.createConnector(dbname, true);
	        res.getWriter().println("Created database: " + dbname);
	        //create a simple doc to place into your new database
	        Map<String, Object> doc = new  HashMap<String, Object>();
	        doc.put("_id",  UUID.randomUUID().toString());
	        doc.put("season", "summer");
	        doc.put("climate", "arid");
	        dbc.create(doc);
	        res.getWriter().println("Added a simple doc!");
	    } catch (Exception e) {
	    	res.getWriter().println(e.getMessage());
	    }
	}
}
