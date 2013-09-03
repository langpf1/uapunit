package uap.workflow.restlet.application;

public enum RuntimeConstants {
	
	/**�����̵Ĳ�������*/
	//ֹͣ
	Terminate(0),
	//����
	Suspend(1),
	//����
	Activate(2),
	//ɾ��
    Delete(3),  

	/**������ʷ�õ��Ļʵ��pk*/
	ActivityInstanceID("activityInsID"),
	
	/**��ҳ���ҵĳ��� */
	PageSize("pagenation[PageSize]"),
	PageNumber("pagenation[PageNumber]");
	
	// ö�ٵ�����ֵ
	private int intValue;
	private String StringValue;
	/**
	 * ö�ٵĹ��췽��
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
