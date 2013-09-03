package uap.workflow.engine.itf;
import uap.workflow.engine.vos.EventSubscriptionVO;
public interface IEventSubscriptionQry {
	EventSubscriptionVO getEventSubscriptionByPk(String pk_subscription);
	EventSubscriptionVO[] getEventByEventName(String eventName);
	EventSubscriptionVO[] getEventSubscriptionByExecutionID(String executionId, String type);
	EventSubscriptionVO[] getSignalEventSubscriptionsByProinsAndEventName(String ProinsPk, String signalName);
	//modify begine
	EventSubscriptionVO[] getEventSubscriptionByProcessInstanceAndActivityId(String processInstance, String activityId,String type);
	EventSubscriptionVO[] getEventSubscriptionByProcessInstance(String processInstance, String type);
	//modify end
}
