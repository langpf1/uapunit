package uap.workflow.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import nc.message.vo.AttachmentVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.TransitionSelectableInfo;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.XPDLNames;
import uap.workflow.engine.vos.AssignableInfo;
import uap.workflow.engine.vos.TaskInstanceVO;

/**
 * 审批流任务项VO对象,与pub_workflownote表对应
 * 
 * @author 樊冠军 2002-6-19
 * @modifier 雷军 2005-3-25 NC31该表增加了几个字段
 * @modifier 雷军 2005-10-20 转换为SuperVO
 * @modifier dingxm 2009-06-24 增加处理后通知人字段 observer，以及选择的加签用户字段extApprovers
 * @modifier leijun 2009-9 去掉金额列
 * @modifier yanke1 2012-1-10 去掉签收消息类型
 * 
 */
public class WorkflownoteVO extends SuperVO implements Serializable{
	/**
	 * 工作项类型-审批
	 */
	public static final String WORKITEM_TYPE_APPROVE = "Z";

	/**
	 * 工作项类型-制单
	 */
	public static final String WORKITEM_TYPE_MAKEBILL = "MAKEBILL";

	/**
	 * 工作项类型-业务消息
	 */
	public static final String WORKITEM_TYPE_BIZ = "BIZ";

	/**
	 * 工作项改派后缀标记
	 */
	public static final String WORKITEM_APPOINT_SUFFIX = "_D";

	/**
	 * 加签产生的工作项标记
	 */
	public static final String WORKITEM_ADDAPPROVER_SUFFIX = "_A";

	/**
	 * 业务消息使用pk_billtype表示节点号的前缀标记
	 */
	public static final String BIZ_NODE_PREFIX = "[F]";

	public static final String FLOWMSG_NEED_CHECK = "MSG_NEEDCHECK";

	public static final String FLOWMSG_AUTO = "MSG_NOCHECK";

	/** 主键 */
	public String pk_checkflow;

	/** 组织单元主键 */
	public String pk_org;

	/** 集团主键 */
	public String pk_group;

	/** 动作代码，扩展代码 */
	public String actiontype;

	/** 单据主键 */
	public String billid;
	
	public String billVersionPK;

	/** 单据号 */
	public String billno;

	/** 单据类型主键 */
	public String pk_billtype;

	/** 业务类型主键 */
	// public String pk_businesstype;
	/** 批语 */
	public String checknote;

	/** 处理时间 */
	public UFDateTime dealdate;

	/** 是否审批 */
	public String ischeck;

	/** 消息内容 */
	public String messagenote;

	/** 发送人PK */
	public String senderman;

	/** 审核人PK */
	public String checkman;

	/** 任务项的处理结果 */
	public String approveresult = null;

	// 工作项的处理状态
	public Integer approvestatus;

	// 消息优先级
	public Integer priority = Integer.valueOf(0);

	public String pk_wf_task;

	public Integer dr;

	// 用户对象
	private String userobject;

	// 工作流类型
	private Integer workflow_type;

	// //////////非数据库字段////////////////
	/** 发送人名称 */
	public String sendername = null;
	
	/** 发送人名称(当前语种) */
	public String sendernameml = null;

	/** 审核人名称 */
	public String checkname = null;
	
	/** 审核人名称(当前语种) */
	public String checknameml = null;

	/** 当前活动关联的一些扩展属性 */
	private HashMap relaProperties = new HashMap();
	
	/** 后继活动的可指派信息 */
	private Vector assignableInfos;

	/*
	 * 新增的 
	 */

	/** 任务信息 */
	private TaskInstanceVO taskInstanceVO;
	/* 下一步任务的信息 zhai 2013.4.3 因为 前台需要下一步任务的信息add*/
	private ITask[] newTasks = null;
	
	
	private List<UserTaskPrepCtx> userTaskPrepCtx;

	/** 驳回信息 */
	private RejectTaskInsCtx backwardInfo;
	/**
	 * 后继转移的可手工选择信息
	 */
	private Vector transitionSelectableInfos;
	
	/** 前加签信息 */
	private CreateBeforeAddSignCtx beforeAddSignCtx;

	/** 后加签信息 */
	private CreateAfterAddSignCtx afterAddSignCtx;

	private boolean isAssign;//可指派
	private boolean isAddSign;//可加签
	private boolean isDelegate;//可改派

	/** 节点指派的信息*/
	private Map<String,List<String>> assignInfoMap = new HashMap<String,List<String>>();
	/** 节点指派的信息*/
	private Map<String,Boolean> assignModeInfoMap = new HashMap<String,Boolean>();
	
	/*
	 * 
	 */

	private String ismsgbind;


	/* 加签选择的用户 */
	private List<String> extApprovers;

	/* 抄送选择的用户，未保存到表中 */
	private List<String> mailExtCpySenders = new ArrayList<String>();
	/* 信息抄送的用户 */
	private List<String> msgExtCpySenders = new ArrayList<String>();

	/* 工作项处理后 需要通知的用户，主要用于被加签人或者被该派人 处理后通知加签/改派的原始用户 */
	private String observer;

	private boolean anyoneCanApprove = false;

	private List<AttachmentVO> AttachmentSetting;

	/** 当前待办人是否对流程进行跟踪 */
	private boolean isTrack = false;

	public WorkflownoteVO() {
	}

	public WorkflownoteVO(String newPk_checkflow) {
		// 为主键字段赋值:
		pk_checkflow = newPk_checkflow;
	}

	public String getBillVersionPK() {
		return billVersionPK;
	}

	public void setBillVersionPK(String billVersionPK) {
		this.billVersionPK = billVersionPK;
	}

	public Integer getWorkflow_type() {
		return workflow_type;
	}

	public void setWorkflow_type(Integer workflow_type) {
		this.workflow_type = workflow_type;
	}

	public List<String> getMailExtCpySenders() {
		return mailExtCpySenders;
	}

	public void setMailExtCpySenders(List<String> extCpySenders) {
		this.mailExtCpySenders = extCpySenders;
	}

	public List<String> getMsgExtCpySenders() {
		return msgExtCpySenders;
	}

	public void setMsgExtCpySenders(List<String> extCpySenders) {
		this.msgExtCpySenders = extCpySenders;
	}

	public String getUserobject() {
		return userobject;
	}

	public void setUserobject(String userobject) {
		this.userobject = userobject;
	}

	public String getPk_wf_task() {
		return pk_wf_task;
	}

	public void setPk_wf_task(String pk_wf_task) {
		this.pk_wf_task = pk_wf_task;
	}

	/**
	 * 根类Object的方法,克隆这个VO对象。 创建日期：(2002-6-19)
	 */
	public Object clone() {
		// 复制基类内容并创建新的VO对象：
		Object o = super.clone();
		WorkflownoteVO workflownote = (WorkflownoteVO) o;

		// 你在下面复制本VO对象的所有属性：
		return workflownote;
	}

	public String getApproveresult() {
		return approveresult;
	}

	public void setApproveresult(String approveresult) {
		this.approveresult = approveresult;
	}

	public Integer getApprovestatus() {
		return approvestatus;
	}

	public void setApprovestatus(Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 属性m_actiontype的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getActiontype() {
		return actiontype;
	}

	/**
	 * 属性m_billid的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getBillid() {
		return billid;
	}

	/**
	 * 属性m_billno的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getBillno() {
		return billno;
	}

	/**
	 * 属性m_checkman的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getCheckman() {
		return checkman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:23:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCheckname() {
		return checkname;
	}

	/**
	 * 属性m_checknote的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getChecknote() {
		return checknote;
	}

	/**
	 * 属性m_dealdate的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getDealdate() {
		return dealdate;
	}

	/**
	 * 属性m_dr的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 返回数值对象的显示名称。 创建日期：(2002-6-19)
	 * 
	 * @return java.lang.String 返回数值对象的显示名称。
	 */
	public String getEntityName() {

		return "Workflownote";
	}

	/**
	 * 属性m_ischeck的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getIscheck() {
		return ischeck;
	}

	/**
	 * 属性m_messagenote的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getMessagenote() {
		return messagenote;
	}

	/**
	 * 属性m_pk_billtype的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * 属性m_pk_checkflow的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_checkflow() {
		return pk_checkflow;
	}

	/**
	 * 属性m_pk_corp的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_org() {
		return pk_org;
	}

	/**
	 * 返回对象标识，用来唯一定位对象。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_checkflow;
	}

	/**
	 * 属性m_senderman的Getter方法。 创建日期：(2002-6-19)
	 * 
	 * @return String
	 */
	public String getSenderman() {
		return senderman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:22:49)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSendername() {
		return sendername;
	}

	/**
	 * 属性m_actiontype的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_actiontype
	 *            String
	 */
	public void setActiontype(String newActiontype) {

		actiontype = newActiontype;
	}

	/**
	 * 属性m_billid的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_billid
	 *            String
	 */
	public void setBillid(String newBillid) {

		billid = newBillid;
	}

	/**
	 * 属性m_billno的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_billno
	 *            String
	 */
	public void setBillno(String newBillno) {

		billno = newBillno;
	}

	/**
	 * 属性m_checkman的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_checkman
	 *            String
	 */
	public void setCheckman(String newCheckman) {

		checkman = newCheckman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:23:02)
	 * 
	 * @param newCheckName
	 *            java.lang.String
	 */
	public void setCheckname(java.lang.String newCheckName) {
		checkname = newCheckName;
	}

	/**
	 * 属性m_checknote的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_checknote
	 *            String
	 */
	public void setChecknote(String newChecknote) {

		checknote = newChecknote;
	}

	/**
	 * 属性m_dealdate的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_dealdate
	 *            UFDateTime
	 */
	public void setDealdate(UFDateTime newDealdate) {

		dealdate = newDealdate;
	}

	/**
	 * 属性m_dr的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_dr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性m_ischeck的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_ischeck
	 *            String
	 */
	public void setIscheck(String newIscheck) {

		ischeck = newIscheck;
	}

	/**
	 * 属性m_messagenote的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_messagenote
	 *            String
	 */
	public void setMessagenote(String newMessagenote) {

		messagenote = newMessagenote;
	}

	/**
	 * 属性m_pk_billtype的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_billtype
	 *            String
	 */
	public void setPk_billtype(String newPk_billtype) {

		pk_billtype = newPk_billtype;
	}

	/**
	 * 属性m_pk_checkflow的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_checkflow
	 *            String
	 */
	public void setPk_checkflow(String newPk_checkflow) {

		pk_checkflow = newPk_checkflow;
	}

	/**
	 * 属性m_pk_corp的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_pk_corp
	 *            String
	 */
	public void setPk_org(String newPk_org) {

		pk_org = newPk_org;
	}

	/**
	 * 设置对象标识，用来唯一定位对象。 创建日期：(2002-6-19)
	 * 
	 * @param pk_checkflow
	 *            String
	 */
	public void setPrimaryKey(String newPk_checkflow) {

		pk_checkflow = newPk_checkflow;
	}

	/**
	 * 属性m_senderman的setter方法。 创建日期：(2002-6-19)
	 * 
	 * @param newM_senderman
	 *            String
	 */
	public void setSenderman(String newSenderman) {

		senderman = newSenderman;
	}

	/**
	 * 此处插入方法说明。 创建日期：(2002-6-21 15:22:49)
	 * 
	 * @param newSenderName
	 *            java.lang.String
	 */
	public void setSendername(java.lang.String newSenderName) {
		sendername = newSenderName;
	}

	/**
	 * 得到当前活动相关的流程属性，目前有 <li>XPDLNames.WORKFLOW_GADGET - 单据组件及其实参 <li>
	 * XPDLNames.EDITABLE_PROPERTIES - 可编辑属性 <li>XPDLNames.ENABLE_BUTTON - 可用按钮
	 * 
	 * @return
	 */
	public HashMap getRelaProperties() {
		return relaProperties;
	}
	
	public TaskInstanceVO getTaskInstanceVO() {
		if (taskInstanceVO == null) {
			taskInstanceVO = new TaskInstanceVO();
		}
		return taskInstanceVO;
	}

	public void setTaskInstanceVO(TaskInstanceVO taskInfo) {
		this.taskInstanceVO = taskInfo;
	}

	public RejectTaskInsCtx getBackwardInfo() {
		if (backwardInfo == null) {
			backwardInfo = new RejectTaskInsCtx();
		}
		return backwardInfo;
	}

	public void setBackwardInfo(RejectTaskInsCtx backwardInfo) {
		this.backwardInfo = backwardInfo;
	}
	
	public void addAssignableInfo(AssignableInfo assignableInfo) {
		getAssignableInfos().add(assignableInfo);
	}
	
	public Vector getAssignableInfos() {
		if (assignableInfos == null) {
			assignableInfos = new Vector(1);
		}
		return assignableInfos;
	}

	public List<UserTaskPrepCtx> getUserTaskPrepCtx() {
		return userTaskPrepCtx;
	}

	public void setUserTaskPrepCtx(List<UserTaskPrepCtx> userTaskPrepCtx) {
		this.userTaskPrepCtx = userTaskPrepCtx;
	}
	
	public void addTransitionSelectableInfo(TransitionSelectableInfo tsi) {
		getTransitionSelectableInfos().add(tsi);
	}
	
	public Vector getTransitionSelectableInfos() {
		if (transitionSelectableInfos == null) {
			transitionSelectableInfos = new Vector(1);
		}
		return transitionSelectableInfos;
	}
	
	public CreateBeforeAddSignCtx getBeforeAddSignCtx() {
		if (beforeAddSignCtx == null) {
			beforeAddSignCtx = new CreateBeforeAddSignCtx();
		}
		return beforeAddSignCtx;
	}

	public void setBeforeAddSignCtx(CreateBeforeAddSignCtx beforeAddSignCtx) {
		this.beforeAddSignCtx = beforeAddSignCtx;
	}

	public CreateAfterAddSignCtx getAfterAddSignCtx() {
		if (afterAddSignCtx == null) {
			afterAddSignCtx = new CreateAfterAddSignCtx();
		}
		return afterAddSignCtx;
	}

	public void setAfterAddSignCtx(CreateAfterAddSignCtx afterAddSignCtx) {
		this.afterAddSignCtx = afterAddSignCtx;
	}
	
	public boolean isAssign() {
		return isAssign;
	}

	public void setAssign(boolean isAssign) {
		this.isAssign = isAssign;
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

	public Map<String, List<String>> getAssignInfoMap() {
		return assignInfoMap;
	}

	public void setAssignInfoMap(Map<String, List<String>> assignInfoMap) {
		this.assignInfoMap = assignInfoMap;
	}

	public Map<String, Boolean> getAssignModeInfoMap() {
		return assignModeInfoMap;
	}

	public void setAssignModeInfoMap(Map<String, Boolean> assignModeInfoMap) {
		this.assignModeInfoMap = assignModeInfoMap;
	}
	
	
	
	public List<String> getExtApprovers() {
		return extApprovers;
	}

	public void setExtApprovers(List<String> extApprovers) {
		this.extApprovers = extApprovers;
	}

	public String getObserver() {
		return observer;
	}

	public void setObserver(String observer) {
		this.observer = observer;
	}

	public String getIsmsgbind() {
		return ismsgbind;
	}

	public void setIsmsgbind(String ismsgbind) {
		this.ismsgbind = ismsgbind;
	}

	public String getPk_group() {
		return pk_group;
	}

	public void setPk_group(String pk_group) {
		this.pk_group = pk_group;
	}

	public boolean isAnyoneCanApprove() {
		return anyoneCanApprove;
	}

	public void setAnyoneCanApprove(boolean anyoneCanApprove) {
		this.anyoneCanApprove = anyoneCanApprove;
	}

	public List getApplicationArgs() {
		return (List) getRelaProperties().get(XPDLNames.APPLICATION_ARGS);
	}

	public List<AttachmentVO> getAttachmentSetting() {
		return AttachmentSetting;
	}

	public void setAttachmentSetting(List<AttachmentVO> attch) {
		AttachmentSetting = attch;
	}

	public boolean isTrack() {
		return isTrack;
	}

	public void setTrack(boolean isTrack) {
		this.isTrack = isTrack;
	}

	public String getSendernameml() {
		return sendernameml;
	}

	public void setSendernameml(String sendernameml) {
		this.sendernameml = sendernameml;
	}

	public String getChecknameml() {
		return checknameml;
	}

	public void setChecknameml(String checknameml) {
		this.checknameml = checknameml;
	}

	public ITask[] getNewTasks() {
		return newTasks;
	}

	public void setNewTasks(ITask[] iTasks) {
		this.newTasks = iTasks;
	}

}