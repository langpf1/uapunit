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
package uap.workflow.engine.bpmn.behavior;
import java.util.List;

import uap.workflow.engine.bpmn.parser.SignalEventDefinition;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.entity.SignalEventSubscriptionEntity;
import uap.workflow.engine.interceptor.CommandContext;
import uap.workflow.engine.mgr.EventSubscriptionManager;
/**
 * @author zhailzh
 */
public class IntermediateThrowSignalEventActivityBehavior extends AbstractBpmnActivityBehavior {
	protected final SignalEventDefinition signalDefinition;
	public IntermediateThrowSignalEventActivityBehavior(SignalEventDefinition signalDefinition) {
		this.signalDefinition = signalDefinition;
	}
	public void execute(IActivityInstance execution) throws Exception {
		CommandContext commandContext = Context.getCommandContext();
		EventSubscriptionManager manager = commandContext.getEventSubscriptionManager();
		// ��ѯ�Ѿ�����ĳ���ź������¼�����ѯ��������¼�
		//List<SignalEventSubscriptionEntity> entitys = manager.findSignalEventSubscriptionsByEventName(signalDefinition.getSignalName());
		List<SignalEventSubscriptionEntity> entitys = manager.findSignalEventSubscriptionsByProinsAndEventName(execution.getProcessInstance().getProInsPk(),signalDefinition.getSignalName());
		for (SignalEventSubscriptionEntity signalEventSubscriptionEntity : entitys) {
			signalEventSubscriptionEntity.eventReceived(null, signalDefinition.isAsync());//ͬ��ִ��
		}
		// �뿪��ǰ�ڵ�
		leave(execution);
	}
}
