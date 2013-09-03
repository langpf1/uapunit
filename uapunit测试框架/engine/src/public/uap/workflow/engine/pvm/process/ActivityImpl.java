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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uap.workflow.bizimpl.listener.ListenerBizImplExtend;
import uap.workflow.engine.bpmn.behavior.UserTaskActivityBehavior;
import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.exception.WorkflowRuntimeException;
import uap.workflow.engine.message.MsgType;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.task.TaskDefinition;
public class ActivityImpl extends ScopeImpl implements IActivity {
	private static final long serialVersionUID = 1L;
	protected List<ITransition> outgoingTransitions = new ArrayList<ITransition>();
	protected Map<String, ITransition> namedOutgoingTransitions = new HashMap<String, ITransition>();
	protected List<ITransition> incomingTransitions = new ArrayList<ITransition>();
	protected ActivityBehavior activityBehavior;
	protected ListenerBizImplExtend listenerBizImplExtend = null;
	protected IScope parent;
	protected boolean isScope;
	protected boolean isAsync;
	protected boolean isExclusive;
	protected int x = -1;
	protected int y = -1;
	protected int width = -1;
	protected int height = -1;
	public IActivity getParentActivity() {
		if (parent instanceof IActivity) {
			return (IActivity) parent;
		}
		return null;
	}
	public IScope getParent() {
		return parent;
	}
	public ActivityImpl(String id, IProcessDefinition processDefinition) {
		super(id, processDefinition);
	}
	public ITransition createOutgoingTransition() {
		return createOutgoingTransition(null);
	}
	public ITransition createOutgoingTransition(String transitionId) {
		ITransition transition = new TransitionImpl(transitionId, processDefinition);
		transition.setSource(this);
		outgoingTransitions.add(transition);
		if (transitionId != null) {
			if (namedOutgoingTransitions.containsKey(transitionId)) {
				throw new WorkflowRuntimeException("activity '" + id + " has duplicate transition '" + transitionId + "'");
			}
			namedOutgoingTransitions.put(transitionId, transition);
		}
		return transition;
	}
	public ITransition findOutgoingTransition(String transitionId) {
		return namedOutgoingTransitions.get(transitionId);
	}
	public String toString() {
		return "Activity(" + id + ")";
	}
	// restricted setters
	protected void setOutgoingTransitions(List<ITransition> outgoingTransitions) {
		this.outgoingTransitions = outgoingTransitions;
	}
	public void setParent(ScopeImpl parent) {
		this.parent = parent;
	}
	protected void setIncomingTransitions(List<ITransition> incomingTransitions) {
		this.incomingTransitions = incomingTransitions;
	}
	// getters and setters
	public List<ITransition> getOutgoingTransitions() {
		return outgoingTransitions;
	}
	public ActivityBehavior getActivityBehavior() {
		return activityBehavior;
	}
	public void setActivityBehavior(ActivityBehavior activityBehavior) {
		this.activityBehavior = activityBehavior;
	}
	public List<ITransition> getIncomingTransitions() {
		return incomingTransitions;
	}
	public boolean isScope() {
		return isScope;
	}
	public void setScope(boolean isScope) {
		this.isScope = isScope;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public boolean isAsync() {
		return isAsync;
	}
	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}
	public boolean isExclusive() {
		return isExclusive;
	}
	public void setExclusive(boolean isExclusive) {
		this.isExclusive = isExclusive;
	}
	@Override
	public MsgType[] getMsgType() {
		return new MsgType[] { MsgType.EMAIL, MsgType.MSGCENTER };
	}
	@Override
	public void setListenerBizImplExtend(ListenerBizImplExtend listenerBizImplExtend) {
		this.listenerBizImplExtend = listenerBizImplExtend;
	}
	@Override
	public ListenerBizImplExtend getListenerBizImplExtend() {
		return this.listenerBizImplExtend;
	}

	public boolean isAfterSign() {
		boolean isAfterSign = false;
		ActivityBehavior activityBehavior = this.getActivityBehavior();
		if(activityBehavior instanceof UserTaskActivityBehavior){
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityBehavior).getTaskDefinition();
			if(taskDefinition.isAfterSign()){
				isAfterSign = true;
			}
		}
		return isAfterSign;
	}
	public boolean isSequence() {
		boolean isSequence = false;
		ActivityBehavior activityBehavior = this.getActivityBehavior();
		if(activityBehavior instanceof UserTaskActivityBehavior){
			TaskDefinition taskDefinition = ((UserTaskActivityBehavior)activityBehavior).getTaskDefinition();
			if(taskDefinition.isSequence()){
				isSequence = true;
			}
		}
		return isSequence;
	}
}
