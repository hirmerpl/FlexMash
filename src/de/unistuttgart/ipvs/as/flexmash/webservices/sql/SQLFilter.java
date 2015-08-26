package de.unistuttgart.ipvs.as.flexmash.webservices.sql;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.jws.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.unistuttgart.ipvs.as.flexmash.utils.twitter_utils.TwitterManager;

/**
 * extracts data from a twitter feed
 */
@WebService(name = "SQLFilter")
public class SQLFilter {

	@WebMethod(operationName = "filterData")
	@WebResult(name = "key")
	public String filterData(@WebParam(name = "key") String key, @WebParam(name = "criteria") String criteria) {
		try {
			System.out.println("-------------------------------------------------------------------------------------");
			System.out.println("Twitter Extractor reached!");
			System.out.println("-------------------------------------------------------------------------------------");

			JSONParser parser = new JSONParser();
			JSONObject articles;

			articles = (JSONObject) parser.parse(key);

			Set keys = articles.keySet();
			List<String> toBeRemoved = new ArrayList();
			for (Object currKey : keys) {

				String articleKey = (String) currKey;

				if (articles.get(articleKey) != null) {
					JSONObject data = new JSONObject();
					JSONObject article = (JSONObject) articles.get(articleKey);

					String query = (String) article.get("category");

					System.out.println("-------------------------------------------------------------------------------------");
					System.out.println("CURRENT TWITTER SEACH STRING IS: " + query);
					System.out.println("-------------------------------------------------------------------------------------");

					if (query != null && !query.equals("")) {
						TwitterManager twitterManager = new TwitterManager();

						data = twitterManager.performQuery(query);

						article.put("sentiment", data.get("sentiment"));
						JSONArray exampleTweets = (JSONArray) data.get("exampleTweets");

						article.put("exampleTweets", exampleTweets);

						articles.put(articleKey, article);
					} else {
						toBeRemoved.add(articleKey);
					}
				}
			}
			for (String s: toBeRemoved) {
				articles.remove(s);
			}
			
			return articles.toJSONString();

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
