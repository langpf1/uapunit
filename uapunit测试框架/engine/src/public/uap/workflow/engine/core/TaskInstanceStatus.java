package uap.workflow.engine.core;
/**
 * 
 * @author tianchw
 * 
 */
public enum TaskInstanceStatus {
	/**ȫ����״̬������û��ָ����״̬���������е�״̬*/
	All(-1),
	/** �����������У� */
	Started(0),
	/** ��� */
	Finished(1),
	/** ������ͣ�� */
	Suspended(2),
	/** ��ֹ */
	Terminated(3),
	/** ��Ч�����ϣ� */
	Inefficient(4),
	
	/** ����*/
	Wait(5),
	/** �ڰ�*/
	Run(6),
	/** �Ѱ�*/
	End(1),
	
	/** ����*/
	UnRead(8),
	/** ����*/
	Readed(9),
	/** �ı�*/
	ReadEnd(10),
	
	/** ǰ��ǩ����*/
	BeforeAddSignSend(11),
	/** ǰ��ǩ�� */
	BeforeAddSignUnderway(12),
	/** ǰ��ǩ���*/
	BeforeAddSignComplete(13),
	/** ǰ��ǩֹͣ*/
	BeforeAddSignStop(14),
	
	/** δ����*/
	HandlerPiece_UnRead(22),
	/** ������*/
	HandlerPiece_Readed(23),
	/** �˻ؼ� */
	HandlerPiece_Rejected(24),
	
	/** ����*/
	ActionType_Normal(25),
	/** ����*/
	ActionType_Deliver(26),
	/** �а�*/
	ActionType_UnderTake(27),
	/** ����*/
	ActionType_Inspector(28);

	// ö�ٵ�����ֵ
	private int intValue;
	/**
	 * ö�ٵĹ��췽��
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
	 * �õ�δ��������״̬��
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
