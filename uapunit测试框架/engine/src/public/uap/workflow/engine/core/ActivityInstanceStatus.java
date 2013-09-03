package uap.workflow.engine.core;
import java.io.Serializable;
/**
 * ��������ʹ�õ�����ʵ��״̬ö��
 */
public enum ActivityInstanceStatus implements Serializable {
	/** ���� */
	Wait(0),
	/** ����/������ */
	Started(1),
	/** ���/ ���� */
	Finished(2),
	/** ����/��ͣ */
	Suspended(3),
	/** ��ֹ */
	Terminated(4),
	/** ��Ч/���� */
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
