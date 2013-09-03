package uap.workflow.pub.app.mobile;

/**
 * 数字签名服务接口
 * 
 * @author leijun 2006-3-9
 */
public interface IMobileSignature {
	/**
	 * 验证签名
	 * @param toSign 待签名原文
	 * @param signedData 签名后结果
	 * @throws Exception 出现任何异常都表示验证失败
	 */
	public void verifySignature(String toSign, String signedData) throws Exception;
}
