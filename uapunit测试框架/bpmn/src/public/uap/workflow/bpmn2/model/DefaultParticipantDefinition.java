package uap.workflow.bpmn2.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.app.participant.IParticipant;
import uap.workflow.app.participant.IParticipantFilterType;
import uap.workflow.app.participant.IParticipantType;

/** 
   参与者实现类
 * @author 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DefaultParticipantDefinition")
public class DefaultParticipantDefinition  implements Serializable,IParticipant{

	private static final long serialVersionUID = 5869293895276754558L;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String participantID;
	@XmlAttribute
	private String code;
	@XmlAttribute
	private String name;
	
	private DefaultParticipantType participantType;
	private DefaultParticipantFilterType participantFilterType;
	
	@XmlTransient
	private Map<String,Object> properties = new HashMap<String, Object> ();

	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}

	public String getParticipantID() {
		return participantID;
	}

	public void setParticipantID(String participantId) {
		this.participantID = participantId;
	}	

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

	public DefaultParticipantType getParticipantType() {
		return participantType;
	}

	public void setParticipantType(DefaultParticipantType participantType) {
		this.participantType = participantType;
	}

	public DefaultParticipantFilterType getParticipantFilterType() 
	{
		return this.participantFilterType;
	}

	public void setParticipantFilterType(DefaultParticipantFilterType participantFilterType) 
	{
		this.participantFilterType = participantFilterType;
	}

	public Map<String, Object> getProperties() 
	{
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
		this.properties.put(key, value);
	}
	
	public IParticipant clone() {
		IParticipant newParticipant = new DefaultParticipantDefinition();
		newParticipant.setID(this.getID());
		newParticipant.setName(this.getName());
		newParticipant.setParticipantFilterType(this.getParticipantFilterType());
		newParticipant.setParticipantID(this.getParticipantID());
		newParticipant.setParticipantType(this.getParticipantType());
		
		Iterator iterator = this.properties.entrySet().iterator();
		while(iterator.hasNext())
		{
			Map.Entry<String, Object> entry = (Map.Entry<String, Object>)iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			newParticipant.setProperty(key, value);
		}
		return newParticipant;
	}

	public void setParticipantType(IParticipantType participantType) {
		this.participantType = (DefaultParticipantType)participantType;
	}

	public void setParticipantFilterType(IParticipantFilterType participantFilterType) {
		this.participantFilterType = (DefaultParticipantFilterType)participantFilterType;
	}
}