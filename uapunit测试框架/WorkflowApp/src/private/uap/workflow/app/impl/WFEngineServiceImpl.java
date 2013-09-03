package uap.workflow.app.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.ml.NCLangResOnserver;
import nc.jdbc.framework.exception.DbException;
import nc.jdbc.framework.generator.SequenceGenerator;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.admin.IWFEngineService;
import uap.workflow.admin.IWorkflowDefine;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.app.exeception.PFBusinessException;
import uap.workflow.vo.IReturnCheckInfo;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.bridge.ProcessInstanceBridge;
import uap.workflow.engine.bridge.TaskInstanceBridge;
import uap.workflow.engine.context.CallBackTaskInsCtx;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.CreateAfterAddSignCtx;
import uap.workflow.engine.context.CreateBeforeAddSignCtx;
import uap.workflow.engine.context.DelegateTaskInsCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.RejectTaskInsCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.core.ProcessInstanceStatus;
import uap.workflow.engine.core.TaskInstanceCreateType;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.itf.IProcessInstanceQry;
import uap.workflow.engine.itf.ITaskInstanceQry;
import uap.workflow.engine.message.MsgType;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.task.TaskDefinition;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.utils.ProcessInstanceUtil;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.ProcessInstanceVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.pub.util.ProcessDataCache;
import uap.workflow.restlet.application.Pagination;
import uap.workflow.vo.ReturnBackWfVo;
import uap.workflow.vo.WFAppParameter;
import uap.workflow.vo.WorkflownoteVO;

/**
 * 工作流引擎服务实现类
 * 
 * @author wzhy 2004-2-21
 * @modifier leijun 2006-4-7 使用动态锁机制不需释放锁了
 * @modifier leijun 2008-8 增加工作流相关处理
 * @modifier leijun 2008-12 根据扩展参数决定是否重新装载VO
 * @modifier guowl 2010-5 6.0不再支持一弃到底的模式
 */
public class WFEngineServiceImpl implements IWFEngineService {
	public static final String APP_FORMINFO = "APP_FORMINFO";

	public WFEngineServiceImpl() {
	}

	/**
	 * 单据"提交"启动审批流时调用
	 * @param paraVo  动作执行参数VO
	 * @param status  单据状态：自由态，提交态，未通过
	 * @throws BusinessException
	 */
	public WorkflownoteVO getWorkflowItemsOnStart(WFAppParameter paraVo, int status) throws BusinessException {
		TaskInstanceVO beginTask = null;
		IProcessDefinition processDef = null;
		WorkflownoteVO worknoteVO = null;
		TaskInstanceVO[] todoTaskVOs = getToDoWorkitems(paraVo.getBillType(), 
				paraVo.getBillId(), paraVo.getOperator());
		if (todoTaskVOs != null && todoTaskVOs.length > 0) {
			beginTask = todoTaskVOs[0];
			paraVo.setProcessDefPK(beginTask.getPk_process_def());
		}

		switch (status) {
		case IReturnCheckInfo.COMMIT:
		case IReturnCheckInfo.NOPASS:
		case IReturnCheckInfo.NOSTATE:
			// 查询可启动的流程定义
			if (StringUtil.isEmptyWithTrim(paraVo.getProcessDefPK()))
				processDef = NCLocator.getInstance().lookup(IWorkflowDefine.class)
						.matchProcessDefitionAccordingBiz(paraVo.getGroupPK(), paraVo.getBillType(), paraVo.getOrgPK(),
								paraVo.getBillMaker(), paraVo.getEmendEnum());
			else
				processDef = findParsedWfProcess(paraVo.getProcessDefPK(), null);

			if (processDef == null) {
				// 没有对应的审批流定义,任何人都可审批通过
				return null;
			}

			// 新建任务要赋 工作流ID,活动ID和单据号
			if (beginTask == null) {
				beginTask = new TaskInstanceVO();
				beginTask.setCreate_type(TaskInstanceCreateType.Makebill.getIntValue());
				//beginTask.setPk_org(paraVo.m_pkOrg);
				beginTask.setPk_task(new SequenceGenerator().generate());
				beginTask.setBegindate(new UFDateTime(paraVo.getBizDateTime()));
			}
			beginTask.setPk_process_def(processDef.getProDefPk());
			beginTask.setActivity_id(processDef.getInitial().getId());// 开始Id，活动
			break;
		default:
			return null;
		}

		beginTask.setForm_no(paraVo.getBusinessObject().getBillNo());
		beginTask.setPk_form_ins(paraVo.getBusinessObject().getBillId());
		beginTask.setPk_form_ins_version(paraVo.getBusinessObject().getBillId());
		beginTask.setPk_bizobject(paraVo.getBusinessObject().getBillType());
		beginTask.setPk_creater(paraVo.getOperator());
		beginTask.setPk_owner(paraVo.getOperator());
		if (worknoteVO == null) {
			worknoteVO = new WorkflownoteVO();
		}

		List<TaskDefinition> taskDefinitions = ProcessDefinitionUtil.getTaskDefinition(null,processDef.getProDefPk()
				, paraVo.getBusinessObject(), paraVo.getOperator());
		for(TaskDefinition taskDefinition : taskDefinitions)
		{
			if(taskDefinition.isAssign())
			{
				worknoteVO.setAssign(true);
			}
		}

		worknoteVO.setTaskInstanceVO(beginTask);
		worknoteVO.setActiontype(paraVo.getActionName());
		return worknoteVO;
	}

	/**
	 * 检查某用户的待办工作项 <li>如果无待办工作项且流程已经启动，则抛出异常
	 * 
	 * @param paraVo
	 * @return WorkflownoteVO
	 */
	public WorkflownoteVO checkUnfinishedWorkitem(WFAppParameter paraVo)
			throws BusinessException, DbException {
		// 查询某用户对某单据的所有待办工作项
		TaskInstanceVO[] todoTasks = getToDoWorkitems(paraVo.getBillType(), paraVo.getBillId(), paraVo.getOperator());
		if (todoTasks != null && todoTasks.length > 0) {
			// 当前操作员有待办工作项
			// FIXME:某用户对某个单据可能同时有两个待办工作项吗？
			TaskInstanceVO todoTaskVO = todoTasks[todoTasks.length - 1];
			if (todoTaskVO.getCreate_type() == TaskInstanceCreateType.Makebill.getIntValue()
					|| todoTaskVO.getCreate_type() == TaskInstanceCreateType.Withdraw.getIntValue())
				throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000388")/* 没有待办任务。 */);

			// 校验流程实例的状态
			if (!hasRunningProcessInstances(paraVo.getBillId())) {
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"EngineService-0004")/* 该单据所处的流程实例已被挂起！ */);
			}

			WorkflownoteVO workflownoteVO = new WorkflownoteVO();
			workflownoteVO.setTaskInstanceVO(todoTaskVO);
			/*
			IBizObject formInfo = BuildFormInfoCtx(paraVo);
			List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(todoTaskVO.getPk_task(),formInfo);
			workflownoteVO.setUserTaskPrepCtx(userTaskPrepCtx);
			*/

			List<TaskDefinition> taskDefinitions = ProcessDefinitionUtil.getTaskDefinition(todoTaskVO.getPk_task(),
					todoTaskVO.getPk_process_def(), paraVo.getBusinessObject(), paraVo.getOperator());
			for(TaskDefinition taskDefinition : taskDefinitions)
			{
				if(taskDefinition.isAssign())
				{
					workflownoteVO.setAssign(true);
				}
			}
			return workflownoteVO;
		} else {
			List<TaskInstanceVO> finishTasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
			.getFinishedTasks(paraVo.getBillType(), paraVo.getBillId(), paraVo.getOperator());
			if (finishTasks != null && finishTasks.size() > 0) {
				throw new PFBusinessException("当前操作人待办任务已经完成！");
			}

			// 当前操作员无待办工作项
//			String pro_def = paraVo.m_flowDefPK;
//			if(pro_def == null && paraVo.m_workFlow!= null && paraVo.m_workFlow.getTaskInstanceVO()!=null)
//			{
//				pro_def = paraVo.m_workFlow.getTaskInstanceVO().getPk_process_def();
//			}
//			if(pro_def !=null)
//			{
				boolean hasRunningProcessInstances = hasRunningProcessInstances(paraVo.getBillId());
				// 该单据有流程实例
				if (hasRunningProcessInstances) {
					throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000263")/* 当前操作人没有待办任务. */);
				}
//			}
			
			// 该单据无流程实例
			IProcessDefinition processDef = null;
			if (StringUtil.isEmptyWithTrim(paraVo.getProcessDefPK()))
				processDef = NCLocator
						.getInstance()
						.lookup(IWorkflowDefine.class)
						.matchProcessDefitionAccordingBiz(paraVo.getBillType(), paraVo.getGroupPK(), paraVo.getOrgPK(),
								paraVo.getBillMaker(), paraVo.getEmendEnum());
			else
				processDef = findParsedWfProcess(paraVo.getProcessDefPK(), null);
			if (processDef == null) {
				// 该制单人无流程定义，任何人都可审批或执行
				return null;
			} else {
				throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000390")/* 该单据有可启动的流程但未启动，无法流转。 */);
			}
		}
	}

	/**
	 * 查询某人对某个单据 尚未处理的工作项
	 * @param billType
	 * @param billId
	 * @param userPK
	 * @throws PFBusinessException 
	 */
	private TaskInstanceVO[] getToDoWorkitems(String billType, String billId, String userPK) throws PFBusinessException {

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

	private List<TaskInstanceVO> filterToDoTask(String userPK, List<TaskInstanceVO> tasks) throws PFBusinessException {
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
					// 判断任务是否有协办任务
					boolean flag = false;
					if (task.getCreate_type() == TaskInstanceCreateType.Normal.getIntValue()) {
						for (TaskInstanceVO collaborationTask : tasks) {
							if (task.getActivity_id().equals(collaborationTask.getActivity_id())
									&& collaborationTask.getCreate_type() == TaskInstanceCreateType.Co_Sponsor.getIntValue()) {// 确定协办
								flag = true;
								if (collaborationTask.getIspass().booleanValue()) {
									retTasks.add(task);// 在协办完成时，主办任务才能执行
								} else {
									throw new PFBusinessException("该单据有协办任务没有完成 主办任务不能执行")/*
																						 * 该单据有协办任务没有完成
																						 * 主办任务不能执行
																						 */;
								}
							}
						}
					}
					if (!flag) {
						retTasks.add(task);
					}
				}
			}
		}
		return retTasks;
	}

	/**
	 * 判断该过程定义是否有正运行中的实例
	 * @param proc_defPK 过程定义PK
	 * @param billId
	 */
	private boolean hasRunningProcessInstances(String billId) {
		ProcessInstanceVO[] proInss = ProcessInstanceUtil.getProcessInstanceVOs(billId);
		if(proInss == null)
			return false;
		for(ProcessInstanceVO processInstanceVO : proInss)
		{
			if(processInstanceVO.getState_proins() == ProcessInstanceStatus.Started.getIntValue())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 流程实例是否执行结束
	 * @param paraVo
	 * @param taskInstanceVO
	 * @return
	 */
	private boolean isProcessInstanceFinished(WFAppParameter paraVo, TaskInstanceVO taskInstanceVO) {
		ProcessInstanceVO[] processInstances = NCLocator.getInstance().lookup(IProcessInstanceQry.class).getProcessInstanceVOs(paraVo.getBillId());
		for(ProcessInstanceVO processInstanceVO : processInstances)
		{
			if(processInstanceVO.getState_proins() == ProcessInstanceStatus.Finished.getIntValue())
			{
				return true;
			}
		}
		return false;
	}
	
	private IProcessDefinition findParsedWfProcess(String wfProcessDefPK, String processInstancePK)
			throws BusinessException {
		try {
			return ProcessDataCache.getWorkflowProcess(wfProcessDefPK, processInstancePK);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
					"UPPpfworkflow-000005")/* 查询流程定义出现XPDL解析异常： */
					+ e.getMessage());
		}
	}

	/**
	 * 启动审批流
	 * 
	 * @param paraVo
	 * @param hmPfExParams
	 */
	public boolean[] startWorkflow(WFAppParameter paraVo, HashMap hmPfExParams) throws BusinessException {
		Logger.debug("动作编码为" + paraVo.getActionName() + "，启动审批流");

		/*
		 * if(paraVo.m_workFlow==null||(paraVo.m_workFlow.getTaskInstanceVO()!=
		 * null &&
		 * StringUtil.isEmptyWithTrim(paraVo.m_workFlow.getTaskInstanceVO
		 * ()..getRejectTacheActivityID()))) // 启动时先删除旧流程信息，流程状态为"通过、进行中"的除外
		 * deleteWhenStartExceptPassOrGoing
		 * (paraVo,WorkflowTypeEnum.Approveflow.getIntValue());
		 */

		if (paraVo.getWorkFlow() == null || paraVo.getWorkFlow().getTaskInstanceVO() == null) {
			// 如果扩展参数中含有流程定义PK，则直接赋值
			Object paramDefPK = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_FLOWPK);
			paraVo.setProcessDefPK(paramDefPK == null ? null : String.valueOf(paramDefPK));

			Object noteChecked = hmPfExParams == null ? null : hmPfExParams
					.get(PfUtilBaseTools.PARAM_NOTE_CHECKED);
			// leijun+2009-7 如果前台PfUtilClient.runAction已经检查不出工作项，这里也没有必要再次检查
			///if (noteChecked == null)
				///paraVo.setWorkFlow(checkWorkflowWhenStart(paraVo));
			if (paraVo.getWorkFlow() == null)
				// 自由态且无可启动的流程定义；审批中、或已通过
				return new boolean[] { false, false };
		}

		// TaskInstanceVO startTask = paraVo.m_workFlow.getTaskInstanceVO();
		// 修正单据号后生成的情况.
		// startTask.setForm_no(paraVo.m_billNo);
		// startTask.setPk_form_ins(paraVo.m_billId);
		// startTask.setPk_form_ins_version(paraVo.m_billVersionPK);

		// 把后继活动的参与者指派信息赋值给任务对象
		// fillAssignableInfo(paraVo, startTask);

		// 任务要赋 输出业务数据,操作人,修改时间
		// startTask.setOutObject(paraVo.m_preValueVo);
		// startTask.setPk_owner(paraVo.m_operator);
		// startTask.setSigndate(new
		// UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
		// startTask.setStatus(TaskInstanceStatus.Finished.getIntValue()); //
		// 制单任务已完成

		/** 执行任务 */
		IProcessDefinition proDef = ProcessDefinitionUtil.getProDefByProDefPk(paraVo.getWorkFlow().getTaskInstanceVO().getPk_process_def());
		NextUserTaskInfoUtil.setCommitProInsCtx(paraVo.getBusinessObject(), paraVo.getOperator());
		List<UserTaskPrepCtx> userTaskPropCtx = NextUserTaskInfoUtil.getStartNextUserTaskInfo(proDef.getProDefPk(), null, paraVo.getOperator());
		List<UserTaskRunTimeCtx> runTimeCtx=new ArrayList<UserTaskRunTimeCtx>();
		boolean isMakeBill = false;
		for(int i=0;i<userTaskPropCtx.size();i++){
			UserTaskRunTimeCtx nextInfo = new UserTaskRunTimeCtx();
			nextInfo.setActivityId(userTaskPropCtx.get(i).getActivityId());
			nextInfo.setUserPks(userTaskPropCtx.get(i).getUserPks());
			runTimeCtx.add(nextInfo);
			IActivity activity = proDef.findActivity(userTaskPropCtx.get(i).getActivityId());
			ActivityBehavior activityBehavior = activity.getActivityBehavior();
			if(activityBehavior instanceof UserTaskActivityBehavior){
				TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityBehavior).getTaskDefinition();
				if(taskDefinition.isMakeBill()){
					isMakeBill = true;
				}
			}
		}
		CommitProInsCtx flowInfo = new CommitProInsCtx();
		flowInfo.setProDefPk(paraVo.getWorkFlow().getTaskInstanceVO().getPk_process_def());
		flowInfo.setUserPk(paraVo.getOperator());
		flowInfo.setNextInfo(runTimeCtx.toArray(new UserTaskRunTimeCtx[0]));
		flowInfo.setMakeBill(isMakeBill);
		//TODO:临时转换
		IFlowRequest request = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), flowInfo);
		IFlowResponse respone = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, respone);

		if(respone.getProcessInstance() != null)
		{
			respone.getProcessInstance().setVariable(this.APP_FORMINFO, paraVo.getBusinessObject());
		}

		ITask[] nextTasks = respone.getNewTasks();
		if (nextTasks == null) {
			return new boolean[] { true, false };
		}

		// 制单任务直接往下执行
		String processInstancePK = null;
		for (ITask task : nextTasks) {
			TaskInstanceVO taskVO = new TaskInstanceBridge().convertT2M(task);
			if (taskVO.getCreate_type() == TaskInstanceCreateType.Makebill.getIntValue()) {
				String oldApproveresult = paraVo.getWorkFlow().getApproveresult();
				String oldChecknote = paraVo.getWorkFlow().getChecknote();
				paraVo.getWorkFlow().setApproveresult("Y");
				paraVo.getWorkFlow().setChecknote("制单活动，自动执行");
				IFlowResponse respone1 = forwardWorkflow(paraVo, taskVO);
				paraVo.getWorkFlow().setApproveresult(oldApproveresult);
				paraVo.getWorkFlow().setChecknote(oldChecknote);
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

	/**
	 * 推动流程往下走
	 */
	public IFlowResponse forwardWorkflow(WFAppParameter paraVo, TaskInstanceVO taskVO) {
		
		// 驳回, 加签的用户也可以驳回！
		if (paraVo.getWorkFlow().getBackwardInfo().getRejectInfo() != null) {


			// 任务要赋 操作人,修改时间
			//taskVO.setFinishdate(new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
			//taskVO.setOutObject(paraVo.m_preValueVo);
			//taskVO.setPk_executer(paraVo.m_operator);
			//taskVO.setOpinion(paraVo.m_workFlow.getChecknote());
			//currentTask.setStatus(WfTaskOrInstanceStatus.Finished.getIntValue());
			//currentTask.setTaskType(WfTaskType.Backward.getIntValue());
			// FIXME::增加驳回结果?! lj@2005-5-16
			//taskVO.setApproveResult("R");

			// 够造驳回的执行信息
			RejectTaskInsCtx rejectTaskCtx = paraVo.getWorkFlow().getBackwardInfo();
			// 执行指令
			IFlowRequest request1 = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), rejectTaskCtx);
			IFlowResponse respone1 = BizProcessServer.createFlowResponse();
			BizProcessServer.execute(request1, respone1);
			return respone1;
		} else {
			// 3-审批
			// 为任务对象填充 后继活动的参与者指派信息
			//fillAssignableInfo(paraVo, currentTask);
			// 为任务对象填充 后继转移的可手工选择信息
			//fillTransitionSelectableInfo(paraVo, currentTask);
			// 任务要赋 输出业务数据,操作人,审批结果,修改时间
			//currentTask.setOutObject(paraVo.m_preValueVo);
			//currentTask.setOperator(paraVo.m_operator);
			//if ("Y".equals(paraVo.m_workFlow.getApproveresult())) {
			//	currentTask.setApproveResult("Y");
			//} else {
			//	currentTask.setApproveResult("N");
			//}
			//currentTask.setModifyTime(new UFDateTime(System.currentTimeMillis()));
			//currentTask.setNote(paraVo.m_workFlow.getChecknote());
			//currentTask.setStatus(WfTaskOrInstanceStatus.Finished.getIntValue());
		
			// 够造下一步的执行信息
			NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
			// 设置当前任务
			nextTaskCtx.setTaskPk(taskVO.getPk_task());
			// 设置当前用户
			nextTaskCtx.setUserPk(paraVo.getOperator());
			nextTaskCtx.setComment(paraVo.getWorkFlow().getChecknote());
			
			if(paraVo.getWorkFlow().getApproveresult()!=null&&paraVo.getWorkFlow().getApproveresult().equals("Y"))
			{
				nextTaskCtx.setPass(true);
			}else{
			    nextTaskCtx.setPass(false);
			}
			
			Map<String,List<String>> assignInfoMap = paraVo.getWorkFlow().getAssignInfoMap();
			List<UserTaskRunTimeCtx> nextInfo = new ArrayList<UserTaskRunTimeCtx>();
			if(paraVo.getWorkFlow().isAssign() && assignInfoMap != null 
					&& assignInfoMap.size() >0)
			{
				Map<String,Boolean> assignModeInfoMap = paraVo.getWorkFlow().getAssignModeInfoMap();
				for (String activityID : assignInfoMap.keySet()) {
					List<String> users = assignInfoMap.get(activityID);
					UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
					runTimeCtx.setActivityId(activityID);
					runTimeCtx.setUserPks(users.toArray(new String[0]));
					runTimeCtx.setSequence(!assignModeInfoMap.get(activityID));
					runTimeCtx.setMsgType(new MsgType[] { MsgType.MSGCENTER });
					nextInfo.add(runTimeCtx);
				}
				paraVo.getWorkFlow().setAssign(false);
			}
			
			NextUserTaskInfoUtil.setNextTaskInsCtx(paraVo.getBusinessObject(), paraVo.getOperator(), nextTaskCtx.isPass());
			List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(taskVO.getPk_task(), paraVo.getBusinessObject(),paraVo.getOperator());
			for (int i = 0; i < userTaskPrepCtx.size(); i++) {
				if(assignInfoMap.containsKey(userTaskPrepCtx.get(i).getActivityId()))
					continue;
				UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
				runTimeCtx.setActivityId(userTaskPrepCtx.get(i).getActivityId());
				runTimeCtx.setUserPks(userTaskPrepCtx.get(i).getUserPks());
				runTimeCtx.setMsgType(new MsgType[] { MsgType.MSGCENTER });
				nextInfo.add(runTimeCtx);

			}
			nextTaskCtx.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));
			// 执行指令
			IFlowRequest request1 = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), nextTaskCtx);
			IFlowResponse respone1 = BizProcessServer.createFlowResponse();
			BizProcessServer.execute(request1, respone1);

			return respone1;
		}
	}

	/**
	 * 根据现有任务构造单据上下文信息
	 */
	private IBusinessKey BuildFormInfoCtxFromTaskInstanceVO(WorkflownoteVO workflownoteVO) {
		IBusinessKey formInfo = new BizObjectImpl();
		TaskInstanceVO taskInstanceVO = workflownoteVO.getTaskInstanceVO();
		if(taskInstanceVO == null)
			return formInfo;
		formInfo.setBillType(taskInstanceVO.getPk_bizobject());
		//formInfo.setTranstype(taskInstanceVO.getPk_biztrans());
		//formInfo.setPkorg(taskInstanceVO.getPk_org());
		formInfo.setBillNo(taskInstanceVO.getForm_no());
		formInfo.setBillId(taskInstanceVO.getPk_form_ins());
		//formInfo.setBillVersionPK(taskInstanceVO.getPk_form_ins_version());
		//formInfo.setBillVos(workflownoteVO.get);
		//formInfo.setPk_group(taskInstanceVO.getPk_group());
//		if(workflownoteVO != null)
//		{
//			formInfo.setApproveNote(workflownoteVO.getChecknote());
//		}
		return formInfo;
	}

	/**
	 * 推动流程往下走
	 */
	public int signalWorkflow(WFAppParameter paraVo) throws BusinessException {
		Logger.error("###WFEngineServiceImpl signalWorkflow 开始 " + System.currentTimeMillis() + "ms");
		if (paraVo.getWorkFlow() != null && paraVo.getWorkFlow().getTaskInstanceVO() != null) {
			this.forwardWorkflow(paraVo, paraVo.getWorkFlow().getTaskInstanceVO());
		}

		WorkflowStausUtil workflowStaus = new WorkflowStausUtil();
		return workflowStaus.queryFlowStatus(paraVo);
	}

	/**
	 * 根据指定task pk得到任务
	 */
	public WorkflownoteVO getWorkitem(String pk_task) throws BusinessException {
		Logger.error("###WFEngineServiceImpl getWorkitem 开始 " + System.currentTimeMillis() + "ms");

		// 根据taskpk查询工作项
		TaskInstanceVO task = NCLocator.getInstance().lookup(ITaskInstanceQry.class).getTaskInsVoByPk(pk_task);
		if (task != null) {
			WorkflownoteVO workflownoteVO = new WorkflownoteVO();
			workflownoteVO.setTaskInstanceVO(task);
			List<TaskDefinition> taskDefinitions = ProcessDefinitionUtil.getTaskDefinition(task.getPk_task(),task.getPk_process_def(),
					null, null);
			for(TaskDefinition taskDefinition : taskDefinitions)
			{
				if(taskDefinition.isAssign())
				{
					workflownoteVO.setAssign(true);
				}
			}
			return workflownoteVO;
		}
		return null;
	}

	/**
	 * 收回、取消提交
	 * @param paraVo
	 */
	public ReturnBackWfVo cancelSubmitWorkflow(WFAppParameter paraVo) throws BusinessException {
		ReturnBackWfVo retBackVO = new ReturnBackWfVo();
		// 只有本人提交的流程才可收回
		String billMaker = ProcessInstanceUtil.getProInsStartupPerson(paraVo.getBillId(), paraVo.getBillType());
		if (billMaker == null) {
			// 判断是否有流程定义
			String proDef = ProcessInstanceUtil.getProcessDefinitionOfProcessInstance(paraVo.getBillId(),paraVo.getBillType());
			if (proDef != null)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"wfMachineImpl-0007")/* 单据尚未提交，无法取消！ */);
		} else if (!billMaker.equals(paraVo.getOperator())) {
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0008")/* 只能取消本人提交的单据！ */);
		} else {
			// 查询后续人工活动是否已处理，已处理，则不能收回；
			List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
					.getFinishedTasks(paraVo.getBillType(), paraVo.getBillId(), null);
			if (tasks != null && tasks.size() > 0) {
				// 当前操作员有已办工作项
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"wfMachineImpl-0009")/* 后续活动已经完成，无法取消提交！ */);
			}
		}

		// 终止流程实例，单据变为自由态
		new WFAdminServiceImpl().terminateWorkflow(paraVo);

		// 任务执行完成后的处理
		// 状态回退为自由态
		retBackVO.setBackState(IReturnCheckInfo.NOSTATE);
		return retBackVO;
	}

	/**
	 * ROLLBACK时的工作流回退处理，UNAPPROVE时的审批流弃审处理, 只支持逐级回退
	 * @param paraVo
	 */
	public ReturnBackWfVo rollbackWorkflow(WFAppParameter paraVo) throws BusinessException {
		ReturnBackWfVo retBackVO = new ReturnBackWfVo();
		// 查询有无 已完成的工作流工作项
//		TaskInstanceVO taskInstanceVO = getFinishedWorkitem(paraVo.getBillType(), paraVo.getBillId(),
//				paraVo.getOperator());
		/***
		 * 注掉上面代码的原因：只有本人有已办任务的前提下才能审批，与逻辑上的驳回不符合
		 * 改为：驳回本身的这条未办的任务
		 * */
		TaskInstanceVO taskInstanceVO = paraVo.getWorkFlow().getTaskInstanceVO();
		
		// /没有已完成的工作项（也没有未完成的工作流工作项？？，或者有未完成的工作流工作项？？）
		if (taskInstanceVO == null) {
			// 如果没有已完成的工作项，则直接返回
			retBackVO.setIsFinish(UFBoolean.TRUE);
			retBackVO.setBackState(IReturnCheckInfo.NOSTATE);
			return retBackVO;
		}

		boolean isFinished = isProcessInstanceFinished(paraVo, taskInstanceVO);
		if (isFinished) {
			// 当前单据是在流程结束后回退的
			retBackVO.setIsFinish(UFBoolean.TRUE);
		}

		CallBackTaskInsCtx callBackTaskCtx = new CallBackTaskInsCtx();
		callBackTaskCtx.setTaskPk(taskInstanceVO.getPk_task());
		IFlowRequest request1 = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), callBackTaskCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);

		// 任务执行完成后的处理
		// 查询是否存在一个已处理完成的工作项，如果有表示该流程弃审为"进行中"
		List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
				.getFinishedTasks(paraVo.getBillType(), paraVo.getBillId(), null);

		if (tasks != null && tasks.size() > 0) {
			// 状态回退为进行中
			retBackVO.setBackState(IReturnCheckInfo.GOINGON);
			retBackVO.setPreCheckMan(taskInstanceVO.getPk_creater());
			if (isFinished) {
				// 审批环节多于1个时，最后一个人弃审要通知制单人
				// TODO:
				// WorknoteManager noteMgr = new WorknoteManager();
				// noteMgr.sendMessageToBillMaker(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
				// "wfMachineImpl-0011")/*流程回退*/, paraVo);
			}
		} else {
			// 状态回退为提交态
			retBackVO.setBackState(IReturnCheckInfo.COMMIT);
			// TODO:
			// WorknoteManager noteMgr = new WorknoteManager();
			// noteMgr.sendMessageToBillMaker(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
			// "wfMachineImpl-0011")/*流程回退*/, paraVo);
		}
		return retBackVO;
	}

	/**
	 * 检查某用户已完成的工作项
	 * <li>如果无已完成工作项，且有流程实例，则抛出异常
	 * @param billType
	 * @param billId
	 * @param checkman
	 */
	private TaskInstanceVO getFinishedWorkitem(String billType, String billId, String checkman) throws BusinessException {
		// 查询某用户对某单据的所有已办工作项
		List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class).getFinishedTasks(billType, billId, checkman);
		if (tasks != null && tasks.size() > 0) {
			// 当前操作员有已办工作项
			return tasks.get(0);
		} else {
			// 当前操作员无已办工作项
			boolean isStartup = hasRunningProcessInstances(billId);
			if (!isStartup) {
				// 该单据无流程实例
				return null;
			} else {
				throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000267")/* 当前操作人没有已办工作项，无法回退 */);
			}
		}
	}
	
	public String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException {
		return "";
	}

	public void appointWorkitem(String billId, String pk_workflownote,
			String checkman, String userID) throws BusinessException {
	}

	/*前加签*/
	public void beforeAddSign(WorkflownoteVO noteVO) throws BusinessException {
		CreateBeforeAddSignCtx beforeAddSignCtx = noteVO.getBeforeAddSignCtx();
		IBusinessKey formInfo = BuildFormInfoCtxFromTaskInstanceVO(noteVO);
		IFlowRequest request1 = BizProcessServer.createFlowRequest(formInfo, beforeAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}

	/*后加签*/
	public void afterAddSign(WorkflownoteVO noteVO) throws BusinessException {
		CreateAfterAddSignCtx afterAddSignCtx=noteVO.getAfterAddSignCtx();
		IBusinessKey formInfo = BuildFormInfoCtxFromTaskInstanceVO(noteVO);
		IFlowRequest request1 = BizProcessServer.createFlowRequest(formInfo, afterAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}
	
	/*改派*/
	public void delegateTask(WorkflownoteVO noteVo,List<String> turnUserPks, String currentUserId) throws BusinessException {
		DelegateTaskInsCtx taskInfoCtx=new DelegateTaskInsCtx();
		taskInfoCtx.setTaskPk(noteVo.getTaskInstanceVO().getPk_task());
		taskInfoCtx.setUserPk(currentUserId);
		taskInfoCtx.setTurnUserPk(turnUserPks);
	    IBusinessKey formInfo = BuildFormInfoCtxFromTaskInstanceVO(noteVo);
		BizProcessServer.execute(BizProcessServer.createFlowRequest(formInfo, taskInfoCtx), BizProcessServer.createFlowResponse());
	}

	public List<TaskInstanceVO> getTasks(int taskState, String keyWord, String bizObject, String pk_user,
			String wherePart, boolean isGetMyStartTask, Pagination page) {
		return TaskUtil.getTasks(taskState, keyWord, bizObject, pk_user, wherePart,isGetMyStartTask, page);
	}
}