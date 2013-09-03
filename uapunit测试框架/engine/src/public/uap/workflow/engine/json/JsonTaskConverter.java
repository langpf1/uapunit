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
package uap.workflow.engine.json;
import java.io.Reader;
import uap.workflow.engine.core.ITask;
import uap.workflow.engine.exception.WorkflowException;
/**
 * @author Tom Baeyens
 */
public class JsonTaskConverter extends JsonObjectConverter<ITask> {
	public ITask toObject(Reader reader) {
		throw new WorkflowException("not yet implemented");
	}
	public JSONObject toJsonObject(ITask task) {
		ITask taskEntity = task;
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", taskEntity.getTaskPk());
		// jsonObject.put("dbversion", taskEntity.getRevision());
		// jsonObject.put("assignee", taskEntity.getAssignee());
		jsonObject.put("name", taskEntity.getName());
		// /jsonObject.put("priority", taskEntity.getPriority());
		jsonObject.put("createTime", taskEntity.getCreateTime());
		if (taskEntity.getProcessDefinition() != null) {
			jsonObject.put("processDefinition", taskEntity.getProcessDefinition().getProDefPk());
		}
		return jsonObject;
	}
}
