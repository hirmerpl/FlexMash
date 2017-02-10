package de.unistuttgart.ipvs.as.flexmash.BPMN;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class MergeExe implements JavaDelegate {
	private final Logger LOGGER = Logger.getLogger(MergeExe.class.getName());

	public static boolean wasExecuted = false;

	public void execute(DelegateExecution execution) throws Exception {

		ExecutionHelper Helper = new ExecutionHelper();
		ArrayList<String> csvLines = new ArrayList<>();
		for (Iterator<String> predecessor = Helper.getPredecessors(execution).iterator(); predecessor.hasNext();) {
			String predName = predecessor.next().toString();
			ArrayList<String> tempInput = (ArrayList<String>) execution.getVariable(predName + "Out");
			tempInput.forEach((e) -> csvLines.add(e));

		}

		System.out.println("Merged output: ");
		csvLines.forEach((k) -> System.out.println(k));

		Helper.setOutput(execution, csvLines);

	}
}
