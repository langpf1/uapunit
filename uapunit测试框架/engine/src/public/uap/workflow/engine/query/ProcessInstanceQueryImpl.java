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
package uap.workflow.engine.query;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

import uap.workflow.engine.core.IProcessInstance;
import uap.workflow.engine.entity.SuspensionState;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.interceptor.CommandExecutor;
import uap.workflow.engine.runtime.ProcessInstanceQuery;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 * @author Frederik Heremans
 * @author Falko Menge
 * @author Daniel Meyer
 */
public class ProcessInstanceQueryImpl extends ExecutionVariableQueryImpl<ProcessInstanceQuery, IProcessInstance> implements ProcessInstanceQuery, Serializable {
	private static final long serialVersionUID = 1L;
	protected String executionId;
	protected String businessKey;
	protected String processDefinitionId;
	protected Set<String> processInstanceIds;
	protected String processDefinitionKey;
	protected String superProcessInstanceId;
	protected String subProcessInstanceId;
	protected SuspensionState suspensionState;
	// Unused, see dynamic query
	protected String activityId;
	protected String eventSubscriptionName;
	protected String eventSubscriptionType;
	public ProcessInstanceQueryImpl() {}
	public ProcessInstanceQueryImpl(CommandContext commandContext) {
		super(commandContext);
	}
	public ProcessInstanceQueryImpl(CommandExecutor commandExecutor) {
		super(commandExecutor);
	}
	public ProcessInstanceQueryImpl processInstanceId(String processInstanceId) {
		if (processInstanceId == null) {
			throw new WorkflowException("Process instance id is null");
		}
		this.executionId = processInstanceId;
		return this;
	}
	public ProcessInstanceQuery processInstanceIds(Set<String> processInstanceIds) {
		if (processInstanceIds == null) {
			throw new WorkflowException("Set of process instance ids is null");
		}
		if (processInstanceIds.isEmpty()) {
			throw new WorkflowException("Set of process instance ids is empty");
		}
		this.processInstanceIds = processInstanceIds;
		return this;
	}
	public ProcessInstanceQuery processInstanceBusinessKey(String businessKey) {
		if (businessKey == null) {
			throw new WorkflowException("Business key is null");
		}
		this.businessKey = businessKey;
		return this;
	}
	public ProcessInstanceQuery processInstanceBusinessKey(String businessKey, String processDefinitionKey) {
		if (businessKey == null) {
			throw new WorkflowException("Business key is null");
		}
		this.businessKey = businessKey;
		this.processDefinitionKey = processDefinitionKey;
		return this;
	}
	public ProcessInstanceQueryImpl processDefinitionId(String processDefinitionId) {
		if (processDefinitionId == null) {
			throw new WorkflowException("Process definition id is null");
		}
		this.processDefinitionId = processDefinitionId;
		return this;
	}
	public ProcessInstanceQueryImpl processDefinitionKey(String processDefinitionKey) {
		if (processDefinitionKey == null) {
			throw new WorkflowException("Process definition key is null");
		}
		this.processDefinitionKey = processDefinitionKey;
		return this;
	}
	public ProcessInstanceQuery superProcessInstanceId(String superProcessInstanceId) {
		this.superProcessInstanceId = superProcessInstanceId;
		return this;
	}
	public ProcessInstanceQuery subProcessInstanceId(String subProcessInstanceId) {
		this.subProcessInstanceId = subProcessInstanceId;
		return this;
	}
	public ProcessInstanceQuery orderByProcessInstanceId() {
		this.orderProperty = ProcessInstanceQueryProperty.PROCESS_INSTANCE_ID;
		return this;
	}
	public ProcessInstanceQuery orderByProcessDefinitionId() {
		this.orderProperty = ProcessInstanceQueryProperty.PROCESS_DEFINITION_ID;
		return this;
	}
	public ProcessInstanceQuery orderByProcessDefinitionKey() {
		this.orderProperty = ProcessInstanceQueryProperty.PROCESS_DEFINITION_KEY;
		return this;
	}
	public ProcessInstanceQuery active() {
		this.suspensionState = SuspensionState.ACTIVE;
		return this;
	}
	public ProcessInstanceQuery suspended() {
		this.suspensionState = SuspensionState.SUSPENDED;
		return this;
	}
	// results /////////////////////////////////////////////////////////////////
	public long executeCount(CommandContext commandContext) {
		checkQueryOk();
		ensureVariablesInitialized();
		return commandContext.getExecutionManager().findProcessInstanceCountByQueryCriteria(this);
	}
	public List<IProcessInstance> executeList(CommandContext commandContext, Page page) {
		checkQueryOk();
		ensureVariablesInitialized();
		return commandContext.getExecutionManager().findProcessInstanceByQueryCriteria(this, page);
	}
	// getters /////////////////////////////////////////////////////////////////
	public boolean getOnlyProcessInstances() {
		return true; // See dynamic query in runtime.mapping.xml
	}
	public String getProcessInstanceId() {
		return executionId;
	}
	public Set<String> getProcessInstanceIds() {
		return processInstanceIds;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public String getProcessDefinitionId() {
		return processDefinitionId;
	}
	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}
	public String getActivityId() {
		return null; // Unused, see dynamic query
	}
	public String getSuperProcessInstanceId() {
		return superProcessInstanceId;
	}
	public String getSubProcessInstanceId() {
		return subProcessInstanceId;
	}
	public SuspensionState getSuspensionState() {
		return suspensionState;
	}
	public void setSuspensionState(SuspensionState suspensionState) {
		this.suspensionState = suspensionState;
	}
	public String getEventSubscriptionName() {
		return eventSubscriptionName;
	}
	public void setEventSubscriptionName(String eventSubscriptionName) {
		this.eventSubscriptionName = eventSubscriptionName;
	}
	public String getEventSubscriptionType() {
		return eventSubscriptionType;
	}
	public void setEventSubscriptionType(String eventSubscriptionType) {
		this.eventSubscriptionType = eventSubscriptionType;
	}
}
