package uap.workflow.pub.app.mobile.vo;


/**
 * �������������ŷ�������VO
 * <li>�������ļ�mobileplugin.xml��Ӧ
 * 
 * @author ewei 2007-9-26 
 */
public class PubDevSmsConfig {
	/**
	 * �������������ŷ����������ַ
	 */
	private String url;
	/**
	 * �������������ŷ������ӳ�ʱʱ��(����)
	 */
	private int timeout;
	
	public String getURL(){
		return url;
	}
	
	public int getTimeout(){
		return timeout;
	}
	
	public void setURL(String url){
		this.url=url;
	}
	
	public void setTimeout(int timeout){
		this.timeout = timeout;
	}
}
