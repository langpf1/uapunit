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
 * 消息VO。主要是预警消息、对发消息和公告栏 <br>
 * 对应表:pub_messageinfo
 * 
 * @author huangzg 2006-3-28
 * @modifier guowl 2008-4 增加封存状态
 */
public class MessageinfoVO extends SuperVO {

	/**
	 * 发送人常量:预警平台. <Note>但是无法保证多语切换时得到同值
	 */
	public static final String SENDER_NAME_PA = MessageVO.getPAMutliLangName();

	private String pk_messageinfo;

	private String senderman;// 发送人ID

	private String checkman;// 接收人ID

	private String pk_corp;

	private Integer type;// 消息类型,如上定义

	private String url;// 消息路径

	private String title;// 消息标题

	private String content;// 消息内容

	private UFDateTime senddate;// 发送日期/含时间

	private UFDateTime dealdate; // 处理日期

	private UFBoolean receivedeleteflag;// 接收删除标志

	private Integer messagestate = Integer.valueOf(0);/* 消息状态.0:未处理.1:已处理.2:已封存*/

	public Integer priority = Integer.valueOf(0);// 消息优先级，常量见messagevo中定义

	public String pk_billtype; // 单据类型

	public String pk_srcbilltype; // 源单据类型

	public String billid; // 单据ID

	public String billno; // 单据号

	private Object filecontent; //文件内容

	private String actiontype;// 扩展字段，目前用于标记该消息是否为"改派"

	private String titlecolor; // 公告消息标题的颜色

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
	 * 属性checkman的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getCheckman() {
		return checkman;
	}

	/**
	 * 属性content的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 属性dr的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return Integer
	 */
	public Integer getDr() {
		return dr;
	}

	/**
	 * 属性pk_corp的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getPk_corp() {
		return pk_corp;
	}

	/**
	 * 属性senddate的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getSenddate() {
		return senddate;
	}

	/**
	 * 属性senderman的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getSenderman() {
		return senderman;
	}

	/**
	 * 属性title的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 属性ts的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return UFDateTime
	 */
	public UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性type的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return Integer
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * 属性url的Getter方法. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 属性checkman的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newCheckman
	 *            String
	 */
	public void setCheckman(String newCheckman) {

		checkman = newCheckman;
	}

	/**
	 * 属性content的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newContent
	 *            String
	 */
	public void setContent(String newContent) {

		content = newContent;
	}

	/**
	 * 属性dr的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newDr
	 *            Integer
	 */
	public void setDr(Integer newDr) {

		dr = newDr;
	}

	/**
	 * 属性pk_corp的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newPk_corp
	 *            String
	 */
	public void setPk_corp(String newPk_corp) {

		pk_corp = newPk_corp;
	}

	/**
	 * 属性senddate的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newSenddate
	 *            UFDateTime
	 */
	public void setSenddate(UFDateTime newSenddate) {

		senddate = newSenddate;
	}

	/**
	 * 属性senderman的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newSenderman
	 *            String
	 */
	public void setSenderman(String newSenderman) {

		senderman = newSenderman;
	}

	/**
	 * 属性title的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newTitle
	 *            String
	 */
	public void setTitle(String newTitle) {

		title = newTitle;
	}

	/**
	 * 属性ts的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newTs
	 *            UFDateTime
	 */
	public void setTs(UFDateTime newTs) {

		ts = newTs;
	}

	/**
	 * 属性type的setter方法. 创建日期:(2006-3-28)
	 * 
	 * @param newType
	 *            Integer
	 */
	public void setType(Integer newType) {

		type = newType;
	}

	/**
	 * 属性url的setter方法. 创建日期:(2006-3-28)
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
	 * 验证对象各属性之间的数据逻辑正确性. 创建日期:(2006-3-28)
	 * 
	 * @exception nc.vo.pub.ValidationException
	 *                如果验证失败,抛出 ValidationException,对错误进行解释.
	 */
	public void validate() throws ValidationException {

		ArrayList<String> errFields = new ArrayList<String>(); // errFields record those null
		// fields that cannot be null.
		// 检查是否为不允许空的字段赋了空值,你可能需要修改下面的提示信息:
		if (pk_messageinfo == null) {
			errFields.add(new String("pk_messageinfo"));
		}
		// construct the exception message:
		StringBuffer message = new StringBuffer();
		message.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("busitype", "busitypehint-000040")/*下列字段不能为空:*/);
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
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:(2006-3-28)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {

		return null;
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:(2006-3-28)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_messageinfo";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:(2006-3-28)
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "pub_messageinfo";
	}

	/**
	 * 使用主键字段进行初始化的构造子. 创建日期:(2006-3-28)
	 */
	public MessageinfoVO() {
		super();
	}

	/**
	 * 使用主键进行初始化的构造子. 创建日期:(2006-3-28)
	 * 
	 * @param Pk_messageinfo
	 *            主键值
	 */
	public MessageinfoVO(String newPk_messageinfo) {
		super();

		// 为主键字段赋值:
		pk_messageinfo = newPk_messageinfo;
	}

	/**
	 * 返回对象标识,用来唯一定位对象. 创建日期:(2006-3-28)
	 * 
	 * @return String
	 */
	public String getPrimaryKey() {

		return pk_messageinfo;
	}

	/**
	 * 设置对象标识,用来唯一定位对象. 创建日期:(2006-3-28)
	 * 
	 * @param pk_messageinfo
	 *            String
	 */
	public void setPrimaryKey(String newPk_messageinfo) {

		pk_messageinfo = newPk_messageinfo;
	}

	/**
	 * 返回数值对象的显示名称. 创建日期:(2006-3-28)
	 * 
	 * @return java.lang.String 返回数值对象的显示名称.
	 */
	public String getEntityName() {

		return "Messageinfo";
	}

	/**
	 * 将MessageinfoVO对象转化为MessageVO对象
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
			String senderman = msginfovo.getSenderman();// 如果发送人的PK为空,那么显示为"系统"
			if (StringUtil.isEmptyWithTrim(senderman)) {
				senderman = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"UPPpfworkflow-000448")/*<<系统>>*/;
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