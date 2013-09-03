package uap.workflow.modeler.uecomponent;

import java.awt.Container;
import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.ui.pub.formulaedit.FormulaEditorDialog;

public class BpmnConditionExpressDlg extends FormulaEditorDialog implements IAssemberPropertyData{

	private static final long serialVersionUID = 659500000707371131L;
	
	private HashMap<String, Object> contextVariables;
	private HashMap<String, Object> flowVariables;
	private HashMap<String, Object> approveResultVariables;
	private HashMap<String, Object> systemEnvVariables;
	private String mdClass;
	
	private PropertyEditorSupport propertyEditor;
	private Object OwnObject;
	private List<IPushOkandCancelListener> listeners  =new ArrayList<IPushOkandCancelListener>();
	//ϵͳ����ҳǩ
	private BpmnSystemEnvTabBuilder  sysEvnBuilder;
	//Ԫ����ҳǩ
	private BpmnMDTabBuilder metadataBuilder;
	//���ݺ���ҳǩ 
	private BpmnFunctionTabBuilder funcBuilder;

	//���̼�����
	private ExprFlowVariablesBuilder flowVariablesBuilder; 
	//���α���
	private ExprContextVariablesBuilder contextVariablesBuilder;
	//�����������
	private ExprApproveResultVariables approveVariablesBuilder;
	
	
	
	public BpmnConditionExpressDlg(Container parent,PropertyEditorSupport propertyEditor,Object OwnObject) {
		super(parent);
		this.OwnObject=OwnObject;
		this.propertyEditor=propertyEditor;
		initBPMNTabs();
	}

	public BpmnConditionExpressDlg(Container parent,PropertyEditorSupport propertyEditor,Object OwnObject,
			HashMap<String, Object> contextVariables, 
			HashMap<String, Object> flowVariables, 
			HashMap<String, Object> approveResultVariables, 
			HashMap<String, Object> systemEnvVariables, 
			String mdClass) {
		super(parent);
		this.OwnObject=OwnObject;
		this.propertyEditor=propertyEditor;
		this.contextVariables = contextVariables;
		this.flowVariables = flowVariables;
		this.approveResultVariables = approveResultVariables;
		this.systemEnvVariables = systemEnvVariables;
		this.mdClass = mdClass;
		initBPMNTabs();
	}

	private void initBPMNTabs(){
		metadataBuilder = new BpmnMDTabBuilder(mdClass);
		contextVariablesBuilder = new ExprContextVariablesBuilder(getFormulaWordSorter(), contextVariables);
		approveVariablesBuilder = new ExprApproveResultVariables(getFormulaWordSorter(),approveResultVariables);
		flowVariablesBuilder = new ExprFlowVariablesBuilder(getFormulaWordSorter(),flowVariables);
		sysEvnBuilder = new BpmnSystemEnvTabBuilder(getFormulaWordSorter(), systemEnvVariables);
		funcBuilder =new BpmnFunctionTabBuilder(getFormulaWordSorter());
		//����һ�����ݺ�����ҳǩ
		
		this.removeTabBuilder(0, 1);
		if (isBuilderExist(metadataBuilder.getTabName(), FormulaEditorDialog.FORMULA_VARIABLE)) {
			setCustomTabBuilder(0, metadataBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		} else {
			addCustomTabBuilder(0, metadataBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		}
		if (isBuilderExist(sysEvnBuilder.getTabName(), FormulaEditorDialog.FORMULA_VARIABLE)) {
			setCustomTabBuilder(1, sysEvnBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		} else {
			addCustomTabBuilder(1, sysEvnBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		}
		if (isBuilderExist(funcBuilder.getTabName(), FormulaEditorDialog.FORMULA_VARIABLE)) {
			setCustomTabBuilder(2, funcBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		} else {
			addCustomTabBuilder(2, funcBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		}
		if (isBuilderExist(approveVariablesBuilder.getTabName(), FormulaEditorDialog.FORMULA_VARIABLE)) {
			setCustomTabBuilder(3, approveVariablesBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		} else {
			addCustomTabBuilder(3, approveVariablesBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		}
		if (isBuilderExist(flowVariablesBuilder.getTabName(), FormulaEditorDialog.FORMULA_VARIABLE)) {
			setCustomTabBuilder(4, flowVariablesBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		} else {
			addCustomTabBuilder(4, flowVariablesBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		}
		if (isBuilderExist(contextVariablesBuilder.getTabName(), FormulaEditorDialog.FORMULA_VARIABLE)) {
			setCustomTabBuilder(5, contextVariablesBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		} else {
			addCustomTabBuilder(5, contextVariablesBuilder, FormulaEditorDialog.FORMULA_VARIABLE);
		}
		this.setSelectedTab("Metadata", 1);
	}
	
	public void setFormulaDesc(String formulaDesc) {
		super.setFormulaDesc(formulaDesc);
		doAfterOKButtonClicked();
	}
	
	
	protected void doAfterOKButtonClicked(){
		propertyEditor.setValue(getFormulaDesc());
		propertyEditor.firePropertyChange();
	}
  
	
	public void SetPropertys(Object obj){
		unassemberData(obj);
	}

	@Override
	public void unassemberData(Object intializeData) {
		setFormulaDesc(intializeData==null?"":intializeData.toString());
	}

	@Override
	public Object assemberData() {
		return null;
	}
}
