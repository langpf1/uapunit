package uap.workflow.engine.utils;
import java.util.ArrayList;
import java.util.List;


import nc.bs.framework.common.NCLocator;
import uap.workflow.engine.bridge.EventSubScriptionBridge;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
import uap.workflow.engine.event.CompensationEventHandler;
import uap.workflow.engine.itf.IEventSubscriptionQry;
import uap.workflow.engine.vos.EventSubscriptionVO;
public class EventSubscriptionUtil {
	public static List<CompensateEventSubscriptionEntity> getCompensateEventSubscriptionByExecutionID(String executionId) {
		List<CompensateEventSubscriptionEntity> list = new ArrayList<CompensateEventSubscriptionEntity>();
		IEventSubscriptionQry eventSubscriptionQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IEventSubscriptionQry.class);
		EventSubscriptionVO[] eventSubscriptionVos = eventSubscriptionQry.getEventSubscriptionByExecutionID(executionId,CompensationEventHandler.EVENT_HANDLER_TYPE);
		if (eventSubscriptionVos == null || eventSubscriptionVos.length == 0) {
			return list;
		}
		for (int i = 0; i < eventSubscriptionVos.length; i++) {
			list.add((CompensateEventSubscriptionEntity)(new EventSubScriptionBridge().convertM2T(eventSubscriptionVos[0])));
		}
		return list;
	}
	//modify begin
	public static List<CompensateEventSubscriptionEntity> getEventSubscriptionByProcessInstanceAndActivityId(String processInstance,String activityId) {
		List<CompensateEventSubscriptionEntity> list = new ArrayList<CompensateEventSubscriptionEntity>();
		IEventSubscriptionQry eventSubscriptionQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IEventSubscriptionQry.class);
		EventSubscriptionVO[] eventSubscriptionVos = eventSubscriptionQry.getEventSubscriptionByProcessInstanceAndActivityId(processInstance,activityId,CompensationEventHandler.EVENT_HANDLER_TYPE);
		if (eventSubscriptionVos == null || eventSubscriptionVos.length == 0) {
			return list;
		}
		for (int i = 0; i < eventSubscriptionVos.length; i++) {
			list.add((CompensateEventSubscriptionEntity)(new EventSubScriptionBridge().convertM2T(eventSubscriptionVos[0])));
		}
		return list;
	}
	public static List<CompensateEventSubscriptionEntity> getEventSubscriptionByProcessInstance(String processInstance) {
		List<CompensateEventSubscriptionEntity> list = new ArrayList<CompensateEventSubscriptionEntity>();
		IEventSubscriptionQry eventSubscriptionQry = NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IEventSubscriptionQry.class);
		EventSubscriptionVO[] eventSubscriptionVos = eventSubscriptionQry.getEventSubscriptionByProcessInstance(processInstance,CompensationEventHandler.EVENT_HANDLER_TYPE);
		if (eventSubscriptionVos == null || eventSubscriptionVos.length == 0) {
			return list;
		}
		for (int i = 0; i < eventSubscriptionVos.length; i++) {
			list.add((CompensateEventSubscriptionEntity)(new EventSubScriptionBridge().convertM2T(eventSubscriptionVos[0])));
		}
		return list;
	}
	
	
	//midify end

}
