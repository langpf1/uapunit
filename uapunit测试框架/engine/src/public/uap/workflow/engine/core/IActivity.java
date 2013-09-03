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
import uap.workflow.bizimpl.listener.ListenerBizImplExtend;
import uap.workflow.engine.message.MsgType;
import uap.workflow.engine.pvm.behavior.ActivityBehavior;
import uap.workflow.engine.pvm.process.ScopeImpl;
/**
 * @author Tom Baeyens
 */
public interface IActivity extends IScope {
	boolean isAsync();
	boolean isExclusive();
	IScope getParent();
	IActivity getParentActivity();
	void setParent(ScopeImpl parent);
	boolean isScope();
	void setScope(boolean isScope);
	void setAsync(boolean isAsync);
	void setExclusive(boolean isExclusive);
	List<ITransition> getIncomingTransitions();
	ITransition createOutgoingTransition(String transitionId);
	List<ITransition> getOutgoingTransitions();
	ActivityBehavior getActivityBehavior();
	ITransition findOutgoingTransition(String transitionId);
	void setActivityBehavior(ActivityBehavior activityBehavior);
	int getX();
	void setX(int x);
	int getY();
	void setY(int y);
	int getWidth();
	void setWidth(int width);
	int getHeight();
	void setHeight(int height);
	MsgType[] getMsgType();
	void setListenerBizImplExtend(ListenerBizImplExtend listenerBizImplExtend);
	ListenerBizImplExtend getListenerBizImplExtend();
	boolean isAfterSign();
	boolean isSequence();
}
