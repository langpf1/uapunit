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

package uap.workflow.engine.test;

import java.util.HashMap;
import java.util.Map;

import uap.workflow.engine.service.ProcessEngine;
import uap.workflow.engine.service.ProcessEngineConfiguration;


/**
 * @author Tom Baeyens
 */
public class ResourceWorkflowTestCase extends AbstractWorkflowTestCase {
  
  protected static Map<String, ProcessEngine> cachedProcessEngines = new HashMap<String, ProcessEngine>();
  
  protected String activitiConfigurationResource;
  
  public ResourceWorkflowTestCase(String activitiConfigurationResource) {
    this.activitiConfigurationResource = activitiConfigurationResource;
  }

  @Override
  protected void initializeProcessEngine() {
    processEngine = cachedProcessEngines.get(activitiConfigurationResource);
    if (processEngine==null) {
      processEngine = ProcessEngineConfiguration
        .createProcessEngineConfigurationFromResource(activitiConfigurationResource)
        .buildProcessEngine();
      cachedProcessEngines.put(activitiConfigurationResource, processEngine);
    }
  }

}
