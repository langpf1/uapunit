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
	//基本信息部分--------------------------------------------------------------
	@XmlAttribute
	public boolean approve;		//审批，使用动作标记
	@XmlAttribute
	public boolean deliver;		//传阅，使用固定的内置逻辑
	@XmlAttribute
	public boolean undertake;	//承办，
	@XmlAttribute
	public String processClass;	//处理类
	@XmlAttribute
	public String form;			//表单
	//@XmlElement
	public void modifyResources(){}//修改审批对话框资源信息
	
	//参与者，包括组织机构、角色、角色组、用户、用户组、回报关系、虚拟角色（主办人、协办人、发起人）、同其他活动节点参与者、自定义参与这类
	//权限控制
	@XmlAttribute
	public boolean canAddSign;	//允许加签
	@XmlAttribute
	public boolean canDelegate;	//允许改派
	@XmlAttribute
	public boolean canTransfer;	//可转发
	@XmlAttribute
	public boolean canDeliver;	//可传阅
	@XmlAttribute
	public boolean canAssign;	//由上一步指派
	@XmlAttribute
	public boolean opinionEditable;	//可编辑意见
	@XmlAttribute
	public boolean opinionNullable;//是否意见可空
	//同部门限定
	@XmlAttribute
	public boolean canHasten;	//允许催办
	@XmlAttribute
	public boolean canPrint;	//允许打印
	@XmlAttribute
	public boolean canRecycle;	//允许收回
	@XmlAttribute
	public boolean canPassthrough; //允许快速通道
	@XmlAttribute
	public boolean canUploadAttachment;	//允许附件上传
	@XmlAttribute
	public boolean canDownloadAttachment;//允许附件下载
	@XmlAttribute
	public boolean canDeleteAttachment;	//允许附件删除
	@XmlAttribute
	public boolean canModifyAttachment;	//允许附件修改
	@XmlAttribute
	public boolean canViewAttachment;	//允许附件查看
	//协办参与者
	@XmlElement(name="collaborationParticipants")
	@PropEditor("uap.workflow.ui.participant.editors.ParticipantPropertyEditor")
	public List<DefaultParticipantDefinition> collaborationParticipants = new ArrayList<DefaultParticipantDefinition>();
	@XmlElement
	public Object voucherPrivilege;
	//活动策略----------------------------------------------------------------
	//回退策略
	@XmlAttribute
	public boolean canReject; //禁止回退，允许回退
	@PropEditor("uap.workflow.modeler.editors.RejectTypeEditor")
	@XmlAttribute(name = "rejectPolicy")
	public String rejectPolicy; //上一步，制单人，全部活动，指定活动
	@XmlAttribute
	public String activityRef;
	//消息提醒----------------------------------------------------------------使用消息提醒和timer节点解决
	//任务创建消息提醒
		//任务创建提醒--使用协同消息
		//制单人控制项--使用协同消息
	//任务完成消息提醒
		//任务完成提醒--使用协同消息
		//制单人控制项--使用协同消息
	//时间估算
		//时间单位
		//提醒时间
		//工作时间
	//超时消息提醒
		//超时提醒--使用协同消息
		//制单人控制项--使用协同控制
	//超时控制
		//超时动作：继续等待、超时终止、超时继续
	

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
