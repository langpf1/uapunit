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
package uap.workflow.engine.jobexecutor;
import java.util.logging.Level;
import java.util.logging.Logger;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.interceptor.CommandContext;
/**
 * @author Tom Baeyens
 * @author Joram Barrez
 */
public class TimerExecuteNestedActivityJobHandler implements JobHandler {
	private static Logger log = Logger.getLogger(TimerExecuteNestedActivityJobHandler.class.getName());
	public static final String TYPE = "timer-transition";
	public String getType() {
		return TYPE;
	}
	public void execute(String configuration, ActivityInstanceEntity execution, CommandContext commandContext) {
		// 这个用来执行边界的time
		IActivity borderEventActivity = execution.getProcessDefinition().findActivity(configuration);
		if (borderEventActivity == null) {
			throw new WorkflowException("Error while firing timer: border event activity " + configuration + " not found");
		}
		try {
			execution=execution.createExecution(borderEventActivity);
			borderEventActivity.getActivityBehavior().execute(execution);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "exception during timer execution", e);
			throw e;
		} catch (Exception e) {
			log.log(Level.SEVERE, "exception during timer execution", e);
			throw new WorkflowException("exception during timer execution: " + e.getMessage(), e);
		}
	}
}
