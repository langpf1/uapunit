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
 * ƽ̨���Ž�������
 * <li>�ɵ�������ִ��
 * 
 * @author leijun 2006-9-18
 * @deprecated yanke1 2012-4-18 v5֮��ShortMessageService����PubDevSmsImplʵ��
 * ����ʵ�ַ�ʽ�²���Ҫ������ѯ���ţ�����ʹ��PubSmsServlet����merp��push����
 * @see PubSmsServlet
 */
public class PfMobileReceiveTask implements ITask {

	private TimeConfigVO m_tc = null;

	public String getName() {
		return NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow1", "PfMobileReceiveTask-000000")/*����ƽ̨���Ž�������*/;
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
			m_tc.setJustInTime(false); //��ʱ
			java.util.Date d = new java.util.Date();
			m_tc.setStartDate(new UFDate(d));

			m_tc.setStartTimeInDay(new UFTime(d));
			m_tc.setPeriodUnit(PeriodUnit.DAY); //��
			m_tc.setPeriod(1); //ÿ��
			m_tc.setPeriodUnitInDay(PeriodUnit.SECOND); //��
			m_tc.setPeriodInDay(180); //ÿ3����
		}
		return m_tc;
	}

	/**
	 * ���ն�������ִ����
	 */
	class PfMobileReceiveTaskBody implements ITaskBody {
		/* (non-Javadoc)
		 * @see nc.bs.uap.scheduler.ITaskBody#execute()
		 */
		public void execute() throws BusinessException {
			Logger.debug("���Ž���������:PfMobileReceiveTaskBody.execute() called");
			//��õ�ǰ��Ķ���Ϣ�����ṩ��
			ShortMessageService sms = WirelessManager.getSMS();
			if (sms == null) {
				Logger.error(">>�Ҳ�����Ķ��ŷ���ʵ���࣬�޷����ͺͽ��ն���");
				return;
			}
			//�������յ�����
			ReceivedSmsVO[] smses = sms.receiveMessages();

			for (int i = 0; i < (smses == null ? 0 : smses.length); i++) {
				//3.��������
				//WARN::��Ҫ����sid�ҵ�ԭ������Ϣ��һЩ������Ϣ������ָ����������ͣ�����ID��
				MobileData condVO = new MobileData();
				condVO.setMobile(smses[i].getMobileNumber());
				condVO.setPk_sid(smses[i].getSessionId());
				//FIXME::ֻ��֧�ֵ�����Դ���������ף�
				BaseDAO dao = new BaseDAO(WirelessManager.getMobileConfig().getDatasource());
				Collection co = dao.retrieve(condVO, true);
				String content = smses[i].getSmsContent(); //�����������Ϊ"Y ͬ��"
				if (!co.isEmpty()) {
					MobileData retVO = (MobileData) co.iterator().next();
					//�����������Ϊ"SP#billtype billid Y ͬ��"
					content = retVO.getCmd() + "#" + retVO.getBilltype() + " " + retVO.getBillid() + " "
							+ smses[i].getSmsContent();
				}

				//4.�������Ϣ
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
