package uap.workflow.ui.client;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.ui.pub.beans.UIDialog;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.itf.IProcessDefinitionQry;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.vos.ProcessDefinitionVO;
import uap.workflow.engine.vos.TaskInstanceVO;
import uap.workflow.itf.IWorkflowServiceUtil;
import uap.workflow.pub.util.PfUtilBaseTools;
import uap.workflow.ui.workitem.ApproveWorkitemAcceptDlg;
import uap.workflow.vo.WorkflownoteVO;


public class WorkflowClientUtil {

	private static String BEFORE_START = "BefroeSubmit";
	private static String AFTER_START = "AfterSubmit";
	private static String BEFORE_FORWARD = "BefroeForward";
	private static String AFTER_FORWARD = "AfterForward";
	private static String BEFORE_REJECT = "BefroeReject";
	private static String AFTER_REJECT = "AfterReject";
	private static String BEFORE_BACKWARD = "BefroeBackward";
	private static String AFTER_BACKWARD = "AfterBackward";
	private static List<WorkflowClientListener> listeners = new ArrayList<WorkflowClientListener>();
	
	private static List<WorkflowClientListener> getListeners(){
		return listeners;
	}
	
	public static void addListener(WorkflowClientListener listener){
		listeners.add(listener);
	}
	
	private static boolean fireEvent(WorkflowClientEvent event){
		boolean isContinue = true;
		Object resultNotify = null;
		for(WorkflowClientListener listener : getListeners()){
			if(BEFORE_START.equals(event.getEventType())){
				resultNotify = listener.beforeSubmit(event);
			}else if(AFTER_START.equals(event.getEventType())){
				resultNotify = listener.beforeSignal(event);
			}else if(BEFORE_FORWARD.equals(event.getEventType())){
				resultNotify = listener.afterSignal(event);
			}else if(AFTER_FORWARD.equals(event.getEventType())){
				resultNotify = listener.beforeReject(event);
			}else if(BEFORE_REJECT.equals(event.getEventType())){
				resultNotify = listener.afterReject(event);
			}else if(AFTER_REJECT.equals(event.getEventType())){
				resultNotify = listener.afterReject(event);
			}else if(BEFORE_BACKWARD.equals(event.getEventType())){
				resultNotify = listener.afterReject(event);
			}else if(AFTER_BACKWARD.equals(event.getEventType())){
				resultNotify = listener.afterReject(event);
			}
			if (resultNotify instanceof Boolean){
				isContinue = (Boolean)resultNotify;
			}
			if (!isContinue)
				break;
		}
		return isContinue;
	}
	
	private static String hasApproveflowDef(AggregatedValueObject billVo)throws BusinessException {

		String bizObject = billVo.getParentVO().getClass().getName();
		IProcessDefinitionQry proDefQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IProcessDefinitionQry.class);
		ProcessDefinitionVO[] proDefVOs = proDefQry.getProcessDefVOAccordingBiz(InvocationInfoProxy.getInstance().getGroupId(), bizObject);
		if (proDefVOs == null || proDefVOs.length == 0) {
			return null;
		}else
			return proDefVOs[0].getPk_prodef();
	}

	public static Object start(Container parent, AggregatedValueObject billvo, Map<String, Object> customData) 
		throws BusinessException {
		Object object = null;
			
		String procDefID = hasApproveflowDef(billvo); 
		WorkflowClientEvent event = new WorkflowClientEvent( BEFORE_START);
		event.setEventArg("procDefID", procDefID);
		if (!fireEvent(event))
			return null;
		if (procDefID != null){
			
			IWorkflowServiceUtil serviceUtil = NCLocator.getInstance().lookup(IWorkflowServiceUtil.class);
			serviceUtil.start(billvo, customData);
			
		}else{	//直接完成
			throw new BusinessException("没能找到流程定义！");
		}
		
		if (!fireEvent(new WorkflowClientEvent(AFTER_START)))
			return null;
		return object;
		
	}
	
	private static WorkflownoteVO checkWorkitemWhenSignal(Container parent,AggregatedValueObject billVo) throws BusinessException {
		IWorkflowServiceUtil serviceUtil = NCLocator.getInstance().lookup(IWorkflowServiceUtil.class); 
		TaskInstanceVO[] taskVOs = serviceUtil.getToDoWorkitems(billVo.getParentVO().getPrimaryKey(), 
				InvocationInfoProxy.getInstance().getUserId());
		
		if (taskVOs == null || taskVOs.length < 1) {
			return null;
		}
		WorkflownoteVO workflownoteVO = new WorkflownoteVO();
		workflownoteVO.setTaskInstanceVO(taskVOs[0]);

		WorkflowClientEvent event = new WorkflowClientEvent(BEFORE_FORWARD);	//告知订阅者
		event.setEventArg("WorkflownoteVO", workflownoteVO);
		if (!fireEvent(event))
			return null;
		
		
		UIDialog dlg = null;
		dlg = new ApproveWorkitemAcceptDlg(parent, workflownoteVO, false);
		
		if (dlg.showModal() != UIDialog.ID_OK) { // 取消了
			workflownoteVO = null;
		}
		
		return workflownoteVO;
	}
	
	public static Object forward(Container parent, AggregatedValueObject billvo, Map<String, Object> customData) 
		throws BusinessException {
		Object object = null;
			
		WorkflownoteVO noteVO = checkWorkitemWhenSignal(parent, billvo);
		if (noteVO != null){
			IWorkflowServiceUtil serviceUtil = NCLocator.getInstance().lookup(IWorkflowServiceUtil.class);			
			return serviceUtil.forward(noteVO, billvo, customData);
		}
		if (!fireEvent(new WorkflowClientEvent(AFTER_FORWARD)))
			return null;		
		return object;		
	}
	
	public static Object reject(Container parent, AggregatedValueObject billvo, Map<String, Object> customData)
		throws BusinessException {
		Object object = null;
		if (!fireEvent(new WorkflowClientEvent(BEFORE_REJECT)))
			return null;
			
		

		
		if (!fireEvent(new WorkflowClientEvent(AFTER_REJECT)))
			return null;		
		return object;		
	}
	
	public static Object backword(Container parent, AggregatedValueObject billvo, Map<String, Object> customData)
		throws BusinessException {
		Object object = null;
		if (!fireEvent(new WorkflowClientEvent(BEFORE_BACKWARD)))
			return null;
		
		if (!fireEvent(new WorkflowClientEvent(AFTER_BACKWARD)))
			return null;		
		return object;		
	}
}
