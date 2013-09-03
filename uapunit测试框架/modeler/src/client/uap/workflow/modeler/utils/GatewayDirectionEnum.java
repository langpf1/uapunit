package uap.workflow.modeler.utils;

public enum GatewayDirectionEnum {
	Unspecified(0),
	Converging(1),
	Diverging(2),
	Mixed(3);

	// ö�ٵ�����ֵ
	private int intValue;
	
	private GatewayDirectionEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static GatewayDirectionEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return Unspecified;
		case 1:
			return Converging;
		case 2:
			return Diverging;
		case 3:
			return Mixed;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0:
			return "Unspecified";
		case 1:
			return "Converging";
		case 2:
			return "Diverging";
		case 3:
			return "Mixed";
		}
		return null;
	}


}
