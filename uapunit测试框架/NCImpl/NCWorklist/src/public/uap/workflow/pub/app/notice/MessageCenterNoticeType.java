package uap.workflow.pub.app.notice;

import uap.workflow.app.notice.INoticeType;

/** 
   ��Ϣ����֪ͨ����
 * @author 
 */

public class MessageCenterNoticeType implements INoticeType{

	String code="MessageCenter";
	String name="��Ϣ����";
	
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