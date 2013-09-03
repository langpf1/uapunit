package uap.workflow.modeler.utils;

public enum ExecutionListenerTypeEnum {

	Start(0), Take(1), End(2);

	// ö�ٵ�����ֵ
	private int intValue;
	
	private ExecutionListenerTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	public String getStrValue() {
		return String.valueOf(this.intValue);
	}
	
	public static ExecutionListenerTypeEnum fromIntValue(int intValue) {
		switch (intValue) {
		// XXX:���뱣֤��ö��ֵһ��
		case 0:
			return Start;
		case 1:
			return Take;
		case 2:
			return End;
		default:
			break;
		}
		return null;
	}
	

	public String toString() {
		switch (getIntValue()) {
		case 0:
			return "Start";
		case 1:
			return "Take";
		case 2:
			return "End";
		}
		return null;
	}

}
