package uap.workflow.app.taskhandling;

import java.util.ArrayList;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.msg.MessageVO;
import uap.workflow.app.taskhandling.ITaskHandlingAdapter;
import uap.workflow.app.taskhandling.TaskHandlingContext;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.XPDLNames;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.PfMessageUtil;
import uap.workflow.pub.app.mobile.IPFMobileCommand;
import uap.workflow.pub.app.mobile.IPFMobileSequence;
import uap.workflow.pub.app.mobile.MobileModal;
import uap.workflow.pub.app.mobile.PfMailAndSMSUtil;
import uap.workflow.pub.app.mobile.vo.MobileData;
import uap.workflow.pub.app.mobile.vo.MobileMsg;


/** 
   短消息工作任务处理实现类
 * @author 
 */
public class SMSAdapter implements ITaskHandlingAdapter{

	@Override
	public void send(TaskInstanceVO taskInstanceVO) {
		try {
			sendMobileAfterAssignment(taskInstanceVO);
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			// 异常吞掉？
		}
	}

	@Override
	public void receive(TaskInstanceVO taskInstanceVO) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 分配完审批流工作项后发送邮件或短信 <li>是否发送邮件或短信，由流程定义决定
	 * 
	 * @param userIds
	 * @param task
	 * @throws BusinessException
	 * @throws DbException
	 */
	private void sendMobileAfterAssignment(TaskInstanceVO task)throws BusinessException{

		Logger.debug(">>分配完审批流工作项后发送邮件或短信 开始");

		// 环节上定义的工作项接收方式
		IProcessDefinition processDefinition = ProcessDefinitionUtil.getProDefByProDefPk(task.getPk_process_def());
		String ptId = null;//bwp.getMailPrintTemplet().getTempletid();
		MobileModal mobileModal = null;//bwp.getMobileModal();

		// 环节上定义的工作项接收方式
		IActivity activity = processDefinition.findActivity(task.getActivity_id());
		Object mobileModalObj = activity.getProperty(XPDLNames.MOBILE_MODAL);
		if (mobileModalObj!=null && !MobileModal.BLANK.getTag().equals(mobileModalObj)) {
			mobileModal = MobileModal.fromString((String) mobileModalObj);
		}

		String[] targetPhones = null;
		if (needSendMobile(mobileModal)) {
			Logger.debug(">>>发送邮件开始");
			String messageReceiver = task.getPk_agenter();
			if(StringUtil.isEmptyWithTrim(messageReceiver))
			{
				messageReceiver = task.getPk_owner();
			}
			String[] users = new String[1];
			users[0]=messageReceiver;
			try {
				targetPhones = PfMailAndSMSUtil.fetchPhonesByUserId(users);
				if (targetPhones == null || targetPhones.length == 0) {
					Logger.warn(">>找不到手机号，无法发送短信，返回");
					return;
				}
			} catch (Exception e) {
				// 获取手机号时出了问题，不应影响流程流转
				Logger.error(e.getMessage(), e);
			}

			MobileMsg mm = new MobileMsg();
			mm.setTargetPhones(targetPhones);
			mm.setMsg(MessageVO.getMessageNoteAfterI18N(PfMessageUtil.getMessageTopic(task,messageReceiver)));

			if (mobileModal.getValue() == MobileModal.MOBILE_APPROVE_INT) {
				// XXX:需要短信审批功能,则保持短信审批的会话ID
				saveSerialNumbers(mm, task);
			} else
				mm.setMsg(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfTaskManager-0002", null, new String[]{mm.getMsg()})/*{0}(注:不可回复)*/);
			// 异步发送短信
			PfMailAndSMSUtil.sendSMS(mm);

		}

		Logger.debug(">>分配完审批流工作项后发送邮件或短信 结束");

	}

	private boolean needSendMobile(MobileModal mobileModal) {
		if (mobileModal == null)
			return false;
		int mobileModalValue = mobileModal.getValue();
		return mobileModalValue != MobileModal.NO_MOBILE_INT && mobileModalValue != MobileModal.BLANK_INT;
	}

	/**
	 * 保持短信审批的会话ID
	 * 
	 * @param mm
	 * @param task
	 * @throws BusinessException
	 */
	private void saveSerialNumbers(MobileMsg mm, TaskInstanceVO task) throws BusinessException {

		Logger.info("WfTaskManager.saveSerialNumbers MobileMsg:" + mm);
		Logger.info("WfTaskManager.saveSerialNumbers WFTask:" + task);
		IPFMobileSequence seq = (IPFMobileSequence) NCLocator.getInstance().lookup(IPFMobileSequence.class.getName());

		ArrayList<MobileData> al = new ArrayList<MobileData>();
		String[] sids = new String[mm.getTargetPhones().length];
		Logger.info("WfTaskManager.saveSerialNumbers sids:" + sids);
		for (int i = 0; i < mm.getTargetPhones().length; i++) {
			String mobile = mm.getTargetPhones()[i];
			MobileData md = new MobileData();
			md.setBillid(task.getPk_form_ins_version());
			md.setBilltype(task.getPk_bizobject());
			md.setCmd(IPFMobileCommand.APPROVE);
			md.setMobile(mobile);

			md.setBizCenterCode(InvocationInfoProxy.getInstance().getBizCenterCode());
			md.setGroupId(InvocationInfoProxy.getInstance().getGroupId());
			md.setGroupNumber(InvocationInfoProxy.getInstance().getGroupNumber());
			md.setHyCode(InvocationInfoProxy.getInstance().getHyCode());
			md.setUserDataSource(InvocationInfoProxy.getInstance().getUserDataSource());
			md.setUserId(InvocationInfoProxy.getInstance().getUserId());
			md.setLangCode(InvocationInfoProxy.getInstance().getLangCode());
			md.setBizDateTime(new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));

			sids[i] = seq.nextStringValue(mobile, 1, 4)[0];
			md.setPk_sid(sids[i]);

			al.add(md);
		}

		Logger.info("WfTaskManager.saveSerialNumbers ArrayList<MobileData>:" + al);
		// 批量插入DB
		BaseDAO dao = new BaseDAO();
		dao.insertVOList(al);
		mm.setSerialNumbers(sids);
	}	
}