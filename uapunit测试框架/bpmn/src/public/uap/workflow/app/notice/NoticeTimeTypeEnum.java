package uap.workflow.app.notice;
/**
 * ֪ͨ��ʱ������ö��
 * @author
 */

public enum NoticeTimeTypeEnum {
	TaskInstanceCreate(1),      //���񴴽�ʱ
	TaskInstanceComplete(2),    //���������ʱ
	TaskInstanceOverTime(3),    //����ʱ���Զ��߰죩
	ProcessInstanceOverTime(4); //���̳�ʱ���Զ��߰죩
	//Reminder(5);              //�߰죨�����߰죩

	//ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
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