package uap.workflow.pub.app.mobile.vo;


/**
 * 公共开发部短信服务配置VO
 * <li>与配置文件mobileplugin.xml对应
 * 
 * @author ewei 2007-9-26 
 */
public class PubDevSmsConfig {
	/**
	 * 公共开发部短信服务服务器地址
	 */
	private String url;
	/**
	 * 公共开发部短信服务连接超时时间(毫秒)
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
