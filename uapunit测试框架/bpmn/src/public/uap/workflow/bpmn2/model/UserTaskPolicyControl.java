package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.annotation.PropEditor;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="UserTaskPolicyControl")
public class UserTaskPolicyControl implements Serializable{

	private static final long serialVersionUID = 3014336204217098023L;
	//������Ϣ����--------------------------------------------------------------
	@XmlAttribute
	public boolean approve;		//������ʹ�ö������
	@XmlAttribute
	public boolean deliver;		//���ģ�ʹ�ù̶��������߼�
	@XmlAttribute
	public boolean undertake;	//�а죬
	@XmlAttribute
	public String processClass;	//������
	@XmlAttribute
	public String form;			//��
	//@XmlElement
	public void modifyResources(){}//�޸������Ի�����Դ��Ϣ
	
	//�����ߣ�������֯��������ɫ����ɫ�顢�û����û��顢�ر���ϵ�������ɫ�������ˡ�Э���ˡ������ˣ���ͬ������ڵ�����ߡ��Զ����������
	//Ȩ�޿���
	@XmlAttribute
	public boolean canAddSign;	//�����ǩ
	@XmlAttribute
	public boolean canDelegate;	//�������
	@XmlAttribute
	public boolean canTransfer;	//��ת��
	@XmlAttribute
	public boolean canDeliver;	//�ɴ���
	@XmlAttribute
	public boolean canAssign;	//����һ��ָ��
	@XmlAttribute
	public boolean opinionEditable;	//�ɱ༭���
	@XmlAttribute
	public boolean opinionNullable;//�Ƿ�����ɿ�
	//ͬ�����޶�
	@XmlAttribute
	public boolean canHasten;	//����߰�
	@XmlAttribute
	public boolean canPrint;	//�����ӡ
	@XmlAttribute
	public boolean canRecycle;	//�����ջ�
	@XmlAttribute
	public boolean canPassthrough; //�������ͨ��
	@XmlAttribute
	public boolean canUploadAttachment;	//�������ϴ�
	@XmlAttribute
	public boolean canDownloadAttachment;//����������
	@XmlAttribute
	public boolean canDeleteAttachment;	//������ɾ��
	@XmlAttribute
	public boolean canModifyAttachment;	//�������޸�
	@XmlAttribute
	public boolean canViewAttachment;	//�������鿴
	//Э�������
	@XmlElement(name="collaborationParticipants")
	@PropEditor("uap.workflow.ui.participant.editors.ParticipantPropertyEditor")
	public List<DefaultParticipantDefinition> collaborationParticipants = new ArrayList<DefaultParticipantDefinition>();
	@XmlElement
	public Object voucherPrivilege;
	//�����----------------------------------------------------------------
	//���˲���
	@XmlAttribute
	public boolean canReject; //��ֹ���ˣ��������
	@PropEditor("uap.workflow.modeler.editors.RejectTypeEditor")
	@XmlAttribute(name = "rejectPolicy")
	public String rejectPolicy; //��һ�����Ƶ��ˣ�ȫ�����ָ���
	@XmlAttribute
	public String activityRef;
	//��Ϣ����----------------------------------------------------------------ʹ����Ϣ���Ѻ�timer�ڵ���
	//���񴴽���Ϣ����
		//���񴴽�����--ʹ��Эͬ��Ϣ
		//�Ƶ��˿�����--ʹ��Эͬ��Ϣ
	//���������Ϣ����
		//�����������--ʹ��Эͬ��Ϣ
		//�Ƶ��˿�����--ʹ��Эͬ��Ϣ
	//ʱ�����
		//ʱ�䵥λ
		//����ʱ��
		//����ʱ��
	//��ʱ��Ϣ����
		//��ʱ����--ʹ��Эͬ��Ϣ
		//�Ƶ��˿�����--ʹ��Эͬ����
	//��ʱ����
		//��ʱ�����������ȴ�����ʱ��ֹ����ʱ����
	

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
	public String getProcessClass() {
		return processClass;
	}
	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public boolean isCanAddSign() {
		return canAddSign;
	}
	public void setCanAddSign(boolean canAddSign) {
		this.canAddSign = canAddSign;
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
	public boolean isCanAssign() {
		return canAssign;
	}
	public void setCanAssign(boolean canAssign) {
		this.canAssign = canAssign;
	}
	public boolean isCanDelegate() {
		return canDelegate;
	}
	public void setCanDelegate(boolean canDelegate) {
		this.canDelegate = canDelegate;
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
	public List<DefaultParticipantDefinition> getCollaborationParticipants() {
		return collaborationParticipants;
	}
	public void setCollaborationParticipants(List<DefaultParticipantDefinition> collaborationParticipants) {
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
}
