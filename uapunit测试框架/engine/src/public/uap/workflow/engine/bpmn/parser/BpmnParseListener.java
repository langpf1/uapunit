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

package uap.workflow.engine.bpmn.parser;

import java.util.List;

import uap.workflow.engine.core.IActivity;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IScope;
import uap.workflow.engine.core.ITransition;
import uap.workflow.engine.variable.VariableDeclaration;
import uap.workflow.engine.xml.Element;

/**
 * Listener which can be registered within the engine to receive events during parsing (and
 * maybe influence ist). Instead of implmenting this interface you migh consider to extend 
 * the {@link AbstractBpmnParseListener}, which contains an empty implementation for all methods
 * and makes your implementation easier and more robust to future changes.
 * 
 * @author Tom Baeyens
 * @author Falko Menge
 * @author Joram Barrez
 */
public interface BpmnParseListener {

  void parseProcess(Element processElement, IProcessDefinition processDefinition);
  void parseStartEvent(Element startEventElement, IScope scope, IActivity startEventActivity);
  void parseExclusiveGateway(Element exclusiveGwElement, IScope scope, IActivity activity);
  void parseInclusiveGateway(Element inclusiveGwElement, IScope scope, IActivity activity);
  void parseComplexGateway(Element complexGwElement, IScope scope, IActivity activity);
  void parseParallelGateway(Element parallelGwElement, IScope scope, IActivity activity);
  void parseScriptTask(Element scriptTaskElement, IScope scope, IActivity activity);
  void parseServiceTask(Element serviceTaskElement, IScope scope, IActivity activity);
  void parseBusinessRuleTask(Element businessRuleTaskElement, IScope scope, IActivity activity);
  void parseTask(Element taskElement, IScope scope, IActivity activity);
  void parseManualTask(Element manualTaskElement, IScope scope, IActivity activity);
  void parseUserTask(Element userTaskElement, IScope scope, IActivity activity);
  void parseEndEvent(Element endEventElement, IScope scope, IActivity activity);
  void parseBoundaryTimerEventDefinition(Element timerEventDefinition, boolean interrupting, IActivity timerActivity);
  void parseBoundaryErrorEventDefinition(Element errorEventDefinition, boolean interrupting, IActivity activity, IActivity nestedErrorEventActivity);
  void parseSubProcess(Element subProcessElement, IScope scope, IActivity activity);
  void parseCallActivity(Element callActivityElement, IScope scope, IActivity activity);
  void parseProperty(Element propertyElement, VariableDeclaration variableDeclaration, IActivity activity);
  void parseSequenceFlow(Element sequenceFlowElement, IScope scopeElement, ITransition transition);
  void parseSendTask(Element sendTaskElement, IScope scope, IActivity activity);
  void parseMultiInstanceLoopCharacteristics(Element activityElement, Element multiInstanceLoopCharacteristicsElement, IActivity activity);
  void parseIntermediateTimerEventDefinition(Element timerEventDefinition, IActivity timerActivity);
  void parseRootElement(Element rootElement, List<IProcessDefinition> processDefinitions);
  void parseReceiveTask(Element receiveTaskElement, IScope scope, IActivity activity);
  void parseIntermediateSignalCatchEventDefinition(Element signalEventDefinition, IActivity signalActivity);
  void parseBoundarySignalEventDefinition(Element signalEventDefinition, boolean interrupting, IActivity signalActivity);
  void parseEventBasedGateway(Element eventBasedGwElement, IScope scope, IActivity activity);
  void parseTransaction(Element transactionElement, IScope scope, IActivity activity);
  void parseCompensateEventDefinition(Element compensateEventDefinition, IActivity compensationActivity);
  void parseIntermediateThrowEvent(Element intermediateEventElement, IScope scope, IActivity activity);
  void parseIntermediateCompensateEventDefinition(Element compensateEventDefinition, IActivity compensateActivity);
  void parseIntermediateLinkCatchEventDefinition(Element linkEventDefinition, IActivity linkActivity); 
}
