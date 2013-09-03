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
package uap.workflow.engine.pvm.runtime;
import java.util.List;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.entity.ActivityInstanceEntity;
/**
 * @author Tom Baeyens
 */
public class AtomicOperationDeleteCascade implements AtomicOperation {
	
	private static final long serialVersionUID = -2009038230472869429L;
	public boolean isAsync(IActivityInstance execution) {
		return false;
	}
	public void execute(IActivityInstance execution) {
		ActivityInstanceEntity firstLeaf = (ActivityInstanceEntity) findFirstLeaf(execution);
		if (firstLeaf.getSubProcessInstance() != null) {
			firstLeaf.getSubProcessInstance().deleteCascade("");
		}
		firstLeaf.performOperation(AtomicOperation.DELETE_CASCADE_FIRE_ACTIVITY_END);
	}
	protected IActivityInstance findFirstLeaf(IActivityInstance execution) {
		List<IActivityInstance> executions = (List<IActivityInstance>) execution.getExecutions();
		if (executions.size() > 0) {
			return findFirstLeaf(executions.get(0));
		}
		return execution;
	}
}
