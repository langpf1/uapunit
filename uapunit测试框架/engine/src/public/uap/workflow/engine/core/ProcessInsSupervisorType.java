package uap.workflow.engine.core;

public enum ProcessInsSupervisorType {
	SUPERVISOR(0),
	TRACKER(1);
	
	private int intValue;
	
	/**
	 * ö�ٵĹ��췽��
	 * @param intValue
	 */
	private ProcessInsSupervisorType(int intValue) {
		this.intValue = intValue;
	}
	
	public int getIntValue() {
		return this.intValue;
	}
	
	public static ProcessInsSupervisorType fromIntValue(int intValue) {
		switch (intValue) {
			//XXX:���뱣֤��ö��ֵһ��
			case 0:
				return SUPERVISOR;
			case 1:
				return TRACKER;
			default:
				break;
		}
		return null;
	}
}
