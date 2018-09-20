package de.unistuttgart.ipvs.as.flexmash.servlet.web;

import org.json.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/GetCluster")
public class GetClusterServlet extends HttpServlet {

    private final String[] policyParams = {"processingTime", "memory", "disk"};
    /*int[] cluster = new int[3];
    double[] average = new double[3];*/

    private JSONObject respJSON = new JSONObject();

    private Connection con;
    private Statement st;
    private ResultSet rs;
    @Override
    @GET
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String op = req.getParameter("opName");
        System.out.println("Operation Name: " + op);

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://192.168.209.250:3306/operations", "root", "password");
            st = con.createStatement();

            String query = "SELECT cluster.cluster FROM operations.cluster WHERE id = \"" + op + "\"";
            rs = st.executeQuery(query);
            while (rs.next()) {
                String overallCluster = rs.getString("cluster");
                respJSON.put("overallCluster", overallCluster);
            }

            for(int i=0; i<policyParams.length; i++) {
                query = "SELECT cluster.cluster_"+policyParams[i]+", cluster.avg_"+policyParams[i]+" FROM operations.cluster WHERE id = \"" + op + "\"";
                rs = st.executeQuery(query);
                while (rs.next()) {
                    String cluster = rs.getString("cluster_"+policyParams[i]);
                    respJSON.put("cluster_"+policyParams[i], cluster);
                    double average = rs.getDouble("avg_"+policyParams[i]);
                    respJSON.put("avg_"+policyParams[i], average);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter printout = resp.getWriter();
        printout.print(respJSON);
    }
}
