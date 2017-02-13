package de.unistuttgart.ipvs.as.flexmash.bpmn.executables;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import de.unistuttgart.ipvs.as.flexmash.utils.*;

public class CSVExe implements JavaDelegate {
	
	/**
	 * The method responsible for Executing the logic for this node type. Each
	 * call for the node generates a new instance of this method with the
	 * updated variables based on the execution context
	 * 
	 **/
	public void execute(DelegateExecution execution) throws Exception {

		ExecutionHelper Helper = new ExecutionHelper();
		Scanner scanner = new Scanner(
				new File(Helper.getInput(execution).toString()));
		ArrayList<String> output = new ArrayList<>();
		while (scanner.hasNext()) {
			String nextLine = scanner.nextLine();
			output.add(nextLine);
		}
		scanner.close();
		Helper.setOutput(execution, output);
	}
}
