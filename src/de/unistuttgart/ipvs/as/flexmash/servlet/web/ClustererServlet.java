package de.unistuttgart.ipvs.as.flexmash.servlet.web;

import org.json.JSONArray;
import org.json.JSONObject;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.POST;
import java.io.IOException;
import java.sql.*;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet("/Clusterer")
public class ClustererServlet extends HttpServlet {

    private Instances[] instances = new Instances[2];
    int[][] assignments = new int[2][];//try to use a map
    private final String[] policyParams = {"processingTime", "memory"};

    private HashMap<String, Instances[]> instancesMap = new HashMap<String, Instances[]>();
    private HashMap<String, int[]> assignmentsMap = new HashMap<String, int[]>();

    private Connection con;
    private Statement st;
    private ResultSet rs;

    JSONArray arrJSON;

    private static final Logger LOGGER = Logger.getLogger(ClustererServlet.class.getName());

    @Override
    @POST
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strJSON = req.getParameter("operations");
        System.out.println("JSON: " + strJSON);

        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            con = DriverManager.getConnection("jdbc:mysql://192.168.209.250:3306/operations", "root", "password");
            st = con.createStatement();

            arrJSON = new JSONArray(strJSON);
            for(int i=0; i<arrJSON.length(); i++){
                JSONObject objJSON = arrJSON.getJSONObject(i);
                String serviceId = objJSON.getString("serviceId");
                String query = "DROP TABLE IF EXISTS operations.`"+serviceId+"`";
                st.executeUpdate(query);
                query = "CREATE TABLE IF NOT EXISTS operations.`"+serviceId+"` (" +
                        "id INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "processingTime DOUBLE UNSIGNED NOT NULL, " +
                        "memory DOUBLE UNSIGNED NOT NULL)";
                st.executeUpdate(query);
                query = "INSERT INTO operations.`"+serviceId+"` (id, processingTime, memory) " +
                        "VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, /*i+0*/getRandom());
                preparedStatement.setInt(3, /*i+100*/getRandom());
                preparedStatement.executeUpdate();

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, /*i+1*/getRandom());
                preparedStatement.setInt(3, /*i+101*/getRandom());
                preparedStatement.executeUpdate();

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, /*i+2*/getRandom());
                preparedStatement.setInt(3, /*i+102*/getRandom());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        loadData();
        createClusters();
        createClusterTable();

        resp.setContentType("text/plain");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("Clustering Finished");
    }

    private int getRandom() {
        Random r = new Random();
        int low = 10;
        int high = 100;
        int result = r.nextInt(high-low) + low;
        return result;
    }

    private void loadData() {
        try {
            for (int i = 0; i < policyParams.length; i++) {

                String policyParam = policyParams[i];

                InstanceQuery query = new InstanceQuery();

                query.setDatabaseURL("jdbc:mysql://192.168.209.250:3306/operations");
                query.setUsername("root");
                query.setPassword("password");
                String strQuery = constructQuery(policyParam);
                query.setQuery(strQuery);
                //if your data is sparse, then you can say so, too:
                //query.setSparseData(true);
                instances[i] = query.retrieveInstances();

                for (int j = 0; j < instances[i].numInstances(); j++) {
                    Instance ins = instances[i].get(j);
                    System.out.println(ins.toString());
//                    System.out.println(ins.toDoubleArray()[0]);
//                    double[] arr = ins.toDoubleArray();
//                    System.out.println(arr[0]);
//                    LOGGER.log(Level.INFO, "Hello " + arr[0]);
//                    System.out.println(ins.value(new Attribute("average")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String constructQuery(String argParameter) {
        String strQuery = "SELECT AVG("+argParameter+") AS average FROM ";
        for(int i=0; i<arrJSON.length(); i++) {
            JSONObject objJSON = arrJSON.getJSONObject(i);
            String serviceId = objJSON.getString("serviceId");
            if (i < arrJSON.length()-1) {
                strQuery += "operations.`"+serviceId+"` UNION ALL SELECT AVG("+argParameter+") FROM ";
            } else {
                strQuery += "operations.`"+serviceId+"`";
            }
        }
        return strQuery;
    }

    private void createClusters() {
        try {
            for (int i = 0; i < policyParams.length; i++) {

                SimpleKMeans kmeans = new SimpleKMeans();
                kmeans.setPreserveInstancesOrder(true);
                kmeans.setNumClusters(3);
                kmeans.buildClusterer(instances[i]);

                // Show cluster association.
                assignments[i] = kmeans.getAssignments();
                for (int cluster : assignments[i])
                    System.out.println(cluster);

                // Show cluster centers.
                Instances centers = kmeans.getClusterCentroids();
                System.out.println(centers);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createClusterTable(){
        try {
//            String query = "DROP TABLE IF EXISTS operations.cluster";
//            st.executeUpdate(query);

            boolean tableExists = false;
            String query = "SHOW TABLES LIKE '%cluster%'";
            rs = st.executeQuery(query);
            while (rs.next()) {
//                String name = rs.getString(1);
//                System.out.println("Table name: "+name);
                tableExists = true;
            }

            query = "CREATE TABLE IF NOT EXISTS operations.cluster (" +
                    "id VARCHAR(36) NOT NULL PRIMARY KEY, " +
                    "name VARCHAR(100) NOT NULL, " +
                    "avg_processingTime DOUBLE UNSIGNED NOT NULL, " +
                    "cluster_processingTime INTEGER UNSIGNED NOT NULL ," +
                    "avg_memory DOUBLE UNSIGNED NOT NULL, " +
                    "cluster_memory INTEGER UNSIGNED NOT NULL)";
            st.executeUpdate(query);

            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                String serviceId = objJSON.getString("serviceId");
                String name = objJSON.getString("name");
                if (tableExists) {
                    query = "UPDATE operations.cluster SET " +
                            "avg_processingTime=?, " +
                            "cluster_processingTime=?, " +
                            "avg_memory=?, " +
                            "cluster_memory=? " +
                            "WHERE id=?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setDouble(1, instances[0].get(i).toDoubleArray()[0]);
                    preparedStatement.setInt(2, assignments[0][i]);
                    preparedStatement.setDouble(3, instances[1].get(i).toDoubleArray()[0]);
                    preparedStatement.setInt(4, assignments[1][i]);
                    preparedStatement.setString(5, serviceId);
                    preparedStatement.executeUpdate();
                } else {
                    query = "INSERT INTO operations.cluster (id, name, avg_processingTime, cluster_processingTime, avg_memory, cluster_memory) " +
                            "VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, serviceId);
                    preparedStatement.setString(2, name);
                    preparedStatement.setDouble(3, instances[0].get(i).toDoubleArray()[0]);
                    preparedStatement.setInt(4, assignments[0][i]);
                    preparedStatement.setDouble(5, instances[1].get(i).toDoubleArray()[0]);
                    preparedStatement.setInt(6, assignments[1][i]);
                    preparedStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
