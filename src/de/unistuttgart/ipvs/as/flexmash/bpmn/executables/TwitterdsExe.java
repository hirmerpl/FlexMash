package de.unistuttgart.ipvs.as.flexmash.bpmn.executables;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class TwitterdsExe implements JavaDelegate {

	/**
	 * The method responsible for Executing the logic for this node type. Each
	 * call for the node generates a new instance of this method with the
	 * updated variables based on the execution context
	 * 
	 **/
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println(
				"Executing process: " + execution.getCurrentActivityId()
						+ " at " + new Timestamp(System.currentTimeMillis()));
		System.out.println(String.format("Parents for the Activity %s are:",
				execution.getCurrentActivityId()));
		@SuppressWarnings("unchecked")
		ArrayList<String> predecessors = (ArrayList<String>) execution
				.getVariable(execution.getCurrentActivityId() + "Pre");
		predecessors.forEach((l) -> System.out.println(l));
	}
}
