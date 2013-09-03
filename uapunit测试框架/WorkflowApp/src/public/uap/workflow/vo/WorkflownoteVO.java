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
 * ������������VO����,��pub_workflownote���Ӧ
 * 
 * @author ���ھ� 2002-6-19
 * @modifier �׾� 2005-3-25 NC31�ñ������˼����ֶ�
 * @modifier �׾� 2005-10-20 ת��ΪSuperVO
 * @modifier dingxm 2009-06-24 ���Ӵ����֪ͨ���ֶ� observer���Լ�ѡ��ļ�ǩ�û��ֶ�extApprovers
 * @modifier leijun 2009-9 ȥ�������
 * @modifier yanke1 2012-1-10 ȥ��ǩ����Ϣ����
 * 
 */
public class WorkflownoteVO extends SuperVO implements Serializable{
	/**
	 * ����������-����
	 */
	public static final String WORKITEM_TYPE_APPROVE = "Z";

	/**
	 * ����������-�Ƶ�
	 */
	public static final String WORKITEM_TYPE_MAKEBILL = "MAKEBILL";

	/**
	 * ����������-ҵ����Ϣ
	 */
	public static final String WORKITEM_TYPE_BIZ = "BIZ";

	/**
	 * ��������ɺ�׺���
	 */
	public static final String WORKITEM_APPOINT_SUFFIX = "_D";

	/**
	 * ��ǩ�����Ĺ�������
	 */
	public static final String WORKITEM_ADDAPPROVER_SUFFIX = "_A";

	/**
	 * ҵ����Ϣʹ��pk_billtype��ʾ�ڵ�ŵ�ǰ׺���
	 */
	public static final String BIZ_NODE_PREFIX = "[F]";

	public static final String FLOWMSG_NEED_CHECK = "MSG_NEEDCHECK";

	public static final String FLOWMSG_AUTO = "MSG_NOCHECK";

	/** ���� */
	public String pk_checkflow;

	/** ��֯��Ԫ���� */
	public String pk_org;

	/** �������� */
	public String pk_group;

	/** �������룬��չ���� */
	public String actiontype;

	/** �������� */
	public String billid;
	
	public String billVersionPK;

	/** ���ݺ� */
	public String billno;

	/** ������������ */
	public String pk_billtype;

	/** ҵ���������� */
	// public String pk_businesstype;
	/** ���� */
	public String checknote;

	/** ����ʱ�� */
	public UFDateTime dealdate;

	/** �Ƿ����� */
	public String ischeck;

	/** ��Ϣ���� */
	public String messagenote;

	/** ������PK */
	public String senderman;

	/** �����PK */
	public String checkman;

	/** ������Ĵ����� */
	public String approveresult = null;

	// ������Ĵ���״̬
	public Integer approvestatus;

	// ��Ϣ���ȼ�
	public Integer priority = Integer.valueOf(0);

	public String pk_wf_task;

	public Integer dr;

	// �û�����
	private String userobject;

	// ����������
	private Integer workflow_type;

	// //////////�����ݿ��ֶ�////////////////
	/** ���������� */
	public String sendername = null;
	
	/** ����������(��ǰ����) */
	public String sendernameml = null;

	/** ��������� */
	public String checkname = null;
	
	/** ���������(��ǰ����) */
	public String checknameml = null;

	/** ��ǰ�������һЩ��չ���� */
	private HashMap relaProperties = new HashMap();
	
	/** ��̻�Ŀ�ָ����Ϣ */
	private Vector assignableInfos;

	/*
	 * ������ 
	 */

	/** ������Ϣ */
	private TaskInstanceVO taskInstanceVO;
	/* ��һ���������Ϣ zhai 2013.4.3 ��Ϊ ǰ̨��Ҫ��һ���������Ϣadd*/
	private ITask[] newTasks = null;
	
	
	private List<UserTaskPrepCtx> userTaskPrepCtx;

	/** ������Ϣ */
	private RejectTaskInsCtx backwardInfo;
	/**
	 * ���ת�ƵĿ��ֹ�ѡ����Ϣ
	 */
	private Vector transitionSelectableInfos;
	
	/** ǰ��ǩ��Ϣ */
	private CreateBeforeAddSignCtx beforeAddSignCtx;

	/** ���ǩ��Ϣ */
	private CreateAfterAddSignCtx afterAddSignCtx;

	private boolean isAssign;//��ָ��
	private boolean isAddSign;//�ɼ�ǩ
	private boolean isDelegate;//�ɸ���

	/** �ڵ�ָ�ɵ���Ϣ*/
	private Map<String,List<String>> assignInfoMap = new HashMap<String,List<String>>();
	/** �ڵ�ָ�ɵ���Ϣ*/
	private Map<String,Boolean> assignModeInfoMap = new HashMap<String,Boolean>();
	
	/*
	 * 
	 */

	private String ismsgbind;


	/* ��ǩѡ����û� */
	private List<String> extApprovers;

	/* ����ѡ����û���δ���浽���� */
	private List<String> mailExtCpySenders = new ArrayList<String>();
	/* ��Ϣ���͵��û� */
	private List<String> msgExtCpySenders = new ArrayList<String>();

	/* �������� ��Ҫ֪ͨ���û�����Ҫ���ڱ���ǩ�˻��߱������� �����֪ͨ��ǩ/���ɵ�ԭʼ�û� */
	private String observer;

	private boolean anyoneCanApprove = false;

	private List<AttachmentVO> AttachmentSetting;

	/** ��ǰ�������Ƿ�����̽��и��� */
	private boolean isTrack = false;

	public WorkflownoteVO() {
	}

	public WorkflownoteVO(String newPk_checkflow) {
		// Ϊ�����ֶθ�ֵ:
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
	 * ����Object�ķ���,��¡���VO���� �������ڣ�(2002-6-19)
	 */
	public Object clone() {
		// ���ƻ������ݲ������µ�VO����
		Object o = super.clone();
		WorkflownoteVO workflownote = (WorkflownoteVO) o;

		// �������渴�Ʊ�VO������������ԣ�
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
	 * ����m_actiontype��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getActiontype() {
		return actiontype;
	}

	/**
	 * ����m_billid��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getBillid() {
		return billid;
	}

	/**
	 * ����m_billno��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getBillno() {
		return billno;
	}

	/**
	 * ����m_checkman��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getCheckman() {
		return checkman;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-6-21 15:23:02)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCheckname() {
		return checkname;
	}

	/**
	 * ����m_checknote��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getChecknote() {
		return checknote;
	}

	/**
	 * ����m_dealdate��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getDealdate() {
		return dealdate;
	}

	/**
	 * ����m_dr��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ������ֵ�������ʾ���ơ� �������ڣ�(2002-6-19)
	 * 
	 * @return java.lang.String ������ֵ�������ʾ���ơ�
	 */
	public String getEntityName() {

		return "Workflownote";
	}

	/**
	 * ����m_ischeck��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getIscheck() {
		return ischeck;
	}

	/**
	 * ����m_messagenote��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getMessagenote() {
		return messagenote;
	}

	/**
	 * ����m_pk_billtype��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * ����m_pk_checkflow��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_checkflow() {
		return pk_checkflow;
	}

	/**
	 * ����m_pk_corp��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPk_org() {
		return pk_org;
	}

	/**
	 * ���ض����ʶ������Ψһ��λ���� �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_checkflow;
	}

	/**
	 * ����m_senderman��Getter������ �������ڣ�(2002-6-19)
	 * 
	 * @return String
	 */
	public String getSenderman() {
		return senderman;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-6-21 15:22:49)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSendername() {
		return sendername;
	}

	/**
	 * ����m_actiontype��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_actiontype
	 *            String
	 */
	public void setActiontype(String newActiontype) {

		actiontype = newActiontype;
	}

	/**
	 * ����m_billid��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_billid
	 *            String
	 */
	public void setBillid(String newBillid) {

		billid = newBillid;
	}

	/**
	 * ����m_billno��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_billno
	 *            String
	 */
	public void setBillno(String newBillno) {

		billno = newBillno;
	}

	/**
	 * ����m_checkman��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_checkman
	 *            String
	 */
	public void setCheckman(String newCheckman) {

		checkman = newCheckman;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-6-21 15:23:02)
	 * 
	 * @param newCheckName
	 *            java.lang.String
	 */
	public void setCheckname(java.lang.String newCheckName) {
		checkname = newCheckName;
	}

	/**
	 * ����m_checknote��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_checknote
	 *            String
	 */
	public void setChecknote(String newChecknote) {

		checknote = newChecknote;
	}

	/**
	 * ����m_dealdate��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_dealdate
	 *            UFDateTime
	 */
	public void setDealdate(UFDateTime newDealdate) {

		dealdate = newDealdate;
	}

	/**
	 * ����m_dr��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_dr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * ����m_ischeck��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_ischeck
	 *            String
	 */
	public void setIscheck(String newIscheck) {

		ischeck = newIscheck;
	}

	/**
	 * ����m_messagenote��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_messagenote
	 *            String
	 */
	public void setMessagenote(String newMessagenote) {

		messagenote = newMessagenote;
	}

	/**
	 * ����m_pk_billtype��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_pk_billtype
	 *            String
	 */
	public void setPk_billtype(String newPk_billtype) {

		pk_billtype = newPk_billtype;
	}

	/**
	 * ����m_pk_checkflow��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_pk_checkflow
	 *            String
	 */
	public void setPk_checkflow(String newPk_checkflow) {

		pk_checkflow = newPk_checkflow;
	}

	/**
	 * ����m_pk_corp��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_pk_corp
	 *            String
	 */
	public void setPk_org(String newPk_org) {

		pk_org = newPk_org;
	}

	/**
	 * ���ö����ʶ������Ψһ��λ���� �������ڣ�(2002-6-19)
	 * 
	 * @param pk_checkflow
	 *            String
	 */
	public void setPrimaryKey(String newPk_checkflow) {

		pk_checkflow = newPk_checkflow;
	}

	/**
	 * ����m_senderman��setter������ �������ڣ�(2002-6-19)
	 * 
	 * @param newM_senderman
	 *            String
	 */
	public void setSenderman(String newSenderman) {

		senderman = newSenderman;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2002-6-21 15:22:49)
	 * 
	 * @param newSenderName
	 *            java.lang.String
	 */
	public void setSendername(java.lang.String newSenderName) {
		sendername = newSenderName;
	}

	/**
	 * �õ���ǰ���ص��������ԣ�Ŀǰ�� <li>XPDLNames.WORKFLOW_GADGET - �����������ʵ�� <li>
	 * XPDLNames.EDITABLE_PROPERTIES - �ɱ༭���� <li>XPDLNames.ENABLE_BUTTON - ���ð�ť
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