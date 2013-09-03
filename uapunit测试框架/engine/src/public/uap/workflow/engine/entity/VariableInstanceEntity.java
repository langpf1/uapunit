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
import java.util.List;
import nc.bs.framework.common.NCLocator;

import uap.workflow.engine.delegate.VariableScope;
import uap.workflow.engine.itf.IVariableInstanceBill;
import uap.workflow.engine.utils.ProcessDefinitionUtil;
import uap.workflow.engine.variable.ValueFields;
import uap.workflow.engine.variable.VariableType;
/**
 * @author Tom Baeyens
 */
public class VariableInstanceEntity extends VariableScopeImpl implements ValueFields, Serializable {
	private static final long serialVersionUID = 1L;
	protected String id;
	protected int revision;
	protected String name;
	protected String processInstanceId;
	protected String executionId;
	protected String taskId;
	protected Long longValue;
	protected Double doubleValue;
	protected String textValue;
	protected String textValue2;
	protected Object cachedValue;
	protected VariableType type;
	public VariableInstanceEntity() {}
	public static VariableInstanceEntity createAndInsert(String name, VariableType type, Object value) {
		VariableInstanceEntity variableInstance = create(name, type, value);
		variableInstance.asyn();
		return variableInstance;
	}
	public static VariableInstanceEntity create(String name, VariableType type, Object value) {
		VariableInstanceEntity variableInstance = new VariableInstanceEntity();
		variableInstance.name = name;
		variableInstance.type = type;
		variableInstance.setValue(value);
		return variableInstance;
	}
	public void setExecution(ActivityInstanceEntity execution) {
		this.executionId = execution.getActInsPk();
		this.processInstanceId = execution.getProcessInstance().getProInsPk();
	}
	public void delete() {
		NCLocator.getInstance(ProcessDefinitionUtil.getProp()).lookup(IVariableInstanceBill.class).delete(this.getId());
	}
	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	public void setExecutionId(String executionId) {
		this.executionId = executionId;
	}
	public Object getValue() {
		if (!type.isCachable() || cachedValue == null) {
			cachedValue = type.getValue(this);
		}
		return cachedValue;
	}
	public void setValue(Object value) {
		type.setValue(value, this);
		cachedValue = value;
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTextValue() {
		return textValue;
	}
	public String getProcessInstanceId() {
		return processInstanceId;
	}
	public String getExecutionId() {
		return executionId;
	}
	public Long getLongValue() {
		return longValue;
	}
	public void setLongValue(Long longValue) {
		this.longValue = longValue;
	}
	public Double getDoubleValue() {
		return doubleValue;
	}
	public void setDoubleValue(Double doubleValue) {
		this.doubleValue = doubleValue;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public String getName() {
		return name;
	}
	public int getRevision() {
		return revision;
	}
	public void setRevision(int revision) {
		this.revision = revision;
	}
	public void setType(VariableType type) {
		this.type = type;
	}
	public VariableType getType() {
		return type;
	}
	public Object getCachedValue() {
		return cachedValue;
	}
	public void setCachedValue(Object cachedValue) {
		this.cachedValue = cachedValue;
	}
	public String getTextValue2() {
		return textValue2;
	}
	public void setTextValue2(String textValue2) {
		this.textValue2 = textValue2;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	@Override
	protected List<VariableInstanceEntity> loadVariableInstances() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected VariableScope getParentVariableScope() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void initializeVariableInstanceBackPointer(VariableInstanceEntity variableInstance) {
		// TODO Auto-generated method stub
		
	}
}
