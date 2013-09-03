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
import java.util.List;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IInstanceListener;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.ITransition;
/**
 * @author Tom Baeyens
 */
public class TransitionImpl extends ProcessElementImpl implements ITransition {
	private static final long serialVersionUID = 1L;
	protected IActivity source;
	protected IActivity destination;
	protected List<IInstanceListener> executionListeners;
	protected List<Integer> waypoints = new ArrayList<Integer>();
	public TransitionImpl(String id, IProcessDefinition processDefinition) {
		super(id, processDefinition);
	}
	public IActivity getSource() {
		return source;
	}
	public void setSource(IActivity source) {
		this.source = source;
	}
	public void setDestination(IActivity destination) {
		this.destination = destination;
		destination.getIncomingTransitions().add(this);
	}
	public IActivity getDestination() {
		return destination;
	}
	public void addExecutionListener(IInstanceListener executionListener) {
		if (executionListeners == null) {
			executionListeners = new ArrayList<IInstanceListener>();
		}
		executionListeners.add(executionListener);
	}
	public String toString() {
		return "(" + source.getId() + ")--" + (id != null ? id + "-->(" : ">(") + destination.getId() + ")";
	}
	@SuppressWarnings("unchecked")
	public List<IInstanceListener> getExecutionListeners() {
		if (executionListeners == null) {
			return Collections.EMPTY_LIST;
		}
		return executionListeners;
	}
	public void setExecutionListeners(List<IInstanceListener> executionListeners) {
		this.executionListeners = executionListeners;
	}
	public List<Integer> getWaypoints() {
		return waypoints;
	}
	public void setWaypoints(List<Integer> waypoints) {
		this.waypoints = waypoints;
	}
}
