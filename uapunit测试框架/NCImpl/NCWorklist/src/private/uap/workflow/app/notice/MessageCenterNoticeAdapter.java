package uap.workflow.app.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.message.templet.bs.IMsgVarCalculater;
import nc.message.templet.bs.MsgContentCreator;
import nc.message.util.IDefaultMsgConst;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.vo.pub.BusinessException;

import uap.workflow.app.message.TaskTopicBusiVarCalculator;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.WorkitemMsgContext;
import uap.workflow.pub.util.Pfi18nTools;
import uap.workflow.vo.WorkflownoteVO;


/** 
   通知实现类
 * @author 
 */
public class MessageCenterNoticeAdapter extends AbstractNoticeAdapter{
	private IActivity activity = null;
	private TaskInstanceVO task = null;
	private Object billEntity = null;
	private INoticeDefinition noticeDefinition = null;
	private WorkitemMsgContext context = null;
	public void sendNotice(TaskInstanceVO task, INoticeDefinition noticeDefinition, NoticeContext noticeContext) {
		this.activity = noticeContext.getActivity();
		this.task = task;
		this.billEntity = noticeContext.getBillEntity();
		this.noticeDefinition = noticeDefinition;
		BuildContext(activity,task,billEntity);
		try {
			executeNCMsg();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			// 异常吞掉？
		}
	}

	private void executeNCMsg() throws BusinessException {
		Map<String, List<String>> rcvMap = classifyReceiverIdByLang();

		if (rcvMap.keySet().size() == 0)
			return;

		IMsgVarCalculater calculator = new TaskTopicBusiVarCalculator(context);
		NCMessage tempNCMsg = new NCMessage();
		MsgContentCreator creator = new MsgContentCreator();
		
		// bugfix for msgContentCreator not resetting origin langcode
		String originLangcode = InvocationInfoProxy.getInstance().getLangCode();
		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		Map<String, NCMessage> msgMap = creator.createMessageUsingTemp(noticeDefinition.getContentTemplate(), 
				pk_group, rcvMap.keySet().toArray(new String[0]), tempNCMsg, calculator, context.getBusiObj(), null);
		InvocationInfoProxy.getInstance().setLangCode(originLangcode);
		
		
		List<NCMessage> toBeSent = new ArrayList<NCMessage>();

		for (Iterator<String> it = msgMap.keySet().iterator(); it.hasNext();) {
			String langcode = it.next();
			NCMessage ncmsg = msgMap.get(langcode);
			
			fillCommonField(ncmsg, langcode,task);
			
			MessageVO msgVO = ncmsg.getMessage();
			msgVO.setReceiver(getStrSequenctFromList(rcvMap.get(langcode)));
			msgVO.setMsgtype("nc");
			
			if (noticeDefinition.getHasReceipt()) {
				msgVO.setMsgsourcetype(IDefaultMsgConst.NOTICE);
				msgVO.setContenttype(WorkflownoteVO.FLOWMSG_NEED_CHECK);
			} else {
				msgVO.setMsgsourcetype(IDefaultMsgConst.NOTICE);
			}
			
			toBeSent.add(ncmsg);
		}
		sendMsgSync(toBeSent.toArray(new NCMessage[0]));
	}

	private Map<String, List<String>> classifyReceiverIdByLang() throws BusinessException {
		String[] cuserids = getReceivers(noticeDefinition,task);

		if (cuserids == null || cuserids.length == 0) {
			return new HashMap<String, List<String>>();
		} else {
			return Pfi18nTools.classifyUsersByLangcode(cuserids);
		}
	}
}