package uap.workflow.modeler.utils;

public enum EventGatewayTypeEnum {
	Exclusive(0), Parallel(1);

	// ö�ٵ�����ֵ
	private int intValue;
	
	private EventGatewayTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static EventGatewayTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return Exclusive;
		case 1:
			return Parallel;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0:
			return "Exclusive";
		case 1:
			return "Parallel";
		}
		return null;
	}

}
