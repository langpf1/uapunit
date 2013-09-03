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
   ����Ϣ����������ʵ����
 * @author 
 */
public class SMSAdapter implements ITaskHandlingAdapter{

	@Override
	public void send(TaskInstanceVO taskInstanceVO) {
		try {
			sendMobileAfterAssignment(taskInstanceVO);
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
	 * ����������������������ʼ������ <li>�Ƿ����ʼ�����ţ������̶������
	 * 
	 * @param userIds
	 * @param task
	 * @throws BusinessException
	 * @throws DbException
	 */
	private void sendMobileAfterAssignment(TaskInstanceVO task)throws BusinessException{

		Logger.debug(">>����������������������ʼ������ ��ʼ");

		// �����϶���Ĺ�������շ�ʽ
		IProcessDefinition processDefinition = ProcessDefinitionUtil.getProDefByProDefPk(task.getPk_process_def());
		String ptId = null;//bwp.getMailPrintTemplet().getTempletid();
		MobileModal mobileModal = null;//bwp.getMobileModal();

		// �����϶���Ĺ�������շ�ʽ
		IActivity activity = processDefinition.findActivity(task.getActivity_id());
		Object mobileModalObj = activity.getProperty(XPDLNames.MOBILE_MODAL);
		if (mobileModalObj!=null && !MobileModal.BLANK.getTag().equals(mobileModalObj)) {
			mobileModal = MobileModal.fromString((String) mobileModalObj);
		}

		String[] targetPhones = null;
		if (needSendMobile(mobileModal)) {
			Logger.debug(">>>�����ʼ���ʼ");
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
					Logger.warn(">>�Ҳ����ֻ��ţ��޷����Ͷ��ţ�����");
					return;
				}
			} catch (Exception e) {
				// ��ȡ�ֻ���ʱ�������⣬��ӦӰ��������ת
				Logger.error(e.getMessage(), e);
			}

			MobileMsg mm = new MobileMsg();
			mm.setTargetPhones(targetPhones);
			mm.setMsg(MessageVO.getMessageNoteAfterI18N(PfMessageUtil.getMessageTopic(task,messageReceiver)));

			if (mobileModal.getValue() == MobileModal.MOBILE_APPROVE_INT) {
				// XXX:��Ҫ������������,�򱣳ֶ��������ĻỰID
				saveSerialNumbers(mm, task);
			} else
				mm.setMsg(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow", "WfTaskManager-0002", null, new String[]{mm.getMsg()})/*{0}(ע:���ɻظ�)*/);
			// �첽���Ͷ���
			PfMailAndSMSUtil.sendSMS(mm);

		}

		Logger.debug(">>����������������������ʼ������ ����");

	}

	private boolean needSendMobile(MobileModal mobileModal) {
		if (mobileModal == null)
			return false;
		int mobileModalValue = mobileModal.getValue();
		return mobileModalValue != MobileModal.NO_MOBILE_INT && mobileModalValue != MobileModal.BLANK_INT;
	}

	/**
	 * ���ֶ��������ĻỰID
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
		// ��������DB
		BaseDAO dao = new BaseDAO();
		dao.insertVOList(al);
		mm.setSerialNumbers(sids);
	}	
}