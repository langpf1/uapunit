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
 * �����ʼ��Ͷ��ŵĺ�̨������
 * <li>ע�⣺ֻ�ɺ�̨����
 *
 */
public class PfMailAndSMSUtil {
	/**
	 * ����Ա�����л�ȡ����Ա���ֻ���Ϣ�����ڶ��ŷ���
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
																												 * �û�û�й�����Ա����
																												 * ��
																												 * �������
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
	 * ���;����������ܵ�HTML�ʼ���������Ϣ���п���
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

		// ���;����������ܵ�HTML�ʼ���������Ϣ���п���
		String subject = MessageVO.getMessageNoteAfterI18N(topic);
//		subject += "(�ɴ򿪸�����������)";
		subject = getEncodedString(subject);
		
		//==================�������������ʼ�-START======================
		sendOfflineApproveEmail(billHtml, email, billId, billtype, checkman, subject);
		//=====================�������������ʼ�-END======================
		
//		String htmlContentNoApprove = mailTemplateHasBillWithoutApprove(billHtml, billId);
//		String htmlContentWithApprove = mailTemplateHasBillWithApprove(billHtml, checkman, billtype,
//				billId, assignInfos);
//
//		// ������ʱ����
//		String fileName = RuntimeEnv.getInstance().getNCHome() + "/webapps/nc_web/pf/" + billId
//				+ ".html";
//		try {
//			writeTempFile(fileName, htmlContentWithApprove);
//		} catch (IOException e) {
//			Logger.error(e.getMessage(), e);
//		}
//		// �������������ܵ��ʼ�����������������
//		sendHtmlEmail(subject, new String[] { email }, htmlContentNoApprove, fileName);
//
//		// ɾ����ʱ�ʼ������ļ�
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
	 * �������������ܵ��ʼ���������Ϣ���п���
	 * <li>����е�����Ϣ����ΪHTML�ʼ���
	 * <li>����޵�����Ϣ����Ϊ�ı��ʼ�
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
			// ���ͼ򵥸�ʽ���ʼ�������������
			sendEmail(subject, NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000000")/*�뼰ʱ��¼����!*/, strEmails, null, null);
		} else {
			// ����HTML�������ݸ�ʽ���ʼ�������������
			sendHtmlEmail(subject, strEmails, mailTemplateHasBillWithoutApprove(billHtml, billId), null);
		}
	}

	/**
	 * ����ģ��HTML�ļ����滻���еĵ�����Ϣ
	 * <li>����������
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
		// �Ӻ�̨�ļ���ȡ�ʼ�ģ��
		String templateURL = RuntimeEnv.getInstance().getNCHome() + "/webapps/nc_web/pf";
		File f = new File(templateURL + "/mail_template.htm");
		String strFile = null;
		try {
			strFile = IOUtil.toString(new BufferedReader(new FileReader(f)));
		} catch (FileNotFoundException e) {
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000001")/*>>�����Ҳ���webapps/nc_web/�µ��ʼ�ģ���ļ�*/);
		} catch (IOException e) {
			throw new PFBusinessException(e.getMessage(), e);
		}

		if (StringUtil.isEmptyWithTrim(strFile))
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000002")/*>>���󣺶�ȡwebapps/nc_web/�µ��ʼ�ģ���ļ�Ϊ��*/;

		// XXX::���Web��������ַ
		String webUrl = ServerConfiguration.getServerConfiguration().getDefWebServerURL();
		String pf_web_url = webUrl + "/pf";

		// �滻����ͼƬ��URL
		strFile = StringUtil.replaceAllString(strFile, "#PF_WEB_URL#", pf_web_url);

		// �滻������Ϣ
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

		// �滻��ʷ������Ϣ
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
	 * ����ģ��HTML�ļ����滻���еĵ�����Ϣ�Լ�������Ϣ
	 * <li>����������
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
		// �Ӻ�̨�ļ���ȡ�ʼ�ģ��
		String templateURL = RuntimeEnv.getInstance().getNCHome() + "/webapps/nc_web/pf";
		String fileName = null;
		// XXX:�ж��ʼ�������Ҫ����ǩ��
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
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000001")/*>>�����Ҳ���webapps/nc_web/�µ��ʼ�ģ���ļ�*/);
		} catch (IOException e) {
			throw new PFBusinessException(e.getMessage(), e);
		}

		if (StringUtil.isEmptyWithTrim(strFile))
			return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000002")/*>>���󣺶�ȡwebapps/nc_web/�µ��ʼ�ģ���ļ�Ϊ��*/;

		// XXX::���Web��������ַ
		String webUrl = ServerConfiguration.getServerConfiguration().getDefWebServerURL();
		String pf_web_url = webUrl + "/pf";
		// �滻����ͼƬ��URL
		strFile = StringUtil.replaceAllString(strFile, "#PF_WEB_URL#", pf_web_url);

		// �滻������Ϣ
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

		// �滻����action
		String strActionServlet = webUrl + "/service/mailapproveserv";
		strFile = StringUtil.replaceAllString(strFile, "#ACTIONSERVLET_URL#", strActionServlet);

		// �滻������
		// ��uid/pwd/billid/billtype/ds��Ϊ5�������ֶ�
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

		// �滻��ʷ������Ϣ
		String tagHistoryBegin = "<!--APPROVE_HISTORY_BEGIN-->";
		String tagHistoryEnd = "<!--APPROVE_HISTORY_END-->";
		index = strFile.indexOf(tagHistoryBegin);
		int indexEnd = strFile.indexOf(tagHistoryEnd);
		String strHistory = strFile.substring(index + tagHistoryBegin.length(), indexEnd);
		strFile = strFile.substring(0, index + tagHistoryBegin.length())
				+ getNoteOfBill(strHistory, billId) + strFile.substring(indexEnd);

		// �滻ָ����Ϣ
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
	 * ��ѯ�õ��ݵ���ʷ������Ϣ����ƴ�յ�HTML��
	 * 
	 * @param strHistory
	 * @param billId
	 * @return
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private static String getNoteOfBill(String strHistory, String billId) throws BusinessException, DbException {
		// ��ѯ�õ��ݵ���ʷ������Ϣ
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
			// XXX:��һ������Ϊ�к�

			// ���ù������Ƿ�Ϊ�޵�
			String strActiontype = String.valueOf(rowData.get(7));
			boolean isMakebill = false;
			if (WorkflownoteVO.WORKITEM_TYPE_MAKEBILL.equalsIgnoreCase(strActiontype))
				isMakebill = true;

			// ��������״��
			int status = ((Integer) rowData.get(4)).intValue();
			String strStatus = resolveApproveStatus(status, isMakebill);

			// ����������
			String strResultI18N = resolveApproveResult(isMakebill ? null : rowData.get(5));

			// �����������ڼ�����ʱ
			//String strSendTime = String.valueOf(rowData.get(6));
			//String strApproveTime = String.valueOf(rowData.get(1));
			// UFDateTime serverTime = ClientEnvironment.getServerTime();
			// String ellapsed = DurationUnit.getElapsedTime(new
			// UFDateTime(strSendTime), new UFDateTime(strApproveTime));

			// �ֱ�Ϊ���������ڡ������ˡ�����״�����������������
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
	 * ��ö��ﻯ���������
	 * 
	 * @param result
	 */
	private static String resolveApproveResult(Object result) {
		String obj = null;
		if (result == null) {
			obj = "";
		} else if (result.equals("Y")) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("102220",
					"UPP102220-000161")/* @res "���ͨ��" */;
		} else if (result.equals("N")) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("102220",
					"UPP102220-000160")/* @res "��˲�ͨ��" */;
		} else if (result.equals("R")) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("102220",
					"UPP102220-000195")/* @res "����" */;
		} else {
			obj = String.valueOf(result);
		}
		return obj;
	}

	/**
	 * ��ö��ﻯ������״��
	 * 
	 * @param iStatus
	 * @param isMakebill �Ƿ�Ϊ�޵�������
	 * @param isCheckMsg
	 */
	private static String resolveApproveStatus(int iStatus, boolean isMakebill) {
		String obj = null;
		if (iStatus == TaskInstanceStatus.Finished.getIntValue()) {
			if (isMakebill) {
				obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000011")/*���޵� */;
			} else {
				obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000198")/*@res"������"*/;
			}
		} else if (iStatus == TaskInstanceStatus.Inefficient.getIntValue()) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("common", "UC001-0000005")/*@res"����"*/;
		} else if (iStatus == TaskInstanceStatus.Started.getIntValue()) {
			obj = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "UPPpfworkflow-000199")/*@res"δ����"*/;
		} else {
			obj = "";
		}
		return obj;
	}
	
	/**
	 * ����HTML�ʼ����ݣ����ɴ�����
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
		Logger.info(">> ����HTML�ʼ�PfMailAndSMSUtil.sendHtmlEmail() called");
		Logger.info("==================");

		SysMessageParam smp = WirelessManager.fetchSysMsgParam();
		if (smp == null)
			throw new MailException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000003")/*����1��û����ȷ�����ʼ��������ļ�/ierp/bin/message4pf.xml*/);
		DefaultSMTP defaultSmtp = smp.getSmtp();
		if (defaultSmtp == null)
			throw new MailException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000004")/*����2��û����ȷ�����ʼ��������ļ�/ierp/bin/message4pf.xml*/);

		String smtpHost = defaultSmtp.getSmtp();
		String fromAddr = defaultSmtp.getSender();
		String userName = defaultSmtp.getUser();
		String password = defaultSmtp.getPassword();
		String senderName = defaultSmtp.getSenderName();
		
		if (!StringUtil.isEmptyWithTrim(password)) {
			password = new Encode().decode(password);
		}
		
		// XXX::���յ�ַ������","�ָ�
		StringBuffer receivers = new StringBuffer();
		for (int i = 0; i < recEmails.length; i++) {
			receivers.append(recEmails[i]);
			receivers.append(",");
		}

		try {
			StringBuffer sb = new StringBuffer();
			sb.append(htmlContent);
			Logger.info("�ʼ�������receivers=" + receivers);
			MailTool.sendHtmlEmail(smtpHost, fromAddr, senderName, userName, password, receivers.toString(),
					subject, sb, fileName);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfMailAndSMSUtil-000005", null, new String[]{e.getMessage()})/*���󣺷���Emailʧ�ܣ�{0}*/);
		}

	}

	/**
	 * �����ʼ��Ĺ��߷�������EJB����
	 * <li>ֻ�ɺ�̨����
	 * <li>�ʼ���������Ϣ������message4pf.xml�ļ���
	 * 
	 * @param title �ʼ�����
	 * @param content �ʼ�����
	 * @param recEmails �������ʼ���ַ
	 * @param ccEmails �������ʼ���ַ
	 * @param attachFiles �����ļ�·��
	 * @throws MailException
	 */
	public static void sendEmail(String title, String content, String[] recEmails, String[] ccEmails,
			String[] attachFiles) throws MailException {
		Logger.info("==================");
		Logger.info(">> �����ʼ�PfMailAndSMSUtil.sendEmail() called");
		Logger.info("==================");
		if (recEmails == null || recEmails.length == 0)
			return;
		
		// XXX:���յ�ַ������","�ָ�
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
		Logger.info("�ʼ�������receivers=" + receivers);

		IMailApproveService mailService = NCLocator.getInstance().lookup(IMailApproveService.class);
		try {
			mailService.sendNormalMail(receivers.toString(), title, content, false);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			throw new MailException();
		}
	}

	

	/**
	 * �����ʼ��Ĺ����࣬��EJB����
	 * <li>ֻ�ɺ�̨����
	 * <li>ʹ�õ��������첽�����ʼ�
	 * 
	 * @param em �ʼ����������Ϣ
	 * @throws BusinessException
	 */
	public static void sendEMS(EmailMsg em) throws BusinessException {
		Logger.info("========================");
		Logger.info(">> ʹ�õ��������첽�����ʼ�");
		Logger.info("========================");
		String[] userIds = em.getUserIds();
		if (userIds == null || userIds.length == 0) {
			Logger.warn(">>�Ҳ��������ˣ��޷������ʼ�������");
			return;
		}

		// /Ӧ��ʹ�õ���������ִ���ʼ���������
		ITaskManager tmgr = (ITaskManager) NCLocator.getInstance().lookup(ITaskManager.class.getName());
		PfEmailSendTask task = new PfEmailSendTask(em);
		tmgr.add(task, TaskPriority.GENERAL);
	}

	/**
	 * ���Ͷ���Ϣ�Ĺ����࣬��EJB����
	 * <li>ֻ�ɺ�̨����
	 * <li>ʹ�õ��������첽���Ͷ���
	 * <li>�����ҪEJB���ã���ʹ��IPFMessage�������
	 * 
	 * @param userIds �����û�PK����
	 * @param content ��������
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
	 * ���Ͷ���Ϣ�Ĺ����࣬��EJB����
	 * <li>ֻ�ɺ�̨����
	 * <li>ʹ�õ��������첽���Ͷ���
	 * <li>�����ҪEJB���ã���ʹ��IPFMessage�������
	 * 
	 * @param mobileVO ���ŷ��������Ϣ
	 * @throws BusinessException
	 */
	public static void sendSMS(MobileMsg mobileVO) throws BusinessException {
		Logger.info("========================");
		Logger.info(">> ʹ�õ��������첽���Ͷ���");
		Logger.info("========================");
		String[] targetPhones = mobileVO.getTargetPhones();
		if (targetPhones == null || targetPhones.length == 0) {
			Logger.warn(">>�Ҳ����ֻ��ţ��޷����Ͷ��ţ�����");
			return;
		}

		// /Ӧ��ʹ�õ���������ִ�ж��ŷ�������
		ITaskManager tmgr = (ITaskManager) NCLocator.getInstance().lookup(ITaskManager.class.getName());
		PfMobileSendTask task = new PfMobileSendTask(mobileVO);
		tmgr.add(task, TaskPriority.GENERAL);

	}

	/**
	 * �ʼ����� ����ı���
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
