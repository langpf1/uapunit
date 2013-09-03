package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.app.notice.INoticeDefinition;
import uap.workflow.app.notice.INoticeType;
import uap.workflow.app.notice.NoticeTimeTypeEnum;
import uap.workflow.app.notice.ReceiverVO;

/** 
   通知实现类
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultNoticeDefinition")
public class DefaultNoticeDefinition  implements Serializable,INoticeDefinition{
	private static final long serialVersionUID = 5677714133018846713L;
	@XmlElement(name="receiver",namespace=NameSpaceConst.BIZEX_URL)
	List<ReceiverVO> receivers = new ArrayList<ReceiverVO>();
	@XmlAttribute
	String contentTemplate;
	@XmlElement
	DefaultNoticeType noticeType;
	@XmlAttribute
	NoticeTimeTypeEnum noticeTime;
	@XmlAttribute
	String condition;
	@XmlAttribute
	boolean hasReceipt;
	@XmlTransient
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

	public DefaultNoticeType getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(DefaultNoticeType noticeType) {
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

	public void setNoticeType(INoticeType noticeType) {
		this.noticeType = (DefaultNoticeType)noticeType;
	}
}