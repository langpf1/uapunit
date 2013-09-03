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
package uap.workflow.engine.core;
import java.util.List;

import uap.workflow.engine.bpmn.data.IOSpecification;
/**
 * @author Tom Baeyens
 */
public interface IScope extends IProcessElement {
	List<? extends IActivity> getActivities();
	IActivity findActivity(String activityId);
	IActivity createActivity(String activityId);
	boolean contains(IActivity activity);
	void setIoSpecification(IOSpecification ioSpecification);
	List<IInstanceListener> getExecutionListeners(String eventName);
	void addExecutionListener(String eventName, IInstanceListener executionListener);
}
