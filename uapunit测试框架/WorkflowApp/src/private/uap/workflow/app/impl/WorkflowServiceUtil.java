package uap.workflow.app.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
//import nc.pubitf.so.m30.mmdp.aid.ParaVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IBusinessKey1;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.bridge.ProcessInstanceBridge;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.message.MsgType;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.itf.IWorkflowServiceUtil;
import uap.workflow.vo.WorkflownoteVO;

public class WorkflowServiceUtil implements IWorkflowServiceUtil{

	public static final String APP_FORMINFO = "APP_FORMINFO";

	@Override
	public Object backword(Object billvo, Map<String, Object> customData) {
		return null;
	}

	@Override
	public Object forward(WorkflownoteVO noteVO, Object billvo, Map<String, Object> customData) {
		forwardWorkflow(billvo, noteVO.getTaskInstanceVO());
		return billvo;
	}

	@Override
	public Object reject(Object billvo, Map<String, Object> customData) {
		return null;
	}

	private TaskInstanceVO checkWorkflowWhenStart(Object vo){
		TaskInstanceVO[] taskVO = null;
		String id = null;
		if (vo instanceof IBusinessKey1){
			id = ((IBusinessKey1)vo).getBizObjectID();
		}else if (vo instanceof AggregatedValueObject){
			try {
				id = ((AggregatedValueObject)vo).getParentVO().getPrimaryKey();
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}
		taskVO = getToDoWorkitems(id, InvocationInfoProxy.getInstance().getUserId());
		if (taskVO == null ||taskVO.length < 1)
			return null;
		return taskVO[0];
	}
	
	private IBusinessKey BuildFormInfoCtx(TaskInstanceVO taskVO, Object vo){
		if (vo instanceof IBusinessKey){
			return (IBusinessKey)vo;
		}else if (vo instanceof AggregatedValueObject){
			BizObjectImpl bizObject = new BizObjectImpl();
			try {
				bizObject.setBillId(((AggregatedValueObject)vo).getParentVO().getPrimaryKey());
				bizObject.setBillNo("");
				bizObject.setBillType(((AggregatedValueObject)vo).getParentVO().getClass().getName());
				bizObject.setBizObjects(new Object[]{vo});
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (IBusinessKey)bizObject;
		}
		return null;		
	}
	
	private IFlowResponse forwardWorkflow(Object vo, TaskInstanceVO taskVO) {

		IBusinessKey formInfo = BuildFormInfoCtx(taskVO, vo);
		NextUserTaskInfoUtil.setNextTaskInsCtx(formInfo, InvocationInfoProxy.getInstance().getUserId(), true);
		List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(taskVO.getPk_task(),formInfo,null);
		// 够造下一步的执行信息
		NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
		// 设置当前任务
		nextTaskCtx.setTaskPk(taskVO.getPk_task());
		// 设置当前用户
		nextTaskCtx.setUserPk(InvocationInfoProxy.getInstance().getUserId());
		nextTaskCtx.setComment("");
		List<UserTaskRunTimeCtx> nextInfo = new ArrayList<UserTaskRunTimeCtx>();
		for (int i = 0; i < userTaskPrepCtx.size(); i++) {
			UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
			runTimeCtx.setActivityId(userTaskPrepCtx.get(i).getActivityId());
			runTimeCtx.setUserPks(userTaskPrepCtx.get(i).getUserPks());
			runTimeCtx.setSequence(true);
			runTimeCtx.setMsgType(new MsgType[] { MsgType.MSGCENTER });
			nextInfo.add(runTimeCtx);

		}
		nextTaskCtx.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));

		if (userTaskPrepCtx != null && userTaskPrepCtx.size() > 0) {

		}
		// 执行指令
		IFlowRequest request = BizProcessServer.createFlowRequest(formInfo, nextTaskCtx);
		IFlowResponse respone = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, respone);
		return respone;
	}
	
	@Override
	public Object start(Object vo, Map<String, Object> customData) throws BusinessException {
		
		TaskInstanceVO taskVO = checkWorkflowWhenStart(vo);
		
		String bizObject = null;
		
		if (taskVO != null)
			throw new BusinessException("流程已经启动了！");
		
		if (vo instanceof IBusinessKey1){
			bizObject = ((IBusinessKey1)vo).getBizObjectType();
		}
		else if (vo instanceof AggregatedValueObject){
			bizObject = ((AggregatedValueObject)vo).getParentVO().getClass().getName();
		}else
			throw new BusinessException("没有定义IBusinessKey");
		
		IProcessDefinitionQry proDefQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO[] proDefVOs = proDefQry.getProcessDefVOAccordingBiz(InvocationInfoProxy.getInstance().getGroupId(), bizObject);
		if (proDefVOs == null || proDefVOs.length == 0) {
			throw new BusinessException("没有定义流程！");
		}
		String pk_user = InvocationInfoProxy.getInstance().getUserId();//等待替换
		IProcessDefinition proDef = ProcessDefinitionUtil.getProDefByProDefPk(proDefVOs[0].getPk_prodef());
		IBusinessKey formInfo = BuildFormInfoCtx(taskVO, vo);
		NextUserTaskInfoUtil.setCommitProInsCtx(formInfo,pk_user);
		List<UserTaskPrepCtx> userTaskPropCtx = NextUserTaskInfoUtil.getStartNextUserTaskInfo(proDef.getProDefPk(), null, pk_user);
		List<UserTaskRunTimeCtx> runTimeCtx=new ArrayList<UserTaskRunTimeCtx>();
		for(int i=0;i<userTaskPropCtx.size();i++){
			UserTaskRunTimeCtx nextInfo = new UserTaskRunTimeCtx();
			nextInfo.setActivityId(userTaskPropCtx.get(i).getActivityId());
			nextInfo.setUserPks(userTaskPropCtx.get(i).getUserPks());
			nextInfo.setSequence(true);
			runTimeCtx.add(nextInfo);
		}
		CommitProInsCtx flowInfo = new CommitProInsCtx();
		flowInfo.setProDefPk(proDefVOs[0].getPk_prodef());
		flowInfo.setUserPk(InvocationInfoProxy.getInstance().getUserId());
		flowInfo.setNextInfo(runTimeCtx.toArray(new UserTaskRunTimeCtx[0]));
		flowInfo.setMakeBill(true);
		//TODO:临时转换
		IFlowRequest request = BizProcessServer.createFlowRequest(formInfo, flowInfo);
		IFlowResponse respone = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, respone);

		if(respone.getProcessInstance() != null)
		{
			respone.getProcessInstance().setVariable(WorkflowServiceUtil.APP_FORMINFO, formInfo);
		}

		ITask[] nextTasks = respone.getNewTasks();
		if (nextTasks == null) {
			return new boolean[] { true, false };
		}

		// 制单任务直接往下执行
		String processInstancePK = null;
		for (ITask task : nextTasks) {
			taskVO = new TaskInstanceBridge().convertT2M(task);
			if (taskVO.getCreate_type() == TaskInstanceCreateType.Makebill.getIntValue()) {
				IFlowResponse respone1 = forwardWorkflow(vo, taskVO);
				if (respone1.getNewTasks() != null&&respone1.getNewTasks().length!=0) {
					processInstancePK = respone1.getNewTasks()[0].getExecution().getProcessInstance().getProInsPk();
				}
			}
		}
		if (processInstancePK == null) {
			return new boolean[] { true, false };
		} else {
			IProcessInstance processInstance = ProcessInstanceUtil.getProcessInstance(processInstancePK);
			ProcessInstanceVO processInstanceVO = new ProcessInstanceBridge().convertT2M(processInstance);
			if (processInstanceVO.getState_proins() == ProcessInstanceStatus.Finished.getIntValue()) {
				// 提交即审批通过
				return new boolean[] { true, true };
			}
		}
		return new boolean[] { true, false };
	}

	public TaskInstanceVO[] getToDoWorkitems(String billId, String userPK) {

		List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
				.getTasksByFormInstancePk(billId);
		List<TaskInstanceVO> retTasks = filterToDoTask(userPK, tasks);
		if(retTasks==null){
			retTasks=new ArrayList<TaskInstanceVO>();
		}
		if(tasks==null){
			tasks=new ArrayList<TaskInstanceVO>();
		}
		if(retTasks.size() == 0 && tasks.size() > 0)
		{
			String processInstanceID = tasks.get(0).getPk_process_instance();
			List<TaskInstanceVO> taskList = NCLocator.getInstance().lookup(ITaskInstanceQry.class).getTaskByProcessInstancePk(processInstanceID);
			retTasks = filterToDoTask(userPK, taskList);
		}
		return retTasks.toArray(new TaskInstanceVO[0]);
	}

	private List<TaskInstanceVO> filterToDoTask(String userPK, List<TaskInstanceVO> tasks) {
		if (tasks == null) {
			return null;
		}
		List<TaskInstanceVO> retTasks = new ArrayList<TaskInstanceVO>();
		for (TaskInstanceVO task : tasks) {
			if ((task.getPk_owner() != null && task.getPk_owner().equalsIgnoreCase(userPK))
					|| (task.getPk_agenter() != null && task.getPk_agenter().equalsIgnoreCase(userPK))) {
				if (task.getState_task() == TaskInstanceStatus.Started.getIntValue()
						|| task.getState_task() == TaskInstanceStatus.Wait.getIntValue()
						|| task.getState_task() == TaskInstanceStatus.Run.getIntValue()
						|| task.getState_task() == TaskInstanceStatus.BeforeAddSignComplete.getIntValue()
						|| task.getState_task() == TaskInstanceStatus.BeforeAddSignSend.getIntValue()) {
					retTasks.add(task);
				}
			}
		}
		return retTasks;
	}

}
