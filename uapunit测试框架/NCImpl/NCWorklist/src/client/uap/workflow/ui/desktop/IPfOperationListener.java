package uap.workflow.ui.desktop;

import java.util.EventListener;


/**
 * 
 */
public interface IPfOperationListener extends EventListener {
	
	public void operationPerformed(PfOperationEvent event);
	
}
