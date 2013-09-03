package uap.workflow.engine.el;

import uap.workflow.app.core.FlowInfoCtx;
import uap.workflow.engine.context.CommitProInsCtx;
import uap.workflow.engine.context.NextTaskInsCtx;
import uap.workflow.engine.entity.TaskEntity;
import uap.workflow.engine.session.WorkflowContext;
import nc.vo.bd.cust.pf.AggCustomerPfVO;
import nc.vo.pub.core.BizObject;
import nc.vo.pub.formulaset.IFormulaConstant;
import nc.vo.pub.formulaset.function.NcInnerFunction;

public class ApproveResultVariables extends NcInnerFunction {

	public ApproveResultVariables() {
		numberOfParameters = 0;
		functionType = IFormulaConstant.FUN_CUSTOM;
		functionDesc ="审批结果信息"; 
	}
	
    /**
     * 审批结果函数
     */
    public static boolean getApproveResult(){
    	FlowInfoCtx flowInfoCtx = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
        if (flowInfoCtx instanceof CommitProInsCtx ){
        	//flowInfoCtx 中间没有isPass这个量
        	return false;
        }
    	return ((NextTaskInsCtx)flowInfoCtx).isPass();
    }
    
    public static String getApproveNote(){
    	FlowInfoCtx flowInfo = WorkflowContext.getCurrentBpmnSession().getFlowInfoCtx();
    	return flowInfo.getComment();
    }
    
	public static String getAttachments(){
		return null;
	}
	public static String[] getCCTo(){
		return null;
	}
	public static String[] getAssignedTo(){
		return null;
	}

}
