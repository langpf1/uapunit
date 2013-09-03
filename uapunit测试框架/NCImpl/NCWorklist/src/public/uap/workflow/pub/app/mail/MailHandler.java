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
	 * 默认构造方法
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
		
		String strMsg = NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MailHandler-000000")/*NC系统已经成功处理了你的请求。*/;
		try {
			//将参数日志
			logParams(uid, pwd, result, note, billid, billtype, ds);

			//XXX:服务器数字签名验证

			//注册数据源到当前Servlet线程
			PfUtilTools.regDataSourceForServlet(ds);

			//校验密码是否正确
			checkPassword(uid, pwd);

			//后台审批单据
			PfUtilTools.approveSilently(billtype, billid, result, note, uid, dispatched_ids);
			PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### 来自NC流程平台的邮件通知*/, strMsg, new String[] { emailAddr }, null, null);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			try {
				PfMailAndSMSUtil.sendEmail(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MobileHandler-000000")/*### 来自NC流程平台的邮件通知*/, e.getMessage(), new String[] { emailAddr },
						null, null);
			} catch (MailException ex) {
				//XXX:忽略
			}
		}
	}
	
	private void logParams(String uid, String pwd, String result, String note, String billid,
			String billtype, String ds) {
		Logger.debug("************MailHandler处理离线审批邮件**************");
		Logger.debug(">>usercode=" + uid + "\n");
		Logger.debug(">>pwd=" + pwd + "\n");
		Logger.debug(">>result=" + result + "\n");
		Logger.debug(">>note=" + note + "\n");
		Logger.debug(">>billid=" + billid + "\n");
		Logger.debug(">>billtype=" + billtype + "\n");
		Logger.debug(">>ds=" + ds + "\n");
	}
	
	/**
	 * 校验密码有效性
	 * @param uid
	 * @param pwd
	 * @throws BusinessException
	 */
	private void checkPassword(String uid, String pwd) throws BusinessException {
		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
		UserVO userVO = umq.getUser(uid);
		String strPwd = userVO.getUser_password();
		if (!strPwd.equalsIgnoreCase(pwd))
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "MailHandler-000001")/*错误：用户密码不正确*/);
	}

}
