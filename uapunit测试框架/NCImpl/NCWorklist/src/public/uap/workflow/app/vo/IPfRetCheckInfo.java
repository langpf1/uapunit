package uap.workflow.app.vo;

/**
 * ���ݵ�����״̬ �б�
 * 
 * @author ���ھ� 2002-10-16
 * @modifier leijun 2008-12 ͳһ���ݵ�����״̬
 */
public interface IPfRetCheckInfo {
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
