package uap.workflow.pub.app.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.app.action.IPFActionName;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.app.query.WorkflowQueryUtil;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.pub.app.mail.vo.EmailMsg;
import uap.workflow.pub.app.message.IPFMessage;
import uap.workflow.pub.app.mobile.PfMailAndSMSUtil;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.InvocationInfo;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.scheduler.ITask;
import nc.bs.uap.scheduler.ITaskBody;
import nc.bs.uap.scheduler.ITaskJudger;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.rbac.IUserManageQuery;
import nc.jdbc.framework.exception.DbException;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.sm.UserVO;
import nc.vo.uap.scheduler.TaskStatus;
import nc.vo.uap.scheduler.TimeConfigVO;
import nc.vo.wfengine.core.util.DateUtilities;

/**
 * ƽ̨�ʼ��������� <li>�ɵ�������ִ��
 * 
 * @author leijun 2008-12
 * @since 5.5
 */
public class PfEmailSendTask implements ITask {

	private EmailMsg emailMsg;

	private TimeConfigVO m_tc;

	public PfEmailSendTask(EmailMsg em) {
		super();
		this.emailMsg = em;
	}

	public String getName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfEmailSendTask-000000")/* ����ƽ̨�ʼ��������� */;
	}

	public String getType() {
		return "PfEmailSendTask";
	}

	public ITaskJudger getTaskJudger() {
		// TODO Auto-generated method stub
		return null;
	}

	public ITaskBody getTaskBody() {
		return new PfEmailSendTaskBody();
	}

	public TimeConfigVO getTimeConfig() {
		if (m_tc == null) {
			m_tc = new TimeConfigVO();
			m_tc.setJustInTime(true, System.currentTimeMillis() + 2000);
			// ��ʱ�����ȴ�2sִ�У��Ա���빤������������ύ�������ڻ�ȡָ����Ϣʱ�ò����ù�����
		}
		return m_tc;
	}

	/**
	 * �����ʼ�����ִ����
	 */
	class PfEmailSendTaskBody implements ITaskBody {

		public void execute() throws BusinessException {
			Logger.debug(">>�ʼ�����������:PfEmailSendTaskBody.execute() called");
			Logger.debug("ʹ��������Ϣ=" + emailMsg.getLangCode());
			Logger.debug("ʹ������Դ��Ϣ=" + emailMsg.getDatasource());
			
			try {
				// yk1+ ˯��5s����ֹ���������ĳ�����δ�ύ���±��߳�ȡ�����ʼ�����ҵ������
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			InvocationInfo info = emailMsg.getInvocationInfo();

			// FIXME: should mandatoryly set invocation info
			if (info != null) {
				InvocationInfoProxy.getInstance().setBizDateTime(info.getBizDateTime());
				InvocationInfoProxy.getInstance().setGroupId(info.getGroupId());
				InvocationInfoProxy.getInstance().setGroupNumber(info.getGroupNumber());
				InvocationInfoProxy.getInstance().setLangCode(info.getLangCode());
				InvocationInfoProxy.getInstance().setUserDataSource(info.getUserDataSource());
				InvocationInfoProxy.getInstance().setUserId(info.getUserId());
			} else {
				// should deprecatedc
				InvocationInfoProxy.getInstance().setLangCode(emailMsg.getLangCode());
				InvocationInfoProxy.getInstance().setUserDataSource(emailMsg.getDatasource());
			}

			MailModal mailModal = emailMsg.getMailModal();
			try {
				if (MailModal.MAIL_INFO_INT == mailModal.getValue()) {
					Logger.debug("�ʼ�֪ͨ���޽������ܵ��ʼ�����������");
					mailNoApprove();
				} else if (MailModal.MAIL_APPROVE_INT == mailModal.getValue()) {
					Logger.debug("�ʼ��������н������ܵ��ʼ������뵥������ÿ���û�");
					mailWithApprove();
				}
			} catch (DbException e) {
				Logger.error(e.getMessage());
				throw new BusinessException(e.getMessage());
			}
			Logger.debug(">>�ʼ�����������:PfEmailSendTaskBody.execute() end");
		}

		public void cancelExecute() throws BusinessException {
		}

		public TaskStatus getStatus() {
			return null;
		}

	}

	/**
	 * ���������޽������ܵ��ʼ�
	 * 
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private void mailNoApprove() throws BusinessException, DbException {
		Logger.debug(">>mailNoApprove() called");
		Logger.debug(">>EmailMsg.billNo=" + emailMsg.getBillNo());

		IUserManageQuery umq = NCLocator.getInstance().lookup(IUserManageQuery.class);
		IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);

		ArrayList alEmails = new ArrayList();
		String[] userIds = emailMsg.getUserIds();
		for (int i = 0; i < userIds.length; i++) {
			Logger.debug(">>userIds[" + i + "]=" + userIds[i]);
			UserVO uservo = umq.getUser(userIds[i]);
			PsndocVO psndoc = null;
			// �������û�������
			if (uservo.getPk_base_doc() != null) {
				psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, uservo.getPk_base_doc());
			}
			String email = psndoc == null ? null : psndoc.getEmail();
			if (StringUtil.isEmptyWithTrim(email)) {
				Logger.error("**�Ҳ����û�=" + userIds[i] + ",��Ӧ���ʼ���ַ");
				continue;
			}
			alEmails.add(email);
		}
		if (alEmails.size() > 0) {
			Logger.debug("��鵥������ע���������������Ƿ�ʵ���˴�ӡ����Դ��ȡ�ӿ�");
			String billHtml = NCLocator.getInstance().lookup(IPFMessage.class).generateBillHtml(emailMsg.getBillId(), emailMsg.getBillType(), emailMsg.getPrintTempletId(), emailMsg.getSenderman());

			Logger.debug(">>�������������ʼ�=" + Arrays.asList(alEmails));
			PfMailAndSMSUtil.sendEmailsWithoutApprove(billHtml, (String[]) alEmails.toArray(new String[0]), emailMsg.getTopic(), emailMsg.getBillId());
		} else {
			Logger.debug("û���ҵ��ʼ���ַ�����跢��");
		}
	}

	/**
	 * ѭ�����ͽ������ܵ��ʼ� <li>�����ʼ����޽������ܣ�����
	 * 
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private void mailWithApprove() throws BusinessException, DbException {
		Logger.debug(">>mailWithApprove() called");
		Logger.debug(">>EmailMsg.billNo=" + emailMsg.getBillNo());
		if (TaskInstanceCreateType.Makebill.getIntValue() == emailMsg.getTasktype()) {
			Logger.debug("���͸��Ƶ��˵Ĳ����ʼ������轻������");
			mailNoApprove();
		} else {
			Logger.debug("���͸������˵������ʼ������н�������");
			IUserPubService userService = NCLocator.getInstance().lookup(IUserPubService.class);
			IWorkflowMachine machine = NCLocator.getInstance().lookup(IWorkflowMachine.class);
			IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);

			Logger.debug("��鵥������ע���������������Ƿ�ʵ���˴�ӡ����Դ��ȡ�ӿ�");
			String billHtml = NCLocator.getInstance().lookup(IPFMessage.class).generateBillHtml(emailMsg.getBillId(), emailMsg.getBillType(), emailMsg.getPrintTempletId(), emailMsg.getSenderman());

			String[] userIds = emailMsg.getUserIds();
			for (int i = 0; i < (userIds == null ? 0 : userIds.length); i++) {
				Logger.debug(">>userIds[" + i + "]=" + userIds[i]);
				PsndocVO psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, userService.queryPsndocByUserid(userIds[i]));
				String email = psndoc == null ? null : psndoc.getEmail();
				if (StringUtil.isEmptyWithTrim(email)) {
					Logger.error("**�Ҳ����û�=" + userIds[i] + ",��Ӧ���ʼ���ַ");
					continue;
				}

				// 1.��õ��ݾۺ�VO
				AggregatedValueObject billVo = WorkflowQueryUtil.fetchBillVO(emailMsg.getBillType(), emailMsg.getBillId());
				
				if (billVo == null)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfEmailSendTask-000001")/*
																																	 * ����
																																	 * ��
																																	 * ���ݵ������ͺ͵���ID��ȡ�������ݾۺ�VO
																																	 */);

				// 2.��ù���������������
				java.util.Date date = new java.util.Date();
				String currentDate = DateUtilities.getInstance().formatJustDay(date);

				// XXX:���ָ����Ϣ
				Logger.debug(">>���ָ����Ϣ");
				HashMap hmPfExParams = new HashMap();
				hmPfExParams.put(PfUtilBaseTools.PARAM_NO_LOCK, PfUtilBaseTools.PARAM_NO_LOCK);
				WorkflownoteVO worknoteVO = machine.checkWorkFlow(IPFActionName.APPROVE + userIds[i], emailMsg.getBillType(), billVo, hmPfExParams);

				Logger.debug(">>���������ʼ�=" + email);
				PfMailAndSMSUtil.sendEmailWithApprove(billHtml, email, emailMsg.getBillId(), emailMsg.getBillType(), emailMsg.getTopic(), userIds[i], worknoteVO == null ? null : worknoteVO.getAssignableInfos());
			}// {end for}
		}
	}
}
