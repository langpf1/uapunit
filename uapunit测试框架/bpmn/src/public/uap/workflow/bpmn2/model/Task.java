package uap.workflow.bpmn2.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.bpmn2.annotation.TypeChangeMonitor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Task")
public class Task extends Activity {
	private static final long serialVersionUID = -3996248598108439925L;
	@PropEditor("uap.workflow.modeler.editors.BpmnTaskTypeEditor")
	@TypeChangeMonitor("task")
	@XmlAttribute
	public int taskType;
	public int getTaskType() {
		return taskType;
	}
	public void setTaskType(int taskType) {
		this.taskType = taskType;
	}
}