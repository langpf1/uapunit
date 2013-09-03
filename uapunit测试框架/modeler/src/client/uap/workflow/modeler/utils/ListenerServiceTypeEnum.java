package uap.workflow.modeler.utils;

public enum ListenerServiceTypeEnum {
	Standard(0),
	Expression(1),
	DelegateExpression(2),
	CallMethod(3);
	//Compatible(4);

	// 枚举的整型值
	private int intValue;
	
	private ListenerServiceTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static ListenerServiceTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		case 0:
			return Standard;
		case 1:
			return Expression;
		case 2:
			return DelegateExpression;
		case 3:
			return CallMethod;
//			case 4:
//			return Compatible;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0:
			return "Standard";
		case 1:
			return "Expression";
		case 2:
			return "DelegateExpression";
		case 3:
			return "CallMethod";
//			case 3:
//			return "Compatible";
		}
		return null;
	}

}
