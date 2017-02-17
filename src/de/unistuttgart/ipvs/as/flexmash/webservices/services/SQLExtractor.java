package de.unistuttgart.ipvs.as.flexmash.webservices.services;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * class to extract data from a SQL database
 */
@WebService(name = "SQLExtractor")
public class SQLExtractor {

	/**
	 * Extracts data from a SQL database
	 * 
	 * @param ip
	 *            the ip of the database
	 * @param port
	 *            the port of the database
	 * @param table
	 *            the table name
	 * 
	 * @return the extracted data as JSON
	 */
	@SuppressWarnings("unchecked")
	@WebMethod(operationName = "extract")
	@WebResult(name = "key")
	public String extract(@WebParam(name = "ip") String ip, @WebParam(name = "port") String port,
			@WebParam(name = "user") String user, @WebParam(name = "password") String password,
			@WebParam(name = "databaseName") String databaseName, @WebParam(name = "table") String table) {
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");

			// Setup the connection with the DB
			Connection connect = (Connection) DriverManager.getConnection(
					"jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?user=" + user + "&password=" + password);

			// Statements allow to issue SQL queries to the database
			Statement statement = (Statement) connect.createStatement();

			// Result set get the result of the SQL query
			ResultSet resultSet = statement.executeQuery("select * from " + table);

			JSONObject result = new JSONObject();
			result.put("result", writeResultSet(resultSet));
			connect.close();
			return result.toJSONString();

		} catch (SQLException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * Result set to JSON
	 * 
	 * @param resultSet
	 *            the result from the SQL database
	 * @return the json representation
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	private JSONArray writeResultSet(ResultSet resultSet) throws SQLException {

		JSONArray result = new JSONArray();
		ResultSetMetaData metaData = resultSet.getMetaData();
		int columns = metaData.getColumnCount();		
		
		// write data to JSON
		while (resultSet.next()) {
			JSONObject temp = new JSONObject();
			for (int i = 1; i < (columns+1); i++) {
				Object value = resultSet.getObject(i);
				temp.put(metaData.getColumnLabel(i), value);
			}
			result.add(temp);
		}
		return result;
	}

}
