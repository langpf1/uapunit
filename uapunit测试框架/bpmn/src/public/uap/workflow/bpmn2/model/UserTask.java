package uap.workflow.bpmn2.model;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;
import uap.workflow.modeler.utils.CreateElementUtils;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserTask")
public class UserTask extends Task {
	private static final long serialVersionUID = 826134627771436147L;
	@XmlAttribute
	public String assignee = "";
	@XmlAttribute
	public Integer priority;
	@XmlAttribute(name="formKey", namespace=NameSpaceConst.BIZEX_URL)
	public String formKey;
	@XmlAttribute
	public String dueDate;
	@XmlTransient
	public List<String> candidateUsers = new ArrayList<String>();
	@XmlTransient
	public List<String> candidateGroups = new ArrayList<String>();
	@PropEditor("uap.workflow.modeler.editors.FormPropertyEditor")
	@XmlTransient
	public List<FormProperty> formProperties = new ArrayList<FormProperty>();
	@uap.workflow.bpmn2.annotation.PropEditor1(value="uap.workflow.modeler.editors.BpmnListenerEditor",type=TaskListener.class)
	@XmlTransient
	public List<TaskListener> taskListeners = new ArrayList<TaskListener>();
	@PropEditor("uap.workflow.ui.participant.editors.ParticipantPropertyEditor")
	@XmlTransient
	public List<DefaultParticipantDefinition> participants = new ArrayList<DefaultParticipantDefinition>();
	@PropEditor("uap.workflow.ui.notice.editors.NoticePropertyEditor")
	@XmlTransient
	public List<DefaultNoticeDefinition> notices = new ArrayList<DefaultNoticeDefinition>();
	@PropEditor("uap.workflow.ui.taskhandling.editors.TaskHandlingPropertyEditor")
	@XmlTransient
	public List<DefaultTaskHandlingDefinition> taskHandlings = new ArrayList<DefaultTaskHandlingDefinition>();
	@XmlAttribute(name="makeBill")
	public boolean makeBill;
	@XmlAttribute(name="openUIStyle"/*, namespace=NameSpaceConst.BIZEX_URL*/)
	@PropEditor("uap.workflow.modeler.editors.OpenUIStyleEditor")
	public String openUIStyle="BisunessUI";
	@XmlAttribute(name="openUIStyle"/*, namespace=NameSpaceConst.BIZEX_URL*/)
	public String openURI;
	@XmlAttribute(name="moduleCode", namespace=NameSpaceConst.BIZEX_URL)
	public String moduleCode;
	@XmlAttribute(name="billOrTranstypeID", namespace=NameSpaceConst.BIZEX_URL)
	public String billOrTranstypeID;
	@PropEditor("uap.workflow.modeler.editors.BpmnBizActionEditor")
	@XmlAttribute(name="billActionID", namespace=NameSpaceConst.BIZEX_URL)
	public String billActionID;
	@XmlAttribute(name="afterSign")
	public boolean afterSign = false;
	@XmlAttribute(name="sequence")
	public boolean sequence = false;
	
	public boolean isSequence() {
		return sequence;
	}
	public void setSequence(boolean sequence) {
		this.sequence = sequence;
	}
	public boolean isAfterSign() {
		return afterSign;
	}
	public void setAfterSign(boolean afterSign) {
		this.afterSign = afterSign;
	}

	@XmlTransient
	public UserTaskPolicyControl userTaskPolicyControl = new UserTaskPolicyControl(); 
	
	public UserTaskPolicyControl getUserTaskPolicyControl() {
		return userTaskPolicyControl;
	}
	public void setUserTaskPolicyControl(UserTaskPolicyControl userTaskPolicyControl) {
		this.userTaskPolicyControl = userTaskPolicyControl;
	}
	public UserTask(){
	}
	public String getPk_billOrTranstype() {
		return billOrTranstypeID;
	}
	public void setPk_billOrTranstype(String pk_billOrTranstype) {
		this.billOrTranstypeID = pk_billOrTranstype;
	}

	public String getModuleCode() {
		return moduleCode;
	}
	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}
	public String getBillOrTranstypeID() {
		return billOrTranstypeID;
	}
	public void setBillOrTranstypeID(String billOrTranstypeID) {
		this.billOrTranstypeID = billOrTranstypeID;
	}
	public String getBillActionID() {
		return billActionID;
	}
	public void setBillActionID(String billActionID) {
		this.billActionID = billActionID;
	}
	public String getAssignee() {
		return assignee;
	}
	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public String getFormKey() {
		return formKey;
	}
	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}
	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public List<String> getCandidateUsers() {
		return candidateUsers;
	}
	public void setCandidateUsers(List<String> candidateUsers) {
		this.candidateUsers = candidateUsers;
	}
	public List<String> getCandidateGroups() {
		return candidateGroups;
	}
	public void setCandidateGroups(List<String> candidateGroups) {
		this.candidateGroups = candidateGroups;
	}
	public List<FormProperty> getFormProperties() {
		return this.formProperties;
	}
	public void setFormProperties(List<FormProperty> formProperties) {
		this.formProperties = formProperties;
	}
	public List<TaskListener> getTaskListeners() {
		return this.taskListeners;
		
	}
	public void setTaskListeners(List<TaskListener> taskListeners) {
		this.taskListeners = taskListeners;
	}
	@Override
	public String provideBeanInfoClass() {
		return "uap.workflow.modeler.bpmn2.beaninfos.UserTaskBeanInfo";
	}
	public int getTaskType() {
		return 0;
	}

	public List<DefaultParticipantDefinition> getParticipants() {
		return participants;
	}
	public void setParticipants(List<DefaultParticipantDefinition> participants) {
		this.participants = participants;
	}
	public List<DefaultNoticeDefinition> getNotices() {
		return notices;
	}
	public void setNotices(List<DefaultNoticeDefinition> notices) {
		this.notices = notices;
	}
	public List<DefaultTaskHandlingDefinition> getTaskHandlings() {
		return taskHandlings;
	}
	public void setTaskHandlings(List<DefaultTaskHandlingDefinition> taskHandlings) {
		this.taskHandlings = taskHandlings;
	}
	public String getOpenUIStyle() {
		return openUIStyle;
	}
	public void setOpenUIStyle(String openUIStyle) {
		this.openUIStyle = openUIStyle;
	}
	public String getOpenURI() {
		return openURI;
	}
	public void setOpenURI(String openURI) {
		this.openURI = openURI;
	}
	public boolean isMakeBill() {
		return makeBill;
	}
	public void setMakeBill(boolean makeBill) {
		this.makeBill = makeBill;
	}

	
	@Override
	public Object replicate() {
		try {
			BaseElement cloneObj = (BaseElement) Class.forName(getClass().getName()).newInstance();
			Method[] methods = getClass().getMethods();
			for (Method setMethod : methods) {
				if (setMethod.getName().startsWith("set")) {
					if (setMethod.getParameterTypes()[0].equals(List.class) || 
						setMethod.getParameterTypes()[0].equals(UserTaskPolicyControl.class)) 
						continue;
					Object value = null;
					// id重新生成
					if (setMethod.getName().equals("setId")) {
						if(cloneObj instanceof Process){
							value = java.util.UUID.randomUUID().toString();
						}else{
							value = new CreateElementUtils().generateElementId(cloneObj);
						}
					} else {
						String getMethodName = "get" + setMethod.getName().substring(3, setMethod.getName().length());
						if (setMethod.getParameterTypes()[0].getName().equals("boolean")) {
							getMethodName = "is" + setMethod.getName().substring(3, setMethod.getName().length());
						}
						for (Method getMethod : methods) {
							if (getMethod.getName().equals(getMethodName)) {
								value = getMethod.invoke(this, new Object[] {});
								break;
							}
						}
					}
					setMethod.invoke(cloneObj, value);
				}
			}
			return cloneObj;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void marshal() {
		super.marshal();
		this.extensionElements = new ExtensionElements(); 
		this.extensionElements.setExecutionListeners(getExecutionListeners());
		this.extensionElements.setTaskListeners(taskListeners); 
		this.extensionElements.setNotices(this.notices);
		this.extensionElements.setParticipants(this.participants);
		this.extensionElements.setTaskHandlings(this.taskHandlings);
		this.extensionElements.setUserTaskPolicyControl(this.userTaskPolicyControl);
		this.extensionElements.getUserTaskPolicyControl().setCollaborationParticipants(this.userTaskPolicyControl.getCollaborationParticipants());
		this.extensionElements.setFormProperties(this.formProperties);
	}

	public void unmarshal() {
		super.unmarshal();
		this.setTaskListeners(this.extensionElements.getTaskListeners());
		this.setNotices(this.extensionElements.getNotices());
		this.setParticipants(this.extensionElements.getParticipants());
		this.setTaskHandlings(this.extensionElements.getTaskHandlings());
		this.setUserTaskPolicyControl(this.extensionElements.getUserTaskPolicyControl());
		if(this.extensionElements.getUserTaskPolicyControl() != null)
		{
			this.getUserTaskPolicyControl().setCollaborationParticipants(this.extensionElements.getUserTaskPolicyControl().getCollaborationParticipants());
		}
		this.setFormProperties(this.extensionElements.getFormProperties());
	}
}
