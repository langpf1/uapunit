package uap.workflow.bpmn2.model;


public class Group extends Artifact {

	private static final long serialVersionUID = -5779273776109656703L;
	public Group()
	{
		this.name="Group";
	}
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.GroupBeanInfo";
	}
}
