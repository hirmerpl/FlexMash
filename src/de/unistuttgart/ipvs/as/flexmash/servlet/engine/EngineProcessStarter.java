package de.unistuttgart.ipvs.as.flexmash.servlet.engine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * class to start the BPEL engine and deploy a process
 */
public class EngineProcessStarter {

	/**
	 * generates all necessary files to deploy a process on ApacheODE
	 * 
	 * @param xml
	 *            the BPEl process as XML string
	 */
	public void generateFiles(String xml) {
		generateBPELFile(xml);
		zipFiles();
		deployZip();
	}

	/**
	 * generates a BPEL file from a BPEL XML string
	 * 
	 * @param bpel
	 *            the BPEL XML string
	 */
	private void generateBPELFile(String bpel) {
		Writer writer = null;

		try {
			// TODO: config file
			File file = new File("files/DataMashupProcess.bpel");
			File dir = new File("files");
			if (!dir.exists()) {
				dir.mkdir();
				System.out.println("dir doesn't exist");
			} else {
				System.out.println("dir exists");
			}
			if (!file.exists()) {
				file.createNewFile();
				System.out.println("bpelfile doesn't exist");
			} else {
				System.out.println("bpelfile exists");
			}

			writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()));
			writer.write(bpel);
		} catch (IOException ex) {
			System.out.println("IOException");
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

	}

	/**
	 * packs all necessary deployment components in a ZIP file to be compatible
	 * with the ApacheODE engine
	 */
	private void zipFiles() {
		ZipOutputStream zipOutStream = null;
		FileInputStream bpelInStream = null;
		FileInputStream wsdlInStream = null;
		FileInputStream deployInStream = null;
		FileInputStream joinInStream = null;
		FileInputStream twitEInStream = null;
		FileInputStream sqlEInStream = null;
		FileInputStream twitFInStream = null;
		FileInputStream sqlFInStream = null;

		try {
			// TODO: paths config

			zipOutStream = new ZipOutputStream(new FileOutputStream("files/DataMashupProcess.zip"));

			bpelInStream = new FileInputStream("files/DataMashupProcess.bpel");
			wsdlInStream = new FileInputStream("files/DataMashupProcessArtifacts.wsdl");
			deployInStream = new FileInputStream("files/deploy.xml");
			joinInStream = new FileInputStream("files/Join.wsdl");
			twitEInStream = new FileInputStream("files/TwitterEtractor.wsdl");
			sqlEInStream = new FileInputStream("files/SQLExtractor.wsdl");
			twitFInStream = new FileInputStream("files/TwitterFilter.wsdl");
			sqlFInStream = new FileInputStream("files/SQLFilter.wsdl");

			addToZipStream(bpelInStream, zipOutStream, "files/DataMashupProcess.bpel");
			addToZipStream(wsdlInStream, zipOutStream, "files/DataMashupProcessArtifacts.wsdl");
			addToZipStream(deployInStream, zipOutStream, "files/deploy.xml");
			addToZipStream(joinInStream, zipOutStream, "files/Join.wsdl");
			addToZipStream(twitEInStream, zipOutStream, "files/TwitterEtractor.wsdl");
			addToZipStream(sqlEInStream, zipOutStream, "files/SQLExtractor.wsdl");
			addToZipStream(twitFInStream, zipOutStream, "files/TwitterFilter.wsdl");
			addToZipStream(sqlFInStream, zipOutStream, "files/SQLFilter.wsdl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (bpelInStream != null) {
				try {
					bpelInStream.close();
				} catch (IOException e) {
				}
			}
			if (wsdlInStream != null) {
				try {
					wsdlInStream.close();
				} catch (IOException e) {
				}
			}
			if (deployInStream != null) {
				try {
					deployInStream.close();
				} catch (IOException e) {
				}
			}
			if (zipOutStream != null) {
				try {
					zipOutStream.closeEntry();
					zipOutStream.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * helper method to add data to a zip file
	 * 
	 * @param in
	 *            the file as stream
	 * @param out
	 *            the output stream
	 * @param name
	 *            the name of the file
	 * @throws IOException
	 *             this exception occurs if the file is not readable or damaged
	 */
	private void addToZipStream(FileInputStream in, ZipOutputStream out, String name) throws IOException {
		int len;
		byte[] buffer = new byte[2048];
		out.putNextEntry(new ZipEntry(new File(name).getName()));
		while ((len = in.read(buffer, 0, buffer.length)) > 0) {
			out.write(buffer, 0, len);
		}

	}

	/**
	 * deploys the generated ZIP in the ApacheODE BPEL engine
	 */
	private void deployZip() {
		try {
			// Create Connection
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			// SOAP Message
			String url = "http://localhost:8080/ode/processes/DeploymentService";

			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			SOAPPart soapPart = soapMessage.getSOAPPart();

			String serverURI = "http://www.apache.org/ode/pmapi";

			// SOAP Envelope
			SOAPEnvelope envelope = soapPart.getEnvelope();
			envelope.addNamespaceDeclaration("pmap", "http://www.apache.org/ode/pmapi");
			envelope.addNamespaceDeclaration("dep", "http://www.apache.org/ode/deployapi");

			// SOAP Body
			SOAPBody soapBody = envelope.getBody();
			SOAPElement soapBodyElem = soapBody.addChildElement("deploy", "pmap");
			SOAPElement soapBodyElemA = soapBodyElem.addChildElement("name");
			soapBodyElemA.addTextNode("DataMashupProcess");
			SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("package");
			SOAPElement soapBodyElem2 = soapBodyElem1.addChildElement("zip", "dep");

			Path zipFilePath = Paths.get("files/DataMashupProcess.zip");
			byte[] zipFileData = Files.readAllBytes(zipFilePath);
			soapBodyElem2.addTextNode(Base64.getMimeEncoder().encodeToString(zipFileData));

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

			soapConnection.close();
		} catch (SOAPException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
}
