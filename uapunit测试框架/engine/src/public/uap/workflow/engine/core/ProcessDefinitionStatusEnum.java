package uap.workflow.engine.core;

/**
 * ���̶��������ö��
 */
public enum ProcessDefinitionStatusEnum {
	/** �ݸ�*/
	Draft(-1),	
	/** δ���� */
	Invalid(0), 
	/** ���� */
	Valid(1),
	/** ����  */	
	Suspend(2),
	/** ���ǩ����������  */	
	AfterSign(3);

	// ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
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
