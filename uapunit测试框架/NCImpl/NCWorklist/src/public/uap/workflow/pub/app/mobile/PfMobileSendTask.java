package uap.workflow.pub.app.mobile;

import uap.workflow.pub.app.mobile.vo.MobileMsg;
import nc.bs.logging.Logger;
import nc.bs.uap.scheduler.ITask;
import nc.bs.uap.scheduler.ITaskBody;
import nc.bs.uap.scheduler.ITaskJudger;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.uap.scheduler.TaskStatus;
import nc.vo.uap.scheduler.TimeConfigVO;

/**
 * ƽ̨���ŷ�������
 * <li>�ɵ�������ִ��
 * 
 * @author leijun 2006-9-18
 */
public class PfMobileSendTask implements ITask {

	private MobileMsg mobileMsg;

	private TimeConfigVO m_tc;

	public PfMobileSendTask(MobileMsg mobileVO) {
		super();
		this.mobileMsg = mobileVO;
	}

	public String getName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfMobileSendTask-000000")/*����ƽ̨���ŷ�������*/;
	}

	public String getType() {
		return "PfMobileSendTask";
	}

	public ITaskJudger getTaskJudger() {
		// TODO Auto-generated method stub
		return null;
	}

	public ITaskBody getTaskBody() {
		return new PfMobileSendTaskBody();
	}

	public TimeConfigVO getTimeConfig() {
		if (m_tc == null) {
			m_tc = new TimeConfigVO();
			m_tc.setJustInTime(true); //��ʱ
		}
		return m_tc;
	}

	/**
	 * ���Ͷ�������ִ����
	 */
	class PfMobileSendTaskBody implements ITaskBody {

		public void execute() throws BusinessException {
			Logger.debug(">>���ŷ���������:PfMobileSendTaskBody.execute() called");
			ShortMessageService sms = WirelessManager.getSMS();
			if (sms == null) {
				Logger.error(">>�Ҳ�����Ķ��ŷ���ʵ���࣬�޷����ͺͽ��ն���");
				return;
			}

			if (!sms.initialize()) {
				//���ŷ����ʼ��ʧ��
				Logger.error(">>���ŷ����ʼ��ʧ��");
				return;
			}

			for (int i = 0; i < mobileMsg.getTargetPhones().length; i++) {
				String[] sids = mobileMsg.getSerialNumbers();
				if (sids == null || sids.length <= i) {
					//����Ҫ�ỰSID
					Logger.debug("**���Է��Ͷ��ţ��ֻ���=" + mobileMsg.getTargetPhones()[i] + ";����="
							+ mobileMsg.getMsg());
					sms.sendMessages(new String[] { mobileMsg.getTargetPhones()[i] }, mobileMsg.getMsg());
				} else {
					//��Ҫ�ỰSID
					Logger.debug("**���Է��Ͷ��ţ��ֻ���=" + mobileMsg.getTargetPhones()[i] + ";����="
							+ mobileMsg.getMsg() + ";�ỰID=" + sids[i]);
					sms.sendMessage(mobileMsg.getTargetPhones()[i], mobileMsg.getMsg(), sids[i]);
				}
			}

			Logger.debug(">>���ŷ���������:PfMobileSendTaskBody.execute() end");
		}

		public void cancelExecute() throws BusinessException {
		}

		public TaskStatus getStatus() {
			return null;
		}

	}
}
