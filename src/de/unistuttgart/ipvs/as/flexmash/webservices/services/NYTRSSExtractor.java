package de.unistuttgart.ipvs.as.flexmash.webservices.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.jws.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * class to extract data from a SQL database
*/
@WebService(name = "NYTRSSExtractor")
public class NYTRSSExtractor {

	/**
	 * Extracts data from a SQL database
	 * 
	 * @param address
	 * 			the database address
	 * 
	 * @return the key to the data in the database
	 */
	@WebMethod(operationName = "extract")
	@WebResult(name = "key")
	public String extract(@WebParam(name = "address") String address) {

		System.out.println("NYTRSSExtractor SERVICE REACHED");
		System.out.print("--------------------------------------------------------------------------");

		try {
			String uri = "http://rss.nytimes.com/services/xml/rss/nyt/" + address + ".xml";
			System.out.println("URI: "+ uri);

			URL url = new URL(uri);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/xml");

			InputStream xml = connection.getInputStream();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);

			Map<String, HashMap<String, String>> articles = new HashMap<String, HashMap<String, String>>();
			

			for (int i = 0; i < doc.getChildNodes().getLength(); i++) {
				Node n = doc.getChildNodes().item(i);
				if (n.getNodeName().equals("rss")) {
										
					Node channelNode = n.getChildNodes().item(1);
					System.out.println("channel node: " + channelNode.getNodeName());
					for (int j = 0; j < channelNode.getChildNodes().getLength(); j++) {
						System.out.println(channelNode.getChildNodes().item(j).getNodeName());
						if (channelNode.getChildNodes().item(j).getNodeName().equals("item")) {
							HashMap<String, String> articleDetails = new HashMap<String, String>();

							// each single article item contained !!!
							String title = "";
							String link = "";
							String category = "";

							int counter = 0;

							Node itemNode = channelNode.getChildNodes().item(j);
							for (int k = 0; k < itemNode.getChildNodes().getLength(); k++) {
								Node currentItem = itemNode.getChildNodes().item(k);
								if (currentItem.getNodeName().equals("title")) {
									title = currentItem.getTextContent();
								} else if (currentItem.getNodeName().equals("link")) {
									link = currentItem.getTextContent();
								} else if (currentItem.getNodeName().equals("category")) {
									String keyword = currentItem.getTextContent().replace(",", " ");
									if (!(counter > 3)) {
										if (!category.equals("")) {
											category += ", " + keyword;
											counter++;
										} else if (category.equals("")) {
											category += keyword;
											counter++;
										}
									}
								}
							}

							// add to map
							articleDetails.put("title", title);
							articleDetails.put("link", link);
							articleDetails.put("category", category);

							System.out.println("Title: " + title + " Link: " + link + " Category: " + category);
							articles.put(Integer.toString(j), articleDetails);
						}
					}
				}
			}

			org.json.simple.JSONObject returnObject = new org.json.simple.JSONObject(articles);

			return returnObject.toJSONString();

		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "failed";
	}
}
