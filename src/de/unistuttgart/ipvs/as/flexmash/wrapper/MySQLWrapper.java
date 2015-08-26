package de.unistuttgart.ipvs.as.flexmash.wrapper;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.util.ArrayList;

/**
 * Wrapper class to connect a MySQL database
 */
public class MySQLWrapper {

	/**
	 * receives data from a MySQL database
	 * 
	 * @param dbName
	 *            the name of the database
	 * @param dbURL
	 *            the URL of the database
	 * @param dbUsername
	 *            the user name of the database
	 * @param dbPassword
	 *            the password of the database
	 * 
	 * @return the data as list
	 */
	public static ArrayList<String> getDataFromMySQL(String dbName, String dbURL, String dbUsername, String dbPassword) {

		Connection connect = null;
		ResultSet resultSet = null;
		DatabaseMetaData rsmeta = null;
		ArrayList<String> tables = new ArrayList<String>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://" + dbURL + "/" + dbName + "?user=" + dbUsername + "&password=" + dbPassword);

			rsmeta = connect.getMetaData();

			resultSet = rsmeta.getTables(null, null, "%", null);

			while (resultSet.next()) {
				tables.add(resultSet.getString(3));
			}
			connect.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tables;
	}
}
