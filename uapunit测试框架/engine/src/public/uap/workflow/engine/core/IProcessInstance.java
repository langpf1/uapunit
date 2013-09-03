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
import java.util.Map;

import uap.workflow.engine.delegate.VariableScope;
/**
 * @author Tom Baeyens
 */
public interface IProcessInstance extends VariableScope {
	String getProInsPk();
	void setStartActiviti(IActivity startActiviti);
	IActivity getStartActiviti();
	IActivityInstance[] getExecutions();
	
	void setParentProcessInstance(IProcessInstance parentProcessInstance);
	IProcessInstance getParentProcessInstance();
	
	void setProcessDefinition(IProcessDefinition processDefinition);
	IProcessDefinition getProcessDefinition();
	
	IActivityInstance getSuperActivityInstance();
	void setSuperActivityInstance(IActivityInstance superActivityInstance);
	
	void start();
	boolean isEnded();
	boolean isSuspended();
	void deleteCascade(String deleteReason);
	void setVariable(String variableName, Object value);
	void setVariables(Map<String, ? extends Object> variables);
	void initialize();
	void asyn();
	void setPk_form_ins(String pk_form_ins);
	void setPk_form_ins_version(String pk_form_ins_version);
	String getPk_form_ins_version();
	String getPk_form_ins();
}
