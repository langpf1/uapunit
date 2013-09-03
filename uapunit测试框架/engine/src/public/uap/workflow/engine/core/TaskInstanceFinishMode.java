package uap.workflow.engine.core;

/**
 * ������ɷ�ʽ
 */
public enum TaskInstanceFinishMode {
	/**
	 * δ���
	 */
	Unfinished(-1), //"FinishType_Unfinished";
	/**
	 * �������
	 */
	Normal(0), //"FinishType_Normal";
	/**
	 * �˻����
	 */
	Reject(1), //"FinishType_Reject";
	/**
	 * �������
	 */
	Deliver(2), //"FinishType_Deliver";
	/**
	 * ת�����
	 */
	Tramsmit(3); //"FinishType_Tramsmit";
	
	//ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
	 * 
	 * @param intValue
	 */
	private TaskInstanceFinishMode(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public static TaskInstanceFinishMode fromIntValue(int intValue) {
		TaskInstanceFinishMode[] array = TaskInstanceFinishMode.class.getEnumConstants();
		for (int i = 0; i < array.length; i++) {
			if (array[i].intValue == intValue) {
				return array[i];
			}
		}
		return null;
	}
	public String toString() {
		return String.valueOf(this.getIntValue());
	}
}
