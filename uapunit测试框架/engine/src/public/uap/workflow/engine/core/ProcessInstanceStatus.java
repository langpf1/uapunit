package uap.workflow.engine.core;
/**
 * ��������ʹ�õ�����ʵ��״̬ö��
 */
public enum ProcessInstanceStatus {
	/**
	 * �����������У�
	 * State_Run = "State_Run";// ������
	 */
	Started(0),
	/**
	 * ���  
	 * State_End = "State_End";// ����
	 */
	Finished(1),
	/**
	 * ������ͣ��
	 * State_Suspended = "State_Suspended";
	 */
	Suspended(2),
	/** ��ֹ */
	Terminated(3),
	/**
	 * ��Ч�����ϣ�
	 * State_Cancel = "State_Cancel";// ����
	 */
	Inefficient(4),
	/**
	 * ����� 
	 * State_NoStart = "State_NoStart";// �����
	 */
	NoStart(5);

	private int intValue;
	/**
	 * ö�ٵĹ��췽��
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
