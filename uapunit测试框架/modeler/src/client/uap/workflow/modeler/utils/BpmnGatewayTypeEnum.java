package uap.workflow.modeler.utils;

public enum BpmnGatewayTypeEnum {
	
	
	ParallelGateWay(0),
	ExclusiveGateWay(1),
	InclusiveGateWay(2),
	EventGateWay(3);

	
	private int intValue;

	
	private BpmnGatewayTypeEnum(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue(){
		return intValue;
	}
	
	
	public String toString(){
		switch (getIntValue()) {
		case 0:
			return "ParallelGateWay";
		case 1:
			return "ExclusiveGateWay";
		case 2:
			return "InclusiveGateWay";
		case 3:
			return "EventGateWay";
		default:
			return null;
		}
	}
}
