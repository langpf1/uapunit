package uap.workflow.app.email;

import java.util.List;

import uap.workflow.pub.app.mail.MailHandler;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.pa.IPreAlertPlugin;
import nc.bs.pub.pa.PreAlertContext;
import nc.bs.pub.pa.PreAlertObject;
import nc.bs.pub.pa.PreAlertReturnType;
import nc.mailapprove.pub.IMailApproveService;
import nc.mailapprove.pub.MailApproveResult;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

/**
 * 从邮件服务器读取邮件，实现邮件查询功能
 * 
 * @author leijun 2007-7-17
 */
public class MailReceivePlugin implements IPreAlertPlugin {
	@Override
	public PreAlertObject executeTask(PreAlertContext context) throws BusinessException {

		Logger.debug(">>平台邮件接收预警插件开始执行=" + new UFDateTime(System.currentTimeMillis()));
		
		//接收离线邮件
		recieveOfflineEmails();
		
		PreAlertObject obj = new PreAlertObject();
		obj.setReturnType(PreAlertReturnType.RETURNNOTHING);
		
		return obj;
	}

	//接收离线邮件
	private void recieveOfflineEmails() throws BusinessException {
		Logger.debug(">>平台邮件接收预警插件:接收离线邮件");
		IMailApproveService mailService = NCLocator.getInstance().lookup(IMailApproveService.class);
		List<MailApproveResult> mailApproveResultList = mailService.fetchMailApproveResults();
		Logger.debug(">>平台邮件接收预警插件:接收离线邮件数量：" + mailApproveResultList.size() + "时间："+ new UFDateTime(System.currentTimeMillis()));
		for (MailApproveResult approveResult : mailApproveResultList) {
			MailHandler.getInstance().emailMsgReceived(approveResult);
		}
	}
}
