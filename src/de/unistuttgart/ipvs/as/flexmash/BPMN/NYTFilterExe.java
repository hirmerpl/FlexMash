package de.unistuttgart.ipvs.as.flexmash.BPMN;

import java.util.logging.Logger;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class NYTFilterExe implements JavaDelegate {
	 private final Logger LOGGER = Logger.getLogger(NYTFilterExe.class.getName());

	  public static boolean wasExecuted=false;

	  public void execute(DelegateExecution execution) throws Exception {


	  }
}
