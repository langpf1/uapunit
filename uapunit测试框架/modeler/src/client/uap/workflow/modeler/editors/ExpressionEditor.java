package uap.workflow.modeler.editors;

import java.awt.Container;
import java.util.HashMap;

import uap.workflow.bpmn2.model.Bpmn2MemoryModel;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.modeler.bpmn.graph.BpmnGraphComponent;
import uap.workflow.modeler.uecomponent.BpmnConditionExpressDlg;
import nc.uap.ws.gen.util.StringUtil;
import nc.ui.ls.MessageBox;
import nc.ui.pub.beans.UIDialog;

public class ExpressionEditor extends AbstractDailogPropertyEditor{

	@Override
	protected UIDialog initializeDlg(Container parent) {
		String billType = "10GY";
		BpmnGraphComponent graphComponent = BpmnGraphComponent.getGraphComponentObject();
		Process process = graphComponent.getMainProcess();
		if (!StringUtil.isEmptyOrNull(process.getObjectType()))
				billType = process.getObjectType();
		

		Bpmn2MemoryModel model = Bpmn2MemoryModel.constructOutputModel(graphComponent);
		if(!valid(model.getMainProcess()))
			model=null;
		HashMap<String, Object> contextVariables = VariablesGather.getContextVariables(null, model.getMainProcess());
		HashMap<String, Object> flowVariables = VariablesGather.getFlowVariables();
		HashMap<String, Object> approveResultVariables = VariablesGather.getApproveResultVariables();
		HashMap<String, Object> systemEnvVariables = VariablesGather.getEvnVariables();

		return new BpmnConditionExpressDlg(parent,
				ExpressionEditor.this,getOwnerObject(),
				contextVariables,
				flowVariables,
				approveResultVariables,
				systemEnvVariables,
				billType);
	}
	private static boolean valid(Process process) 
	{
		boolean valid=true;
		if (StringUtil.isEmptyOrNull(process.getObjectType()))
		{
			MessageBox.showMessageDialog("提示", "对象类型不能为空。");
			valid=false;
		}
		return valid;
	}
	@Override
	protected void doButtonClick() {
		((BpmnConditionExpressDlg)dlg).SetPropertys(getValue());
		//dlg.setVisible(true);
		dlg.showModal();
	}
}
