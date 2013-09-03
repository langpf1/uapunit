package uap.workflow.pub.app.notice;

import uap.workflow.app.notice.INoticeType;

/** 
   短消息通知类型
 * @author 
 */

public class SMSNoticeType implements INoticeType{
	String code="SMS";
	String name="短消息";
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}