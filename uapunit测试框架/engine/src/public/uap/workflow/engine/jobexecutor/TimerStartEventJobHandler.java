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

import uap.workflow.engine.entity.ActivityInstanceEntity;
import uap.workflow.engine.interceptor.CommandContext;
public class TimerStartEventJobHandler implements JobHandler {
	public static final String TYPE = "timer-start-event";
	public String getType() {
		return TYPE;
	}
	public void execute(String configuration, ActivityInstanceEntity execution, CommandContext commandContext) {
		// try {
		// new StartProcessInstanceCmd(configuration, null,
		// null).execute(commandContext);
		// } catch (RuntimeException e) {
		// throw e;
		// } catch (Exception e) {
		// throw new ActivitiException("exception during timer execution: " +
		// e.getMessage(), e);
		// }
	}
}
