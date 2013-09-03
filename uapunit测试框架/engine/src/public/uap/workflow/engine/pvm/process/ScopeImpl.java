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
package uap.workflow.engine.pvm.process;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.workflow.engine.bpmn.data.IOSpecification;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.exception.WorkflowRuntimeException;
/**
 * @author Tom Baeyens
 */
public abstract class ScopeImpl extends ProcessElementImpl implements IScope {
	private static final long serialVersionUID = 1L;
	protected List<IActivity> activities = new ArrayList<IActivity>();
	protected Map<String, IActivity> namedActivities = new HashMap<String, IActivity>();
	protected Map<String, List<IInstanceListener>> executionListeners = new HashMap<String, List<IInstanceListener>>();
	protected IOSpecification ioSpecification;
	public ScopeImpl(String id, IProcessDefinition processDefinition) {
		super(id, processDefinition);
	}
	public IActivity findActivity(String activityId) {
		IActivity localActivity = namedActivities.get(activityId);
		if (localActivity != null) {
			return localActivity;
		}
		for (IActivity activity : activities) {
			IActivity nestedActivity = activity.findActivity(activityId);
			if (nestedActivity != null) {
				return nestedActivity;
			}
		}
		return null;
	}
	public IActivity createActivity() {
		return createActivity(null);
	}
	public IActivity createActivity(String activityId) {
		IActivity activity = new ActivityImpl(activityId, processDefinition);
		if (activityId != null) {
			if (processDefinition.findActivity(activityId) != null) {
				throw new WorkflowRuntimeException("duplicate activity id '" + activityId + "'");
			}
			namedActivities.put(activityId, activity);
		}
		activity.setParent(this);
		activities.add(activity);
		return activity;
	}
	public boolean contains(IActivity activity) {
		if (namedActivities.containsKey(activity.getId())) {
			return true;
		}
		for (IActivity nestedActivity : activities) {
			if (nestedActivity.contains(activity)) {
				return true;
			}
		}
		return false;
	}
	// event listeners
	// //////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public List<IInstanceListener> getExecutionListeners(String eventName) {
		List<IInstanceListener> executionListenerList = getExecutionListeners().get(eventName);
		if (executionListenerList == null) {
			executionListenerList = getExecutionListeners().get("extensionListener");
		}else{
			executionListenerList.addAll(getExecutionListeners().get("extensionListener"));
		}
		if (executionListenerList != null) {
			return executionListenerList;
		}
		return Collections.EMPTY_LIST;
	}

	public void addExecutionListener(String eventName, IInstanceListener executionListener) {
		addExecutionListener(eventName, executionListener, -1);
	}
	public void addExecutionListener(String eventName, IInstanceListener executionListener, int index) {
		List<IInstanceListener> listeners = executionListeners.get(eventName);
		if (listeners == null) {
			listeners = new ArrayList<IInstanceListener>();
			executionListeners.put(eventName, listeners);
		}
		if (index < 0) {
			listeners.add(executionListener);
		} else {
			listeners.add(index, executionListener);
		}
	}
	public Map<String, List<IInstanceListener>> getExecutionListeners() {
		return executionListeners;
	}
	public List<IActivity> getActivities() {
		return activities;
	}
	public IOSpecification getIoSpecification() {
		return ioSpecification;
	}
	public void setIoSpecification(IOSpecification ioSpecification) {
		this.ioSpecification = ioSpecification;
	}
}
