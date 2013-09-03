package uap.workflow.bpmn2.model;

public class ManualTask extends Task
{
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.ManualTaskBeanInfo";
	}
	public int getTaskType() {
		return 4;
	}
}