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
 * 平台短信发送任务
 * <li>由调度引擎执行
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
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfMobileSendTask-000000")/*流程平台短信发送任务*/;
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
			m_tc.setJustInTime(true); //即时
		}
		return m_tc;
	}

	/**
	 * 发送短信任务执行体
	 */
	class PfMobileSendTaskBody implements ITaskBody {

		public void execute() throws BusinessException {
			Logger.debug(">>短信发送任务体:PfMobileSendTaskBody.execute() called");
			ShortMessageService sms = WirelessManager.getSMS();
			if (sms == null) {
				Logger.error(">>找不到活动的短信服务实现类，无法发送和接收短信");
				return;
			}

			if (!sms.initialize()) {
				//短信服务初始化失败
				Logger.error(">>短信服务初始化失败");
				return;
			}

			for (int i = 0; i < mobileMsg.getTargetPhones().length; i++) {
				String[] sids = mobileMsg.getSerialNumbers();
				if (sids == null || sids.length <= i) {
					//不需要会话SID
					Logger.debug("**尝试发送短信：手机号=" + mobileMsg.getTargetPhones()[i] + ";内容="
							+ mobileMsg.getMsg());
					sms.sendMessages(new String[] { mobileMsg.getTargetPhones()[i] }, mobileMsg.getMsg());
				} else {
					//需要会话SID
					Logger.debug("**尝试发送短信：手机号=" + mobileMsg.getTargetPhones()[i] + ";内容="
							+ mobileMsg.getMsg() + ";会话ID=" + sids[i]);
					sms.sendMessage(mobileMsg.getTargetPhones()[i], mobileMsg.getMsg(), sids[i]);
				}
			}

			Logger.debug(">>短信发送任务体:PfMobileSendTaskBody.execute() end");
		}

		public void cancelExecute() throws BusinessException {
		}

		public TaskStatus getStatus() {
			return null;
		}

	}
}
