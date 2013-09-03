package uap.workflow.bpmn2.model.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import uap.workflow.bpmn2.model.BaseElement;
import uap.workflow.bpmn2.model.FlowNode;
import uap.workflow.bpmn2.model.Process;
import uap.workflow.bpmn2.model.definition.CancelEventDefinition;
import uap.workflow.bpmn2.model.definition.CompensateEventDefinition;
import uap.workflow.bpmn2.model.definition.ConditionalEventDefinition;
import uap.workflow.bpmn2.model.definition.ErrorEventDefinition;
import uap.workflow.bpmn2.model.definition.EscalationEventDefinition;
import uap.workflow.bpmn2.model.definition.EventDefinition;
import uap.workflow.bpmn2.model.definition.LinkEventDefinition;
import uap.workflow.bpmn2.model.definition.MessageEventDefinition;
import uap.workflow.bpmn2.model.definition.SignalEventDefinition;
import uap.workflow.bpmn2.model.definition.TerminateEventDefinition;
import uap.workflow.bpmn2.model.definition.TimerEventDefinition;
import uap.workflow.modeler.utils.CreateElementUtils;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Event")
public class Event extends FlowNode {

	private static final long serialVersionUID = 2286264104117046500L;

	@XmlElement(name = "cancelEventDefinition")
	public CancelEventDefinition cancelEventDefinition;
	@XmlElement(name = "compensateEventDefinition")
	public CompensateEventDefinition compensateEventDefinition;
	@XmlElement(name = "conditionalEventDefinition")
	public ConditionalEventDefinition conditionalEventDefinition;
	@XmlElement(name = "errorEventDefinition")
	public ErrorEventDefinition errorEventDefinition;
	@XmlElement(name = "escalationEventDefinition")
	public EscalationEventDefinition escalationEventDefinition;
	@XmlElement(name = "linkEventDefinition")
	public LinkEventDefinition linkEventDefinition;
	@XmlElement(name = "messageEventDefinition")
	public MessageEventDefinition messageEventDefinition;
	@XmlElement(name = "signalEventDefinition")
	public SignalEventDefinition signalEventDefinition;
	@XmlElement(name = "timerEventDefinition")
	public TimerEventDefinition timerEventDefinition;
	@XmlElement(name = "terminateEventDefinition")
	public TerminateEventDefinition terminateEventDefinition;
	@XmlElement(name = "eventDefinitions")
	protected List<EventDefinition> eventDefinitions = new ArrayList<EventDefinition>();

	public CancelEventDefinition getCancelEventDefinition() {
		return cancelEventDefinition;
	}

	public void setCancelEventDefinition(CancelEventDefinition cancelEventDefinition) {
		this.cancelEventDefinition = cancelEventDefinition;
	}

	public CompensateEventDefinition getCompensateEventDefinition() {
		return compensateEventDefinition;
	}

	public void setCompensateEventDefinition(CompensateEventDefinition compensateEventDefinition) {
		this.compensateEventDefinition = compensateEventDefinition;
	}

	public ConditionalEventDefinition getConditionalEventDefinition() {
		return conditionalEventDefinition;
	}

	public void setConditionalEventDefinition(ConditionalEventDefinition conditionalEventDefinition) {
		this.conditionalEventDefinition = conditionalEventDefinition;
	}

	public ErrorEventDefinition getErrorEventDefinition() {
		return errorEventDefinition;
	}

	public void setErrorEventDefinition(ErrorEventDefinition errorEventDefinition) {
		this.errorEventDefinition = errorEventDefinition;
	}

	public EscalationEventDefinition getEscalationEventDefinition() {
		return escalationEventDefinition;
	}

	public void setEscalationEventDefinition(EscalationEventDefinition escalationEventDefinition) {
		this.escalationEventDefinition = escalationEventDefinition;
	}

	public LinkEventDefinition getLinkEventDefinition() {
		return linkEventDefinition;
	}

	public void setLinkEventDefinition(LinkEventDefinition linkEventDefinition) {
		this.linkEventDefinition = linkEventDefinition;
	}

	public MessageEventDefinition getMessageEventDefinition() {
		return messageEventDefinition;
	}

	public void setMessageEventDefinition(MessageEventDefinition messageEventDefinition) {
		this.messageEventDefinition = messageEventDefinition;
	}

	public SignalEventDefinition getSignalEventDefinition() {
		return signalEventDefinition;
	}

	public void setSignalEventDefinition(SignalEventDefinition signalEventDefinition) {
		this.signalEventDefinition = signalEventDefinition;
	}

	public TimerEventDefinition getTimerEventDefinition() {
		return timerEventDefinition;
	}

	public void setTimerEventDefinition(TimerEventDefinition timerEventDefinition) {
		this.timerEventDefinition = timerEventDefinition;
	}

	public List<EventDefinition> getEventDefinitions() {
		return this.eventDefinitions;
	}

	public void setEventDefinitions(List<EventDefinition> eventDefinitions) {
		this.eventDefinitions = eventDefinitions;
	}

//	@Override
//	public String provideBeanInfoClass() {
//		return "nc.activiti.bpmn2.beaninfos.EventBeanInfo";
//	}
	
	
	
	public void ConstructDefinition(String clzNames){
		
		String[] arrayClz = clzNames.split(",");
		
		for(int i = 0; i < arrayClz.length; i++){
			if (arrayClz[i].length() < 1)
				continue;
			Class<?> clz = null;
			try {
				clz = Class.forName(arrayClz[i]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			java.lang.reflect.Field[] fields = getClass().getFields();//81702804
			for(int j = 0; j < fields.length; j++){
				if(fields[j].getType().equals(clz)){
					try {
						fields[j].set(this, clz.newInstance());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		
	}

	@Override
	public Object replicate() {
		try {
			BaseElement cloneObj = (BaseElement) Class.forName(getClass().getName()).newInstance();
			Method[] methods = getClass().getMethods();
			for (Method setMethod : methods) {
				if (setMethod.getName().startsWith("set")) {
					if (setMethod.getParameterTypes()[0].equals(List.class)) 
						continue;
					Object value = null;
					// id重新生成
					if (setMethod.getName().equals("setId")) {
						if(cloneObj instanceof Process){
							value = java.util.UUID.randomUUID().toString();
						}else{
							value = new CreateElementUtils().generateElementId(cloneObj);
						}
					} else {
						String getMethodName = "get" + setMethod.getName().substring(3, setMethod.getName().length());
						if (setMethod.getParameterTypes()[0].getName().equals("boolean")) {
							getMethodName = "is" + setMethod.getName().substring(3, setMethod.getName().length());
						}
						for (Method getMethod : methods) {
							if (getMethod.getName().equals(getMethodName)) {
								value = getMethod.invoke(this, new Object[] {});
								break;
							}
						}
					}
					setMethod.invoke(cloneObj, value);
				}
			}
			if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=7&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-7,((Event)cloneObj).name.length()).equals("Message"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.MessageEventDefinition");
			}
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=6&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-6,((Event)cloneObj).name.length()).equals("Cancel"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.CancelEventDefinition");
			}
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=12&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-12,((Event)cloneObj).name.length()).equals("Compensation"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.CompensateEventDefinition");
			}
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=11&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-11,((Event)cloneObj).name.length()).equals("Conditional"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.ConditionalEventDefinition");
			}
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=5&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-5,((Event)cloneObj).name.length()).equals("Error"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.ErrorEventDefinition");
			}	
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=10&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-10,((Event)cloneObj).name.length()).equals("Escalation"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.EscalationEventDefinition");
			}	
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=4&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-4,((Event)cloneObj).name.length()).equals("Link"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.LinkEventDefinition");
			}
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=6&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-6,((Event)cloneObj).name.length()).equals("Signal"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.SignalEventDefinition");
			}	
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=5&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-5,((Event)cloneObj).name.length()).equals("Timer"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.TimerEventDefinition");
			}
			else if(cloneObj instanceof Event&&((Event)cloneObj).name.length()>=9&&((Event)cloneObj).name.substring(((Event)cloneObj).name.length()-9,((Event)cloneObj).name.length()).equals("Terminate"))
			{
				((Event)cloneObj).ConstructDefinition("uap.workflow.bpmn2.model.definition.TerminateEventDefinition");
			}
			return cloneObj;
		} catch (Exception e) {
			return null;
		}
	}
}
