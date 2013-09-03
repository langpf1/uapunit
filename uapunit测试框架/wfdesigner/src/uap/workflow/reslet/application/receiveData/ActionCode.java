package uap.workflow.reslet.application.receiveData;
/**
 * 
 * @author zhailzh
 * 
 */
public enum ActionCode {
	/** ��׼ */
    APPROVE(0),
	/** ����׼ */
	UNAPPROVE(1),
	/** ���� */
	REJECT(2),
	/**��ǩ */
	ADDSIGN(3),
	/**���� */
	REASSIGN(4),
	/**ָ��*/
	ASSIGN(5);
	
	// ö�ٵ�����ֵ
	private int intValue;
	/**
	 * ö�ٵĹ��췽��
	 * 
	 * @param intValue
	 */
	private ActionCode(int intValue) {
		this.intValue = intValue;
	}
	public int getIntValue() {
		return this.intValue;
	}
	public String toString() {
		return String.valueOf(this.getIntValue());
	}
}