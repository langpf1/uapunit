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
public class AbstractNoticeAdapter implements INoticeAdapter
{
	public void sendNotice(TaskInstanceVO task, INoticeDefinition noticeDefinition, NoticeContext noticeContext) {

	}

	protected WorkitemMsgContext BuildContext(IActivity activity,TaskInstanceVO task,Object billEntity) {
		WorkitemMsgContext context = new WorkitemMsgContext();
		
		int actType = (Integer) activity.getProperty("ActivityType");
		boolean isWorkflow = true;// WorkflowTypeEnum.Workflow.getIntValue() == task.getWorkflowType();
		String actionType = TaskTopicResolver.getActionType(actType, isWorkflow);
		String result = TaskTopicResolver.getResult(isWorkflow, task.getActiontype(), task, task.getIspass().toString()); 
		
		context.setActionType(actionType);
		context.setAgent(task.getPk_agenter());
		context.setBillid(task.getPk_form_ins_version());
		context.setBillno(task.getForm_no());
		context.setBillType(task.getPk_bizobject());
		context.setBusiObj(billEntity);
		context.setCheckman(task.getPk_owner());
		context.setCheckNote(task.getOpinion());//paraVO.m_workFlow.getChecknote()
		context.setSender(task.getPk_creater());
		context.setResult(result);	
		return context;
	}
	
	protected String[] getReceivers(INoticeDefinition noticeDefinition,TaskInstanceVO task) throws BusinessException {
		List<ReceiverVO> rcvs = null;
		try {
			rcvs = noticeDefinition.getReceivers();
		} catch (Exception e) {
			// yk+ 若获取receivers时出现了异常（在只配置了消息没有配置接收人时会出现这样的情况），
			// 不可以影响流程正常运转
			// 因此只log，不抛出
			Logger.error("Workflow Platform: error occured while acquiring receivers: " + e.getMessage(), e);
			return null;
		}

		return new MsgReceiverUtil().getMessageReceivers(rcvs.toArray(new ReceiverVO[rcvs.size()]), task);
	}	

	protected void fillCommonField(NCMessage ncmsg, String langcode,TaskInstanceVO task) {
		MessageVO msgVO = ncmsg.getMessage();
		msgVO.setSendtime(new UFDateTime());
		msgVO.setIshandled(new UFBoolean(false));
		msgVO.setSendstate(new UFBoolean(true));
		msgVO.setPk_org(task.getPk_org());
		msgVO.setPk_detail(task.getPk_task());
		msgVO.setPk_group(InvocationInfoProxy.getInstance().getGroupId());
		msgVO.setDetail(task.getPk_form_ins_version() + "@" + task.getPk_bizobject() 
				+ "@" + task.getForm_no() + "@" + task.getPk_task());
		msgVO.setSender(PfMessageUtil.DEFAULT_SENDER);
		
		String currLangcode = InvocationInfoProxy.getInstance().getLangCode();
		{
			InvocationInfoProxy.getInstance().setLangCode(langcode);

			msgVO.setSubject(nc.vo.pub.msg.MessageVO.getMessageNoteAfterI18N(msgVO.getSubject()));
			msgVO.setContent(nc.vo.pub.msg.MessageVO.getMessageNoteAfterI18N(msgVO.getContent()));

			InvocationInfoProxy.getInstance().setLangCode(currLangcode);
		}
	}
	
	protected void sendMsgAsync(final NCMessage[] ncmsgs) throws BusinessException {
		try {
			ITaskManager mgr = NCLocator.getInstance().lookup(ITaskManager.class);
			
			ITask task = new ITask() {
				
				@Override
				public String getType() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public TimeConfigVO getTimeConfig() {
					TimeConfigVO config = new TimeConfigVO();
					config.setJustInTime(true);
					
					return config;
				}
				
				@Override
				public ITaskJudger getTaskJudger() {
					return null;
				}
				
				@Override
				public ITaskBody getTaskBody() {
					return new ITaskBody() {
						
						@Override
						public TaskStatus getStatus() {
							// TODO Auto-generated method stub
							return null;
						}
						
						@Override
						public void execute() throws BusinessException {
							try {
								sendMsgSync(ncmsgs);
							} catch (Exception e) {
								Logger.error(e.getMessage(), e);
							}
						}
						
						@Override
						public void cancelExecute() throws BusinessException {
							// TODO Auto-generated method stub
							
						}
					};
				}
				
				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return null;
				}
			};
			mgr.add(task, TaskPriority.GENERAL);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}

	protected void sendMsgSync(NCMessage[] ncmsgs) throws BusinessException {
		try {
			MessageCenter.sendMessage(ncmsgs);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new BusinessException(e);
		}
	}
	protected String getStrSequenctFromList(List<String> list) {
		if (list == null || list.size() == 0)
			return "";

		StringBuffer sb = new StringBuffer();

		for (String str : list) {
			sb.append(",");
			sb.append(str);
		}

		return sb.substring(1);
	}	
}