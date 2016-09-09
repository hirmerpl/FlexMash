package de.unistuttgart.ipvs.as.flexmash.webservices.operations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

import javax.jws.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebService(name = "Join")
/**
 * web service that joins two data sources
 */
public class Join {

	@WebMethod(operationName = "joinData")
	@WebResult(name = "key")
	/**
	 * joins two data sources
	 * 
	 * @param key1
	 * 			cache key of the first data source
	 * @param key2
	 * 			cache key of the second data source
	 * @param criteria
	 * 			the join criteria
	 * @return the key of the joined data in the cache
	 */
	public String joinData(@WebParam(name = "key1") String key1, @WebParam(name = "key2") String key2, @WebParam(name = "criteria") String criteria) {
		try {

			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("JOIN reached!");
			System.out.println("-------------------------------------------------------------------------------------");

			File file = new File("C:\\result.html");

			String script = "<script>window.twttr = (function(d, s, id) {				  var js, fjs = d.getElementsByTagName(s)[0],				    t = window.twttr || {};				  if (d.getElementById(id)) return t;				  js = d.createElement(s);				  js.id = id;				  js.src = \"https://platform.twitter.com/widgets.js\";				  fjs.parentNode.insertBefore(js, fjs);				 				  t._e = [];				  t.ready = function(f) {				    twttr.widgets.load();				    t._e.push(f);				  };				 				  return t;				}(document, \"script\", \"twitter-wjs\"));				 </script>";

			String html = script
					+ "<html><head><link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css\"><title>Data Mashup Result (Robust)</title></head><body><table class=\"table table-striped\" style=\"width: 100%;\"><tr><th>Article</th><th>Keywords</th><th>Overall Sentiment</th><th>Example Tweets</th></tr>";

			JSONParser parser = new JSONParser();
			JSONObject articles = (JSONObject) parser.parse(key1);

			@SuppressWarnings("rawtypes")
			Set keys = articles.keySet();

			for (Object key : keys) {
				if (key instanceof String) {

					System.out.println("-------------------------------------------------------------------------------------");
					System.out.println("Getting Entries!");
					System.out.println("-------------------------------------------------------------------------------------");

					String currKey = (String) key;
					JSONObject article = (JSONObject) articles.get(currKey);

					if (article.get("title") != null && article.get("link") != null && article.get("category") != null && article.get("sentiment") != null
							&& article.get("exampleTweets") != null) {

						html += "<tr><td>" + article.get("title") + " (<a href=" + article.get("link") + " target=\"_blank\">link)</td><td>"
								+ article.get("category") + "</td><td>" + article.get("sentiment") + "</td><td>";

						JSONArray exampleTweets = (JSONArray) article.get("exampleTweets");

						for (int i = 0; i < exampleTweets.size(); i++) {
							html += exampleTweets.get(i) + "<hr/>";
						}
						html += "</td></tr>";
					}
				}
			}

			html += "</table></body></html>";
			BufferedWriter writer;
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(html);
			writer.close();

			return html;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
