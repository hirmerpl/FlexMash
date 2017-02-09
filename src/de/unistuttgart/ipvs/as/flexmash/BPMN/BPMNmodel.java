package de.unistuttgart.ipvs.as.flexmash.BPMN;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.model.bpmn.*;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.BpmnModelElementInstance;
import org.camunda.bpm.model.bpmn.instance.Definitions;
import org.camunda.bpm.model.bpmn.instance.FlowNode;
import org.camunda.bpm.model.bpmn.instance.Process;
import org.camunda.bpm.model.bpmn.instance.SequenceFlow;

public class BPMNmodel {

	public BpmnModelInstance ModelInstance;
	public Definitions Definitions;
	public Process MainProcess;
	public Map<String, Object> variables;
	public String fileName;

	public BPMNmodel(String MainProcessID) {
		this.ModelInstance = Bpmn.createExecutableProcess().done();
		this.Definitions = ModelInstance.newInstance(Definitions.class);
		this.Definitions.setTargetNamespace("http://camunda.org/examples");
		this.MainProcess = createMainProcess(MainProcessID);
		this.variables = new HashMap<String, Object>();
	}

	private <T extends BpmnModelElementInstance> Process createMainProcess(String id) {
		Process element = this.ModelInstance.newInstance(Process.class);
		element.setId(id);
		element.setExecutable(true);
		this.Definitions.addChildElement(element);
		this.ModelInstance.setDefinitions(this.Definitions);
		return element;
	}

	public <T extends BpmnModelElementInstance> T createElement(BpmnModelElementInstance parentElement, String id,
			Class<T> elementClass) {
		T element = this.ModelInstance.newInstance(elementClass);
		element.setAttributeValue("id", id, true);
		parentElement.addChildElement(element);
		this.ModelInstance.setDefinitions(this.Definitions);
		return element;
	}

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

	public void Validate() {
		Bpmn.validateModel(this.ModelInstance);
		this.ModelInstance.setDefinitions(this.Definitions);

	}
}
