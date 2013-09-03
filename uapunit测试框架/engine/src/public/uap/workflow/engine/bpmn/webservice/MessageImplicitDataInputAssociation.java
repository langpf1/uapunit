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
package uap.workflow.engine.bpmn.webservice;


import uap.workflow.engine.bpmn.behavior.WebServiceActivityBehavior;
import uap.workflow.engine.bpmn.data.AbstractDataAssociation;
import uap.workflow.engine.bpmn.data.FieldBaseStructureInstance;
import uap.workflow.engine.core.IActivityInstance;

/**
 * An implicit data input association between a source and a target.
 * source is a variable in the current execution context and target is a property in the message
 * 
 * @author Esteban Robles Luna
 */
public class MessageImplicitDataInputAssociation extends AbstractDataAssociation {

  public MessageImplicitDataInputAssociation(String source, String target) {
    super(source, target);
  }

  @Override
  public void evaluate(IActivityInstance execution) {
    Object value = execution.getVariable(this.source);
    MessageInstance message = (MessageInstance) execution.getVariable(WebServiceActivityBehavior.CURRENT_MESSAGE);
    if (message.getStructureInstance() instanceof FieldBaseStructureInstance) {
      FieldBaseStructureInstance structure = (FieldBaseStructureInstance) message.getStructureInstance();
      structure.setFieldValue(this.target, value);
    }
  }
}