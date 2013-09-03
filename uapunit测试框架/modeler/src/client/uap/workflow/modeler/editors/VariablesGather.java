package uap.workflow.modeler.editors;

import java.util.HashMap;
import java.util.List;

import nc.uap.ws.gen.util.StringUtil;
import nc.vo.pub.formulaedit.FormulaItem;
import uap.workflow.bpmn2.model.Activity;
import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.CallActivity;
import uap.workflow.bpmn2.model.CustomProperty;
import uap.workflow.bpmn2.model.DataAssociation;
import uap.workflow.bpmn2.model.FlowElement;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.bpmn2.model.ServiceTask;

public class VariablesGather {

	public static HashMap<String, Object> getFlowVariables(){
		HashMap<String, Object> flowVariables = new HashMap<String, Object>();
		flowVariables.put("实例总数", new FormulaItem("实例总数","nrOfInstances","实例总数"));
		flowVariables.put("活动实例数", new FormulaItem("活动实例数","nrOfActiveInstances","活动实例数"));
		flowVariables.put("完成的实例数", new FormulaItem("完成的实例数","nrOfCompletedInstances","完成的实例数"));
		flowVariables.put("生单结果", new FormulaItem("生单结果","generationBillResult","生单结果"));
		flowVariables.put("流程上下文参数", new FormulaItem("PfParameterVO","PfParameterVO","PfParameterVO"));		
		flowVariables.put("分支完成数", new FormulaItem("分支完成数","nbrOfExecutionsJoined","分支完成数"));		
		flowVariables.put("分支总数", new FormulaItem("分支总数","nbrOfExecutionsToJoin","分支总数"));		
		flowVariables.put("流程初始数据", new FormulaItem("流程初始数据","initializeData","流程初始数据"));
		return flowVariables;
	}
	
	public static HashMap<String, Object> getApproveResultVariables(){
		HashMap<String, Object> approveResultVariables = new HashMap<String, Object>();
		approveResultVariables.put("审批结果", new FormulaItem("审批结果","getApproveResult()","审批结果"));
		approveResultVariables.put("审批意见", new FormulaItem("审批意见","getApproveNote()","审批意见"));
		approveResultVariables.put("附件", new FormulaItem("附件","getAttachments()","附件"));
		approveResultVariables.put("抄送给", new FormulaItem("抄送给","getCCTo()","抄送给"));
		approveResultVariables.put("指派给", new FormulaItem("指派给","getAssignedTo()","指派给"));
		return approveResultVariables;
	}
	
	public static HashMap<String, Object> getEvnVariables(){
		HashMap<String, Object> hsFormulaItems = new HashMap<String, Object>();
		String fName = null;
		fName = "用户编码";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getUserCode()", fName));
		fName = "用户id";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getUserID()", fName));
		fName = "集团编码";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getGroupCode()", fName));
		fName = "集团id";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getGroupID()", fName));
		fName = "登录业务日期";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getBizDate()", fName));
		fName = "登录业务时间";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getBizDateTime()", fName));
		fName = "登录语言编码";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getLangCode()", fName));

		return hsFormulaItems;
	}
	
	private static void getCustomProperty(List<CustomProperty> properties , 
			HashMap<String, Object> contextVariables, String scope, String name){
		String varName = "";
		for(CustomProperty property : properties){
			varName = property.getName();
			addVariables(contextVariables, scope+name+"-"+varName, varName);		
		}
	}
	
	private static void addVariables(HashMap<String, Object> contextVariables, String displayName, String variable){
		contextVariables.put(displayName.toLowerCase(), 
				new FormulaItem(displayName,variable,displayName));		
	}
	
	public static HashMap<String, Object> getContextVariables(BaseElement currentElement, Process process){
		HashMap<String, Object> contextVariables = new HashMap<String, Object>();
		getCustomProperty(process.getCustomProperties(), contextVariables, "流程", process.getName());
		String varName = null;
		for(FlowElement element : process.getFlowElements()){
			if (element instanceof Activity){
				getCustomProperty(((Activity)element).getCustomProperties(), 
						contextVariables, "流程元素", ((Activity)element).getName());
				if (element instanceof ServiceTask){
					varName = ((ServiceTask)element).getResultVariableName();
					if (!StringUtil.isEmptyOrNull(varName)){
						addVariables(contextVariables, 
							"服务结果"+((ServiceTask)element).getName()+"-"+varName, varName);
					}
				}else if (element instanceof CallActivity){
					List<DataAssociation> outVars = ((CallActivity)element).getOutParameters();
					for(DataAssociation var : outVars){
						varName = var.getTarget();
						if (StringUtil.isEmptyOrNull(varName)){
							addVariables(contextVariables, 
									"调用活动输出"+((ServiceTask)element).getName()+"-"+varName, varName);
						}
					}
				}
			}
		}
		return contextVariables;
	}
}
