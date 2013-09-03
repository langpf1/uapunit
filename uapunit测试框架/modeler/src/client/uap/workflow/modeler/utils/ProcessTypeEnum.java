package uap.workflow.modeler.utils;

public enum ProcessTypeEnum {
	None(0), Private(1), Public(2);

	// 枚举的整型值
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
		// XXX:必须保证与枚举值一致
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
