package uap.workflow.bpmn2.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.app.taskhandling.ITaskHandlingDefinition;
import uap.workflow.app.taskhandling.ITaskHandlingType;

/** 
   通知模型实现类
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultTaskHandling")
public class DefaultTaskHandlingDefinition implements Serializable,ITaskHandlingDefinition{

	private static final long serialVersionUID = -241200422030363886L;
	@XmlElement
	DefaultTaskHandlingType taskHandleType;

	public DefaultTaskHandlingType getTaskHandleType() {
		return taskHandleType;
	}

	public void setTaskHandleType(DefaultTaskHandlingType taskHandleType) {
		this.taskHandleType = taskHandleType;
	}

	public void setTaskHandleType(ITaskHandlingType taskHandleType) {
		this.taskHandleType = (DefaultTaskHandlingType)taskHandleType;
	}
}