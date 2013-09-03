package uap.workflow.pub.app.message.vo;

import java.util.ArrayList;

import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.NullFieldException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;

/**
 * ��ϢVO����Ҫ��Ԥ����Ϣ���Է���Ϣ�͹����� <br>
 * ��Ӧ��:pub_messageinfo
 * 
 * @author huangzg 2006-3-28
 * @modifier guowl 2008-4 ���ӷ��״̬
 */
public class MessageinfoVO extends SuperVO {

	/**
	 * �����˳���:Ԥ��ƽ̨. <Note>�����޷���֤�����л�ʱ�õ�ֵͬ
	 */
	public static final String SENDER_NAME_PA = MessageVO.getPAMutliLangName();

	private String pk_messageinfo;

	private String senderman;// ������ID

	private String checkman;// ������ID

	private String pk_corp;

	private Integer type;// ��Ϣ����,���϶���

	private String url;// ��Ϣ·��

	private String title;// ��Ϣ����

	private String content;// ��Ϣ����

	private UFDateTime senddate;// ��������/��ʱ��

	private UFDateTime dealdate; // ��������

	private UFBoolean receivedeleteflag;// ����ɾ����־

	private Integer messagestate = Integer.valueOf(0);/* ��Ϣ״̬.0:δ����.1:�Ѵ���.2:�ѷ��*/

	public Integer priority = Integer.valueOf(0);// ��Ϣ���ȼ���������messagevo�ж���

	public String pk_billtype; // ��������

	public String pk_srcbilltype; // Դ��������

	public String billid; // ����ID

	public String billno; // ���ݺ�

	private Object filecontent; //�ļ�����

	private String actiontype;// ��չ�ֶΣ�Ŀǰ���ڱ�Ǹ���Ϣ�Ƿ�Ϊ"����"

	private String titlecolor; // ������Ϣ�������ɫ

	private Integer dr;

	private UFDateTime ts;

	// ---not stored in the table,just for MessageVO----
	private String sendermanName;

	private String checkmanName;
	
	private String pk_wf_msg;
	private boolean needFlowCheck;
	private String pk_wf_task;

	public String getTitlecolor() {
		return titlecolor;
	}

	public void setTitlecolor(String titlecolor) {
		this.titlecolor = titlecolor;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public Object getFilecontent() {
		return filecontent;
	}

	public void setFilecontent(Object filecontent) {
		this.filecontent = filecontent;
	}

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getPk_billtype() {
		return pk_billtype;
	}

	public void setPk_billtype(String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	public String getPk_srcbilltype() {
		return pk_srcbilltype;
	}

	public void setPk_srcbilltype(String pk_srcbilltype) {
		this.pk_srcbilltype = pk_srcbilltype;
	}

	/**
	 * ����checkman��Getter����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getCheckman() {
		return checkman;
	}

	/**
	 * ����content��Getter����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * ����dr��Getter����. ��������:(2006-3-28)
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * ����pk_corp��Getter����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * ����senddate��Getter����. ��������:(2006-3-28)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getSenddate() {
		return senddate;
	}

	/**
	 * ����senderman��Getter����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getSenderman() {
		return senderman;
	}

	/**
	 * ����title��Getter����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * ����ts��Getter����. ��������:(2006-3-28)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����type��Getter����. ��������:(2006-3-28)
	 * 
	 * @return Integer
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * ����url��Getter����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * ����checkman��setter����. ��������:(2006-3-28)
	 * 
	 * @param newCheckman
	 *            String
	 */
	public void setCheckman(String newCheckman) {

		checkman = newCheckman;
	}

	/**
	 * ����content��setter����. ��������:(2006-3-28)
	 * 
	 * @param newContent
	 *            String
	 */
	public void setContent(String newContent) {

		content = newContent;
	}

	/**
	 * ����dr��setter����. ��������:(2006-3-28)
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * ����pk_corp��setter����. ��������:(2006-3-28)
	 * 
	 * @param newPk_corp
	 *            String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * ����senddate��setter����. ��������:(2006-3-28)
	 * 
	 * @param newSenddate
	 *            UFDateTime
	 */
	public void setSenddate(UFDateTime newSenddate) {

		senddate = newSenddate;
	}

	/**
	 * ����senderman��setter����. ��������:(2006-3-28)
	 * 
	 * @param newSenderman
	 *            String
	 */
	public void setSenderman(String newSenderman) {

		senderman = newSenderman;
	}

	/**
	 * ����title��setter����. ��������:(2006-3-28)
	 * 
	 * @param newTitle
	 *            String
	 */
	public void setTitle(String newTitle) {

		title = newTitle;
	}

	/**
	 * ����ts��setter����. ��������:(2006-3-28)
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * ����type��setter����. ��������:(2006-3-28)
	 * 
	 * @param newType
	 *            Integer
	 */
	public void setType(Integer newType) {

		type = newType;
	}

	/**
	 * ����url��setter����. ��������:(2006-3-28)
	 * 
	 * @param newUrl
	 *            String
	 */
	public void setUrl(String newUrl) {

		url = newUrl;
	}

	public Integer getMessagestate() {
		return messagestate;
	}

	public void setMessagestate(Integer state) {
		this.messagestate = state;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getCheckmanName() {
		return checkmanName;
	}

	public void setCheckmanName(String checkmanName) {
		this.checkmanName = checkmanName;
	}

	public String getSendermanName() {
		return sendermanName;
	}

	public void setSendermanName(String sendermanName) {
		this.sendermanName = sendermanName;
	}

	public UFDateTime getDealdate() {
		return dealdate;
	}

	public void setDealdate(UFDateTime dealdate) {
		this.dealdate = dealdate;
	}

	public UFBoolean getReceivedeleteflag() {
		return receivedeleteflag;
	}

	public void setReceivedeleteflag(UFBoolean receivedeleteflag) {
		this.receivedeleteflag = receivedeleteflag;
	}

	/**
	 * ��֤���������֮��������߼���ȷ��. ��������:(2006-3-28)
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                �����֤ʧ��,�׳� ValidationException,�Դ�����н���.
	 */
	public void validate() throws ValidationException {

		ArrayList<String> errFields = new ArrayList<String>(); // errFields record those null
		// fields that cannot be null.
		// ����Ƿ�Ϊ������յ��ֶθ��˿�ֵ,�������Ҫ�޸��������ʾ��Ϣ:
		if (pk_messageinfo == null) {
			errFields.add(new String("pk_messageinfo"));
		}
		// construct the exception message:
		StringBuffer message = new StringBuffer();
		message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000040")/*�����ֶβ���Ϊ��:*/);
		if (errFields.size() > 0) {
			String[] temp = (String[]) errFields.toArray(new String[0]);
			message.append(temp[0]);
			for (int i = 1; i < temp.length; i++) {
				message.append(",");
				message.append(temp[i]);
			}
			// throw the exception:
			throw new NullFieldException(message.toString());
		}
	}

	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:(2006-3-28)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:(2006-3-28)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_messageinfo";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:(2006-3-28)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "pub_messageinfo";
	}

	/**
	 * ʹ�������ֶν��г�ʼ���Ĺ�����. ��������:(2006-3-28)
	 */
	public MessageinfoVO() {
		super();
	}

	/**
	 * ʹ���������г�ʼ���Ĺ�����. ��������:(2006-3-28)
	 * 
	 * @param Pk_messageinfo
	 *            ����ֵ
	 */
	public MessageinfoVO(String newPk_messageinfo) {
		super();

		// Ϊ�����ֶθ�ֵ:
		pk_messageinfo = newPk_messageinfo;
	}

	/**
	 * ���ض����ʶ,����Ψһ��λ����. ��������:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_messageinfo;
	}

	/**
	 * ���ö����ʶ,����Ψһ��λ����. ��������:(2006-3-28)
	 * 
	 * @param pk_messageinfo
	 *            String
	 */
	public void setPrimaryKey(String newPk_messageinfo) {

		pk_messageinfo = newPk_messageinfo;
	}

	/**
	 * ������ֵ�������ʾ����. ��������:(2006-3-28)
	 * 
	 * @return java.lang.String ������ֵ�������ʾ����.
	 */
	public String getEntityName() {

		return "Messageinfo";
	}

	/**
	 * ��MessageinfoVO����ת��ΪMessageVO����
	 * 
	 * @param msginfovo
	 * @return
	 */
	public static MessageVO transMsgInfoVO2MsgVO(MessageinfoVO msginfovo) {
		MessageVO msgvo = new MessageVO();
		if (msginfovo != null) {
			msgvo.setPrimaryKey(msginfovo.getPrimaryKey());
			msgvo.setMsgType(msginfovo.getType().intValue());
			msgvo.setCorpPK(msginfovo.getPk_corp());
			msgvo.setCheckerCode(msginfovo.getCheckman());
			msgvo.setCheckerName(msginfovo.getCheckmanName());
			String senderman = msginfovo.getSenderman();// ��������˵�PKΪ��,��ô��ʾΪ"ϵͳ"
			if (StringUtil.isEmptyWithTrim(senderman)) {
				senderman = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"UPPpfworkflow-000448")/*<<ϵͳ>>*/;
				msgvo.setSenderName(senderman);
			} else {
				msgvo.setSenderName(msginfovo.getSendermanName().trim());
			}
			msgvo.setSenderCode(senderman);
			msgvo.setMailAddress(msginfovo.getUrl());
			msgvo.setTitle(msginfovo.getTitle());
			msgvo.setMessageNote(msginfovo.getContent());
			msgvo.setIsCheck(UFBoolean
					.valueOf(msginfovo.getMessagestate().intValue() == MessageStatus.STATE_CHECKED ? true : false));
			//Added by guowl, 2008-4-2
			msgvo.setIsSealed(UFBoolean
					.valueOf(msginfovo.getMessagestate().intValue() == MessageStatus.STATE_SEALED ? true : false));
			msgvo.setSendDateTime(msginfovo.getSenddate());
			msgvo.setDealDateTime(msginfovo.getDealdate());

			msgvo.setPriority(msginfovo.getPriority().intValue());
			msgvo.setBillPK(msginfovo.getBillid());
			msgvo.setBillNO(msginfovo.getBillno());
			msgvo.setPk_billtype(msginfovo.getPk_billtype());
			msgvo.setPk_srcbilltype(msginfovo.getPk_srcbilltype());
			msgvo.setActionTypeCode(msginfovo.getActiontype());
			msgvo.setTitleColor(msginfovo.getTitlecolor());
			//
			msgvo.setFilecontent((byte[]) msginfovo.getFilecontent());
			if(msginfovo.isNeedFlowCheck()) {
				msgvo.setUserobject("[FLOWMSG]" +msginfovo.getPk_wf_msg()+msginfovo.getPk_wf_task());
			}
		}

		return msgvo;
	}

	public String getPk_wf_msg() {
		return pk_wf_msg;
	}

	public void setPk_wf_msg(String pk_wf_msg) {
		this.pk_wf_msg = pk_wf_msg;
	}

	public boolean isNeedFlowCheck() {
		return needFlowCheck;
	}

	public void setNeedFlowCheck(boolean needFlowCheck) {
		this.needFlowCheck = needFlowCheck;
	}

	public String getPk_wf_task() {
		return pk_wf_task;
	}

	public void setPk_wf_task(String pk_wf_task) {
		this.pk_wf_task = pk_wf_task;
	}

}