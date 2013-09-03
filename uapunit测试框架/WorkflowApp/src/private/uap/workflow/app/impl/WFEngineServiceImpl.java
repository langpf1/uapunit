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
 * �������������ʵ����
 * 
 * @author wzhy 2004-2-21
 * @modifier leijun 2006-4-7 ʹ�ö�̬�����Ʋ����ͷ�����
 * @modifier leijun 2008-8 ���ӹ�������ش���
 * @modifier leijun 2008-12 ������չ���������Ƿ�����װ��VO
 * @modifier guowl 2010-5 6.0����֧��һ�����׵�ģʽ
 */
public class WFEngineServiceImpl implements IWFEngineService {
	public static final String APP_FORMINFO = "APP_FORMINFO";

	public WFEngineServiceImpl() {
	}

	/**
	 * ����"�ύ"����������ʱ����
	 * @param paraVo  ����ִ�в���VO
	 * @param status  ����״̬������̬���ύ̬��δͨ��
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
			// ��ѯ�����������̶���
			if (StringUtil.isEmptyWithTrim(paraVo.getProcessDefPK()))
				processDef = NCLocator.getInstance().lookup(IWorkflowDefine.class)
						.matchProcessDefitionAccordingBiz(paraVo.getGroupPK(), paraVo.getBillType(), paraVo.getOrgPK(),
								paraVo.getBillMaker(), paraVo.getEmendEnum());
			else
				processDef = findParsedWfProcess(paraVo.getProcessDefPK(), null);

			if (processDef == null) {
				// û�ж�Ӧ������������,�κ��˶�������ͨ��
				return null;
			}

			// �½�����Ҫ�� ������ID,�ID�͵��ݺ�
			if (beginTask == null) {
				beginTask = new TaskInstanceVO();
				beginTask.setCreate_type(TaskInstanceCreateType.Makebill.getIntValue());
				//beginTask.setPk_org(paraVo.m_pkOrg);
				beginTask.setPk_task(new SequenceGenerator().generate());
				beginTask.setBegindate(new UFDateTime(paraVo.getBizDateTime()));
			}
			beginTask.setPk_process_def(processDef.getProDefPk());
			beginTask.setActivity_id(processDef.getInitial().getId());// ��ʼId���
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
	 * ���ĳ�û��Ĵ��칤���� <li>����޴��칤�����������Ѿ����������׳��쳣
	 * 
	 * @param paraVo
	 * @return WorkflownoteVO
	 */
	public WorkflownoteVO checkUnfinishedWorkitem(WFAppParameter paraVo)
			throws BusinessException, DbException {
		// ��ѯĳ�û���ĳ���ݵ����д��칤����
		TaskInstanceVO[] todoTasks = getToDoWorkitems(paraVo.getBillType(), paraVo.getBillId(), paraVo.getOperator());
		if (todoTasks != null && todoTasks.length > 0) {
			// ��ǰ����Ա�д��칤����
			// FIXME:ĳ�û���ĳ�����ݿ���ͬʱ���������칤������
			TaskInstanceVO todoTaskVO = todoTasks[todoTasks.length - 1];
			if (todoTaskVO.getCreate_type() == TaskInstanceCreateType.Makebill.getIntValue()
					|| todoTaskVO.getCreate_type() == TaskInstanceCreateType.Withdraw.getIntValue())
				throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000388")/* û�д������� */);

			// У������ʵ����״̬
			if (!hasRunningProcessInstances(paraVo.getBillId())) {
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"EngineService-0004")/* �õ�������������ʵ���ѱ����� */);
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
				throw new PFBusinessException("��ǰ�����˴��������Ѿ���ɣ�");
			}

			// ��ǰ����Ա�޴��칤����
//			String pro_def = paraVo.m_flowDefPK;
//			if(pro_def == null && paraVo.m_workFlow!= null && paraVo.m_workFlow.getTaskInstanceVO()!=null)
//			{
//				pro_def = paraVo.m_workFlow.getTaskInstanceVO().getPk_process_def();
//			}
//			if(pro_def !=null)
//			{
				boolean hasRunningProcessInstances = hasRunningProcessInstances(paraVo.getBillId());
				// �õ���������ʵ��
				if (hasRunningProcessInstances) {
					throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
							"UPPpfworkflow-000263")/* ��ǰ������û�д�������. */);
				}
//			}
			
			// �õ���������ʵ��
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
				// ���Ƶ��������̶��壬�κ��˶���������ִ��
				return null;
			} else {
				throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000390")/* �õ����п����������̵�δ�������޷���ת�� */);
			}
		}
	}

	/**
	 * ��ѯĳ�˶�ĳ������ ��δ����Ĺ�����
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
					// �ж������Ƿ���Э������
					boolean flag = false;
					if (task.getCreate_type() == TaskInstanceCreateType.Normal.getIntValue()) {
						for (TaskInstanceVO collaborationTask : tasks) {
							if (task.getActivity_id().equals(collaborationTask.getActivity_id())
									&& collaborationTask.getCreate_type() == TaskInstanceCreateType.Co_Sponsor.getIntValue()) {// ȷ��Э��
								flag = true;
								if (collaborationTask.getIspass().booleanValue()) {
									retTasks.add(task);// ��Э�����ʱ�������������ִ��
								} else {
									throw new PFBusinessException("�õ�����Э������û����� ����������ִ��")/*
																						 * �õ�����Э������û�����
																						 * ����������ִ��
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
	 * �жϸù��̶����Ƿ����������е�ʵ��
	 * @param proc_defPK ���̶���PK
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
	 * ����ʵ���Ƿ�ִ�н���
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
					"UPPpfworkflow-000005")/* ��ѯ���̶������XPDL�����쳣�� */
					+ e.getMessage());
		}
	}

	/**
	 * ����������
	 * 
	 * @param paraVo
	 * @param hmPfExParams
	 */
	public boolean[] startWorkflow(WFAppParameter paraVo, HashMap hmPfExParams) throws BusinessException {
		Logger.debug("��������Ϊ" + paraVo.getActionName() + "������������");

		/*
		 * if(paraVo.m_workFlow==null||(paraVo.m_workFlow.getTaskInstanceVO()!=
		 * null &&
		 * StringUtil.isEmptyWithTrim(paraVo.m_workFlow.getTaskInstanceVO
		 * ()..getRejectTacheActivityID()))) // ����ʱ��ɾ����������Ϣ������״̬Ϊ"ͨ����������"�ĳ���
		 * deleteWhenStartExceptPassOrGoing
		 * (paraVo,WorkflowTypeEnum.Approveflow.getIntValue());
		 */

		if (paraVo.getWorkFlow() == null || paraVo.getWorkFlow().getTaskInstanceVO() == null) {
			// �����չ�����к������̶���PK����ֱ�Ӹ�ֵ
			Object paramDefPK = hmPfExParams == null ? null : hmPfExParams.get(PfUtilBaseTools.PARAM_FLOWPK);
			paraVo.setProcessDefPK(paramDefPK == null ? null : String.valueOf(paramDefPK));

			Object noteChecked = hmPfExParams == null ? null : hmPfExParams
					.get(PfUtilBaseTools.PARAM_NOTE_CHECKED);
			// leijun+2009-7 ���ǰ̨PfUtilClient.runAction�Ѿ���鲻�����������Ҳû�б�Ҫ�ٴμ��
			///if (noteChecked == null)
				///paraVo.setWorkFlow(checkWorkflowWhenStart(paraVo));
			if (paraVo.getWorkFlow() == null)
				// ����̬���޿����������̶��壻�����С�����ͨ��
				return new boolean[] { false, false };
		}

		// TaskInstanceVO startTask = paraVo.m_workFlow.getTaskInstanceVO();
		// �������ݺź����ɵ����.
		// startTask.setForm_no(paraVo.m_billNo);
		// startTask.setPk_form_ins(paraVo.m_billId);
		// startTask.setPk_form_ins_version(paraVo.m_billVersionPK);

		// �Ѻ�̻�Ĳ�����ָ����Ϣ��ֵ���������
		// fillAssignableInfo(paraVo, startTask);

		// ����Ҫ�� ���ҵ������,������,�޸�ʱ��
		// startTask.setOutObject(paraVo.m_preValueVo);
		// startTask.setPk_owner(paraVo.m_operator);
		// startTask.setSigndate(new
		// UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
		// startTask.setStatus(TaskInstanceStatus.Finished.getIntValue()); //
		// �Ƶ����������

		/** ִ������ */
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
		//TODO:��ʱת��
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

		// �Ƶ�����ֱ������ִ��
		String processInstancePK = null;
		for (ITask task : nextTasks) {
			TaskInstanceVO taskVO = new TaskInstanceBridge().convertT2M(task);
			if (taskVO.getCreate_type() == TaskInstanceCreateType.Makebill.getIntValue()) {
				String oldApproveresult = paraVo.getWorkFlow().getApproveresult();
				String oldChecknote = paraVo.getWorkFlow().getChecknote();
				paraVo.getWorkFlow().setApproveresult("Y");
				paraVo.getWorkFlow().setChecknote("�Ƶ�����Զ�ִ��");
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
				// �ύ������ͨ��
				return new boolean[] { true, true };
			}
		}
		return new boolean[] { true, false };
	}

	/**
	 * �ƶ�����������
	 */
	public IFlowResponse forwardWorkflow(WFAppParameter paraVo, TaskInstanceVO taskVO) {
		
		// ����, ��ǩ���û�Ҳ���Բ��أ�
		if (paraVo.getWorkFlow().getBackwardInfo().getRejectInfo() != null) {


			// ����Ҫ�� ������,�޸�ʱ��
			//taskVO.setFinishdate(new UFDateTime(InvocationInfoProxy.getInstance().getBizDateTime()));
			//taskVO.setOutObject(paraVo.m_preValueVo);
			//taskVO.setPk_executer(paraVo.m_operator);
			//taskVO.setOpinion(paraVo.m_workFlow.getChecknote());
			//currentTask.setStatus(WfTaskOrInstanceStatus.Finished.getIntValue());
			//currentTask.setTaskType(WfTaskType.Backward.getIntValue());
			// FIXME::���Ӳ��ؽ��?! lj@2005-5-16
			//taskVO.setApproveResult("R");

			// ���첵�ص�ִ����Ϣ
			RejectTaskInsCtx rejectTaskCtx = paraVo.getWorkFlow().getBackwardInfo();
			// ִ��ָ��
			IFlowRequest request1 = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), rejectTaskCtx);
			IFlowResponse respone1 = BizProcessServer.createFlowResponse();
			BizProcessServer.execute(request1, respone1);
			return respone1;
		} else {
			// 3-����
			// Ϊ���������� ��̻�Ĳ�����ָ����Ϣ
			//fillAssignableInfo(paraVo, currentTask);
			// Ϊ���������� ���ת�ƵĿ��ֹ�ѡ����Ϣ
			//fillTransitionSelectableInfo(paraVo, currentTask);
			// ����Ҫ�� ���ҵ������,������,�������,�޸�ʱ��
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
		
			// ������һ����ִ����Ϣ
			NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
			// ���õ�ǰ����
			nextTaskCtx.setTaskPk(taskVO.getPk_task());
			// ���õ�ǰ�û�
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
			// ִ��ָ��
			IFlowRequest request1 = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), nextTaskCtx);
			IFlowResponse respone1 = BizProcessServer.createFlowResponse();
			BizProcessServer.execute(request1, respone1);

			return respone1;
		}
	}

	/**
	 * �������������쵥����������Ϣ
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
	 * �ƶ�����������
	 */
	public int signalWorkflow(WFAppParameter paraVo) throws BusinessException {
		Logger.error("###WFEngineServiceImpl signalWorkflow ��ʼ " + System.currentTimeMillis() + "ms");
		if (paraVo.getWorkFlow() != null && paraVo.getWorkFlow().getTaskInstanceVO() != null) {
			this.forwardWorkflow(paraVo, paraVo.getWorkFlow().getTaskInstanceVO());
		}

		WorkflowStausUtil workflowStaus = new WorkflowStausUtil();
		return workflowStaus.queryFlowStatus(paraVo);
	}

	/**
	 * ����ָ��task pk�õ�����
	 */
	public WorkflownoteVO getWorkitem(String pk_task) throws BusinessException {
		Logger.error("###WFEngineServiceImpl getWorkitem ��ʼ " + System.currentTimeMillis() + "ms");

		// ����taskpk��ѯ������
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
	 * �ջء�ȡ���ύ
	 * @param paraVo
	 */
	public ReturnBackWfVo cancelSubmitWorkflow(WFAppParameter paraVo) throws BusinessException {
		ReturnBackWfVo retBackVO = new ReturnBackWfVo();
		// ֻ�б����ύ�����̲ſ��ջ�
		String billMaker = ProcessInstanceUtil.getProInsStartupPerson(paraVo.getBillId(), paraVo.getBillType());
		if (billMaker == null) {
			// �ж��Ƿ������̶���
			String proDef = ProcessInstanceUtil.getProcessDefinitionOfProcessInstance(paraVo.getBillId(),paraVo.getBillType());
			if (proDef != null)
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"wfMachineImpl-0007")/* ������δ�ύ���޷�ȡ���� */);
		} else if (!billMaker.equals(paraVo.getOperator())) {
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
					"wfMachineImpl-0008")/* ֻ��ȡ�������ύ�ĵ��ݣ� */);
		} else {
			// ��ѯ�����˹���Ƿ��Ѵ����Ѵ��������ջأ�
			List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
					.getFinishedTasks(paraVo.getBillType(), paraVo.getBillId(), null);
			if (tasks != null && tasks.size() > 0) {
				// ��ǰ����Ա���Ѱ칤����
				throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
						"wfMachineImpl-0009")/* ������Ѿ���ɣ��޷�ȡ���ύ�� */);
			}
		}

		// ��ֹ����ʵ�������ݱ�Ϊ����̬
		new WFAdminServiceImpl().terminateWorkflow(paraVo);

		// ����ִ����ɺ�Ĵ���
		// ״̬����Ϊ����̬
		retBackVO.setBackState(IReturnCheckInfo.NOSTATE);
		return retBackVO;
	}

	/**
	 * ROLLBACKʱ�Ĺ��������˴���UNAPPROVEʱ��������������, ֻ֧���𼶻���
	 * @param paraVo
	 */
	public ReturnBackWfVo rollbackWorkflow(WFAppParameter paraVo) throws BusinessException {
		ReturnBackWfVo retBackVO = new ReturnBackWfVo();
		// ��ѯ���� ����ɵĹ�����������
//		TaskInstanceVO taskInstanceVO = getFinishedWorkitem(paraVo.getBillType(), paraVo.getBillId(),
//				paraVo.getOperator());
		/***
		 * ע����������ԭ��ֻ�б������Ѱ������ǰ���²������������߼��ϵĲ��ز�����
		 * ��Ϊ�����ر��������δ�������
		 * */
		TaskInstanceVO taskInstanceVO = paraVo.getWorkFlow().getTaskInstanceVO();
		
		// /û������ɵĹ����Ҳû��δ��ɵĹ��������������������δ��ɵĹ��������������
		if (taskInstanceVO == null) {
			// ���û������ɵĹ������ֱ�ӷ���
			retBackVO.setIsFinish(UFBoolean.TRUE);
			retBackVO.setBackState(IReturnCheckInfo.NOSTATE);
			return retBackVO;
		}

		boolean isFinished = isProcessInstanceFinished(paraVo, taskInstanceVO);
		if (isFinished) {
			// ��ǰ�����������̽�������˵�
			retBackVO.setIsFinish(UFBoolean.TRUE);
		}

		CallBackTaskInsCtx callBackTaskCtx = new CallBackTaskInsCtx();
		callBackTaskCtx.setTaskPk(taskInstanceVO.getPk_task());
		IFlowRequest request1 = BizProcessServer.createFlowRequest(paraVo.getBusinessObject(), callBackTaskCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);

		// ����ִ����ɺ�Ĵ���
		// ��ѯ�Ƿ����һ���Ѵ�����ɵĹ��������б�ʾ����������Ϊ"������"
		List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class)
				.getFinishedTasks(paraVo.getBillType(), paraVo.getBillId(), null);

		if (tasks != null && tasks.size() > 0) {
			// ״̬����Ϊ������
			retBackVO.setBackState(IReturnCheckInfo.GOINGON);
			retBackVO.setPreCheckMan(taskInstanceVO.getPk_creater());
			if (isFinished) {
				// �������ڶ���1��ʱ�����һ��������Ҫ֪ͨ�Ƶ���
				// TODO:
				// WorknoteManager noteMgr = new WorknoteManager();
				// noteMgr.sendMessageToBillMaker(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
				// "wfMachineImpl-0011")/*���̻���*/, paraVo);
			}
		} else {
			// ״̬����Ϊ�ύ̬
			retBackVO.setBackState(IReturnCheckInfo.COMMIT);
			// TODO:
			// WorknoteManager noteMgr = new WorknoteManager();
			// noteMgr.sendMessageToBillMaker(NCLangRes4VoTransl.getNCLangRes().getStrByID("pfworkflow",
			// "wfMachineImpl-0011")/*���̻���*/, paraVo);
		}
		return retBackVO;
	}

	/**
	 * ���ĳ�û�����ɵĹ�����
	 * <li>���������ɹ������������ʵ�������׳��쳣
	 * @param billType
	 * @param billId
	 * @param checkman
	 */
	private TaskInstanceVO getFinishedWorkitem(String billType, String billId, String checkman) throws BusinessException {
		// ��ѯĳ�û���ĳ���ݵ������Ѱ칤����
		List<TaskInstanceVO> tasks = NCLocator.getInstance().lookup(ITaskInstanceQry.class).getFinishedTasks(billType, billId, checkman);
		if (tasks != null && tasks.size() > 0) {
			// ��ǰ����Ա���Ѱ칤����
			return tasks.get(0);
		} else {
			// ��ǰ����Ա���Ѱ칤����
			boolean isStartup = hasRunningProcessInstances(billId);
			if (!isStartup) {
				// �õ���������ʵ��
				return null;
			} else {
				throw new PFBusinessException(NCLangResOnserver.getInstance().getStrByID("pfworkflow",
						"UPPpfworkflow-000267")/* ��ǰ������û���Ѱ칤����޷����� */);
			}
		}
	}
	
	public String findParentProcessInstancePK(String subProcessInstancePK) throws DAOException {
		return "";
	}

	public void appointWorkitem(String billId, String pk_workflownote,
			String checkman, String userID) throws BusinessException {
	}

	/*ǰ��ǩ*/
	public void beforeAddSign(WorkflownoteVO noteVO) throws BusinessException {
		CreateBeforeAddSignCtx beforeAddSignCtx = noteVO.getBeforeAddSignCtx();
		IBusinessKey formInfo = BuildFormInfoCtxFromTaskInstanceVO(noteVO);
		IFlowRequest request1 = BizProcessServer.createFlowRequest(formInfo, beforeAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}

	/*���ǩ*/
	public void afterAddSign(WorkflownoteVO noteVO) throws BusinessException {
		CreateAfterAddSignCtx afterAddSignCtx=noteVO.getAfterAddSignCtx();
		IBusinessKey formInfo = BuildFormInfoCtxFromTaskInstanceVO(noteVO);
		IFlowRequest request1 = BizProcessServer.createFlowRequest(formInfo, afterAddSignCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	}
	
	/*����*/
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