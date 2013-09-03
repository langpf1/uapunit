package uap.workflow.app.participant;

import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

/** 
   参与者实现类
 * @author 
 */
public class BasicParticipant implements IParticipant{

	private String id;
	private String participantId;
	private String code;
	private String name;
	private IParticipantType participantType;
	private IParticipantFilterType participantFilterType;
	private Map<String,Object> properties = new HashMap<String, Object> ();

	public String getID() {
		return id;
	}
	
	public void setID(String id) {
		this.id = id;
	}

	public String getParticipantID() {
		return participantId;
	}

	public void setParticipantID(String participantId) {
		this.participantId = participantId;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IParticipantType getParticipantType() {
		return participantType;
	}

	public void setParticipantType(IParticipantType participantType) {
		this.participantType = participantType;
	}

	public IParticipantFilterType getParticipantFilterType() 
	{
		return this.participantFilterType;
	}

	public void setParticipantFilterType(IParticipantFilterType participantFilterType) 
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
		IParticipant newParticipant = new BasicParticipant();
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}