package uap.workflow.engine.core;


/**
 * ���񴴽�����
 */
public enum TaskInstanceCreateType {
	/** �Ƶ����޸�����*/
	Makebill(1),
	/** ������ת*/
	Normal(2), //"CreateType_Normal";��������
	/** ���ǩ����*/
	AfterAddSign(3), //"CreateType_AfterAddSign";���ǩ����
	/** ǰ��ǩ����*/
	BeforeAddSign(4), //"CreateType_BeforeAddSign";ǰ��ǩ����
	/** �м�ǩ����*/
	MiddleAddSign(5), //"CreateType_MiddleAddSign";�м�ǩ����
	/** ָ�ɲ���*/
	Assign(6), 
	/** ���ɲ���*/
	AlterAssign(7),
	/** ���Ĳ���*/
	Deliver(8), //"CreateType_Deliver";���Ĳ���
	/** ���� Backward(4) */
	Reject(9), //"CreateType_Reject";�˻ز���
	/** ��������*/
	Withdraw(10),
	/** �а�ʱ���������*/
	Sponsor(11), 
	/** �а�ʱ��Э�����*/
	Co_Sponsor(12);
	
	//ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
	 * @param intValue
	 */
	private TaskInstanceCreateType(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}
	
	/**
	 * �Ƿ�ָ��
	 * @param intValue
	 */
	public static boolean isAssign(int intValue) {
		if(intValue == Assign.intValue)
			return true;
		return false;
	}

	/**
	 * �Ƿ��ǩ
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
