package de.unistuttgart.ipvs.as.flexmash.bpmn.executables;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.unistuttgart.ipvs.as.flexmash.utils.ExecutionHelper;
import de.unistuttgart.ipvs.as.flexmash.utils.serviceplatform.ServiceModel;

public class CSVFilterExe implements JavaDelegate {

	/**
	 * The method responsible for Executing the logic for this node type. Each
	 * call for the node generates a new instance of this method with the
	 * updated variables based on the execution context
	 * 
	 **/
	@SuppressWarnings("unchecked")
	public void execute(DelegateExecution execution) throws Exception {
		ExecutionHelper Helper = new ExecutionHelper();
		String filterInput = Helper.getInput(execution).toString();
		ArrayList<String> csvLines = new ArrayList<>();
		System.out.println(Helper.getInput(execution).toString());
		System.out.println(execution.getCurrentActivityName()+ "  " + execution.getCurrentActivityId());
		for (Iterator<String> predecessor = Helper.getPredecessors(execution)
				.iterator(); predecessor.hasNext();) {
			String predName = predecessor.next().toString();
			@SuppressWarnings("unchecked")
			ArrayList<String> tempInput = (ArrayList<String>) execution
					.getVariable(predName + "Out");
			tempInput.forEach((e) -> csvLines.add(e));
		}

		
		ArrayList<String> filteredOutput = new ArrayList<>();

		Map<String, Object> input = new HashMap<>();
		input.put("filterinput", filterInput);
		input.put("csvinput", csvLines);
		ServiceModel test = new ServiceModel(execution.getCurrentActivityName());
		System.out.println(Helper.getInput(execution).toString());
		System.out.println(execution.getCurrentActivityName()+ "  " + execution.getCurrentActivityId()+"  "+test.getAddress());
		Map<String, Object> result = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		result = mapper.readValue(Helper.sendInputToPlatform(input, new URL(test.getAddress())).toString(), HashMap.class);
		filteredOutput = (ArrayList<String>) result.get("output");
		System.out
				.println(String.format("Filtered output of the activity %1$s: ",
						execution.getCurrentActivityId()));
		filteredOutput.forEach((k) -> System.out.println(k));
		System.out.println(" \n");
		Helper.setOutput(execution, filteredOutput);

	}
}
