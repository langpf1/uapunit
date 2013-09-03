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
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.handler.ActivityInstanceEndHandler;
import uap.workflow.engine.history.HistoricFormProperty;
import uap.workflow.engine.util.ClockUtil;
/**
 * @author Tom Baeyens
 */
public class HistoricFormPropertyEntity extends HistoricDetailEntity implements HistoricFormProperty {
	protected String propertyId;
	protected String propertyValue;
	public HistoricFormPropertyEntity() {}
	public HistoricFormPropertyEntity(IActivityInstance execution, String propertyId, String propertyValue) {
		this(execution, propertyId, propertyValue, null);
	}
	public HistoricFormPropertyEntity(IActivityInstance execution, String propertyId, String propertyValue, String taskId) {
		this.processInstanceId = execution.getProcessInstance().getProInsPk();
		this.executionId = execution.getActInsPk();
		this.taskId = taskId;
		this.propertyId = propertyId;
		this.propertyValue = propertyValue;
		this.time = ClockUtil.getCurrentTime();
		HistoricActivityInstanceEntity historicActivityInstance = ActivityInstanceEndHandler.findActivityInstance(execution);
		if (historicActivityInstance != null) {
			this.activityInstanceId = historicActivityInstance.getId();
		}
	}
	public String getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	public String getPropertyValue() {
		return propertyValue;
	}
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
}
