package uap.workflow.vo;

/**
 * ���ݵ�����״̬ �б�
 * ͳһ���ݵ�����״̬
 * 
 * ��IPfRetCheckInfoһ�������workflowapp����NC����ϵ����IReturnCheckInfo
 */
public interface IReturnCheckInfo {
	/*  �������ڲ�ʹ�õĵ���״̬  */
	/**
	 * ����̬
	 */
	public static final int NOSTATE = -1;

	/**
	 * δͨ�� ̬
	 */
	public static final int NOPASS = 0;

	/**
	 * ͨ�� ̬
	 */
	public static final int PASSING = 1;

	/**
	 * ������ ̬
	 */
	public static final int GOINGON = 2;

	/**
	 * �ύ ̬
	 */
	public static final int COMMIT = 3;

}
