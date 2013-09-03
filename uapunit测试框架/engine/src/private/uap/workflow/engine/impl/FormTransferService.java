package uap.workflow.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import uap.workflow.app.core.BizObjectImpl;
import uap.workflow.app.core.IBusinessKey;
import uap.workflow.app.core.IFlowRequest;
import uap.workflow.app.core.IFlowResponse;
import uap.workflow.engine.bpmn.behavior.MultiInstanceActivityBehavior;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.context.SubmitStartFormCtx;
import uap.workflow.engine.context.SubmitTaskFormCtx;
import uap.workflow.engine.context.UserTaskPrepCtx;
import uap.workflow.engine.context.UserTaskRunTimeCtx;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.form.FormData;
import uap.workflow.engine.form.FormProperty;
import uap.workflow.engine.itf.IFormTransferService;
import uap.workflow.engine.message.MsgType;
import uap.workflow.engine.server.BizProcessServer;
import uap.workflow.engine.utils.NextUserTaskInfoUtil;
import uap.workflow.engine.utils.TaskUtil;
import uap.workflow.engine.vos.TaskInstanceVO;

public class FormTransferService implements IFormTransferService {

//	public StartFormData getStartFormProperties(String proDefPk) {
//		return BizProcessServer.getProcessEngine().getFormService().getStartFormData(proDefPk);
//	}

	public List<FormProperty> getStartFormProperties(String proDefPk) {
		return BizProcessServer.getProcessEngine().getFormService().getStartFormData(proDefPk).getFormProperties();
	}

	@Override
	public Object getStartFormTemplate(String proDefPk) {
		return BizProcessServer.getProcessEngine().getFormService().getRenderedStartForm(proDefPk);
	}

//	public TaskFormData getTaskFormProperties(String taskId) {
//		return BizProcessServer.getProcessEngine().getFormService().getTaskFormData(taskId);
//	}

	public List<FormProperty> getTaskFormProperties(String taskId) {
		FormData data = BizProcessServer.getProcessEngine().getFormService().getTaskFormData(taskId);
		return data.getFormProperties(); 
	}

	@Override
	public Object getTaskFormTemplate(String taskId) {
		return BizProcessServer.getProcessEngine().getFormService().getRenderedTaskForm(taskId);
	}
	/**
	 * 构造单据上下文信息
	 */
	private IBusinessKey BuildFormInfoCtx(TaskEntity task) {
		TaskInstanceVO  bizObject = task.getTaskInsVo();
		Object vo = null;
		IActivityInstance entity = task.getExecution().getParent();
		IActivityInstance supera = entity.getSuperExecution(); 
		IActivity activity = task.getExecution().getActivity();
		if (activity.getParentActivity() != null){
			activity = activity.getParentActivity();
			if (activity.getActivityBehavior() instanceof MultiInstanceActivityBehavior){
				String varName = ((MultiInstanceActivityBehavior)activity.getActivityBehavior()).getCollectionElementVariable();
				vo = (Object)((TaskEntity)task).getVariable(varName);
			}
		}
		
		IBusinessKey formInfo = new BizObjectImpl();
		formInfo.setBillType(bizObject.getPk_bizobject());
		//formInfo.setTranstype(bizObject.getPk_biztrans());
		//formInfo.setPkorg(bizObject.getPk_org());
		formInfo.setBillNo(bizObject.getForm_no());
		formInfo.setBillId(bizObject.getPk_form_ins());
		//formInfo.setBillVersionPK(bizObject.getPk_form_ins_version());
		//formInfo.setBillVos(new Object[]{vo});
		//formInfo.setPk_group(bizObject.getPk_group());
		//formInfo.setApproveNote("Form Submit ");
		return formInfo;
	}


	@Override
	public void submitTaskFormData(String pkTask, Map<String, String> properties) {
		SubmitTaskFormCtx flowInfo = new SubmitTaskFormCtx();
		flowInfo.setTaskPk(pkTask);
		flowInfo.setProperties(properties);

//		IFlowRequest request = BizProcessServer.createFlowRequest(null, flowInfo);
//		IFlowResponse respone = BizProcessServer.createFlowResponse();
//		BizProcessServer.exec(request, respone);

		//BizProcessServer.getProcessEngine().getFormService().submitTaskFormData(taskId, properties);
		IBusinessKey formInfo = null;
		ITask task = TaskUtil.getTaskByTaskPk(pkTask);
		formInfo = BuildFormInfoCtx((TaskEntity)task);
		List<UserTaskPrepCtx> userTaskPrepCtx = NextUserTaskInfoUtil.getNextUserTaskInfo(pkTask,formInfo,null);
		// 够造下一步的执行信息
		NextTaskInsCtx nextTaskCtx = new NextTaskInsCtx();
		// 设置当前任务
		nextTaskCtx.setTaskPk(pkTask);
		// 设置当前用户
		nextTaskCtx.setUserPk(InvocationInfoProxy.getInstance().getUserId());
		nextTaskCtx.setComment("Form Submit");
		List<UserTaskRunTimeCtx> nextInfo=new ArrayList<UserTaskRunTimeCtx>();
		for(int i=0;i<userTaskPrepCtx.size();i++){
			UserTaskRunTimeCtx runTimeCtx = new UserTaskRunTimeCtx();
			runTimeCtx.setActivityId(userTaskPrepCtx.get(i).getActivityId());
			runTimeCtx.setUserPks(userTaskPrepCtx.get(i).getUserPks());
			runTimeCtx.setMsgType(new MsgType[]{MsgType.MSGCENTER});
			nextInfo.add(runTimeCtx);
			
		}
		nextTaskCtx.setNextInfo(nextInfo.toArray(new UserTaskRunTimeCtx[0]));
		
		if(userTaskPrepCtx!=null&&userTaskPrepCtx.size()>0){
	
			
		}
		// 执行指令
		IFlowRequest request1 = BizProcessServer.createFlowRequest(formInfo, nextTaskCtx);
		IFlowResponse respone1 = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request1, respone1);
	
		
	}

	@Override
	public void submitStartFormData(String proDefPk, Map<String, String> properties) {
		SubmitStartFormCtx flowInfo = new SubmitStartFormCtx();
		flowInfo.setProcessDefinitionId(proDefPk);
		flowInfo.setProperties(properties);

		IFlowRequest request = BizProcessServer.createFlowRequest(null, flowInfo);
		IFlowResponse respone = BizProcessServer.createFlowResponse();
		BizProcessServer.execute(request, respone);
		
		//BizProcessServer.getProcessEngine().getFormService().submitStartFormData(proDefPk, properties);
	}

}
