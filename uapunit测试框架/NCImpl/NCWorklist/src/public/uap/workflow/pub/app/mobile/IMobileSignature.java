package uap.workflow.pub.app.mobile;

/**
 * ����ǩ������ӿ�
 * 
 * @author leijun 2006-3-9
 */
public interface IMobileSignature {
	/**
	 * ��֤ǩ��
	 * @param toSign ��ǩ��ԭ��
	 * @param signedData ǩ������
	 * @throws Exception �����κ��쳣����ʾ��֤ʧ��
	 */
	public void verifySignature(String toSign, String signedData) throws Exception;
}
