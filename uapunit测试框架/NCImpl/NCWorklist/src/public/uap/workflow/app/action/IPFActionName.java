package uap.workflow.app.action;

/**
 * ������ƽ̨�н��е��õĵ��ݶ������� ���г�������
 * 
 */
public interface IPFActionName {
	/**
	 * ����������
	 */
	public static final String APPROVE = "APPROVE";

	/**
	 * ����������
	 */
	public static final String UNAPPROVE = "UNAPPROVE";

	/**
	 * ɾ��
	 */
	public static final String DEL_DELETE = "DELETE";

	/**
	 * ����
	 */
	public static final String DEL_DISCARD = "DISCARD";

	/**
	 * ʧЧ���Ƴ�
	 */
	public static final String DEL_SOBLANKOUT = "BLANKOUT";

	/**
	 * �ύ������
	 */
	public static final String SAVE = "SAVE";
	
	/**
	 * �ջ�������
	 */
	public static final String UNSAVE = "UNSAVE";
	
	/**
	 * ȡ���ύ������
	 */
	public static final String RECALL = "RECALL";

	/**
	 * �༭���޸ģ�Ҳ�ɴ�����������
	 */
	public static final String EDIT = "EDIT";

	/**
	 * ����
	 */
	public static final String WRITE = "WRITE";

	/**
	 * ����������
	 */
	public static final String START = "START";

	/**
	 * ִ�й�����
	 */
	public static final String SIGNAL = "SIGNAL";

	/**
	 * ���˹�����
	 */
	public static final String ROLLBACK = "ROLLBACK";
	
	/**
	 * ��ֹ������
	 */
	public static final String TERMINATE = "TERMINATE";
}
