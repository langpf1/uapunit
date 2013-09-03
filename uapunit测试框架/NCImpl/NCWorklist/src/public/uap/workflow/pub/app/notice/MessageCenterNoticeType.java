package uap.workflow.pub.app.notice;

import uap.workflow.app.notice.INoticeType;

/** 
   消息中心通知类型
 * @author 
 */

public class MessageCenterNoticeType implements INoticeType{

	String code="MessageCenter";
	String name="消息中心";
	
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