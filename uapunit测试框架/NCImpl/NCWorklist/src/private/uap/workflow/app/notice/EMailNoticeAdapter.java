package uap.workflow.app.notice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.scheduler.ITask;
import nc.bs.uap.scheduler.ITaskBody;
import nc.bs.uap.scheduler.ITaskJudger;
import nc.bs.uap.scheduler.ITaskManager;
import nc.itf.uap.IUAPQueryBS;
import nc.message.templet.bs.IMsgVarCalculater;
import nc.message.templet.bs.MsgContentCreator;
import nc.message.util.MessageCenter;
import nc.message.vo.MessageVO;
import nc.message.vo.NCMessage;
import nc.pubitf.rbac.IUserPubService;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.uap.scheduler.TaskPriority;
import nc.vo.uap.scheduler.TaskStatus;
import nc.vo.uap.scheduler.TimeConfigVO;
import uap.workflow.app.message.MsgReceiverUtil;
import uap.workflow.app.message.TaskTopicBusiVarCalculator;
import uap.workflow.app.message.TaskTopicResolver;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.app.message.PfMessageUtil;
import uap.workflow.pub.app.message.WorkitemMsgContext;
import uap.workflow.pub.util.Pfi18nTools;


/** 
   通知实现类
 * @author 
 */
public class EMailNoticeAdapter extends AbstractNoticeAdapter{

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
			executeEmailMsg();
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);

			// 异常吞掉？
		}
	}

	private void executeEmailMsg() throws BusinessException{
		Map<String, List<String>> rcvMap = classifyReceiverEmailByLang();

		if (rcvMap.keySet().size() == 0)
			return;

		IMsgVarCalculater calculator = new TaskTopicBusiVarCalculator(context);
		NCMessage tempNCMsg = new NCMessage();
		MsgContentCreator creator = new MsgContentCreator();

		String pk_group = InvocationInfoProxy.getInstance().getGroupId();
		Map<String, NCMessage> msgMap = creator.createMessageUsingTemp(noticeDefinition.getContentTemplate(), 
				pk_group, rcvMap.keySet().toArray(new String[0]), tempNCMsg, calculator, context.getBusiObj(), null);
		
		List<NCMessage> toBeSent = new ArrayList<NCMessage>();

		for (Iterator<String> it = msgMap.keySet().iterator(); it.hasNext();) {
			String langcode = it.next();
			NCMessage ncmsg = msgMap.get(langcode);
			
			fillCommonField(ncmsg, langcode, task);
			
			MessageVO msgVO = ncmsg.getMessage();
			msgVO.setReceiver(getStrSequenctFromList(rcvMap.get(langcode)));
			msgVO.setMsgtype("email");
			
			toBeSent.add(ncmsg);
		}
		sendMsgAsync(toBeSent.toArray(new NCMessage[0]));
	}
	
	private Map<String, List<String>> classifyReceiverEmailByLang() throws BusinessException {
		Map<String, List<String>> rcvMap = new HashMap<String, List<String>>();

		String[] cuserids = getReceivers(noticeDefinition, task);

		if (cuserids == null || cuserids.length == 0) {
			return rcvMap;
		}

		for (String cuserid : cuserids) {
			String langcode = Pfi18nTools.getLangCodeOfUser(cuserid);
			String email = getEmailOfUser(cuserid);

			if (StringUtil.isEmptyWithTrim(email))
				continue;

			List<String> list = rcvMap.get(langcode);
			if (list == null) {
				list = new ArrayList<String>();
				rcvMap.put(langcode, list);
			}

			list.add(email);
		}

		return rcvMap;
	}
	
	private String getEmailOfUser(String cuserid) throws BusinessException {

		IUserPubService userService = NCLocator.getInstance().lookup(IUserPubService.class);
		String pk_psndoc = userService.queryPsndocByUserid(cuserid);

		if (StringUtil.isEmptyWithTrim(pk_psndoc)) {
			Logger.error("用户" + cuserid + "未关联人员档案", new Throwable());
			return null;
		}

		IUAPQueryBS uapQry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		PsndocVO psndoc = (PsndocVO) uapQry.retrieveByPK(PsndocVO.class, pk_psndoc);
		String email = psndoc == null ? null : psndoc.getEmail();

		return email;
	}
}