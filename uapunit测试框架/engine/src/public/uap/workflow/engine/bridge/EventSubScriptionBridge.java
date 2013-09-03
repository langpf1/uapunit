package uap.workflow.engine.bridge;
import nc.vo.pub.lang.UFDateTime;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
import uap.workflow.engine.entity.EventSubscriptionEntity;
import uap.workflow.engine.entity.SignalEventSubscriptionEntity;
import uap.workflow.engine.utils.DateUtil;
import uap.workflow.engine.vos.EventSubscriptionVO;
public class EventSubScriptionBridge implements IBridge<EventSubscriptionVO, EventSubscriptionEntity> {
	@Override
	public EventSubscriptionVO convertT2M(EventSubscriptionEntity object) {
		EventSubscriptionVO vo = new EventSubscriptionVO();
		vo.setActivity_id(object.getActivityId());
		vo.setCreated(new UFDateTime(object.getCreated()));
		vo.setEventname(object.getEventName());
		vo.setEventtype(object.getEventType());
		vo.setPk_execution(object.getExecution().getActInsPk());
		vo.setPk_processInstance(object.getExecution().getProcessInstance().getProInsPk());
		vo.setPk_subscription(object.getId());
		return vo;
	}
	@Override
	public EventSubscriptionEntity convertM2T(EventSubscriptionVO object) {
		EventSubscriptionEntity entity = null;
		if ("signal".equalsIgnoreCase(object.getEventtype())) {
			entity = new SignalEventSubscriptionEntity();
		} else if ("compensate".equalsIgnoreCase(object.getEventtype())) {
			entity = new CompensateEventSubscriptionEntity();
		}
		entity.setActivityId(object.getActivity_id());
		entity.setCreated(DateUtil.convert(object.getCreated()));
		entity.setEventName(object.getEventname());
		entity.setExecutionId(object.getPk_execution());
		entity.setProcessInstanceId(object.getPk_processInstance());
		entity.setId(object.getPk_subscription());
		return entity;
	}
}
