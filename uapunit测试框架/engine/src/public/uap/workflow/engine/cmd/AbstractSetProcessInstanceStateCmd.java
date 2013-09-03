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
package uap.workflow.engine.cmd;

import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.entity.SuspensionState;
import uap.workflow.engine.entity.SuspensionState.SuspensionStateUtil;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.Command;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * 
 * @author Daniel Meyer
 */
public abstract class AbstractSetProcessInstanceStateCmd implements Command<Void> {
	protected final String executionId;
	public AbstractSetProcessInstanceStateCmd(String executionId) {
		this.executionId = executionId;
	}
	public Void execute(CommandContext commandContext) {
		if (executionId == null) {
			throw new WorkflowException("ProcessInstanceId cannot be null.");
		}
		ActivityInstanceEntity executionEntity = commandContext.getExecutionManager().getActInsByActInsPk(executionId);
		if (executionEntity == null) {
			throw new WorkflowException("Cannot find processInstance for id '" + executionId + "'.");
		}
		SuspensionStateUtil.setSuspensionState(executionEntity, getNewState());
		return null;
	}
	protected abstract SuspensionState getNewState();
}
