package de.unistuttgart.ipvs.as.flexmash.BPMN;

import org.camunda.bpm.engine.delegate.DelegateExecution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.*;

public class ExecutionHelper {

	
	public List<String> getPredecessorNodes(DelegateExecution execution){
		
		 BpmnModelInstance instance = execution.getBpmnModelInstance();
		 ServiceTask thistask= instance.getModelElementById(execution.getCurrentActivityId());
		 List<String> Predecessors = new ArrayList<String>();
		 
		 for(Iterator<SequenceFlow> it = thistask.getIncoming().iterator();it.hasNext();){
			 SequenceFlow incoming = it.next();
			 Predecessors.add(incoming.getId());
		 }
		 return Predecessors;
	}
	
	public String getFilePath(){
		
		return  System.getenv("FLEXMASH")+"/files/";
	}
	
	public Object getInput(DelegateExecution execution){
		return execution.getVariable( execution.getCurrentActivityId()+"In" );
	}
	
	public String getURLPath(){
		
		return "http://localhost:8080/Data_Mashup/services/";
			
	}
}
