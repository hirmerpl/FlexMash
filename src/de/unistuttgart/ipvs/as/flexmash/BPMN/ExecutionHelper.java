package de.unistuttgart.ipvs.as.flexmash.BPMN;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.impl.instance.FlowNodeImpl;
import org.camunda.bpm.model.bpmn.impl.instance.To;
import org.camunda.bpm.model.bpmn.instance.*;
import org.camunda.bpm.model.xml.Model;
import org.camunda.bpm.model.xml.ModelInstance;
import de.unistuttgart.ipvs.as.flexmash.transformation.MashupPlantoBPMNConverter;

public class ExecutionHelper {

	
	public String getFilePath() {

		return System.getenv("FLEXMASH") + "/files/";
	}

	public Object getInput(DelegateExecution execution) {
		return execution.getVariable(execution.getCurrentActivityId() + "In");
	}
	
	public void setOutput(DelegateExecution execution, Object output){
		
		execution.setVariable(execution.getCurrentActivityId() + "Out", output);
	}

	public String getURLPath() {
		return "http://localhost:8080/Data_Mashup/services/";
	}

	public void addValues(Map<String, ArrayList<String>> hashMap, String key, String value) {
		ArrayList<String> tempList = null;
		if (hashMap.containsKey(key)) {
			tempList = (ArrayList<String>) hashMap.get(key);
			if (tempList == null)
				tempList = new ArrayList<String>();
			tempList.add(value);
		} else {
			tempList = new ArrayList<String>();
			tempList.add(value);
		}
		hashMap.put(key, tempList);
	}

	public ArrayList<String> getPredecessors (DelegateExecution execution){

		return (ArrayList<String>) execution.getVariable(execution.getCurrentActivityId() + "Pre");
	}
}
