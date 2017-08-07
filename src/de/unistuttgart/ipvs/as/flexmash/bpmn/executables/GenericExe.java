package de.unistuttgart.ipvs.as.flexmash.bpmn.executables;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.unistuttgart.ipvs.as.flexmash.utils.ExecutionHelper;
import de.unistuttgart.ipvs.as.flexmash.utils.serviceplatform.ServiceModel;

public class GenericExe implements JavaDelegate {
	/**
	 * The method responsible for Executing the logic for this node type. Each
	 * call for the node generates a new instance of this method with the
	 * updated variables based on the execution context
	 * 
	 **/
	@Override
	@SuppressWarnings("unchecked")
	public void execute(DelegateExecution execution) throws Exception {

		ExecutionHelper Helper = new ExecutionHelper();
		ServiceModel Service = new ServiceModel(
				execution.getCurrentActivityName());
		String userInput = Helper.getInput(execution).toString();
		Map<String, Object> input = new HashMap<>();
		if (!userInput.equals(null) && Service.getParameters().size()>1)
			input.put(Service.getParameters().get(0), userInput);
		int paramCount = Service.getParameters().size()>1?1:0;

		for (String string : Helper.getPredecessors(execution)) {
			String predName = string.toString();
			System.out.println("GETTING INPUT: " + predName + "Out "+Service.getParameters().size());
			ArrayList<String> tempInput = (ArrayList<String>) execution
					.getVariable(predName + "Out");

			System.out.println("PUTTING INPUT: "
					+ Service.getParameters().size());
			tempInput.forEach(k -> System.out.println(k));
			input.put(Service.getParameters().get(paramCount), tempInput);
			paramCount++;
		}

		ArrayList<String> receivedOutput = new ArrayList<>();

		System.out.println(Helper.getInput(execution).toString());
		System.out.println(execution.getCurrentActivityName() + "  "
				+ execution.getCurrentActivityId() + "  "
				+ Service.getAddress());
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> result = mapper.readValue(
				Helper.sendInputToPlatform(input, new URL(Service.getAddress()))
				.toString(),
				HashMap.class);
		receivedOutput = (ArrayList<String>) result.get("output");
		Helper.setOutput(execution, receivedOutput);
		System.out.println(input.keySet());
		System.out.println("Output: ");
		receivedOutput.forEach((k) -> System.out.println(k));

	}
}
