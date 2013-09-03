package uap.workflow.modeler.bpmn2.beaninfos;

import java.lang.reflect.Field;

import uap.workflow.bpmn2.model.definition.CancelEventDefinition;
import uap.workflow.bpmn2.model.definition.ConditionalEventDefinition;
import uap.workflow.bpmn2.model.definition.EscalationEventDefinition;
import uap.workflow.bpmn2.model.definition.EventDefinition;
import uap.workflow.bpmn2.model.definition.MessageEventDefinition;
import uap.workflow.bpmn2.model.definition.SignalEventDefinition;
import uap.workflow.bpmn2.model.definition.TerminateEventDefinition;
import uap.workflow.bpmn2.model.definition.TimerEventDefinition;
import uap.workflow.bpmn2.model.event.StartEvent;
import uap.workflow.modeler.utils.BpmnModelerConstants;

import nc.bs.logging.Logger;

public class StartEventBeanInfo extends EventBeanInfo {

	public StartEventBeanInfo() {
		super(StartEvent.class);
	}
	
	@Override
	public void adjustProperties(Object obj) {
		Field[] fields = obj.getClass().getFields();
		Object fieldValue = null;
		boolean isStartEventNone = true;
		for(Field field : fields){
			try{
				fieldValue = field.get(obj);
			}catch(Exception e){
				Logger.error(e.getMessage());
			}
			if (fieldValue == null)
				continue;
			Class<?> clz = field.getType(); 
			if (clz.equals(ConditionalEventDefinition.class) ||
					clz.equals(TimerEventDefinition.class) ||
					clz.equals(CancelEventDefinition.class) ||
					clz.equals(SignalEventDefinition.class) ||
					clz.equals(EscalationEventDefinition.class) ||
					clz.equals(TerminateEventDefinition.class) ||
					clz.equals(MessageEventDefinition.class)){
				addProperty("interrupting").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
			}
			
			if (clz.getSuperclass() != null && clz.getSuperclass().equals(EventDefinition.class)){		//如果包含EventDefinition，则不是starteventnone
				isStartEventNone = false;
			}
		}
		if (isStartEventNone){
			addProperty("initiator").setCategory(BpmnModelerConstants.CATEGORY_FORM);
			addProperty("formKey").setCategory(BpmnModelerConstants.CATEGORY_FORM);
			addProperty("formProperties").setCategory(BpmnModelerConstants.CATEGORY_FORM);
		}
		super.adjustProperties(obj);
	}

}
