package uap.workflow.modeler.bpmn2.beaninfos;

import java.lang.reflect.Field;

import nc.bs.logging.Logger;
import uap.workflow.bpmn2.model.definition.CancelEventDefinition;
import uap.workflow.bpmn2.model.definition.CompensateEventDefinition;
import uap.workflow.bpmn2.model.definition.ConditionalEventDefinition;
import uap.workflow.bpmn2.model.definition.ErrorEventDefinition;
import uap.workflow.bpmn2.model.definition.EscalationEventDefinition;
import uap.workflow.bpmn2.model.definition.LinkEventDefinition;
import uap.workflow.bpmn2.model.definition.MessageEventDefinition;
import uap.workflow.bpmn2.model.definition.SignalEventDefinition;
import uap.workflow.bpmn2.model.definition.TerminateEventDefinition;
import uap.workflow.bpmn2.model.definition.TimerEventDefinition;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class EventBeanInfo extends FlowNodeBeanInfo {

	public EventBeanInfo(Class<?> type) {
		super(type);
	}

	public void adjustProperties(Object obj) {
		//IntermediateCatchEvent event = (IntermediateCatchEvent)obj;
		Field[] fields = obj.getClass().getFields();
		Object fieldValue = null;
		for(Field field : fields){
			try{
				fieldValue = field.get(obj);
			}catch(Exception e){
				Logger.error(e.getMessage());
			}
			if (fieldValue == null)
				continue;
			Class<?> clz = field.getType(); 
			if (clz.equals(LinkEventDefinition.class)){
				addProperty("linkEventDefinition.target.value", obj.getClass(),true).setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
			}
			if(clz.equals(TimerEventDefinition.class)){
				addProperty("timerEventDefinition.timeDate", obj.getClass(),true).setCategory("Timer Event Definition");
				addProperty("timerEventDefinition.timeDuration", obj.getClass(),true).setCategory("Timer Event Definition");
				addProperty("timerEventDefinition.timeCycle", obj.getClass(),true).setCategory("Timer Event Definition");
			}
			if(clz.equals(CompensateEventDefinition.class)){
				addProperty("compensateEventDefinition.waitForCompletion", obj.getClass(),true).setCategory("Compensate Event Definition");
				addProperty("compensateEventDefinition.activityReference", obj.getClass(),true).setCategory("Compensate Event Definition");
			}
			if(clz.equals(CancelEventDefinition.class)){
			}
			if(clz.equals(ConditionalEventDefinition.class)){
				addProperty("conditionalEventDefinition.condition.value", obj.getClass(),true).setCategory("Conditional Event Definition");
			}
			if(clz.equals(ErrorEventDefinition.class)){
				addProperty("errorEventDefinition.errorCode", obj.getClass(),true).setCategory("Error Event Definition");
			}
			if(clz.equals(EscalationEventDefinition.class)){
				addProperty("escalationEventDefinition.escalationRef", obj.getClass(),true).setCategory("Escalation Event Definition");
			}
			if(clz.equals(MessageEventDefinition.class)){
				//addProperty("messageEventDefinition.operationRef.name", obj.getClass(),true).setCategory("Message Event Definition");
				//addProperty("messageEventDefinition.messageRef.name", obj.getClass(),true).setCategory("Message Event Definition");
				addProperty("messageEventDefinition.operationRef", obj.getClass(),true).setCategory("Message Event Definition");
				addProperty("messageEventDefinition.messageRef", obj.getClass(),true).setCategory("Message Event Definition");
			}
			if(clz.equals(SignalEventDefinition.class)){
				addProperty("signalEventDefinition.signalRef", obj.getClass(),true).setCategory("Signal Event Definition");
			}
			if(clz.equals(TerminateEventDefinition.class)){
			}
		}
	}

}
