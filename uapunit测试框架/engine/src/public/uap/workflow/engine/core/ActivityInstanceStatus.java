package uap.workflow.engine.core;
import java.io.Serializable;
/**
 * 流程引擎使用的流程实例状态枚举
 */
public enum ActivityInstanceStatus implements Serializable {
	/** 代办 */
	Wait(0),
	/** 启动/运行中 */
	Started(1),
	/** 完成/ 结束 */
	Finished(2),
	/** 挂起/暂停 */
	Suspended(3),
	/** 终止 */
	Terminated(4),
	/** 无效/作废 */
	Inefficient(5);
	
	private int intValue;
	
	private ActivityInstanceStatus(int intValue) {
		this.intValue = intValue;
	}
	public int getIntValue() {
		return this.intValue;
	}
	public static ActivityInstanceStatus fromIntValue(int intValue) {
		ActivityInstanceStatus[] array = ActivityInstanceStatus.class.getEnumConstants();
		for (int i = 0; i < array.length; i++) {
			if (array[i].intValue == intValue) {
				return array[i];
			}
		}
		return null;
	}
}
