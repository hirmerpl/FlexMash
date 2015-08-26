package de.unistuttgart.ipvs.as.flexmash.servlet.engine;

import java.io.IOException;

import javax.xml.soap.*;

/**
 * class to start, manage and stop a process in the BPEL engine
 */
public class BPELEngineCommunicator {

	/**
	 * 
	 * method to start a process in the BPEL engine and process it's output
	 * 
	 * @param sqlExtractorAddress
	 *            input parameters of the web service that extracts data using
	 *            SQL
	 * @param twitterExtractorHashtag
	 *            input parameters of the web service that extracts Tweets using
	 *            the Twitter API
	 * @param sqlFilterCriteria
	 *            input parameters of the web service that filters database data
	 * @param twitterFilterCriteria
	 *            input parameters of the web service that filters tweets
	 * @param joinCriteria
	 *            input parameters of the web service that joins data
	 * @return the output of the executed process
	 */
	public String callEngine(String sqlExtractorAddress, String twitterExtractorHashtag, String sqlFilterCriteria, String twitterFilterCriteria,
			String joinCriteria) {

		String reply = null;

		try {
			// Create Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// TODO: use config file
			// SOAP Message
			String url = "http://localhost:8080/ode/processes/DataMashupProcess";

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
			SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("sqlExtractorAdress", "bpel");
			soapBodyElem1.addTextNode(sqlExtractorAddress);
			SOAPElement soapBodyElem5 = soapBodyElem.addChildElement("twitterExtractorHashtag", "bpel");
			soapBodyElem5.addTextNode(twitterExtractorHashtag);
			SOAPElement soapBodyElem6 = soapBodyElem.addChildElement("sqlFilterCriteria", "bpel");
			soapBodyElem6.addTextNode("");
			SOAPElement soapBodyElem7 = soapBodyElem.addChildElement("twitterFilterCriteria", "bpel");
			soapBodyElem7.addTextNode(twitterFilterCriteria);
			SOAPElement soapBodyElem8 = soapBodyElem.addChildElement("joinCriteria", "bpel");
			soapBodyElem8.addTextNode(joinCriteria);

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
