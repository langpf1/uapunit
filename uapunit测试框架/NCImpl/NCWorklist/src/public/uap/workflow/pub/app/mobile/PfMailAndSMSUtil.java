package uap.workflow.pub.app.mobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.common.RuntimeEnv;
import nc.bs.framework.server.ServerConfiguration;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.bs.pub.mail.MailTool;
import nc.bs.uap.scheduler.ITaskManager;
import nc.bs.uap.sf.excp.MailException;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.exception.DbException;
import nc.mailapprove.pub.IMailApproveService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.framework.rsa.Encode;
import nc.vo.jcom.io.IOUtil;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.pub.IDapType;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.pf.OrganizeUnit;
import nc.vo.uap.scheduler.TaskPriority;
import sun.misc.BASE64Encoder;

import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.query.WorkflowQueryUtil;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.vos.AssignableInfo;
import uap.workflow.pub.app.mail.PfEmailSendTask;
import uap.workflow.pub.app.mail.vo.DefaultSMTP;
import uap.workflow.pub.app.mail.vo.EmailMsg;
import uap.workflow.pub.app.message.vo.MessageVO;
import uap.workflow.pub.app.message.vo.SysMessageParam;
import uap.workflow.pub.app.mobile.vo.MobileMsg;
import uap.workflow.vo.WorkflownoteVO;


/**
 * 发送邮件和短信的后台工具类
 * <li>注意：只可后台调用
 *
 */
public class PfMailAndSMSUtil {
	/**
	 * 从人员档案中获取操作员的手机信息，用于短信发送
	 * 
	 * @param userIds
	 * @throws BusinessException
	 */
	public static String[] fetchPhonesByUserId(String[] userIds) throws BusinessException {
		if (userIds == null || userIds.length == 0)
			return null;
		ArrayList alPhones = new ArrayList();
		try {
			IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
			IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
			for (int i = 0; i < userIds.length; i++) {
				UserVO uservo = umq.getUser(userIds[i]);
				//PsnbasdocVO psnVO = umq.getPsnbasdocByUserid(userIds[i]);
				if (uservo.getPk_base_doc() == null) {
					Logger.error(NCLangResOnserver.getInstance().getStrByID("pfworkflow", "UPPpfworkflow-000908" /*
																												 * 用户没有关联人员档案
																												 * ，
																												 * 请关联上
																												 * !
																												 */));
					continue;

				}
				PsndocVO psnVO = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, uservo.getPk_base_doc());
				if (psnVO != null) {
					String phone = psnVO.getMobile();
					if (phone != null && phone.length() > 0)
						alPhones.add(phone);
				}
			}
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(e.getMessage());
		}
		return (String[]) alPhones.toArray(new String[] {});
	}
	
	/**
	 * 发送具有审批功能的HTML邮件，单据信息可有可无
	 * 
	 * @param billHtml
	 * @param email
	 * @param billId
	 * @param billtype
	 * @param topic
	 * @param checkman
	 * @param assignInfos
	 * @throws BusinessException
	 */
	public static void sendEmailWithApprove(String billHtml, String email, String billId,
			String billtype, String topic, String checkman, Vector assignInfos) throws BusinessException {

		// 发送具有审批功能的HTML邮件，单据信息可有可无
		String subject = MessageVO.getMessageNoteAfterI18N(topic);
//		subject += "(可打开附件进行审批)";
		subject = getEncodedString(subject);
		
		//==================发送离线审批邮件-START======================
		sendOfflineApproveEmail(billHtml, email, billId, billtype, checkman, subject);
		//=====================发送离线审批邮件-END======================
		
//		String htmlContentNoApprove = mailTemplateHasBillWithoutApprove(billHtml, billId);
//		String htmlContentWithApprove = mailTemplateHasBillWithApprove(billHtml, checkman, billtype,
//				billId, assignInfos);
//
//		// 产生临时附件
//		String fileName = RuntimeEnv.getInstance().getNCHome() + "/webapps/nc_web/pf/" + billId
//				+ ".html";
//		try {
//			writeTempFile(fileName, htmlContentWithApprove);
//		} catch (IOException e) {
//			Logger.error(e.getMessage(), e);
//		}
//		// 发送无审批功能的邮件，并带上审批附件
//		sendHtmlEmail(subject, new String[] { email }, htmlContentNoApprove, fileName);
//
//		// 删除临时邮件附件文件
//		File f = new File(fileName);
//		f.delete();
	}

	private static void sendOfflineApproveEmail(String billHtml, String email,
			String billId, String billtype, String checkman, String subject)
			throws BusinessException {
		IMailApproveService mailService = NCLocator.getInstance().lookup(IMailApproveService.class);
		String dsName = InvocationInfoProxy.getInstance().getUserDataSource();
		IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		UserVO user = (UserVO)uapQry.retrieveByPK(UserVO.class, checkman);
		String userName = user.getUser_name();
		String userPwd = user.getUser_password();
		mailService.sendAppoveMail(dsName, billId, billtype, billHtml, checkman, user.getUser_code(), userName, userPwd, email, subject);
	}

	private static String getEncodedString(String name) {
		if (name.getBytes().length == name.length())
			return name;
		BASE64Encoder encoder = new BASE64Encoder();
		String prefix = "=?gb2312?B?";
		String suffix = "?=";
		StringBuilder sb = new StringBuilder();
		int maxLen = 20;
		while (name.length() > maxLen) {
			String s = name.substring(0, maxLen);
			sb.append(prefix).append(encoder.encode(s.getBytes())).append(suffix);
			name = name.substring(maxLen);
		}
		if (name.length() > 0) {
			sb.append(prefix).append(encoder.encode(name.getBytes())).append(suffix);
		}
		return sb.toString();
	}

	private static void writeTempFile(String fileName, String strContent) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(fileName);
			fw.write(strContent);
		} catch (IOException e) {
			Logger.error(e.getMessage(), e);
			throw e;
		} finally {
			if (fw != null)
				fw.close();
		}
	}

	/**
	 * 发送无审批功能的邮件，单据信息可有可无
	 * <li>如果有单据信息，则为HTML邮件；
	 * <li>如果无单据信息，则为文本邮件
	 * 
	 * @param billHtml
	 * @param strEmails
	 * @param topic
	 * @param billId
	 * @throws BusinessException
	 * @throws DbException 
	 */
	public static void sendEmailsWithoutApprove(String billHtml, String[] strEmails, String topic,
			String billId) throws BusinessException, DbException {
		String subject = MessageVO.getMessageNoteAfterI18N(topic);
		subject = getEncodedString(subject);

		if (StringUtil.isEmptyWithTrim(billHtml)) {
			// 发送简单格式的邮件，无审批功能
			sendEmail(subject, NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000000")/*请及时登录处理!*/, strEmails, null, null);
		} else {
			// 发送HTML单据内容格式的邮件，无审批功能
			sendHtmlEmail(subject, strEmails, mailTemplateHasBillWithoutApprove(billHtml, billId), null);
		}
	}

	/**
	 * 解释模板HTML文件，替换其中的单据信息
	 * <li>无审批功能
	 * 
	 * @param billHtml
	 * @param billId
	 * @return
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private static String mailTemplateHasBillWithoutApprove(String billHtml, String billId)
			throws BusinessException, DbException {
		Logger.debug("mailTemplateHasBillWithoutApprove() called");
		// 从后台文件读取邮件模板
		String templateURL = RuntimeEnv.getInstance().getNCHome() + "/webapps/nc_web/pf";
		File f = new File(templateURL + "/mail_template.htm");
		String strFile = null;
		try {
			strFile = IOUtil.toString(new BufferedReader(new FileReader(f)));
		} catch (FileNotFoundException e) {
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000001")/*>>错误：找不到webapps/nc_web/下的邮件模板文件*/);
		} catch (IOException e) {
			throw new PFBusinessException(e.getMessage(), e);
		}

		if (StringUtil.isEmptyWithTrim(strFile))
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000002")/*>>错误：读取webapps/nc_web/下的邮件模板文件为空*/;

		// XXX::获得Web服务器地址
		String webUrl = ServerConfiguration.getServerConfiguration().getDefWebServerURL();
		String pf_web_url = webUrl + "/pf";

		// 替换所有图片的URL
		strFile = StringUtil.replaceAllString(strFile, "#PF_WEB_URL#", pf_web_url);

		// 替换单据信息
		if (StringUtil.isEmptyWithTrim(billHtml)) {
			String tagBillBegin = "<!--BILL_INFO_BEGIN-->";
			String tagBillEnd = "<!--BILL_INFO_END-->";
			int index = strFile.indexOf(tagBillBegin);
			int indexEnd = strFile.indexOf(tagBillEnd);
			strFile = strFile.substring(0, index + tagBillBegin.length()) + strFile.substring(indexEnd);
		} else {
			String tag2BillBegin = "<!--BILL_INFO2_BEGIN-->";
			String tag2BillEnd = "<!--BILL_INFO2_END-->";
			int index = strFile.indexOf(tag2BillBegin);
			int indexEnd = strFile.indexOf(tag2BillEnd);
			strFile = strFile.substring(0, index + tag2BillBegin.length()) + billHtml
					+ strFile.substring(indexEnd);
		}

		// 替换历史审批信息
		String tagHistoryBegin = "<!--APPROVE_HISTORY_BEGIN-->";
		String tagHistoryEnd = "<!--APPROVE_HISTORY_END-->";
		int index = strFile.indexOf(tagHistoryBegin);
		int indexEnd = strFile.indexOf(tagHistoryEnd);
		String strHistory = strFile.substring(index + tagHistoryBegin.length(), indexEnd);
		strFile = strFile.substring(0, index + tagHistoryBegin.length())
				+ getNoteOfBill(strHistory, billId) + strFile.substring(indexEnd);

		return strFile;

	}

	/**
	 * 解释模板HTML文件，替换其中的单据信息以及审批信息
	 * <li>有审批功能
	 * 
	 * @param billHtml
	 * @param checkman
	 * @param billType
	 * @param billId
	 * @param assignInfos
	 * @return
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private static String mailTemplateHasBillWithApprove(String billHtml, String checkman,
			String billType, String billId, Vector assignInfos) throws BusinessException, DbException {
		Logger.debug("mailTemplateHasBillWithApprove() called");
		// 从后台文件读取邮件模板
		String templateURL = RuntimeEnv.getInstance().getNCHome() + "/webapps/nc_web/pf";
		String fileName = null;
		// XXX:判断邮件审批需要数字签名
		boolean isSignature = WirelessManager.getMobileConfig().isSignature();
		if (isSignature)
			fileName = "/approvemail_template2.htm";
		else
			fileName = "/approvemail_template.htm";
		File f = new File(templateURL + fileName);
		String strFile = null;
		try {
			strFile = IOUtil.toString(new BufferedReader(new FileReader(f)));
		} catch (FileNotFoundException e) {
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000001")/*>>错误：找不到webapps/nc_web/下的邮件模板文件*/);
		} catch (IOException e) {
			throw new PFBusinessException(e.getMessage(), e);
		}

		if (StringUtil.isEmptyWithTrim(strFile))
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000002")/*>>错误：读取webapps/nc_web/下的邮件模板文件为空*/;

		// XXX::获得Web服务器地址
		String webUrl = ServerConfiguration.getServerConfiguration().getDefWebServerURL();
		String pf_web_url = webUrl + "/pf";
		// 替换所有图片的URL
		strFile = StringUtil.replaceAllString(strFile, "#PF_WEB_URL#", pf_web_url);

		// 替换单据信息
		if (StringUtil.isEmptyWithTrim(billHtml)) {
			String tagBillBegin = "<!--BILL_INFO_BEGIN-->";
			String tagBillEnd = "<!--BILL_INFO_END-->";
			int index = strFile.indexOf(tagBillBegin);
			int indexEnd = strFile.indexOf(tagBillEnd);
			strFile = strFile.substring(0, index + tagBillBegin.length()) + strFile.substring(indexEnd);
		} else {
			String tag2BillBegin = "<!--BILL_INFO2_BEGIN-->";
			String tag2BillEnd = "<!--BILL_INFO2_END-->";
			int index = strFile.indexOf(tag2BillBegin);
			int indexEnd = strFile.indexOf(tag2BillEnd);
			strFile = strFile.substring(0, index + tag2BillBegin.length()) + billHtml
					+ strFile.substring(indexEnd);
		}

		// 替换表单的action
		String strActionServlet = webUrl + "/service/mailapproveserv";
		strFile = StringUtil.replaceAllString(strFile, "#ACTIONSERVLET_URL#", strActionServlet);

		// 替换隐藏域
		// 将uid/pwd/billid/billtype/ds作为5个隐藏字段
		String strHidden = "<p><input name=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_UID
				+ "\" type=\"hidden\" id=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_UID
				+ "\" value=\""
				+ checkman
				+ "\">\n"
				+ "<input name=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_DS
				+ "\" type=\"hidden\" id=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_DS
				+ "\" value=\""
				+ InvocationInfoProxy.getInstance().getUserDataSource()
				+ "\">\n"
				+ "<input name=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_BILLTYPE
				+ "\" type=\"hidden\" id=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_BILLTYPE
				+ "\" value=\""
				+ billType
				+ "\">\n"
				+ "<input name=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_BILLID
				+ "\" type=\"hidden\" id=\""
				+ PfMailAndSMSUtil.MAIL_APPROVE_BILLID
				+ "\" value=\""
				+ billId
				+ "\">\n"
				+ (isSignature ? "<input name=\"" + PfMailAndSMSUtil.MAIL_SIGNED_APPROVE_UID
						+ "\" type=\"hidden\" id=\"" + PfMailAndSMSUtil.MAIL_SIGNED_APPROVE_UID + "\">" : "")
				+ "</p>\n";

		String tagHidden = "<!--HIDDEN_FIELD-->";
		int index = strFile.indexOf(tagHidden);
		strFile = strFile.substring(0, index + tagHidden.length()) + strHidden
				+ strFile.substring(index);

		// 替换历史审批信息
		String tagHistoryBegin = "<!--APPROVE_HISTORY_BEGIN-->";
		String tagHistoryEnd = "<!--APPROVE_HISTORY_END-->";
		index = strFile.indexOf(tagHistoryBegin);
		int indexEnd = strFile.indexOf(tagHistoryEnd);
		String strHistory = strFile.substring(index + tagHistoryBegin.length(), indexEnd);
		strFile = strFile.substring(0, index + tagHistoryBegin.length())
				+ getNoteOfBill(strHistory, billId) + strFile.substring(indexEnd);

		// 替换指派信息
		String tagDispatchBegin = "<!--DISPATCH_BEGIN-->";
		String tagDispatchEnd = "<!--DISPATCH_END-->";
		index = strFile.indexOf(tagDispatchBegin);
		indexEnd = strFile.indexOf(tagDispatchEnd);
		if (assignInfos == null || assignInfos.size() == 0) {
			strFile = strFile.substring(0, index + tagDispatchBegin.length())
					+ strFile.substring(indexEnd);
		} else {
			String tagDispatchActivityBegin = "<!--DISPATCH_ACTIVITY_BEGIN-->";
			String tagDispatchActivityEnd = "<!--DISPATCH_ACTIVITY_END-->";
			int index_a = strFile.indexOf(tagDispatchActivityBegin);
			int indexEnd_a = strFile.indexOf(tagDispatchActivityEnd);
			String strDispatchAct = strFile.substring(index_a + tagDispatchActivityBegin.length(),
					indexEnd_a);

			StringBuffer strTemp1 = new StringBuffer();
			for (Iterator iter = assignInfos.iterator(); iter.hasNext();) {
				AssignableInfo ainfo = (AssignableInfo) iter.next();
				strDispatchAct = StringUtil.replaceIgnoreCase(strDispatchAct, "#ACTIVITY_NAME#", ainfo
						.getDesc());

				String tagSelectUserBegin = "<!--SELECT_USERS_BEGIN-->";
				String tagSelectUserEnd = "<!--SELECT_USERS_END-->";
				int index_b = strDispatchAct.indexOf(tagSelectUserBegin);
				int indexEnd_b = strDispatchAct.indexOf(tagSelectUserEnd);
				String strUser = strDispatchAct
						.substring(index_b + tagSelectUserBegin.length(), indexEnd_b);
				
				StringBuffer strTemp2 = new StringBuffer();
				
				for (int i = 0; i < ainfo.getOuUsers().size(); i++) {
					OrganizeUnit ou = (OrganizeUnit) ainfo.getOuUsers().get(i);
					String ssstrUser = StringUtil.replaceIgnoreCase(strUser, "#USER_ID#", ou.getPk() + "#"
							+ ainfo.getActivityDefId());
					ssstrUser = StringUtil.replaceIgnoreCase(ssstrUser, "#USER_NAME#", ou.getName());
					strTemp2.append(ssstrUser);
				}
				strTemp1.append(strDispatchAct.substring(0, index_b + tagSelectUserBegin.length()));
				strTemp1.append(strTemp2);
				strTemp1.append(strDispatchAct.substring(indexEnd_b));
			}

			strFile = strFile.substring(0, index_a + tagDispatchActivityBegin.length()) + strTemp1.toString()
					+ strFile.substring(indexEnd_a);

		}

		return strFile;
	}

	/**
	 * 查询该单据的历史审批信息，并拼凑到HTML中
	 * 
	 * @param strHistory
	 * @param billId
	 * @return
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private static String getNoteOfBill(String strHistory, String billId) throws BusinessException, DbException {
		// 查询该单据的历史审批信息
		String sql = "select finishdate,opinion,b.user_name,state_task,ispass,begindate,actiontype "
				+ "from wf_task a, sm_user b "
				+ "where pk_executer=b.cuserid and finishdate is not null and pk_form_ins_version='"
				+ billId
				+ "' order by a.dealdate";
		
		Vector checkedData = null;
		checkedData = WorkflowQueryUtil.queryDataBySQL(sql, 7, new int[] { IDapType.UFDATE, IDapType.STRING,
				IDapType.STRING, IDapType.INTEGER, IDapType.STRING, IDapType.UFDATE, IDapType.STRING });

		StringBuffer strRet = new StringBuffer();
		for (int i = 0; i < (checkedData == null ? 0 : checkedData.size()); i++) {
			Vector rowData = (Vector) checkedData.get(i);
			// XXX:第一列数据为行号

			// 检查该工作项是否为修单
			String strActiontype = String.valueOf(rowData.get(7));
			boolean isMakebill = false;
			if (WorkflownoteVO.WORKITEM_TYPE_MAKEBILL.equalsIgnoreCase(strActiontype))
				isMakebill = true;

			// 解析审批状况
			int status = ((Integer) rowData.get(4)).intValue();
			String strStatus = resolveApproveStatus(status, isMakebill);

			// 解析审核意见
			String strResultI18N = resolveApproveResult(isMakebill ? null : rowData.get(5));

			// 根据审批日期计算历时
			//String strSendTime = String.valueOf(rowData.get(6));
			//String strApproveTime = String.valueOf(rowData.get(1));
			// UFDateTime serverTime = ClientEnvironment.getServerTime();
			// String ellapsed = DurationUnit.getElapsedTime(new
			// UFDateTime(strSendTime), new UFDateTime(strApproveTime));

			// 分别为：审批日期、审批人、审批状况、审批意见、批语
			String strTemp = StringUtil.replaceAllString(strHistory, "#DATE#", String.valueOf(rowData
					.get(1)));
			strTemp = StringUtil.replaceAllString(strTemp, "#CHECKMAN#", String.valueOf(rowData.get(3)));
			strTemp = StringUtil.replaceAllString(strTemp, "#STATUS#", strStatus);
			strTemp = StringUtil.replaceAllString(strTemp, "#RESULT#", strResultI18N);
			Object noteObj = rowData.get(2);
			strTemp = StringUtil.replaceAllString(strTemp, "#CHECKNOTE#", noteObj == null ? "" : String
					.valueOf(noteObj));
			strRet.append(strTemp);
			strRet.append("\n");
		}
		return strRet.toString();
	}

	/**
	 * 获得多语化的审批结果
	 * 
	 * @param result
	 */
	private static String resolveApproveResult(Object result) {
		String obj = null;
		if (result == null) {
			obj = "";
		} else if (result.equals("Y")) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("102220",
					"UPP102220-000161")/* @res "审核通过" */;
		} else if (result.equals("N")) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("102220",
					"UPP102220-000160")/* @res "审核不通过" */;
		} else if (result.equals("R")) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("102220",
					"UPP102220-000195")/* @res "驳回" */;
		} else {
			obj = String.valueOf(result);
		}
		return obj;
	}

	/**
	 * 获得多语化的审批状况
	 * 
	 * @param iStatus
	 * @param isMakebill 是否为修单工作项
	 * @param isCheckMsg
	 */
	private static String resolveApproveStatus(int iStatus, boolean isMakebill) {
		String obj = null;
		if (iStatus == TaskInstanceStatus.Finished.getIntValue()) {
			if (isMakebill) {
				obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000011")/*已修单 */;
			} else {
				obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000198")/*@res"已审批"*/;
			}
		} else if (iStatus == TaskInstanceStatus.Inefficient.getIntValue()) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000005")/*@res"作废"*/;
		} else if (iStatus == TaskInstanceStatus.Started.getIntValue()) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000199")/*@res"未审批"*/;
		} else {
			obj = "";
		}
		return obj;
	}
	
	/**
	 * 发送HTML邮件内容，并可带附件
	 * 
	 * @param subject
	 * @param recEmails
	 * @param htmlContent
	 * @param fileName
	 * @throws BusinessException
	 */
	public static void sendHtmlEmail(String subject, String[] recEmails, String htmlContent,
			String fileName) throws BusinessException {
		Logger.info("==================");
		Logger.info(">> 发送HTML邮件PfMailAndSMSUtil.sendHtmlEmail() called");
		Logger.info("==================");

		SysMessageParam smp = WirelessManager.fetchSysMsgParam();
		if (smp == null)
			throw new MailException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000003")/*错误1：没有正确配置邮件服务器文件/ierp/bin/message4pf.xml*/);
		DefaultSMTP defaultSmtp = smp.getSmtp();
		if (defaultSmtp == null)
			throw new MailException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000004")/*错误2：没有正确配置邮件服务器文件/ierp/bin/message4pf.xml*/);

		String smtpHost = defaultSmtp.getSmtp();
		String fromAddr = defaultSmtp.getSender();
		String userName = defaultSmtp.getUser();
		String password = defaultSmtp.getPassword();
		String senderName = defaultSmtp.getSenderName();
		
		if (!StringUtil.isEmptyWithTrim(password)) {
			password = new Encode().decode(password);
		}
		
		// XXX::接收地址必须以","分隔
		StringBuffer receivers = new StringBuffer();
		for (int i = 0; i < recEmails.length; i++) {
			receivers.append(recEmails[i]);
			receivers.append(",");
		}

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(htmlContent);
			Logger.info("邮件接收者receivers=" + receivers);
			MailTool.sendHtmlEmail(smtpHost, fromAddr, senderName, userName, password, receivers.toString(),
					subject, sb, fileName);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000005", null, new String[]{e.getMessage()})/*错误：发送Email失败：{0}*/);
		}

	}

	/**
	 * 发送邮件的工具方法，非EJB调用
	 * <li>只可后台调用
	 * <li>邮件服务器信息配置在message4pf.xml文件中
	 * 
	 * @param title 邮件主题
	 * @param content 邮件内容
	 * @param recEmails 接收人邮件地址
	 * @param ccEmails 抄送人邮件地址
	 * @param attachFiles 附件文件路径
	 * @throws MailException
	 */
	public static void sendEmail(String title, String content, String[] recEmails, String[] ccEmails,
			String[] attachFiles) throws MailException {
		Logger.info("==================");
		Logger.info(">> 发送邮件PfMailAndSMSUtil.sendEmail() called");
		Logger.info("==================");
		if (recEmails == null || recEmails.length == 0)
			return;
		
		// XXX:接收地址必须以","分隔
		StringBuffer receivers = new StringBuffer();
		for (int i = 0; i < recEmails.length; i++) {
			receivers.append(recEmails[i]);
			receivers.append(",");
		}
		StringBuffer ccs = new StringBuffer();
		for (int i = 0; i < (ccEmails == null ? 0 : ccEmails.length); i++) {
			ccs.append(ccEmails[i]);
			ccs.append(",");
		}
		Logger.info("邮件接收者receivers=" + receivers);

		IMailApproveService mailService = NCLocator.getInstance().lookup(IMailApproveService.class);
		try {
			mailService.sendNormalMail(receivers.toString(), title, content, false);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new MailException();
		}
	}

	

	/**
	 * 发送邮件的工具类，非EJB调用
	 * <li>只可后台调用
	 * <li>使用调度引擎异步发送邮件
	 * 
	 * @param em 邮件发送相关信息
	 * @throws BusinessException
	 */
	public static void sendEMS(EmailMsg em) throws BusinessException {
		Logger.info("========================");
		Logger.info(">> 使用调度引擎异步发送邮件");
		Logger.info("========================");
		String[] userIds = em.getUserIds();
		if (userIds == null || userIds.length == 0) {
			Logger.warn(">>找不到接收人，无法发送邮件，返回");
			return;
		}

		// /应该使用调度引擎来执行邮件发送任务
		ITaskManager tmgr = (ITaskManager) NCLocator.getInstance().lookup(ITaskManager.class.getName());
		PfEmailSendTask task = new PfEmailSendTask(em);
		tmgr.add(task, TaskPriority.GENERAL);
	}

	/**
	 * 发送短消息的工具类，非EJB调用
	 * <li>只可后台调用
	 * <li>使用调度引擎异步发送短信
	 * <li>如果需要EJB调用，则使用IPFMessage组件服务
	 * 
	 * @param userIds 接收用户PK数组
	 * @param content 短信内容
	 * @throws Exception
	 */
	public static void sendSMS(String[] userIds, String content) throws BusinessException {
		String[] targetPhones = fetchPhonesByUserId(userIds);

		MobileMsg mm = new MobileMsg();
		mm.setTargetPhones(targetPhones);
		mm.setMsg(content);

		sendSMS(mm);
	}

	/**
	 * 发送短消息的工具类，非EJB调用
	 * <li>只可后台调用
	 * <li>使用调度引擎异步发送短信
	 * <li>如果需要EJB调用，则使用IPFMessage组件服务
	 * 
	 * @param mobileVO 短信发送相关信息
	 * @throws BusinessException
	 */
	public static void sendSMS(MobileMsg mobileVO) throws BusinessException {
		Logger.info("========================");
		Logger.info(">> 使用调度引擎异步发送短信");
		Logger.info("========================");
		String[] targetPhones = mobileVO.getTargetPhones();
		if (targetPhones == null || targetPhones.length == 0) {
			Logger.warn(">>找不到手机号，无法发送短信，返回");
			return;
		}

		// /应该使用调度引擎来执行短信发送任务
		ITaskManager tmgr = (ITaskManager) NCLocator.getInstance().lookup(ITaskManager.class.getName());
		PfMobileSendTask task = new PfMobileSendTask(mobileVO);
		tmgr.add(task, TaskPriority.GENERAL);

	}

	/**
	 * 邮件审批 所需的变量
	 */
	public static String MAIL_APPROVE_BILLID = "BILLID";
	public static String MAIL_APPROVE_BILLTYPE = "BILLTYPE";
	public static String MAIL_APPROVE_CHECKNOTE = "CHECKNOTE";
	public static String MAIL_APPROVE_DS = "DATASOURCE";
	public static String MAIL_APPROVE_PWD = "PASSWORD";
	public static String MAIL_APPROVE_RESULT = "RESULT";
	public static String MAIL_APPROVE_UID = "USERID";
	public static String MAIL_DISPATCH_ID = "DISPATCHID";
	public static String MAIL_SIGNED_APPROVE_UID = "SIGNED_USERID";


}
