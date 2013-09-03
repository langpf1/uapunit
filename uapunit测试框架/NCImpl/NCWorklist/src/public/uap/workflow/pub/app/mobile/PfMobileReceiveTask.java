package uap.workflow.pub.app.mobile;

import java.util.Collection;

import uap.workflow.pub.app.mobile.vo.MobileData;
import uap.workflow.pub.app.mobile.vo.ReceivedSmsVO;

import nc.bs.dao.BaseDAO;
import nc.bs.logging.Logger;
import nc.bs.uap.scheduler.ITask;
import nc.bs.uap.scheduler.ITaskBody;
import nc.bs.uap.scheduler.ITaskJudger;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFTime;
import nc.vo.uap.scheduler.PeriodUnit;
import nc.vo.uap.scheduler.TaskStatus;
import nc.vo.uap.scheduler.TimeConfigVO;

/**
 * 平台短信接收任务
 * <li>由调度引擎执行
 * 
 * @author leijun 2006-9-18
 * @deprecated yanke1 2012-4-18 v5之后ShortMessageService采用PubDevSmsImpl实现
 * 该种实现方式下不需要主动轮询短信，而是使用PubSmsServlet接受merp的push短信
 * @see PubSmsServlet
 */
public class PfMobileReceiveTask implements ITask {

	private TimeConfigVO m_tc = null;

	public String getName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfMobileReceiveTask-000000")/*流程平台短信接收任务*/;
	}

	public String getType() {
		return "PfMobileReceiveTask";
	}

	public ITaskJudger getTaskJudger() {
		return null;
	}

	public ITaskBody getTaskBody() {
		return new PfMobileReceiveTaskBody();
	}

	public TimeConfigVO getTimeConfig() {
		if (m_tc == null) {
			m_tc = new TimeConfigVO();
			m_tc.setJustInTime(false); //定时
			java.util.Date d = new java.util.Date();
			m_tc.setStartDate(new UFDate(d));

			m_tc.setStartTimeInDay(new UFTime(d));
			m_tc.setPeriodUnit(PeriodUnit.DAY); //天
			m_tc.setPeriod(1); //每天
			m_tc.setPeriodUnitInDay(PeriodUnit.SECOND); //秒
			m_tc.setPeriodInDay(180); //每3分钟
		}
		return m_tc;
	}

	/**
	 * 接收短信任务执行体
	 */
	class PfMobileReceiveTaskBody implements ITaskBody {
		/* (non-Javadoc)
		 * @see nc.bs.uap.scheduler.ITaskBody#execute()
		 */
		public void execute() throws BusinessException {
			Logger.debug("短信接收任务体:PfMobileReceiveTaskBody.execute() called");
			//获得当前活动的短消息服务提供者
			ShortMessageService sms = WirelessManager.getSMS();
			if (sms == null) {
				Logger.error(">>找不到活动的短信服务实现类，无法发送和接收短信");
				return;
			}
			//批量接收到短信
			ReceivedSmsVO[] smses = sms.receiveMessages();

			for (int i = 0; i < (smses == null ? 0 : smses.length); i++) {
				//3.分析短信
				//WARN::需要根据sid找到原发送信息的一些额外信息，比如指令串，单据类型，单据ID等
				MobileData condVO = new MobileData();
				condVO.setMobile(smses[i].getMobileNumber());
				condVO.setPk_sid(smses[i].getSessionId());
				//FIXME::只能支持单数据源，即单帐套？
				BaseDAO dao = new BaseDAO(WirelessManager.getMobileConfig().getDatasource());
				Collection co = dao.retrieve(condVO, true);
				String content = smses[i].getSmsContent(); //如果审批，则为"Y 同意"
				if (!co.isEmpty()) {
					MobileData retVO = (MobileData) co.iterator().next();
					//如果审批，则为"SP#billtype billid Y 同意"
					content = retVO.getCmd() + "#" + retVO.getBilltype() + " " + retVO.getBillid() + " "
							+ smses[i].getSmsContent();
				}

				//4.处理短消息
				MobileHandler.getInstance().mobileMsgReceived(smses[i].getMobileNumber(), content,
						smses[i].getSessionId());
			}
		}

		/* (non-Javadoc)
		 * @see nc.bs.uap.scheduler.ITaskBody#cancelExecute()
		 */
		public void cancelExecute() throws BusinessException {

		}

		/* (non-Javadoc)
		 * @see nc.bs.uap.scheduler.ITaskBody#getStatus()
		 */
		public TaskStatus getStatus() {
			return null;
		}

	}
}
