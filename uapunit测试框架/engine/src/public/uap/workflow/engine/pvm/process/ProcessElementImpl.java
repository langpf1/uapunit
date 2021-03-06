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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import uap.workflow.engine.core.IProcessDefinition;
import uap.workflow.engine.core.IProcessElement;
import uap.workflow.engine.observer.AbstractObservable;
/**
 * common properties for process definition, activity and transition including
 * event listeners.
 * 
 * @author Tom Baeyens
 */
public class ProcessElementImpl extends AbstractObservable implements IProcessElement {
	private static final long serialVersionUID = 1L;
	protected String id;
	protected IProcessDefinition processDefinition;
	protected Map<String, Object> properties;
	public ProcessElementImpl(String id, IProcessDefinition processDefinition) {
		this.id = id;
		this.processDefinition = processDefinition;
	}
	public void setProperty(String name, Object value) {
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		properties.put(name, value);
	}
	public Object getProperty(String name) {
		if (properties == null) {
			return null;
		}
		return properties.get(name);
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> getProperties() {
		if (properties == null) {
			return Collections.EMPTY_MAP;
		}
		return properties;
	}
	// getters and setters
	// //////////////////////////////////////////////////////
	public String getId() {
		return id;
	}
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}
	public IProcessDefinition getProcessDefinition() {
		return processDefinition;
	}
}
