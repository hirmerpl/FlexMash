package de.unistuttgart.ipvs.as.flexmash.utils.twitter_utils;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * class to manage the twitter feed node
 */
public class TwitterManager {
	SentimentClassifier sentClassifier;
	int LIMIT = 5; // the number of retrieved tweets
	ConfigurationBuilder cb;
	Twitter twitter;

	/**
	 * class constructor, handle access token
	 */
	public TwitterManager() {
		cb = new ConfigurationBuilder();
		cb.setOAuthConsumerKey("ouvmuU7vamjO1TbM6HTCCELWb");
		cb.setOAuthConsumerSecret("m3aC4o24a3X3M6IiXZjHrKAHRvnagVeVIb2I2XV4XAzCbRN0EF");
		cb.setOAuthAccessToken("3064389503-OsGVQ4JnhOhYHTyDYilbModW8Es80kNPm4lMsDc");
		cb.setOAuthAccessTokenSecret("4nJdPenR49QZMI4hgETUMv83RYJHw2prgkOhRRcfD3NNw");
		twitter = new TwitterFactory(cb.build()).getInstance();
		sentClassifier = new SentimentClassifier();
	}

	/**
	 * accesses the Twitter API and returns Tweets for a hashtag
	 * 
	 * @param inQuery
	 *            the hashtag
	 * @return the Tweets as JSON
	 * 
	 * @throws InterruptedException
	 *             thrown if the Twitter feed is interrupted
	 * @throws IOException
	 *             thrown if there is a problem with IO
	 */
	@SuppressWarnings("unchecked")
	public JSONObject performQuery(String inQuery) throws InterruptedException, IOException {
		try {
			if (!inQuery.equals("") && inQuery != null) {
				Query query = new Query(inQuery);
				query.setCount(5);
				JSONObject result = new JSONObject();
				JSONArray exampleTweets = new JSONArray();
				int posCounter = 0;
				int negCounter = 0;

				int count = 0;
				QueryResult r = null;

				do {
					ArrayList<Status> ts = new ArrayList<Status>();

					r = twitter.search(query);
					ts = (ArrayList<Status>) r.getTweets();

					System.out.println("-------------------------------------------------------------------------------------");
					System.out.println("NUMER OF FOUND TWEETS: " + ts.size());
					System.out.println("-------------------------------------------------------------------------------------");

					for (int i = 0; i < ts.size() && count < LIMIT; i++) {
						count++;
						Status t = ts.get(i);
						long link = t.getId();

						OEmbedRequest oembed = new OEmbedRequest(link, "statuses/oembed");

						OEmbed test = twitter.getOEmbed(oembed);
						System.out.println("EMBED CODE: " + test.getHtml());

						exampleTweets.add(test.getHtml());

						String sent = sentClassifier.classify(t.getText());
						System.out.println("CURRENT SENT IS: " + sent);
						if (sent.equals("pos")) {
							posCounter++;
						} else {
							negCounter++;
						}
					}
				} while ((query = r.nextQuery()) != null && count < LIMIT);

				String overAllSentiment = "";

				if (posCounter > negCounter) {
					overAllSentiment = "Sentiment is Positive!";
				} else if (negCounter > posCounter) {
					overAllSentiment = "Sentiment is Negative!";
				} else {
					overAllSentiment = "Sentiment is Neutral";
				}

				result.put("sentiment", overAllSentiment);
				result.put("exampleTweets", exampleTweets);
				return result;
			}
		} catch (TwitterException te) {
			System.out.println("Couldn't connect: " + te);
		}
		return null;
	}
}
