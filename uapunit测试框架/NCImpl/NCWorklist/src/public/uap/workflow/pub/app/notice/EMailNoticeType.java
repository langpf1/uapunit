package uap.workflow.pub.app.notice;

import uap.workflow.app.notice.INoticeType;

/** 
   邮件通知类型
 * @author 
 */

public class EMailNoticeType implements INoticeType{
	String code="EMail";
	String name="邮件";
	
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