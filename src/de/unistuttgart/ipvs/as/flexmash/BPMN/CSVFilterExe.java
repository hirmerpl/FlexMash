package de.unistuttgart.ipvs.as.flexmash.BPMN;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class CSVFilterExe implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(CSVFilterExe.class.getName());

	public static boolean wasExecuted = false;

	public void execute(DelegateExecution execution) throws Exception {
		ExecutionHelper Helper = new ExecutionHelper();
		String filterInput = Helper.getInput(execution).toString();
		ArrayList<String> csvLines = new ArrayList<>();
		for (Iterator<String> predecessor = Helper.getPredecessors(execution).iterator(); predecessor.hasNext();) {
			String predName = predecessor.next().toString();
			ArrayList<String> tempInput = (ArrayList<String>) execution.getVariable(predName + "Out");
			tempInput.forEach((e) -> csvLines.add(e));

		}

		String[] keywords = filterInput.split(",");
		ArrayList<String> filteredOutput = new ArrayList<>();

		for (Iterator<String> it = csvLines.iterator(); it.hasNext();) {
			String tempInput = it.next();
			for (int i = 0; i < keywords.length; i++) {

				if (tempInput.toLowerCase().contains(keywords[i].toLowerCase()))
					filteredOutput.add(tempInput);
			}
		}
		System.out.println(String.format("Filtered output of the activity %s: ", execution.getCurrentActivityId()));
		filteredOutput.forEach((k) -> System.out.println(k));
		System.out.println(" \n");
		Helper.setOutput(execution, filteredOutput);

	}
}
