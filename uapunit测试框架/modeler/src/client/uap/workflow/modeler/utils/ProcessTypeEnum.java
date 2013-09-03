package uap.workflow.modeler.utils;

public enum ProcessTypeEnum {
	None(0), Private(1), Public(2);

	// ö�ٵ�����ֵ
	private int intValue;
	
	private ProcessTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static ProcessTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return None;
		case 1:
			return Private;
		case 2:
			return Public;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0:
			return "None";
		case 1:
			return "Private";
		case 2:
			return "Public";
		}
		return null;
	}

}
