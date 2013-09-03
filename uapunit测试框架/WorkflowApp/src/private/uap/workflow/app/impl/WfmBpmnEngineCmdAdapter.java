//package uap.workflow.app.impl;
//
//import nc.bs.framework.common.NCLocator;
//import nc.uap.lfw.core.LfwRuntimeEnvironment;
//import nc.uap.lfw.core.log.LfwLogger;
//import nc.uap.wfm.constant.WfmConstants;
//import nc.uap.wfm.exe.WfmCmd;
//import nc.uap.wfm.utils.AppUtil;
//import nc.vo.pub.BusinessException;
//import nc.vo.pub.lang.UFDateTime;
//import uap.workflow.admin.IWFEngineService;
//import uap.workflow.app.core.IBusinessKey;
//import uap.workflow.app.vo.IPfRetCheckInfo;
//import uap.workflow.engine.vos.TaskInstanceVO;
//import uap.workflow.vo.WFAppParameter;
//import uap.workflow.vo.WorkflownoteVO;
//
//public class WfmBpmnEngineCmdAdapter implements IWfmEngineCmdAdapter {
//	/**
//	 * 发起任务
//	 */
//	public void commitExe(Object formInfoCtx, Object flowInfoCtx, String prodefPk) {
//		WFAppParameter wFAppParameter=new WFAppParameter();
//		wFAppParameter.setProcessDefPK(prodefPk);
//		wFAppParameter.setOperator(LfwRuntimeEnvironment.getLfwSessionBean().getPk_user());
//		wFAppParameter.setGroupPK(LfwRuntimeEnvironment.getLfwSessionBean().getPk_unit());
//		wFAppParameter.setBillMaker(LfwRuntimeEnvironment.getLfwSessionBean().getPk_user());
//		IBusinessKey bizKey2=(IBusinessKey)formInfoCtx;
//		wFAppParameter.setBusinessObject(bizKey2);
//		try {
//			WorkflownoteVO workflownoteVO = NCLocator.getInstance().lookup(IWFEngineService.class).getWorkflowItemsOnStart(wFAppParameter,IPfRetCheckInfo.NOSTATE);
//			if(workflownoteVO != null && workflownoteVO.getTaskInstanceVO()!=null)
//			{
//				wFAppParameter.setWorkFlow(workflownoteVO);
//				NCLocator.getInstance().lookup(IWFEngineService.class).startWorkflow(wFAppParameter,null);
//			}
//			else
//			{
//				
//			}
//		} catch (BusinessException e) {
//			LfwLogger.error(e.getMessage(), e);
//		}
//	}
//	/**
//	 * 提交是否指派
//	 */
//	public boolean commitIsAssign(Object formInfoCtx, String prodefPk) {
//		WFAppParameter wFAppParameter=new WFAppParameter();
//		wFAppParameter.setProcessDefPK(prodefPk);
//		IBusinessKey businessObject=(IBusinessKey)formInfoCtx;
//		wFAppParameter.setBusinessObject(businessObject);
//		WorkflownoteVO workflownoteVO=null;
//		try {
//			workflownoteVO= NCLocator.getInstance().lookup(IWFEngineService.class).getWorkflowItemsOnStart(wFAppParameter,IPfRetCheckInfo.NOSTATE);
//		} catch (BusinessException e) {
//			e.printStackTrace();
//		} 
//		if(workflownoteVO!=null)
//			return workflownoteVO.isAssign();
//		else
//			return false;
//	}
//	/**
//	 * 构构建启动流程时的流程上下文
//	 * @param flowTypePk
//	 * @param taskPk
//	 * @param prodefPk
//	 * @return
//	 */
//	public Object CommitBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		WFAppParameter wFAppParameter=new WFAppParameter();
//		wFAppParameter.setProcessDefPK(prodefPk);
//		WorkflownoteVO workflownoteVO=new WorkflownoteVO();
//		TaskInstanceVO taskInfo=(TaskInstanceVO) WfmEngineAdapter.getInstance().getTask(taskPk);
//		workflownoteVO.setTaskInstanceVO(taskInfo);
//		wFAppParameter.setWorkFlow(workflownoteVO);
//		return wFAppParameter;
//	}
//
//	public Object NextBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public Object NextBuilderWfmFormInfoCtx() {
//		return null;
//	}
//
//	public boolean nextIsAssign(Object formInfoCtx, String taskPk) {
//		return false;
//	}
//
//	public void bachNextExecute(String[] taskPks, String opnion) {
//	}
//
//	public Object transBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	@Override
//	public Object commitBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object nextBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Object nextBuilderWfmFormInfoCtx() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	public Object stopBuilderWfmFlowInfoCtx(String taskPk) {
//		return null;
//	}
//
//	public Object rectractBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public Object recallBeakBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public Object rejectBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public boolean rejectIsAssign(String taskPk) {
//		return false;
//	}
//
//	public Object interimBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public Object deliverBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public void commitExecute(Object formInfoCtx, String flowTypePk, String prodefPk) {
//	}
//
//	public void batchNextInfoExecute(String[] taskPks, String opnion) {
//	}
//
//	public Object batchNextInfoDoBatchTask() {
//		return null;
//	}
//
//	public Object affteAddSignBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk,
//			String prodefPk) {
//		return null;
//	}
//
//	public Object addSignBuilderWfmFlowInfoCtx(String flowTypePk, String taskPk, String prodefPk) {
//		return null;
//	}
//
//	public void defaultBuilderWfmFormInfoCtx(WfmCmd command, Object formInfoCtx) {
//		formInfoCtx =AppUtil.getCntAppCtx().getAppAttribute(WfmConstants.WfmAppAttr_FormInFoCtx);
//		IBusinessKey business=(IBusinessKey)formInfoCtx;
//		
//		
//		//单据PK
//		business.setBillId("1001AA100033000DFYUD");
//		//生成单据号
//		business.setBillNo(new UFDateTime().toLocalString());
//		//单据类型
//		business.setBillType(command.flowTypePk);
//		//保存？
//		command.formInfoCtx = formInfoCtx;
//	}
//
//	public void defaultBuilderWfmFlowInfoCtx(WfmCmd command, Object flowInfoCtx) {
//	}
//
//	public void defaultExe(Object formInfoCtx, Object flowInfoCtx) {
//	}
//
//}
