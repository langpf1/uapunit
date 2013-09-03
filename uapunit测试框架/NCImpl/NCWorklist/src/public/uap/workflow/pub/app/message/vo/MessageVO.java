package uap.workflow.pub.app.message.vo;

import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;


/**
 * ��Ϣ��װVO��
 * 
 * @author ������ 2001-6-22
 * @modifier �׾� 2005-3-30 ���ﻯi18n
 * @modifier guowl 2008-4-2 �����Ƿ����־
 * @modifier guowl 2008-4-14 ����filecontent���ԣ����ڱ�ʾ��������
 * @modifier leijun 2008-10 ����m_titleColor����
 */
public class MessageVO extends ValueObject {
	/** ��ҵ����Ϣ��ʶ */
	public final static String NOT_BUSINESS_MSG = "XX";

	/** ��Ϣ״̬���� */
	public final static int MESSAGE_NOT_DEALED = 0;

	public final static int MESSAGE_DEALED = 1;

	public final static int MESSAGE_ALL = 2;

	/**
	 * ��Ϣ���ڿռ䣺����
	 * <li>XXX:6.0�ϲ�Ϊһ�� 
	 */
	//public final static int SPACE_IN_BULLETIN = -1; // ������
	//public final static int SPACE_IN_PREALERT = 1;// Ԥ����Ϣ
	public final static int SPACE_IN_WORKLIST = 0;// ��������

	/** ��Ϣ���� */
	private String primaryKey = null;

	/** �������ͱ��� */
	private String pk_billtype = null;

	/** Դ�������ͱ��� */
	private String pk_srcbilltype = null;

	/** ���ݺ� */
	private String billNO = null;

	/** �������� */
	private String billPK = null;

	/** ��˾���� */
	private String corpPK = null;

	/** �������ͱ��룬��Ϣ��չ���� */
	private String actionTypeCode = null;

	/** �����˱���,��������PK */
	private String senderCode = null;

	/** ���������� */
	private String senderName = null;

	/** �����˱���,��������PK */
	private String checkerCode = null;

	/** ���������� */
	private String checkerName = null;

	/** �Ƿ��Ѵ��� */
	private UFBoolean isCheck = null;

	/** ������� */
	private String checkNote = null;

	/** ����ʱ�� */
	private UFDateTime sendDateTime = null;

	/** ����ʱ�� */
	private UFDateTime dealDateTime = null;

	/** ��Ϣ���� */
	private String messageNote = null;

	/** ���� */
	private String m_title = null;

	/** ������ɫ */
	private String m_titleColor = null;

	/** ������Ϣ��htmlҳ���ڷ������ĵ�ַ */
	private String m_mailAddress = null;

	// ��Ϣ���� huangzg++
	private int msgType = MessageTypes.MSG_TYPE_APPROVE;

	private int priority = MessagePriority.PRI_NORMAL;

	/** ����Ϣ�Ĵ򿪷�ʽ�Ƿ�ǿ��Ϊ�Ի��� */
	private boolean isForceDialogOpen = false;

	/** ��������*/
	private byte[] filecontent = null;

	/** �����Ƿ��޸�*/
	private boolean isAttachChanged = false;

	/** �Ƿ��ѷ��*/
	private UFBoolean isSealed = UFBoolean.FALSE;

	/** �û����� */
	private String userobject;

	/** ���칤���������Ĺ��������ͣ�1=������ 3=����������<code>IApproveflowConst</code> */
	private int iWorkflowtype;

	public MessageVO() {
		super();
	}

	public String getTitleColor() {
		return m_titleColor;
	}

	public void setTitleColor(String color) {
		m_titleColor = color;
	}

	public int getWorkflowtype() {
		return iWorkflowtype;
	}

	public void setWorkflowtype(int workflowtype) {
		iWorkflowtype = workflowtype;
	}

	public String getUserobject() {
		return userobject;
	}

	public void setUserobject(String userobject) {
		this.userobject = userobject;
	}

	public boolean isForceDialogOpen() {
		return isForceDialogOpen;
	}

	public void setForceDialogOpen(boolean isForceDialogOpen) {
		this.isForceDialogOpen = isForceDialogOpen;
	}

	public String getPk_srcbilltype() {
		return pk_srcbilltype;
	}

	public void setPk_srcbilltype(String pk_srcbilltype) {
		this.pk_srcbilltype = pk_srcbilltype;
	}

	/**
	 * ���ض������ͱ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getActionTypeCode() {
		return actionTypeCode;
	}

	/**
	 * ���ص��ݱ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getBillNO() {
		return billNO;
	}

	/**
	 * ���������� �������ڣ�(2001-10-11 15:12:36)
	 * 
	 * @since��V1.00
	 * @return String
	 */
	public String getBillPK() {
		return billPK;
	}

	/**
	 * ����ҵ�����ͱ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getPk_billtype() {
		return pk_billtype;
	}

	/**
	 * ���ش����˱���  
	 * 
	 * @return String
	 */
	public String getCheckerCode() {
		return checkerCode;
	}

	/**
	 * ���ش��������� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getCheckerName() {
		return checkerName;
	}

	/**
	 * ���ش������ �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getCheckNote() {
		return checkNote;
	}

	/**
	 * ��˾������ �������ڣ�(2001-10-11 15:05:52)
	 * 
	 * @since��V1.00
	 * @return String
	 */
	public String getCorpPK() {
		return corpPK;
	}

	/**
	 * ���ش���ʱ�� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getDealDateTime() {
		return dealDateTime;
	}

	/**
	 * ������ֵ�������ʾ���ơ� �������ڣ�(2001-2-15 14:18:08)
	 * 
	 * @return String ������ֵ�������ʾ���ơ�
	 */
	public String getEntityName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000049")/*��Ϣ����*/;
	}

	/**
	 * ������Ϣ��htmlҳ���ڷ������ĵ�ַ �������ڣ�(2001-9-13 17:53:44)
	 * 
	 * @since ��V1.00
	 * @return String
	 */
	public String getMailAddress() {
		return m_mailAddress;
	}

	/**
	 * ������Ϣ���� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getMessageNote() {
		return messageNote;
	}

	/**
	 * �������� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {
		return primaryKey;
	}

	/**
	 * ���ӷ���ʱ�� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getSendDateTime() {
		return sendDateTime;
	}

	/**
	 * ���ӷ����˱��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getSenderCode() {
		return senderCode;
	}

	/**
	 * ���ӷ��������� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return String
	 */
	public String getSenderName() {
		return senderName;
	}

	/**
	 * ���ر��� �������ڣ�(2001-6-21 16:42:49)
	 * 
	 * @return String
	 */
	public String getTitle() {
		return m_title;
	}

	/**
	 * �����Ƿ��ѱ����� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public nc.vo.pub.lang.UFBoolean isCheck() {
		return isCheck;
	}

	/**
	 * ���ö������ͱ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newActionTypeCode String
	 */
	public void setActionTypeCode(String newActionTypeCode) {
		actionTypeCode = newActionTypeCode;
	}

	/**
	 * ���õ��ݱ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newBillNO String
	 */
	public void setBillNO(String newBillNO) {
		billNO = newBillNO;
	}

	/**
	 * ���������� �������ڣ�(2001-10-11 15:12:36)
	 * 
	 * @since��V1.00
	 * @param newBillPK String
	 */
	public void setBillPK(String newBillPK) {
		billPK = newBillPK;
	}

	/**
	 * ����ҵ�����ͱ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newBillTypeCode String
	 */
	public void setPk_billtype(String newBillTypeCode) {
		pk_billtype = newBillTypeCode;
	}

	/**
	 * ���ô����˱��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newCheckerCode String
	 */
	public void setCheckerCode(String newCheckerCode) {
		checkerCode = newCheckerCode;
	}

	/**
	 * ���ô��������� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newCheckerName String
	 */
	public void setCheckerName(String newCheckerName) {
		checkerName = newCheckerName;
	}

	/**
	 * ���ô������ �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newCheckNote String
	 */
	public void setCheckNote(String newCheckNote) {
		checkNote = newCheckNote;
	}

	/**
	 * ��˾������ �������ڣ�(2001-10-11 15:05:52)
	 * 
	 * @since��V1.00
	 * @param newCorpPK String
	 */
	public void setCorpPK(String newCorpPK) {
		corpPK = newCorpPK;
	}

	/**
	 * ���ô���ʱ�� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newDealDateTime nc.vo.pub.lang.UFDateTime
	 */
	public void setDealDateTime(nc.vo.pub.lang.UFDateTime newDealDateTime) {
		dealDateTime = newDealDateTime;
	}

	/**
	 * �����Ƿ��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newIsCheck nc.vo.pub.lang.UFBoolean
	 */
	public void setIsCheck(nc.vo.pub.lang.UFBoolean newIsCheck) {
		isCheck = newIsCheck;
	}

	/**
	 * ������Ϣ��htmlҳ���ڷ������ĵ�ַ �������ڣ�(2001-9-13 17:53:44)
	 * 
	 * @since ��V1.00
	 * @param newMailAddress String
	 */
	public void setMailAddress(String newMailAddress) {
		m_mailAddress = newMailAddress;
	}

	/**
	 * ������Ϣ���� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newMessageNote String
	 */
	public void setMessageNote(String newMessageNote) {
		messageNote = newMessageNote;
	}

	/**
	 * ������Ϣ���� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newPrimaryKey String
	 */
	public void setPrimaryKey(String newPrimaryKey) {
		primaryKey = newPrimaryKey;
	}

	/**
	 * ���÷���ʱ�� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newSendDateTime nc.vo.pub.lang.UFDateTime
	 */
	public void setSendDateTime(nc.vo.pub.lang.UFDateTime newSendDateTime) {
		sendDateTime = newSendDateTime;
	}

	/**
	 * ���÷����˱��� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newSenderCode String
	 */
	public void setSenderCode(String newSenderCode) {
		senderCode = newSenderCode;
	}

	/**
	 * ���÷��������� �������ڣ�(2001-6-22 13:39:44)
	 * 
	 * @param newSenderName String
	 */
	public void setSenderName(String newSenderName) {
		senderName = newSenderName;
	}

	/**
	 * ���ñ��� �������ڣ�(2001-6-21 16:42:49)
	 * 
	 * @param newTitle String
	 */
	public void setTitle(String newTitle) {
		m_title = newTitle;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int msgPriority) {
		this.priority = msgPriority;
	}

	/**
	 * ֻ��ʾ����������
	 * <li>NOTE::���������б����ֱ����ʾ�ĸö���!
	 * <li>����Ԥ��ƽ̨��Ϣ,name�̶�,���������ݿ��ȡ
	 * 
	 * @modifier huangzg ��������û�б�Ҫ���Ƿ����˺ͽ��յĲ���,����ֻ��ʾ�����ˡ�
	 */
	public String toString() {
		StringBuffer name = new StringBuffer();
		if (getMsgType() == MessageTypes.MSG_TYPE_PA) {
			// Ԥ��ƽ̨����Ϣ
			name.append(getPAMutliLangName());
			for (int i = name.toString().getBytes().length; i < 20; i++){
				name.append(".");
			}
			name.append(getAlertNoteAfterI18N());
			return name.toString();
		} else {
			name.append(getSenderName());
			if (name.length() == 0) {
				name.append("Oops!");
			}
			for (int i = name.toString().getBytes().length; i < 20; i++) {
				name.append(".");
			}
			name.append(getMessageNoteAfterI18N(getMessageNote()));
			return name.toString();
		}
	}

	/**
	 * ����Ԥ��ƽ̨��һ���ƵĶ�����ʾ
	 */
	public static String getPAMutliLangName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "alertSender");/* Ԥ��ƽ̨ */
	}

	/**
	 * Ԥ��ƽ̨����Ϣ����
	 * <li>pub_workflownote���н�������"2005-03-23 13:26:54"ʱ�䴮
	 * 
	 * @author �׾� 2005-3-30 20:34:22
	 */
	private String getAlertNoteAfterI18N() {
		String originalNote = getMessageNote();
		if (originalNote == null || originalNote.length() < 1)
			return "Oops!";
		// �����NC31��ǰ�汾����������,�򲻽���I18N
		if (originalNote.length() > "2005-03-23 13:26:54".length())
			return originalNote;
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "alertNote", null,
				new String[] { originalNote });
	}

	/**
	 * pub_workflownote��messagenote�ֶδ�NC31��ʼ�洢�ĸ�ʽΪ:
	 * <li>��"t5{commitBill}a{notify}b{checkBill}test111",
	 * <li>��Ҫ����ת��
	 * 
	 * @author �׾� 2005-3-30 20:34:22
	 */
	public static String getMessageNoteAfterI18N(String originalNote) {
		if (originalNote == null || originalNote.length() < 1)
			//return "Oops!";
			return "";

		boolean bStart = false;
		StringBuffer sbb = new StringBuffer();
		StringBuffer sbc = new StringBuffer();
		for (int i = 0; i < (originalNote == null ? 0 : originalNote.length()); i++) {
			char c = originalNote.charAt(i);
			// Logger.debug(c);
			if (c == '{') {
				bStart = true;
				sbb.setLength(0);
				continue;
			} else if (c == '}') {
				bStart = false;
				sbc
						.append(" "
								+ NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", sbb.toString()));
				sbb.setLength(0);
				continue;
			}
			if (bStart) {
				sbb.append(c);
			} else {
				sbc.append(c);
			}
		}
		return sbc.toString();
	}

	/* (non-Javadoc)
	 * @see nc.vo.pub.ValueObject#validate()
	 */
	public void validate() throws nc.vo.pub.ValidationException {
	}

	public UFBoolean isSealed() {
		return isSealed;
	}

	public void setIsSealed(UFBoolean isSealed) {
		this.isSealed = isSealed;
	}

	public byte[] getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(byte[] filecontent) {
		this.filecontent = filecontent;
	}

	public boolean isAttachChanged() {
		return isAttachChanged;
	}

	public void setAttachChanged(boolean isAttachChanged) {
		this.isAttachChanged = isAttachChanged;
	}

}