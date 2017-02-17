package de.unistuttgart.ipvs.as.flexmash.bpmn.executables;

import java.util.ArrayList;
import java.util.Iterator;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import de.unistuttgart.ipvs.as.flexmash.utils.ExecutionHelper;

public class MergeExe implements JavaDelegate {
	/**
	 * The method responsible for Executing the logic for this node type. Each
	 * call for the node generates a new instance of this method with the
	 * updated variables based on the execution context
	 * 
	 **/
	public void execute(DelegateExecution execution) throws Exception {

		ExecutionHelper Helper = new ExecutionHelper();
		ArrayList<String> csvLines = new ArrayList<>();
		for (Iterator<String> predecessor = Helper.getPredecessors(execution)
				.iterator(); predecessor.hasNext();) {
			String predName = predecessor.next().toString();
			@SuppressWarnings("unchecked")
			ArrayList<String> tempInput = (ArrayList<String>) execution
					.getVariable(predName + "Out");
			tempInput.forEach((e) -> csvLines.add(e));

		}

		System.out.println("Merged output: ");
		csvLines.forEach((k) -> System.out.println(k));

		Helper.setOutput(execution, csvLines);

	}
}
