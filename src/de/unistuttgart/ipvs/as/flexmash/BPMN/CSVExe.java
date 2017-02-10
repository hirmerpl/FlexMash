package de.unistuttgart.ipvs.as.flexmash.BPMN;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class CSVExe implements JavaDelegate {
	 private final Logger LOGGER = Logger.getLogger(CSVExe.class.getName());

	  public static boolean wasExecuted=false;

	  public void execute(DelegateExecution execution) throws Exception {

		  ExecutionHelper Helper = new ExecutionHelper();
		  Scanner scanner = new Scanner(new File(Helper.getInput(execution).toString()));
	        ArrayList<String> output = new ArrayList<>();
	        while(scanner.hasNext()){
	        	String nextLine = scanner.nextLine();
	            output.add(nextLine);
	        }
	        scanner.close();
	        Helper.setOutput(execution, output);
	  }
}
