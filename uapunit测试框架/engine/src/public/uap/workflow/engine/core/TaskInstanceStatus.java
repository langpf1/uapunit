package uap.workflow.engine.core;
/**
 * 
 * @author tianchw
 * 
 */
public enum TaskInstanceStatus {
	/**全部的状态：即是没有指定的状态，包含所有的状态*/
	All(-1),
	/** 启动（运行中） */
	Started(0),
	/** 完成 */
	Finished(1),
	/** 挂起（暂停） */
	Suspended(2),
	/** 终止 */
	Terminated(3),
	/** 无效（作废） */
	Inefficient(4),
	
	/** 待办*/
	Wait(5),
	/** 在办*/
	Run(6),
	/** 已办*/
	End(1),
	
	/** 待阅*/
	UnRead(8),
	/** 已阅*/
	Readed(9),
	/** 阅毕*/
	ReadEnd(10),
	
	/** 前加签发送*/
	BeforeAddSignSend(11),
	/** 前加签中 */
	BeforeAddSignUnderway(12),
	/** 前加签完成*/
	BeforeAddSignComplete(13),
	/** 前加签停止*/
	BeforeAddSignStop(14),
	
	/** 未看件*/
	HandlerPiece_UnRead(22),
	/** 看过件*/
	HandlerPiece_Readed(23),
	/** 退回件 */
	HandlerPiece_Rejected(24),
	
	/** 正常*/
	ActionType_Normal(25),
	/** 传阅*/
	ActionType_Deliver(26),
	/** 承办*/
	ActionType_UnderTake(27),
	/** 督办*/
	ActionType_Inspector(28);

	// 枚举的整型值
	private int intValue;
	/**
	 * 枚举的构造方法
	 * 
	 * @param intValue
	 */
	private TaskInstanceStatus(int intValue) {
		this.intValue = intValue;
	}
	public int getIntValue() {
		return this.intValue;
	}
	/**
	 * 得到未完成任务的状态集
	 */
	public static String getUnfinishedStatusSet() {
		return "" + Suspended.getIntValue();
	}
	public static TaskInstanceStatus fromIntValue(int intValue) {
		TaskInstanceStatus[] array = TaskInstanceStatus.class.getEnumConstants();
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
