package uap.workflow.pub.app.mobile;

/**
 * ����ƽ̨֧�ֵĶ���ָ��
 * 
 * @author leijun 2006-3-9
 */
public interface IPFMobileCommand {
	/**
	 * ����
	 */
	String APPROVE = "SP";

	/**
	 * ����
	 */
	String UNAPPROVE = "QS";

	/**
	 * ��ѯδ��������
	 */
	String QUERY_WORKITEM_TODO = "CN";

	/**
	 * ��ѯ�Ѵ�������
	 */
	String QUERY_WORKITEM_DONE = "CD";
}
