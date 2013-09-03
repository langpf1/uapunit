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

package uap.workflow.engine.persistence;


import uap.workflow.engine.context.Context;
import uap.workflow.engine.db.DbSqlSession;
import uap.workflow.engine.db.PersistentObject;
import uap.workflow.engine.interceptor.Session;
import uap.workflow.engine.mgr.AttachmentManager;
import uap.workflow.engine.mgr.DeploymentManager;
import uap.workflow.engine.mgr.ExecutionManager;
import uap.workflow.engine.mgr.GroupManager;
import uap.workflow.engine.mgr.HistoricActivityInstanceManager;
import uap.workflow.engine.mgr.HistoricDetailManager;
import uap.workflow.engine.mgr.HistoricTaskInstanceManager;
import uap.workflow.engine.mgr.IdentityInfoManager;
import uap.workflow.engine.mgr.IdentityLinkManager;
import uap.workflow.engine.mgr.MembershipManager;
import uap.workflow.engine.mgr.ProcessDefinitionManager;
import uap.workflow.engine.mgr.ProcessInstanceManager;
import uap.workflow.engine.mgr.ResourceManager;
import uap.workflow.engine.mgr.TaskManager;
import uap.workflow.engine.mgr.UserManager;
import uap.workflow.engine.mgr.VariableInstanceManager;


/**
 * @author Tom Baeyens
 */
public abstract class AbstractManager implements Session {
  
  public void insert(PersistentObject persistentObject) {
    getDbSqlSession().insert(persistentObject);
  }

  public void delete(PersistentObject persistentObject) {
    getDbSqlSession().delete(persistentObject.getClass(), persistentObject.getId());
  }

  protected DbSqlSession getDbSqlSession() {
    return getSession(DbSqlSession.class);
  }

  protected <T> T getSession(Class<T> sessionClass) {
    return Context.getCommandContext().getSession(sessionClass);
  }

  protected DeploymentManager getDeploymentManager() {
    return getSession(DeploymentManager.class);
  }

  protected ResourceManager getResourceManager() {
    return getSession(ResourceManager.class);
  }
  
  protected ProcessDefinitionManager getProcessDefinitionManager() {
    return getSession(ProcessDefinitionManager.class);
  }

  protected ExecutionManager getProcessInstanceManager() {
    return getSession(ExecutionManager.class);
  }

  protected TaskManager getTaskManager() {
    return getSession(TaskManager.class);
  }

  protected IdentityLinkManager getIdentityLinkManager() {
    return getSession(IdentityLinkManager.class);
  }

  protected VariableInstanceManager getVariableInstanceManager() {
    return getSession(VariableInstanceManager.class);
  }

  protected ProcessInstanceManager getHistoricProcessInstanceManager() {
    return getSession(ProcessInstanceManager.class);
  }

  protected HistoricDetailManager getHistoricDetailManager() {
    return getSession(HistoricDetailManager.class);
  }

  protected HistoricActivityInstanceManager getHistoricActivityInstanceManager() {
    return getSession(HistoricActivityInstanceManager.class);
  }
  
  protected HistoricTaskInstanceManager getHistoricTaskInstanceManager() {
    return getSession(HistoricTaskInstanceManager.class);
  }
  
  protected UserManager getUserManager() {
    return getSession(UserManager.class);
  }
  
  protected GroupManager getGroupManager() {
    return getSession(GroupManager.class);
  }
  
  protected IdentityInfoManager getIdentityInfoManager() {
    return getSession(IdentityInfoManager.class);
  }
  
  protected MembershipManager getMembershipManager() {
    return getSession(MembershipManager.class);
  }
  
  protected AttachmentManager getAttachmentManager() {
    return getSession(AttachmentManager.class);
  }
  
  public void close() {
  }

  public void flush() {
  }
}
