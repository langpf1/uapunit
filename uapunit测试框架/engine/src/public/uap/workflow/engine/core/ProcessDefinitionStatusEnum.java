package uap.workflow.engine.core;

/**
 * 流程定义的类型枚举
 */
public enum ProcessDefinitionStatusEnum {
	/** 草稿*/
	Draft(-1),	
	/** 未启用 */
	Invalid(0), 
	/** 启用 */
	Valid(1),
	/** 挂起  */	
	Suspend(2),
	/** 后加签产生的流程  */	
	AfterSign(3);

	// 枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
	 * @param intValue
	 */
	private ProcessDefinitionStatusEnum(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public static ProcessDefinitionStatusEnum fromIntValue(int intValue) {
		ProcessDefinitionStatusEnum[] array = ProcessDefinitionStatusEnum.class.getEnumConstants();
		for (int i = 0; i < array.length; i++) {
			if (array[i].intValue == intValue) {
				return array[i];
			}
		}
		return null;
	}
}
