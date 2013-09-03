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
 * ���ʼ���������ȡ�ʼ���ʵ���ʼ���ѯ����
 * 
 * @author leijun 2007-7-17
 */
public class MailReceivePlugin implements IPreAlertPlugin {
	@Override
	public PreAlertObject executeTask(PreAlertContext context) throws BusinessException {

		Logger.debug(">>ƽ̨�ʼ�����Ԥ�������ʼִ��=" + new UFDateTime(System.currentTimeMillis()));
		
		//���������ʼ�
		recieveOfflineEmails();
		
		PreAlertObject obj = new PreAlertObject();
		obj.setReturnType(PreAlertReturnType.RETURNNOTHING);
		
		return obj;
	}

	//���������ʼ�
	private void recieveOfflineEmails() throws BusinessException {
		Logger.debug(">>ƽ̨�ʼ�����Ԥ�����:���������ʼ�");
		IMailApproveService mailService = NCLocator.getInstance().lookup(IMailApproveService.class);
		List<MailApproveResult> mailApproveResultList = mailService.fetchMailApproveResults();
		Logger.debug(">>ƽ̨�ʼ�����Ԥ�����:���������ʼ�������" + mailApproveResultList.size() + "ʱ�䣺"+ new UFDateTime(System.currentTimeMillis()));
		for (MailApproveResult approveResult : mailApproveResultList) {
			MailHandler.getInstance().emailMsgReceived(approveResult);
		}
	}
}
