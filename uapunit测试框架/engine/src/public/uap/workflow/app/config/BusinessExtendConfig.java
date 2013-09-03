package uap.workflow.app.config;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/** 
   业务扩展配置项
 * @author 
 */
@XmlRootElement(name = "BusinessExtend")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessExtendConfig")
public class BusinessExtendConfig implements Serializable{
	private static final long serialVersionUID = -9045110756346954073L;
	@XmlElementWrapper(name = "ParticipantTypes")
	@XmlElement(name = "ParticipantType")
	public List<ParticipantTypeConfig> participantTypes = new ArrayList<ParticipantTypeConfig>();
	
	@XmlElementWrapper(name = "ParticipantFilterTypes")
	@XmlElement(name = "ParticipantFilterType")
	public List<ParticipantFilterTypeConfig> participantFilterTypes = new ArrayList<ParticipantFilterTypeConfig>();

	@XmlElementWrapper(name = "NoticeTypes")
	@XmlElement(name = "NoticeType")
	public List<NoticeTypeConfig> noticeTypes = new ArrayList<NoticeTypeConfig>();
	
	@XmlElementWrapper(name = "TaskHandlingTypes")
	@XmlElement(name = "TaskHandlingType")
	public List<TaskHandlingTypeConfig> taskHandlingTypes = new ArrayList<TaskHandlingTypeConfig>();

	public List<ParticipantTypeConfig> getParticipantTypes() {
		return participantTypes;
	}

	public void setParticipantTypes(List<ParticipantTypeConfig> participantTypes) {
		this.participantTypes = participantTypes;
	}

	public List<ParticipantFilterTypeConfig> getParticipantFilterTypes() {
		return participantFilterTypes;
	}

	public void setParticipantFilterTypes(List<ParticipantFilterTypeConfig> participantFilterTypes) {
		this.participantFilterTypes = participantFilterTypes;
	}

	public List<NoticeTypeConfig> getNoticeTypes() {
		return noticeTypes;
	}

	public void setNoticeTypes(List<NoticeTypeConfig> noticeTypes) {
		this.noticeTypes = noticeTypes;
	}

	public List<TaskHandlingTypeConfig> getTaskHandlingTypes() {
		return taskHandlingTypes;
	}

	public void setTaskHandlingTypes(List<TaskHandlingTypeConfig> taskHandlingTypes) {
		this.taskHandlingTypes = taskHandlingTypes;
	}
}