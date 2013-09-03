package uap.workflow.modeler.bpmn2.beaninfos;

import java.lang.reflect.Field;

import nc.bs.logging.Logger;
import uap.workflow.bpmn2.model.definition.CancelEventDefinition;
import uap.workflow.bpmn2.model.definition.ConditionalEventDefinition;
import uap.workflow.bpmn2.model.definition.EscalationEventDefinition;
import uap.workflow.bpmn2.model.definition.MessageEventDefinition;
import uap.workflow.bpmn2.model.definition.SignalEventDefinition;
import uap.workflow.bpmn2.model.definition.TerminateEventDefinition;
import uap.workflow.bpmn2.model.definition.TimerEventDefinition;
import uap.workflow.bpmn2.model.event.BoundaryEvent;
import uap.workflow.modeler.utils.BpmnModelerConstants;

public class BoundaryEventBeanInfo extends EventBeanInfo {

	public BoundaryEventBeanInfo() {
		super(BoundaryEvent.class);
		addProperty("cancelActivity").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
		addProperty("attachedToRef").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
	}
	
	@Override
	public void adjustProperties(Object obj) {
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
			if (clz.equals(ConditionalEventDefinition.class) ||
					clz.equals(TimerEventDefinition.class) ||
					clz.equals(CancelEventDefinition.class) ||
					clz.equals(SignalEventDefinition.class) ||
					clz.equals(EscalationEventDefinition.class) ||
					clz.equals(TerminateEventDefinition.class) ||
					clz.equals(MessageEventDefinition.class)){
				
				addProperty("interrupting").setCategory(BpmnModelerConstants.CATEGORY_MAINCONFIG);
			}
		}
		super.adjustProperties(obj);
	}
}
