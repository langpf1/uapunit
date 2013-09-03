package uap.workflow.pub.app.mail;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.sf.excp.MailException;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.mailapprove.pub.MailApproveResult;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import uap.workflow.app.client.PfUtilTools;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.pub.app.mobile.PfMailAndSMSUtil;

public class MailHandler {
	
	private static MailHandler instance = new MailHandler();
	
	/**
	 * Ĭ�Ϲ��췽��
	 */
	private MailHandler() {
		super();
	}

	public static MailHandler getInstance() {
		return instance;
	}
	
	public void emailMsgReceived(MailApproveResult approveResult) {
		String uid = approveResult.getUserID();
		String pwd = approveResult.getUserPWD();
		String result = approveResult.getApproveResult();
		String note = approveResult.getApproveContent();
		String billid = approveResult.getBillID();
		String billtype = approveResult.getBillType();
		String ds = approveResult.getDsName();
		String emailAddr = approveResult.getToMail();
		String[] dispatched_ids = null;
		
		String strMsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MailHandler-000000")/*NCϵͳ�Ѿ��ɹ��������������*/;
		try {
			//��������־
			logParams(uid, pwd, result, note, billid, billtype, ds);

			//XXX:����������ǩ����֤

			//ע������Դ����ǰServlet�߳�
			PfUtilTools.regDataSourceForServlet(ds);

			//У�������Ƿ���ȷ
			checkPassword(uid, pwd);

			//��̨��������
			PfUtilTools.approveSilently(billtype, billid, result, note, uid, dispatched_ids);
			PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### ����NC����ƽ̨���ʼ�֪ͨ*/, strMsg, new String[] { emailAddr }, null, null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			try {
				PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### ����NC����ƽ̨���ʼ�֪ͨ*/, e.getMessage(), new String[] { emailAddr },
						null, null);
			} catch (MailException ex) {
				//XXX:����
			}
		}
	}
	
	private void logParams(String uid, String pwd, String result, String note, String billid,
			String billtype, String ds) {
		Logger.debug("************MailHandler�������������ʼ�**************");
		Logger.debug(">>usercode=" + uid + "\n");
		Logger.debug(">>pwd=" + pwd + "\n");
		Logger.debug(">>result=" + result + "\n");
		Logger.debug(">>note=" + note + "\n");
		Logger.debug(">>billid=" + billid + "\n");
		Logger.debug(">>billtype=" + billtype + "\n");
		Logger.debug(">>ds=" + ds + "\n");
	}
	
	/**
	 * У��������Ч��
	 * @param uid
	 * @param pwd
	 * @throws BusinessException
	 */
	private void checkPassword(String uid, String pwd) throws BusinessException {
		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO userVO = umq.getUser(uid);
		String strPwd = userVO.getUser_password();
		if (!strPwd.equalsIgnoreCase(pwd))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MailHandler-000001")/*�����û����벻��ȷ*/);
	}

}
