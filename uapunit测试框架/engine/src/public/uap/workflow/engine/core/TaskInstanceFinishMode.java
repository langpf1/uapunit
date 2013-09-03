package uap.workflow.engine.core;

/**
 * 任务完成方式
 */
public enum TaskInstanceFinishMode {
	/**
	 * 未完成
	 */
	Unfinished(-1), //"FinishType_Unfinished";
	/**
	 * 正常完成
	 */
	Normal(0), //"FinishType_Normal";
	/**
	 * 退回完成
	 */
	Reject(1), //"FinishType_Reject";
	/**
	 * 传阅完成
	 */
	Deliver(2), //"FinishType_Deliver";
	/**
	 * 转发完成
	 */
	Tramsmit(3); //"FinishType_Tramsmit";
	
	//枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
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
