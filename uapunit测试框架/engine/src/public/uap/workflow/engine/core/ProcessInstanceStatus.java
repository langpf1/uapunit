package uap.workflow.engine.core;
/**
 * 流程引擎使用的流程实例状态枚举
 */
public enum ProcessInstanceStatus {
	/**
	 * 启动（运行中）
	 * State_Run = "State_Run";// 运行中
	 */
	Started(0),
	/**
	 * 完成  
	 * State_End = "State_End";// 结束
	 */
	Finished(1),
	/**
	 * 挂起（暂停）
	 * State_Suspended = "State_Suspended";
	 */
	Suspended(2),
	/** 终止 */
	Terminated(3),
	/**
	 * 无效（作废）
	 * State_Cancel = "State_Cancel";// 作废
	 */
	Inefficient(4),
	/**
	 * 拟稿中 
	 * State_NoStart = "State_NoStart";// 拟稿中
	 */
	NoStart(5);

	private int intValue;
	/**
	 * 枚举的构造方法
	 * 
	 * @param intValue
	 */
	private ProcessInstanceStatus(int intValue) {
		this.intValue = intValue;
	}
	public int getIntValue() {
		return this.intValue;
	}
	public static ProcessInstanceStatus fromIntValue(int intValue) {
		switch (intValue) {
		case 0:
			return Started;
		case 1:
			return Finished;
		case 2:
			return Suspended;
		case 3:
			return Terminated;
		case 4:
			return Inefficient;
		case 5:
			return NoStart;
		default:
			break;
		}
		return null;
	}
}
