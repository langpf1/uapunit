package uap.workflow.modeler.utils;

public enum BpmnTaskTypeEnum {
	
	
	UserTask(0),
	ScriptTask(1),
	ServiceTask(2),
	MailTask(3),
	ManualTask(4),
	ReceiveTask(5),
	BusinessRuleTask(6);

	
	private int intValue;

	
	private BpmnTaskTypeEnum(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue(){
		return intValue;
	}
	
	
	public String toString(){
		switch (getIntValue()) {
		case 0:
			return "UserTask";
		case 1:
			return "ScriptTask";
		case 2:
			return "ServiceTask";
		case 3:
			return "MailTask";
		case 4:
			return "ManualTask";
		case 5:
			return "ReceiveTask";
		case 6:
			return "BusinessRuleTask";
		default:
			return null;
		}
	}
	
}
