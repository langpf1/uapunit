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
		flowVariables.put("ʵ������", new FormulaItem("ʵ������","nrOfInstances","ʵ������"));
		flowVariables.put("�ʵ����", new FormulaItem("�ʵ����","nrOfActiveInstances","�ʵ����"));
		flowVariables.put("��ɵ�ʵ����", new FormulaItem("��ɵ�ʵ����","nrOfCompletedInstances","��ɵ�ʵ����"));
		flowVariables.put("�������", new FormulaItem("�������","generationBillResult","�������"));
		flowVariables.put("���������Ĳ���", new FormulaItem("PfParameterVO","PfParameterVO","PfParameterVO"));		
		flowVariables.put("��֧�����", new FormulaItem("��֧�����","nbrOfExecutionsJoined","��֧�����"));		
		flowVariables.put("��֧����", new FormulaItem("��֧����","nbrOfExecutionsToJoin","��֧����"));		
		flowVariables.put("���̳�ʼ����", new FormulaItem("���̳�ʼ����","initializeData","���̳�ʼ����"));
		return flowVariables;
	}
	
	public static HashMap<String, Object> getApproveResultVariables(){
		HashMap<String, Object> approveResultVariables = new HashMap<String, Object>();
		approveResultVariables.put("�������", new FormulaItem("�������","getApproveResult()","�������"));
		approveResultVariables.put("�������", new FormulaItem("�������","getApproveNote()","�������"));
		approveResultVariables.put("����", new FormulaItem("����","getAttachments()","����"));
		approveResultVariables.put("���͸�", new FormulaItem("���͸�","getCCTo()","���͸�"));
		approveResultVariables.put("ָ�ɸ�", new FormulaItem("ָ�ɸ�","getAssignedTo()","ָ�ɸ�"));
		return approveResultVariables;
	}
	
	public static HashMap<String, Object> getEvnVariables(){
		HashMap<String, Object> hsFormulaItems = new HashMap<String, Object>();
		String fName = null;
		fName = "�û�����";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getUserCode()", fName));
		fName = "�û�id";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getUserID()", fName));
		fName = "���ű���";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getGroupCode()", fName));
		fName = "����id";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getGroupID()", fName));
		fName = "��¼ҵ������";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getBizDate()", fName));
		fName = "��¼ҵ��ʱ��";
		hsFormulaItems.put(fName, new FormulaItem(fName, "getBizDateTime()", fName));
		fName = "��¼���Ա���";
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
		getCustomProperty(process.getCustomProperties(), contextVariables, "����", process.getName());
		String varName = null;
		for(FlowElement element : process.getFlowElements()){
			if (element instanceof Activity){
				getCustomProperty(((Activity)element).getCustomProperties(), 
						contextVariables, "����Ԫ��", ((Activity)element).getName());
				if (element instanceof ServiceTask){
					varName = ((ServiceTask)element).getResultVariableName();
					if (!StringUtil.isEmptyOrNull(varName)){
						addVariables(contextVariables, 
							"������"+((ServiceTask)element).getName()+"-"+varName, varName);
					}
				}else if (element instanceof CallActivity){
					List<DataAssociation> outVars = ((CallActivity)element).getOutParameters();
					for(DataAssociation var : outVars){
						varName = var.getTarget();
						if (StringUtil.isEmptyOrNull(varName)){
							addVariables(contextVariables, 
									"���û���"+((ServiceTask)element).getName()+"-"+varName, varName);
						}
					}
				}
			}
		}
		return contextVariables;
	}
}
