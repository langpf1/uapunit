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
import java.util.List;
import java.util.Map;


import uap.workflow.engine.core.ITask;
import uap.workflow.engine.entity.VariableInstanceEntity;
import uap.workflow.engine.persistence.AbstractManager;
import uap.workflow.engine.service.WfmServiceFacility;
import uap.workflow.engine.utils.VariableInstanceUtil;
/**
 * @author Tom Baeyens
 */
public class VariableInstanceManager extends AbstractManager {
	public void deleteVariableInstance(VariableInstanceEntity variableInstance) {
		WfmServiceFacility.getVariableInstanceBill().delete(variableInstance.getId());
	}
	public List<VariableInstanceEntity> findVariableInstancesByTaskId(String taskId) {
		return VariableInstanceUtil.getVarInsByTaskPk(taskId);
	}
	public List<VariableInstanceEntity> findVariableInstancesByExecutionId(String executionId) {
		return VariableInstanceUtil.getVarInsByActInsPk(executionId);
	}
	public List<VariableInstanceEntity> findVariableInstancesByProcessInstancePk(String pk_processInstance) {
		return VariableInstanceUtil.getVarInsByProInsPk(pk_processInstance);
	}
	public void deleteVariableInstanceByTask(ITask task) {
		Map<String, VariableInstanceEntity> variableInstances = task.getVariableInstances();
		if (variableInstances != null) {
			for (VariableInstanceEntity variableInstance : variableInstances.values()) {
				deleteVariableInstance(variableInstance);
			}
		}
	}
}
