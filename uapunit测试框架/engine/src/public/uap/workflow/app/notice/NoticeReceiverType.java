package uap.workflow.app.notice;
import java.util.HashMap;

/**
 * ֪ͨ����������
 */
public enum NoticeReceiverType {
	STATION(0),  //��λ
	ROLE(1),     //��->��ɫ
	USER(2),     //�û�������Ա��
	SYSTEM(3),   //ϵͳ����
	CUSTOM(4);   //�Զ��壬�ɵ��ݵĲ��ʵ��

	//ö�ٵ�����ֵ
	private int intValue;

	/**
	 * ö�ٵĹ��췽��
	 * 
	 * @param intValue
	 */
	private NoticeReceiverType(int intValue) {
		this.intValue = intValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public static NoticeReceiverType fromIntValue(int intValue) {
		NoticeReceiverType[] array = NoticeReceiverType.class.getEnumConstants();
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
