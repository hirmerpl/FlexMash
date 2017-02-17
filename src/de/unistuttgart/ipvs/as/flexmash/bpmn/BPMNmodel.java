package de.unistuttgart.ipvs.as.flexmash.bpmn;

import java.util.HashMap;
import java.util.Map;
import org.camunda.bpm.model.bpmn.*;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

public class BPMNmodel {

	/**
	 * the model instance holding the elements created for the BPMN model 
	 */
	public BpmnModelInstance ModelInstance;
	/**
	 * the Definitions of the model instance holding all the elements and the 
	 * transitions created
	 */
	public Definitions Definitions;
	/**
	 * The main process of the BPMN model that contains all the mapped transitions 
	 * and processes 
	 * 
	 */
	public Process MainProcess;
	/**
	 * List to hold all input and output variable for each process within the 
	 * BPMN model itself
	 */
	public Map<String, Object> variables;
	/**
	 * the name of the BPMN created file for the BPMN model 
	 */
	public String fileName;

	/**
	 * Constructor for the BPMN model that takes the name of the model as an input and creates
	 * the executable model instance and it's definitions and initializes the main process and 
	 * list of variables 
	 * 
	 * @param MainProcessID
	 */
	public BPMNmodel(String MainProcessID) {
		this.ModelInstance = Bpmn.createExecutableProcess().done();
		this.Definitions = ModelInstance.newInstance(Definitions.class);
		this.Definitions.setTargetNamespace("http://camunda.org/examples");
		this.MainProcess = createMainProcess(MainProcessID);
		this.variables = new HashMap<String, Object>();
	}

	/**
	 * Method to create the main process that holds the entire mapped processes and 
	 * should only be used by this class
	 * 
	 * @param id for the main process 
	 * @return process of type Process
	 */
	private <T extends BpmnModelElementInstance> Process createMainProcess(String id) {
		Process element = this.ModelInstance.newInstance(Process.class);
		element.setId(id);
		element.setExecutable(true);
		this.Definitions.addChildElement(element);
		this.ModelInstance.setDefinitions(this.Definitions);
		return element;
	}

	/**
	 * 
	 * General method for creating different types of elements within the main process created by 
	 * the BPMN model
	 * 
	 * @param parentElement
	 * @param id
	 * @param elementClass
	 * 
	 * @return the element created
	 */
	public <T extends BpmnModelElementInstance> T createElement(BpmnModelElementInstance parentElement, String id,
			Class<T> elementClass) {
		T element = this.ModelInstance.newInstance(elementClass);
		element.setAttributeValue("id", id, true);
		parentElement.addChildElement(element);
		this.ModelInstance.setDefinitions(this.Definitions);
		return element;
	}

	/**
	 * Method to create the sequence flows (transitions) between the different nodes contained within the 
	 * model based on how the user models the mashup
	 * 
	 * @param process
	 * @param from
	 * @param to
	 * @return result sequence flow
	 */
	public SequenceFlow createSequenceFlow(Process process, FlowNode from, FlowNode to) {
		String identifier = from.getId() + "-" + to.getId();
		SequenceFlow sequenceFlow = createElement(process, identifier, SequenceFlow.class);
		process.addChildElement(sequenceFlow);
		sequenceFlow.setSource(from);
		from.getOutgoing().add(sequenceFlow);
		sequenceFlow.setTarget(to);
		to.getIncoming().add(sequenceFlow);
		return sequenceFlow;
	}

	/**
	 * A method to validate the final BPMN model before deploying or running it
	 * 
	 */
	public void Validate() {
		Bpmn.validateModel(this.ModelInstance);
		this.ModelInstance.setDefinitions(this.Definitions);

	}
}
