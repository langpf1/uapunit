/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uap.workflow.engine.mgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


import uap.workflow.engine.bridge.EventSubScriptionBridge;
import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.entity.CompensateEventSubscriptionEntity;
import uap.workflow.engine.entity.EventSubscriptionEntity;
import uap.workflow.engine.entity.MessageEventSubscriptionEntity;
import uap.workflow.engine.entity.SignalEventSubscriptionEntity;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.query.EventSubscriptionQueryImpl;
import uap.workflow.engine.query.Page;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.vos.EventSubscriptionVO;
/**
 * @author Daniel Meyer
 */
public class EventSubscriptionManager extends AbstractManager {
	/** keep track of subscriptions created in the current command */
	protected List<EventSubscriptionEntity> createdSignalSubscriptions = new ArrayList<EventSubscriptionEntity>();
	protected List<EventSubscriptionEntity> createdCompensateSubscriptions = new ArrayList<EventSubscriptionEntity>();
	public void insert(EventSubscriptionEntity persistentObject) {
		WfmServiceFacility.getEventSubscriptionBill().asyn(new EventSubScriptionBridge().convertT2M(persistentObject));
		if (persistentObject instanceof SignalEventSubscriptionEntity) {
			createdSignalSubscriptions.add(persistentObject);
		}
		if(persistentObject instanceof CompensateEventSubscriptionEntity)
			createdCompensateSubscriptions.add(persistentObject);
	}
	public void deleteEventSubscription(EventSubscriptionEntity persistentObject) {
		WfmServiceFacility.getEventSubscriptionBill().delete(persistentObject.getId());
		if (persistentObject instanceof SignalEventSubscriptionEntity) {
			createdSignalSubscriptions.remove(persistentObject);
		}
	}
	public EventSubscriptionEntity findEventSubscriptionbyId(String id) {
		return (EventSubscriptionEntity) getDbSqlSession().selectOne("selectEventSubscription", id);
	}
	public long findEventSubscriptionCountByQueryCriteria(EventSubscriptionQueryImpl eventSubscriptionQueryImpl) {
		final String query = "selectEventSubscriptionCountByQueryCriteria";
		return (Long) getDbSqlSession().selectOne(query, eventSubscriptionQueryImpl);
	}
	@SuppressWarnings("unchecked")
	public List<EventSubscriptionEntity> findEventSubscriptionsByQueryCriteria(EventSubscriptionQueryImpl eventSubscriptionQueryImpl, Page page) {
		final String query = "selectEventSubscriptionByQueryCriteria";
		return getDbSqlSession().selectList(query, eventSubscriptionQueryImpl, page);
	}
	public List<SignalEventSubscriptionEntity> findSignalEventSubscriptionsByEventName(String eventName) {
		EventSubscriptionVO[] vos = WfmServiceFacility.getEventSubscriptionQry().getEventByEventName(eventName);
		Set<SignalEventSubscriptionEntity> selectList = new HashSet<SignalEventSubscriptionEntity>();
		for (int i = 0; i < vos.length; i++) {
			EventSubscriptionEntity entity = new EventSubScriptionBridge().convertM2T(vos[i]);
			if (entity instanceof SignalEventSubscriptionEntity) {
				selectList.add((SignalEventSubscriptionEntity) entity);
			}
		}
		return new ArrayList<SignalEventSubscriptionEntity>(selectList);
	}
	public List<SignalEventSubscriptionEntity> findSignalEventSubscriptionsByProinsAndEventName(String ProInsPk,String signalName) {
		EventSubscriptionVO[] vos = WfmServiceFacility.getEventSubscriptionQry().getSignalEventSubscriptionsByProinsAndEventName(ProInsPk,signalName);
		Set<SignalEventSubscriptionEntity> selectList = new HashSet<SignalEventSubscriptionEntity>();
		for (int i = 0; i < vos.length; i++) {
			EventSubscriptionEntity entity = new EventSubScriptionBridge().convertM2T(vos[i]);
			if (entity instanceof SignalEventSubscriptionEntity) {
				selectList.add((SignalEventSubscriptionEntity) entity);
			}
		}
		return new ArrayList<SignalEventSubscriptionEntity>(selectList);
	}		
	@SuppressWarnings("unchecked")
	public List<SignalEventSubscriptionEntity> findSignalEventSubscriptionsByExecution(String executionId) {
		final String query = "selectSignalEventSubscriptionsByExecution";
		Set<SignalEventSubscriptionEntity> selectList = new HashSet<SignalEventSubscriptionEntity>(getDbSqlSession().selectList(query, executionId));
		// add events created in this command (not visible yet in query)
		for (EventSubscriptionEntity entity : createdSignalSubscriptions) {
			if (entity instanceof SignalEventSubscriptionEntity && executionId.equals(entity.getExecutionId())) {
				selectList.add((SignalEventSubscriptionEntity) entity);
			}
		}
		return new ArrayList<SignalEventSubscriptionEntity>(selectList);
	}
	@SuppressWarnings("unchecked")
	public List<SignalEventSubscriptionEntity> findSignalEventSubscriptionsByNameAndExecution(String name, String executionId) {
		final String query = "selectSignalEventSubscriptionsByNameAndExecution";
		Map<String, String> params = new HashMap<String, String>();
		params.put("executionId", executionId);
		params.put("eventName", name);
		Set<SignalEventSubscriptionEntity> selectList = new HashSet<SignalEventSubscriptionEntity>(getDbSqlSession().selectList(query, params));
		// add events created in this command (not visible yet in query)
		for (EventSubscriptionEntity entity : createdSignalSubscriptions) {
			if (entity instanceof SignalEventSubscriptionEntity && executionId.equals(entity.getExecutionId()) && name.equals(entity.getEventName())) {
				selectList.add((SignalEventSubscriptionEntity) entity);
			}
		}
		return new ArrayList<SignalEventSubscriptionEntity>(selectList);
	}
	@SuppressWarnings("unchecked")
	public List<EventSubscriptionEntity> findEventSubscriptions(String executionId, String type) {
		final String query = "selectEventSubscriptionsByExecutionAndType";
		Map<String, String> params = new HashMap<String, String>();
		params.put("executionId", executionId);
		params.put("eventType", type);
		return getDbSqlSession().selectList(query, params);
	}
	@SuppressWarnings("unchecked")
	public List<EventSubscriptionEntity> findEventSubscriptions(String executionId, String type, String activityId) {
		final String query = "selectEventSubscriptionsByExecutionTypeAndActivity";
		Map<String, String> params = new HashMap<String, String>();
		params.put("executionId", executionId);
		params.put("eventType", type);
		params.put("activityId", activityId);
		return getDbSqlSession().selectList(query, params);
	}
	@SuppressWarnings("unchecked")
	public List<EventSubscriptionEntity> findEventSubscriptionsByConfiguration(String type, String configuration) {
		final String query = "selectEventSubscriptionsByConfiguration";
		Map<String, String> params = new HashMap<String, String>();
		params.put("eventType", type);
		params.put("configuration", configuration);
		return getDbSqlSession().selectList(query, params);
	}
	@SuppressWarnings("unchecked")
	public List<EventSubscriptionEntity> findEventSubscriptionByName(String type, String eventName) {
		final String query = "selectEventSubscriptionsByName";
		Map<String, String> params = new HashMap<String, String>();
		params.put("eventType", type);
		params.put("eventName", eventName);
		return getDbSqlSession().selectList(query, params);
	}
	public MessageEventSubscriptionEntity findMessageStartEventSubscriptionByName(String messageName) {
		MessageEventSubscriptionEntity entity = (MessageEventSubscriptionEntity) getDbSqlSession().selectOne("selectMessageStartEventSubscriptionByName", messageName);
		return entity;
	}
}
