package de.unistuttgart.ipvs.as.flexmash.utils.twitter_utils;

import java.io.File;
import java.io.IOException;

import com.aliasi.classify.ConditionalClassification;
import com.aliasi.classify.LMClassifier;
import com.aliasi.util.AbstractExternalizable;

/**
 * class to find out the sentiment of a Tweet
 */
public class SentimentClassifier {
	String[] categories;
	LMClassifier classifier;

	/**
	 * class constructor
	 */
	public SentimentClassifier() {

		try {
			classifier = (LMClassifier) AbstractExternalizable.readObject(new File(System.getenv("FLEXMASH")+"/files/classifier.txt"));
			categories = classifier.categories();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * classifies a text
	 * 
	 * @param text
	 *            the text as string
	 * @return the classification
	 */
	public String classify(String text) {
		ConditionalClassification classification = classifier.classify(text);
		return classification.bestCategory();
	}
}
