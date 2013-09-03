package uap.workflow.modeler.utils;

public enum ServiceImplementTypeEnum {
	
	Standard(0),			//build in  
	WebService(1),
	Expression(2),
	DelegateExpression(3),
	CallMethod(4);//, 
//	GenerateBill(5);
	
	// ö�ٵ�����ֵ
	private int intValue;
	
	private ServiceImplementTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static ServiceImplementTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return Standard;
		case 1:
			return WebService;
		case 2:
			return Expression;
		case 3:
			return DelegateExpression;
		case 4:
			return CallMethod;
//		case 5:
//			return GenerateBill;
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
			return "WebService";
		case 2:
			return "Expression";
		case 3:
			return "DelegateExpression";
		case 4:
			return "CallMethod";
//		case 5:
//			return "GenerateBill";
		}
		return null;
	}

}
