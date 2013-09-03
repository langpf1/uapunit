package uap.workflow.modeler.editors;

import uap.workflow.modeler.uecomponent.BpmnCellLib;
import uap.workflow.modeler.utils.BpmnTaskTypeEnum;

public class BpmnTaskTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] { BpmnTaskTypeEnum.UserTask, BpmnTaskTypeEnum.ScriptTask, BpmnTaskTypeEnum.ServiceTask, BpmnTaskTypeEnum.MailTask, BpmnTaskTypeEnum.ManualTask,
				BpmnTaskTypeEnum.ReceiveTask, BpmnTaskTypeEnum.BusinessRuleTask};
	}

	protected String[] IconURLS() {
		return new String[] { 
				BpmnCellLib.ICON_PALETTE_USERTASK, 
				BpmnCellLib.ICON_PALETTE_SCRIPTTASK, 
				BpmnCellLib.ICON_PALETTE_SERVICETASK, 
				BpmnCellLib.ICON_PALETTE_MAILTASK, 
				BpmnCellLib.ICON_PALETTE_MANUALTASK,
				BpmnCellLib.ICON_PALETTE_RECEIVETASK, 
				BpmnCellLib.ICON_PALETTE_BUSINESSRULETASK };
	};
}
