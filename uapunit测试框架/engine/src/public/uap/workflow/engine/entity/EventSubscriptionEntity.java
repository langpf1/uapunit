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
package uap.workflow.engine.entity;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.db.PersistentObject;
import uap.workflow.engine.event.EventHandler;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.jobexecutor.ProcessEventJobHandler;
import uap.workflow.engine.util.ClockUtil;
/**
 * @author Daniel Meyer
 */
public abstract class EventSubscriptionEntity implements PersistentObject {
	// persistent state ///////////////////////////
	protected String id;
	protected int revision = 1;
	protected String eventType;
	protected String eventName;
	protected String executionId;
	protected String processInstanceId;
	protected String activityId;
	protected String configuration;
	protected Date created;
	// runtime state /////////////////////////////
	protected IActivityInstance execution;
	protected IActivity activity;
	// ///////////////////////////////////////////
	public EventSubscriptionEntity() {
		this.created = ClockUtil.getCurrentTime();
	}
	public EventSubscriptionEntity(IActivityInstance executionEntity) {
		this();
		setExecution(executionEntity);
		setActivity(execution.getActivity());
		this.processInstanceId = executionEntity.getProcessInstance().getProInsPk();
	}
	// processing /////////////////////////////
	public void eventReceived(Serializable payload, boolean processASync) {
		if (processASync) {
			scheduleEventAsync(payload);
		} else {
			processEventSync(payload);
		}
	}
	protected void processEventSync(Object payload) {
		EventHandler eventHandler = Context.getProcessEngineConfiguration().getEventHandler(eventType);
		if (eventHandler == null) {
			throw new WorkflowException("Could not find eventhandler for event of type '" + eventType + "'.");
		}
		eventHandler.handleEvent(this, payload, Context.getCommandContext());
	}
	protected void scheduleEventAsync(Serializable payload) {
		final CommandContext commandContext = Context.getCommandContext();
		MessageEntity message = new MessageEntity();
		message.setJobHandlerType(ProcessEventJobHandler.TYPE);
		message.setJobHandlerConfiguration(id);
		commandContext.getJobManager().send(message);
	}
	// persistence behavior /////////////////////
	public void delete() {
		Context.getCommandContext().getEventSubscriptionManager().deleteEventSubscription(this);
	}
	public void insert() {
		Context.getCommandContext().getEventSubscriptionManager().insert(this);
	}
	public Object getPersistentState() {
		HashMap<String, Object> persistentState = new HashMap<String, Object>();
		persistentState.put("executionId", executionId);
		persistentState.put("configuration", configuration);
		return persistentState;
	}
	// getters & setters ////////////////////////////
	public IActivityInstance getExecution() {
		if (execution == null && executionId != null) {
			execution = Context.getCommandContext().getExecutionManager().getActInsByActInsPk(executionId);
		}
		return execution;
	}
	public void setExecution(IActivityInstance execution) {
		this.execution = execution;
		if (execution != null) {
			this.executionId = execution.getActInsPk();
		}
	}
	public IActivity getActivity() {
		if (activity == null && activityId != null) {
			IActivityInstance execution = getExecution();
			if (execution != null) {
				IProcessDefinition processDefinition = execution.getProcessDefinition();
				activity = processDefinition.findActivity(activityId);
			}
		}
		return activity;
	}
	public void setActivity(IActivity activity) {
		this.activity = activity;
		if (activity != null) {
			this.activityId = activity.getId();
		}
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getRevision() {
		return revision;
	}
	public void setRevision(int revision) {
		this.revision = revision;
	}
	public int getRevisionNext() {
		return revision + 1;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getExecutionId() {
		return executionId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public String getConfiguration() {
		return configuration;
	}
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}
	public String getActivityId() {
		return activityId;
	}
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventSubscriptionEntity other = (EventSubscriptionEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
