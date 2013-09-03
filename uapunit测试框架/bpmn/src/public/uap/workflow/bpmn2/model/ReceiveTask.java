package uap.workflow.bpmn2.model;

public class ReceiveTask extends Task
{
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ReceiveTaskBeanInfo";
	}
	
	public int getTaskType() {
		return 5;
	}
}

/* Location:           E:\dev tools\eclipse-SDK-3.7.2-win32\eclipse\plugins\org.activiti.designer.model_5.9.1.jar
 * Qualified Name:     org.activiti.designer.bpmn2.model.ReceiveTask
 * JD-Core Version:    0.5.4
 */