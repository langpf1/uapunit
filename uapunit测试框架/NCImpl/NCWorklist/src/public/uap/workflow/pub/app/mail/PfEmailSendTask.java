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
 * 平台邮件发送任务 <li>由调度引擎执行
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
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfEmailSendTask-000000")/* 流程平台邮件发送任务 */;
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
			// 即时，但等待2s执行，以便插入工作项的事务先提交，否则在获取指派信息时得不到该工作项
		}
		return m_tc;
	}

	/**
	 * 发送邮件任务执行体
	 */
	class PfEmailSendTaskBody implements ITaskBody {

		public void execute() throws BusinessException {
			Logger.debug(">>邮件发送任务体:PfEmailSendTaskBody.execute() called");
			Logger.debug("使用语种信息=" + emailMsg.getLangCode());
			Logger.debug("使用数据源信息=" + emailMsg.getDatasource());
			
			try {
				// yk1+ 睡眠5s，防止审批动作的长事务未提交导致本线程取不到邮件审批业务数据
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
					Logger.debug("邮件通知：无交互功能的邮件，批量发送");
					mailNoApprove();
				} else if (MailModal.MAIL_APPROVE_INT == mailModal.getValue()) {
					Logger.debug("邮件审批：有交互功能的邮件，必须单独发给每个用户");
					mailWithApprove();
				}
			} catch (DbException e) {
				Logger.error(e.getMessage());
				throw new BusinessException(e.getMessage());
			}
			Logger.debug(">>邮件发送任务体:PfEmailSendTaskBody.execute() end");
		}

		public void cancelExecute() throws BusinessException {
		}

		public TaskStatus getStatus() {
			return null;
		}

	}

	/**
	 * 批量发送无交互功能的邮件
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
			// 关联了用户档案。
			if (uservo.getPk_base_doc() != null) {
				psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, uservo.getPk_base_doc());
			}
			String email = psndoc == null ? null : psndoc.getEmail();
			if (StringUtil.isEmptyWithTrim(email)) {
				Logger.error("**找不到用户=" + userIds[i] + ",对应的邮件地址");
				continue;
			}
			alEmails.add(email);
		}
		if (alEmails.size() > 0) {
			Logger.debug("检查单据类型注册的审批流检查类是否实现了打印数据源获取接口");
			String billHtml = NCLocator.getInstance().lookup(IPFMessage.class).generateBillHtml(emailMsg.getBillId(), emailMsg.getBillType(), emailMsg.getPrintTempletId(), emailMsg.getSenderman());

			Logger.debug(">>发送无审批的邮件=" + Arrays.asList(alEmails));
			PfMailAndSMSUtil.sendEmailsWithoutApprove(billHtml, (String[]) alEmails.toArray(new String[0]), emailMsg.getTopic(), emailMsg.getBillId());
		} else {
			Logger.debug("没有找到邮件地址，无需发送");
		}
	}

	/**
	 * 循环发送交互功能的邮件 <li>驳回邮件（无交互功能）除外
	 * 
	 * @throws BusinessException
	 * @throws DbException 
	 */
	private void mailWithApprove() throws BusinessException, DbException {
		Logger.debug(">>mailWithApprove() called");
		Logger.debug(">>EmailMsg.billNo=" + emailMsg.getBillNo());
		if (TaskInstanceCreateType.Makebill.getIntValue() == emailMsg.getTasktype()) {
			Logger.debug("发送给制单人的驳回邮件，无需交互功能");
			mailNoApprove();
		} else {
			Logger.debug("发送给审批人的审批邮件，具有交互功能");
			IUserPubService userService = NCLocator.getInstance().lookup(IUserPubService.class);
			IWorkflowMachine machine = NCLocator.getInstance().lookup(IWorkflowMachine.class);
			IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);

			Logger.debug("检查单据类型注册的审批流检查类是否实现了打印数据源获取接口");
			String billHtml = NCLocator.getInstance().lookup(IPFMessage.class).generateBillHtml(emailMsg.getBillId(), emailMsg.getBillType(), emailMsg.getPrintTempletId(), emailMsg.getSenderman());

			String[] userIds = emailMsg.getUserIds();
			for (int i = 0; i < (userIds == null ? 0 : userIds.length); i++) {
				Logger.debug(">>userIds[" + i + "]=" + userIds[i]);
				PsndocVO psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, userService.queryPsndocByUserid(userIds[i]));
				String email = psndoc == null ? null : psndoc.getEmail();
				if (StringUtil.isEmptyWithTrim(email)) {
					Logger.error("**找不到用户=" + userIds[i] + ",对应的邮件地址");
					continue;
				}

				// 1.获得单据聚合VO
				AggregatedValueObject billVo = WorkflowQueryUtil.fetchBillVO(emailMsg.getBillType(), emailMsg.getBillId());
				
				if (billVo == null)
					throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "PfEmailSendTask-000001")/*
																																	 * 错误
																																	 * ：
																																	 * 根据单据类型和单据ID获取不到单据聚合VO
																																	 */);

				// 2.获得工作项并设置审批意见
				java.util.Date date = new java.util.Date();
				String currentDate = DateUtilities.getInstance().formatJustDay(date);

				// XXX:获得指派信息
				Logger.debug(">>获得指派信息");
				HashMap hmPfExParams = new HashMap();
				hmPfExParams.put(PfUtilBaseTools.PARAM_NO_LOCK, PfUtilBaseTools.PARAM_NO_LOCK);
				WorkflownoteVO worknoteVO = machine.checkWorkFlow(IPFActionName.APPROVE + userIds[i], emailMsg.getBillType(), billVo, hmPfExParams);

				Logger.debug(">>发送审批邮件=" + email);
				PfMailAndSMSUtil.sendEmailWithApprove(billHtml, email, emailMsg.getBillId(), emailMsg.getBillType(), emailMsg.getTopic(), userIds[i], worknoteVO == null ? null : worknoteVO.getAssignableInfos());
			}// {end for}
		}
	}
}
