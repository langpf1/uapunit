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
import javax.script.ScriptException;

import uap.workflow.engine.bpmn.helper.ErrorPropagation;
import uap.workflow.engine.context.Context;
import uap.workflow.engine.core.IActivityInstance;
import uap.workflow.engine.delegate.BpmnError;
import uap.workflow.engine.exception.WorkflowException;
import uap.workflow.engine.scripting.ScriptingEngines;
/**
 * activity implementation of the BPMN 2.0 script task.
 * 
 * @author Joram Barrez
 * @author Christian Stettler
 * @author Falko Menge
 */
public class ScriptTaskActivityBehavior extends TaskActivityBehavior {
	protected final String script;
	protected final String language;
	protected final String resultVariable;
	public ScriptTaskActivityBehavior(String script, String language, String resultVariable) {
		this.script = script;
		this.language = language;
		this.resultVariable = resultVariable;
	}
	public void execute(IActivityInstance execution) throws Exception {
		ScriptingEngines scriptingEngines = Context.getProcessEngineConfiguration().getScriptingEngines();
		boolean noErrors = true;
			try {
				if(script!=null&&language!=null){//增加判断条件，因script为空的时候，产生异常 -- zhailzh
					Object result = scriptingEngines.evaluate(script, language, execution);
					if (resultVariable != null) {
						execution.setVariable(resultVariable, result);
					}	
				}
			//if(1==1) throw new WorkflowRuntimeException("");
		} catch (WorkflowException e) {
			noErrors = false;
			if (e.getCause() instanceof ScriptException && e.getCause().getCause() instanceof ScriptException && e.getCause().getCause().getCause() instanceof BpmnError) {
				ErrorPropagation.propagateError((BpmnError) e.getCause().getCause().getCause(), execution);
			} else {
				throw e;
			}
		}
		if (noErrors) {
			leave(execution);
		}
	}
}
