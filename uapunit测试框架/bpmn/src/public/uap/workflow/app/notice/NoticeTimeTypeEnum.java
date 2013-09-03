package uap.workflow.app.notice;
/**
 * 通知的时机类型枚举
 * @author
 */

public enum NoticeTimeTypeEnum {
	TaskInstanceCreate(1),      //任务创建时
	TaskInstanceComplete(2),    //任务处理完成时
	TaskInstanceOverTime(3),    //任务超时（自动催办）
	ProcessInstanceOverTime(4); //流程超时（自动催办）
	//Reminder(5);              //催办（主动催办）

	//枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
	 * 
	 * @param intValue
	 */
	private NoticeTimeTypeEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public static NoticeTimeTypeEnum fromIntValue(int intValue) {
		NoticeTimeTypeEnum[] array = NoticeTimeTypeEnum.class.getEnumConstants();
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