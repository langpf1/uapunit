package uap.workflow.engine.impl;
import java.util.List;
import nc.bs.dao.DAOException;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanListProcessor;
import uap.workflow.engine.core.TaskInstanceStatus;
import uap.workflow.engine.dao.WfBaseDAO;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.itf.IEventSubscriptionQry;
import uap.workflow.engine.logger.WorkflowLogger;
import uap.workflow.engine.vos.EventSubscriptionVO;
import uap.workflow.engine.vos.ProcessInstanceVO;
public class EventSubscriptionQry implements IEventSubscriptionQry {
	@Override
	public EventSubscriptionVO getEventSubscriptionByPk(String pk_subscription) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			EventSubscriptionVO[] taskInsVos = (EventSubscriptionVO[]) dao.queryByCondition(EventSubscriptionVO.class, "pk_subscription='" + pk_subscription + "'");
			if (taskInsVos == null || taskInsVos.length == 0) {
				return null;
			}
			return (EventSubscriptionVO) taskInsVos[0];
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public EventSubscriptionVO[] getEventByEventName(String eventName) {
		WfBaseDAO dao = new WfBaseDAO();
		String sql = "select * from wf_subscription where eventtype = 'signal' and eventname = '" + eventName + "'";
		List<EventSubscriptionVO> eventSubscriptionVos;
		try {
			eventSubscriptionVos = (List<EventSubscriptionVO>) dao.executeQuery(sql, new BeanListProcessor(EventSubscriptionVO.class));
			if (eventSubscriptionVos == null || eventSubscriptionVos.size() == 0) {
				return null;
			}
			return eventSubscriptionVos.toArray(new EventSubscriptionVO[0]);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	@SuppressWarnings("unchecked")
	public EventSubscriptionVO[] getSignalEventSubscriptionsByProinsAndEventName(String ProinsPk, String signalName){
		WfBaseDAO dao = new WfBaseDAO();
		String sql = "select * from wf_subscription where eventtype = 'signal' and pk_processinstance = '" + ProinsPk + "'and eventname = '" + signalName + "'";
		List<EventSubscriptionVO> eventSubscriptionVos;
		try {
			eventSubscriptionVos = (List<EventSubscriptionVO>) dao.executeQuery(sql, new BeanListProcessor(EventSubscriptionVO.class));
			if (eventSubscriptionVos == null || eventSubscriptionVos.size() == 0) {
				return null;
			}
			return eventSubscriptionVos.toArray(new EventSubscriptionVO[0]);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage(), e);
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}	
	public EventSubscriptionVO[] getEventSubscriptionByExecutionID(String executionId, String type) {
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_subscription where pk_execution=? and eventtype=?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(executionId);
			parameter.addParam(type);
			@SuppressWarnings("unchecked")
			List<EventSubscriptionVO> vos = (List<EventSubscriptionVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(EventSubscriptionVO.class));
			if (vos == null || vos.size() == 0) {
				return null;
			}
			return vos.toArray(new EventSubscriptionVO[0]);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	//modify begine
	public EventSubscriptionVO[] getEventSubscriptionByProcessInstanceAndActivityId(String processInstance, String activityId,String type){
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_subscription where pk_processinstance=? and activity_id=? and eventtype=?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(processInstance);
			parameter.addParam(activityId);
			parameter.addParam(type);
			@SuppressWarnings("unchecked")
			List<EventSubscriptionVO> vos = (List<EventSubscriptionVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(EventSubscriptionVO.class));
			if (vos == null || vos.size() == 0) {
				return null;
			}
			return vos.toArray(new EventSubscriptionVO[0]);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	public EventSubscriptionVO[] getEventSubscriptionByProcessInstance(String processInstance,String type){
		WfBaseDAO dao = new WfBaseDAO();
		try {
			String sql = "select * from wf_subscription where pk_processinstance=? and eventtype=?";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(processInstance);
			parameter.addParam(type);
			@SuppressWarnings("unchecked")
			List<EventSubscriptionVO> vos = (List<EventSubscriptionVO>) dao.executeQuery(sql, parameter, new BeanListProcessor(EventSubscriptionVO.class));
			if (vos == null || vos.size() == 0) {
				return null;
			}
			return vos.toArray(new EventSubscriptionVO[0]);
		} catch (DAOException e) {
			WorkflowLogger.error(e.getMessage());
			throw new WorkflowRuntimeException(e.getMessage());
		}
	}
	
	//modify end 
}
