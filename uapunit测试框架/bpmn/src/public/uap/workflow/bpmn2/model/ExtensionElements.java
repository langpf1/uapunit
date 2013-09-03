package uap.workflow.bpmn2.model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExtensionElements")
public class ExtensionElements  implements Serializable{
	private static final long serialVersionUID = 826134627771436147L;
	@XmlElement(name = "participant",namespace=NameSpaceConst.BIZEX_URL)
	public List<DefaultParticipantDefinition> participants = new ArrayList<DefaultParticipantDefinition>();
	@XmlElement(name = "notice",namespace=NameSpaceConst.BIZEX_URL)
	public List<DefaultNoticeDefinition> notices = new ArrayList<DefaultNoticeDefinition>();
	@XmlElement(name = "taskHandling",namespace=NameSpaceConst.BIZEX_URL)
	public List<DefaultTaskHandlingDefinition> taskHandlings = new ArrayList<DefaultTaskHandlingDefinition>();
	@XmlElement(name = "taskListener", namespace=NameSpaceConst.BIZEX_URL)
	public List<TaskListener> taskListeners = new ArrayList<TaskListener>();
	@XmlElement(name = "executionListener", namespace=NameSpaceConst.BIZEX_URL)
	public List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();
	@XmlElement(namespace=NameSpaceConst.BIZEX_URL,name="userTaskPolicyControl")
	public List<UserTaskPolicyControl> userTaskPolicyControl = new ArrayList<UserTaskPolicyControl>(); 
	@XmlElement(name="customProperty",namespace=NameSpaceConst.BIZEX_URL)
	public List<CustomProperty> customProperties = new ArrayList<CustomProperty>();
	@XmlElement(namespace=NameSpaceConst.BIZEX_URL,name="organization")
	public String[] organization;
	@XmlElement(name="field",namespace=NameSpaceConst.BIZEX_URL)
	public List<FieldExtension> fieldExtensions = new ArrayList<FieldExtension>();
	@XmlElement(name="formProperty",namespace=NameSpaceConst.BIZEX_URL)
	public List<FormProperty> formProperties = new ArrayList<FormProperty>();
	@XmlElement(name="in", namespace=NameSpaceConst.BIZEX_URL)
	public List<DataAssociation> inParameters = new ArrayList<DataAssociation>();
	@XmlElement(name="out", namespace=NameSpaceConst.BIZEX_URL)
	public List<DataAssociation> outParameters = new ArrayList<DataAssociation>();



	public String[] getOrganization() {
		return organization;
	}
	public void setOrganization(String[] organization) {
		this.organization = organization;
	}
	public UserTaskPolicyControl getUserTaskPolicyControl() {
		if(userTaskPolicyControl.size()<1)
			return null;
		return userTaskPolicyControl.get(0);
	}
	public void setUserTaskPolicyControl(UserTaskPolicyControl userTaskPolicyControl) {
		this.userTaskPolicyControl.clear();
		this.userTaskPolicyControl.add(userTaskPolicyControl);
	}
	public List<CustomProperty> getCustomProperties() {
		return customProperties;
	}
	public void setUserTaskPolicyControl(List<UserTaskPolicyControl> userTaskPolicyControl) {
		this.userTaskPolicyControl = userTaskPolicyControl;
	}
	public List<TaskListener> getTaskListeners() {
		return taskListeners;
	}
	public void setTaskListeners(List<TaskListener> taskListeners) {
		this.taskListeners = taskListeners;
	}
	public List<ExecutionListener> getExecutionListeners() {
		return executionListeners;
	}
	public void setExecutionListeners(List<ExecutionListener> executionListeners) {
		this.executionListeners = executionListeners;
	}
	public List<DefaultParticipantDefinition> getParticipants() {
		return participants;
	}
	public void setParticipants(List<DefaultParticipantDefinition> participants) {
		this.participants = participants;
	}
	public List<DefaultTaskHandlingDefinition> getTaskHandlings() {
		return taskHandlings;
	}
	public void setTaskHandlings(List<DefaultTaskHandlingDefinition> taskHandlings) {
		this.taskHandlings = taskHandlings;
	}
	public List<DefaultNoticeDefinition> getNotices() {
		return notices;
	}
	public void setNotices(List<DefaultNoticeDefinition> notices) {
		this.notices = notices;
	}
	public void setCustomProperties(List<CustomProperty> customProperties) {
		this.customProperties = customProperties;
	}
	public List<FieldExtension> getFieldExtensions() {
		return fieldExtensions;
	}
	public void setFieldExtensions(List<FieldExtension> fieldExtensions) {
		this.fieldExtensions = fieldExtensions;
	}
	public List<FormProperty> getFormProperties() {
		return formProperties;
	}
	public void setFormProperties(List<FormProperty> formProperties) {
		this.formProperties = formProperties;
	}
	public List<DataAssociation> getInParameters() {
		return inParameters;
	}
	public void setInParameters(List<DataAssociation> inParameters) {
		this.inParameters = inParameters;
	}
	public List<DataAssociation> getOutParameters() {
		return outParameters;
	}
	public void setOutParameters(List<DataAssociation> outParameters) {
		this.outParameters = outParameters;
	}
}
