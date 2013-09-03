/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.task;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import uap.workflow.app.notice.INoticeDefinition;
import uap.workflow.app.participant.IParticipant;
import uap.workflow.app.taskhandling.ITaskHandlingDefinition;
import uap.workflow.engine.core.ITaskListener;
import uap.workflow.engine.delegate.Expression;
import uap.workflow.engine.form.TaskFormHandler;
/**
 * Container for task definition information gathered at parsing time.
 * 
 * @author Joram Barrez
 */
public class TaskDefinition implements Serializable {
	protected String key;
	// assignment fields
	protected Expression nameExpression;
	protected Expression descriptionExpression;
	protected Expression assigneeExpression;
	protected Set<Expression> candidateUserIdExpressions = new HashSet<Expression>();
	protected Set<Expression> candidateGroupIdExpressions = new HashSet<Expression>();
	protected Expression dueDateExpression;
	protected Expression priorityExpression;
	protected List<IParticipant> participants;
	protected List<INoticeDefinition> notices;
	protected List<ITaskHandlingDefinition> taskHandlings;
	protected String openUIStyle;
	protected String openURI;
	protected boolean makeBill = false;
	
	protected boolean sequence = false;
	protected boolean isAssign;
	protected boolean isAfterSign;
	protected boolean isAddSign;
	protected boolean isDelegate;
	
	protected boolean approve;			//������ʹ�ö������
	protected boolean deliver;			//���ģ�ʹ�ù̶��������߼�
	protected boolean undertake;		//�а죬
	//protected String processClass;	//������
	//protected String form;			//��
	protected void modifyResources(){}	//�޸������Ի�����Դ��Ϣ
	//protected boolean canAddSign;		//�����ǩ
	//protected boolean canDelegate;		//�������
	protected boolean canTransfer;		//��ת��
	protected boolean canDeliver;		//�ɴ���
	//protected boolean canAssign;		//����һ��ָ��
	protected boolean opinionEditable;	//�ɱ༭���
	protected boolean opinionNullable;	//�Ƿ�����ɿ�
	protected boolean canHasten;		//����߰�
	protected boolean canPrint;			//�����ӡ
	protected boolean canRecycle;		//�����ջ�
	protected boolean canPassthrough; 	//�������ͨ��
	protected boolean canUploadAttachment;	//�������ϴ�
	protected boolean canDownloadAttachment;//����������
	protected boolean canDeleteAttachment;	//������ɾ��
	protected boolean canModifyAttachment;	//�������޸�
	protected boolean canViewAttachment;	//�������鿴
	protected List<IParticipant> collaborationParticipants;
	protected Object voucherPrivilege;
	protected boolean canReject; 		//��ֹ���ˣ��������
	protected String rejectPolicy; 		//��һ�����Ƶ��ˣ�ȫ�����ָ���
	protected String activityRef;
	
	
	public boolean isSequence() {
		return sequence;
	}
	public void setSequence(boolean isSequence) {
		this.sequence = isSequence;
	}
	
	public boolean isAddSign() {
		return isAddSign;
	}
	public void setAddSign(boolean isAddSign) {
		this.isAddSign = isAddSign;
	}
	
	public boolean isDelegate() {
		return isDelegate;
	}
	public void setDelegate(boolean isDelegate) {
		this.isDelegate = isDelegate;
	}
	public boolean isAssign() {
		return isAssign;
	}
	public void setAssign(boolean isAssign) {
		this.isAssign = isAssign;
	}
	public boolean isAfterSign() {
		return isAfterSign;
	}
	public void setAfterSign(boolean isAfterSign) {
		this.isAfterSign = isAfterSign;
	}
	
	// form fields
	protected TaskFormHandler taskFormHandler;
	// task listeners
	protected Map<String, List<ITaskListener>> taskListeners = new HashMap<String, List<ITaskListener>>();
	public TaskDefinition(TaskFormHandler taskFormHandler) {
		this.taskFormHandler = taskFormHandler;
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public Expression getNameExpression() {
		return nameExpression;
	}
	public void setNameExpression(Expression nameExpression) {
		this.nameExpression = nameExpression;
	}
	public Expression getDescriptionExpression() {
		return descriptionExpression;
	}
	public void setDescriptionExpression(Expression descriptionExpression) {
		this.descriptionExpression = descriptionExpression;
	}
	public Expression getAssigneeExpression() {
		return assigneeExpression;
	}
	public void setAssigneeExpression(Expression assigneeExpression) {
		this.assigneeExpression = assigneeExpression;
	}
	public Set<Expression> getCandidateUserIdExpressions() {
		return candidateUserIdExpressions;
	}
	public void addCandidateUserIdExpression(Expression userId) {
		candidateUserIdExpressions.add(userId);
	}
	public Set<Expression> getCandidateGroupIdExpressions() {
		return candidateGroupIdExpressions;
	}
	public void addCandidateGroupIdExpression(Expression groupId) {
		candidateGroupIdExpressions.add(groupId);
	}
	public Expression getPriorityExpression() {
		return priorityExpression;
	}
	public void setPriorityExpression(Expression priorityExpression) {
		this.priorityExpression = priorityExpression;
	}
	public TaskFormHandler getTaskFormHandler() {
		return taskFormHandler;
	}
	public void setTaskFormHandler(TaskFormHandler taskFormHandler) {
		this.taskFormHandler = taskFormHandler;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Expression getDueDateExpression() {
		return dueDateExpression;
	}
	public void setDueDateExpression(Expression dueDateExpression) {
		this.dueDateExpression = dueDateExpression;
	}
	public Map<String, List<ITaskListener>> getTaskListeners() {
		return taskListeners;
	}
	public void setTaskListeners(Map<String, List<ITaskListener>> taskListeners) {
		this.taskListeners = taskListeners;
	}
	public List<ITaskListener> getTaskListener(String eventName) {
		return taskListeners.get(eventName);
	}
	public void addTaskListener(String eventName, ITaskListener taskListener) {
		List<ITaskListener> taskEventListeners = taskListeners.get(eventName);
		if (taskEventListeners == null) {
			taskEventListeners = new ArrayList<ITaskListener>();
			taskListeners.put(eventName, taskEventListeners);
		}
		taskEventListeners.add(taskListener);
	}
	
	public List<IParticipant> getParticipants() {
		if (participants == null) {
			participants = new ArrayList<IParticipant>();
		}
		return participants;
	}
	
	public void setParticipants(List<IParticipant> participants) {
		this.participants = participants;
	}
	
	public List<INoticeDefinition> getNotices() {
		if (notices == null) {
			notices = new ArrayList<INoticeDefinition>();
		}
		return notices;
	}
	
	public void setNotices(List<INoticeDefinition> notices) {
		this.notices = notices;
	}
	
	public List<ITaskHandlingDefinition> getTaskHandlings() {
		if (taskHandlings == null) {
			taskHandlings = new ArrayList<ITaskHandlingDefinition>();
		}
		return taskHandlings;
	}
	
	public void setTaskHandlings(List<ITaskHandlingDefinition> taskHandlings) {
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
	public boolean isApprove() {
		return approve;
	}
	public void setApprove(boolean approve) {
		this.approve = approve;
	}
	public boolean isDeliver() {
		return deliver;
	}
	public void setDeliver(boolean deliver) {
		this.deliver = deliver;
	}
	public boolean isUndertake() {
		return undertake;
	}
	public void setUndertake(boolean undertake) {
		this.undertake = undertake;
	}
	public boolean isCanTransfer() {
		return canTransfer;
	}
	public void setCanTransfer(boolean canTransfer) {
		this.canTransfer = canTransfer;
	}
	public boolean isCanDeliver() {
		return canDeliver;
	}
	public void setCanDeliver(boolean canDeliver) {
		this.canDeliver = canDeliver;
	}
	public boolean isOpinionEditable() {
		return opinionEditable;
	}
	public void setOpinionEditable(boolean opinionEditable) {
		this.opinionEditable = opinionEditable;
	}
	public boolean isOpinionNullable() {
		return opinionNullable;
	}
	public void setOpinionNullable(boolean opinionNullable) {
		this.opinionNullable = opinionNullable;
	}
	public boolean isCanHasten() {
		return canHasten;
	}
	public void setCanHasten(boolean canHasten) {
		this.canHasten = canHasten;
	}
	public boolean isCanPrint() {
		return canPrint;
	}
	public void setCanPrint(boolean canPrint) {
		this.canPrint = canPrint;
	}
	public boolean isCanRecycle() {
		return canRecycle;
	}
	public void setCanRecycle(boolean canRecycle) {
		this.canRecycle = canRecycle;
	}
	public boolean isCanPassthrough() {
		return canPassthrough;
	}
	public void setCanPassthrough(boolean canPassthrough) {
		this.canPassthrough = canPassthrough;
	}
	public boolean isCanUploadAttachment() {
		return canUploadAttachment;
	}
	public void setCanUploadAttachment(boolean canUploadAttachment) {
		this.canUploadAttachment = canUploadAttachment;
	}
	public boolean isCanDownloadAttachment() {
		return canDownloadAttachment;
	}
	public void setCanDownloadAttachment(boolean canDownloadAttachment) {
		this.canDownloadAttachment = canDownloadAttachment;
	}
	public boolean isCanDeleteAttachment() {
		return canDeleteAttachment;
	}
	public void setCanDeleteAttachment(boolean canDeleteAttachment) {
		this.canDeleteAttachment = canDeleteAttachment;
	}
	public boolean isCanModifyAttachment() {
		return canModifyAttachment;
	}
	public void setCanModifyAttachment(boolean canModifyAttachment) {
		this.canModifyAttachment = canModifyAttachment;
	}
	public boolean isCanViewAttachment() {
		return canViewAttachment;
	}
	public void setCanViewAttachment(boolean canViewAttachment) {
		this.canViewAttachment = canViewAttachment;
	}
	public List<IParticipant> getCollaborationParticipants() {
		return collaborationParticipants;
	}
	public void setCollaborationParticipants(List<IParticipant> collaborationParticipants) {
		this.collaborationParticipants = collaborationParticipants;
	}
	public Object getVoucherPrivilege() {
		return voucherPrivilege;
	}
	public void setVoucherPrivilege(Object voucherPrivilege) {
		this.voucherPrivilege = voucherPrivilege;
	}
	public boolean isCanReject() {
		return canReject;
	}
	public void setCanReject(boolean canReject) {
		this.canReject = canReject;
	}
	public String getRejectPolicy() {
		return rejectPolicy;
	}
	public void setRejectPolicy(String rejectPolicy) {
		this.rejectPolicy = rejectPolicy;
	}
	public String getActivityRef() {
		return activityRef;
	}
	public void setActivityRef(String activityRef) {
		this.activityRef = activityRef;
	}
	public void setCandidateUserIdExpressions(Set<Expression> candidateUserIdExpressions) {
		this.candidateUserIdExpressions = candidateUserIdExpressions;
	}
	public void setCandidateGroupIdExpressions(Set<Expression> candidateGroupIdExpressions) {
		this.candidateGroupIdExpressions = candidateGroupIdExpressions;
	}
}
