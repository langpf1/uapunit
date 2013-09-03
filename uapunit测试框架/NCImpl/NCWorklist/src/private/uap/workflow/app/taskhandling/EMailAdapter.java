package uap.workflow.app.taskhandling;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import uap.workflow.app.taskhandling.ITaskHandlingAdapter;
import uap.workflow.app.taskhandling.TaskHandlingContext;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.XPDLNames;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.mail.MailModal;
import uap.workflow.pub.app.mail.vo.EmailMsg;
import uap.workflow.pub.app.message.PfMessageUtil;
import uap.workflow.pub.app.mobile.PfMailAndSMSUtil;
import uap.workflow.pub.util.PfUtilBaseTools;


/** 
   �ʼ�����������ʵ����
 * @author 
 */
public class EMailAdapter implements ITaskHandlingAdapter{

	@Override
	public void send(TaskInstanceVO taskInstanceVO) {
		try {
			sendEmailAfterAssignment(taskInstanceVO);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			// �쳣�̵���
		}
	}

	@Override
	public void receive(TaskInstanceVO taskInstanceVO) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * ����������������������ʼ������ <li>�Ƿ����ʼ��������̶������
	 * 
	 * @param userIds
	 * @param task
	 * @throws BusinessException
	 * @throws DbException
	 */
	private void sendEmailAfterAssignment(TaskInstanceVO task) throws BusinessException {
		Logger.debug(">>����������������������ʼ���ʼ");
		// �����϶���Ĺ�������շ�ʽ
		IProcessDefinition processDefinition = ProcessDefinitionUtil.getProDefByProDefPk(task.getPk_process_def());
		// ��ѯ���̶����ģ��ID
		String ptId = null;//bwp.getMailPrintTemplet().getTempletid();
		MailModal mailModal = null;//processDefinition.getMailModal();
		IActivity activity = processDefinition.findActivity(task.getActivity_id());
		Object mailModalObj = activity.getProperty(XPDLNames.MAIL_MODAL);
		if (mailModalObj!=null && !MailModal.BLANK.getTag().equals(mailModalObj)) {
			mailModal = MailModal.fromString((String) mailModalObj);
		}
		if (needSendMail(mailModal)) {
			Logger.debug(">>>�����ʼ���ʼ");
			String messageReceiver = task.getPk_agenter();
			if(StringUtil.isEmptyWithTrim(messageReceiver))
			{
				messageReceiver = task.getPk_owner();
			}
			String[] users = new String[1];
			users[0]=messageReceiver;
		
			int iLastTaskType = task.getCreate_type();
			String currentCheckman = iLastTaskType == TaskInstanceCreateType.Makebill.getIntValue() ? null : task.getPk_creater();
			EmailMsg em = new EmailMsg();
			em.setMailModal(mailModal);
			em.setUserIds(users);
			em.setBillId(task.getPk_form_ins_version());
			em.setBillNo(task.getForm_no());
			em.setBillType(PfUtilBaseTools.getRealBilltype(task.getPk_bizobject()));
			em.setPrintTempletId(ptId);
			em.setTopic(PfMessageUtil.getMessageTopic(task, messageReceiver));
			em.setSenderman(currentCheckman);
			em.setTasktype(task.getCreate_type());
			em.setInvocationInfo(getInvocationInfo());
			// �첽�����ʼ�
			PfMailAndSMSUtil.sendEMS(em);
		}
		
		Logger.debug(">>����������������������ʼ� ����");

	}

	private InvocationInfo getInvocationInfo() {
		InvocationInfo info = new InvocationInfo();

		info.setBizDateTime(InvocationInfoProxy.getInstance().getBizDateTime());
		info.setGroupId(InvocationInfoProxy.getInstance().getGroupId());
		info.setGroupNumber(InvocationInfoProxy.getInstance().getGroupNumber());
		info.setLangCode(InvocationInfoProxy.getInstance().getLangCode());
		info.setUserDataSource(InvocationInfoProxy.getInstance().getUserDataSource());
		info.setUserId(InvocationInfoProxy.getInstance().getUserId());

		return info;
	}

	private boolean needSendMail(MailModal mailModal) {
		if (mailModal == null)
			return false;
		int mailModalValue = mailModal.getValue();
		return mailModalValue != MailModal.NO_MAIL_INT && mailModalValue != MailModal.BLANK_INT;
	}
}