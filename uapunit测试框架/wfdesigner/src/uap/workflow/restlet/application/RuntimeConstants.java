package uap.workflow.restlet.application;

public enum RuntimeConstants {
	
	/**对流程的操作编码*/
	//停止
	Terminate(0),
	//挂起
	Suspend(1),
	//激活
	Activate(2),
	//删除
    Delete(3),  

	/**审批历史用到的活动实例pk*/
	ActivityInstanceID("activityInsID"),
	
	/**分页查找的常量 */
	PageSize("pagenation[PageSize]"),
	PageNumber("pagenation[PageNumber]");
	
	// 枚举的整型值
	private int intValue;
	private String StringValue;
	/**
	 * 枚举的构造方法
	 * 
	 * @param intValue
	 */
	private RuntimeConstants (int intValue) {
		this.intValue = intValue;
	}
	private RuntimeConstants (String stringValue) {
		this.StringValue = stringValue;
	}
	public int getintValue() {
		return this.intValue;
	}
	public String getStringValue() {
		return this.StringValue;
	}
}
