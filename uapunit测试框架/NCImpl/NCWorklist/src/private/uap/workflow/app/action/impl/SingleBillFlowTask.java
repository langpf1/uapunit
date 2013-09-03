package uap.workflow.app.action.impl;

import java.util.HashMap;
import java.util.concurrent.Callable;

import uap.workflow.admin.IWorkflowMachine;
import uap.workflow.vo.WorkflownoteVO;

import nc.bs.framework.common.NCLocator;
import nc.vo.pub.AggregatedValueObject;

public class SingleBillFlowTask implements Callable {

	String actionName;
	String billOrTranstype;
	AggregatedValueObject billvo;
	Object userObjAry;
	WorkflownoteVO worknoteVO;
	HashMap eParam;

	public SingleBillFlowTask(String actionName, String billOrTranstype, AggregatedValueObject billvos, Object userObjAry, WorkflownoteVO worknoteVO, HashMap eParam) {
		this.actionName = actionName;
		this.billOrTranstype = billOrTranstype;
		this.billvo = billvos;
		this.userObjAry = userObjAry;
		this.worknoteVO = worknoteVO;
		this.eParam = eParam;
	}

	@Override
	public Object call() throws Exception {
		// TODO Auto-generated method stub
		return NCLocator.getInstance().lookup(IWorkflowMachine.class).processSingleBillFlow_RequiresNew(actionName, billOrTranstype, worknoteVO, billvo, userObjAry, eParam);
	}

}
