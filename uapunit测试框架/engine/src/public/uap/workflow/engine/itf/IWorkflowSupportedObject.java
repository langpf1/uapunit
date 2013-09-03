package uap.workflow.engine.itf;

import java.io.Serializable;

public interface IWorkflowSupportedObject extends Serializable {
	String getID();
	String getMDComponentName();
	IWorkflowSupportedObject getByID(String ID);
}
