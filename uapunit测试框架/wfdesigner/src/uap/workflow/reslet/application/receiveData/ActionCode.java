package uap.workflow.reslet.application.receiveData;
/**
 * 
 * @author zhailzh
 * 
 */
public enum ActionCode {
	/** 批准 */
    APPROVE(0),
	/** 不批准 */
	UNAPPROVE(1),
	/** 驳回 */
	REJECT(2),
	/**加签 */
	ADDSIGN(3),
	/**改派 */
	REASSIGN(4),
	/**指派*/
	ASSIGN(5);
	
	// 枚举的整型值
	private int intValue;
	/**
	 * 枚举的构造方法
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