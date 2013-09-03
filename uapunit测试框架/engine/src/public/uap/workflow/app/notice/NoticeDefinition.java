package uap.workflow.app.notice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/** 
   通知实现类
 * @author 
 */
public class NoticeDefinition implements INoticeDefinition{
	List<ReceiverVO> receivers = new ArrayList<ReceiverVO>();
	String contentTemplate;
	INoticeType noticeType;
	NoticeTimeTypeEnum noticeTime;
	String condition;
	boolean hasReceipt;
	private Map<String, Object> properties;
	
	public List<ReceiverVO> getReceivers() {
		return receivers;
	}

	public void setReceivers(List<ReceiverVO> receivers) {
		this.receivers = receivers;
	}

	public String getContentTemplate() {
		return contentTemplate;
	}

	public void setContentTemplate(String contentTemplate) {
		this.contentTemplate = contentTemplate;
	}

	public INoticeType getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(INoticeType noticeType) {
		this.noticeType = noticeType;
	}

	public NoticeTimeTypeEnum getNoticeTime() {
		return noticeTime;
	}

	public void setNoticeTime(NoticeTimeTypeEnum noticeTime) {
		this.noticeTime = noticeTime;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public boolean getHasReceipt() {
		return hasReceipt;
	}

	public void setHasReceipt(boolean hasReceipt) {
		this.hasReceipt = hasReceipt;
	}

	public Map<String, Object> getProperties() {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	public Object getProperty(String key) {
		if(this.properties != null && this.properties.containsKey(key))
		{
			return this.properties.get(key);
		}
		return null;
	}

	public void setProperty(String key, Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		this.properties.put(key, value);
	}
}