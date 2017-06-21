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

public class MergeExe implements JavaDelegate {
	/**
	 * The method responsible for Executing the logic for this node type. Each
	 * call for the node generates a new instance of this method with the
	 * updated variables based on the execution context
	 * 
	 **/
	public void execute(DelegateExecution execution) throws Exception {

		ExecutionHelper Helper = new ExecutionHelper();
		String mergeCriteria = Helper.getInput(execution).toString();
		Map<String, Object> input = new HashMap<>();
		input.put("mergeCriteria", mergeCriteria);
		ArrayList<String> csvLines = new ArrayList<>();
		int count = 1;
		for (Iterator<String> predecessor = Helper.getPredecessors(execution)
				.iterator(); predecessor.hasNext();) {
			String predName = predecessor.next().toString();
			@SuppressWarnings("unchecked")
			ArrayList<String> tempInput = (ArrayList<String>) execution
					.getVariable(predName + "Out");
			input.put("mergeInput"+count, tempInput);
			count++;
		}

		ArrayList<String> filteredOutput = new ArrayList<>();

		ServiceModel test = new ServiceModel(execution.getCurrentActivityName());
		System.out.println(Helper.getInput(execution).toString());
		System.out.println(execution.getCurrentActivityName()+ "  " + execution.getCurrentActivityId()+"  "+test.getAddress());
		Map<String, Object> result = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		result = mapper.readValue(Helper.sendInputToPlatform(input, new URL(test.getAddress())).toString(), HashMap.class);
		filteredOutput = (ArrayList<String>) result.get("output");

		Helper.setOutput(execution, filteredOutput);
		System.out.println(input.keySet());
		System.out.println("Merged output: ");
		filteredOutput.forEach((k) -> System.out.println(k));

		Helper.setOutput(execution, csvLines);

	}
}
