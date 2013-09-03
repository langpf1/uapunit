package uap.workflow.modeler.bpmn2.beaninfos;

import uap.workflow.bpmn2.model.UserTask;
import uap.workflow.modeler.utils.BpmnModelerConstants;
public class UserTaskBeanInfo extends TaskBeanInfo {

	public UserTaskBeanInfo(){
		super(UserTask.class);
//		addProperty("assignee").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
//		addProperty("candidateUsers").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
//		addProperty("candidateGroups").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("dueDate").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("priority").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("billActionID").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("makeBill").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("formKey").setCategory(BpmnModelerConstants.CATEGORY_FORM);
		addProperty("formProperties").setCategory(BpmnModelerConstants.CATEGORY_FORM);
		addProperty("taskListeners").setCategory(BpmnModelerConstants.CATEGORY_LISTENERS);
		addProperty("participants").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("notices").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("taskHandlings").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("openUIStyle").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("openURI").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);

		String catagory = "Policy Control";
		addProperty("userTaskPolicyControl.approve", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.deliver", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.undertake", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.processClass", UserTask.class,true).setCategory(catagory);
		//addProperty("userTaskPolicyControl.form", UserTask.class,true).setCategory(catagory);
		//addProperty("userTaskPolicyControl.modifyResources", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canAddSign", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canDelegate", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canTransfer", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canDeliver", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canAssign", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.opinionEditable", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.opinionNullable", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canHasten", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canPrint", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canRecycle", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canPassthrough", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canUploadAttachment", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canDownloadAttachment", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canDeleteAttachment", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canModifyAttachment", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canViewAttachment", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.collaborationParticipants", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.canReject", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.rejectPolicy", UserTask.class,true).setCategory(catagory);
		addProperty("userTaskPolicyControl.activityRef", UserTask.class,true).setCategory(catagory);

	}
}
