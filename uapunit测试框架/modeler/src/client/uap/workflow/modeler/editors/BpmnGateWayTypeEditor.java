package uap.workflow.modeler.editors;

import uap.workflow.modeler.uecomponent.BpmnCellLib;
import uap.workflow.modeler.utils.BpmnGatewayTypeEnum;

public class BpmnGateWayTypeEditor extends AbstractBpmnComBoxEditor {

	@Override
	protected Object[] initTags() {
		return new Object[] {BpmnGatewayTypeEnum.ParallelGateWay,
				BpmnGatewayTypeEnum.ExclusiveGateWay,
				BpmnGatewayTypeEnum.InclusiveGateWay,
				BpmnGatewayTypeEnum.EventGateWay};
	}
	protected String[] IconURLS() {
		return new String[] { BpmnCellLib.ICON_PALETTE_PARALLELGATEWAY, 
				BpmnCellLib.ICON_PALETTE_EXCLUSIVEGATEWAY, 
				BpmnCellLib.ICON_PALETTE_INCLUSIVEGATEWAY, 
				BpmnCellLib.ICON_PALETTE_EVENTGATEWAY_EVENTBASED};
	};

}
