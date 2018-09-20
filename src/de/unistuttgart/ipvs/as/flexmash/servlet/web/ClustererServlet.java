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
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Logger;

@WebServlet("/Clusterer")
public class ClustererServlet extends HttpServlet {

    private Instances overallInstances;
    int[] overallAssignments;
    private Instances overallCentroids;

    double[] averageOverallCentroids = new double[3];
    String[] overallClusterTranslation = new String[3];

    private Instances[] instances = new Instances[3];
    int[][] assignments = new int[3][];
    private Instances[] centroids = new Instances[3];

    double[][] averageCentroids = new double[3][3];
    String[][] clusterTranslation = new String[3][3];

    private final String[] policyParams = {"processingTime", "memory", "disk"};

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
                        "memory DOUBLE UNSIGNED NOT NULL, " +
                        "disk DOUBLE UNSIGNED NOT NULL)";
                st.executeUpdate(query);
                query = "INSERT INTO operations.`"+serviceId+"` (id, processingTime, memory, disk) " +
                        "VALUES (?, ?, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, /*i+0*/getRandom());
                preparedStatement.setInt(3, /*i+100*/getRandom());
                preparedStatement.setInt(4, /*i+200*/getRandom());
                preparedStatement.executeUpdate();

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, /*i+1*/getRandom());
                preparedStatement.setInt(3, /*i+101*/getRandom());
                preparedStatement.setInt(4, /*i+201*/getRandom());
                preparedStatement.executeUpdate();

                preparedStatement = con.prepareStatement(query);
                preparedStatement.setNull(1, 0);
                preparedStatement.setInt(2, /*i+2*/getRandom());
                preparedStatement.setInt(3, /*i+102*/getRandom());
                preparedStatement.setInt(4, /*i+202*/getRandom());
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
        resp.getWriter().write("Clustering finished");
    }

    private int getRandom() {
        Random r = new Random();
        int low = 50;
        int high = 999;
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

        overallInstances = instances[0];
        for (int i = 1; i < policyParams.length; i++) {
            overallInstances = Instances.mergeInstances(overallInstances, instances[i]);
        }
        for (int j = 0; j < overallInstances.numInstances(); j++) {
            Instance ins = overallInstances.get(j);
            System.out.println(ins.toString());
        }
    }

    private String constructQuery(String argParameter) {
        String strQuery = "SELECT AVG("+argParameter+") AS avg_"+argParameter+" FROM ";
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
                centroids[i] = kmeans.getClusterCentroids();
                System.out.println(centroids[i]);

                for (int j = 0; j < centroids[i].numInstances(); j++) {
                    Instance ins = centroids[i].get(j);
                    double[] _centroids = ins.toDoubleArray();
                    averageCentroids[i][j] = _centroids[0];
                }

                double[] _averageCentroids = averageCentroids[i].clone();
                Arrays.sort(_averageCentroids);

                if(_averageCentroids[0] == averageCentroids[i][0]) {
                    clusterTranslation [i][0] = "Low";
                } else if (_averageCentroids[0] == averageCentroids[i][1]) {
                    clusterTranslation [i][1] = "Low";
                } else if (_averageCentroids[0] == averageCentroids[i][2]) {
                    clusterTranslation [i][2] = "Low";
                }

                if(_averageCentroids[1] == averageCentroids[i][0]) {
                    clusterTranslation[i][0] = "Medium";
                } else if (_averageCentroids[1] == averageCentroids[i][1]) {
                    clusterTranslation[i][1] = "Medium";
                } else if (_averageCentroids[1] == averageCentroids[i][2]) {
                    clusterTranslation[i][2] = "Medium";
                }

                if(_averageCentroids[2] == averageCentroids[i][0]) {
                    clusterTranslation[i][0] = "High";
                } else if (_averageCentroids[2] == averageCentroids[i][1]) {
                    clusterTranslation[i][1] = "High";
                } else if (_averageCentroids[2] == averageCentroids[i][2]) {
                    clusterTranslation[i][2] = "High";
                }
            }

            SimpleKMeans kmeans = new SimpleKMeans();
            kmeans.setPreserveInstancesOrder(true);
            kmeans.setNumClusters(3);
            kmeans.buildClusterer(overallInstances);

            // Show cluster association.
            overallAssignments = kmeans.getAssignments();
            for (int cluster : overallAssignments)
                System.out.println(cluster);

            // Show cluster centers.
            overallCentroids = kmeans.getClusterCentroids();
            System.out.println(overallCentroids);

            for (int j = 0; j < overallCentroids.numInstances(); j++) {
                Instance ins = overallCentroids.get(j);
                double[] _centroids = ins.toDoubleArray();
                double sum = 0;
                for (int i = 0; i < policyParams.length; i++) {
                    sum += _centroids[i];
                }
                averageOverallCentroids[j] = sum/policyParams.length;
            }

            double[] _averageOverallCentroids = averageOverallCentroids.clone();
            Arrays.sort(_averageOverallCentroids);

            if(_averageOverallCentroids[0] == averageOverallCentroids[0]) {
                overallClusterTranslation[0] = "Low";
            } else if (_averageOverallCentroids[0] == averageOverallCentroids[1]) {
                overallClusterTranslation[1] = "Low";
            } else if (_averageOverallCentroids[0] == averageOverallCentroids[2]) {
                overallClusterTranslation[2] = "Low";
            }

            if(_averageOverallCentroids[1] == averageOverallCentroids[0]) {
                overallClusterTranslation[0] = "Medium";
            } else if (_averageOverallCentroids[1] == averageOverallCentroids[1]) {
                overallClusterTranslation[1] = "Medium";
            } else if (_averageOverallCentroids[1] == averageOverallCentroids[2]) {
                overallClusterTranslation[2] = "Medium";
            }

            if(_averageOverallCentroids[2] == averageOverallCentroids[0]) {
                overallClusterTranslation[0] = "High";
            } else if (_averageOverallCentroids[2] == averageOverallCentroids[1]) {
                overallClusterTranslation[1] = "High";
            } else if (_averageOverallCentroids[2] == averageOverallCentroids[2]) {
                overallClusterTranslation[2] = "High";
            }

            System.out.println(kmeans.toString());

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
                    "cluster VARCHAR(6) NOT NULL," +
                    "avg_processingTime DOUBLE UNSIGNED NOT NULL, " +
                    "cluster_processingTime VARCHAR(6) NOT NULL, " +
                    "avg_memory DOUBLE UNSIGNED NOT NULL, " +
                    "cluster_memory VARCHAR(6) NOT NULL, " +
                    "avg_disk DOUBLE UNSIGNED NOT NULL, " +
                    "cluster_disk VARCHAR(6) NOT NULL)";
            st.executeUpdate(query);

            for (int i = 0; i < arrJSON.length(); i++) {
                JSONObject objJSON = arrJSON.getJSONObject(i);
                String serviceId = objJSON.getString("serviceId");
                String name = objJSON.getString("name");
                if (tableExists) {
                    query = "UPDATE operations.cluster SET " +
                            "cluster=?, " +
                            "avg_processingTime=?, " +
                            "cluster_processingTime=?, " +
                            "avg_memory=?, " +
                            "cluster_memory=?, " +
                            "avg_disk=?, " +
                            "cluster_disk=? " +
                            "WHERE id=?";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, overallClusterTranslation[overallAssignments[i]]);
                    preparedStatement.setDouble(2, instances[0].get(i).toDoubleArray()[0]);
                    preparedStatement.setString(3, clusterTranslation[0][assignments[0][i]]);
                    preparedStatement.setDouble(4, instances[1].get(i).toDoubleArray()[0]);
                    preparedStatement.setString(5, clusterTranslation[1][assignments[1][i]]);
                    preparedStatement.setDouble(6, instances[2].get(i).toDoubleArray()[0]);
                    preparedStatement.setString(7, clusterTranslation[2][assignments[2][i]]);
                    preparedStatement.setString(8, serviceId);
                    preparedStatement.executeUpdate();
                } else {
                    query = "INSERT INTO operations.cluster (id, name, cluster, avg_processingTime, cluster_processingTime, avg_memory, cluster_memory, avg_disk, cluster_disk) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.setString(1, serviceId);
                    preparedStatement.setString(2, name);
                    preparedStatement.setString(3, overallClusterTranslation[overallAssignments[i]]);
                    preparedStatement.setDouble(4, instances[0].get(i).toDoubleArray()[0]);
                    preparedStatement.setString(5, clusterTranslation[0][assignments[0][i]]);
                    preparedStatement.setDouble(6, instances[1].get(i).toDoubleArray()[0]);
                    preparedStatement.setString(7, clusterTranslation[1][assignments[1][i]]);
                    preparedStatement.setDouble(8, instances[2].get(i).toDoubleArray()[0]);
                    preparedStatement.setString(9, clusterTranslation[2][assignments[2][i]]);
                    preparedStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
