package de.unistuttgart.ipvs.as.flexmash.servlet.engine;

import java.io.IOException;

import javax.xml.soap.*;

/**
 * Class to start, manage and stop a process in the BPEL engine
 */
public class BPELEngineCommunicator {

	/**
	 * TODO: make generic
	 * 
	 * Method to start a process in the BPEL engine and process it's output
	 * 
	 * @param nytRSSExtractorAddress
	 *            input parameters of the web service that extracts data using SQL
	 * @param twitterExtractorHashtag
	 *            input parameters of the web service that extracts Tweets using  the Twitter API
	 * @param twitterServiceCriteria
	 *            input parameters of the web service that filters database data
	 * @param twitterFilterCriteria
	 *            input parameters of the web service that filters tweets
	 * @param joinCriteria
	 *            input parameters of the web service that joins data
	 * @return the output of the executed process
	 */
	public static String callEngine(String nytRSSExtractorAddress, String twitterExtractorHashtag, String twitterServiceCriteria, String twitterFilterCriteria,
			String joinCriteria) {

		String reply = null;

		try {
			// Create Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// TODO: use config file
			// SOAP Message
			String url = "http://localhost:8081/ode/processes/DataMashupProcess";

			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			String serverURI = "http://bpel.data_mashup.as.ipvs.informatik.uni-stuttgart.de";

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("bpel", "http://bpel.data_mashup.as.ipvs.informatik.uni_stuttgart.de");

			// SOAP Body
			SOAPBody soapBody = envelope.getBody();
			SOAPElement soapBodyElem = soapBody.addChildElement("DataMashupProcessRequest", "bpel");
			SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("NYTRSSExtractorAdress", "bpel");
			soapBodyElem1.addTextNode(nytRSSExtractorAddress);
			SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("twitterExtractorHashtag", "bpel");
			soapBodyElem5.addTextNode("");
			SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("TwitterServiceCriteria", "bpel");
			soapBodyElem6.addTextNode("");
			SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("twitterFilterCriteria", "bpel");
			soapBodyElem7.addTextNode("");
			SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("joinCriteria", "bpel");
			soapBodyElem8.addTextNode("");

			MimeHeaders headers = soapMessage.getMimeHeaders();
			headers.addHeader("SOAPAction", serverURI + "DataMashupProcessRequest");

			soapMessage.saveChanges();

			/* Print the request message */
			System.out.print("Request SOAP Message:");
			try {
				soapMessage.writeTo(System.out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println();

			SOAPMessage soapResponse = soapConnection.call(soapMessage, url);

			// print SOAP Response
			System.out.print("Response SOAP Message:");
			try {
				soapResponse.writeTo(System.out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// extract result
			SOAPBody soapResponseBody = soapResponse.getSOAPBody();
			reply = soapResponseBody.getElementsByTagName("tns:result").item(0).getTextContent();

			soapConnection.close();
		} catch (SOAPException e) {
			e.printStackTrace();
			reply = "SOAP-Exception";
		}

		return reply;
	}
}
