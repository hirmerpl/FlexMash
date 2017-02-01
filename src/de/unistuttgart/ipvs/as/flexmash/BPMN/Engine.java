package de.unistuttgart.ipvs.as.flexmash.BPMN;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;


public class Engine {

	ProcessEngine processEngine ;
	public Engine(){
	
		this.processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
				  .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
				  .setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000").setProcessEngineName("inAppEngine")
				  .buildProcessEngine();
	}
	
	public Deployment deployProcessModel (String FileName, BPMNmodel Model){
		return processEngine.getRepositoryService().createDeployment().addModelInstance(FileName, Model.ModelInstance).deploy();
		
	}
	
	public ProcessInstance runProcessModel (String ProcessName, BPMNmodel Model){
		
		return processEngine.getRuntimeService().startProcessInstanceByKey(ProcessName, Model.variables );
	}
}
