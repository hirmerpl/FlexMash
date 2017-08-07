package de.unistuttgart.ipvs.as.flexmash.servlet.engine;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import de.unistuttgart.ipvs.as.flexmash.bpmn.BPMNmodel;

/**
 * @author KMahrous
 *
 */
public class Engine {

	/**
	 * The process engine itself
	 */
	ProcessEngine processEngine;

	/**
	 * Constructor for the process engine
	 */
	public Engine() {

		this.processEngine = ProcessEngineConfiguration
				.createStandaloneInMemProcessEngineConfiguration()
				.setDatabaseSchemaUpdate(
						ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE)
				.setJdbcUrl("jdbc:h2:mem:my-own-db;DB_CLOSE_DELAY=1000")
				.setProcessEngineName("inAppEngine").buildProcessEngine();
	}

	/**
	 * Method to deploy a BPMN model with the generated BPMN file within an
	 * engine instance
	 * 
	 * @param FileName
	 * @param Model
	 * @return
	 */
	public Deployment deployProcessModel(String FileName, BPMNmodel Model) {
		return this.processEngine.getRepositoryService().createDeployment()
				.addModelInstance(FileName, Model.ModelInstance).deploy();

	}

	/**
	 * Method to run a BPMN model with the generated BPMN file within an engine
	 * instance
	 * 
	 * @param ProcessName
	 * @param Model
	 * @return
	 */
	public ProcessInstance runProcessModel(String ProcessName,
			BPMNmodel Model) {
		
		return this.processEngine.getRuntimeService()
				.startProcessInstanceByKey(ProcessName, Model.variables);
	}

public void close(){
	this.processEngine.close();
}
}
