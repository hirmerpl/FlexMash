package de.unistuttgart.ipvs.as.flexmash.BPMN;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import de.unistuttgart.ipvs.as.flexmash.BPMN.ExecutionHelper;

public class TwitterdsExe implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(TwitterdsExe.class.getName());

	public static boolean wasExecuted = false;

	public void execute(DelegateExecution execution) throws Exception {
		ExecutionHelper Helper = new ExecutionHelper();
		System.out.println("Executing process: "+execution.getCurrentActivityId()+" at "+ new Timestamp(System.currentTimeMillis()));
		System.out.println(String.format("Parents for the Activity %s are:",execution.getCurrentActivityId()) );
		ArrayList<String > predecessors = (ArrayList<String >)execution.getVariable(execution.getCurrentActivityId()+"Pre");
		predecessors.forEach((l)-> System.out.println(l));
	}
}
