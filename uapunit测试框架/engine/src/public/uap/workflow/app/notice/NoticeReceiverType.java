package uap.workflow.app.notice;
import java.util.HashMap;

/**
 * 通知接收者类型
 */
public enum NoticeReceiverType {
	STATION(0),  //岗位
	ROLE(1),     //组->角色
	USER(2),     //用户（操作员）
	SYSTEM(3),   //系统变量
	CUSTOM(4);   //自定义，由单据的插件实现

	//枚举的整型值
	private int intValue;

	/**
	 * 枚举的构造方法
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
