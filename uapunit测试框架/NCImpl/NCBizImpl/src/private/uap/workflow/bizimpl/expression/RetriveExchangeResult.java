package uap.workflow.bizimpl.expression;

import java.util.Map;

import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.formulaset.IFormulaConstant;
import nc.vo.pub.formulaset.function.NcInnerFunction;

public class RetriveExchangeResult extends NcInnerFunction {

	public RetriveExchangeResult() {
		numberOfParameters = 2;
		functionType = IFormulaConstant.FUN_CUSTOM;
		functionDesc ="获取生单结果，格式getExchangedResult('generatedBillResultVariable','objectType')"; 
	}
	
	public static AggregatedValueObject[] getExchangedResult(Map generatedBillResult, String objectType){
		//ProcessInstanceEntity procInstance = 
		//	(ProcessInstanceEntity) WorkflowContext.getCurrentBpmnSession().getResponse().getCurrentTask().getProcessInstance(); 
			//(ProcessInstanceEntity)WorkflowContext.getCurrentBpmnSession().getProcessInstance();
		
		//Map<String, Object> generatedBill = (Map<String, Object>)procInstance.getVariable(generatedBillResultVariable);
		
		if(generatedBillResult == null)
			return null;
		return (AggregatedValueObject[])generatedBillResult.get(objectType);
	}

}
