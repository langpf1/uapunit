package uap.workflow.engine.core;


/**
 * 任务创建类型
 */
public enum TaskInstanceCreateType {
	/** 制单或修改任务*/
	Makebill(1),
	/** 正常流转*/
	Normal(2), //"CreateType_Normal";正常产生
	/** 后加签产生*/
	AfterAddSign(3), //"CreateType_AfterAddSign";后加签产生
	/** 前加签产生*/
	BeforeAddSign(4), //"CreateType_BeforeAddSign";前加签产生
	/** 中加签产生*/
	MiddleAddSign(5), //"CreateType_MiddleAddSign";中加签产生
	/** 指派产生*/
	Assign(6), 
	/** 改派产生*/
	AlterAssign(7),
	/** 传阅产生*/
	Deliver(8), //"CreateType_Deliver";传阅产生
	/** 驳回 Backward(4) */
	Reject(9), //"CreateType_Reject";退回产生
	/** 弃审或回退*/
	Withdraw(10),
	/** 承办时，主办产生*/
	Sponsor(11), 
	/** 承办时，协办产生*/
	Co_Sponsor(12);
	
	//枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
	 * @param intValue
	 */
	private TaskInstanceCreateType(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	/**
	 * 是否指派
	 * @param intValue
	 */
	public static boolean isAssign(int intValue) {
		if(intValue == Assign.intValue)
			return true;
		return false;
	}

	/**
	 * 是否加签
	 * @param intValue
	 */
	public static boolean isAddApprover(int intValue) {
		if(intValue == AfterAddSign.intValue || intValue == BeforeAddSign.intValue || intValue == MiddleAddSign.intValue)
			return true;
		return false;
	}

	public static TaskInstanceCreateType fromIntValue(int intValue) {
		TaskInstanceCreateType[] array = TaskInstanceCreateType.class.getEnumConstants();
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
